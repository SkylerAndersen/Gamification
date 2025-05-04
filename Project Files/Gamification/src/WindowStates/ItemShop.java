package WindowStates;

import ApplicationDefaults.*;
import DataStructures.FileHandler;
import DataStructures.GenericItem;
import DataStructures.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemShop extends WindowState implements AcceptMouseResponse {
    private JPanel popup, cart, cartContent, total, background, row, row2, grid, inventoryGrid, coins, coinWrapper;
    private boolean popupOpen, shopView, confirmDialog;
    private int runningTotalCart, purse;
    private MouseController mouseController;
    private ResizeListener headerListener, contentListener;
    private JFrame parentFrame;
    private ResizeListener rowResize, rowResize2;
    private ArrayList<GenericItem> cartItems, shopItems, inventoryItems;
    private Dimension dialogControl;
    private static final int BOW_COST = 100, SWORD_COST = 200, DUAL_SWORD_COST = 400, HELMET_COST = 600,
    CHESTPLATE_COST = 900, LEGGINGS_COST = 900;

    public ItemShop(JFrame parentFrame) {
        // setup frame
        super(WindowStateName.ITEM_SHOP);
        this.parentFrame = parentFrame;

        // setup a popup UI
        popup = new BevelPanel();
        popup.setBackground(Color.PINK);
        ((BevelPanel) popup).setRoundTop(true);
        ((BevelPanel) popup).setRoundBottom(true);
        ((BevelPanel) popup).setRounding(10);
        popup.setOpaque(false);
        popupOpen = false;
        runningTotalCart = 0;
        purse = 0;
        confirmDialog = false;

        // setup capacities
        shopItems = new ArrayList<>(6);
        inventoryItems = new ArrayList<>(6);

        // setup UI
        shopView = true;
        cartItems = new ArrayList<>();
        background = setupUI();

        // setup bounds and add items
        background.setBounds(0,0,1200,800);
        popup.setBounds(0,0,100,100);
        getContentPane().add(background,0);
        parentFrame.addComponentListener(new ResizeListener(parentFrame,background));
        mouseController = new MouseController(this);
        parentFrame.addMouseListener(mouseController.getMouseListener());
        parentFrame.addMouseMotionListener(mouseController.getMouseMotionListener());
    }

    public void updateShop () {
        grid.removeAll();
        grid.setLayout(new GridLayout(4,11));
        for (int i = 0; i < 44; i++)
            if (i < shopItems.size())
                grid.add(setupItem(shopItems.get(i)));
            else
                grid.add(setupItem());
        grid.repaint();
        grid.revalidate();
    }

    public void updateInventory () {
        inventoryGrid.removeAll();
        inventoryGrid.setLayout(new GridLayout(4,11));
        for (int i = 0; i < 44; i++)
            if (i < inventoryItems.size())
                inventoryGrid.add(setupItem(inventoryItems.get(i),true));
            else
                inventoryGrid.add(setupItem());
        inventoryGrid.repaint();
        inventoryGrid.revalidate();
    }

    public void showDialog () {
        confirmDialog = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1000; i > 0; i-=20) {
                    dialogControl.setSize(100,i);
                    headerListener.componentResized(null);

                    try {
                        Thread.sleep(1);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        });
        thread.start();
    }

    public void cancel () {
        confirmDialog = false;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i+=20) {
                    dialogControl.setSize(100,i);
                    headerListener.componentResized(null);

                    try {
                        Thread.sleep(1);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        });
        thread.start();
    }

    public void purchase () {
        int total = runningTotalCart;
        for (int i = cartItems.size()-1; i >= 0; i--) {
            GenericItem item = cartItems.get(i);
            removeFromCart(item);
            shopItems.remove(item);
            inventoryItems.add(item);
        }
        updateShop();
        updateInventory();
        cancel();
        purse -= total;
        coins = setupCoins(purse);
        coinWrapper.remove(0);
        coinWrapper.add(coins,BorderLayout.CENTER);
        coinWrapper.repaint();
        row2.getMouseListeners()[0].mousePressed(null);
    }

    public void itemToggled (GenericItem item, boolean added) {
        if (added)
            addToCart(item);
        else
            removeFromCart(item);

        if (cartItems.isEmpty())
            hideCart();
        else
            showCart();
    }

    public void onDrag (int x, int y) {}

    public void onRightClick (int x, int y) {
        popupOpen = true;
        popup.setBounds(x,y,popup.getWidth(),popup.getHeight());
        add(popup,0);
        repaint();
        revalidate();
    }

    public void onLeftClick (int x, int y) {
        boolean outsideXRange = x < popup.getX() || x > popup.getX()+popup.getWidth();
        boolean outsideYRange = y+30 < popup.getY() || y > popup.getY()+30+popup.getHeight();
        if (outsideYRange || outsideXRange) {
            popupOpen = false;
            remove(popup);
            repaint();
            revalidate();
        }
    }

    public JPanel setupUI () {
        JPanel background = new JPanel();
        JPanel headerLayer = new JPanel();
        BevelPanel header = new BevelPanel();
        JLabel headerlabel = new JLabel();
        coins = setupCoins(244);
        JPanel contentLayer = new JPanel();
        row = new JPanel();
        row2 = new JPanel();
        BevelPanel shop = new BevelPanel();
        JLabel shopLabel = new JLabel();
        grid = new JPanel();
        BevelPanel inventory = new BevelPanel();
        JLabel inventoryLabel = new JLabel();
        inventoryGrid = new JPanel();
        BevelPanel cart = new BevelPanel();
        JLabel cartLabel = new JLabel();
        cartContent = new JPanel();
        total = new JPanel();
        JLabel totalLabel = new JLabel();
        JPanel totalCost = setupCoins(10);

        // setting up background
        background.setLayout(null);
        background.setBackground(new Color(223,223,223));

        // setting up header layer
        headerLayer.setLayout(new BorderLayout());
        headerListener = new ResizeListener(parentFrame,headerLayer);
        parentFrame.addComponentListener(headerListener);
        headerLayer.setOpaque(false);
        headerLayer.setBackground(new Color(0,0,0,0));
        headerLayer.setBounds(0,0,2000,2000);
        background.add(headerLayer);

        // setting up header wrapper
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper,BoxLayout.Y_AXIS));
        headerWrapper.setOpaque(false);
        headerWrapper.setBackground(new Color(0,0,0,0));
        headerLayer.add(headerWrapper,BorderLayout.NORTH);

        // setting up header
        header.setLayout(new FlowLayout());
        header.setRoundBottom(true);
        header.setBackground(new Color(201,201,201));
        header.setPreferredSize(new Dimension(400,100));
        header.setMinimumSize(new Dimension(200,100));
        header.setMaximumSize(new Dimension(600,100));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerWrapper.add(header);

        // setting up header content
        headerlabel.setText("Item Shop");
        headerlabel.setFont(new Font("Helvetica",Font.PLAIN,72));
        headerlabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        headerlabel.setForeground(new Color(124,124,124));
        header.add(headerlabel);
        coinWrapper = new JPanel();
        coinWrapper.setLayout(new BorderLayout());
        coinWrapper.add(coins,BorderLayout.CENTER);
        coinWrapper.setBorder(BorderFactory.createEmptyBorder(15,10,0,0));
        coinWrapper.setBackground(header.getBackground());
        header.add(coinWrapper);

        // setup Notification Slider
        JPanel notificationSlider = new JPanel();
        notificationSlider.setLayout(new GridBagLayout());
        notificationSlider.setOpaque(false);
        notificationSlider.setPreferredSize(new Dimension(2000,2000));
        notificationSlider.setMinimumSize(notificationSlider.getPreferredSize());
        notificationSlider.setMaximumSize(notificationSlider.getPreferredSize());
        headerLayer.add(notificationSlider,BorderLayout.CENTER);

        // setup dialog control
        JPanel controlPanelDialog = new JPanel();
        controlPanelDialog.setOpaque(false);
        dialogControl = new Dimension(100,1000);
        controlPanelDialog.setPreferredSize(dialogControl);
        headerLayer.add(controlPanelDialog,BorderLayout.SOUTH);

        // setup notification wrapper to center notification on slider
        JPanel notificationWrapper = new JPanel();
        notificationWrapper.setLayout(new BorderLayout());
        notificationWrapper.setPreferredSize(new Dimension(400,200));
        notificationWrapper.setMinimumSize(notificationWrapper.getPreferredSize());
        notificationWrapper.setMaximumSize(notificationWrapper.getPreferredSize());
        notificationWrapper.setOpaque(false);
        notificationWrapper.setBackground(Color.RED);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        notificationSlider.add(notificationWrapper,c);

        // setup notification
        BevelPanel notification = new BevelPanel();
        notification.setRoundTop(true);
        notification.setRoundBottom(true);
        notification.setBackground(new Color(168,168,168));
        notification.setDecorativeBorder(new DecorativeBorder(new Color(150,150,150),1));
        notification.setLayout(new BorderLayout());
        JLabel notifMessage = new JLabel("Confirm?");
        notifMessage.setFont(new Font("Helvetica",Font.PLAIN,32));
        notifMessage.setForeground(new Color(124,124,124));
        notifMessage.setHorizontalAlignment(SwingConstants.CENTER);
        notifMessage.setVerticalAlignment(SwingConstants.CENTER);
        notifMessage.setBorder(BorderFactory.createEmptyBorder(20,10,10,10));
        notification.add(notifMessage,BorderLayout.NORTH);
        notificationWrapper.add(notification,BorderLayout.CENTER);

        // setup choices
        JPanel choices = new JPanel();
        notification.add(choices,BorderLayout.SOUTH);
        choices.setLayout(new GridBagLayout());
        BevelPanel cancel = new BevelPanel();
        cancel.setBackground(Color.RED);
        cancel.setLayout(new BorderLayout());
        cancel.setPreferredSize(new Dimension(100,50));
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        choices.add(cancel,c);
        BevelPanel purchase = new BevelPanel();
        purchase.setBackground(Color.GREEN);
        purchase.setLayout(new BorderLayout());
        purchase.setPreferredSize(new Dimension(100,50));
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        choices.add(purchase,c);
        choices.setOpaque(false);
        choices.setBorder(BorderFactory.createEmptyBorder(0,40,20,40));
        cancel.setRoundTop(true);
        cancel.setRoundBottom(true);
        purchase.setRoundTop(true);
        purchase.setRoundBottom(true);
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ItemShop.this.cancel();
            }
        });
        purchase.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(ItemShop.this::purchase);
            }
        });
        parentFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (confirmDialog)
                    ItemShop.this.cancel();
            }
            @Override
            public void componentMoved(ComponentEvent e) {
                if (confirmDialog)
                    ItemShop.this.cancel();
            }
        });

        // adding text
        JLabel messageText = new JLabel("<html><p style=\"text-align=center;\">" +
                "Would you like to confirm purchase. You cannot reverse this</p></html>");
        JLabel cancelText = new JLabel("Cancel");
        JLabel purchaseText = new JLabel("Purchase");
        messageText.setHorizontalAlignment(SwingConstants.CENTER);
        messageText.setVerticalAlignment(SwingConstants.CENTER);
        messageText.setFont(new Font("Helvetica",Font.PLAIN,18));
        cancelText.setHorizontalAlignment(SwingConstants.CENTER);
        cancelText.setVerticalAlignment(SwingConstants.CENTER);
        cancelText.setFont(new Font("Helvetica",Font.PLAIN,18));
        purchaseText.setHorizontalAlignment(SwingConstants.CENTER);
        purchaseText.setVerticalAlignment(SwingConstants.CENTER);
        purchaseText.setFont(new Font("Helvetica",Font.PLAIN,18));
        messageText.setForeground(new Color(124,124,124));
        cancelText.setForeground(new Color(124,124,124));
        purchaseText.setForeground(new Color(124,124,124));
        messageText.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));
        cancelText.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        purchaseText.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        notification.add(messageText,BorderLayout.CENTER);
        cancel.add(cancelText);
        purchase.add(purchaseText);

        // setting up content layer
        contentLayer.setLayout(null);
        contentListener = new ResizeListener(parentFrame,contentLayer,0,110);
        contentListener.setIgnoreY(true);
        parentFrame.addComponentListener(contentListener);
        contentLayer.setOpaque(false);
        contentLayer.setBackground(new Color(0,0,0,0));
        contentLayer.setBounds(0,110,2000,5000);
        background.add(contentLayer);

        // setting up row
        row.setLayout(new BorderLayout());
        row.setBounds(0,0,500,500);
        rowResize = new ResizeListener(parentFrame,row,0,0,0,200);
        parentFrame.addComponentListener(rowResize);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBackground(background.getBackground());
        contentLayer.add(row);
        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (shopView)
                    return;
                Thread thread = new Thread(ItemShop.this::hideInventory);
                thread.start();
                shopView = true;
            }
        });

        // setting up shop
        JPanel shopWrapper = new JPanel();
        shopWrapper.setLayout(new BorderLayout());
        shopWrapper.setBackground(background.getBackground());
        shopWrapper.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
        row.add(shopWrapper,BorderLayout.CENTER);
        shop.setLayout(new BorderLayout());
        shop.setRoundTop(true);
        shop.setRoundBottom(true);
        shop.setRounding(300);
        shop.setBackground(new Color(201,201,201));
        shopWrapper.add(shop);

        // setting up shop label
        shopLabel.setText("Shop Items");
        shopLabel.setFont(new Font("Helvetica",Font.PLAIN,48));
        shopLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shopLabel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        shopLabel.setForeground(new Color(124,124,124));
        shop.add(shopLabel,BorderLayout.NORTH);

        // setting up grid
        JPanel gridWrapper = new JPanel();
        gridWrapper.setLayout(new BorderLayout());
        gridWrapper.setOpaque(false);
        gridWrapper.setBackground(new Color(0,0,0,0));
        gridWrapper.setBorder(BorderFactory.createEmptyBorder(0,50,50,50));
        shop.add(gridWrapper,BorderLayout.CENTER);
        grid.setLayout(new GridLayout(4,11));
        grid.setOpaque(false);
        gridWrapper.add(grid,BorderLayout.CENTER);

        // setting up grid content
        for (int i = 0; i < 44; i++)
            if (i < shopItems.size())
                grid.add(setupItem(shopItems.get(i)));
            else
                grid.add(setupItem());

        // setting up row 2
        row2.setLayout(new BorderLayout());
        rowResize2 = new ResizeListener(parentFrame,row2,0,-200,0,-400);
        parentFrame.addComponentListener(rowResize2);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.setBackground(background.getBackground());
        contentLayer.add(row2);
        row2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!shopView)
                    return;
                hideCart();
                Thread thread = new Thread(ItemShop.this::showInventory);
                thread.start();
                shopView = false;
            }
        });

        // setting up inventory
        JPanel inventoryWrapper = new JPanel();
        inventoryWrapper.setLayout(new BorderLayout());
        inventoryWrapper.setBackground(background.getBackground());
        inventoryWrapper.setBorder(BorderFactory.createEmptyBorder(10,30,0,30));
        inventory.setLayout(new BorderLayout());
        inventory.setRoundTop(true);
        inventory.setRoundBottom(true);
        inventory.setBackground(new Color(201,201,201));
        inventory.setRounding(300);
