package RPGGame;

import RPGGame.CharClass.*;
import RPGGame.Location.*;
import RPGGame.Item.*;
import java.util.*;

public class RPGGameApp {
    public static void main(String[] args) {
        // --- 1. การเตรียมความพร้อมของเกม (Initialization) ---
        Weapon sword = new Weapon("Steel Sword", "Melee", 10, "Slash");
        Weapon bow = new Weapon("Recurve Bow", "Ranged", 10, "Pierce");
        Weapon staff = new Weapon("Oak Staff", "Magic", 10, "Spark");
        Weapon mace = new Weapon("Holy Mace", "Magic", 5, "Blunt");

        Warrior warrior = new Warrior("Arthur", 19, 1, 300, 20, 15, 10, 10, sword);
        Archer archer = new Archer("Legolas", 19, 1, 250, 25, 5, 30, 85, bow);
        Mage mage = new Mage("Gandalf", 19, 1, 200, 30, 5, 15, 100, staff);
        Healer healer = new Healer("Mercy", 19, 1, 220, 10, 10, 12, mace);

        List<Character> playerParty = new ArrayList<>(Arrays.asList(warrior, archer, mage, healer));
        Party myParty = new Party(playerParty, 100); // เริ่มต้นด้วย 100 Gold

        // สร้างสถานที่ต่างๆ (เพิ่ม Dragon Cave เข้ามาด้วย)
        Location townInn = new Inn("The Prancing Pony Inn", "A warm place to rest your tired bones.", 20);
        Location weaponShop = new Shop("Garnet's Goods", "We have everything an adventurer needs!");
        Location darkForest = new Forest("Whispering Woods", "A dark and gloomy forest full of monsters.");
        Location creepyDungeon = new Dungeon("Catacombs of Despair", "A multi-level dungeon with terrifying creatures and obstacles.");
        Location dragonCave = new DragonCave("Dragon Cave", "A scorching hot cave where the legendary Fire Dragon slumbers.");

        // --- 2. หน้าจอเริ่มเกม (Start Screen & Story) ---
        printIntro();

        // --- 3. ระบบลูปหลักของเกม (Main Menu / Camp) ---
        boolean isPlaying = true;

        while(isPlaying && myParty.isAlive()) {
            System.out.println("\n🔥 === CAMP (MAIN MENU) === 🔥");
            System.out.println("What would you like to do?");
            System.out.println("1. 🗺️ Explore the World (Travel)");
            System.out.println("2. 📋 Check Party Status");
            System.out.println("3. 🎒 Open Inventory");
            System.out.println("0. ❌ Quit Game");
            System.out.print("Select an action: ");

            int choice = InputHandler.getValidChoice(0, 3);
            switch (choice) {
                case 1 -> exploreMenu(myParty, townInn, weaponShop, darkForest, creepyDungeon, dragonCave);
                case 2 -> showPartyStatus(myParty);
                case 3 -> openInventoryMenu(myParty);
                case 0 -> {
                    System.out.println("Quitting game... Goodbye hero!");
                    isPlaying = false;
                }
            }
        }

        if (!myParty.isAlive()) {
            System.out.println("\n💀 Your journey ends here. The party has fallen...");
        }
    }

    // ==========================================
    // HELPER METHODS (ฟังก์ชันเสริมเพื่อจัดระเบียบโค้ด)
    // ==========================================

    private static void printIntro() {
        System.out.println("=========================================================");
        System.out.println("          🐲 LEGEND OF THE DRAGON ORB 🐲                 ");
        System.out.println("=========================================================");
        System.out.println(" The realm of Topgard is engulfed in darkness.");
        System.out.println(" A terrifying Fire Dragon has awakened from its slumber,");
        System.out.println(" threatening to turn the world into ash. You lead a");
        System.out.println(" party of brave heroes on a quest to find the mythical");
        System.out.println(" 'Dragon Orb' — the only artifact capable of piercing");
        System.out.println(" the dragon's magical barrier. Prepare yourselves...");
        System.out.println("=========================================================\n");
        System.out.println("Press ENTER to start your adventure...");
        InputHandler.getStringInput(); // รอให้ผู้เล่นกด Enter
    }

    private static void exploreMenu(Party party, Location inn, Location shop, Location forest, Location dungeon, Location cave) {
        boolean exploring = true;
        while (exploring && party.isAlive()) {
            System.out.println("\n🗺️ === WORLD MAP === 🗺️");
            System.out.println("1. 🏠 Enter " + inn.getName());
            System.out.println("2. 🛒 Enter " + shop.getName());
            System.out.println("3. 🌲 Enter " + forest.getName());
            System.out.println("4. 🏰 Enter " + dungeon.getName());
            System.out.println("5. 🌋 Enter " + cave.getName() + " (Boss)");
            System.out.println("6. 🔍 Inspect a Location (View Description)");
            System.out.println("0. 🔙 Back to Camp");
            System.out.print("Where do you want to go?: ");

            int choice = InputHandler.getValidChoice(0, 6);
            switch (choice) {
                case 1 -> inn.enter(party);
                case 2 -> shop.enter(party);
                case 3 -> forest.enter(party);
                case 4 -> dungeon.enter(party);
                case 5 -> cave.enter(party);
                case 6 -> inspectLocationMenu(inn, shop, forest, dungeon, cave);
                case 0 -> exploring = false;
            }
        }
    }

