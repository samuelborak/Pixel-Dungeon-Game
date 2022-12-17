import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
public class MazeDriver						//Driver Program
{
   public static MazeGrid screen;					//Game window
   
   public static void main(String[]args)
   {
      
      screen = new MazeGrid();
      JFrame frame = new JFrame("Arrays Represented in Graphics with Keyboard Input");	//window title
      frame.setSize(817, 840);					//Size of game window
      frame.setLocation(100, 50);				//location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.addKeyListener(new listen());		//Get input from the keyboard
   }
   
   public static class listen implements KeyListener 
   {
   
      public void keyTyped(KeyEvent e)
      {
      
      }
      
      public void keyPressed(KeyEvent e)
      {
      
      }
   
      public void keyReleased(KeyEvent e)
      {
         screen.processUserInput(e.getKeyCode());
      }
   }
}