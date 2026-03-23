package RPGGame;

import java.util.*;
import RPGGame.Character;
import RPGGame.Decorator.*;
import RPGGame.BattleInfo;
import RPGGame.Item.Item;
import RPGGame.Item.Stackable;

public class BattleManager {
    private final Scanner scanner = new Scanner(System.in);
    private int globalTurnCount = 1;

    public void runBattle(Party partyA, List<Character> partyB) {
        List<Character> playerPartyList = partyA.getMembers(); // ดึง List ออกมาใช้ในระบบเดิม
        System.out.println("\n" + "=".repeat(30));
        System.out.println(" ⚔️ BATTLE START! ⚔️ ");
        System.out.println("=".repeat(30));
        while (partyA.isAlive() && isPartyAlive(partyB)) {
            System.out.println("\n--- TURN " + globalTurnCount + " ---");
            List<Character> allParticipants = new ArrayList<>();
            allParticipants.addAll(playerPartyList);
            allParticipants.addAll(partyB);
            allParticipants.sort((c1, c2) -> Double.compare(c2.getSpeed(), c1.getSpeed()));
            for (Character active : allParticipants) {
                if (!active.isAlive() || !partyA.isAlive() || !isPartyAlive(partyB)) continue;
                List<Character> allies = playerPartyList.contains(active) ? playerPartyList : partyB;
                List<Character> enemies = playerPartyList.contains(active) ? partyB : playerPartyList;
                BattleInfo.setAllies(allies);
                BattleInfo.setEnemies(enemies);
                if (active.isAuto) {
                    handleAutoTurn(active, enemies);
                } else {
                    handlePlayerTurn(active, partyA, enemies);
                }
            }
            globalTurnCount++;
        }
        handleBattleEnd(partyA, partyB);
    }

    // เปลี่ยน Signature ให้รับ Party เข้ามาด้วย
    private void handlePlayerTurn(Character active, Party playerParty, List<Character> enemies) {
        boolean turnFinished = false;
        while (!turnFinished) {
            System.out.println("\n>> " + active.getName().toUpperCase() + " (" + active.getCharClass() + ") | HP: " + active.getHp() + "/" + active.getMaxHP());
            System.out.println("1. Attack");
            System.out.println("2. Skills" + (active.getSkills().isEmpty() ? " [LOCKED]" : ""));
            System.out.println("3. Items"); // 🌟 เมนูใหม่
            System.out.println("4. Check Status");
            System.out.println("5. Toggle Auto Mode");
            System.out.print("Selection: ");

            int choice = InputHandler.getValidChoice(1, 5); // อัปเดตให้เลือกได้ 5 ข้อ
            switch (choice) {
                case 1 -> turnFinished = performAttackSelection(active, enemies, active.getAttack(), "Attack");
                case 2 -> {
                    if (active.getSkills().isEmpty()) {
                        System.out.println("❌ This character has no skills!");
                    } else {
                        turnFinished = openSkillMenu(active, enemies);
                    }
                }
                case 3 -> turnFinished = openItemMenu(active, playerParty); // 🌟 เรียกเมนูใช้ไอเทม
                case 4 -> active.displayCharacterDetails();
                case 5 -> {
                    active.setAuto(true);
                    System.out.println("🤖 Auto Mode enabled for " + active.getName());
                    handleAutoTurn(active, enemies);
                    turnFinished = true;
                }
            }
        }
    }

    private boolean openSkillMenu(Character active, List<Character> enemies) {
        Map<String, Attack> skills = active.getSkills();
        List<String> skillNames = new ArrayList<>(skills.keySet());
        System.out.println("\n--- " + active.getName().toUpperCase() + "'S SKILLS ---");
        for (int i = 0; i < skillNames.size(); i++) {
            String name = skillNames.get(i);
            int cd = getRemainingCD(active, name);
            String status = (cd == 0) ? "[READY]" : "[COOLDOWN: " + cd + " turns]";
            System.out.println((i + 1) + ". " + name + " " + status);
        }
        System.out.println("0. [Back]");
        System.out.print("Selection: ");
        int choice = InputHandler.getValidChoice(0, skillNames.size()) - 1;
        if (choice >= 0 && choice < skillNames.size()) {
            String selectedName = skillNames.get(choice);
            if (getRemainingCD(active, selectedName) > 0) {
                System.out.println("❌ That skill is not ready yet!");
                return false;
            }
            if (performAttackSelection(active, enemies, skills.get(selectedName), selectedName)) {
                active.getLastUsedTurn().put(selectedName, globalTurnCount);
                return true;
            }
        }
        return false;
    }
    // อย่าลืม import RPGGame.Item.Item; และ import RPGGame.Item.Stackable; ไว้ด้านบนด้วยนะครับ

