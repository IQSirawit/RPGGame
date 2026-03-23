package RPGGame.CharClass;

import RPGGame.Attack;
import RPGGame.BaseAttack;
import RPGGame.Character;
import RPGGame.Decorator.*;
import RPGGame.Weapon;

public class Archer extends Character {
    private int Accuracy;
    private boolean isRangeAdvantage;

    public Archer(String name, int level, int classTier, int maxHP, int damage, int defense, int speed, int accuracy, Weapon weapon) {
        super(name, level, classTier, maxHP, damage, defense, speed, weapon, "Archer");
        Accuracy = accuracy;
        this.isRangeAdvantage = false;
        this.attack = new RangeAdvantageDecorator(
                new BaseAttack(),
                new ScalingDamageDecorator(new BaseAttack(), 1.2, 0)
        );
        addBaseSkills();
        if (this.classTier >= 2) {
            applyTier2Upgrades(false);
        }
    }

    private void addBaseSkills() {
        this.addSkill("Take Cover", new SelfTargetDecorator(new ToggleCoverDecorator(new BaseAttack())), 0);
        this.addSkill("Charged Shot", new ScalingDamageDecorator(new AccuracyDecorator(new BaseAttack(), this.getAccuracy()), 2.5, 10), 2);
    }

    private void applyTier2Upgrades(boolean showMessage) {
        this.charClass = "Hunter";
        this.addSkill("Precise Shot", new ScalingDamageDecorator(new AccuracyDecorator(new CritChanceDecorator(new BaseAttack(), 15), 100), 1.4, 5), 1);
        Attack normalSnipe = new ScalingDamageDecorator(new AccuracyDecorator(new ArmorPierceDecorator(new BaseAttack(), 20), this.getAccuracy()+10), 1.65, 10);
        Attack advantageSnipe = new ScalingDamageDecorator(new AccuracyDecorator(new CritChanceDecorator(new ArmorPierceDecorator(new BaseAttack(), 30), 30), this.getAccuracy()+1), 1.65, 15);
        this.addSkill("Snipe Shot", new RangeAdvantageDecorator(normalSnipe, advantageSnipe), 3);
        if (showMessage) {
            System.out.println(this.getName() + " evolved into a " + this.charClass + "!");
        }
    }

    public int getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(int accuracy) {
        Accuracy = accuracy;
    }

    public boolean hasRangeAdvantage() {
        return isRangeAdvantage;
    }

    public void setRangeAdvantage(boolean rangeAdvantage) {
        this.isRangeAdvantage = rangeAdvantage;
    }

    @Override
    public void displayCharacterDetails() {
        System.out.println();
        System.out.println("--- " + this.getDisplayName().toUpperCase() + " (" + this.getCharClass().toUpperCase() + ") ---");
        System.out.println(" Status: " + this.getStatus());
        System.out.println(" Level: " + this.getLevel());
        System.out.println(" Health Points: " + this.getHp() + "/" + this.getMaxHP());
        System.out.println(" Damage: " + this.getDamage());
        System.out.println(" Defense: " + this.getDefense());
        System.out.println(" Accuracy: " + this.getAccuracy() + "%");
        System.out.print(" Range Advantage: ");
        if(this.hasRangeAdvantage()) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
        System.out.println(" Weapon: " + this.weapon.getName() + " (Type: " + this.weapon.getType() + ", Damage: " + this.weapon.getDamage() + ", Ability: " + this.weapon.getAbility() + ")");
        System.out.println(" Skills learned: " + this.getSkills().keySet());
    }

    public void levelUp() {
        super.levelUp();
        this.setMaxHP(this.getMaxHP()+15);
        this.setHp(this.getMaxHP());
        this.setSpeed(this.getSpeed()+5);
        this.setCritChance(this.getCritChance()+3);
        this.setAccuracy(this.getAccuracy()+2);
        if (this.level >= 20 && this.classTier == 1) {
            this.classTier = 2;
            applyTier2Upgrades(true);
        }
    }

    public void resetStatus() {
        super.resetStatus();
        if (this.isRangeAdvantage) {
            this.setSpeed(this.getSpeed() / 0.7);
            this.setDefense(this.getDefense() - 10);
            this.isRangeAdvantage = false;
        }
    }

}
