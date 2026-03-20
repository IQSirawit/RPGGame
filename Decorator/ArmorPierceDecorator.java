package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;
import RPGGame.CharClass.Warrior;

public class ArmorPierceDecorator extends AttackDecorator {
    private int piercePercentage;
    public ArmorPierceDecorator(Attack wrappedAttack, int piercePercentage) {
        super(wrappedAttack);
        if (piercePercentage < 0 || piercePercentage > 100) {
            throw new IllegalArgumentException("Pierce percentage must be between 0 and 100.");
        }
        this.piercePercentage = piercePercentage;
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        if (!(target instanceof Character enemy)) {
            wrappedAttack.attack(attacker, target);
            return;
        }
        int originalDefense = enemy.getDefense();
        int originalArmor = 0;
        boolean isWarrior = (enemy instanceof Warrior);
        double multiplier = piercePercentage / 100.0;
        int reducedDefense = (int) (originalDefense * (1 - multiplier));
        enemy.setDefense(reducedDefense);
        if (isWarrior) {
            Warrior warriorVictim = (Warrior) enemy;
            originalArmor = warriorVictim.getArmorValue();
            warriorVictim.setArmorValue(0);
            System.out.println("🎯 ARMOR PIERCE: Bypassing " + warriorVictim.getName() + "'s heavy armor!");
        }
        System.out.println("🎯 PIERCING: Ignoring " + (int)piercePercentage + "% of defense (" + originalDefense + " -> " + reducedDefense + ")");
        try {
            wrappedAttack.attack(attacker, target);
        } finally {
            enemy.setDefense(originalDefense);
            if (isWarrior) {
                ((Warrior) enemy).setArmorValue(originalArmor);
            }
        }
    }
}