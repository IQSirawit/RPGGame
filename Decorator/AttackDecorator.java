package RPGGame.Decorator;

import RPGGame.Attack;

public abstract class AttackDecorator implements Attack {

    protected final Attack wrappedAttack;

    protected AttackDecorator(Attack wrappedAttack) {
        if (wrappedAttack == null) {
            throw new IllegalArgumentException("Wrapped attack cannot be null.");
        }
        this.wrappedAttack = wrappedAttack;
    }

    public Attack getWrappedAttack() {
        return wrappedAttack;
    }
}
