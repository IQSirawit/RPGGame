package RPGGame.Location;

import RPGGame.Character;
import RPGGame.CharClass.Mage; // ไว้เช็คมานา
import RPGGame.InputHandler;
import RPGGame.Party;

import java.util.List;

public class Inn extends Location {
    private int costPerNight;

    public Inn(String name, String description, int costPerNight) {
        super(name, description);
        this.costPerNight = costPerNight;
    }

    public int getCostPerNight() {
        return costPerNight;
    }

    public void setCostPerNight(int costPerNight) {
        this.costPerNight = costPerNight;
    }

    @Override
    public void enter(Party party) {
        System.out.println("\n🏠 === " + this.getName() + " ===");
        System.out.println(this.getDescription());
        System.out.println("Cost to rest: " + costPerNight + " gold. (You have: " + party.getGold() + "G)");
        System.out.print("Do you want to rest? (1. Yes / 2. No): ");

        int choice = InputHandler.getValidChoice(1, 2);
        if (choice == 1) {
            // เช็คว่ามีเงินพอจ่ายไหม
            if (party.spendGold(costPerNight)) {
                System.out.println("\n💤 The party pays " + costPerNight + "G and rests for the night...");
                System.out.println("💰 Remaining Gold: " + party.getGold() + "G");
                for (Character c : party.getMembers()) { // เข้าถึงตัวละครผ่าน getMembers()
                    if(c.isAlive()) {
                        c.setHp(c.getMaxHP());
                        if(c instanceof RPGGame.CharClass.Mage) {
                            ((RPGGame.CharClass.Mage) c).setMana(((RPGGame.CharClass.Mage) c).getMaxMana());
                        }
                        System.out.println("✨ " + c.getName() + " is fully healed!");
                    }
                }
            } else {
                System.out.println("❌ Innkeeper: You don't have enough gold! Come back when you're richer.");
            }
        } else {
            System.out.println("You leave the inn.");
        }
    }
}