    private static void showPartyStatus(Party party) {
        System.out.println("\n📋 === PARTY STATUS ===");
        System.out.println("💰 Total Gold: " + party.getGold() + "G");
        for (Character hero : party.getMembers()) {
            hero.displayCharacterDetails();
        }
        System.out.println("\nPress ENTER to continue...");
        InputHandler.getStringInput();
    }

    private static void openInventoryMenu(Party party) {
        boolean viewing = true;
        while(viewing && party.isAlive()) {
            Inventory inv = party.getInventory();
            List<Item> items = inv.getItems();

            System.out.println("\n🎒 === PARTY INVENTORY ===");
            System.out.println("💰 Gold: " + party.getGold() + "G");
            if (items.isEmpty()) {
                System.out.println(" (Your inventory is completely empty.)");
                System.out.println("0. 🔙 Back");
                System.out.print("Select: ");
                InputHandler.getValidChoice(0, 0);
                return;
            }

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.print(" " + (i + 1) + ". " + item.getName());
                if (item instanceof Stackable stackItem) {
                    System.out.print(" (x" + stackItem.getQuantity() + ")");
                }
                System.out.println();
            }
            System.out.println(" 0. 🔙 Back");
            System.out.print("Select an item to view details or use: ");

            int choice = InputHandler.getValidChoice(0, items.size());
            if (choice == 0) {
                viewing = false;
            } else {
                Item selected = items.get(choice - 1);
                handleItemAction(party, selected);
                inv.cleanUpEmptyItems(); // เคลียร์ขวดยาที่เหลือ 0
            }
        }
    }

    private static void handleItemAction(Party party, Item item) {
        System.out.println("\n🔍 === ITEM DETAILS ===");
        item.displayDetails(); // เรียกดูรายละเอียดที่เขียนไว้ในคลาสลูกแต่ละคลาส

        // เช็คว่าไอเทมนี้เป็นประเภทกดใช้ได้หรือไม่
        if (item instanceof Consumable consumable) {
            System.out.println("\nDo you want to use this item?");
            System.out.println("1. ✅ Use Item");
            System.out.println("0. ❌ Cancel");
            System.out.print("Select: ");
            int action = InputHandler.getValidChoice(0, 1);
            if (action == 1) {
                useItemOnPartyMember(party, consumable, item.getName());
            }
        } else {
            System.out.println("\n(This item cannot be consumed.)");
            System.out.println("0. 🔙 Back");
            System.out.print("Select: ");
            InputHandler.getValidChoice(0, 0);
        }
    }

    private static void useItemOnPartyMember(Party party, Consumable consumable, String itemName) {
        System.out.println("\nSelect target for " + itemName + ":");
        List<Character> allies = party.getMembers();
        for (int i = 0; i < allies.size(); i++) {
            Character ally = allies.get(i);
            String status = ally.isAlive() ? "(HP: " + ally.getHp() + "/" + ally.getMaxHP() + ")" : "[FAINTED 💀]";
            System.out.println((i + 1) + ". " + ally.getName() + " " + status);
        }
        System.out.println("0. ❌ Cancel");
        System.out.print("Select target: ");

        int targetChoice = InputHandler.getValidChoice(0, allies.size());
        if (targetChoice == 0) return;

        Character target = allies.get(targetChoice - 1);

        // เช็คเงื่อนไขก่อนใช้ไอเทมนอกการต่อสู้
        if (!target.isAlive()) {
            System.out.println("❌ " + target.getName() + " is fainted! Items have no effect.");
            return;
        }
        if (!consumable.canUse(target)) {
            System.out.println("❌ " + target.getName() + " cannot use this item!");
            return;
        }

        // กดใช้งานไอเทม
        consumable.use(target);
        System.out.println("\nPress ENTER to continue...");
        InputHandler.getStringInput();
    }

    // ✨ เมธอดใหม่สำหรับแสดงคำอธิบายสถานที่
    private static void inspectLocationMenu(Location inn, Location shop, Location forest, Location dungeon, Location cave) {
        System.out.println("\n🔍 Which location do you want to inspect?");
        System.out.println("1. " + inn.getName());
        System.out.println("2. " + shop.getName());
        System.out.println("3. " + forest.getName());
        System.out.println("4. " + dungeon.getName());
        System.out.println("5. " + cave.getName());
        System.out.println("0. ❌ Cancel");
        System.out.print("Select a location to view details: ");

        int choice = InputHandler.getValidChoice(0, 5);
        Location selected = null;

        switch (choice) {
            case 1 -> selected = inn;
            case 2 -> selected = shop;
            case 3 -> selected = forest;
            case 4 -> selected = dungeon;
            case 5 -> selected = cave;
            case 0 -> { return; } // ยกเลิกและกลับไปหน้า Map
        }

        // ดึงข้อมูลจาก .getDescription() ของ Location นั้นๆ มาแสดง
        if (selected != null) {
            System.out.println("\n📖 [" + selected.getName() + "] - " + selected.getDescription());
            System.out.println("Press ENTER to continue...");
            InputHandler.getStringInput(); // รอให้ผู้เล่นอ่านจบและกด Enter
        }
    }
}