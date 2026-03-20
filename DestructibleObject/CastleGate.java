package RPGGame.DestructibleObject;

import RPGGame.Destructible;

public class CastleGate implements Destructible {
    protected String name;
    protected String location;
    protected int maxHP;
    protected int hp;
    protected boolean isLocked;
    protected boolean isReinforced;
    protected boolean isDestroyed;

    public CastleGate(String location, int maxHP, boolean isLocked, boolean isReinforced) {
        this.location = location;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.isLocked = isLocked;
        this.isReinforced = isReinforced;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    @Override
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isReinforced() {
        return isReinforced;
    }

    public void setReinforced(boolean reinforced) {
        isReinforced = reinforced;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    public void displayGateDetails(){
        System.out.println();
        System.out.println("--- " + this.getLocation().toUpperCase() + " (WOODEN BOX) ---");
        if(this.isDestroyed()) {
            System.out.println(" Status: BREACHED");
        }
        else {
            System.out.println(" Status: INTACT");
        }
        System.out.println(" Durability: " + this.getHp() + "/" + this.getMaxHP());
        if(this.isLocked()) {
            System.out.println(" Locked: Yes");
        }
        else {
            System.out.println(" Locked: No");
        }
        if(this.isReinforced()) {
            System.out.println(" Reinforced: Yes");
        }
        else {
            System.out.println(" Reinforced: No");
        }
    }

    @Override
    public String getDisplayName() {
        return this.location;
    }

    @Override
    public int takeDamage(int damage) {
        if (isReinforced) {
            int reduced = damage / 2;
            System.out.println("\uD83D\uDDC4\uFE0F " + this.getLocation() + " is REINFORCED! (reduces damage from " + damage + " to " + reduced + ")");
            damage = reduced;
        }
        hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println("\uD83D\uDEAA " + this.getLocation() + " takes " + damage + " damage! (Durability: " + this.getHp() + "/" + this.getMaxHP() + ")");
        if (isDestroyed()) {
            System.out.println("\uD83D\uDCA5 " +this.getLocation() + " has been BREACHED! the gate COLLAPSES!");
        }
        return damage;
    }

    @Override
    public boolean isDestroyed() {
        return this.hp <= 0;
    }
}
