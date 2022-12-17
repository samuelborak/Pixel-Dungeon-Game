import java.util.Scanner;

public class RPGBattle{
   private RPGCharacter hero;
   private RPGCharacter monster1;
   private RPGCharacter monster2;
   private RPGCharacter monster3;

   /**
    * Instantiates each of the three monsters as either a warrior or a mage.
    */
   private void createMonsters(){
      int classType = (int)(Math.random() * 2) + 1;
      if (classType == 1){
         monster1 = new RPGCharacter("Scar", 1);
      }
      else{
         monster1 = new RPGCharacter("Jafar", 2);
      }
      classType = (int)(Math.random() * 2) + 1;
      if (classType == 1){
         monster2 = new RPGCharacter("Shere Khan", 1);
      }
      else{
         monster2 = new RPGCharacter("Queen Grimhilde", 2);
      }
      classType = (int)(Math.random() * 2) + 1;
      if (classType == 1){
         monster3 = new RPGCharacter("Shan-Yu", 1);
      }
      else{
         monster3 = new RPGCharacter("Maleficent", 2);
      }
   }
   
   /**
    * Show the stats of all non-defeated characters
    */
   private void showStats(){
      System.out.println(hero);
      if(!monster1.isDefeated()){
         System.out.println(monster1);
      }
      if(!monster2.isDefeated()){
         System.out.println(monster2);
      }
      if(!monster3.isDefeated()){
         System.out.println(monster3);
      }
   }
   
   /**
    * Show action options
    */
   private void showMenu(){
      System.out.println("Enter 1 to attack");
      System.out.println("Enter 2 to heal (costs 10MP)");
      System.out.println("Enter 3 to perform a special attack (costs 20MP)(has a 20% chance to miss)");
   }
   
   /**
    * Prompts hero through attacking a monster
    */
   private void attackChoice(Scanner in){
      while(true){
         System.out.println("Which monster would you like to attack?");
         boolean monster1Alive = !monster1.isDefeated();
         boolean monster2Alive = !monster2.isDefeated();
         boolean monster3Alive = !monster3.isDefeated();
         if(monster1Alive){
            System.out.println("Enter 1 to attack " + monster1.getName());
         }
         if(monster2Alive){
            System.out.println("Enter 2 to attack " + monster2.getName());
         }
         if(monster3Alive){
            System.out.println("Enter 3 to attack " + monster3.getName());
         }
         int choice = 0;
         try{
            choice = in.nextInt();
         }
         catch(Exception e){
            System.out.println("Invalid input");
            in.nextLine();
         }
         if (monster1Alive && choice == 1){
            hero.attack(monster1);
            break;
         }
         else if (monster2Alive && choice == 2){
            hero.attack(monster2);
            break;
         }
         else if (monster3Alive && choice == 3){
            hero.attack(monster3);
            break;
         }
      }
   }
   
   private void specialChoice(Scanner in){
      while(true){
         System.out.println("Which monster would you like to attack?");
         boolean monster1Alive = !monster1.isDefeated();
         boolean monster2Alive = !monster2.isDefeated();
         boolean monster3Alive = !monster3.isDefeated();
         if(monster1Alive){
            System.out.println("Enter 1 to special attack " + monster1.getName());
         }
         if(monster2Alive){
            System.out.println("Enter 2 to special attack " + monster2.getName());
         }
         if(monster3Alive){
            System.out.println("Enter 3 to special attack " + monster3.getName());
         }
         int choice = 0;
         try{
            choice = in.nextInt();
         }
         catch(Exception e){
            System.out.println("Invalid input");
            in.nextLine();
         }
         if (monster1Alive && choice == 1){
            hero.specialAttack(monster1);
            break;
         }
         else if (monster2Alive && choice == 2){
            hero.specialAttack(monster2);
            break;
         }
         else if (monster3Alive && choice == 3){
            hero.specialAttack(monster3);
            break;
         }
      }
   }

   /**
    * Runs through the action sequence for a single monster to
    * take action (attack or heal).
    */
   private void singleMonsterAction(RPGCharacter monster){
      boolean chooseAttack = true;
      boolean chooseHeal = false;
      //50% chance to heal if MP high enough
      if (monster.getMP() >= 10 && Math.random() < 0.5){
         chooseAttack = false;
         chooseHeal = true;
      }
      //10% chance to erroneously heal            
      else if (Math.random() < 0.1){
         chooseAttack = false;
         chooseHeal = true;
      }
      if (chooseHeal){
         int target = (int)(Math.random() * 3) + 1;
         if (target == 1){
            monster.heal(monster1);
         }
         else if (target == 2){
            monster.heal(monster2);
         }
         else{
            monster.heal(monster3);
         }
      }
      else{
         monster.attack(hero);
      }
   }
      
  /**
   * Allows each non-defeated monster to attack or heal.
   */      
   private void monsterActionSequence(){
      boolean monster1Alive = !monster1.isDefeated();
      boolean monster2Alive = !monster2.isDefeated();
      boolean monster3Alive = !monster3.isDefeated();
      if (monster1Alive){
         singleMonsterAction(monster1);
      }
      if (monster2Alive){
         singleMonsterAction(monster2);
      }
      if (monster3Alive){
         singleMonsterAction(monster3);
      }
   }
   
   /**
    * Controls the battle sequence
    */
   private void battle(Scanner in){
      boolean gamePlaying = true;
      while (gamePlaying){
         showStats();
         int choice = 0;
         while (true){
            try{
               showMenu();
               choice = in.nextInt();
            }
            catch(Exception e){
               System.out.println("Invalid input");
               in.nextLine();
            }
            if (choice == 1){
               attackChoice(in);
               break;
            }
            else if (choice == 2){
               hero.heal(hero);
               break;
            }
            else if(choice == 3){
               specialChoice(in);
               break;
            }
         }
         monsterActionSequence();
         if (hero.isDefeated()){
            System.out.println("Monsters win!");
            gamePlaying = false;
         }
         else if (monster1.isDefeated() && monster2.isDefeated() 
                  && monster3.isDefeated()){
            System.out.println(hero.getName() + " wins!");
            gamePlaying = false;
         }
      }
   }
   
   /**
    * Allows user to create the hero, calls method to create monsters,
    * then calls method to begin battle.
    */
   private void start(){
      int classChoice = 0;
      Scanner in = new Scanner(System.in);
      System.out.println("Enter a name for your hero");
      String name = in.nextLine();
      //test for valid integer input for character class
      while (true){
         try{
            System.out.println("Enter 1 to play as a warrior, 2 to play as a mage.");
            classChoice = in.nextInt();
         }
         catch(Exception e){
            System.out.println("Invalid input");
            in.nextLine();
         }
         if (classChoice == 1){
            hero = new RPGCharacter(name, 1);
            break;
         }
         else if (classChoice == 2){
            hero = new RPGCharacter(name, 2);
            break;
         }
      }
      createMonsters();
      battle(in);
   }

   /**
    * Game starts here.
    */
   public static void main(String[] args){
      RPGBattle rpg = new RPGBattle();
      rpg.start();
   }
}