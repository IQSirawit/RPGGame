package RPGGame.CharClass;

import RPGGame.BaseAttack;
import RPGGame.Character;
import RPGGame.Decorator.*;
import RPGGame.SupportAbility;
import RPGGame.Weapon;

public class Healer extends Character {
    public Healer(String name, int level, int classTier, int maxHP, int damage, int defense, int speed, Weapon weapon) {
        super(name, level, classTier, maxHP, damage, defense, speed, weapon, "Healer");
        this.attack = new BaseAttack();
        applyBaseSkills();
        if (this.classTier >= 2) {
            applyTier2Upgrades(false);
        }
    }

    private void applyBaseSkills() {
        this.addSkill("Prayer",
                new SelfTargetDecorator(new HealDecorator(new SupportAbility(), 1.5)), 0);

        this.addSkill("Holy Light",
                new AllyTargetDecorator(new HealDecorator(new SupportAbility(), 2)), 1);
    }

    private void applyTier2Upgrades(boolean showMessage) {
        this.charClass = "Cleric";
        this.addSkill("Sanctuary",
                new AoEDecorator(new AllyTargetDecorator(new HealDecorator(new SupportAbility(), 0.8))),3);
        this.addSkill("Divine Judgement",
                new ScalingDamageDecorator(new LifeStealDecorator(new BaseAttack(), 20), 2, 20), 3);
        if (showMessage) {
            System.out.println(this.getName() + " evolved into a " + this.getCharClass() + "!");
        }
    }

    @Override
    public void levelUp() {
        super.levelUp();
        this.setMaxHP(this.getMaxHP() + 50);
        this.setHp(this.getMaxHP());
        this.setDamage(this.getDamage() + 3);
        this.setSpeed(this.getSpeed()+5);
        this.setDefense(this.getDefense()+10);
        if (this.level >= 20 && this.classTier == 1) {
            this.classTier = 2;
            applyTier2Upgrades(true);
        }
    }
}