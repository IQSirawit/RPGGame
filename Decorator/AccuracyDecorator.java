package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;

public class AccuracyDecorator extends AttackDecorator {
    private final String actionName;
    private final int accuracyChance;

    public AccuracyDecorator(Attack wrappedAttack, String actionName, int accuracyChance) {
        super(wrappedAttack);
        this.actionName = actionName;
        this.accuracyChance = accuracyChance;
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        System.out.println("-🎯 " + attacker.getName() + " is aiming " + actionName + "... [Chance to Hit: " + accuracyChance + "%]");
        boolean isHit = (Math.random() * 100) <= accuracyChance;
        if (!isHit) {
            System.out.println("-💨 MISS! The attack failed to connect.");
            return;
        }
        System.out.println("-✅ HIT!");
        wrappedAttack.attack(attacker, target);
    }
}