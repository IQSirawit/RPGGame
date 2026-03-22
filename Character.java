package RPGGame;

import java.util.*;

public class Character implements Destructible {
    protected String name;
    protected int level, hp, maxHP, damage, defense, critChance, classTier, xp, nextLevelXp, xpReward;
    protected double speed;
    protected Weapon weapon;
    protected String charClass, status="Active";
    protected boolean isAlive = true;
    protected boolean isAuto = false;
    protected Attack attack;
    protected Map<String, Attack> skills = new LinkedHashMap<>();
    protected Map<String, Integer> skillCooldowns = new HashMap<>();
    protected Map<String, Integer> lastUsedTurn = new HashMap<>();
    protected int goldReward;

    public Character(String name, int level, int classTier, int maxHP, int damage, int defense, double speed, Weapon weapon, String charClass) {
        this.name = name;
        this.level = level;
        this.classTier = classTier;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.damage = damage;
        this.defense = defense;
        this.speed = speed;
        this.critChance = 5;
        this.weapon = weapon;
        this.charClass = charClass;
        this.isAlive = true;
        this.attack = new BaseAttack();
        this.isAuto = false;
        this.xp = 0;
        this.nextLevelXp = level*100;
        this.xpReward = -1;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getClassTier() {
        return classTier;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public double getSpeed() {
        return speed;
    }

    public int getCritChance() {
        return critChance;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public String getCharClass() {
        return charClass;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getStatus() {
        return status;
    }

    public Attack getAttack() {
        return attack;
    }

    public Map<String, Attack> getSkills() {
        return skills;
    }

    public Map<String, Integer> getSkillCooldowns() {
        return skillCooldowns;
    }

    public Map<String, Integer> getLastUsedTurn() {
        return lastUsedTurn;
    }

    public int getXp() {
        return xp;
    }

    public int getNextLevelXp() {
        return nextLevelXp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setClassTier(int classTier) {
        this.classTier = classTier;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setNextLevelXp(int nextLevelXp) {
        this.nextLevelXp = nextLevelXp;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public void displayCharacterDetails() {
        System.out.println();
        System.out.println("--- " + this.getName().toUpperCase() + " (" + this.getCharClass().toUpperCase() + ") ---");
        System.out.println(" Status: " + this.getStatus());
        System.out.println(" Level: " + this.getLevel());
        System.out.println(" Health Points: " + this.getHp() + "/" + this.getMaxHP());
        System.out.println(" Damage: " + this.getDamage());
        System.out.println(" Defense: " + this.getDefense());
        System.out.println(" Weapon: " + this.getWeapon().getName() + " (Type: " + this.getWeapon().getType() + ", Damage: " + this.getWeapon().getDamage() + ", Ability: " + this.getWeapon().getAbility() + ")");
        System.out.println(" Skills learned: " + this.getSkills().keySet());
    }

    public void addSkill(String name, Attack skillChain, int cooldownTurns) {
        skills.put(name, skillChain);
        skillCooldowns.put(name, cooldownTurns);
        lastUsedTurn.put(name, -100); // Initialize so it's ready to use at turn 1
    }

    public void attack(Destructible target) {
        System.out.println("\n--- " + this.getDisplayName() + " Attacks " + target.getDisplayName() + " with " + this.getWeapon().getName() + "! ---");
        attack.attack(this,target);
        System.out.println();
    }

    public int takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - this.defense);
        System.out.println("⛊ " + this.name + " Defense: " + this.defense + " (reduces damage from " + damage + " to " + actualDamage + ")");
        this.hp -= actualDamage;
        System.out.println("💥 Actual Damage Taken: " + actualDamage);
        if (this.hp <= 0) {
            this.hp = 0;
            this.isAlive = false;
            this.status = "Fainted";
            System.out.println("💀 " + this.name + " fainted!");
        } else {
            System.out.println("❤️ " + this.name + "'s HP: " + this.hp + "/" + this.maxHP);
        }
        return actualDamage;
    }

    public void gainXp(int amount) {
        this.xp += amount;
        System.out.println("\n✨ " + name + " gained " + amount + " XP! (" + xp + "/" + nextLevelXp + ")");

        while (this.xp >= nextLevelXp) {
            levelUp();
        }
    }

    public void levelUp() {
        this.setLevel(this.getLevel()+1);
        this.setXp(this.getXp()-this.getNextLevelXp());
        this.setNextLevelXp(this.getLevel()*100);
        this.setMaxHP(this.getMaxHP()+20);
        this.setHp(this.getMaxHP());
        this.setDamage(this.getDamage()+5);
        this.setDefense(this.getDefense()+3);
        this.setSpeed(this.getSpeed()+2);
        this.setCritChance(this.getCritChance()+2);
        System.out.println("🎊 LEVEL UP! " + name + " is now Level " + level + "!");
    }

    public void setXpReward(int xp) {
        this.xpReward = xp;
    }

    public int getXpReward() {
        if (this.xpReward != -1) {
            return this.xpReward;
        }
        return this.level * 25;
    }

    @Override
    public boolean isDestroyed() {
        return this.hp <= 0;
    }
}