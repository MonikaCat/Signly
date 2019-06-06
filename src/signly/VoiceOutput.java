package signly;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


/**
* This class implements FreeTTS speech synthesizer
* @author Monika Pusz 
* University of the West of England
* Student number: 16024757
* Course: Computer Science 
* @version 1.0
*/


public class VoiceOutput {
    
    private static final String VOICENAME_kevin = "kevin";
    
    public static void speak(String text) {
      Voice voice;
      VoiceManager voiceManager = VoiceManager.getInstance();
      voice = voiceManager.getVoice(VOICENAME_kevin);
      voice.allocate();
      voice.speak(text);
      
     }
    
}
