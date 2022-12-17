import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.io.*;
import java.util.*;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
	
public class MazeGrid extends JPanel
{	
	//images to use for the board
   private ImageIcon blank = new ImageIcon("images/blank.JPG"); //the exit for lower levels
   private ImageIcon blank2 = new ImageIcon("images/blank2.png"); //the floor for higher levels
   private ImageIcon white = new ImageIcon("images/white.jpg"); //the exit for lower levels
   private ImageIcon white2 = new ImageIcon("images/white2.png"); //the exit for higher levels
   private ImageIcon wall = new ImageIcon("images/brick.png"); //the wall for higher levels
   private ImageIcon wall2 = new ImageIcon("images/brick2.png"); //the wall for lower levels
   private ImageIcon black = new ImageIcon("images/black.png"); //unexplored tile
   private ImageIcon event = new ImageIcon("images/event.png"); //event tile for upper floors
   private ImageIcon event2 = new ImageIcon("images/event2.png"); //event tile for lower floors

   private static final int SIZE=40;	//size of cell being drawn
 
   private static final int DELAY=10;	//#miliseconds delay between each time the enemy moves and screen refreshes for the timer
   private static final int SPEED=1;	//when the crosshair moves from one cell to the next, this is how many pixels they move in each frame (designated by the timer)
   private static final int ANIMATION_DELAY = 10;	//the speed at which the animations occur for the players
   //This array will be represented graphically on the screen
   private static Floortile[][][] board;			//we will fill with Floortiles
  
   private int floor;
   private Player player;
   
   private Point[] startPos;
   
      
   private static final int UP = 0;		//movement directions to use as index for moveDir array
   private static final int RIGHT = 1;
   private static final int DOWN = 2;
   private static final int LEFT = 3;
   
   private Timer t;							//used to set the speed of the enemy that moves around the screen
   
   private RPGEnemy enemy1; //used for the Battles with enemies
   private RPGEnemy enemy2;
   
   private RPGCharacter hero = new RPGCharacter(); //used to represent the player
   
   public static Scanner input = new Scanner(System.in);
   
