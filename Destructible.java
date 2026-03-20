package RPGGame;

public interface Destructible {
    String getDisplayName();
    int getHp();
    int getMaxHP();
    int takeDamage(int damage);
    boolean isDestroyed();
}
