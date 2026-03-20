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
        if (target instanceof Character victim) {
            int healAmount = (int) (attacker.getDamage() * healMultiplier);
            int oldHp = victim.getHp();
            victim.setHp(oldHp + healAmount);
            if (victim.getHp() > victim.getMaxHP()) {
                victim.setHp(victim.getMaxHP());
            }
            int actualHealed = victim.getHp() - oldHp;
            System.out.println("💚 " + victim.getName() + " was healed for " + actualHealed + " HP!");
        }
        wrappedAttack.attack(attacker, target);
    }
}