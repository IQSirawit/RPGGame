package RPGGame.Item;

import RPGGame.Character;
import RPGGame.Consumable;

public class HealthPotion implements Consumable {
    protected String name;
    protected int healPower;
    protected int quantity;

    public HealthPotion(String name, int healPower, int quantity) {
        this.name = name;
        this.healPower = healPower;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealPower() {
        return healPower;
    }

    public void setHealPower(int healPower) {
        this.healPower = healPower;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void displayPotionDetails(){
        System.out.println();
        System.out.println("--- " + this.getName().toUpperCase() + " ---");
        System.out.println(" Healing Power: " + this.getHealPower() + " HP");
        System.out.print(" Quantity: " + this.getQuantity());
        System.out.print(" Type: Consumable");
        System.out.println();
    }

    @Override
    public void use(Character user) {
        System.out.println("  \uD83E\uDDEA " + user.getDisplayName() + " drinks " + this.getName() + "!");
        System.out.println("     ❤\uFE0F Health restored: " + user.getHp() + " → " + (user.getHp()+this.healPower) + " (+" + this.getHealPower() + ")");
        user.setHp(user.getHp() + this.getHealPower());
        if (user.getHp() > user.getMaxHP()) {
            user.setHp(user.getMaxHP());
        }
        this.setQuantity(this.getQuantity()-1);
        System.out.println("     \uD83D\uDCCA " + this.getName() + " remaining: " + this.getQuantity());
    }
}
