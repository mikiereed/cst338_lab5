
package assig5;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.*;

public class CardTable extends JFrame
{
	/*
	 * the following five members are needed is to establish the grid layout 
	 * for the JPanels, the organization of which depends on how many cards and
	 * players will be displayed.
	 */
	static int MAX_CARDS_PER_HAND = 56;
	static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games
	   
	private int numCardsPerHand;
	private int numPlayers;
	
	public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
	
	/*
	 * The constructor filters input, adds any panels to the JFrame, and 
	 * establishes layouts
	 */
	public CardTable( String title, int numCardsPerHand, int numPlayers )
	{
		super(title);
		
		if( numCardsPerHand <= MAX_CARDS_PER_HAND )
			this.numCardsPerHand = numCardsPerHand;
		
		if( numPlayers <= MAX_PLAYERS )
			this.numPlayers = numPlayers;
		
		frameInit();
		
		this.setLayout( new GridLayout(3, 1) );
		
		/*
		 * three Public JPanels, one for each hand (player-bottom and 
		 * computer-top) and a middle "playing" JPanel. 
		 */
		pnlComputerHand = new JPanel( new GridLayout(1, numCardsPerHand) );
		pnlPlayArea = new JPanel( new GridLayout(2, 2) );
		pnlHumanHand = new JPanel( new GridLayout(1, numCardsPerHand) );
		
		setLayout( new BorderLayout(20, 10));
		
		this.add( pnlComputerHand, BorderLayout.NORTH );
		this.add( pnlPlayArea, BorderLayout.CENTER );
		this.add( pnlHumanHand, BorderLayout.SOUTH );
		
		pnlComputerHand.setBorder( new TitledBorder("Computer Hand") );
		pnlPlayArea.setBorder( new TitledBorder("Playing Area") );
		pnlHumanHand.setBorder( new TitledBorder("Your Hand") );
	}
	
	// Accessors for the two instance members
	public int getNumCardsPerHand()
	{
		return this.numCardsPerHand;
	}
	
	public int getNumPlayers()
	{
		return this.numPlayers;
	}
}
