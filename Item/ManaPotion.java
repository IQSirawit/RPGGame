package RPGGame.Item;

import RPGGame.Character;
import RPGGame.Consumable;
import RPGGame.CharClass.Mage; // ต้อง import Mage เข้ามาเพื่อเช็คอาชีพ
import RPGGame.RPGGameApp;

public class ManaPotion extends Item implements Consumable, Stackable {
    private int manaPower;
    private int quantity;

    public ManaPotion(String name, String description, int price, int manaPower, int quantity) {
        super(name, description, price);
        this.manaPower = manaPower;
        this.quantity = quantity;
    }

    public void setManaPower(int manaPower) {
        this.manaPower = manaPower;
    }

    public int getManaPower() {
        return manaPower;
    }

    // --- Implement Stackable ---
    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    @Override
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // --- Implement Consumable ---
    @Override
    public void use(Character user) {
        System.out.println("  🧪 " + user.getDisplayName() + " drinks " + this.getName() + "!");

        // ตรวจสอบว่าผู้ใช้เป็น Mage หรืออาชีพที่มีเวทมนตร์หรือไม่
        if (user instanceof Mage magicUser) {
            int oldMana = magicUser.getMana();
            magicUser.setMana(Math.min(magicUser.getMaxMana(), magicUser.getMana() + this.manaPower));
            System.out.println("     ✨ Mana restored: " + oldMana + " → " + magicUser.getMana() + " (+" + this.manaPower + ")");
        } else {
            // ถ้าเป็น Warrior, Archer หรือตัวละครที่ไม่มี Mana ดื่มเข้าไป
            System.out.println("     💧 It tastes like sweet blueberries, but has no effect on non-magic users.");
        }

        this.addQuantity(-1); // ลดจำนวนยาลง 1 ขวด
        System.out.println("     📊 " + this.getName() + " remaining: " + this.getQuantity());
    }

    @Override
    public boolean canUse(Character target) {
        return target instanceof RPGGame.CharClass.Mage;
    }

    // --- Implement abstract method จาก Item ---
    @Override
    public void displayDetails() {
        System.out.println("--- " + this.getName().toUpperCase() + " ---");
        System.out.println(" Description: " + this.getDescription());
        System.out.println(" Mana Power: " + this.getManaPower() + " MP");
        System.out.println(" Quantity: " + this.getQuantity());
        System.out.println(" Value: " + this.getPrice() + " Gold");
    }
}