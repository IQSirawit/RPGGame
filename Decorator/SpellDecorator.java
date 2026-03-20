package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.CharClass.Mage;
import RPGGame.Character;
import RPGGame.Destructible;

public class SpellDecorator extends AttackDecorator {
    private final String spellName;
    private final int manaCost;

    public SpellDecorator(Attack wrappedAttack, String spellName, int manaCost) {
        super(wrappedAttack);
        this.spellName = spellName;
        this.manaCost = manaCost;
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        if (!(attacker instanceof Mage mage)) {
            wrappedAttack.attack(attacker, target);
            return;
        }
        if (mage.getMana() < manaCost) {
            System.out.println("❌ " + mage.getName() + " tries to cast " + spellName +
                    " but is out of mana! (Needs: " + manaCost + ")");
            return; // Chain stops here
        }
        mage.setMana(mage.getMana() - manaCost);
        System.out.println("✨ " + mage.getName() + " spent " + manaCost + " mana to cast " + spellName + ".");

        wrappedAttack.attack(attacker, target);
    }
}