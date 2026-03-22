package RPGGame;

import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    // เมธอดสำหรับรับค่าเฉพาะตัวเลข และต้องอยู่ในช่วง min ถึง max
    public static int getValidChoice(int min, int max) {
        while (true) {
            try {
                // รับค่าเป็น String ก่อนเพื่อป้องกัน Error จากการพิมพ์ตัวอักษร
                String input = scanner.nextLine().trim();

                // ถ้ารับค่าว่างมา ให้ข้ามไป
                if (input.isEmpty()) {
                    continue;
                }

                int choice = Integer.parseInt(input); // แปลงเป็นตัวเลข

                // ตรวจสอบว่าตัวเลขอยู่ในช่วงที่กำหนดหรือไม่
                if (choice >= min && choice <= max) {
                    return choice; // ค่าถูกต้อง ส่งกลับไปใช้งาน
                } else {
                    System.out.print("⚠️ โปรดเลือกหมายเลขระหว่าง " + min + " ถึง " + max + " เท่านั้น: ");
                }
            } catch (NumberFormatException e) {
                // ดักจับ Error กรณีที่ผู้เล่นพิมพ์ตัวอักษรหรือสัญลักษณ์
                System.out.print("❌ ข้อมูลไม่ถูกต้อง! โปรดกรอกเป็น 'ตัวเลข' เท่านั้น: ");
            }
        }
    }

    // เผื่อใช้รับค่าข้อความธรรมดา เช่น ตั้งชื่อตัวละคร
    public static String getStringInput() {
        return scanner.nextLine().trim();
    }
}