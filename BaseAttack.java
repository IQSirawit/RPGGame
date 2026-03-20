package RPGGame;

public class BaseAttack implements Attack {

    @Override
    public void attack(Character attacker, Destructible target) {
        if (!attacker.isAlive()) {
            System.out.println(attacker.getName() + " cannot attack because they have fainted.");
            return;
        }
        int totalDamage = attacker.getDamage() + attacker.getWeapon().getBaseDamage();
        System.out.println();
        System.out.println(attacker.getName() + " Attack " + target.getDisplayName() + " with " + attacker.getWeapon().getName() + "!");
        System.out.println("⚔️ Raw Attack Damage: " + totalDamage);
        boolean isCrit = (Math.random() * 100) < attacker.getCritChance();
        if (isCrit) {
            totalDamage *= 2;
            System.out.println("💥 CRITICAL HIT! " + attacker.getName() + " deals massive damage!");
        }
        target.takeDamage(totalDamage);
    }
}
