package BlackJack_Sim;
import java.util.ArrayList;

class BlackjackGame {
    Deck deck;

    // Dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    // Player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    public BlackjackGame() {
        startGame();
    }

    public void startGame() {
        deck = new Deck();

        // Dealer setup
        dealerHand = new ArrayList<>();
        dealerSum = 0;
        dealerAceCount = 0;

        // Player setup
        playerHand = new ArrayList<>();
        playerSum = 0;
        playerAceCount = 0;
    }

    public void dealInitialCards() {
        // Dealer's initial cards
        hiddenCard = deck.drawCard();
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.drawCard();
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        // Player's initial cards
        for (int i = 0; i < 2; i++) {
            card = deck.drawCard();
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount--;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount--;
        }
        return dealerSum;
    }
}
