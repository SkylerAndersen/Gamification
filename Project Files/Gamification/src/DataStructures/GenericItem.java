package DataStructures;

import javax.swing.*;
import java.awt.*;

public class GenericItem {
    private String name;
    private int cost;
    private JLabel inventoryLook;
    private JLabel characterLook;
    private String inventoryLookName;
    private String characterLookName;

    public GenericItem (String name, int cost, String inventoryLookName, String characterLookName) {
        this.name = name;
        this.cost = cost;
        this.inventoryLookName = inventoryLookName;
        this.characterLookName = characterLookName;
        inventoryLook = new JLabel();
        characterLook = new JLabel();
        inventoryLook.setHorizontalAlignment(SwingConstants.CENTER);
        inventoryLook.setVerticalAlignment(SwingConstants.CENTER);
        inventoryLook.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventoryLook.setAlignmentY(Component.CENTER_ALIGNMENT);
        characterLook.setHorizontalAlignment(SwingConstants.CENTER);
        characterLook.setVerticalAlignment(SwingConstants.CENTER);
        characterLook.setAlignmentX(Component.CENTER_ALIGNMENT);
        characterLook.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    public void load (FileHandler fileHandler) {
        Image icon = fileHandler.retrieveImage(inventoryLookName).getImage();
        Image scaled = icon.getScaledInstance(35, icon.getHeight(null) * 35 /
                icon.getWidth(null),Image.SCALE_AREA_AVERAGING);
        inventoryLook.setIcon(new ImageIcon(scaled));

        characterLook.setIcon(fileHandler.retrieveImage(characterLookName));
        inventoryLook.repaint();
        characterLook.repaint();
    }

    public JLabel getInventoryLook () {
        return inventoryLook;
    }

    public JLabel getCharacterLook () {
        return characterLook;
    }

    public String getName () {
        return name;
    }

    public int getCost () {
        return cost;
    }

    public String getInventoryLookName () {
        return inventoryLookName;
    }

    public String getCharacterLookName () {
        return characterLookName;
    }
}
