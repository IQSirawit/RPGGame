package RPGGame;

import java.util.*;
import RPGGame.Character;
import RPGGame.Decorator.*;
import RPGGame.BattleInfo;

public class BattleManager {
    private final Scanner scanner = new Scanner(System.in);
    private int globalTurnCount = 1;

    public void runBattle(List<Character> partyA, List<Character> partyB) {
        System.out.println("\n" + "=".repeat(30));
        System.out.println(" ⚔️ BATTLE START! ⚔️ ");
        System.out.println("=".repeat(30));
        while (isPartyAlive(partyA) && isPartyAlive(partyB)) {
            System.out.println("\n--- TURN " + globalTurnCount + " ---");
            List<Character> allParticipants = new ArrayList<>();
            allParticipants.addAll(partyA);
            allParticipants.addAll(partyB);
            allParticipants.sort((c1, c2) -> Double.compare(c2.getSpeed(), c1.getSpeed()));
            for (Character active : allParticipants) {
                if (!active.isAlive() || !isPartyAlive(partyA) || !isPartyAlive(partyB)) continue;
                List<Character> allies = partyA.contains(active) ? partyA : partyB;
                List<Character> enemies = partyA.contains(active) ? partyB : partyA;
                BattleInfo.setAllies(allies);
                BattleInfo.setEnemies(enemies);
                if (active.isAuto) {
                    handleAutoTurn(active, enemies);
                } else {
                    handlePlayerTurn(active, enemies);
                }
            }
            globalTurnCount++;
        }
        handleBattleEnd(partyA, partyB);
    }

    private void handlePlayerTurn(Character active, List<Character> enemies) {
        boolean turnFinished = false;
        while (!turnFinished) {
            System.out.println("\n>> " + active.getName().toUpperCase() + " (" + active.getCharClass() + ") | HP: " + active.getHp() + "/" + active.getMaxHP());
            System.out.println("1. Attack");
            System.out.println("2. Skills" + (active.getSkills().isEmpty() ? " [LOCKED]" : ""));
            System.out.println("3. Check Status");
            System.out.println("4. Toggle Auto Mode");
            System.out.print("Selection: ");
            int choice = scanner.hasNextInt() ? scanner.nextInt() : 0;
            scanner.nextLine();
            switch (choice) {
                case 1 -> turnFinished = performAttackSelection(active, enemies, active.getAttack(), "Attack");
                case 2 -> {
                    if (active.getSkills().isEmpty()) {
                        System.out.println("❌ This character has no skills!");
                    } else {
                        turnFinished = openSkillMenu(active, enemies);
                    }
                }
                case 3 -> active.displayCharacterDetails();
                case 4 -> {
                    active.setAuto(true);
                    System.out.println("🤖 Auto Mode enabled for " + active.getName());
                    handleAutoTurn(active, enemies);
                    turnFinished = true;
                }
                default -> System.out.println("Invalid selection.");
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
        int choice = scanner.nextInt() - 1;
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
        int choice = scanner.nextInt() - 1;
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

    private void handleBattleEnd(List<Character> partyA, List<Character> partyB) {
        System.out.println("\n" + "=".repeat(30));
        if (isPartyAlive(partyA)) {
            System.out.println("✨ VICTORY! Players have won! ✨");
            int totalXpPool = 0;
            for (Character enemy : partyB) {
                totalXpPool += enemy.getXpReward();
            }
            List<Character> survivors = partyA.stream().filter(Character::isAlive).toList();
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