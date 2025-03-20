package WindowStates;

import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;

import javax.swing.*;
import java.awt.*;

public class Character extends WindowState {

    private JLabel characterLabel; // Label for character image/text

    public Character() {
        super(WindowStateName.CHARACTER);

        // Set GridBagLayout for spacing
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH; // Expands components to fill space

        //TOP SECTION - CHARACTER DISPLAY
        JPanel characterPanel = new JPanel();
        characterPanel.setPreferredSize(new Dimension(500, 300));
        characterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.6;
        getContentPane().add(characterPanel, gbc);

        // Character label (Text Placeholder for now, Replaceable with Image)
        characterLabel = new JLabel("Character Image Here", SwingConstants.CENTER);
        characterLabel.setFont(new Font("Arial", Font.BOLD, 20));
        characterPanel.add(characterLabel);

        // ITEMS LABEL
        JLabel itemLabel = new JLabel("Items");
        itemLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        getContentPane().add(itemLabel, gbc);

        // ✅ BOTTOM SECTION - ITEM SELECTION
        JPanel itemPanel = new JPanel(new GridBagLayout());
        itemPanel.setPreferredSize(new Dimension(500, 200));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        gbc.gridy = 2;
        getContentPane().add(itemPanel, gbc);

        // New GridBagConstraints for the itemPanel buttons
        GridBagConstraints itemGbc = new GridBagConstraints();
        itemGbc.insets = new Insets(5, 5, 5, 5);
        itemGbc.fill = GridBagConstraints.HORIZONTAL;

        // Buttons for item selection
        JButton helmet = new JButton("Helmet");
        itemGbc.gridx = 0;
        itemGbc.gridy = 0;
        itemPanel.add(helmet, itemGbc);
        helmet.addActionListener(e -> updateCharacter("Wearing Helmet"));

        JButton armor = new JButton("Armor");
        itemGbc.gridx = 1;
        itemGbc.gridy = 0;
        itemPanel.add(armor, itemGbc);
        armor.addActionListener(e -> updateCharacter("Wearing Armor"));

        JButton legs = new JButton("Legs");
        itemGbc.gridx = 0;
        itemGbc.gridy = 1;
        itemPanel.add(legs, itemGbc);
        legs.addActionListener(e -> updateCharacter("Wearing Leggings"));

        JButton weapons = new JButton("Weapon");
        itemGbc.gridx = 1;
        itemGbc.gridy = 1;
        itemPanel.add(weapons, itemGbc);
        weapons.addActionListener(e -> updateCharacter("Holding Weapon"));
    }

    // Updates character panel (Text-based or Image-based)
    private void updateCharacter(String text) {
        characterLabel.setText(text);


    }

    @Override
    public void onEscapePressed () {
        setNextWindow(WindowStateName.MISSION_SELECT);
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        close();
    }

    @Override
    public void save(FileHandler fileHandler) {}

    @Override
    public void load(FileHandler fileHandler) {}
}


