package RPGGame.Save;

import java.io.*;

import RPGGame.Character;
import RPGGame.Item.*; // นำเข้า Item เพื่อให้สร้าง Object ได้
import RPGGame.Party;

public class SaveManager {
    private static final String SAVE_DIR = "Save";
    private static final String SAVE_FILE = SAVE_DIR + "/savegame.txt";

    public static void saveGame(Party party) {
        File directory = new File(SAVE_DIR);
        if (!directory.exists()) directory.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            writer.println("GOLD," + party.getGold());

            for (RPGGame.Character hero : party.getMembers()) {
                writer.println("CHAR," + hero.getName() + "," + hero.getLevel() + "," + hero.getHp() + "," + hero.getMaxHP() + "," + hero.getXp());
            }

            // ✨ 1. เพิ่มการวนลูปเซฟไอเทมในกระเป๋า
            for (Item item : party.getInventory().getItems()) {
                if (item instanceof Stackable stackItem) {
                    writer.println("ITEM," + item.getName() + "," + stackItem.getQuantity());
                } else {
                    // ถ้าไม่ใช่ Stackable (เช่น Dragon Orb) ให้ถือว่ามีจำนวน 1
                    writer.println("ITEM," + item.getName() + ",1");
                }
            }

            System.out.println("💾 Game saved successfully to " + SAVE_FILE + "!");
        } catch (IOException e) {
            System.out.println("❌ Failed to save game: " + e.getMessage());
        }
    }

    public static boolean loadGame(Party party) { // เปลี่ยนเป็นคืนค่า boolean เพื่อบอกว่าโหลดสำเร็จไหม
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("❌ No save file found in " + SAVE_DIR + " directory!");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // ✨ 2. เคลียร์กระเป๋าเดิมทิ้งก่อนโหลดของใหม่ เพื่อไม่ให้ไอเทมเบิ้ล
            party.getInventory().getItems().clear();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals("GOLD")) {
                    int savedGold = Integer.parseInt(data[1]);
                    int currentGold = party.getGold();
                    if(savedGold > currentGold) party.addGold(savedGold - currentGold);
                    else party.spendGold(currentGold - savedGold);
                }
                else if (data[0].equals("CHAR")) { // โหลดตัวละคร
                    String name = data[1];
                    int level = Integer.parseInt(data[2]);
                    int hp = Integer.parseInt(data[3]);
                    int maxHp = Integer.parseInt(data[4]);
                    int xp = Integer.parseInt(data[5]);

                    for (Character hero : party.getMembers()) {
                        if (hero.getName().equals(name)) {
                            hero.setLevel(level);
                            hero.setMaxHP(maxHp);
                            hero.setHp(hp);
                            hero.setXp(xp);
                            if(hp > 0) {
                                hero.setAlive(true);
                                hero.setStatus("Active");
                            } else {
                                hero.setAlive(false);
                                hero.setStatus("Fainted");
                            }
                        }
                    }
                }
                // ✨ 3. โหลดไอเทม
                else if (data[0].equals("ITEM")) {
                    String itemName = data[1];
                    int quantity = Integer.parseInt(data[2]);

                    // สร้าง Object ไอเทมตามชื่อที่เซฟไว้
                    switch (itemName) {
                        case "Health Potion" -> party.getInventory().addItem(new HealthPotion("Health Potion", "Heals 50 HP", 50, 50, quantity));
                        case "Mana Potion" -> party.getInventory().addItem(new ManaPotion("Mana Potion", "Heals mana 100 MP", 20, 100, quantity));
                        case "Dragon Orb" -> party.getInventory().addItem(new DragonOrb());
                    }
                }
            }
            System.out.println("📂 Game loaded successfully from " + SAVE_FILE + "!");
            return true;
        } catch (IOException | NumberFormatException e) {
            System.out.println("❌ Failed to load game. File might be corrupted: " + e.getMessage());
            return false;
        }
    }
}