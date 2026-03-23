package RPGGame.Item;

public interface Stackable {
    int getQuantity();
    void setQuantity(int quantity);
    void addQuantity(int amount);
}
