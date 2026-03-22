package RPGGame.Location;

import RPGGame.Character;
import RPGGame.InputHandler;
import RPGGame.Item.HealthPotion;
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
            System.out.println("1. Buy Health Potion (50g)");
            System.out.println("0. Leave Shop");
            System.out.print("Select: ");

            int choice = InputHandler.getValidChoice(0, 1);
            if (choice == 1) {
                // เช็คว่ามีเงินพอไหม
                if (party.spendGold(50)) {
                    System.out.println("✅ You bought a Health Potion! (Remaining Gold: " + party.getGold() + "G)");
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