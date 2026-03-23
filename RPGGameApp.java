package RPGGame;

import RPGGame.CharClass.*;
import RPGGame.Location.*;
import RPGGame.Item.*;
import java.util.*;

public class RPGGameApp {
    public static void main(String[] args) {
        Weapon sword = new Weapon("Steel Sword", "Melee", 10, "Slash");
        Weapon bow = new Weapon("Recurve Bow", "Ranged", 10, "Pierce");
        Weapon staff = new Weapon("Oak Staff", "Magic", 10, "Spark");
        Weapon mace = new Weapon("Holy Mace", "Magic", 5, "Blunt");

        Warrior warrior = new Warrior("Arthur", 19, 1, 300, 20, 15, 10, 10, sword);
        Archer archer = new Archer("Legolas", 19, 1, 250, 25, 5, 30, 85, bow);
        Mage mage = new Mage("Gandalf", 19, 1, 200, 30, 5, 15, 100, staff);
        Healer healer = new Healer("Mercy", 19, 2, 220, 10, 10, 12, mace);

        List<Character> playerParty = new ArrayList<>(Arrays.asList(warrior, archer, mage, healer));

        // ✨ สร้าง Party ใหม่ มีเงินเริ่มต้น 100 Gold
        Party myParty = new Party(playerParty, 100);
        List<Character> enemyList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            char label = (char) ('A' + i);
            Warrior dummy = new Warrior("Dummy " + label, 1, 1, 1, 1, 0, 1, 0, sword);
            dummy.setXpReward(1500);
            dummy.setGoldReward(100);
            enemyList.add(dummy);
        }
        Party enemyParty = new Party(enemyList);
        System.out.println("=== SYSTEM TEST: EVOLUTION & HEALING ===");
        System.out.println("Scenario: Heroes are Level 19. Defeating 1HP dummies will trigger Level 20.");
        BattleManager bm = new BattleManager();
        bm.runBattle(myParty, enemyParty);

        System.out.println("\n=== POST-BATTLE STATS ===");
        for (Character hero : playerParty) {
            hero.displayCharacterDetails();
        }

        Location townInn = new Inn("The Prancing Pony Inn", "A warm place to rest your tired bones.", 20);
        Location weaponShop = new Shop("Garnet's Goods", "We have everything an adventurer needs!");
        Location darkForest = new Forest("Whispering Woods", "A dark and gloomy forest full of monsters.");
        Location creepyDungeon = new Dungeon("Catacombs of Despair", "A multi-level dungeon with terrifying creatures and obstacles.");

        Scanner mapScanner = new Scanner(System.in);
        boolean isPlaying = true;

        while(isPlaying && playerParty.stream().anyMatch(Character::isAlive)) {
            System.out.println("\n🗺️ === WORLD MAP ===");
            System.out.println("1. Enter " + townInn.getName());
            System.out.println("2. Enter " + weaponShop.getName());
            System.out.println("3. Enter " + darkForest.getName());
            System.out.println("4. Enter " + creepyDungeon.getName());
            System.out.println("0. Quit Game");
            System.out.print("Where do you want to go?: ");

            int choice = InputHandler.getValidChoice(0, 4);
            switch (choice) {
                case 1 -> townInn.enter(myParty); // ทำงานแบบ Inn
                case 2 -> weaponShop.enter(myParty); // ทำงานแบบ Shop
                case 3 -> darkForest.enter(myParty); // ทำงานแบบ Forest (เข้าต่อสู้)
                case 4 -> creepyDungeon.enter(myParty);
                case 0 -> isPlaying = false;
                default -> System.out.println("Invalid destination!");
            }
        }
    }
}