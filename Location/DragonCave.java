package RPGGame.Location;

import RPGGame.Character;
import RPGGame.CharClass.Warrior; // ใช้ Warrior เป็นโครงสร้างของบอสเพื่อให้มีค่า Armor
import RPGGame.Party;
import RPGGame.Weapon;
import RPGGame.BattleManager;

import java.util.ArrayList;
import java.util.List;

public class DragonCave extends Location {

    public DragonCave(String name, String description) {
        super(name, description);
    }

    @Override
    public void enter(Party party) {
        System.out.println("\n🌋 === " + this.getName() + " ===");
        System.out.println(this.getDescription());

        // 1. ตรวจสอบเงื่อนไขการเข้า (ต้องมี Dragon Orb)
        if (!party.getInventory().hasItem("Dragon Orb")) {
            System.out.println("⛔ A mystical barrier of fiery energy blocks your path.");
            System.out.println("   (You need the 'Dragon Orb' to break this barrier!)");
            return; // เด้งกลับออกไปหน้า World Map
        }

        // 2. ผ่านเงื่อนไข ทำลายบาเรีย
        System.out.println("🔮 The Dragon Orb resonates with the barrier... The fiery wall dissipates!");
        System.out.println("⚠️ ROARRR! The ground shakes violently as the Final Boss descends!");

        // 3. สร้างปาร์ตี้ของศัตรูที่มีแค่มังกรไฟตัวเดียว
        List<Character> enemyList = generateFinalBoss(party);
        Party finalBossParty = new Party(enemyList);

        // 4. เข้าสู่ฉากต่อสู้
        BattleManager bm = new BattleManager();
        bm.runBattle(party, finalBossParty);

        // 5. สรุปผลหลังจากสู้จบ (เช็คว่าปาร์ตี้เรายังรอดอยู่ไหม)
        if (party.isAlive()) {
            System.out.println("\n🎉🏆 === CONGRATULATIONS! YOU BEAT THE GAME! === 🏆🎉");
            System.out.println("You have defeated the legendary Fire Dragon!");
            System.out.println("Peace has been restored to the realm thanks to your brave party.");
            System.out.println("Thanks for playing!");
            System.exit(0); // บังคับจบเกม (ปิดโปรแกรม) หลังจากชนะบอสใหญ่
        } else {
            System.out.println("\n💀 === GAME OVER === 💀");
            System.out.println("Your party was turned to ash... The Fire Dragon reigns supreme.");
            // ไม่ต้อง System.exit(0) ตรงนี้ เพราะลูปในหน้าหลักจะหยุดเองเมื่อปาร์ตี้ตายหมด
        }
    }

    private List<Character> generateFinalBoss(Party party) {
        // หา Level ที่สูงที่สุดในปาร์ตี้เพื่อนำมาคำนวณความแข็งแกร่งของมังกร
        int maxLevel = 1;
        for (Character hero : party.getMembers()) {
            if (hero.getLevel() > maxLevel) {
                maxLevel = hero.getLevel();
            }
        }

        // กำหนดให้บอสมีเลเวลมากกว่าผู้เล่นที่เก่งที่สุด 2 เลเวล
        int bossLevel = maxLevel + 2;

        // คำนวณสเตตัสให้ดูเป็นบอสใหญ่ (เลือดเยอะ ตีแรงกว่าปกติ)
        int bossHP = 1500 + (bossLevel * 50);
        int bossDamage = 15 + (bossLevel * 2);
        int bossDefense = 5 + (bossLevel / 2);
        int bossSpeed = 10 + (bossLevel / 4);
        int bossArmor = 5;

        Weapon dragonBreath = new Weapon("Inferno Breath", "Magic", 15, "Burn");

        // สร้างมังกรไฟ โดยใช้คลาส Warrior เพื่อให้สามารถใช้ระบบ ArmorValue ได้
        Warrior fireDragon = new Warrior("Ignis The Fire Dragon", bossLevel, 3, bossHP, bossDamage, bossDefense, bossSpeed, bossArmor, dragonBreath);
        fireDragon.setXpReward(10000); // XP มหาศาล
        fireDragon.setGoldReward(5000);

        List<Character> enemies = new ArrayList<>();
        enemies.add(fireDragon);

        return enemies;
    }
}