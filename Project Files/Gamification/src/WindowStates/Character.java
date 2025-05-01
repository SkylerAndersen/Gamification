package WindowStates;

import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;
import DataStructures.PlayerData;
import DataStructures.GenericItem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Character window: displays the player sprite and a 6×3 inventory grid
 * with selection highlighting and equip/unequip controls.
 */
public class Character extends WindowState {

    // --- CONFIG ---
    private static final int INV_COLS    = 6;
    private static final int INV_ROWS    = 3;
    private static final int SLOT_SIZE   = 50;    // px

    // --- UI COMPONENTS ---
    private JLabel       characterLabel;    // slot for composed character sprite
    private JLabel       levelLabel;
    private JLabel       xpLabel;
    private JLabel       coinLabel;
    private JPanel       inventoryPanel;    // 6×3 grid for inventory icons
    private JButton      equipButton;       // applies selected equip
    private JButton      unequipAllButton;  // clears all equips

    // --- STATE ---
    private FileHandler         fileHandler;
    private List<GenericItem>   inventory       = new ArrayList<>();
    private List<GenericItem>   equippedItems   = new ArrayList<>();
    private List<JLabel>        inventoryCells  = new ArrayList<>();
    private int                 selectedIndex   = -1;
    private final Border        defaultBorder   = BorderFactory.createEmptyBorder(2,2,2,2);
    private final Border        highlightBorder = BorderFactory.createLineBorder(Color.GREEN, 2);

    public Character() {
        super(WindowStateName.CHARACTER);

        // Layout setup
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.BOTH;

        // Back button
        JButton backBtn = new JButton("Back to Mission Select");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        getContentPane().add(backBtn, gbc);
        backBtn.addActionListener(e -> {
            setCloseEvent(WindowStateEvent.SWITCH_STATE);
            setNextWindow(WindowStateName.MISSION_SELECT);
            close();
        });

        //stat panel to the left of character
        JPanel stats = new JPanel();
        stats.setLayout(new GridBagLayout());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0;
        getContentPane().add(stats, gbc);

        //adding labels for statpanel
        xpLabel = new JLabel ("XP: ");
        xpLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0;
        stats.add(xpLabel, gbc);

        levelLabel = new JLabel ("Level: ");
        levelLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        stats.add(levelLabel, gbc);

        coinLabel = new JLabel ("Coin: ");
        coinLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        stats.add(coinLabel, gbc);





        // Character display panel
        JPanel characterPanel = new JPanel();
        characterPanel.setPreferredSize(new Dimension(500, 300));
        characterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1; gbc.weighty = 0.6;
        getContentPane().add(characterPanel, gbc);

        characterLabel = new JLabel("", SwingConstants.CENTER);
        characterLabel.setPreferredSize(new Dimension(200, 200));
        characterPanel.add(characterLabel);

        // Items header
        JLabel itemsHeader = new JLabel("Items", SwingConstants.CENTER);
        itemsHeader.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 0;
        getContentPane().add(itemsHeader, gbc);

        // Inventory grid
        inventoryPanel = new JPanel();
        inventoryPanel.setPreferredSize(new Dimension(500, 200));
        inventoryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        inventoryPanel.setLayout(new GridLayout(INV_ROWS, INV_COLS, 5, 5));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 0.4;
        getContentPane().add(inventoryPanel, gbc);

        // Equip/Unequip buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        equipButton      = new JButton("Equip Selected");
        unequipAllButton = new JButton("Unequip All");
        controlPanel.add(equipButton);
        controlPanel.add(unequipAllButton);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 0;
        getContentPane().add(controlPanel, gbc);

        // Button actions
        equipButton.addActionListener(e -> {
            if (selectedIndex >= 0 && selectedIndex < inventory.size()) {
                GenericItem item = inventory.get(selectedIndex);
                if (!equippedItems.contains(item)) {
                    equippedItems.add(item);
                    updateCharacterDisplay();
                }
            }
        });
        unequipAllButton.addActionListener(e -> {
            equippedItems.clear();
            updateCharacterDisplay();
        });
    }

