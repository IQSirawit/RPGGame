package RPGGame.CharClass;

import RPGGame.BaseAttack;
import RPGGame.Character;
import RPGGame.Decorator.*;
import RPGGame.Weapon;

public class Warrior extends Character {
    private int armorValue;

    public Warrior(String name, int level, int classTier, int maxHP, int damage, int defense, int speed, int armorValue, Weapon weapon) {
        super(name, level, classTier, maxHP, damage, defense, speed, weapon, "Warrior");
        this.armorValue = armorValue;
        this.attack = new BaseAttack();
        addBaseSkills();
        if (this.classTier >= 2) {
            applyTier2Upgrades(false);
        }
    }

    private void addBaseSkills() {
        this.addSkill("Powerful Strike", new ScalingDamageDecorator(new BaseAttack(), "Powerful Strike", 1.3, 0), 1);
    }

    private void applyTier2Upgrades(boolean showMessage) {
        this.charClass = "Knight";
        this.addSkill("Grand Smash",
                new ScalingDamageDecorator(new ArmorPierceDecorator(new BaseAttack(), 30), "Grand Smash", 1.8, 5), 3);
        this.addSkill("Whirlwind Slash",
                new AoEDecorator(new ScalingDamageDecorator(new BaseAttack(), "Whirlwind Slash", 0.7, 5)), 2);
        if (showMessage) {
            System.out.println(this.getName() + " evolved into a " + this.charClass + "!");
        }
    }

    public int getArmorValue() {
        return armorValue;
    }

    public void setArmorValue(int armorValue) {
        this.armorValue = armorValue;
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
        System.out.println(" Armor Value: " + this.getArmorValue());
        System.out.println(" Weapon: " + this.weapon.getName() + " (Type: " + this.weapon.getType() + ", Damage: " + this.weapon.getDamage() + ", Ability: " + this.weapon.getAbility() + ")");
        System.out.println(" Skills learned: " + this.getSkills().keySet());
    }

    @Override
    public int takeDamage(int damage) {
        System.out.println("🛡️ Warrior Armor Absorbs " + this.armorValue + " damage! (reduces damage from " + damage + " to " + (damage-this.getArmorValue()) + ")");
        damage -= this.armorValue;
        if (damage < 0) {
            damage = 0;
        }
        super.takeDamage(damage);
        return damage;
    }

    public void levelUp() {
        super.levelUp();
        this.setDamage(this.getDamage()+5);
        this.setDefense(this.getDefense()+5);
        this.setCritChance(this.getCritChance()+1);
        this.setArmorValue(this.getArmorValue()+2);
        if (this.level >= 20 && this.classTier == 1) {
            this.classTier = 2;
            applyTier2Upgrades(true);
        }
    }
}
