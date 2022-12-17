import javax.swing.ImageIcon;
public class Floortile
{
   private int west;
   private int east;
   private int north;
   private int south;
   private boolean isWall;
   private boolean isVisited;
   private boolean isEvent;
   private boolean isExit;
   private boolean isDone;
   public Floortile()
   {
      west = 0;
      east = 0;
      north = 0;
      south = 0;
      isWall = true;
      isVisited = false;
      isEvent = false;
      isDone = false;
   }
   public int getWest()
   {
      return west;
   }
   public int getEast()
   {
      return east;
   }
   public int getNorth()
   {
      return north;
   }
   public int getSouth()
   {
      return south;
   }
   public int setWest()
   {
      west = 1;
      return west;
   }
   public int setEast()
   {
      east = 1;
      return east;
   }
   public int setNorth()
   {
      north  = 1;
      return north;
   }
   public int setSouth()
   {
      south = 1;
      return south;
   }
   public boolean isAWall() //returns true if the tile is a wall false otherwise
   {
      if(isWall == true)
         return true;
      else
         return false;
   }
   public void setWall(boolean x) //sets the tile to a wall or not
   {
      isWall = x;
   }
   
   public boolean isVisited() //returns true if the tile is visited false otherwise
   {
      if(isVisited == true)
         return true;
      else
         return false;
   
   }
   public boolean isAnExit() //returns true if the tile is an exit false otherwise
   {
      if(isExit == true)
         return true;
      else
         return false;
   
   }
   public void nowExit(boolean x) //sets the tile to an exit or not
   {
      isExit = x;
   }
   public void nowVisited(boolean x) //sets the tile to visited or not
   {
      isVisited = x;
   }
   public void nowEvent(boolean x) //sets the tile to an event or not
   {
      isEvent = x;
   }
   public boolean isEvent()
   {
      if(isEvent == true)
         return true;
      else
         return false;
   }
   public boolean isDone() //returns true if the tile is an exit false otherwise
   {
      if(isDone == true)
         return true;
      else
         return false;
   
   }
   public void nowDone(boolean x) //Visited name already used, this wioll be used for the event boolean to see if it will trigger
   {
      isDone = x;
   }
   public String toString()
   {
      if(isWall == true)
         return "*";
      else
         return " ";
   }
}