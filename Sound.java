import javax.sound.midi.*;

public class Sound
{
//sound stuff
   protected static MidiChannel[] channels=null;		//MIDI channels
   protected static Instrument[] instr;					//MIDI instrument bank
   public static int soundOffFrame, nextNoteFrame;    //frame number to turn sound off, or go to the next note for melody notes
   public static int nextNoteFrame2;                  //frame number to go to the next note for pedal tones
   protected static final int PIANO = 0;
   protected static final int HARPSICHORD = 6;
   protected static final int CELESTA = 8;
   protected static final int VIBRAPHONE = 11;
   protected static final int XYLOPHONE = 13;
   protected static final int NYLON_GUIT = 24;
   protected static final int STEEL_GUIT = 25;
   protected static final int GUIT_HARM = 31;
   protected static final int ORCH_HIT = 55;
   protected static final int SHAKUHACHI = 57;
   protected static final int SPACE_VOICE = 91;
   protected static final int HALO_PAD = 94;
   protected static final int CRYSTAL = 98;
   protected static final int SITAR = 104;
   protected static final int BANJO = 105;
   protected static final int SHAMISEN = 106;
   protected static final int KALIMBA = 108;
   protected static final int TINKERBELL = 112;
   protected static final int STEEL_DRUM = 114;
   protected static final int WOODBLOCK = 115;
   protected static final int TAIKO = 116;
   protected static final int MELO_TOM = 117;
   protected static final int SYNTH_DRUM = 118;
   protected static final int REVERSE_CYM = 119;
   protected static final int FRET_NOISE = 120;
   protected static final int BREATH_NOISE = 121;
   protected static final int SEA_SHORE = 122;
   protected static final int BIRD = 123;
   protected static final int HELICOPTER = 125;
   protected static final int APPLAUSE = 126;
   protected static final int GUNSHOT = 127;

   public static void initialize()
   {
   //sound stuff
      try 
      {
         Synthesizer synth = MidiSystem.getSynthesizer();
         synth.open();
         Sound.channels = synth.getChannels();
         Sound.instr = synth.getDefaultSoundbank().getInstruments();
      }
      catch (Exception ignored) 
      {}
      channels[0].programChange(instr[PIANO].getPatch().getProgram());
      channels[1].programChange(instr[PIANO].getPatch().getProgram());
      channels[2].programChange(instr[PIANO].getPatch().getProgram());
      channels[0].allNotesOff();		//turn sounds off 
      channels[1].allNotesOff();		//turn sounds off 
      channels[2].allNotesOff();		//turn sounds off 
   
      soundOffFrame = Integer.MAX_VALUE;
      nextNoteFrame = 0;
      nextNoteFrame2 = 0;
   
   }

 //pre:  numTracks >= 0 && numTracks < channels.length
 //post: turns sound of numTracks number of channels
   public static void silence(int numTracks)
   {
      for(int i=0; i<numTracks && i<channels.length; i++)
      {
         channels[i].allNotesOff();		//turn sounds off 
      }
   }
   
   public static void Movement()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[CRYSTAL].getPatch().getProgram()); //player sound at a radnom pitch
      int pitch = rollDie(46,80);          
      int velocity = 100;
      channels[1].noteOn(pitch, velocity);
   }
   public static void Heal()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[HALO_PAD].getPatch().getProgram());
      int pitch = rollDie(66,68);          
      int velocity = 100;
      channels[1].noteOn(pitch, velocity);
   }
   public static void changeLevel()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[WOODBLOCK].getPatch().getProgram());
      int pitch = rollDie(19,34);          
      int velocity = 120;
      channels[1].noteOn(pitch, velocity);
   }
   
   public static void Attack()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[TAIKO].getPatch().getProgram());
      int pitch = rollDie(53,69);          
      int velocity = 100;
      channels[1].noteOn(pitch, velocity);
   }
   
   public static void Enemy()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[STEEL_GUIT].getPatch().getProgram());
      int pitch = rollDie(16,32);          
      int velocity = 100;
      channels[1].noteOn(pitch, velocity);
   }
   
   public static void Special()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = rollDie(45,46);          
      int velocity = 100;
      channels[1].noteOn(pitch, velocity);
   }
   public static void Battle()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[BANJO].getPatch().getProgram());
      int pitch = rollDie(51,61);          
      int velocity = 100;
      channels[1].noteOn(pitch, velocity);
   }
   public static void Reward()
   {
      //if(!CultimaPanel.soundOn)
         //return;
      channels[1].programChange(instr[STEEL_DRUM].getPatch().getProgram());
      int pitch = rollDie(72,76);          
      int velocity = 100;
      channels[1].noteOn(pitch, velocity);
   }
   public static int rollDie(int min, int max)
   {
      return (int)(Math.random()*(max-min+1)) + min;
   }

   public static int rollDie(int numSides)
   {
      return rollDie(1, numSides);
   }

}