package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;

public class ScalingDamageDecorator extends AttackDecorator {
    private final double multiplier;
    private final int flatBonus;
    private final String skillName;

    public ScalingDamageDecorator(Attack wrappedAttack, String skillName, double multiplier, int flatBonus) {
        super(wrappedAttack);
        this.skillName = skillName;
        this.multiplier = multiplier;
        this.flatBonus = flatBonus;
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        int originalDamage = attacker.getDamage();
        int enhancedDamage = (int) Math.round(originalDamage * multiplier) + flatBonus;
        attacker.setDamage(enhancedDamage);
        if (multiplier != 1.0) System.out.println("-⚡ Multiplier: " + multiplier + "x");
        if (flatBonus > 0) System.out.println("-➕ Bonus Damage: +" + flatBonus);
        try {
            wrappedAttack.attack(attacker, target);
        } finally {
            attacker.setDamage(originalDamage);
        }
    }
}
