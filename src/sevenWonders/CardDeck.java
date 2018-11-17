package sevenWonders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Jan on 2017-01-09.
 */
public class CardDeck {
    private CardList cards;

    public CardDeck() {
        cards = new CardList();
    }

    //Adds card to the bottom of the deck.
    public void addCard(PlayCard cardToAdd){
        cards.add(cardToAdd);
    }

    public void shuffleDeck(){
        for (int i=0;i<cards.size();i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(i, cards.size());
            Collections.swap(cards, i, randomNum);
        }
    }

    /**
     * Shuffles the deck and splits it into noOfDecks new CardDecks.
     * No consideration to whether all subdecks will be of equal size is done.
     * @param noOfDecks
     * @return ArrayList of sub-decks.
     */
    ArrayList<CardDeck> split(int noOfDecks) {
        shuffleDeck();
        ArrayList<CardDeck> result = new ArrayList<CardDeck>();
        for (int i=0;i<noOfDecks;i++) {
            result.add(new CardDeck());
        }
        Iterator<PlayCard> iter=cards.iterator();
        int curDeck = 0;
        while(iter.hasNext()) {
            result.get(curDeck).addCard(iter.next());
            iter.remove();
            curDeck++;
            if(curDeck >= noOfDecks)
                curDeck = 0;
        }
        return result;
    }

    public boolean haveCard(PlayCard card) {
        return cards.contains(card);
    }

    public PlayCard pickCard(PlayCard card) {
        PlayCard res = card;
        cards.remove(card);
        return res;
    }

    public String toString() {
        String res = "card deck. Size = " + cards.size() + ".\n";
        for(int i=0;i<cards.size();i++) {
            res+=cards.get(i).toString()+"\n";
        }
        return res;
    }

    public PlayCard getCard(int i) {
        if (cards.size() > i) {
            return cards.get(i);
        } else {
            return null;
        }
    }

    public int size() {
        return cards.size();
    }

    public CardList getCardsAsList() {
        return cards;
    }
}