    @Override
    public void onEscapePressed() {
        setNextWindow(WindowStateName.MISSION_SELECT);
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        close();
    }

    @Override
    public void save(FileHandler fileHandler) {
        // no-op
    }

    @Override
    public void load(FileHandler fileHandler) {
        this.fileHandler = fileHandler;

        // Load saved player context
        PlayerData.load(fileHandler);
        xpLabel.setText("XP: " + PlayerData.xp);
        levelLabel.setText("Level: " + PlayerData.level);
        coinLabel.setText("Coin: " + PlayerData.coins);


        // 1) Build inventory list from saved array
        String[] array = fileHandler.retrieveStringArray(PlayerData.name + "-inventory-items");
        Set<String> available = new HashSet<>(Arrays.asList(array));
        inventory.clear();
        if (available.contains("bow"))
            inventory.add(new GenericItem("Bow", 5, "shop-bow", "bow"));
        if (available.contains("single-sword"))
            inventory.add(new GenericItem("Sword", 10, "shop-single-sword", "single-sword"));
        if (available.contains("dual-sword"))
            inventory.add(new GenericItem("Dual Sword", 15, "shop-dual-sword", "dual-sword"));
        if (available.contains("helmet"))
            inventory.add(new GenericItem("Helmet", 20, "shop-helmet", "helmet"));
        if (available.contains("chestplate"))
            inventory.add(new GenericItem("Chestplate", 25, "shop-chestplate", "chestplate"));
        if (available.contains("leggings"))
            inventory.add(new GenericItem("Leggings", 30, "shop-leggings", "leggings"));
        // load icons
        for (GenericItem item : inventory) item.load(fileHandler);

        // 2) Update character display (base + equipped items)
        updateCharacterDisplay();

        // 3) Populate inventory grid with selection
        inventoryCells.clear();
        inventoryPanel.removeAll();
        int slots = INV_COLS * INV_ROWS;
        for (int i = 0; i < slots; i++) {
            JLabel cell = new JLabel("", SwingConstants.CENTER);
            cell.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
            cell.setBorder(defaultBorder);
            final int idx = i;
            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedIndex = idx;
                    updateSelectionHighlight();
                }
            });
            if (i < inventory.size()) {
                GenericItem item = inventory.get(i);
                cell.setIcon(item.getInventoryLook().getIcon());
            }
            inventoryCells.add(cell);
            inventoryPanel.add(cell);
        }
        updateSelectionHighlight();

        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }

    /**
     * Redraws the character label by compositing base sprite with equipped item sprites.
     */
    private void updateCharacterDisplay() {
        int w = characterLabel.getWidth() > 0 ? characterLabel.getWidth() : characterLabel.getPreferredSize().width;
        int h = characterLabel.getHeight() > 0 ? characterLabel.getHeight() : characterLabel.getPreferredSize().height;
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        // draw base
        Image baseImg = fileHandler.retrieveImage("player").getImage()
                .getScaledInstance(w, h, Image.SCALE_SMOOTH);
        g.drawImage(baseImg, 0, 0, null);
        // draw each equipped item on top
        for (GenericItem item : equippedItems) {
            Image overlay = fileHandler.retrieveImage(item.getCharacterLookName()).getImage()
                    .getScaledInstance(w, h, Image.SCALE_SMOOTH);
            g.drawImage(overlay, 0, 0, null);
        }
        g.dispose();
        characterLabel.setIcon(new ImageIcon(combined));
        characterLabel.revalidate();
        characterLabel.repaint();
    }

    /**
     * Highlights the selected inventory cell with a green border.
     */
    private void updateSelectionHighlight() {
        for (int i = 0; i < inventoryCells.size(); i++) {
            JLabel cell = inventoryCells.get(i);
            cell.setBorder(i == selectedIndex ? highlightBorder : defaultBorder);
        }
    }
}