//        parentFrame.addComponentListener(new ResizeListener(parentFrame,inventoryWrapper,0,600));
        inventoryWrapper.add(inventory,BorderLayout.CENTER);
        row2.add(inventoryWrapper,BorderLayout.CENTER);

        // setting up inventory label
        inventoryLabel.setText("Inventory");
        inventoryLabel.setFont(new Font("Helvetica",Font.PLAIN,48));
        inventoryLabel.setForeground(new Color(124,124,124));
        inventoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inventoryLabel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        inventory.add(inventoryLabel,BorderLayout.NORTH);

        // setting up inventory grid
        JPanel invGridWrapper = new JPanel();
        invGridWrapper.setLayout(new BorderLayout());
        invGridWrapper.setOpaque(false);
        invGridWrapper.setBackground(new Color(0,0,0,0));
        invGridWrapper.setBorder(BorderFactory.createEmptyBorder(0,50,50,0));

        inventoryGrid.setLayout(new GridLayout(4,11));
        inventoryGrid.setOpaque(false);
        invGridWrapper.add(inventoryGrid,BorderLayout.CENTER);
        inventory.add(invGridWrapper,BorderLayout.CENTER);

        // setting up inventory grid content
        for (int i = 0; i < 44; i++) {
            inventoryGrid.add(setupItem());
        }

        // setting up cart
        JPanel removingRounding = new JPanel();
        removingRounding.setBackground(new Color(201,201,201));
        removingRounding.setPreferredSize(new Dimension(20,100));
        cart.setLayout(new BorderLayout());
        cart.setRoundTop(true);
        cart.setRoundBottom(true);
        cart.setBackground(new Color(201,201,201));
        cart.setPreferredSize(new Dimension(0,400));
        cart.add(removingRounding,BorderLayout.EAST);
        this.cart = cart;
        row.add(cart,BorderLayout.EAST);

        // setting up cart label
        cartLabel.setText("Cart");
        cartLabel.setFont(new Font("Helvetica",Font.PLAIN,32));
        cartLabel.setBorder(BorderFactory.createEmptyBorder(20,75,20,0));
        cartLabel.setForeground(new Color(124,124,124));
        JPanel cartBody = new JPanel();
        cartBody.setOpaque(false);
        cartBody.setLayout(new BorderLayout());
        cartBody.add(cartLabel,BorderLayout.NORTH);
        cart.add(cartBody,BorderLayout.CENTER);

        // setting up cart content
        cartContent.setLayout(new BoxLayout(cartContent,BoxLayout.Y_AXIS));
        cartContent.setOpaque(false);
        cartBody.add(cartContent,BorderLayout.CENTER);
        cartBody.setBorder(BorderFactory.createEmptyBorder(0,25,0,0));

        // setting up total cost
        total.setLayout(new FlowLayout());
        total.setOpaque(false);
        cartBody.add(total,BorderLayout.SOUTH);
        total.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (runningTotalCart <= purse)
                    ItemShop.this.showDialog();
            }
        });

        // setting up total label
        totalLabel.setText("Total: ");
        totalLabel.setFont(new Font("Helvetica",Font.PLAIN,24));
        totalLabel.setForeground(new Color(124,124,124));
        total.add(totalLabel);

        // setting up total cost
        total.add(setupCoins(0));

        return background;
    }

    private void addToCart (GenericItem item) {
        cartItems.add(item);
        int cost = item.getCost();
        runningTotalCart += cost;
        cartContent.add(setupItemCost(item.getInventoryLook(),cost));
        total.remove(1);
        total.add(setupCoins(runningTotalCart,true));
        cartContent.repaint();
        cartContent.revalidate();
        total.repaint();
    }

    private void removeFromCart (GenericItem item) {
        int cost = item.getCost();
        int index = 0;
        for (index = 0; index < cartItems.size(); index++)
            if (cartItems.get(index) == item)
                break;
        cartContent.remove(index);
        runningTotalCart -= cost;
        total.remove(1);
        total.add(setupCoins(runningTotalCart,true));
        cartItems.remove(item);
        cartContent.repaint();
        cartContent.revalidate();
        total.repaint();
    }

    public void showCart () {
        cart.setPreferredSize(new Dimension(260,400));
        parentFrame.revalidate();
        parentFrame.repaint();
        contentListener.componentResized(null);
        headerListener.componentResized(null);
        rowResize.componentResized(null);
        rowResize2.componentResized(null);
    }

    public void hideCart () {
        cart.setPreferredSize(new Dimension(0,400));
        parentFrame.revalidate();
        parentFrame.repaint();
        contentListener.componentResized(null);
        headerListener.componentResized(null);
        rowResize.componentResized(null);
        rowResize2.componentResized(null);
    }

    public void showInventory () {
        // move the window up
        for (int i = 0; i >= -550; i-=10) {
            translate(0,i);
            parentFrame.revalidate();
            parentFrame.repaint();
            contentListener.componentResized(null);
            headerListener.componentResized(null);
            rowResize.componentResized(null);
            rowResize2.componentResized(null);
            try {
                Thread.sleep(1);
            } catch (Exception e) {e.printStackTrace();}
        }

        // change the resize listener for row
        parentFrame.removeComponentListener(rowResize);
        rowResize = new ResizeListener(parentFrame,row,0,0);
        row.setBounds(0,0,500,500);
        rowResize.setIgnoreY(true);
        parentFrame.addComponentListener(rowResize);

        // change the resize listener for row2
        parentFrame.removeComponentListener(rowResize2);
        rowResize2 = new ResizeListener(parentFrame,row2,0,500,0,-400);
        parentFrame.addComponentListener(rowResize2);

        parentFrame.revalidate();
        parentFrame.repaint();
        contentListener.componentResized(null);
        headerListener.componentResized(null);
        rowResize.componentResized(null);
        rowResize2.componentResized(null);
    }

    public void hideInventory () {
        // move the window to standard
        for (int i = -550; i < 1; i+=10) {
            translate(0,i);
            parentFrame.revalidate();
            parentFrame.repaint();
            contentListener.componentResized(null);
            headerListener.componentResized(null);
            rowResize.componentResized(null);
            rowResize2.componentResized(null);
            try {
                Thread.sleep(1);
            } catch (Exception e) {e.printStackTrace();}
        }

        // change the resize listener for row
        parentFrame.removeComponentListener(rowResize);
        rowResize.setIgnoreY(false);
        rowResize = new ResizeListener(parentFrame,row,0,0,0,200);
        parentFrame.addComponentListener(rowResize);

        // change the resize listener for row2
        parentFrame.removeComponentListener(rowResize2);
        rowResize2 = new ResizeListener(parentFrame,row2,0,-200,0,-400);
        parentFrame.addComponentListener(rowResize2);

        parentFrame.revalidate();
        parentFrame.repaint();
        contentListener.componentResized(null);
        headerListener.componentResized(null);
        rowResize.componentResized(null);
        rowResize2.componentResized(null);

        if (!cartItems.isEmpty())
            showCart();
    }

    private void translate (int x, int y) {
        headerListener.translate(x/6,y/6);
        contentListener.translate(x,y);
    }

    private void translateHeader (int x, int y) {
        headerListener.translate(x,y);
    }

    private void translateContent (int x, int y) {
        contentListener.translate(x,y);
    }

    private JPanel setupItemCost (JLabel icon, int coins) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0,0,0,0));
        panel.setOpaque(true);
        panel.setLayout(new FlowLayout());

        BevelPanel item = new BevelPanel();
        item.setRoundTop(true);
        item.setRoundBottom(true);
        item.setRounding(35);
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(50,50));
        item.setBackground(new Color(168,168,168));
        item.setLayout(new BorderLayout());

        JLabel iconCopy = new JLabel();
        iconCopy.setHorizontalAlignment(SwingConstants.CENTER);
        iconCopy.setVerticalAlignment(SwingConstants.CENTER);
        iconCopy.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconCopy.setAlignmentY(Component.CENTER_ALIGNMENT);
        iconCopy.setIcon(icon.getIcon());

        item.add(iconCopy);

        panel.add(item);
        panel.add(setupCoins(coins));
        panel.setMaximumSize(new Dimension(500,60));
        return panel;
    }

    private JPanel setupItem () {
        return setupItem(null);
    }

    private JPanel setupItem (GenericItem item) {
        return setupItem(item,false);
    }

    private JPanel setupItem (GenericItem item, boolean disabled) {
        BevelPanel panel = new BevelPanel();
        panel.setRoundTop(true);
        panel.setRoundBottom(true);
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(50,50));
        panel.setBackground(new Color(168,168,168));

        if (item != null) {
            panel.setLayout(new BorderLayout());
            panel.add(item.getInventoryLook(),BorderLayout.CENTER);
        }

        JPanel spacerX = new JPanel();
        spacerX.setPreferredSize(new Dimension(1,1));
        JPanel spacerY = new JPanel();
        spacerY.setPreferredSize(new Dimension(1,1));
        JPanel spacerX2 = new JPanel();
        spacerX2.setPreferredSize(new Dimension(1,1));
        JPanel spacerY2 = new JPanel();
        spacerY2.setPreferredSize(new Dimension(1,1));
        JPanel contentArea = new JPanel();
        contentArea.setPreferredSize(new Dimension(50,50));
        contentArea.setLayout(new BorderLayout());
        contentArea.add(panel,BorderLayout.CENTER);
        spacerX.setOpaque(false);
        spacerX2.setOpaque(false);
        spacerY.setOpaque(false);
        spacerY2.setOpaque(false);
        contentArea.setOpaque(false);

        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(50,50));
        wrapper.setOpaque(false);
        wrapper.setLayout(new BorderLayout());

        wrapper.add(spacerX,BorderLayout.WEST);
        wrapper.add(spacerX2,BorderLayout.EAST);
        wrapper.add(spacerY,BorderLayout.NORTH);
        wrapper.add(spacerY2,BorderLayout.SOUTH);
        wrapper.add(contentArea,BorderLayout.CENTER);
        wrapper.addComponentListener(new ItemResizeListener(wrapper,contentArea,spacerX,spacerX2,
                spacerY,spacerY2));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        if (item != null && !disabled) {
            panel.addMouseListener(new MouseAdapter() {
                private boolean selected = false;

                @Override
                public void mousePressed(MouseEvent e) {
                    if (confirmDialog)
                        return;
                    selected = !selected;
                    if (selected) {
                        ItemShop.this.itemToggled(item,true);
                        panel.setBackground(new Color(140, 140, 140));
                        panel.repaint();
                    } else {
                        ItemShop.this.itemToggled(item,false);
                        panel.setBackground(new Color(168, 168, 168));
                        panel.repaint();
                    }
                }
            });
        } else {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (confirmDialog)
                        return;
                    panel.setBackground(new Color(140, 140, 140));
                    panel.repaint();
                    Timer timer = new Timer(250, new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            panel.setBackground(new Color(168, 168, 168));
                            panel.repaint();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            });
        }

        return wrapper;
    }

    private JPanel setupCoins (int coins) {
        return setupCoins(coins,false);
    }

    private JPanel setupCoins (int coins, boolean withColor) {
        BevelPanel panel = new BevelPanel();
        panel.setRoundTop(true);
        panel.setRoundBottom(true);
        panel.setOpaque(false);
        if (withColor)
            if (runningTotalCart <= purse)
                panel.setBackground(Color.GREEN);
            else
                panel.setBackground(Color.RED);
        else
            panel.setBackground(new Color(173,173,173));
        panel.setPreferredSize(new Dimension(120,50));
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(coins+" ⛁");
        label.setFont(new Font("Helvetica",Font.PLAIN,32));
        label.setForeground(new Color(124,124,124));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label,BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void onEscapePressed () {
        setNextWindow(WindowStateName.MISSION_SELECT);
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        close();
    }

    @Override
    public void save(FileHandler fileHandler) {
        fileHandler.save(PlayerData.currentUser + "-hasLoaded",true);
        PlayerData.xp = purse;
        PlayerData.save(fileHandler);
        String[] shopItemsList = new String[shopItems.size()];
        String[] inventoryItemsList = new String[inventoryItems.size()];
        for (int i = 0; i < shopItems.size(); i++)
            shopItemsList[i] = shopItems.get(i).getCharacterLookName();
        for (int i = 0; i < inventoryItems.size(); i++)
            inventoryItemsList[i] = inventoryItems.get(i).getCharacterLookName();
        fileHandler.save(PlayerData.name + "-shop-items",shopItemsList);
        fileHandler.save(PlayerData.name + "-inventory-items",inventoryItemsList);
    }

    @Override
    public void load(FileHandler fileHandler) {
        // ensure graphics are loaded correctly
        for (ComponentListener listener : parentFrame.getComponentListeners())
            if (listener instanceof ResizeListener)
                listener.componentResized(null);

        // initialize money
        PlayerData.load(fileHandler);
        purse = PlayerData.xp;
        coins = setupCoins(purse);
        coinWrapper.remove(0);
        coinWrapper.add(coins,BorderLayout.CENTER);
        coinWrapper.repaint();

        // check if this is the first time loading for this profile
        boolean hasLoaded = fileHandler.retrieveBoolean(PlayerData.currentUser + "-hasLoaded");

        // reset shop and inventory items, because we will be re-loading them from storage
        inventoryItems = new ArrayList<>();
        shopItems = new ArrayList<>();

        // initialize items available for purchase
        if (!hasLoaded) { // setup defaults
            shopItems.add(new GenericItem("Bow", BOW_COST, "shop-bow", "bow"));
            shopItems.add(new GenericItem("Sword", SWORD_COST, "shop-single-sword", "single-sword"));
            shopItems.add(new GenericItem("Dual Sword", DUAL_SWORD_COST, "shop-dual-sword", "dual-sword"));
            shopItems.add(new GenericItem("Helmet", HELMET_COST, "shop-helmet", "helmet"));
            shopItems.add(new GenericItem("Chestplate", CHESTPLATE_COST, "shop-chestplate", "chestplate"));
            shopItems.add(new GenericItem("Leggings", LEGGINGS_COST, "shop-leggings", "leggings"));
        } else { // load previous shop items
            String[] array = fileHandler.retrieveStringArray(PlayerData.name + "-shop-items");
            Set<String> availableItems = Arrays.stream(array).collect(Collectors.toSet());
            if (availableItems.contains("bow"))
                shopItems.add(new GenericItem("Bow", BOW_COST, "shop-bow", "bow"));
            if (availableItems.contains("single-sword"))
                shopItems.add(new GenericItem("Sword", SWORD_COST, "shop-single-sword", "single-sword"));
            if (availableItems.contains("dual-sword"))
                shopItems.add(new GenericItem("Dual Sword", DUAL_SWORD_COST, "shop-dual-sword", "dual-sword"));
            if (availableItems.contains("helmet"))
                shopItems.add(new GenericItem("Helmet", HELMET_COST, "shop-helmet", "helmet"));
            if (availableItems.contains("chestplate"))
                shopItems.add(new GenericItem("Chestplate", CHESTPLATE_COST, "shop-chestplate", "chestplate"));
            if (availableItems.contains("leggings"))
                shopItems.add(new GenericItem("Leggings", LEGGINGS_COST, "shop-leggings", "leggings"));
        }

        // initialize items in inventory
        if (hasLoaded) {
            String[] array = fileHandler.retrieveStringArray(PlayerData.name + "-inventory-items");
            Set<String> availableItems = Arrays.stream(array).collect(Collectors.toSet());
            if (availableItems.contains("bow"))
                inventoryItems.add(new GenericItem("Bow", BOW_COST, "shop-bow", "bow"));
            if (availableItems.contains("single-sword"))
                inventoryItems.add(new GenericItem("Sword", SWORD_COST, "shop-single-sword", "single-sword"));
            if (availableItems.contains("dual-sword"))
                inventoryItems.add(new GenericItem("Dual Sword", DUAL_SWORD_COST, "shop-dual-sword", "dual-sword"));
            if (availableItems.contains("helmet"))
                inventoryItems.add(new GenericItem("Helmet", HELMET_COST, "shop-helmet", "helmet"));
            if (availableItems.contains("chestplate"))
                inventoryItems.add(new GenericItem("Chestplate", CHESTPLATE_COST, "shop-chestplate", "chestplate"));
            if (availableItems.contains("leggings"))
                inventoryItems.add(new GenericItem("Leggings", LEGGINGS_COST, "shop-leggings", "leggings"));
        }

        // update UI
        updateShop();
        updateInventory();

        // load images from items from resources folder
        for (GenericItem item : shopItems)
            item.load(fileHandler);
        for (GenericItem item : inventoryItems)
            item.load(fileHandler);
    }
}
