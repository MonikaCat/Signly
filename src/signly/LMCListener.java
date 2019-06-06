package signly;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;


/**
* This class extends LMC Listener Class
* This class implements a controller class for Signly application 
* @author Monika Pusz 
* University of the West of England
* Student number: 16024757
* Course: Computer Science 
* @version 1.0
*/

class LMCListener extends Listener {
    
    // Initialize and declare the instance variables 
    public  ArrayList<String> output_message = new ArrayList<>();
    String[] letter_gestures = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
    //String[] gestures = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
    String header = "ThumbX, ThumbY, ThumbZ, IndexX, IndexY, IndexZ, MiddleX, MiddleY, MiddleZ, RingX, RingY, RingZ, PinkyX, PinkyY, PinkyZ, Letter \n";
    float[] right_hand = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    String[] temp = new String[right_hand.length];
    String previous_letter = "";
    String save_letter = "";
    int counter = 1;
    private final int number = 100;
    File save_file = new File("src//test_data//testN222.csv");
    int training_counter;
    int total_saved = 0;
    int same_letter = 0;
    static List<String> sentence_builder = new ArrayList<String>();
    static StringBuilder s = new StringBuilder();
    
    //static StringJoiner joiner = new StringJoiner("");
    
    
	
    public void onInit(Controller controller)
    {
        System.out.println("Initialized!");
    }
    
	
    public void onConnect(Controller controller)
    {
        System.out.println("Connected! ");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
    }
    
    public void onDisconnect(Controller controller)
    {
        System.out.println("Disconnected");
    }
    
    public void onExit(Controller controller)
    {
        System.out.println("Exited!");
    }
    
    
    
