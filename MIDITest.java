import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.util.ArrayList;

public class MIDITest extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
   private int SIZE=700;   						//screen SIZE
   private MidiChannel[] channels=null;		//MIDI channels
   private Instrument[] instr;					//MIDI instrument bank
   private int instrument = 127/2;				//current instrument selected (0-127)
   private int volume = 80;						//current instrument volume (0-127)
   private int pitch = 127/2;						//current instrument pitch (0-127)
   private int mouseX, mouseY;					//current mouse position
   private ArrayList<Color> circleColors;		//to display circles on the screen for notes being played
   private ArrayList<Point> circleLocations;
   private ArrayList<Integer> circleSizes;


   public MIDITest()
   {
      int delay = 0;
      Timer t = new Timer(delay, new Listener());
      t.start();
      addMouseMotionListener(this);
      addMouseListener(this);
      addMouseWheelListener(this);
      mouseX = SIZE/2;
      mouseY = SIZE/2;
      circleColors = new ArrayList();
      circleLocations = new ArrayList();
      circleSizes = new ArrayList();
      try 
      {
         Synthesizer synth = MidiSystem.getSynthesizer();
         synth.open();
         channels = synth.getChannels();
         instr = synth.getDefaultSoundbank().getInstruments();
      }
      catch (Exception ignored) 
      {}
      channels[0].programChange(instr[instrument].getPatch().getProgram());
   }

//display control menu and circle to represent sound being played
   public void paintComponent(Graphics g) 
   {
      super.paintComponent(g);
      g.setColor(Color.black);                           //draw black background
      g.fillRect(0,0,SIZE,SIZE);        
      for(int i=0; i<circleLocations.size(); i++)			//draw circles for notes being played
      {
         int locX = (int)(circleLocations.get(i)).getX();
         int locY = (int)(circleLocations.get(i)).getY();
         Color color = circleColors.get(i);
         int SIZE = circleSizes.get(i);
         g.setColor(color);
         g.fillOval(locX, locY, SIZE, SIZE);
      }
   
      g.setColor(Color.black);
   
      g.setFont(new Font("Monospaced", Font.BOLD, 20));  //show user controls
      g.drawString("LEFT BUTTON sound", SIZE, (SIZE/7));
   
      g.drawString("RIGHT BUTTON silence",SIZE,(int)(SIZE/4.67));
   
      g.drawString("UP-DOWN volume:"+volume,SIZE,(int)(SIZE/3.5));
   
      g.drawString("MOUSE WHEEL instrument:"+instrument,SIZE,(int)(SIZE/2.8));
      
      g.drawString(instr[instrument].toString().substring(12),SIZE,(int)(SIZE/2.33));
   
      g.drawString("LEFT-RIGHT pitch:"+pitch+" "+intToKey(pitch),SIZE,(int)(SIZE/2));
   
      if(mouseX < SIZE && mouseY < SIZE)						//draw circle for the note that can be played
      {
         g.setColor(new Color(pitch*2, volume*2, Math.abs((instr[instrument]).hashCode())%256));
         g.drawOval(mouseX, mouseY, volume, volume);
      }
   }

   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) 
      {
         repaint();
      }
   }

//given a ourNote, return its normalized value where 0 is the first ourNote in the scale (C)
   public static int normalize(int ourNote)
   {
      while(ourNote>=12)			//strip out any octaves
         ourNote-=12;   	
      return ourNote;
   }

//given a MIDI note value, return its corresponding key (multiples of 12 are C)
//returns "?" if it is not found
   public static String intToKey(int num)
   {
      num=normalize(num);   	
      switch(num)
      {
         case 0: 
            return "C";
         case 1: 
            return "C#";
         case 2: 
            return "D";
         case 3: 
            return "D#";
         case 4: 
            return "E";
         case 5: 
            return "F";
         case 6: 
            return "F#";
         case 7: 
            return "G";
         case 8: 
            return "G#";
         case 9: 
            return "A";
         case 10:
            return "A#";
         case 11:
            return "B";
      }
      return "?";			//unknown note value sent
   }

   public static void main(String[] args)
   {
      JFrame frame=new JFrame("MIDI!!!!");
      frame.setContentPane(new MIDITest());
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setVisible(true);
      frame.setSize(1400,800);
   }

//*****BEGIN MOUSE CONTROLS*****
//vertical mouse movement to change volume, horizontal mouse movement to change pitch
   public void mouseMoved(MouseEvent e) 
   {
      mouseX = e.getX();
      mouseY = e.getY();
      volume = (SIZE - mouseY) / (SIZE/128);      
      if(volume < 0)									//clip volume - no lower than 0, no larger than 127
         volume = 0;
      else if(volume > 127)
         volume = 127;	
      pitch = (SIZE - mouseX) / (SIZE/128);      
      if(pitch < 0)									//clip pitch - no lower than 0, no larger than 127
         pitch = 0;
      else if(pitch > 127)
         pitch = 127;
   }

//clicking mouse button will turn a sound on (LEFT) or off (RIGHT)
   public void mouseClicked( MouseEvent e )
   {
      int button = e.getButton();
      if(button == MouseEvent.BUTTON1)
      {
         channels[0].noteOn(pitch, volume);
         circleColors.add(new Color(pitch*2, volume*2, Math.abs((instr[instrument]).hashCode())%256)); 
         circleLocations.add(new Point(mouseX, mouseY)); 
         circleSizes.add(new Integer(volume));
      }
      else if(button == MouseEvent.BUTTON3)
      {
         channels[0].allNotesOff(); 		//turn sounds off
         circleColors.clear();				//empty circles on the screen for notes being played
         circleLocations.clear();
         circleSizes.clear();
      }
   }

//mouse wheel movement will change the instrument patch
   public void mouseWheelMoved(MouseWheelEvent e) 
   {
      int WHEEL_SENSITIVITY=1;
      int notches = e.getWheelRotation();
      if (notches <= -WHEEL_SENSITIVITY && instrument < 127) 
      {
         instrument++;   
         channels[0].programChange(instr[instrument].getPatch().getProgram());
      } 
      else 
         if (notches >= WHEEL_SENSITIVITY  && instrument > 0) 
         {
            instrument--;
            channels[0].programChange(instr[instrument].getPatch().getProgram());
         }
   }

   public void mousePressed( MouseEvent e )
   {}

   public void mouseReleased( MouseEvent e )
   {}

   public void mouseEntered( MouseEvent e )
   {}

   public void mouseExited( MouseEvent e )
   {}

   public void mouseDragged(MouseEvent e) 
   {}
//*****END MOUSE CONTROLS
}