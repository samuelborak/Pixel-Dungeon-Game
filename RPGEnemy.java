public class RPGEnemy
{
   private int hp;
   private int mp;
   private int attack;
   private int defense;
   private boolean defeated;
   private boolean isAlone;   //is true when the enemy is alone
   private int num;
   public RPGEnemy(int x, boolean y)
   {
      hp = (int)(Math.random()*(49))+51;
      mp = 50;
      attack = (int)(Math.random()*(34))+1;
      defense =(int) (Math.random()*(24))+26;
      defeated = false;
      num = x;
      isAlone = y;
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
   public int getDefense()
   {
      return defense;
   }
   public void subtractHP(int n)
   {
      hp = hp-n;
   }
   public void addHP(int n)
   {
      hp = hp+n;
   }
   public void consumeMP()
   {
      mp = mp-10;
   } 
   public void consumeSpecialMP()
   {
      mp = mp-20;
   } 
   /**
    * This attacks other using a damage formula based on this attack and defense power.
    * If the attack causes other's HP to drop to 0 or below, other is defeated.
    */
   public void attack (RPGCharacter other){
      System.out.println("Enemy #"+num+"" + " attacks " + "You" + "!");
      int damage = (int)(this.attack / (1000.0 /(1000 + other.getDefense())));  //Formula sourced from: http://rpg.wikia.com/wiki/Damage_Formula
      System.out.println("Does " + damage + " damage!");
      other.subtractHP(damage);
      if(other.getHP() <= 0){
         other.isNowDefeated();
         System.out.println("You" + " have been defeated!"); 
      }
   }
   /**
    * This heals other if this RPGCharacter's MP points are high enough.
    * Warriors are healed by 70.
    * Mages are healed by 30.
    */
   public void heal(RPGEnemy other){
      //Is this healing oneself?
      if (this != other){
         System.out.println("Enemy #"+num+"" + " heals themselves!");
         if (this.mp >= 10){
            this.consumeMP();
            int healPoints;
            healPoints = 30;
            hp = hp+ healPoints;
         }
         else
            System.out.println("Not enough MP!");
      }
      else{
         System.out.println("Enemy #"+num+"" + " heals " + "their ally!");
      //Does this have sufficient MP?
         if (this.mp >= 10){
            this.consumeMP();
            int healPoints;
            healPoints = 30;
         //Only heal if other was not previously defeated
            if (other.isDefeated() == true || isAlone == true){
               System.out.println("There is no ally to heal!");
            }
            else{
               other.addHP(healPoints);
               System.out.println("Enemy #"+num+"'s ally" + "'s HP is increased by " + healPoints);
            }
         }
         else{
            System.out.println("Not enough MP!");
         }
      }
   }
   public String toString()
   {
      return "Enemy #"+num+" stats: \n HP: "+hp+"    MP: "+mp+"\n Attack:"+attack+"    Defense:"+defense+"";
   }
}