    /**
    * This method handles the access to the data in the frame
    * It checks if frame is valid, hands are present and and saves normalized coordinates values in right_hand array
    * It checks boolean parameters run_training and run_classifier to perform saving gesture or neural network calculations
    * @param controller
    */
	
	
    public void onFrame(Controller controller) {
       
       int classifier_counter = 0;
        Frame frame = controller.frame();
        double[] right_hand = new double[15];
 

        if(frame.isValid() && frame.hands().isEmpty())
        {
            Classifier.jLabel3.setText("No hand detected!");
            Training.jLabel3.setText("No hand detected!");
        }
        
        else if(!frame.isValid())
        {
            Classifier.jLabel3.setText("Frame is not valid");
            Training.jLabel3.setText("Frame is not valid");
        }
        
        else if(frame.isValid())
        {
           try {
               Thread.sleep(100);
           } catch (InterruptedException ex) {
               Logger.getLogger(LMCListener.class.getName()).log(Level.SEVERE, null, ex);
           }
            //System.out.println("Frame is valid");
            HandList hands = frame.hands();
			
            // Loop through detected hands 
            for(Hand hand : hands)
            {
             
	    // Ensure the hand is right 
            if(hand.isRight())
            {   
//            Classifier.jLabel3.setText("");
//            Training.jLabel3.setText("");
       
		// Obtain the vector data for each finger in relation to the center of the palm 
		// Normalize the input 		
                Vector palmPosition = hand.palmPosition();
                Vector thumb = hand.fingers().get(0).tipPosition().minus(palmPosition).normalized();
                Vector index = hand.fingers().get(1).tipPosition().minus(palmPosition).normalized();
                Vector middle = hand.fingers().get(2).tipPosition().minus(palmPosition).normalized();
                Vector ring = hand.fingers().get(3).tipPosition().minus(palmPosition).normalized();
                Vector pinky = hand.fingers().get(4).tipPosition().minus(palmPosition).normalized();
                
 
                    // Save the coordinates in array 	
                    right_hand[0] = thumb.getX();
                    right_hand[1] = thumb.getY();
                    right_hand[2] = thumb.getZ();
                    right_hand[3] = index.getX();
                    right_hand[4] = index.getY();
                    right_hand[5] = index.getZ();
                    right_hand[6] = middle.getX();
                    right_hand[7] = middle.getY();
                    right_hand[8] = middle.getZ();
                    right_hand[9] = ring.getX();
                    right_hand[10] = ring.getY();
                    right_hand[11] = ring.getZ();
                    right_hand[12] = pinky.getX();
                    right_hand[13] = pinky.getY();
                    right_hand[14] = pinky.getZ();
                
            }
            else if(hand.isLeft())
            {
                Classifier.jLabel3.setText("Left hand detected! Please switch to right hand!");
                Training.jLabel3.setText("Left hand detected! Please switch to right hand!");
            }
            
            // Check if the function was called from Classifier window
            // Run Neural Network calculations 	
            if(Classifier.run_classifier == true)
            {
                calculate_network(controller, right_hand);
                classifier_counter++;
                if(classifier_counter == 100)
                {
                    Classifier.run_classifier = false;
                }
            }
            
			
			
            // Check if the function was called from Training window
            // Save the gesture
            // Display total number of gestures saved to user
			
            else if(Training.run_training == true)
            { 
                try {
                    saveGesture(right_hand, Training.save_letter);
                    Training.jLabel9.setText(String.valueOf(total_saved));
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(LMCListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            }
        }
            
            
       
    }
    
    
	
    /**
    * This method handles Neural Network calculations 
    * @param controller
    * @param right_hand
    */             
            
    public void calculate_network(Controller controller, double[] right_hand)
    {
            

             // Load Trained Model of Neural Network
           NeuralNetwork nnet = NeuralNetwork.createFromFile("src\\test_data\\NewSignlyNN.nnet");
        
          // NeuralNetwork nnet = NeuralNetwork.createFromFile("src\\test_data\\NewNeuralNetwork1.nnet");
           
       // NeuralNetwork nnet = NeuralNetwork.createFromFile("D:\\neurophs777\\NeurophProjectwrwr\\Neural Networks\\NewNeuralNetwork1.nnet");
           
            
            //NeuralNetwork nnet = NeuralNetwork.createFromFile("C:\\Users\\Mandoka\\Desktop\\new project here\\Signly\\NewSignlyNN.nnet");
            

            // Obtain the input from the controller
            nnet.setInput(right_hand);

            // Classify the input 
            nnet.calculate();

            // Save the output data in array
            double[] output = nnet.getOutput();

            // Obtain the match with the highest scored letter 
            String found_letter = findLetter(findMaxValue(output));
            
            double[] match_value = findMaxValue(output);
            double set_value = match_value[0];
            //System.out.println("SET VALUE " + set_value);

            

            // Check if new gesture is different from the last one 
            if(!(found_letter.equals(previous_letter)) )
            {

                
                // Display new match to user 
                Classifier.jLabel2.setText(found_letter);
                
                


                


                // Display match % of the classified gesture
                Classifier.jLabel9.setText("Match: " + (int)(set_value*100) + " %");

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LMCListener.class.getName()).log(Level.SEVERE, null, ex);
                }

                

                // Save the letter as a previous letter 
                previous_letter = found_letter; 
            }
            
            
            else
            {
                // Add counter to ensure displayed gesture confidence
                same_letter++;

                // Display and audio output the match once the confidence level has reached 10
                if(same_letter == 25)
                {
                    sentence_builder.add(found_letter);
                                      
                   //s.append(found_letter);
                   //joiner.add(sentence_builder);
                
                    Classifier.jLabel10.setText(sentence_builder.toString());
                    VoiceOutput.speak(found_letter);
                    same_letter = 0;
                }
            }

            
    }
    
    
              

    /**
    * This method returns the highest match value and its position found in output array 
    * @param output_values
    * @returns max_value
    */       
        
      public double[] findMaxValue(double output_values[])
            {
      double max = output_values[0];
      int counter;
      double position = 0;

      //Find and save highest value in output_values array 
      for(counter = 0; counter < output_values.length; counter++)
      {
          if(output_values[counter] > max)
          {
              max = output_values[counter];
              position = Double.valueOf(counter); 

          }
      }

      double[] max_value = new double[2];

      // Save its value and position in max_value array

        max_value[0] = max;
        max_value[1] = position;  


      return max_value;
    }


      
    /**
    * This method returns English letter according to the position of the highest value found in output array 
    * @param max_value
    * @returns letterString
    */
      
      public String findLetter(double[] max_value)
      {
          int valuem = (int) max_value[1];

          String letterString;
          
          // Check the position of the highest match saved in max_value arrary 
          switch(valuem)
          {
             
              case 0: letterString = "A";
              break;
              case 1: letterString = "B";
              break;
              case 2: letterString = "C";
              break;
              case 3: letterString = "D";
              break;
              case 4: letterString = "E";
              break;
              case 5: letterString = "F";
              break;
              case 6: letterString = "G";
              break;
              case 7: letterString = "H";
              break;
              case 8: letterString = "I";
              break;
              case 9: letterString = "J";
              break;
              case 10: letterString = "K";
              break;
              case 11: letterString = "L";
              break;
              case 12: letterString = "M";
              break;
              case 13: letterString = "N";
              break;
              case 14: letterString = "O";
              break;
              case 15: letterString = "P";
              break;
              case 16: letterString = "Q";
              break;
              case 17: letterString = "R";
              break;
              case 18: letterString = "S";
              break;
              case 19: letterString = "T";
              break;
              case 20: letterString = "U";
              break;
              case 21: letterString = "V";
              break;
              case 22: letterString = "W";
              break;
              case 23: letterString = "X";
              break;
              case 24: letterString = "Y";
              break;
              case 25: letterString = "Z";
              break;
              default: letterString = "Invalid letter";
              break;
     
          }
          return letterString;
      }
      
      
      
    /**
    * This method handles gesture saving functions 
    * @param gesture
    * @param letter
    */
         
    public void saveGesture(double[] gesture, String letter) throws IOException, InterruptedException
    {
         
        
        // Create new writer 
        BufferedWriter writer = new BufferedWriter(new FileWriter(save_file.getName(), true));
        //writer.append(header);
               
        // Save obtained coordinates in letter_gestures array 
        for(int j = 0; j<gesture.length;j++)
        {
            temp[j] = String.valueOf(gesture[j]);
           
            letter_gestures[j] = temp[j];
        }
        
        //Skip first 15 rows as they contain Vector data
        int value_index = 15;
        
        String letter_returned;
        
        
        // Save letter output Vectors according to found match in letter_gestures array
        switch(letter)
        {
            case "A": letter_gestures[value_index] = "1";
            break;
            case "B": letter_gestures[value_index + 1] = "1";
            break;
            case "C": letter_gestures[value_index + 2] = "1";
            break;
            case "D": letter_gestures[value_index + 3] = "1";
            break;
            case "E": letter_gestures[value_index + 4] = "1";
            break;
            case "F": letter_gestures[value_index + 5] = "1";
            break;
            case "G": letter_gestures[value_index + 6] = "1";
            break;
            case "H": letter_gestures[value_index + 7] = "1";
            break;
            case "I": letter_gestures[value_index + 8] = "1";
            break;
            case "J": letter_gestures[value_index + 9] = "1";
            break;
            case "K": letter_gestures[value_index + 10] = "1";
            break;
            case "L": letter_gestures[value_index + 11] = "1";
            break;
            case "M": letter_gestures[value_index + 12] = "1";
            break;
            case "N": letter_gestures[value_index + 13] = "1";
            break;
            case "O": letter_gestures[value_index + 14] = "1";
            break;
            case "P": letter_gestures[value_index + 15] = "1";
            break;
            case "Q": letter_gestures[value_index + 16] = "1";
            break;
            case "R": letter_gestures[value_index + 17] = "1";
            break;
            case "S": letter_gestures[value_index + 18] = "1";
            break;
            case "T": letter_gestures[value_index + 19] = "1";
            break;
            case "U": letter_gestures[value_index + 20] = "1";
            break;
            case "V": letter_gestures[value_index + 21] = "1";
            break;
            case "W": letter_gestures[value_index + 22] = "1";
            break;
            case "X": letter_gestures[value_index + 23] = "1";
            break;
            case "Y": letter_gestures[value_index + 24] = "1";
            break;
            case "Z": letter_gestures[value_index + 25] = "1";
            break;

            default:
               throw new IllegalArgumentException("Invalid letter! Please Try Again! ");
        }
        
				// Create new CSV file and save the data stored in letter_gestures array 
                if(!(save_file.exists()))
                {
                
                for(int i = 0; i < letter_gestures.length; i++)
                {
                    writer.append(letter_gestures[i]);
                    writer.append(',');
                    if(i + 1 == letter_gestures.length)
                    {
                        writer.append(letter);
                        writer.newLine();
                    }
                }
                }
                else
                {
                   for(int i = 0; i < letter_gestures.length; i++)
                {
                    writer.append(letter_gestures[i]);
                    writer.append(',');
                    if(i + 1 == letter_gestures.length)
                    {
                        writer.append(letter);
                        writer.newLine();
                    }
                } 
                }
                writer.flush();
                training_counter++;
                total_saved++;
                
				// Stop recording process after 100 saving gestures 
                if(training_counter == 500)
                {
                    Training.run_training = false;
                    Training.controller.removeListener(Training.listener);
                    training_counter = 0;
                }
                
                

    }
    
    
    

    
}