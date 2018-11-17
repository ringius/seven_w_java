package sevenWonders;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Jan on 2017-02-01.c1
 */
public class CardReader {

    private HashMap<Integer, CardDeck> decks;

    public CardReader() {
        decks = new HashMap<>();
        decks.put(1, new CardDeck());
        decks.put(2, new CardDeck());
        decks.put(3, new CardDeck());
        decks.put(4, new CardDeck());
    }

    public void initDecks(int noOfPlayers) {
        PlayCard[] cards = readCardsFromFile();

        for (PlayCard p : cards) {

            int nop = p.getMinNoOfPlayers();
            if (nop <= noOfPlayers) {
                int a = p.getAge();
                CardDeck d = decks.get(a);
                if (d == null) {
                    System.out.println("Wrong");
                }
                d.addCard(p);
            }
        }

        // Add a number of Guild cards to age 3
        int noOfGuildCards = noOfPlayers + 2;
        for (int i=0;i<noOfGuildCards;i++) {
            decks.get(3).addCard(decks.get(4).getCard(i));
        }

        for (CardDeck c : decks.values()) {
            c.shuffleDeck();
        }
    }

    public PlayCard[] readCardsFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();

        PlayCard sm[] = null;
        try {
            FileInputStream in = new FileInputStream("cards.json");
            sm = objectMapper.readValue(in, PlayCard[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<Integer, PlayCard>cardMap = new HashMap<>();

        for (PlayCard p: sm) {
            cardMap.put(p.getId(), p);
        }
        //setup predecessors and load image file
        for (PlayCard p : sm) {
            p.initPredRefs(cardMap);
            p.loadImage();
        }

        return sm;

    }

    public CardDeck getDeck(int age) {
        if (age < 1 || age > 3) {
            return null;
        }
        return decks.get(age);
    }

    public static void testCardReader() {
        CardReader r = new CardReader();
        r.initDecks(3);
        CardDeck d = r.getDeck(1);
        ArrayList<CardDeck> l = d.split(3);
        for (CardDeck c:l) {
            System.out.println(c);
        }

        System.out.println("done");
        System.exit(0);
    }
}
