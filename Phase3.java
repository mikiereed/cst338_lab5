package assig5;

/* Team 1

 * Austin Ah Loo
 * Ramon Lucindo
 * Mikie Reed
 * Mitchell Saunders
 * Nick Saunders
 * CST 338 - Module 5: GUI Cards
 * 
 *  */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;


public class Phase3
{
   public static CardTable myCardTable;
   public static CardGameFramework highCardGame;
   public static final int NUM_CARDS_PER_HAND = 7;
   public static final int NUM_PLAYERS = 2;
   public static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   public static JButton[] humanLabels = new JButton[NUM_CARDS_PER_HAND];
   public static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS];
   public static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS];
   public static Card[] winnings = new Card[1 * 52]; //change 1 for # of decks
   public static int numTimesWon = 0;
   public static boolean readyToPlayCard = true;
   
   // static for the card icons and their corresponding labels
   public static final char[] CARD_NUMBERS = new char[]
   {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
   public static final char[] SUITS = new char[]
   {'C', 'D', 'H', 'S'};
   public static final int NUM_CARD_IMAGES = (CARD_NUMBERS.length *
         SUITS.length) + 1;
   // + 1 for back card
   public static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];
   
   public static void main(String[] args)
   {
      GUICard.loadCardIcons();

      // Create CardGameFramework
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      
      int k;

      /**
       * CardFramework object to leverage the dealing of cards to the GUI
       * display
       */
      
      highCardGame = new CardGameFramework(numPacksPerDeck,
              numJokersPerPack, numUnusedCardsPerPack, unusedCardsPerPack,
              NUM_PLAYERS, NUM_CARDS_PER_HAND);
      
      highCardGame.deal();
      myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 800);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
      myCardTable.setVisible(true);
      
      //The computer and player hands will be built up from highCardGame hands
      buildHands();
      
      //Create labels for computer and player
      for (k = 0; k < NUM_PLAYERS; k++)
      {
         if(k == 0)
            playLabelText[k] = new JLabel("Computer", JLabel.CENTER);
         
         if(k == 1)
            playLabelText[k] = new JLabel("You", JLabel.CENTER);    
       }
      
      //Call helper method to put the playLabelText onto the myCardTable
      resetPlayArea();
      
      // show everything to the user 
      myCardTable.repaint();
      myCardTable.setVisible(true);
   }
   
   private static void buildHands()
   {
      /**
       * Generate the JLabels (for computer) and JButtons (for player)
       * based off of the hands stored in highGameCard and add them to 
       * myCardTable to be visible.
       */
      myCardTable.pnlComputerHand.removeAll();
      myCardTable.pnlHumanHand.removeAll();
      Icon tempIcon;
      // Create labels and add them to myCardTable for computer-----------------
      for (int k = 0; k < highCardGame.getHand(0).getNumCards(); k++)
      {
         if (k < highCardGame.getHand(0).getNumCards())
         {
            computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
         }
         myCardTable.pnlComputerHand.add(computerLabels[k]);
      }
      
      // Create labels and add them to myCardTable for human-----------------
      for (int k = 0; k < highCardGame.getHand(1).getNumCards(); k++)
      {
         if (k < highCardGame.getHand(1).getNumCards())
         {
            tempIcon = GUICard.getIcon(highCardGame.getHand(1)
                                                   .inspectCard(k));
            humanLabels[k] = new JButton(tempIcon);
            myCardTable.pnlHumanHand.add(new CardButton(humanLabels[k]
                                                               .getIcon()));
         }           
      }
      myCardTable.setVisible(true); 
   }
   
   private static void addCardsToTable()
   {
      /**
       * For both computer and player, add the chosen cards (stored in 
       * playedCardLabels) to the myCardTable middle JPanel
       */
      //if there are no panels currently on the frame, then add some
      if (myCardTable.pnlPlayArea.getComponents().length <= NUM_PLAYERS)
      {
         // adding cards to the play area panel
         for(int k = 0; k < NUM_PLAYERS; k++ )
         {
            myCardTable.pnlPlayArea.add(playedCardLabels[k]);
            myCardTable.repaint();
         }
      }
   }
   
   private static void selectComputerCard()
   {
      /**
       * Generate the choice of card (from the computer's hand) for the
       * computer based off of the value of the card in the hand. The computer
       * will always choose the highest value of card in the hand.
       */
      Hand computerHand = highCardGame.getHand(0);
      Card tempCard;
      int highestValueIndex = -1;
      int tempCardValue = -1; 
      for (int i = 0; i < computerHand.getNumCards(); i++)
      {
         tempCard = computerHand.inspectCard(i);
         
         if (Card.valueAsInt(tempCard) > tempCardValue)
         {
            tempCardValue = Card.valueAsInt(tempCard);
            highestValueIndex = i;
         }
      }
      playedCardLabels[0] = new JLabel(GUICard.getIcon(computerHand.
                                             inspectCard(highestValueIndex)));
   }
   
   private static int didHumanWin()
   {
      /**
       * Determine if human or computer won the game. Return -1 if computer,
       * 0 if tie, and 1 if player.
       */
      Card computerCard = getCardFromPlayer(0);
      Card humanCard = getCardFromPlayer(1);
      
      int valueOfComputerCard = Card.valueAsInt(computerCard);
      int valueOfHumanCard = Card.valueAsInt(humanCard);
      
      if (valueOfHumanCard == valueOfComputerCard)
      {
         return 0;
      }
      else if (valueOfHumanCard > valueOfComputerCard)
      {
         return 1;
      }
      else
      {
         return -1;
      }
   }
   
   private static String getWinMessage()
   {
      /**
       * Based off of the results of didHumanWin() method, return an
       * Appropriate message to be displayed in a JLabel later.
       */
      int winStatus = didHumanWin();
      
      switch (winStatus)
      {
         case -1: return "Computer Wins!";
         case 0: return "Tie!";
         case 1: return "You Win!";
         default: return "NO WINNER - ERROR";
      }
   }
   
   private static Card getCardFromPlayer(int playerIndex)
   {
      /**
       * When provided a player number (0 for computer, 1 or more for player)
       * this will return the card that the entity had most recently chosen
       * to play.
       */
      String cardString = playedCardLabels[playerIndex].getIcon().toString();
      cardString = cardString.substring(cardString.indexOf('/') + 1);
      Card tempCard = getCardFromFilename(cardString);
      return tempCard;
   }
   
   private static Card getCardFromFilename(String filename)
   {
      /**
       * When provided the filename of an icon for a card image, this function
       * will return a card instance that has the same suit and value.
       * Note: The filename be in a standard format and must not have a folder
       * listed before it. The first two characters must be like "A8", where
       * "A" is the suit, and "8" is the value of the card.
       */
      char suitChar = filename.charAt(1);
      char valueChar = filename.charAt(0);
      Card tempCard = new Card();
      switch (suitChar)
      {
         case 'C': tempCard.suit = Card.Suit.clubs;
            break; 
         case 'D': tempCard.suit = Card.Suit.diamonds;
            break;
         case 'H': tempCard.suit = Card.Suit.hearts;
            break;
         case 'S': tempCard.suit = Card.Suit.spades;
            break;
         default: tempCard.suit = Card.Suit.spades;
            break;
      }
      tempCard.value = valueChar;
      return tempCard;
   }
   
   private static int getIndexOfCardInHand(int playerIndex, Card card)
   {
      /**
       * When provided with both the player index and a card object, this will
       * return the index of the equivalent card (card with same suit and value)
       * that resides in the hand of that entity.
       */
      Hand tempHand = highCardGame.getHand(playerIndex);
      Card tempCard;
      for (int i = 0; i < tempHand.getNumCards(); i++)
      {
         tempCard = tempHand.inspectCard(i);
         if (card.equals(tempCard))
         {
            return i;
         }
      }
      return -1;
   }
   
   private static void removePlayedCardsFromHands()
   {
      /**
       * This method will gather the most recently played card from previously
       * defined methods and will play card using Hand.playCard() method.
       * Then will call buildHands() which will reconstruct the JLabels
       * and JButtons in the myCardTable JPanels.
       */
      //find the index of the card in the hand, then remove it via playCard()
      Card tempCard = getCardFromPlayer(0);
      int cardInHandIndex = getIndexOfCardInHand(0, tempCard);
      highCardGame.getHand(0).playCard(cardInHandIndex);
      
      tempCard = getCardFromPlayer(1);
      cardInHandIndex = getIndexOfCardInHand(1, tempCard);
      highCardGame.getHand(1).playCard(cardInHandIndex);
      
      buildHands();
      myCardTable.repaint();
      myCardTable.setVisible(true);
   }

   private static void resetPlayArea()
   {
      /**
       * This is responsible for clearing and preparing the play area of 
       * myCardTable for the next round of cards. This will clear and then 
       * re-write JLabels back into the main play area. 
       */
      myCardTable.pnlPlayArea.removeAll();
      // adding labels to the PA panel under the cards
      myCardTable.pnlPlayArea.add(playLabelText[0]);
      myCardTable.pnlPlayArea.add(playLabelText[1]);
   }
   
   private static void roundEndDisplay()
   {
      /**
       * When the round has ended (signaled by the selection of cards by
       * all players), generate a win/lose/tie message and paste it along side
       * a JButton that allows the player to advance to the next round.
       * This method also comes with an anonymous action listener attached
       * to the JButton. When the JButton is pressed, the message JPanel
       * is cleared out, play area is reset and hands are rebuilt.
       */
      //determine winner via determineRoundWinner()
      JLabel roundEndLabel = new JLabel(getWinMessage());
      JButton nextRoundBtn = new JButton("Click for next round");
      nextRoundBtn.addActionListener(new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               myCardTable.pnlMsgArea.removeAll();
               myCardTable.repaint();
               //reset play area for next round
               resetPlayArea();
               buildHands();
               //now that the reset button has been pressed, card buttons can
               //be pressed again
               readyToPlayCard = true;
            }
         });
      myCardTable.pnlMsgArea.add(roundEndLabel);
      myCardTable.pnlMsgArea.add(nextRoundBtn);
   }
   
   @SuppressWarnings("serial")
   /**
    * Class CardButton is a typical JButton that has a defined ActionLister.
    * These buttons are designed to hold card icons are clickable by the player
    * when a selection has been made. Upon selection, multiple actions occur
    * that drive the game forward such as selecting computer card, adding cards
    * to the main play area and removing the cards from all of the hands.
    * @author mitch
    *
    */
   public static class CardButton extends JButton implements ActionListener
   {
      public CardButton(Icon icon)
      {
         super(icon);
         addActionListener(this);
      }
      @Override
      public void actionPerformed(ActionEvent e)
      {
         if (readyToPlayCard == true)
         {
            //create JLabel from the JButton clicked and add to playedCardLabels
            JButton source = (JButton)e.getSource();
            playedCardLabels[1] = new JLabel(source.getIcon());
            //Choose computer card based off of computer hand
            selectComputerCard();
            //add all selections to main play area
            addCardsToTable();
            //display winner message and button to advance
            roundEndDisplay();
            //add both cards to winnings if user won
            if (didHumanWin() == 1)
            {
               winnings[numTimesWon] = getCardFromPlayer(0);
               winnings[numTimesWon + 1] = getCardFromPlayer(1);
               numTimesWon += 2;
            }
            //remove chosen cards out of computer an player's hands
            removePlayedCardsFromHands();
            myCardTable.setVisible(true);
            //now that a card has just been played, change the flag so that
            //subsequent button presses do nothing
            readyToPlayCard = false;
         }
      }
   }

   public static class Card
   {
      private static final char DEFAULT_VALUE = 'A';
      private static final Suit DEFAULT_SUIT = Suit.spades;
         
      // Suit enum of possible suits for cards
      public enum Suit
       {
           clubs, diamonds, hearts, spades;
       }
       
       // private data members
       private char value;
       private Suit suit;
       private boolean errorFlag;
       
       public static char[] valuRanks = 
         {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
       
       public static Suit[] suitRanks = 
         { Suit.clubs, Suit.diamonds, Suit.hearts, Suit.spades };

       /**
        * Default Constructor for Card
        * Sets value = DEFAULT_VALUE, Suit = DEFAULT_SUIT
        * errorFlag set based on default constants
        *
        */
       public Card()
       {
          this.set(DEFAULT_VALUE, DEFAULT_SUIT); // default
       }
       
       /**
        * Constructor for Card with only errorFlag argument
        * Mainly used if errorFlag needs to be set
        * Sets value = DEFAULT_VALUE, Suit = DEFAULT_SUIT
        *
        */
       public Card( boolean errorFlag )
       {
          this.set(DEFAULT_VALUE, DEFAULT_SUIT); // default
          this.errorFlag = errorFlag;
       }
       
       /**
        * Constructor for Card
        * Sets value, Suit, and errorFlag based on arguments
        *
        * @param char value = set card value
        * @param Suit suit = set card suit
        */
       public Card(char value, Suit suit)
       {
           this.set(Character.toUpperCase(value), suit);
       }

       /**
        * Override method toString for Card object
        *
        * @return String (value + " of " + suit) or INVALID if errorFlag == true
        */
       @Override
       public String toString()
       {
          if (errorFlag)
          {
             return "[ INVALID ]";
          }
          return this.value + " of " + this.suit;
       }

       /**
        * Mutator for Card object
        * sets object.errorFlag based on validity
        *
        * @param char value = set card value
        * @param Suit suit = set card suit
        * @return Boolean based on success
        */
       public boolean set(char value, Suit suit)
       {
           if (isValid(value, suit))
           {
               this.value = value;
               this.suit = suit;
               errorFlag = false;
               return true;
           }
           errorFlag = true;
           return false;
       }

       /**
        * Accessor for errorFlag
        *
        * @return boolean errorFlag
        */
       public boolean getErrorFlag()
       {
           return errorFlag;
       }

       /**
        * Accessor for suit
        *
        * @return Suit suit
        */
       public Suit getSuit()
       {
           return suit;
       }

       /**
        * Accessor for value
        *
        * @return char value
        */
       public char getValue()
       {
           return value;
       }

       /**
        * Method to test if two cards are equal
        *
        * @param Card card = card to test object against
        * @return Boolean true if equal, false otherwise
        */
       public boolean equals(Card card)
       {
           if (value == card.getValue() && suit == card.getSuit())
           {
               return true;
           }
           return false;
       }

       //Checks if the value is a digit (2 - 9) or (T(10), J, Q, K, A)
       /**
        * Constuctor for Card
        * Sets value, Suit, and errorFlag based on arguments
        *
        * @param char value = value to check
        * @param Suit suit = suit to check (does not check at this time)
        */
       private boolean isValid( char value, Suit suit )
       {
          String goodChars = "TJQKA";
          boolean valueGood = goodChars.contains(Character.toString(value));
          if ((Character.isDigit(value) && value > 1) || valueGood)
          {
             return true;
          }
          return false;
       }
       
       // sort the incoming array of cards using a bubble sort routine. 
      public static void arraySort( Card[] cards, int arraySize )
      {
         boolean changed = true;
         
         while( changed )
         {
            changed = false;
            
            for( int i = 0; i < arraySize; i++ )
            {
               if( suitAsInt(cards[i]) > suitAsInt(cards[i + 1]) )
               {
                  swapCards( cards, i, i + 1 );
                  changed = true;
               }
               else if( suitAsInt(cards[i]) == suitAsInt(cards[i + 1]) )
               {
                  if( valueAsInt(cards[i]) > valueAsInt(cards[i + 1]) )
                  {
                     swapCards( cards, i, i + 1 );
                     changed = true;
                  }
               }
            }
         }  
      }
       
       public static int valueAsInt(Card card)
       {
         char testValue = card.getValue();
           switch ((char)testValue)
           {
               case 'A':
                   return 0;
               case '2':
                   return 1;
               case '3':
                   return 2;
               case '4':
                   return 3;
               case '5':
                   return 4;
               case '6':
                   return 5;
               case '7':
                   return 6;
               case '8':
                   return 7;
               case '9':
                   return 8;
               case 'T':
                   return 9;
               case 'J':
                   return 10;
               case 'Q':
                   return 11;
               case 'K':
                   return 12;
               default:
                   return 13;
           }
       }
       
       public static int suitAsInt( Card card )
      {  
          // handles nullPointerExepction when card becomes invalid
          if(card.getSuit() != null) 
          {
             switch( card.getSuit() )
             {
                case clubs:
                   return 0;
                case diamonds:
                   return 1;
                case hearts:
                   return 2;
                default:
                   return 3;
             }
          }
          else
             return 0;
           
      }
       
       public static void swapCards( Card[] array, int card1, int card2 )
      {
         Card temp;
         
         temp = array[card1];
         array[card1] = array[card2];
         array[card2] = temp;
      }
   }
   public static class Hand
   {

       public static final int MAX_CARDS = 100;
       private Card[] myCards;
       private int numCards;

       /**
        * Default Constructor for Hand
        * sets myCards = Card[MAX_CARDS]
        * sets numCards = 0
        *
        */
       public Hand()
       {
          this.resetHand();
       }
       
       /**
        * remove all cards from the hand
        *
        */
       public void resetHand()
       {
          // if myCards is not empty, properly empty it?
          this.myCards = new Card[MAX_CARDS];
          this.numCards = 0;
       }

       /**
        * adds a card to the next available position in the myCards array
        * 
        * @param Card card = card to add to hand
        * @return boolean based on success of method
        */
       public boolean takeCard(Card card)
       {
           if (numCards >= MAX_CARDS)
           {
               return false;
           }
           else
           {
               myCards[numCards++] = new Card(card.getValue(), card.getSuit());
               return true;
           }
       }

       /**
        * returns and removes the card in the top occupied position of the array
        * 
        * @return Card cardToPlay
        */
       public Card playCard()
       {
         this.numCards--;
           return myCards[this.numCards];
       }
       
       /**
        * To work in the cardGameFramework class, this method will remove the 
        * card at a location and slide all of the cards down one spot in the
        *  myCards array.
        * @param cardIndex
        * @return
        */
       public Card playCard( int cardIndex )
       {
          if ( numCards == 0 ) //error
          {
             //Creates a card that does not work
             return new Card('M', Card.Suit.spades);
          }
          //Decreases numCards.
          Card card = myCards[cardIndex];
          
          numCards--;
          for( int i = cardIndex; i < numCards; i++ )
          {
             myCards[i] = myCards[i + 1];
          }
          
          myCards[numCards] = null;
          
          return card;
        }

       /**
        * stringizer that the client can use to display the entire hand
        * 
        * @return String entireHand
        */
       public String toString()
       {
          if (this.numCards == 0)
          {
             return "Your hand is empty";
          }
          String entireHand = "";
          for (int i = 0; i < (this.numCards - 1); i++) 
          { 
             entireHand += this.myCards[i] + ", ";
          }
          // last card does not need ", " after
          entireHand += this.myCards[this.numCards - 1]; 
          return entireHand;
       }

       /**
        * Accessor for numCards
        * 
        * @return int numCards
        */
       public int getNumCards()
       {
           return numCards;
       }

       /**
        * Accessor for an individual card
        * Returns a card with errorFlag = true if k is bad
        * 
        * @return String entireHand
        */
       public Card inspectCard(int k)
       {
          if (k >= numCards)
          {
             // make card with errorFlag = true
             Card badCard = new Card(true);
             return badCard;
          }
          return myCards[k];
       }
       
       // sort the hand by calling the arraySort() method in the Card class.
       public void sort()
       {
           if ( this.getNumCards() != 0 )
               Card.arraySort(myCards, numCards);
       }
       
   }
   public static class GUICard
   {
      /*
       * The 52 + 4 jokers Icons will be read and stored into the iconCards[][] array.
       * The card-back image in the iconBack member.  None of these data need to be 
       * stored more than once, so this is a class without instance data.  This class 
       * is used is to produce an image icon when the client needs one. 
       */
      // 14 = A thru K + joker
      private static Icon[][] iconCards = new ImageIcon[14][4];
      
      private static Icon iconBack;
      private static boolean iconsLoaded = false;
      
      /*
       * loadCardIcons method similar to Phase 1, but storing the Icons in a 2-D array
       * Doesn't require the client to call this method. 
       */
      public static void loadCardIcons()
      {
         /*
          * Build the file names in a loop such as AC.gif, 2C.gif, TC.gif, etc.
          * For each file name, read it in and use it to instantiate each of
          * the 57 Icons in the Icon 2-D array
          */
         if( !iconsLoaded )
         {
            for( int i = 0; i < 4; i++ )
            {
               for( int j = 0; j < 14; j++ )
               {
                  String file = "images/" + turnIntIntoCardValue(j) +
                        turnIntIntoCardSuit(i) + ".gif";
                  iconCards[j][i] = new ImageIcon(file);
               }
            }
            
            iconBack = new ImageIcon("images/BK.gif");
            iconsLoaded = true;
         }
      }
      
      // converts int to card value (0 - 14 --> "A", "2",..., "Q", "K", etc)
      public static String turnIntIntoCardValue( int j )
      {
         String[] cardValues = 
            {"A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K",
                  "X"};
         
         if( j >= 0 && j <= 13 )
            return cardValues[j];
         
         return "";
      }
      
      // converts int to card suits ("D", "C", "H", "S")
      public static String turnIntIntoCardSuit( int k )
      {
         String[] suites = {"C", "D", "H", "S"};
         
         if( k >= 0 && k <= 3 )
            return suites[k];
         
         return "";
      }
      
      public static Icon getIcon( Card card )
      {
         return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
      }
      
      public static Icon getBackCardIcon()
      {
         return iconBack;
      }
      
   }
   //does not declare a static final serialVersioUID field type long
   @SuppressWarnings("serial")
   public static class CardTable extends JFrame
   {
      /*
       * the following five members are needed is to establish the grid layout 
       * for the JPanels, the organization of which depends on how many cards and
       * players will be displayed.
       */
      public static final int MAX_CARDS_PER_HAND = 56;
      public static final int MAX_PLAYERS = 2;  //for now, we only allow 2 person games
         
      private int numCardsPerHand;
      private int numPlayers;
      
      public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea, pnlMsgArea,
                     pnlCenterArea;
      
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
         pnlHumanHand = new JPanel( new GridLayout(1, numCardsPerHand) );
         
         pnlCenterArea = new JPanel( new GridLayout(2, 1) );
         pnlPlayArea = new JPanel( new GridLayout(2, 2) );
         pnlMsgArea = new JPanel();
         
         setLayout( new BorderLayout(20, 10));
         
         this.add( pnlComputerHand, BorderLayout.NORTH );
         //this.add( pnlPlayArea, BorderLayout.CENTER );
         pnlCenterArea.add(pnlPlayArea);
         pnlCenterArea.add(pnlMsgArea);
         this.add( pnlCenterArea, BorderLayout.CENTER );
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
   
   public static class CardGameFramework
   {
    private static final int MAX_PLAYERS = 50;

    private int numPlayers;
    private int numPacks;            // # standard 52-card packs per deck
                                     // ignoring jokers or unused cards
    private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
    private int numUnusedCardsPerPack;  // # cards removed from each pack
    private int numCardsPerHand;        // # cards to deal each player
    private Deck deck;               // holds the initial full deck and gets
                                     // smaller (usually) during play
    private Hand[] hand;             // one Hand for each player
    private Card[] unusedCardsPerPack;   // an array holding the cards not used
                                         // in the game.  e.g. pinochle does not
                                         // use cards 2-8 of any suit

    public CardGameFramework( int numPacks, int numJokersPerPack,
          int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
          int numPlayers, int numCardsPerHand)
    {
       int k;

       // filter bad values
       if (numPacks < 1 || numPacks > 6)
          numPacks = 1;
       if (numJokersPerPack < 0 || numJokersPerPack > 4)
          numJokersPerPack = 0;
       if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
          numUnusedCardsPerPack = 0;
       if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
          numPlayers = 4;
       // one of many ways to assure at least one full deal to all players
       if  (numCardsPerHand < 1 ||
             numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
             / numPlayers )
          numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

       // allocate
       this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
       this.hand = new Hand[numPlayers];
       for (k = 0; k < numPlayers; k++)
          this.hand[k] = new Hand();
       deck = new Deck(numPacks);

       // assign to members
       this.numPacks = numPacks;
       this.numJokersPerPack = numJokersPerPack;
       this.numUnusedCardsPerPack = numUnusedCardsPerPack;
       this.numPlayers = numPlayers;
       this.numCardsPerHand = numCardsPerHand;
       for (k = 0; k < numUnusedCardsPerPack; k++)
          this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

       // prepare deck and shuffle
       newGame();
    }

    // constructor overload/default for game like bridge
    public CardGameFramework()
    {
       this(1, 0, 0, null, 4, 13);
    }

    public Hand getHand(int k)
    {
       // hands start from 0 like arrays

       // on error return automatic empty hand
       if (k < 0 || k >= numPlayers)
          return new Hand();

       return hand[k];
    }

    public Card getCardFromDeck() { return deck.dealCard(); }

    public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

    public void newGame()
    {
       int k, j;

       // clear the hands
       for (k = 0; k < numPlayers; k++)
          hand[k].resetHand();

       // restock the deck
       deck.init(numPacks);

       // remove unused cards
       for (k = 0; k < numUnusedCardsPerPack; k++)
          deck.removeCard( unusedCardsPerPack[k] );

       // add jokers
       for (k = 0; k < numPacks; k++)
          for ( j = 0; j < numJokersPerPack; j++)
             deck.addCard( new Card('X', Card.Suit.values()[j]) );

       // shuffle the cards
       deck.shuffle();
    }

    public boolean deal()
    {
       // returns false if not enough cards, but deals what it can
       int k, j;
       boolean enoughCards;

       // clear all hands
       for (j = 0; j < numPlayers; j++)
          hand[j].resetHand();

       enoughCards = true;
       for (k = 0; k < numCardsPerHand && enoughCards ; k++)
       {
          for (j = 0; j < numPlayers; j++)
             if (deck.getNumCards() > 0)
                hand[j].takeCard( deck.dealCard() );
             else
             {
                enoughCards = false;
                break;
             }
       }

       return enoughCards;
    }

    public void sortHands()
    {
       int k;

       for (k = 0; k < numPlayers; k++)
          hand[k].sort();
    }

    public Card playCard(int playerIndex, int cardIndex)
    {
       // returns bad card if either argument is bad
       if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
           cardIndex < 0 || cardIndex > numCardsPerHand - 1)
       {
          //Creates a card that does not work
          return new Card('M', Card.Suit.spades);      
       }
    
       // return the card played
       return hand[playerIndex].playCard(cardIndex);
    
    }


    public boolean takeCard(int playerIndex)
    {
       // returns false if either argument is bad
       if (playerIndex < 0 || playerIndex > numPlayers - 1)
          return false;
      
        // Are there enough Cards?
        if (deck.getNumCards() <= 0)
           return false;

        return hand[playerIndex].takeCard(deck.dealCard());
    }

   }
   public static class Deck
   {

      /** Define a public final int value like MAX_CARDS, and initialize it to 
      * allow a maximum of six packs (6×56 cards).
      */
      public final int MAX_CARDS = 312; // max 6 decks of 56 cards
         
      private static Card[] masterPack;
      private Card[] cards;
      private int topCard;
      private int numPacks;
      public static boolean haveAllocatedMasterPack = false;

      /** constructors that populate the arrays and assigns initial values to
       *  members. Overload so that if no parameters are passed, 1 pack is assumed.
       */ 
       public Deck()
       {
         this.numPacks = 1;
           init(numPacks);
           //allocateMasterPack();
       }

       // A constructor that populates the arrays and assigns initial values to
       // members with the assistance of init(). This constructor is an overload
       // of the default constructor allowing for an parameter being set with the 
       // number of packs the deck will contain.
       // Takes one parameter, an int numPacks, that is used to to create a deck
       // of cards that is a combination of more than one pack
       public Deck(int numPacks)
       {
         this.numPacks = numPacks;
           init(numPacks);
           //allocateMasterPack();
       }

       // This public method initializes a deck of card according to the parameter
       // numPacks which is passed to it. This method calls on private method
       // allocateMasterPack() which sets the static array with 52 cards used to 
       // create each pack that is added to the Deck.
       // Here the private members numPacks and topCard are allso set accordingly.
       // The parameter int numPacks tells the method how many packs of cards are
       // to be added to the Deck from the masterPack.
       public void init(int numPacks)
       {
           allocateMasterPack();
           this.numPacks = numPacks;
           this.topCard = (52 * this.numPacks) - 1;

           cards = new Card[52 * numPacks];

           for (int pack = 0; pack < numPacks; pack++)
           {
               for (int card = 0; card < masterPack.length; card++)
               {
                   cards[(52 * pack) + card] = masterPack[card];
               }
           }
       }

       // This public method is used to randomly shuffle the cards within a deck
       // so that all card are out of sequence.
       public void shuffle()
       {
           int split = cards.length / 2;
           Random rand = new Random();
           int shufCount = 5 * (52 * numPacks);

           do
           {
               int cardA = rand.nextInt(split);
               int cardB = rand.nextInt(split) + split;

               Card temp = cards[cardA];
               cards[cardA] = cards[cardB];
               cards[cardB] = temp;

               shufCount--;
           } while (shufCount != 0);

       }

       // A public method that returns the card on the top of the deck replacing
       // its position in the array with a null, and moving the topCard private
       // member to the next card in the deck.
       public Card dealCard()
       {
           Card retVal;
           if (topCard < 0)
           {
               retVal = new Card('B', Card.Suit.clubs);
           }
           else
           {
               retVal = cards[topCard];
               cards[topCard] = null;
               topCard--;
           }
           return retVal;
       }

       // This is an accessor method used to retreive the position of the current
       // top card in the deck array.
       public int getTopCard()
       {
           return topCard;
       }
       
       // Accessor for an individual card. 
       //Returns a card with errorFlag = true if k is bad
       public Card inspectCard(int k)
       {
           if (cards[k].getErrorFlag())
               return cards[k];
           else
               return new Card();
           
       }
       
       // This private method is used to fill the masterPack array with 52 unique
       // cards, which can than be used to populate a deck as needed. This method
       // will only run when the first deck in the program is initialized. If the
       // masterPack array is not null the method simply end makeing no changes to
       // the masterPack array.
       /** this is a private method that will be called by the constructor; will not 
        *  allow itself to be executed more than once
        */
       
       private static void allocateMasterPack()
       {
           if (masterPack == null)
           {
               masterPack = new Card[52];

               for (Card.Suit sVal : Card.Suit.values())
               {
                   for (int k = 1; k < 14; k++)
                   {
                       Card newCard;
                       int value = k % 14;
                       
                       switch (value)
                       {
                           case 1:
                               newCard = new Card('A', sVal);
                               break;
                           case 10:
                               newCard = new Card('T', sVal);
                               break;
                           case 11:
                               newCard = new Card('J', sVal);
                               break;
                           case 12:
                               newCard = new Card('Q', sVal);
                               break;
                           case 13:
                               newCard = new Card('K', sVal);
                               break;
                           default:
                               newCard = new Card(Integer.toString(value).charAt(0), sVal);
                       }
                       
                       if (sVal == Card.Suit.clubs)
                       {
                           masterPack[k - 1] = newCard;
                       }
                       else if (sVal == Card.Suit.diamonds)
                       {
                           masterPack[(k - 1) + 13] = newCard;
                       }
                       else if (sVal == Card.Suit.hearts)
                       {
                           masterPack[(k - 1) + 26] = newCard;
                       }
                       else // default to spades
                       {
                           masterPack[(k - 1) + 39] = newCard;
                       }
                   }
               }
           }
       }
       
       /**
        * Add cards to deck; ensure no extra instances of card added and
        * return false if too many
        * @param card
        * @return
        */
       public boolean addCard( Card card )
       {
         int numOccurances = 0;
         
         for( int i = 0; i <= topCard; i++ )
         {
            if( cards[i].equals(card) )
               numOccurances++;
         }
         
         if( numOccurances > numPacks )
            return false;
         
         cards[topCard++] = card;
         return true;
       }
       
       /**
        * Remove cards from deck; replace current topCard in its place
        * and return false if target card is not in deck
        */
       public boolean removeCard( Card card )
       {
        int j;
        boolean found = false;
        
        for( j = 0; j < topCard; j++ )
        {
           while( cards[j].equals(card) )
           {
              if( j >= topCard )
                 break;
              
              cards[j] = cards[topCard - 1];
              topCard--;
              found = true;
           }
        }
        
        return found;
       }
       
       /**
        * return the number of cards remaining in the deck.
        * @return count
        */
       public int getNumCards()
       {
         int count = 0;
         
         for( Card card : cards )
         {
            if( card != null )
               count++;
         }
         return count;
       }
       
       /**
        * put all of the cards in the deck back into the right order 
        * according to their values; call Card class arraySort to do sort
        */
       public void sort()
       {
         if( this.getNumCards() != 0 )
         {
            Card.arraySort( cards, numPacks * 52 );
         }
       }
       
   }   
}
