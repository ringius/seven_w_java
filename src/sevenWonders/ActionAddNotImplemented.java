package sevenWonders;

/**
 * Created by Jan on 2017-02-03.
 */
public class ActionAddNotImplemented extends Action {
    public ActionAddNotImplemented() {

    }

    public boolean perform(Player p) throws Exception {
       Exception e = new Exception("Action not implemented for card ");
       System.out.println(e);
       return false;
    }
}
