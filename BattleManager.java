package RPGGame;

import java.util.*;

import RPGGame.CharClass.*;
import RPGGame.Character;
import RPGGame.Decorator.*;
import RPGGame.BattleInfo;
import RPGGame.Item.Item;
import RPGGame.Item.Stackable;

public class BattleManager {
    private int globalTurnCount = 1;

    public void runBattle(Party players, Party enemies) {
        this.globalTurnCount = 1;
        for (Character hero : players.getMembers()) {
            hero.resetStatus();
            hero.getLastUsedTurn().clear();
        }
        for (Character enemy : enemies.getMembers()) {
            enemy.setAuto(true);
        }
        System.out.println("\n" + "=".repeat(30));
        System.out.println(" ⚔️ BATTLE START! ⚔️ ");
        System.out.println("=".repeat(30));

        while (players.isAlive() && enemies.isAlive()) {
            System.out.println("\n--- TURN " + globalTurnCount + " ---");
            List<Character> allParticipants = new ArrayList<>();
            allParticipants.addAll(players.getMembers());
            allParticipants.addAll(enemies.getMembers());
            allParticipants.sort((c1, c2) -> Double.compare(c2.getSpeed(), c1.getSpeed()));
            for (Character active : allParticipants) {
                if (!active.isAlive() || !players.isAlive() || !enemies.isAlive()) continue;
                System.out.println("\n" + "-".repeat(20));
                boolean isPlayerSide = players.getMembers().contains(active);
                List<Character> allyList = isPlayerSide ? players.getMembers() : enemies.getMembers();
                List<Character> opponentList = isPlayerSide ? enemies.getMembers() : players.getMembers();
                BattleInfo.setAllies(allyList);
                BattleInfo.setEnemies(opponentList);
                if (active.isAuto) {
                    handleAutoTurn(active, allyList, opponentList);
                } else {
                    handlePlayerTurn(active, players, opponentList);
                }
            }
            globalTurnCount++;
        }
        handleBattleEnd(players, enemies);
    }

