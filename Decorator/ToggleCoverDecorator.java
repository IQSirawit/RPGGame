package RPGGame.Decorator;

import RPGGame.Attack;
import RPGGame.CharClass.Archer;
import RPGGame.Character;
import RPGGame.Destructible;

public class ToggleCoverDecorator extends AttackDecorator {
    public ToggleCoverDecorator(Attack wrappedAttack) {
        super(wrappedAttack);
    }

    @Override
    public void attack(Character attacker, Destructible target) {
        if (attacker instanceof Archer archer) {
            if (!archer.hasRangeAdvantage()) {
                archer.setRangeAdvantage(true);
                archer.setSpeed(archer.getSpeed()*0.7);
                archer.setDefense(archer.getDefense()+10);
                archer.setStatus("Active [In Cover]");
                System.out.println("🛡️ " + archer.getName() + " takes cover! [Range Advantage: ON] [Speed: " + archer.getSpeed() + "]");
            } else {
                archer.setRangeAdvantage(false);
                archer.setSpeed(archer.getSpeed()/0.7);
                archer.setDefense(archer.getDefense()-10);
                archer.setStatus("Active");
                System.out.println("🏃 " + archer.getName() + " leaves cover! [Range Advantage: OFF] [Speed: " + archer.getSpeed() + "]");
            }
        }
        wrappedAttack.attack(attacker, target);
    }
}