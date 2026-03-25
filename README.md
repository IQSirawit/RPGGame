# 🐲 Legend of the Dragon Orb (Java OOP Text-based RPG)

## 📌 คำอธิบายโปรเจกต์ (Project Description)
โปรเจกต์นี้เป็นเกม RPG แบบข้อความ (Text-based RPG) ที่พัฒนาด้วยภาษา Java เพื่อแสดงถึงความเข้าใจและการประยุกต์ใช้หลักการเขียนโปรแกรมเชิงวัตถุ (Object-Oriented Programming - OOP) เช่น Class, Inheritance, Interface, Polymorphism (Overriding & Overloading) พร้อมทั้งมีระบบการจัดการข้อมูลไฟล์ (Save/Load) และการดักจับข้อผิดพลาดของข้อมูลนำเข้า (Input Validation)

## 🚀 ฟีเจอร์หลัก (Key Features)
* **ระบบสายอาชีพ (Classes):** มีตัวละคร 4 สายอาชีพ (Warrior, Archer, Mage, Healer) ที่มีสเตตัสและสกิลแตกต่างกัน
* **ระบบต่อสู้ (Combat System):** ต่อสู้แบบ Turn-based มีระบบโจมตีปกติ, สกิลติดคูลดาวน์, การคำนวณดาเมจและเกราะป้องกัน
* **ระบบสิ่งกีดขวาง (Destructible Objects):** สามารถโจมตีทำลายสิ่งกีดขวางในแผนที่ เช่น หีบสมบัติ (Wooden/Golden Box), ประตู (Castle Gate) เพื่อรับรางวัล
* **ระบบกระเป๋าและไอเทม (Inventory & Consumables):** สามารถซื้อ, เก็บ, ซ้อนทับ (Stack), และใช้งานไอเทมฟื้นฟู (Health/Mana Potion) 
* **ระบบบันทึกและโหลดเกม (Save/Load System):** บันทึกสถานะของตัวละคร, เงิน, และไอเทมในกระเป๋าลงในไฟล์ Text และโหลดกลับมาเล่นต่อได้
* **ระบบจัดการ Input:** ป้องกันโปรแกรมค้างหรือ Error จากการพิมพ์ผิดด้วยการทำ Input Validation (`try-catch` กับ `NumberFormatException`)

## 🛠️ โครงสร้างหลักและ OOP (OOP Structure)
โปรเจกต์นี้ได้นำหลักการ OOP มาประยุกต์ใช้อย่างครบถ้วน ดังนี้:
* **Classes & Objects:** มีการสร้างคลาสแยกตามหมวดหมู่ชัดเจน เช่น `Character`, `Weapon`, `Party`, `Location`
* **Inheritance:** คลาสสายอาชีพ (เช่น `Warrior`, `Mage`) สืบทอดคุณสมบัติมาจากคลาสหลัก `Character`
* **Interface:** มีการใช้ Interface เพื่อกำหนดพฤติกรรม เช่น:
  * `Destructible`: สำหรับสิ่งที่ถูกโจมตีได้ (ทั้งตัวละครและสิ่งกีดขวาง)
  * `Consumable`: สำหรับไอเทมที่กดใช้งานได้
  * `Stackable`: สำหรับไอเทมที่รวมจำนวนช่องเดียวกันได้
* **Polymorphism (Overriding):** คลาสลูกมีการเขียนทับเมธอดของคลาสแม่ เช่น `takeDamage()` ใน `Warrior` ที่มีการคำนวณหักลบค่าเกราะ (Armor) 
* **Polymorphism (Overloading):** คลาส `Party` มี Constructor 2 แบบ (แบบที่กำหนดเงินเริ่มต้น และแบบที่ไม่กำหนด)
* **Design Pattern (Bonus):** มีการใช้ Decorator Pattern ในการออกแบบระบบ Skills และ Attributes เสริมของการโจมตี (เช่น `HealDecorator`, `AoEDecorator`)

## 🎮 สถานการณ์ใช้งานจริง (Use Cases)
ตัวเกมรองรับ Use Cases หลัก 5 กรณี ดังนี้:
1. **การสำรวจและต่อสู้ (Explore & Battle):** ผู้เล่นเดินทางไปตามสถานที่ต่างๆ จัดการมอนสเตอร์ หรือทำลายสิ่งกีดขวางในดันเจี้ยน
2. **การจัดการกระเป๋า (Inventory Management):** ผู้เล่นเปิดดูกระเป๋า และเลือกใช้งานไอเทมฟื้นฟูพลังชีวิต/มานาให้กับตัวละครที่เลือก
3. **การซื้อของในร้านค้า (Shop):** ผู้เล่นใช้เงินที่ดรอปจากมอนสเตอร์เพื่อซื้อยาหรือ Key Item (Dragon Orb)
4. **การฟื้นฟูสถานะ (Resting):** ผู้เล่นสามารถพักผ่อนที่ Inn (เสียเงิน) เพื่อฟื้นฟู HP/MP เต็ม หรือพักที่ Campfire (ฟรี) เพื่อฟื้นฟูบางส่วน
5. **การบันทึกและโหลดข้อมูล (Save & Load):** ผู้เล่นบันทึกความคืบหน้าของเกมลงไฟล์ และสามารถโหลดไฟล์เดิมกลับมาเล่นใหม่ได้แม้จะปิดโปรแกรมไปแล้ว หรือโหลดเมื่อปาร์ตี้ตาย (Game Over)

## ▶️ วิธีการรันโปรแกรม (How to Run)
**ข้อกำหนดเบื้องต้น:** ต้องติดตั้ง Java Development Kit (JDK) เวอร์ชัน 17 ขึ้นไป

1. เปิด Terminal หรือ Command Prompt
2. นำทาง (cd) ไปยังโฟลเดอร์หลักของโปรเจกต์ (โฟลเดอร์ที่มีโฟลเดอร์ `RPGGame` อยู่ข้างใน)
3. พิมพ์คำสั่งสำหรับคอมไพล์โค้ดทั้งหมด:
   ```bash
   javac RPGGame/*.java RPGGame/*/*.java RPGGame/*/*/*.java
4. พิมพ์คำสั่งสำหรับรันโปรแกรม:
   ```bash
   java RPGGame.RPGGameApp