    private boolean openItemMenu(Character active, Party playerParty) {
        Inventory inventory = playerParty.getInventory();
        List<RPGGame.Item.Item> allItems = inventory.getItems();

        // 1. ดึงมาเฉพาะไอเทมที่ใช้กดกินได้ (Consumable)
        List<Consumable> usableItems = new java.util.ArrayList<>();
        for (RPGGame.Item.Item item : allItems) {
            if (item instanceof Consumable) {
                usableItems.add((Consumable) item);
            }
        }

        if (usableItems.isEmpty()) {
            System.out.println("❌ The party has no usable items!");
            return false; // ไม่เสียเทิร์น ให้กลับไปเลือก Action ใหม่
        }

        // 2. แสดงรายการไอเทม
        System.out.println("\n--- 🎒 PARTY INVENTORY ---");
        for (int i = 0; i < usableItems.size(); i++) {
            RPGGame.Item.Item item = (RPGGame.Item.Item) usableItems.get(i);
            System.out.print((i + 1) + ". " + item.getName());

            if (item instanceof RPGGame.Item.Stackable stackItem) {
                System.out.print(" (x" + stackItem.getQuantity() + ")");
            }
            System.out.println(" - " + item.getDescription());
        }
        System.out.println("0. [Back]");
        System.out.print("Select item to use: ");

        int itemChoice = InputHandler.getValidChoice(0, usableItems.size());
        if (itemChoice == 0) return false;

        Consumable selectedItem = usableItems.get(itemChoice - 1);

        // 3. แสดงรายชื่อเพื่อนในทีมเพื่อเลือกเป้าหมายที่จะป้อนไอเทม (รวมตัวเองด้วย)
        System.out.println("\nSelect target for " + ((RPGGame.Item.Item)selectedItem).getName() + ":");
        List<Character> allies = playerParty.getMembers();
        for (int i = 0; i < allies.size(); i++) {
            Character ally = allies.get(i);
            String status = ally.isAlive() ? "(HP: " + ally.getHp() + "/" + ally.getMaxHP() + ")" : "[FAINTED 💀]";
            System.out.println((i + 1) + ". " + ally.getName() + " " + status);
        }
        System.out.println("0. [Cancel]");
        System.out.print("Select target: ");

        int targetChoice = InputHandler.getValidChoice(0, allies.size());
        if (targetChoice == 0) return false;

        Character target = allies.get(targetChoice - 1);

        // 4. ตรวจสอบสถานะและความเข้ากันได้ของสายอาชีพ
        if (!target.isAlive()) {
            System.out.println("❌ " + target.getName() + " is fainted! Items have no effect.");
            return false;
        }

        // 🌟 ระบบป้องกันสายอาชีพ (เช่น Warrior จะกิน Mana Potion ไม่ได้)
        if (!selectedItem.canUse(target)) {
            System.out.println("❌ " + target.getName() + " (" + target.getCharClass() + ") cannot use this item!");
            return false; // คืนค่า false เพื่อให้ไม่เสียเทิร์น
        }

        // 5. ใช้งานสำเร็จ
        System.out.println("\n✨ " + active.getName() + " uses " + ((RPGGame.Item.Item)selectedItem).getName() + " on " + target.getName() + "!");
        selectedItem.use(target);
        inventory.cleanUpEmptyItems(); // เคลียร์ขวดยาเปล่าออกจากกระเป๋าปาร์ตี้

        return true; // เสียเทิร์น
    }

    private AoEDecorator findAoEDecorator(Attack attack) {
        if (attack instanceof AoEDecorator) return (AoEDecorator) attack;
        if (attack instanceof AttackDecorator ad) {
            return findAoEDecorator(ad.getWrappedAttack());
        }
        return null;
    }