    private void handlePlayerTurn(Character active, Party playerParty, List<Character> enemies) {
        boolean turnFinished = false;
        while (!turnFinished) {
            System.out.println(">> " + active.getName().toUpperCase() + " (" + active.getCharClass() + ") | HP: " + active.getHp() + "/" + active.getMaxHP());
            System.out.println("1. Attack");
            System.out.println("2. Skills" + (active.getSkills().isEmpty() ? " [LOCKED]" : ""));
            System.out.println("3. Items");
            System.out.println("4. Check Status");
            System.out.println("5. Toggle Auto Mode");
            System.out.print("Selection: ");

            int choice = InputHandler.getValidChoice(1, 5);
            switch (choice) {
                case 1 -> turnFinished = performAttackSelection(active, enemies, active.getAttack(), "Attack");
                case 2 -> turnFinished = openSkillMenu(active, enemies);
                case 3 -> turnFinished = openItemMenu(active, playerParty);
                case 4 -> active.displayCharacterDetails();
                case 5 -> {
                    active.setAuto(true);
                    System.out.println("🤖 Auto Mode enabled for " + active.getName() + "\n");
                    handleAutoTurn(active, playerParty.getMembers(), enemies);
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

    private boolean openItemMenu(Character active, Party playerParty) {
        Inventory inventory = playerParty.getInventory();
        List<Item> allItems = inventory.getItems();
        List<Consumable> usableItems = new ArrayList<>();
        for (Item item : allItems) {
            if (item instanceof Consumable) {
                usableItems.add((Consumable) item);
            }
        }

        if (usableItems.isEmpty()) {
            System.out.println("❌ The party has no usable items!");
            return false;
        }

        System.out.println("\n--- 🎒 PARTY INVENTORY ---");
        for (int i = 0; i < usableItems.size(); i++) {
            Item item = (Item) usableItems.get(i);
            System.out.print((i + 1) + ". " + item.getName());
            if (item instanceof Stackable stackItem) {
                System.out.print(" (x" + stackItem.getQuantity() + ")");
            }
            System.out.println(" - " + item.getDescription());
        }
        System.out.println("0. [Back]");
        System.out.print("Select item: ");

        int itemChoice = InputHandler.getValidChoice(0, usableItems.size());
        if (itemChoice == 0) return false;
        Consumable selectedItem = usableItems.get(itemChoice - 1);
        List<Character> allies = playerParty.getMembers();
        System.out.println("\nSelect target:");
        for (int i = 0; i < allies.size(); i++) {
            Character ally = allies.get(i);
            System.out.println((i + 1) + ". " + ally.getName() + (ally.isAlive() ? "" : " [FAINTED]"));
        }
        int targetChoice = InputHandler.getValidChoice(0, allies.size());
        if (targetChoice == 0) return false;
        Character target = allies.get(targetChoice - 1);
        if (!target.isAlive() || !selectedItem.canUse(target)) {
            System.out.println("❌ Cannot use item on this target!");
            return false;
        }
        selectedItem.use(target);
        inventory.cleanUpEmptyItems();
        return true;
    }

    private boolean performAttackSelection(Character attacker, List<Character> targets, Attack attackType, String actionName) {
        if (attackType instanceof SelfTargetDecorator) {
            attackType.attack(attacker, attacker);
            return true;
        }
        List<Character> possibleTargets = targets;
        if (hasAllyTargetDecorator(attackType)) {
            possibleTargets = BattleInfo.getAllies();
        }
        if (findAoEDecorator(attackType) != null) {
            attackType.attack(attacker, possibleTargets.get(0));
            return true;
        }
        List<Character> aliveTargets = possibleTargets.stream().filter(Character::isAlive).toList();
        if (aliveTargets.isEmpty()) return false;
        System.out.println("\nSelect target for " + actionName + ":");
        for (int i = 0; i < aliveTargets.size(); i++) {
            System.out.println((i + 1) + ". " + aliveTargets.get(i).getName());
        }
        System.out.print("Selection: ");
        int choice = InputHandler.getValidChoice(0, aliveTargets.size()) - 1;
        if (choice >= 0) {
            attackType.attack(attacker, aliveTargets.get(choice));
            return true;
        }
        return false;
    }

    private void handleAutoTurn(Character active, List<Character> allies, List<Character> opponents) {
        for (String skillName : active.getSkills().keySet()) {
            Attack skill = active.getSkills().get(skillName);
            if (getRemainingCD(active, skillName) == 0) {
                int cost = getManaCost(skill);
                if (active instanceof Mage mage && mage.getMana() < cost) {
                    continue;
                }

                if (isToggleAlreadyActive(skill, active)) {
                    continue;
                }

                Character target = null;

                if (skill instanceof SelfTargetDecorator) {
                    target = active;
                    if ((double)active.getHp() / active.getMaxHP() >= 0.9) continue;

                } else if (isStanceSkill(skill)) {
                    target = active;

                } else if (hasAllyTargetDecorator(skill)) {
                    target = allies.stream()
                            .filter(a -> a.isAlive() && (double)a.getHp() / a.getMaxHP() < 0.9)
                            .min(Comparator.comparingDouble(a -> (double)a.getHp() / a.getMaxHP()))
                            .orElse(null);

                    if (target == null) continue;
                } else {
                    target = findWeakest(opponents);
                }

                if (target != null) {
                    System.out.println("🤖 [AUTO] " + active.getName() + " uses " + skillName + " on " + target.getName() + "!");
                    skill.attack(active, target);
                    active.getLastUsedTurn().put(skillName, globalTurnCount);
                    return;
                }
            }
        }
        Character enemyTarget = findWeakest(opponents);
        if (enemyTarget != null) {
            System.out.println("🤖 [AUTO] " + active.getName() + " attacks " + enemyTarget.getName() + "!");
            active.getAttack().attack(active, enemyTarget);
        }
    }

    private Character findWeakest(List<Character> group) {
        return group.stream()
                .filter(Character::isAlive)
                .min(Comparator.comparingDouble(c -> (double) c.getHp() / c.getMaxHP()))
                .orElse(null);
    }

    private int getManaCost(Attack skill) {
        if (skill instanceof SpellDecorator sd) return sd.getManaCost();
        if (skill instanceof AttackDecorator ad) return getManaCost(ad.getWrappedAttack());
        return 0;
    }

    private boolean isStanceSkill(Attack skill) {
        if (skill instanceof ToggleCoverDecorator) return true;
        if (skill instanceof AttackDecorator ad) return isStanceSkill(ad.getWrappedAttack());
        return false;
    }

    private boolean isToggleAlreadyActive(Attack skill, Character active) {
        if (skill instanceof ToggleCoverDecorator && active instanceof Archer archer) {
            return archer.hasRangeAdvantage(); // If true, they are already in cover!
        }

        if (skill instanceof AttackDecorator ad) {
            return isToggleAlreadyActive(ad.getWrappedAttack(), active);
        }

        return false;
    }

    private int getRemainingCD(Character c, String skillName) {
        int lastUsed = c.getLastUsedTurn().getOrDefault(skillName, -100);
        int cooldown = c.getSkillCooldowns().getOrDefault(skillName, 0);
        return Math.max(0, (lastUsed + cooldown + 1) - globalTurnCount);
    }

    private void handleBattleEnd(Party players, Party enemies) {
        System.out.println("\n" + "=".repeat(30));
        if (players.isAlive()) {
            System.out.println("✨ VICTORY! ✨");
            int totalXp = 0;
            int totalGold = 0;
            for (Character enemy : enemies.getMembers()) {
                totalXp += enemy.getXpReward();
                totalGold += enemy.getGoldReward();
            }
            players.addGold(totalGold);
            System.out.println("💰 Found " + totalGold + " Gold!");
            List<Character> survivors = players.getMembers().stream().filter(Character::isAlive).toList();
            if (!survivors.isEmpty()) {
                int xpPerPerson = totalXp / survivors.size();
                survivors.forEach(h -> h.gainXp(xpPerPerson));
            }
        } else {
            System.out.println("💀 DEFEAT! 💀");
        }
    }

    private AoEDecorator findAoEDecorator(Attack attack) {
        if (attack instanceof AoEDecorator) return (AoEDecorator) attack;
        if (attack instanceof AttackDecorator ad) return findAoEDecorator(ad.getWrappedAttack());
        return null;
    }

    private boolean hasAllyTargetDecorator(Attack attack) {
        if (attack instanceof AllyTargetDecorator) return true;
        if (attack instanceof AttackDecorator ad) return hasAllyTargetDecorator(ad.getWrappedAttack());
        return false;
    }
}