package RPGGame.Item;

import RPGGame.Character;
import RPGGame.Consumable;

// ยาเพิ่มเลือด เป็น Item ที่ กินได้(Consumable) และ ซ้อนทับกันได้(Stackable)
public class HealthPotion extends Item implements Consumable, Stackable {
    private int healPower;
    private int quantity;

    public HealthPotion(String name, String description, int price, int healPower, int quantity) {
        super(name, description, price); // ส่งข้อมูลพื้นฐานไปให้คลาสแม่ Item
        this.healPower = healPower;
        this.quantity = quantity;
    }

    public void setHealPower(int healPower) {
        this.healPower = healPower;
    }

    public int getHealPower() { return healPower; }

    // --- Implement Stackable ---
    @Override
    public int getQuantity() { return quantity; }

    @Override
    public void setQuantity(int quantity) { this.quantity = Math.max(0, quantity); }

    @Override
    public void addQuantity(int amount) { this.quantity += amount; }

    // --- Implement Consumable ---
    @Override
    public void use(Character user) {
        System.out.println("  🧪 " + user.getDisplayName() + " drinks " + this.getName() + "!");
        int oldHp = user.getHp();
        user.setHp(Math.min(user.getMaxHP(), user.getHp() + this.healPower));
        System.out.println("     ❤️ Health restored: " + oldHp + " → " + user.getHp());

        this.addQuantity(-1); // ลดจำนวนลง 1
        System.out.println("     📊 " + this.getName() + " remaining: " + this.getQuantity());
    }

    @Override
    public boolean canUse(Character target) {
        return true;
    }

    // --- Implement abstract method จาก Item ---
    @Override
    public void displayDetails() {
        System.out.println("--- " + this.getName().toUpperCase() + " ---");
        System.out.println(" Description: " + this.getDescription());
        System.out.println(" Healing Power: " + this.getHealPower() + " HP");
        System.out.println(" Quantity: " + this.getQuantity());
        System.out.println(" Value: " + this.getPrice() + " Gold");
    }
}