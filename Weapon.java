package RPGGame;

public class Weapon {
    protected String name;
    protected String type;
    protected int damage;
    protected String ability;

    public Weapon(String name, String type, int damage, String ability) {
        this.name = name;
        this.type = type;
        this.damage = damage;
        this.ability = ability;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public String getAbility() {
        return ability;
    }

    public int getBaseDamage() {
        return damage;
    }
}