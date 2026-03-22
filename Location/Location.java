package RPGGame.Location;

import RPGGame.Character;
import RPGGame.Party;

import java.util.List;
import java.util.Scanner;

public abstract class Location {
    protected String name;
    protected String description;


    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Abstract method บังคับให้คลาสลูกต้องมีฟังก์ชันนี้
    // รับ List<Character> เพื่อให้เข้าไปทั้งปาร์ตี้ได้
    public abstract void enter(Party party);
}