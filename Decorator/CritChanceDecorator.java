package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;

public class CritChanceDecorator extends AttackDecorator {
    private final int bonusCrit;
    public CritChanceDecorator(Attack wrappedAttack, int bonusCrit) {
        super(wrappedAttack);
        this.bonusCrit = bonusCrit;
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        int originalCrit = attacker.getCritChance();
        attacker.setCritChance(originalCrit + bonusCrit);
        try {
            System.out.println("🎯 Focused! Crit chance increased by " + bonusCrit + "%");
            wrappedAttack.attack(attacker, target);
        } finally {
            attacker.setCritChance(originalCrit);
        }
    }
}