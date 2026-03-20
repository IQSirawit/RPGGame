package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.Character;
import RPGGame.Destructible;

public class AllyTargetDecorator extends AttackDecorator {
    public AllyTargetDecorator(Attack wrappedAttack) {
        super(wrappedAttack);
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        wrappedAttack.attack(attacker, target);
    }
}