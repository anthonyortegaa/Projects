package BlackJack_Sim;

import java.util.ArrayList;
import java.util.Collections;


class Deck {
    ArrayList<Card> cards;

    public Deck() {
        buildDeck();
        shuffleDeck();
    }

    public void buildDeck() {
        cards = new ArrayList<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (String type : types) {
            for (String value : values) {
                Card card = new Card(value, type);
                cards.add(card);
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }
}