    private boolean performAttackSelection(Character attacker, List<Character> targets, Attack attackType, String actionName) {
        if (attackType instanceof SelfTargetDecorator) {
            System.out.println("\n✨ " + attacker.getName() + " uses " + actionName + "!");
            attackType.attack(attacker, attacker);
            return true;
        }
        List<Character> possibleTargets = targets;
        if (attackType instanceof AllyTargetDecorator) {
            possibleTargets = BattleInfo.getAllies();
        }
        boolean isAoE = (findAoEDecorator(attackType) != null);
        if (isAoE) {
            System.out.println("\n✨ " + attacker.getName() + " uses " + actionName + "!");
            attackType.attack(attacker, targets.get(0));
            return true;
        }
        List<Character> aliveTargets = targets.stream().filter(Character::isAlive).toList();
        if (aliveTargets.isEmpty()) return false;
        System.out.println("\nSelect target for " + actionName + ":");
        for (int i = 0; i < aliveTargets.size(); i++) {
            System.out.println((i + 1) + ". " + aliveTargets.get(i).getName() + " (HP: " + aliveTargets.get(i).getHp() + ")");
        }
        System.out.println("0. Cancel");
        System.out.print("Selection: ");
        int choice = InputHandler.getValidChoice(0, aliveTargets.size()) - 1;
        if (choice >= 0 && choice < aliveTargets.size()) {
            System.out.println("\n✨ " + attacker.getName() + " uses " + actionName + "!");
            attackType.attack(attacker, aliveTargets.get(choice));
            return true;
        }
        return false;
    }

    private int getRemainingCD(Character c, String skillName) {
        int lastUsed = c.getLastUsedTurn().getOrDefault(skillName, -100);
        int cooldown = c.getSkillCooldowns().getOrDefault(skillName, 0);
        int readyAt = lastUsed + cooldown + 1;
        return Math.max(0, readyAt - globalTurnCount);
    }

    private void handleAutoTurn(Character active, List<Character> enemies) {
        List<Character> allies = BattleInfo.getAllies();
        for (String skillName : active.getSkills().keySet()) {
            if (getRemainingCD(active, skillName) == 0) {
                Attack skill = active.getSkills().get(skillName);

                if (isSupportSkill(skill)) {
                    Character weakestAlly = findWeakest(allies);
                    if (weakestAlly.getHp() < (weakestAlly.getMaxHP() * 0.9)) {
                        useSkillInAuto(active, skill, skillName, weakestAlly);
                        return;
                    }
                    continue;
                }
                Character weakestEnemy = findWeakest(enemies);
                useSkillInAuto(active, skill, skillName, weakestEnemy);
                return;
            }
        }
        Character target = findWeakest(enemies);
        System.out.println("\n⚔️ " + active.getName() + " attacks " + target.getName() + "!");
        active.getAttack().attack(active, target);
    }

    private boolean isSupportSkill(Attack skill) {
        return (skill instanceof AllyTargetDecorator || skill instanceof SelfTargetDecorator);
    }

    private Character findWeakest(List<Character> group) {
        Character weakest = null;
        double lowestHPPercentage = 1.1;
        for (Character c : group) {
            if (c.isAlive()) {
                double currentPercentage = (double) c.getHp() / c.getMaxHP();
                if (currentPercentage < lowestHPPercentage) {
                    lowestHPPercentage = currentPercentage;
                    weakest = c;
                }
            }
        }
        return weakest;
    }

    private void useSkillInAuto(Character active, Attack skill, String name, Character target) {
        System.out.println("\n🤖 [AUTO] " + active.getName() + " uses " + name + " on " + target.getName() + "!");
        skill.attack(active, target);
        active.getLastUsedTurn().put(name, globalTurnCount);
    }

    private boolean isPartyAlive(List<Character> party) {
        return party.stream().anyMatch(Character::isAlive);
    }

    // เปลี่ยน signature รับค่า Party
    private void handleBattleEnd(Party partyA, List<Character> partyB) {
        List<Character> playerPartyList = partyA.getMembers();

        System.out.println("\n" + "=".repeat(30));
        if (partyA.isAlive()) {
            System.out.println("✨ VICTORY! Players have won! ✨");
            int totalXpPool = 0;
            int totalGoldPool = 0; // เพิ่มตัวแปรเก็บเงินรวม

            for (Character enemy : partyB) {
                totalXpPool += enemy.getXpReward();
                totalGoldPool += enemy.getGoldReward(); // ดึงเงินจากศัตรู
            }

            // มอบเงินให้กระเป๋ากองกลางของปาร์ตี้
            partyA.addGold(totalGoldPool);
            System.out.println("💰 Party found " + totalGoldPool + " Gold! (Total Gold: " + partyA.getGold() + "G)");

            List<Character> survivors = playerPartyList.stream().filter(Character::isAlive).toList();
            if (!survivors.isEmpty()) {
                int xpPerPerson = totalXpPool / survivors.size();
                System.out.println("Total XP Pool: " + totalXpPool + " | Each survivor gets: " + xpPerPerson);
                for (Character hero : survivors) {
                    hero.gainXp(xpPerPerson);
                }
            }
        } else {
            System.out.println("💀 DEFEAT! The party has fallen... 💀");
        }
        System.out.println("=".repeat(30));
    }
}