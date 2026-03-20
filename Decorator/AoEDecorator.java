package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.BattleInfo;
import RPGGame.Character;
import RPGGame.Destructible;
import java.util.List;

public class AoEDecorator extends AttackDecorator {
    public AoEDecorator(Attack wrappedAttack) {
        super(wrappedAttack);
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        List<Character> targets;
        if (BattleInfo.getAllies().contains(target)) {
            targets = BattleInfo.getAllies();
        } else {
            targets = BattleInfo.getEnemies();
        }
        System.out.println("💥 AOE EFFECT: " + attacker.getName() + " affects the whole group!");
        for (Character t : targets) {
            if (t.isAlive()) {
                wrappedAttack.attack(attacker, t);
            }
        }
    }
}