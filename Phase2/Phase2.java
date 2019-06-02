/* Team 1
 * Austin Ah Loo
 * Ramon Lucindo
 * Mikie Reed
 * Mitchell Saunders
 * Nick Saunders
 * CST 338 - Module 4: Optical Barcode
 */

package assig5;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


public class Phase2
{
	static CardTable myCardTable;
	static CardGameFramework highCardGame;
	static Hand[] cardStacks = new Hand[2];
	static int NUM_CARDS_PER_HAND = 7;
	static int NUM_PLAYERS = 2;
	static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
	static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
	static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS];
	
	/**
	 * returns a new random card for the main to use in its tests.
	 * @return newCard
	 */
	public static Card generateRandomCard()
	{
		char value;
		Card.Suit suit;
		int suitRand, valueRand;
		
		suitRand = (int)(Math.random() * 4);
		valueRand = (int)(Math.random() * 14);
		
		suit = Card.suitRanks[suitRand];
		value = Card.valuRanks[valueRand];
		
		Card newCard = new Card( value, suit );
		return newCard;
		
	}
	
	public static void main( String[] args )
	{
		
		GUICard.loadCardIcons();

        // Create CardGameFramework
        int numPacksPerDeck = 1;
        int numJokersPerPack = 0;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        /**
         * CardFramework object to leverage the dealing of cards to the GUI
         * display
         */
        highCardGame = new CardGameFramework(numPacksPerDeck,
                numJokersPerPack, numUnusedCardsPerPack, unusedCardsPerPack,
                NUM_PLAYERS, NUM_CARDS_PER_HAND);

        
        highCardGame.deal();
        cardStacks[0] = new Hand();
        cardStacks[1] = new Hand();
        

        myCardTable
                = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
        myCardTable.setSize(800, 600);
        myCardTable.setLocationRelativeTo(null);
        myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myCardTable.setVisible(true);

        buildPanels();
        
	}
        
    public static void buildPanels()
    {
            int k;
            Icon tempIcon;

            for ( k = 0; k < NUM_CARDS_PER_HAND; k++ )
            {
                computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
                
                tempIcon = GUICard.getIcon(highCardGame.getHand(1).inspectCard(k));
                
                humanLabels[k] = new JLabel(tempIcon);
            }

            for ( k = 0; k < NUM_PLAYERS; k++ )
            {
                if( k == 0 )
                	playLabelText[k] = new JLabel("Computer", JLabel.CENTER);
                
                if( k == 1 )
                	playLabelText[k] = new JLabel("You", JLabel.CENTER);
                
            }

            // ADD LABELS TO PANELS -----------------------------------------
            for ( k = 0; k < NUM_CARDS_PER_HAND; k++ )
            {
                myCardTable.pnlComputerHand.add(computerLabels[k]);
                myCardTable.pnlHumanHand.add(humanLabels[k]);
            }
            
            // and two random cards in the play region (simulating a computer/hum ply)
            for( k = 0; k < NUM_PLAYERS; k++ )
            {
            	playedCardLabels[k] = new JLabel( GUICard.getIcon(
            			generateRandomCard()) );
            }
            
            // adding cards to the play area panel
            for( k = 0; k < NUM_PLAYERS; k++ )
            {
            	myCardTable.pnlPlayArea.add(playedCardLabels[k]);
            }
            
            // adding lables to the PA panel under the cards
            myCardTable.pnlPlayArea.add(playLabelText[0]);
            myCardTable.pnlPlayArea.add(playLabelText[1]);
            
            // show everything to the user
            myCardTable.setVisible(true);

        }

	    

}
