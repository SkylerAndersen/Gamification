package WindowStates;

import ApplicationDefaults.*;
import DataStructures.FileHandler;
import DataStructures.GenericItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ItemShop extends WindowState implements AcceptMouseResponse {
    private JPanel popup;
    private boolean popupOpen;
    private MouseController mouseController;
    private ResizeListener headerListener;
    private ResizeListener contentListener;
    private JPanel cart;
    private JFrame parentFrame;
    private JPanel background;
    private JPanel row;
    private JPanel row2;
    private ResizeListener rowResize;
    private ResizeListener rowResize2;
    private boolean shopView;
    private ArrayList<GenericItem> cartItems;

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

        // setup UI
        shopView = true;
        cartItems = new ArrayList<>();
        cartItems.add(new GenericItem());
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
        JPanel coins = setupCoins(244);
        JPanel contentLayer = new JPanel();
        row = new JPanel();
        row2 = new JPanel();
        BevelPanel shop = new BevelPanel();
        JLabel shopLabel = new JLabel();
        JPanel grid = new JPanel();
        BevelPanel inventory = new BevelPanel();
        JLabel inventoryLabel = new JLabel();
        JPanel inventoryGrid = new JPanel();
        BevelPanel cart = new BevelPanel();
        JLabel cartLabel = new JLabel();
        JPanel cartContent = new JPanel();
        JPanel total = new JPanel();
        JLabel totalLabel = new JLabel();
        JPanel totalCost = setupCoins(10);

        // setting up background
        background.setLayout(null);
        background.setBackground(new Color(223,223,223));

        // setting up header layer
        headerLayer.setLayout(new BoxLayout(headerLayer,BoxLayout.Y_AXIS));
        headerListener = new ResizeListener(parentFrame,headerLayer);
        parentFrame.addComponentListener(headerListener);
        headerLayer.setOpaque(false);
        headerLayer.setBackground(new Color(0,0,0,0));
        headerLayer.setBounds(0,0,2000,2000);
        background.add(headerLayer);

        // setting up header
        header.setLayout(new FlowLayout());
        header.setRoundBottom(true);
        header.setBackground(new Color(201,201,201));
        header.setPreferredSize(new Dimension(400,100));
        header.setMinimumSize(new Dimension(200,100));
        header.setMaximumSize(new Dimension(600,100));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLayer.add(header);

        // setting up header content
        headerlabel.setText("Item Shop");
        headerlabel.setFont(new Font("Helvetica",Font.PLAIN,72));
        headerlabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        headerlabel.setForeground(new Color(124,124,124));
        header.add(headerlabel);
        JPanel coinWrapper = new JPanel();
        coinWrapper.setLayout(new BorderLayout());
        coinWrapper.add(coins,BorderLayout.CENTER);
        coinWrapper.setBorder(BorderFactory.createEmptyBorder(15,10,0,0));
        coinWrapper.setBackground(header.getBackground());
        header.add(coinWrapper);

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
        // setting up item costs
        for (int i = 0; i < 3; i++) {
            cartContent.add(setupItemCost(i*10));
        }

        // setting up total cost
        total.setLayout(new FlowLayout());
        total.setOpaque(false);
        cartBody.add(total,BorderLayout.SOUTH);

        // setting up total label
        totalLabel.setText("Total: ");
        totalLabel.setFont(new Font("Helvetica",Font.PLAIN,24));
        totalLabel.setForeground(new Color(124,124,124));
        total.add(totalLabel);

        // setting up total cost
        total.add(setupCoins(10));

        return background;
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

    private JPanel setupItemCost (int coins) {
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

        panel.add(item);
        panel.add(setupCoins(coins));
        panel.setMaximumSize(new Dimension(500,60));
        return panel;
    }

    private JPanel setupItem () {
        BevelPanel panel = new BevelPanel();
        panel.setRoundTop(true);
        panel.setRoundBottom(true);
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(50,50));
        panel.setBackground(new Color(168,168,168));

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

        return wrapper;
    }

    private JPanel setupCoins (int coins) {
        BevelPanel panel = new BevelPanel();
        panel.setRoundTop(true);
        panel.setRoundBottom(true);
        panel.setOpaque(false);
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

    }

    @Override
    public void load(FileHandler fileHandler) {
        for (ComponentListener listener : parentFrame.getComponentListeners())
            if (listener instanceof ResizeListener)
                listener.componentResized(null);
    }
}
