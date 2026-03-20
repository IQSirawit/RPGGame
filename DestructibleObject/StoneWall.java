package RPGGame.DestructibleObject;

import RPGGame.Destructible;

public class StoneWall implements Destructible {
    protected String name;
    protected String location;
    protected int maxHP;
    protected int hp;
    protected String type;

    public StoneWall(String location, int maxHP, String type) {
        this.location = location;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void displayWallDetails(){
        System.out.println();
        System.out.println("--- " + this.getLocation().toUpperCase() + " (WOODEN BOX) ---");
        System.out.println("Type: " + this.getType());
        if(this.isDestroyed()) {
            System.out.println(" Status: DESTROYED");
        }
        else {
            System.out.println(" Status: INTACT");
        }
        System.out.println(" Durability: " + this.getHp() + "/" + this.getMaxHP());
    }

    @Override
    public String getDisplayName() {
        return this.location;
    }

    @Override
    public int takeDamage(int damage) {
        hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println("\uD83E\uDDF1 " + this.getLocation() + " takes " + damage + " damage! (Durability: " + this.getHp() + "/" + this.getMaxHP() + ")");
        if (isDestroyed()) {
            System.out.println("\uD83D\uDCA5 " + this.getLocation() + " has CRUMBLED to pieces!");
        }
        return damage;
    }

    @Override
    public boolean isDestroyed() {
        return this.hp <= 0;
    }
}
