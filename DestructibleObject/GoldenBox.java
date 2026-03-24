package RPGGame.DestructibleObject;

import RPGGame.Party;
import RPGGame.Item.DragonOrb;

public class GoldenBox extends WoodenBox {

    public GoldenBox(String name, int maxHP, boolean isLocked, String items, int goldReward) {

        super(name, maxHP, isLocked, items, goldReward);
    }

    @Override
    public void breakOpen(Party party) {
        if (isDestroyed()) {
            System.out.println("🔓 The golden lock broke! " + this.getDisplayName() + " shines brightly!");

            if (this.goldReward > 0 && party != null) {
                party.addGold(this.goldReward);
                System.out.println("💰 Party gained " + this.goldReward + " Gold from the golden chest!");
            }

            // ระบบสุ่มโอกาส 50% (0.0 ถึง 0.5)
            if (Math.random() < 0.50 && party != null) {
                // เช็คว่ามี Dragon Orb ในกระเป๋าหรือยัง
                if (party.getInventory().hasItem("Dragon Orb")) {
                    System.out.println("🔮 You found a rare glowing stone, but since you already have the Dragon Orb, it shattered into 300 Gold!");
                    party.addGold(300); // ให้เงินชดเชยแทน
                } else {
                    System.out.println("🔮 INCREDIBLE! You found a Dragon Orb inside the chest!");
                    party.getInventory().addItem(new DragonOrb());
                }
            } else {
                System.out.println("🎁 The chest contained some ancient dust, but no Dragon Orb this time.");
            }
        } else {
            System.out.println("⚠️ Cannot break open " + this.getDisplayName() + " - it's still intact!");
        }
    }
}