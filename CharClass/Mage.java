package RPGGame.CharClass;

import RPGGame.BaseAttack;
import RPGGame.Character;
import RPGGame.Decorator.*;
import RPGGame.Weapon;

public class Mage extends Character {
    private int mana;
    private int maxMana;

    public Mage(String name, int level, int classTier, int maxHP, int damage, int defense, int speed, int maxMana, Weapon weapon) {
        super(name, level, classTier, maxHP, damage, defense, speed, weapon, "Mage");
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.attack = new BaseAttack();
        addBaseSkills();
        if (this.classTier >= 2) {
            applyTier2Upgrades(false);
        }
    }

    private void addBaseSkills() {
        this.addSkill("Magic Missile", new ScalingDamageDecorator(new SpellDecorator(new BaseAttack(), 20), 1.0, 25), 0);
    }

    private void applyTier2Upgrades(boolean showMessage) {
        this.charClass = "Wizard";
        this.addSkill("Blizzard",
                new SpellDecorator(new AoEDecorator(new ScalingDamageDecorator(new BaseAttack(), 0.7, 0)), 50), 3);
        this.addSkill("Life Drain",
                new SpellDecorator(new LifeStealDecorator(new ScalingDamageDecorator(new BaseAttack(), 1.2, 0), 20), 40), 2);
        if (showMessage) {
            System.out.println(this.getName() + " evolved into a " + this.charClass + "!");
        }
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    @Override
    public void displayCharacterDetails() {
        System.out.println();
        System.out.println("--- " + this.getDisplayName().toUpperCase() + " (" + this.getCharClass().toUpperCase() + ") ---");
        System.out.println(" Status: " + this.getStatus());
        System.out.println(" Level: " + this.getLevel());
        System.out.println(" Health Points: " + this.getHp() + "/" + this.getMaxHP());
        System.out.println(" Mana: " + this.getMana() + "/" + this.getMaxMana());
        System.out.println(" Damage: " + this.getDamage());
        System.out.println(" Defense: " + this.getDefense());
        System.out.println(" Weapon: " + this.weapon.getName() + " (Type: " + this.weapon.getType() + ", Damage: " + this.weapon.getDamage() + ", Ability: " + this.weapon.getAbility() + ")");
        System.out.println(" Skills learned: " + this.getSkills().keySet());
    }

    public void levelUp() {
        super.levelUp();
        this.setMaxHP(this.getMaxHP()+10);
        this.setHp(this.getMaxHP());
        this.setMaxMana(this.getMaxMana()+25);
        this.setMana(this.getMaxMana());
        this.setDamage(this.getDamage()+5);
        this.setDefense(this.getDefense()+2);
        if (this.level >= 20 && this.classTier == 1) {
            this.classTier = 2;
            applyTier2Upgrades(true);
        }
    }
}
