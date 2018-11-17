package sevenWonders;

import java.util.ArrayList;

/**
 * Created by Jan on 2017-01-24.
 */
public class CardList extends ArrayList<PlayCard> {

    public String toString() {
        if (size() == 0) {
            return "[]";
        }

        String res = "[";
        for (PlayCard c : this) {
            res += c + ",";
        }
        String r = res.substring(0, res.length()-1);
        r += "]";
        return r;
    }
}
