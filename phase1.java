/**
 * 
 */
package assig5;

import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Mikie
 *
 */
public class phase1
{

   // static for the card icons and their corresponding labels
   static final char[] CARD_NUMBERS = new char[]
   { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X' };
   static final char[] SUITS = new char[]
   { 'C', 'D', 'H', 'S' };
   static final int NUM_CARD_IMAGES = (CARD_NUMBERS.length * SUITS.length) + 1;
   // + 1 for back card
   static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];

   static void loadCardIcons()
   {
      String folderName = "images/";
      String fileType = ".gif";
      String cardImage = "";
      String backCard = folderName + "BK" + fileType;
      for (int i = 0; i < NUM_CARD_IMAGES; i++)
      {
         cardImage = folderName;
         cardImage += Character
                        .toString(CARD_NUMBERS[i % CARD_NUMBERS.length]);
         int suitInt = i / CARD_NUMBERS.length;
         if (suitInt >= SUITS.length)
         {
            // Past suited cards, use back card
            icon[i] = new ImageIcon(backCard);
            continue;
         }
         cardImage += Character.toString(SUITS[suitInt]);
         cardImage += fileType;
         icon[i] = new ImageIcon(cardImage);
      }
   }

   // a simple main to throw all the JLabels out there for the world to see
   public static void main(String[] args)
   {
      int k;

      // prepare the image icon array
      loadCardIcons();

      // establish main frame in which program will run
      JFrame frmMyWindow = new JFrame("Card Room");
      frmMyWindow.setSize(1150, 650);
      frmMyWindow.setLocationRelativeTo(null);
      frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // set up layout which will control placement of buttons, etc.
      FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
      frmMyWindow.setLayout(layout);

      // prepare the image label array
      JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         labels[k] = new JLabel(icon[k]);

      // place your 3 controls into frame
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         frmMyWindow.add(labels[k]);

      // show everything to the user
      frmMyWindow.setVisible(true);
   }

}
