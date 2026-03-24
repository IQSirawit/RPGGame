package RPGGame;

import RPGGame.Item.Item;
import RPGGame.Item.Stackable;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items; // เปลี่ยนมาเก็บ Item ทั่วไป

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item newItem) {
        // ถ้าไอเทมใหม่มีพฤติกรรม "ซ้อนทับกันได้"
        if (newItem instanceof Stackable newStackItem) {
            for (Item existingItem : items) {
                // เช็คว่าเป็นไอเทมชนิดเดียวกันและชื่อเดียวกันไหม
                if (existingItem.getClass() == newItem.getClass() &&
                        existingItem.getName().equals(newItem.getName())) {

                    Stackable existingStackItem = (Stackable) existingItem;
                    existingStackItem.addQuantity(newStackItem.getQuantity()); // จับรวมกัน
                    return; // เสร็จสิ้นการเพิ่มของ
                }
            }
        }
        // ถ้าเป็นไอเทมที่ Stack ไม่ได้ (เช่น ดาบ) หรือยังไม่มีในกระเป๋า ก็เพิ่มเข้าไปใหม่เลย
        items.add(newItem);
    }

    public void cleanUpEmptyItems() {
        // ลบไอเทมที่ซ้อนทับได้แต่มีจำนวน <= 0 ออกจากกระเป๋า
        items.removeIf(item -> item instanceof Stackable && ((Stackable) item).getQuantity() <= 0);
    }

    public void displayInventory() {
        System.out.println("\n🎒 === PARTY INVENTORY ===");
        if (items.isEmpty()) {
            System.out.println(" The inventory is empty.");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.print(" " + (i + 1) + ". " + item.getName());
            if (item instanceof Stackable stackItem) {
                System.out.print(" (x" + stackItem.getQuantity() + ")");
            }
            System.out.println();
        }
    }

    public boolean hasItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true; // เจอไอเทมนี้ในกระเป๋าแล้ว
            }
        }
        return false; // ยังไม่มีไอเทมนี้
    }

    public List<Item> getItems() {
        return items;
    }
}