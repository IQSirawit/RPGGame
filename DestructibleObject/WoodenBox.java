package RPGGame.DestructibleObject;

import RPGGame.Destructible;

public class WoodenBox implements Destructible {
    protected String name;
    protected int maxHP;
    protected int hp;
    protected boolean isLocked;
    protected String items;

    public WoodenBox(String name, int maxHP, boolean isLocked, String items) {
        this.name = name;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.isLocked = isLocked;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void displayBoxDetails(){
        System.out.println();
        System.out.println("--- " + this.getDisplayName().toUpperCase() + " (WOODEN BOX) ---");
        if(this.isDestroyed()) {
            System.out.println(" Status: Destroyed");
        }
        else {
            System.out.println(" Status: Intact");
        }
        System.out.println(" Health Points: " + this.getHp() + "/" + this.getMaxHP());
        System.out.print(" Locked: ");
        if (this.isLocked) {
            System.out.println("Yes \uD83D\uDD12");
        }
        else {
            System.out.println("No \uD83D\uDD13");
        }
        System.out.println(" Contains: " + this.getItems());
    }

    public void breakOpen() {
        if (isDestroyed()) {
            System.out.println("\uD83D\uDD13 The lock broke! " + this.getDisplayName() + " breaks open!");
            System.out.println("\uD83C\uDF81 Contents revealed! " + this.getItems());
        }
        else {
            System.out.println("⚠\uFE0F Cannot break open " + this.getDisplayName() + " - it's still intact!");
        }
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public int takeDamage(int damage) {
        if (isDestroyed()) return 0;
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println("\uD83D\uDCA5 " + this.getName() + " takes " + damage + " damage!");
        System.out.print("\uD83D\uDCE6 " + this.getName() + " HP: " + this.getHp() + "/" + this.getMaxHP());
        if (isDestroyed()) {
            System.out.println(" (DESTROYED!)");
        } else {
            System.out.println();
        }
        return damage;
    }

    @Override
    public boolean isDestroyed() {
        return this.hp <= 0;
    }
}