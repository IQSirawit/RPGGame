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
   
## ผู้พัฒนา (Developer)
1. นาย สายกลาง จะวะนะ 682110198
2. นาย สิรวิชญ์ ยวงคำ 682110199
3. นาย ปัณณวิชญ์ สิทธิตัน 682110181
4. นาย ศุภวิชญ์ อ้ายเสาร์ 682110196

## 🛠️ โครงสร้างไฟล์และสถาปัตยกรรม OOP (File Structure & OOP Design)
``` text
 📦 RPGGame (Main Package)
  ┣ 📜 RPGGameApp.java       - คลาสหลักสำหรับรันโปรแกรม (Entry Point) รวม Game Loop [Main Class]
  ┣ 📜 Character.java        - ต้นแบบของตัวละครทุกตัว มีสเตตัสและเมธอดพื้นฐาน [Base Class / implements Destructible]
  ┣ 📜 Party.java            - ระบบปาร์ตี้ที่เก็บรวบรวมตัวละครและกระเป๋าไอเทมเข้าด้วยกัน [Composition]
  ┣ 📜 Weapon.java           - คลาสจัดการอาวุธและดาเมจพื้นฐาน [Base Class]
  ┣ 📜 Inventory.java        - คลาสกระเป๋าสำหรับเก็บและจัดการไอเทม [Composition]
  ┣ 📜 BattleManager.java    - ระบบจัดการลูปการต่อสู้ (Turn-based) และคำนวณเทิร์น [Manager Class]
  ┣ 📜 BattleInfo.java       - คลาสเก็บสถานะและข้อมูลปาร์ตี้ระหว่างต่อสู้ [Static Utility]
  ┣ 📜 InputHandler.java     - ระบบรับค่าและตรวจสอบข้อมูลนำเข้า (Input Validation) [Utility Class]
  ┣ 📜 Destructible.java     - กำหนดพฤติกรรมสิ่งที่ถูกโจมตีได้และมี HP [Interface]
  ┣ 📜 Consumable.java       - กำหนดพฤติกรรมของไอเทมที่กดใช้งานได้ [Interface]
  ┣ 📜 Attack.java           - กำหนดรูปแบบการโจมตี [Interface]
  ┣ 📜 BaseAttack.java       - การโจมตีพื้นฐาน [Concrete Class / implements Attack]
  ┗ 📜 SupportAbility.java   - สกิลสนับสนุนสำหรับ Healer [Concrete Class / implements Attack]
 
  ┣ 📂 CharClass (หมวดหมู่อาชีพตัวละคร)
  ┃ ┣ 📜 Warrior.java        - อาชีพนักรบ มีระบบเกราะ (Armor) [Subclass ของ Character]
  ┃ ┣ 📜 Mage.java           - อาชีพนักเวท มีระบบมานา (Mana) [Subclass ของ Character]
  ┃ ┣ 📜 Archer.java         - อาชีพนักธนู มีระบบความแม่นยำและการหลบเข้าที่กำบัง [Subclass ของ Character]
  ┃ ┗ 📜 Healer.java         - อาชีพสายซัพพอร์ต เน้นสกิลรักษา [Subclass ของ Character]
 
  ┣ 📂 Location (หมวดหมู่สถานที่ในเกม)
  ┃ ┣ 📜 Location.java       - ต้นแบบสถานที่ บังคับให้คลาสลูกต้องมีเมธอด enter() [Abstract Class]
  ┃ ┣ 📜 Inn.java            - โรงแรมสำหรับจ่ายเงินฟื้นฟู HP/MP [Subclass ของ Location]
  ┃ ┣ 📜 Shop.java           - ร้านค้าสำหรับซื้อไอเทม [Subclass ของ Location]
  ┃ ┣ 📜 Forest.java         - ป่าสำหรับต่อสู้กับมอนสเตอร์ [Subclass ของ Location]
  ┃ ┣ 📜 Dungeon.java        - ดันเจี้ยนตะลุยศัตรูเป็น Wave และมีสิ่งกีดขวาง [Subclass ของ Location]
  ┃ ┗ 📜 DragonCave.java     - ถ้ำบอสใหญ่ ตรวจสอบเงื่อนไขไอเทมก่อนเข้า [Subclass ของ Location]
 
  ┣ 📂 Item (หมวดหมู่ไอเทม)
  ┃ ┣ 📜 Item.java           - ต้นแบบไอเทมทั้งหมด มีการบังคับใช้ displayDetails() [Abstract Class]
  ┃ ┣ 📜 Stackable.java      - กำหนดพฤติกรรมไอเทมที่ซ้อนทับกันได้ [Interface]
  ┃ ┣ 📜 HealthPotion.java   - ยาฟื้นฟูเลือด [Subclass / implements Consumable, Stackable]
  ┃ ┣ 📜 ManaPotion.java     - ยาฟื้นฟูมานา [Subclass / implements Consumable, Stackable]
  ┃ ┗ 📜 DragonOrb.java      - ไอเทมสำคัญ (Key Item) สำหรับจบเกม [Subclass ของ Item]
 
  ┣ 📂 DestructibleObject (หมวดหมู่สิ่งกีดขวาง)
  ┃ ┣ 📜 WoodenBox.java      - กล่องไม้ธรรมดา ตีแตกแล้วสุ่มดรอปเงิน [Concrete Class / implements Destructible]
  ┃ ┣ 📜 GoldenBox.java      - กล่องทองคำ ดรอปเงินเยอะและมีโอกาสดรอป Dragon Orb [Subclass ของ WoodenBox]
  ┃ ┣ 📜 CastleGate.java     - ประตูเหล็ก มีระบบลดดาเมจ (Reinforced) [Concrete Class / implements Destructible]
  ┃ ┗ 📜 StoneWall.java      - กำแพงหินกีดขวาง [Concrete Class / implements Destructible]
 
  ┣ 📂 Save (หมวดหมู่การจัดการไฟล์)
  ┃ ┗ 📜 SaveManager.java    - ระบบอ่าน/เขียนไฟล์ (I/O) เพื่อบันทึกและโหลดเกม [Utility Class]
 
  ┗ 📂 Decorator (หมวดหมู่การดัดแปลงสกิล - Design Pattern)
    ┣ 📜 AttackDecorator.java - ต้นแบบระบบดัดแปลงการโจมตี [Abstract Class / implements Attack]
    ┣ 📜 AoEDecorator.java    - ดัดแปลงสกิลให้โจมตีหมู่ [Subclass ของ AttackDecorator]
    ┣ 📜 HealDecorator.java   - ดัดแปลงสกิลให้เป็นการฮีล [Subclass ของ AttackDecorator]
    ┗ 📜 ... (และ Decorator อื่นๆ เช่น เพิ่มดาเมจ, เจาะเกราะ, ดูดเลือด)
