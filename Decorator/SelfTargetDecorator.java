package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;

public class SelfTargetDecorator extends AttackDecorator {
    public SelfTargetDecorator(Attack wrappedAttack) {
        super(wrappedAttack);
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        wrappedAttack.attack(attacker, attacker);
    }
}