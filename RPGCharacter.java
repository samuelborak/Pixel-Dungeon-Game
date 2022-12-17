public class RPGCharacter
{
   private int hp;
   private int mp;
   private int attack;
   private int defense;
   private int special;
   private boolean defeated;
   public RPGCharacter()
   {
      hp = 75;
      mp = 50;
      attack = 30;
      defense = 75;
      special = 100;
      defeated = false;
   }
   public boolean isDefeated()
   {
      return defeated;
   }
   public void isNowDefeated()
   {
      defeated = true;
   }
   public int getMP()
   {
      return mp;
   }
   public int getHP()
   {
      return hp;
   }
   public void subtractHP(int n)
   {
      hp = hp-n;
   }
   public void addHP(int n)
   {
      hp = hp+n;
   }
   public void addMP(int n)
   {
      mp = mp+n;
   }
   public void consumeMP()
   {
      mp = mp-10;
   } 
   public void consumeSpecialMP()
   {
      mp = mp-20;
   }
   public int getAttack()
   {
      return attack;
   }
   public int getDefense()
   {
      return defense;
   }
   public void addAttack(int n)
   {
      attack = attack+n;
   }
   public void addDefense(int n)
   {
      defense = defense+n;
   }  
   /**
    * This attacks other using a damage formula based on this attack and defense power.
    * If the attack causes other's HP to drop to 0 or below, other is defeated.
    */
   public void attack(RPGEnemy other)
   {
      System.out.println("You" + " attack " + "the enemy" + "!");
      int damage = (int)(this.attack / (1000.0 /(1000 + defense)));  //Formula sourced from: http://rpg.wikia.com/wiki/Damage_Formula
      System.out.println("Does " + damage + " damage!");
      other.subtractHP(damage);
      if(other.getHP() <= 0){
         other.isNowDefeated();
         System.out.println("The enemy" + " is defeated!"); 
      }
   }
   public void specialAttack(RPGEnemy other)
   {
      int miss;
      if (this.mp >= 20)
      {
         this.consumeSpecialMP();
         System.out.println("You special attack the enemy!");
         int damage = (int)(this.special / (1000.0 /(1000 + other.getDefense())));
         miss = (int)(Math.random()*100);
         if(miss<20)
         {
            damage = 0;
            other.subtractHP(damage);
            System.out.println("The special attack missed!");
         }
         else{
            System.out.println("Does " + damage + " damage!");
            other.subtractHP(damage);
            if(other.getHP() <= 0){
               other.isNowDefeated();
               System.out.println("The enemy" + " is defeated!");
            }
         }
      }      
      else{
         System.out.println("Not enough MP!");
      }
   }
   /**
    * This heals other if this RPGCharacter's MP points are high enough.
    * Warriors are healed by 70.
    * Mages are healed by 30.
    */
   public void heal(RPGCharacter other){
      System.out.println("You heal yourself!");
      //Does this have sufficient MP?
      if(this.mp >= 10){
         this.consumeMP();
         int healPoints;
         //What is other's class?
         healPoints = 30;
         hp = hp + healPoints;
         System.out.println("You heal 30 HP!");
      }
      else{
         System.out.println("Not enough MP!");
      }
   }
   public String toString()
   {
      return "Your Stats:\n HP: "+hp+"    MP: "+mp+"\n Attack:"+attack+"   Special: "+special+"    Defense:"+defense+"";
   }
}