package RPGGame;

import RPGGame.CharClass.*;
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
        Healer healer = new Healer("Mercy", 19, 1, 220, 10, 10, 12, mace);

        List<Character> playerParty = new ArrayList<>(Arrays.asList(warrior, archer, mage, healer));

        List<Character> enemyParty = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            char label = (char) ('A' + i);
            Warrior dummy = new Warrior("Dummy " + label, 1, 1, 1, 1, 0, 1, 0, sword);
            dummy.setXpReward(1500);
            dummy.setAuto(true);
            enemyParty.add(dummy);
        }

        System.out.println("=== SYSTEM TEST: EVOLUTION & HEALING ===");
        System.out.println("Scenario: Heroes are Level 19. Defeating 1HP dummies will trigger Level 20.");
        BattleManager bm = new BattleManager();
        bm.runBattle(playerParty, enemyParty);

        System.out.println("\n=== POST-BATTLE STATS ===");
        for (Character hero : playerParty) {
            hero.displayCharacterDetails();
        }
    }
}