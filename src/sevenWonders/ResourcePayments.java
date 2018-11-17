package sevenWonders;

import java.util.ArrayList;

/**
 * Created by Jan on 2017-01-24.
 */
public class ResourcePayments extends ArrayList<ResourcePayment> {

    public ResourcePayments() {
        super();
    }

    public ResourcePayments(ResourcePayments original) {
        super(original);
    }

    public void  sort() {
        sort((p1, p2)-> (RawMaterial.string(p1.getMaterialType()).compareTo(RawMaterial.string(p2.getMaterialType()))));
    }

    public boolean equals(ResourcePayments o) {
        if (isEmpty() && o.isEmpty())
            return true;

        if(size() != o.size())
            return false;


        int foundEquals = 0;
        for (int i=0;i<size();i++) {
            for (int j = 0; j < size(); j++) {
                if (get(i).equals(o.get(j)))
                    foundEquals++;
            }
        }
        return foundEquals == size();
    }

    public ArrayList<ResourcePayments> getAllCombinations() {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        ArrayList<ResourcePayments> endResult = new ArrayList<>();
        for (int i=1;i<size()+1;i++) {
            ArrayList<ArrayList<Integer>> pRes = getComb(i, 0);
            for (ArrayList<Integer> s : pRes) {
                res.add(s);
            }
        }

        for (ArrayList<Integer> item : res) {
            endResult.add(formResourcePayments(item));
        }
        return endResult;
    }

    protected ResourcePayments formResourcePayments(ArrayList<Integer>indexList) {
        ResourcePayments p = new ResourcePayments();
        for (Integer i : indexList) {
            ResourcePayment pItem = get(i.intValue());
            p.add(pItem);
        }
        return p;
    }

    public ArrayList<ArrayList<Integer>> getComb(int size, int startIndex) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();

        if (size == 0 || startIndex + size > size()) {
            res.add(new ArrayList<Integer>());
            return res;
        }

        int first;
        for (int i = startIndex; i < (size()+1 - size); i++) {
            first = i;
            ArrayList<ArrayList<Integer>> partRes = getComb(size - 1, i + 1);
            for (ArrayList<Integer> l : partRes) {
                l.add(i);
                res.add(l);
            }
        }
        return res;
    }

    //Simply add the list of payments to his
    public void addPayment(ResourcePayments p) {
        if (p == this) {
            return;
        }
        for (ResourcePayment pItem : p) {
            this.add(pItem);
        }
    }

    public ResourcePayments plus(ResourcePayments pay) {
        ResourcePayments p = new ResourcePayments(this);
        p.addPayment(pay);
        return p;
    }
}