   public MazeGrid()
   {
      int numRows = 20;		//array has a size of 20 by 20 and is 5 florrs tall
      int numColumns = 20;
      int numFloors = 5;
      startPos = new Point[numFloors];
      floor = 0;
      board = new Floortile[numRows][numColumns][numFloors];
      for(int r=0;r<board.length;r++)					//fill the board with walls
      {
         for(int c=0;c<board[0].length;c++)
         {
            for(int f = 0; f < numFloors; f++)
            {
               Floortile x = new Floortile();
               board[r][c][f] = x;
            }
         }
      }
      for(int f = 0; f<numFloors; f++)
      {
         //set the starting position to visited for the maze which will malke a floor tile
         if(f < 2) //if the maze is the first in the stack
         {
            generate(10, 10, f);
            generate(10, 10, f);
         }
         else if(f < 4)
         {
            generate(10, 10, f);
            generate(10, 10, f);
            generate(10, 10, f);
         }
         else
         {
            generate(10, 10, f);
            generate(10, 10, f);
            generate(10, 10, f);
            generate(10, 10, f);  
         }
         for(int r=0;r<board.length;r++)					//fill with random values (0,1)
         {
            for(int c=0;c<board[0].length;c++)
            {
            
               if(r == 0) //the edges are turned into walls always
               {
                  board[r][c][f].setWall(true);
                  continue;
               }  
               else if(r == board.length-1) //the edges are turned into walls always
               {
                  board[r][c][f].setWall(true);
                  continue;
               }
               else if(c == 0) //the edges are turned into walls always
               {
                  board[r][c][f].setWall(true);
                  continue;
               }
               else if(c == board[0].length-1) //the edges always visited so the maze doesn't go out of bounds
               {
                  board[r][c][f].setWall(true);
                  continue;
               }
            }
         }
         for(int r=0;r<board.length;r++)					//fill the board with event squares
         {
            for(int c=0;c<board[0].length;c++)
            {
               for(int l = 0; l < numFloors; l++)
               {
                  int random = 10;
                  if(floor ==0)
                  {
                     random = (int)(Math.random()*(26));
                  }
                  else if(floor == 1)
                  {
                     random = (int)(Math.random()*(26));
                  }
                  else if(floor == 2)
                  {
                     random = (int)(Math.random()*(26));
                  }
                  else if(floor == 3)
                  {
                     random = (int)(Math.random()*(26));
                  }
                  else if(floor == 4)
                  {
                     random = (int)(Math.random()*(26));
                  }
                  if(random == 0 && board[r][c][l].isAWall() == false)
                  {
                     board[r][c][l].nowEvent(true);
                  }
               }
            }
         }
         int startPositionX = (int)((Math.random()*(board.length-2)) +1); //the starting position of the player in the x coordinate
         int startPositionY = (int)((Math.random()*(board[0].length-2)) +1); //the starting position of the player in the y coordinate;
         while(board[startPositionX][startPositionY][f].isAWall())
         {
            startPositionX = (int)((Math.random()*(board.length-2)) +1); //the starting position of the player in the x coordinate
            startPositionY = (int)((Math.random()*(board[0].length-2)) +1); //the starting position of the player in the y coordinate;
         }
         board[startPositionX][startPositionY][f].nowEvent(false);
         startPos[f] = new Point(startPositionX,startPositionY);
         int exitPositionX = (int)((Math.random()*(board.length-2)) +1); //the exit position of the maze in the x coordinate
         int exitPositionY = (int)((Math.random()*(board[0].length-2)) +1); //the exit position of the maze in the y coordinate
         while(board[exitPositionX][exitPositionY][f].isAWall())
         {
            exitPositionX = (int)((Math.random()*(board.length-2)) +1); //the exit position of the maze in the x coordinate is set to a random non-wall tile
            exitPositionY = (int)((Math.random()*(board[0].length-2)) +1); //the exit position of the maze in the y coordinate is set to a random non-wall tile
         }
         board[exitPositionX][exitPositionY][f].nowEvent(false);
         board[exitPositionX][exitPositionY][f].nowExit(true);
      }
      String[][] playerImages = new String[1][1]; //shows the animation of the player character
      playerImages[0][0] = "images/player.png";
      int rr = 0;
      int rc = 0;
      while(board[rr][rc][floor].isAWall() == true)
      {
         rr = (int)((Math.random()*board.length-1) +1);
         rc = (int)((Math.random()*board[0].length-1) +1);
      }
      player = new Player("US",  rr, rc, playerImages, ANIMATION_DELAY);
      board[rr][rc][0].nowVisited(true);
      Sound.initialize();
      t = new Timer(DELAY, new Listener());			//the higher the value of DELAY, the slower the enemy will move
      t.start();
   
   }
   
   public void generate(int startPositionX, int startPositionY, int f)
   {
       //Code taken from https://algs4.cs.princeton.edu/41graph/Maze.java.html
      while(startPositionX+1 <= board.length-1 && startPositionY+1 <= board[0].length-1 && startPositionY-1 >= 1 && startPositionX-1 >= 1)  // while there is an unvisited neighbor and the neighbors aren't out of bounds
      {
            //pick random neighbor and sets the current tile to a blank and the random neighbor tile to a blank then recursively calls again in that direction
         double r = (int)(Math.random()*4);
         if(r == 0 && startPositionY+1 <= board[0].length-1) {
            board[startPositionX][startPositionY][f].setWall(false);
            board[startPositionX][startPositionY+1][f].setWall(false);
            generate(startPositionX, startPositionY + 1, f);
            break;
         }
         else if(r == 1 && startPositionX+1 <= board.length-1) {
            board[startPositionX][startPositionY][f].setWall(false);
            board[startPositionX+1][startPositionY][f].setWall(false);
            generate(startPositionX+1, startPositionY, f);
            break;
         }
         else if(r == 2 && startPositionY-1 >= 1) {
            board[startPositionX][startPositionY][f].setWall(false);
            board[startPositionX][startPositionY-1][f].setWall(false);
            generate(startPositionX, startPositionY-1, f);
            break;
         }
         else if(r == 3 && startPositionX-1 >= 1) {
            board[startPositionX][startPositionY][f].setWall(false);
            board[startPositionX-1][startPositionY][f].setWall(false);
            generate(startPositionX-1, startPositionY, f);
            break;
         }
      } 
   }
   
