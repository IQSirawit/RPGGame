package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;

public class HealDecorator extends AttackDecorator {
    private final double healMultiplier;

    public HealDecorator(Attack wrappedAttack, double healMultiplier) {
        super(wrappedAttack);
        this.healMultiplier = healMultiplier;
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        int healAmount = (int)(attacker.getDamage() * healMultiplier);
        if (target instanceof Character t) {
            int hpBefore = t.getHp();
            t.setHp(Math.min(t.getMaxHP(), t.getHp() + healAmount));
            int actualHealed = t.getHp() - hpBefore;
            System.out.println(" 💚 " + t.getName() + " was healed for " + actualHealed + " HP!");
        }
    }
}