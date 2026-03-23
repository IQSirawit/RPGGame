package RPGGame.Location;

import RPGGame.Character;
import RPGGame.CharClass.Warrior;
import RPGGame.Party;
import RPGGame.Weapon;
import RPGGame.BattleManager;
import java.util.ArrayList;
import java.util.List;

public class Forest extends Location {

    public Forest(String name, String description) {
        super(name, description);
    }

    @Override
    public void enter(Party party) {
        System.out.println("\n🌲 === " + this.getName() + " ===");
        System.out.println("⚠️ Beware! Wild enemies are approaching!");

        List<RPGGame.Character> enemyList = generateEnemies();
        Party enemyParty = new Party(enemyList);
        BattleManager bm = new BattleManager();
        bm.runBattle(party, enemyParty);
    }

    private List<Character> generateEnemies() {
        List<Character> enemies = new ArrayList<>();
        Weapon claws = new Weapon("Monster Claws", "Melee", 10, "Slash");

        // ตัวอย่างการสร้างศัตรู (อาจจะสุ่มจำนวน หรือชนิดได้)
        Warrior goblin = new Warrior("Goblin", 5, 1, 100, 15, 5, 15, 2, claws);
        goblin.setXpReward(50);
        goblin.setGoldReward(20);
        enemies.add(goblin);

        return enemies;
    }
}