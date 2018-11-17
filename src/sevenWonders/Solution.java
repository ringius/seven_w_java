package sevenWonders;

/**
 * Created by Jan on 2017-01-23.
 */
public class Solution {
    private ResourceList remainingCost;
    private ResourcePayments payment;
    private boolean canAfford;
    private Solutions cascadedSolutions;
    private int priceEast;
    private int priceWest;
    private int priceBank;
    private boolean canAffordPrice;
    private boolean priceCalculated;
    private boolean shallSell;

    public Solution(ResourceList rem, ResourcePayments payment, boolean afforded) {
        this.shallSell = false;
        this.remainingCost = rem;
        this.payment = payment;
        this.canAfford = afforded;
        this.cascadedSolutions = null;
        this.priceEast = -1;
        this.priceWest = -1;

        if (shallSell)
            this.priceBank = 5000;
        else
            this.priceBank = -1;

        canAffordPrice = false;
        priceCalculated = false;
    }

    public Solution(int bankCost, boolean afforded) {
        this(new ResourceList(), new ResourcePayments(), afforded);
        this.priceBank = bankCost;
        this.canAfford = afforded;
    }

    public Solution() {
        this(4711, false);
        this.shallSell = true;
    }

    public boolean isSubsetOf(Solution o) {
        if (canAfford != o.canAfford)
            return false;
        if (priceEast > o.priceEast || priceWest > o.priceWest || priceBank > o.priceBank)
            return false;
        for(RawMaterial.type t : remainingCost.getResources().keySet()) {
            int n1 = remainingCost.getResourceAmount(t);
            int n2 = o.getRemainingCost().getResourceAmount(t);
            if (n1 > n2)
                return false;
        }
        return true;
    }

    public boolean equals(Solution o) {
        boolean sameRemainingCost = remainingCost.equals(o.remainingCost);
        boolean samePayment = payment.equals(o.payment);
        boolean sameAffordStatus = (canAfford == o.canAfford);
        return (sameRemainingCost && samePayment && sameAffordStatus);
    }

    public boolean isAffordable() {
        return canAfford;
    }

    public ResourcePayments getPayment() {
        return payment;
    }

    public ResourceList getRemainingCost() {
        return remainingCost;
    }

    public void sort() {
        payment.sort();
    }

    public void setCascadedSolutions(Solutions cascadedSolutions) {
        this.cascadedSolutions = cascadedSolutions;
    }

    public Solutions getCascadedSolutions() {
        return cascadedSolutions;
    }

    public String toString() {
        String res = new String();
        res += "[ Remaining cost: " + remainingCost + ". Payment: ";
        if (payment == null || payment.size() == 0) {
            res += "[]";
        } else {
            for (ResourcePayment p : payment) {
                res += "[" + p + "], ";
            }
        }

        if (canAfford)
            res += " Resources Available: YES. ";
        else
            res += " Resources Available: NO. ";

        res += " Total cost = " + getTotalCost() + " ]";

        if (cascadedSolutions != null) {
            res += " \n" + cascadedSolutions;
        }
        return res;
    }

    public void setPriceEast(int val) {
        priceEast = val;
    }

    public void setPriceWest(int val) {
        priceWest = val;
    }

    public void setPriceBank(int val) {
        priceBank = val;
    }

    public int getPriceEast() {
        return priceEast;
    }

    public int getPriceWest() {
        return priceWest;
    }

    public int getPriceBank() {
        return priceBank;
    }

    public void setCanAffordPrice(boolean v) {
        canAffordPrice = v;
    }

    public boolean canAffordPrice() {
        return canAffordPrice;
    }

    public int getTotalCost() {
        return (Math.max(priceWest,0) +
                Math.max(priceEast,0) +
                Math.max(priceBank,0));
    }

    public boolean isPriceCalculated() {
        return priceCalculated;
    }

    public void setPriceIsCalculated(boolean priceIsCalculated) {
        this.priceCalculated = priceIsCalculated;
    }

    public String textDescripton() {
        if (!isAffordable()) {
            String res = "You cannot buy this card (Missing ";
            res += getRemainingCost().toString() + ").";
            return res;
        } else {
            String res = new String();
            res += "Total cost: " + getTotalCost();
            if (priceEast > 0 || priceWest > 0) {
                res += "(";
                if (priceEast > 0)
                    res += ("East: " + priceEast + ".");
                if (priceWest > 0)
                    res += ("West: " + priceWest + ".");
                res += ")";
            }
            return res;
        }
    }

    public boolean shallSell() {
        return shallSell;
    }
}
