package RPGGame.Location;

import RPGGame.Character;
import RPGGame.InputHandler;
import RPGGame.Item.HealthPotion;
import RPGGame.Item.ManaPotion;
import RPGGame.Party;

import java.util.List;

public class Shop extends Location {

    public Shop(String name, String description) {
        super(name, description);
    }

    @Override
    public void enter(Party party) {
        boolean shopping = true;
        while (shopping) {
            System.out.println("\n🛒 === " + this.getName() + " ===");
            System.out.println(this.getDescription());
            System.out.println("💰 Your Gold: " + party.getGold() + "G");
            System.out.println("2. Buy Health Potion (50g)");
            System.out.println("1. Buy Mana Potion (20g)");
            System.out.println("0. Leave Shop");
            System.out.print("Select: ");

            int choice = InputHandler.getValidChoice(0, 2);
            if (choice == 2) {
                // เช็คว่ามีเงินพอไหม
                if (party.spendGold(50)) {
                    System.out.println("✅ You bought a Health Potion! (Remaining Gold: " + party.getGold() + "G)");
                    party.getInventory().addItem(new HealthPotion("Health Potion", "Heals 50 HP", 50, 50, 1));
                } else {
                    System.out.println("❌ Shopkeeper: You can't afford that!");
                }
            } else if (choice == 1) {
                if (party.spendGold(20)) {
                    System.out.println("✅ You bought a Mana Potion! (Remaining Gold: " + party.getGold() + "G)");
                    party.getInventory().addItem(new ManaPotion("Mana Potion", "Heals mana 100 MP", 20, 100, 1));
                } else {
                    System.out.println("❌ Shopkeeper: You can't afford that!");
                }
            } else {
                System.out.println("👋 Shopkeeper: Come back soon!");
                shopping = false;
            }
        }
    }
}