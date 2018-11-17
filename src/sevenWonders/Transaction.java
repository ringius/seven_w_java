package sevenWonders;

/**
 * Created by Jan on 2017-02-27.
 */
public class Transaction {
    public enum tType {BUY, SELL, INIT}
    private tType type;
    private Solution solution;

    public Transaction(Transaction.tType type, Solution s) {
        this.type = type;
        this.solution = s;
    }

    public Transaction(Transaction.tType type) {
        this(type, null);
    }

    public Transaction.tType getType() {
        return type;
    }

    public Solution getSolution() {
        return solution;
    }
}