	//post:  shows different pictures on the screen in grid format depending on the values stored in the array board
	//			0-blank, 1-white, 2-black and gives priority to drawing the player
   public void showBoard(Graphics g)	
   {
      int x =0, y=0;		//upper left corner location of where image will be drawn
      int tempX = 0, tempY = 0;			//save locations for graphic position of where the player is to be used to draw the player in motion when transitioning from one cell to another
      for(int r=0;r<board.length;r++)
      {
         x = 0;						//reset the row distance
         for(int c=0;c<board[0].length;c++)
         {
            //for(int f = 0; f<floor; f++)
            {
               if(board[r][c][floor].isAnExit())
                  board[r][c][floor].nowEvent(false);
               if(board[r][c][floor].isAnExit() == true && board[r][c][floor].isVisited() == true && floor < 3)
                  g.drawImage(white.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               else if(board[r][c][floor].isAnExit() == true && board[r][c][floor].isVisited() == true && floor >= 3) //different images for lower floors
                  g.drawImage(white2.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               
               else if(board[r][c][floor].isEvent() == true && board[r][c][floor].isVisited() == true && board[r][c][floor].isDone() == false && floor < 3)
                  g.drawImage(blank.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               else if(board[r][c][floor].isEvent() == true && board[r][c][floor].isVisited() == true && board[r][c][floor].isDone() == false && floor >= 3) //different images for lower floors
                  g.drawImage(blank2.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               
               else if(board[r][c][floor].isAWall() == false && board[r][c][floor].isVisited() == true && floor < 3 || board[r][c][floor].isEvent() == true && board[r][c][floor].isVisited() == true && board[r][c][floor].isDone() == false && floor < 3)
                  g.drawImage(blank.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               else if(board[r][c][floor].isAWall() == false && board[r][c][floor].isVisited() == true && floor >= 3 || board[r][c][floor].isEvent() == true && board[r][c][floor].isVisited() == true && board[r][c][floor].isDone() == false && floor >= 3) //different images for lower floors
                  g.drawImage(blank2.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               
               else if(board[r][c][floor].isAWall() == true && floor < 3)
                  g.drawImage(wall.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               else if(board[r][c][floor].isAWall() == true && floor >= 3) //different images for lower floors
                  g.drawImage(wall2.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               
               else
                  g.drawImage(black.getImage(), x, y, SIZE, SIZE, null);  //scaled image
               x+=SIZE;
            }
         }
         y+=SIZE;
      }
      g.drawImage(player.getPictureAndAdvance().getImage(), player.findX(SIZE), player.findY(SIZE), SIZE, SIZE, null);  //scaled image
   
   }
	//pre:  all arguments are valid pixel coordinates >= 1 && <= screen size
	//post: returns the distance between the two points to see if there is a collision between players
   public static double distance(int x1, int y1, int x2, int y2)
   {
      return Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));
   }

//THIS METHOD IS ONLY CALLED THE MOMENT A KEY IS HIT - NOT AT ANY OTHER TIME
	//pre:   k is a valid keyCode
	//post:  changes the players position depending on the key that was pressed (sent from the driver)
	//keeps the player in the bounds of the size of the array board, then the enemy moves
   public void processUserInput(int k)
   {
      if((k==KeyEvent.VK_Q || k==KeyEvent.VK_ESCAPE) && board[player.getRow()-1][player.getCol()][floor].isEvent() == false && board[player.getRow()+1][player.getCol()][floor].isEvent() == false && board[player.getRow()][player.getCol()+1][floor].isEvent() == false && board[player.getRow()][player.getCol()-1][floor].isEvent() == false)					//End the program	
      {
         System.out.println("Good Bye!");
         System.exit(1);
      }
      //cancel move order if we are already moving from one space to the next
      if(player.isMoving())
         return;
      player.clearDirections();
      player.setMoveIncrX(0);
      player.setMoveIncrY(0);
      if(k==KeyEvent.VK_UP && player.getRow()>0 && board[player.getRow()-1][player.getCol()][floor].isAWall() == false)
      {
         Sound.Movement();
         player.setDirection(UP);
         board[player.getRow()-1][player.getCol()][floor].nowVisited(true);
         board[player.getRow()-1+1][player.getCol()][floor].nowVisited(true);
         board[player.getRow()-1-1][player.getCol()][floor].nowVisited(true);
         board[player.getRow()-1][player.getCol()+1][floor].nowVisited(true);
         board[player.getRow()-1][player.getCol()-1][floor].nowVisited(true);
         if(board[player.getRow()-1][player.getCol()][floor].isEvent() == true)
         {
            Sound.silence(98);
            Sound.Battle();
            eventTriggered();
            board[player.getRow()-1][player.getCol()][floor].nowEvent(false);
         }         
      }
      else if(k==KeyEvent.VK_DOWN && player.getRow()<board.length-1 && board[player.getRow()+1][player.getCol()][floor].isAWall() == false)
      {
         Sound.Movement();
         player.setDirection(DOWN);
         board[player.getRow()+1][player.getCol()][floor].nowVisited(true);
         board[player.getRow()+1+1][player.getCol()][floor].nowVisited(true);
         board[player.getRow()+1-1][player.getCol()][floor].nowVisited(true);
         board[player.getRow()+1][player.getCol()+1][floor].nowVisited(true);
         board[player.getRow()+1][player.getCol()-1][floor].nowVisited(true);
         if(board[player.getRow()+1][player.getCol()][floor].isEvent() == true)
         {
            Sound.silence(98);
            Sound.Battle();
            eventTriggered();
            board[player.getRow()+1][player.getCol()][floor].nowEvent(false);
         }      
      }
      else if(k==KeyEvent.VK_LEFT && player.getCol()>0 && board[player.getRow()][player.getCol()-1][floor].isAWall() == false)
      {
         Sound.Movement();
         player.setDirection(LEFT);
         board[player.getRow()][player.getCol()-1][floor].nowVisited(true);
         board[player.getRow()-1][player.getCol()-1][floor].nowVisited(true);
         board[player.getRow()+1][player.getCol()-1][floor].nowVisited(true);
         board[player.getRow()][player.getCol()-1+1][floor].nowVisited(true);
         board[player.getRow()][player.getCol()-1-1][floor].nowVisited(true);
         if(board[player.getRow()][player.getCol()-1][floor].isEvent() == true)
         {
            Sound.silence(98);
            Sound.Battle();
            eventTriggered();
            board[player.getRow()][player.getCol()-1][floor].nowEvent(false);
         }    
      }
      else if(k==KeyEvent.VK_RIGHT && player.getCol()<board[0].length-1 && board[player.getRow()][player.getCol()+1][floor].isAWall() == false)
      {
         Sound.Movement();
         player.setDirection(RIGHT);
         board[player.getRow()][player.getCol()+1][floor].nowVisited(true);
         board[player.getRow()-1][player.getCol()+1][floor].nowVisited(true);
         board[player.getRow()+1][player.getCol()+1][floor].nowVisited(true);
         board[player.getRow()][player.getCol()+1+1][floor].nowVisited(true);
         board[player.getRow()][player.getCol()+1-1][floor].nowVisited(true);
         if(board[player.getRow()][player.getCol()+1][floor].isEvent() == true)
         {
            Sound.silence(98);
            Sound.Battle();
            eventTriggered();
            board[player.getRow()][player.getCol()+1][floor].nowEvent(false);
         } 
      }
      else if(k==KeyEvent.VK_ENTER && board[player.getRow()][player.getCol()][floor].isAnExit() == true && floor < 4)
      {
         floor++;
         Sound.changeLevel();
         player.setRow((int)startPos[floor].getX());
         player.setCol((int)startPos[floor].getY());
         board[(int)startPos[floor].getX()][(int)startPos[floor].getY()][floor].nowVisited(true);
      }
      else if(k==KeyEvent.VK_ENTER && board[player.getRow()][player.getCol()][floor].isAnExit() == true && floor == 4)
      {
         int score = hero.getHP()+hero.getMP()+hero.getAttack()+hero.getDefense();//add together all the stats of the player to get  score
         System.out.println("");
         System.out.println("You Win!!!");
         System.out.println("Your HP: "+hero.getHP()+" plus Your MP: "+hero.getMP()+" plus Your Attack: "+hero.getAttack()+" plus Your Defence: "+hero.getDefense()+" equals your score.");
         System.out.println("Your score is "+score+"");
         System.exit(1);
      }              
      repaint();			//refresh the screen
   }

   public void movePlayerSmoothly()
   {
      if(Math.abs(player.getMoveIncrX()) >= SIZE || Math.abs(player.getMoveIncrY()) >= SIZE)
      {
         player.setMoveIncrX(0);
         player.setMoveIncrY(0);
         if(player.isMovingUp() && player.getRow()>0 && board[player.getRow()-1][player.getCol()][floor].isAWall() == false)
            player.setRow(player.getRow()-1);
         else 
            if(player.isMovingDown() && player.getRow()<board.length-1 && board[player.getRow()+1][player.getCol()][floor].isAWall() == false)
               player.setRow(player.getRow()+1);
            else
               if(player.isMovingLeft() && player.getCol()>0 && board[player.getRow()][player.getCol()-1][floor].isAWall() == false)
                  player.setCol(player.getCol()-1);
               else 
                  if(player.isMovingRight() && player.getCol()<board[0].length-1 && board[player.getRow()][player.getCol()+1][floor].isAWall() == false)
                     player.setCol(player.getCol()+1);
         player.clearDirections();            
      }
      else
      {
         if(player.isMovingUp() && player.getRow()>0)
            player.addMoveIncrY(-1*SPEED);
         else 
            if(player.isMovingDown() && player.getRow()<board.length-1)
               player.addMoveIncrY(SPEED);
            else
               if(player.isMovingLeft() && player.getCol()>0)
                  player.addMoveIncrX(-1*SPEED);
               else 
                  if(player.isMovingRight() && player.getCol()<board[0].length-1)
                     player.addMoveIncrX(SPEED);
      }
   }   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      g.setColor(Color.blue);		//draw a blue boarder around the board
      g.fillRect(0, 0, (board[0].length*SIZE), (board.length*SIZE));
      showBoard(g);					//draw the contents of the array board on the screen
   }
   
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)	//this is called for each timer iteration - make the enemy move randomly
      {
         movePlayerSmoothly();
         repaint();
      }
   }
   public void eventTriggered() //called when the player steps on an event space
   {
      Scanner in = new Scanner(System.in);
      int chance = (int)(Math.random()*2)+1;
      enemy1 = new RPGEnemy(1,true);
      enemy2 = new RPGEnemy(2,true);
      if(chance == 1)
      {
         enemy1 = new RPGEnemy(1,true);
      }
      else
      {
         enemy1 = new RPGEnemy(1,false);
         enemy2 = new RPGEnemy(2,false);
      }
      battle(in, chance); 
   }   
   public void findReward() //randomly gives a reward to the player
   {
      int random = (int)(Math.random()*4);
      if(random == 0)
      {
         System.out.println("You found and drank a health potion! HP increased by 40!");
         hero.addHP(40);
      }
      else if(random == 1)
      {
         System.out.println("You found and drank a mana potion! MP increased by 10!");
         hero.addMP(10);
      }
      else if(random == 2)
      {
         System.out.println("You find some armor! Defence increased by 10!");
         hero.addDefense(10);
      }
      else if(random == 3)
      {
         System.out.println("You find a better weapon! Attack increased by 10!");
         hero.addAttack(10);
      }
   }
   
   
   
   
   
   
   
   
   
   
   
   
   //********************************************BATTLE SYSTEM***************************************************************************************
   
   /**
    * Show the stats of all non-defeated characters
    */
   private void showStats(int x){
      System.out.println(""+hero.toString()+"");
      if(enemy1.getHP() == 0)
      {
         enemy1.isNowDefeated();
      }
      if(enemy2.getHP() == 0)
      {
         enemy2.isNowDefeated();
      }
      if(x == 2)
      {
         if(!enemy1.isDefeated()){
            System.out.println(""+enemy1.toString()+"");
         }
         if(!enemy2.isDefeated()){
            System.out.println(""+enemy2.toString()+"");
         }
      }
      if(x == 1)
      {
         if(!enemy1.isDefeated()){
            System.out.println(""+enemy1.toString()+"");
         }
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
    * Prompts hero through attacking a enemy
    */
   private void attackChoice(Scanner in, int x){
      while(true){
         if(x == 2)
         {
            Sound.silence(94);
            System.out.println("Which enemy would you like to attack?");
            boolean enemy1Alive = !enemy1.isDefeated();
            boolean enemy2Alive = !enemy2.isDefeated();
            if(enemy1Alive){
               System.out.println("Enter 1 to attack " + "the first enemy");
            }
            if(enemy2Alive){
               System.out.println("Enter 2 to attack " + "the second enemy");
            }
            int choice = 0;
            try{
               choice = in.nextInt();
            }
            catch(Exception e){
               System.out.println("Invalid input");
               in.nextLine();
            }
            if (enemy1Alive && choice == 1){
               Sound.Attack();
               hero.attack(enemy1);
               break;
            }
            else if (enemy2Alive && choice == 2){
               Sound.Attack();
               hero.attack(enemy2);
               break;
            }
         }
         else
         {
            Sound.silence(94);
            boolean enemy1Alive = !enemy1.isDefeated();
            if(enemy1Alive){
               System.out.println("Enter 1 to attack " + "the first enemy");
            }
            int choice = 0;
            try{
               choice = in.nextInt();
            }
            catch(Exception e){
               System.out.println("Invalid input");
               in.nextLine();
            }
            if (enemy1Alive && choice == 1){
               Sound.Attack();
               hero.attack(enemy1);
               break;
            }  
         }
      }
   }
   //the player uses a special attack the does huge damage buyt has a chance to miss
   private void specialChoice(Scanner in, int x){
      while(true){
         if( x ==2)
         {
            Sound.silence(94);
            System.out.println("Which enemy would you like to attack?");
            boolean enemy1Alive = !enemy1.isDefeated();
            boolean enemy2Alive = !enemy2.isDefeated();
            if(enemy1Alive){
               System.out.println("Enter 1 to special attack " + "the first enemy");
            }
            if(enemy2Alive){
               System.out.println("Enter 2 to special attack " + "the second enemy");
            }
            int choice = 0;
            try{
               choice = in.nextInt();
            }
            catch(Exception e){
               System.out.println("Invalid input");
               in.nextLine();
            }
            if (enemy1Alive && choice == 1){
               Sound.Special();
               hero.specialAttack(enemy1);
               break;
            }
            else if (enemy2Alive && choice == 2){
               Sound.Special();
               hero.specialAttack(enemy2);
               break;
            }
         }
         
         else
         {
            Sound.silence(94);
            System.out.println("Enter 1 to special attack " + "the first enemy");
            int choice = 0;
            try{
               choice = in.nextInt();
            }
            catch(Exception e){
               System.out.println("Invalid input");
               in.nextLine();
            }
            if (choice == 1){
               Sound.Special();
               hero.specialAttack(enemy1);
               break;
            }
         }
      }
   }

   /**
    * Runs through the action sequence for a single enemy to
    * take action (attack or heal).
    */
   private void singleMonsterAction(RPGEnemy enemy){
      boolean chooseAttack = true;
      boolean chooseHeal = false;
      //50% chance to heal if MP high enough
      if (enemy.getMP() >= 10 && Math.random() < 0.5){
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
            Sound.Heal();
            enemy.heal(enemy1);
         }
         else if (target == 2){
            Sound.Heal();
            enemy.heal(enemy2);
         }
      }
      else{
         Sound.Enemy();
         enemy.attack(hero);
      }
   }
      
  /**
   * Allows each non-defeated enemy to attack or heal.
   */      
   private void enemyActionSequence(int x){
      boolean enemy1Alive = !enemy1.isDefeated();
      boolean enemy2Alive = !enemy2.isDefeated();
      if(x == 1)
      {
         if (enemy1Alive){
            singleMonsterAction(enemy1);
         }
      }
      if(x == 2)
      {
         if (enemy1Alive){
            singleMonsterAction(enemy1);
         }
         if (enemy2Alive){
            singleMonsterAction(enemy2);
         }
      }
   }
   
   /**
    * Controls the battle sequence
    */
   private void battle(Scanner in, int x){
      boolean gamePlaying = true;
      while (gamePlaying){
         showStats(x);
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
               
               attackChoice(in, x);
               break;
            }
            else if (choice == 2){
               Sound.silence(94);
               Sound.Heal();
               hero.heal(hero);
               break;
            }
            else if(choice == 3){
               specialChoice(in, x);
               break;
            }
         }
         enemyActionSequence(x);
         if (hero.isDefeated()){
            System.out.println("");
            System.out.println("Monsters win!");
            gamePlaying = false;
            int score = hero.getHP()+hero.getMP()+hero.getAttack()+hero.getDefense();//add together all the stats of the player to get  score
            System.out.println("Your HP: "+hero.getHP()+" plus Your MP: "+hero.getMP()+" plus Your Attack: "+hero.getAttack()+" plus Your Defence: "+hero.getDefense()+" equals your score.");
            System.out.println("Your score is "+score+"");
            System.out.println("Game Over.");
            System.exit(1);
         }
         else if (x == 2 && enemy1.isDefeated() && enemy2.isDefeated()){
            System.out.println("You" + " win!");
            System.out.println("");
            Sound.Reward();
            findReward();
            gamePlaying = false;
         }
         else if(x == 1 && enemy1.isDefeated())
         {
            System.out.println("You" + " win!");
            System.out.println("");
            Sound.Reward();
            findReward();
            gamePlaying = false;
         }
      }
   }
}