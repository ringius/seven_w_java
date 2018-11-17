package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public abstract class MetaWonder {
    protected Wonder a;
    protected Wonder b;
    protected String name;
    protected boolean claimed;

    public MetaWonder(String name) {
        this.name = name;
        a = null;
        b = null;
        this.claimed = false;
    }

    public String getName() {
        return name;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public Wonder pick(boolean side_a) {
        if (claimed)
            return null;

        claimed = true;
        if (side_a) {
            return generateA();
        } else {
            return generateB();
        }
    }

    abstract protected Wonder generateA();

    abstract protected Wonder generateB();
}
