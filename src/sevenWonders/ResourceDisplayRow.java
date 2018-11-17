package sevenWonders;

import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jan on 2017-02-25.
 */
public class ResourceDisplayRow {
    private ResourceDisplay resourceDisplay;
    private RawMaterial.type material;
    private Label text;
    private HashMap<Integer, Label> labels;
    private int row;
    private Label publicAmount;
    private Label privateAmount;

    public ResourceDisplayRow(ResourceDisplay resourceDisplay, RawMaterial.type type, String text, int row) {
        this.resourceDisplay = resourceDisplay;
        this.material = type;
        this.text = new Label(text);
        labels = new HashMap();
        this.publicAmount = new Label("0");
        this.privateAmount = new Label("0");
        this.row = row;
        labels.put(0, this.text);
        labels.put(1, this.publicAmount);
        labels.put(2, this.privateAmount);
    }

    public void updatePublic(ResourceBank bank) {
        publicAmount.setText("" + bank.getAmountOfMaterial(material) + " ");
    }

    public void updatePrivate(ResourceBank bank) {
        privateAmount.setText("" +bank.getAmountOfMaterial(material) + " ");
    }

    public String getName() {
        return text.getText();
    }

    public void addToGrid() {
        for (Integer i : labels.keySet()) {
            resourceDisplay.add(labels.get(i), i, this.row);
        }
    }

    public void setOne(int col) {
        if (labels.get(col) == null) {
            Label text = new Label("1");
            labels.put(col, text);
            resourceDisplay.add(text, col, this.row);
        } else {
            Label l = labels.get(col);
            if (l == null)
                labels.put(col, new Label("tjohoo"));
        }
        labels.get(col).setText("1");
    }

    public void setText(int col, String s) {
        Label l = labels.get(col);
        if (l == null) {
            l = new Label();
            labels.put(col, l);
            resourceDisplay.add(l, col, this.row);
        }
        l.setText(s);
    }

    public void clearRow(int col) {
        for (Label l : labels.values()) {
            l.setText("");
        }
    }

    public void clearColumn(int colIndex) {
        setText(colIndex, "");
    }
}
