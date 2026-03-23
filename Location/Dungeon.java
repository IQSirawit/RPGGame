package RPGGame.Location;

import RPGGame.Character;
import RPGGame.CharClass.Warrior;
import RPGGame.Party;
import RPGGame.Weapon;
import RPGGame.BattleManager;
import RPGGame.InputHandler;
import RPGGame.Destructible;
import RPGGame.DestructibleObject.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dungeon extends Location {
    private Random random = new Random();

    public Dungeon(String name, String description) {
        super(name, description);
    }

    @Override
    public void enter(Party party) {
        System.out.println("\n🏰 === " + this.getName() + " ===");
        System.out.println(this.getDescription());

        // สุ่มจำนวน Wave 3 ถึง 5 รอบ
        int waves = random.nextInt(3) + 3;
        System.out.println("You sense " + waves + " waves of enemies ahead...");

        // จดจำเงินก่อนเข้าดันเจี้ยนเพื่อใช้สรุปตอนจบ
        int initialGold = party.getGold();

        for (int i = 1; i <= waves; i++) {
            if (!party.isAlive()) {
                break;
            }

            System.out.println("\n🌊 --- WAVE " + i + " / " + waves + " ---");

            if (random.nextDouble() < 0.4) {
                handleObstacleEvent(party);
            }

            List<RPGGame.Character> enemyList = generateEnemies(i);
            Party enemyParty = new Party(enemyList, 0);
            BattleManager bm = new BattleManager();
            bm.runBattle(party, enemyParty);
        }

        // --- สรุปผลหลังจบดันเจี้ยน ---
        if (party.isAlive()) {
            int earnedGold = party.getGold() - initialGold;
            System.out.println("\n🎉 === DUNGEON CLEARED! === 🎉");
            System.out.println("You survived all " + waves + " waves in the " + this.getName() + "!");
            System.out.println("💰 Total Gold Earned: " + earnedGold + "G");
            System.out.println("✨ XP is distributed among the survivors.");
        } else {
            System.out.println("\n💀 === DUNGEON FAILED === 💀");
            System.out.println("Your party was defeated in the dungeon...");
        }
    }

    private void handleObstacleEvent(Party party) {
        int type = random.nextInt(3);
        Destructible obstacle = null;

        // สุ่มสิ่งกีดขวาง
        switch (type) {
            case 0 -> obstacle = new WoodenBox("Old Chest", 150, true, "100 Gold", 100);
            case 1 -> obstacle = new StoneWall("Dungeon Wall", 250, "Cobblestone");
            case 2 -> obstacle = new CastleGate("Iron Gate", 300, true, true);
        }

        System.out.println("\n⚠️ A " + obstacle.getDisplayName() + " blocks your path!");

        // ลูปให้ผู้เล่นเลือกตัวละครมาตีสิ่งกีดขวางจนกว่าจะแตก
        while (!obstacle.isDestroyed()) {
            System.out.println("\n🛡️ " + obstacle.getDisplayName() + " HP: " + obstacle.getHp() + "/" + obstacle.getMaxHP());
            System.out.println("Select a character to attack the obstacle:");

            List<Character> aliveMembers = party.getMembers().stream().filter(Character::isAlive).toList();
            for (int i = 0; i < aliveMembers.size(); i++) {
                System.out.println((i + 1) + ". " + aliveMembers.get(i).getName() + " (DMG: " + aliveMembers.get(i).getDamage() + ")");
            }

            System.out.print("Selection: ");
            int choice = InputHandler.getValidChoice(1, aliveMembers.size()) - 1;
            Character attacker = aliveMembers.get(choice);

            // โจมตีสิ่งกีดขวาง
            attacker.attack(obstacle);

            // ถ้าเป็นกล่องไม้แล้วตีแตก ให้ของรางวัลพิเศษ
            if (obstacle instanceof WoodenBox && obstacle.isDestroyed()) {
                ((WoodenBox) obstacle).breakOpen(party);
            }
        }
        System.out.println("✅ The path is clear! Proceeding forward...");
    }

    private List<Character> generateEnemies(int wave) {
        List<Character> enemies = new ArrayList<>();
        Weapon claws = new Weapon("Monster Claws", "Melee", 10 + (wave * 2), "Slash");

        // จำนวนศัตรูจะเพิ่มขึ้นตาม Wave (อย่างน้อย 1 ตัว)
        int numEnemies = random.nextInt(2) + 1 + (wave / 2);

        for (int i = 0; i < numEnemies; i++) {
            Warrior monster = new Warrior("Dungeon Skeleton " + (char)('A' + i), 5 + wave, 1, 80 + (wave * 20), 10 + (wave * 5), 5 + wave, 10, 2, claws);
            monster.setXpReward(60 + (wave * 20));
            monster.setGoldReward(30 + (wave * 10));
            enemies.add(monster);
        }

        return enemies;
    }
}