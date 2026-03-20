package RPGGame;

import java.util.List;

public class BattleInfo {
    private static List<Character> enemies;
    private static List<Character> allies; // Add this
    public static void setEnemies(List<Character> e) { enemies = e; }
    public static List<Character> getEnemies() { return enemies; }
    public static void setAllies(List<Character> a) { allies = a; } // Add this
    public static List<Character> getAllies() { return allies; }    // Add this
}