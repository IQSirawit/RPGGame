package RPGGame;

public interface Consumable {
    void use(Character user);

    boolean canUse(Character target);
}
