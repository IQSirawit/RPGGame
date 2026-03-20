package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;

public class LifeStealDecorator extends AttackDecorator {
    private final int percentage;
    public LifeStealDecorator(Attack wrappedAttack, int percentage) {
        super(wrappedAttack);
        if (percentage < 1 || percentage > 100) {
            throw new IllegalArgumentException("Life steal percentage must be between 1 and 100.");
        }
        this.percentage = percentage;
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        wrappedAttack.attack(attacker, target);
        if (!attacker.isAlive()) return;
        double ratio = percentage / 100.0;
        int healBase = attacker.getDamage();
        int healAmount = (int) Math.round(healBase * ratio);
        int hpBefore = attacker.getHp();
        int missingHP = attacker.getMaxHP() - attacker.getHp();
        if (missingHP <= 0) {
            return;
        }
        int restored = Math.min(healAmount, missingHP);
        attacker.setHp(attacker.getHp() + restored);
        System.out.println("-🩸 [Life Steal " + percentage + "%] Restored " + restored + " HP to " + attacker.getName() + " (" + hpBefore + " -> " + attacker.getHp() + ")");
    }
}