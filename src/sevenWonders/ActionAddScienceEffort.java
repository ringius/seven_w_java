package sevenWonders;

/**
 * Created by Jan on 2017-02-02.
 */
public class ActionAddScienceEffort extends Action {
    private University.ResearchType type;
    private int amount;

    public ActionAddScienceEffort() {
        type = University.ResearchType.None;
        amount = 0 ;
    }

    public ActionAddScienceEffort(University.ResearchType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public void setType(University.ResearchType type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public University.ResearchType getType() {
        return this.type;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean perform(Player p) {
        if (p != null ) {
            p.getUniversity().addResearch(this.type, this.amount);
        }
        return true;
    }
}
