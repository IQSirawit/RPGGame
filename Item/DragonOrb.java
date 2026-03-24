package RPGGame.Item;

public class DragonOrb extends Item {

    public DragonOrb() {
        // กำหนดชื่อ คำอธิบาย และราคา 1000 G
        super("Dragon Orb", "A mysterious glowing orb. Reserved for a future quest.", 1000);
    }

    @Override
    public void displayDetails() {
        System.out.println("--- " + this.getName().toUpperCase() + " ---");
        System.out.println(" Description: " + this.getDescription());
        System.out.println(" Value: " + this.getPrice() + " Gold");
        System.out.println(" Note: This is a Key Item. It cannot be used in battle and does not stack.");
    }

}