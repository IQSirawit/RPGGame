package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.CharClass.Archer;
import RPGGame.Character;
import RPGGame.Destructible;

public class RangeAdvantageDecorator extends AttackDecorator {
    private final Attack advantageAttack;
    public RangeAdvantageDecorator(Attack normalAttack, Attack advantageAttack) {
        super(normalAttack);
        this.advantageAttack = advantageAttack;
    }
    @Override
    public void attack(Character attacker, Destructible target) {
        if (attacker instanceof Archer archer && archer.hasRangeAdvantage()) {
            System.out.println("🏹 RANGE ADVANTAGE: The Archer has a clear shot!");
            advantageAttack.attack(attacker, target);
        } else {
            wrappedAttack.attack(attacker, target);
        }
    }
}