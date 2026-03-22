package RPGGame;

import java.util.List;

public class Party {
    private List<Character> members;
    private int gold;

    public Party(List<Character> members, int startingGold) {
        this.members = members;
        this.gold = startingGold;
    }

    public List<Character> getMembers() {
        return members;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    // เมธอดสำหรับจ่ายเงิน จะคืนค่า true ถ้าเงินพอ และ false ถ้าเงินไม่พอ
    public boolean spendGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

    // ตรวจสอบว่าในปาร์ตี้ยังมีคนรอดชีวิตอยู่ไหม
    public boolean isAlive() {
        return members.stream().anyMatch(Character::isAlive);
    }
}