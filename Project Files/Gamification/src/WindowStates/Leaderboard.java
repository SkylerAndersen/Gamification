package WindowStates;

import ApplicationDefaults.*;
import DataStructures.FileHandler;

import javax.swing.*;
import java.awt.*;

public class Leaderboard extends WindowState implements AcceptMouseResponse {
    private JFrame parentFrame;
    private JPanel popup;
    private boolean popupOpen;
    private MouseController mouseController;
    private ResizeListener headerListener;
    private ResizeListener contentListener;
    private JPanel cart;
    public Leaderboard(JFrame parentFrame) {
        // setup frame
        super(WindowStateName.LEADERBOARD);
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
        JPanel background = setupUI();

        // setup bounds and add items
        background.setBounds(0,0,1200,800);
        popup.setBounds(0,0,100,100);
        getContentPane().add(background,0);
        parentFrame.addComponentListener(new ResizeListener(parentFrame,background));
        mouseController = new MouseController(this);
        parentFrame.addMouseListener(mouseController.getMouseListener());
        parentFrame.addMouseMotionListener(mouseController.getMouseMotionListener());
    }

    @Override
    public void onDrag (int x, int y) {}

    @Override
    public void onRightClick (int x, int y) {
        popupOpen = true;
        popup.setBounds(x,y,popup.getWidth(),popup.getHeight());
        add(popup,0);
        repaint();
        revalidate();
    }

    @Override
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
        background.setLayout(new BorderLayout());
        background.setBackground(new Color(223,223,223));

        JPanel headerContainer =  new JPanel();
        headerContainer.setOpaque(false);
        headerContainer.setLayout(new BoxLayout(headerContainer,BoxLayout.Y_AXIS));
        background.add(headerContainer,BorderLayout.NORTH);

        BevelPanel header = new BevelPanel();
        header.setRoundBottom(true);
        header.setBackground(new Color(201,201,201));
        header.setPreferredSize(new Dimension(400,100));
        header.setMinimumSize(new Dimension(200,100));
        header.setMaximumSize(new Dimension(600,100));
        header.setLayout(new BorderLayout());
        headerContainer.add(header);

        JPanel leaderboardArea = new JPanel();
        leaderboardArea.setLayout(new BorderLayout());
        leaderboardArea.setOpaque(false);
        leaderboardArea.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
        background.add(leaderboardArea,BorderLayout.CENTER);

        BevelPanel leaderboardHeader = new BevelPanel();
        leaderboardHeader.setRoundTop(true);
        leaderboardHeader.setRounding(300);
        leaderboardHeader.setBackground(new Color(175,175,175));
        parentFrame.addComponentListener(new RoundingResize(leaderboardHeader,300,1200,800,leaderboardArea));
        leaderboardHeader.setPreferredSize(new Dimension(100,80));
        leaderboardHeader.setLayout(new BorderLayout());
        leaderboardArea.add(leaderboardHeader,BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(new Color(201,201,201));
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        leaderboardArea.add(content,BorderLayout.CENTER);

        BevelPanel leaderboardFooter = new BevelPanel();
        leaderboardFooter.setRoundBottom(true);
        leaderboardFooter.setRounding(300);
        leaderboardFooter.setBackground(new Color(201,201,201));
        parentFrame.addComponentListener(new RoundingResize(leaderboardFooter,300,1200,800,leaderboardArea));
        leaderboardFooter.setPreferredSize(new Dimension(100,80));
        leaderboardFooter.setLayout(new BoxLayout(leaderboardFooter,BoxLayout.Y_AXIS));
        leaderboardArea.add(leaderboardFooter,BorderLayout.SOUTH);

        JPanel addPlayerWrapper = new JPanel();
        addPlayerWrapper.setPreferredSize(new Dimension(800,50));
        addPlayerWrapper.setLayout(new BorderLayout());
        addPlayerWrapper.setOpaque(false);
        addPlayerWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        addPlayerWrapper.setPreferredSize(new Dimension(400,50));
        addPlayerWrapper.setMinimumSize(new Dimension(100,50));
        addPlayerWrapper.setMaximumSize(new Dimension(800,50));
        addPlayerWrapper.setBorder(BorderFactory.createEmptyBorder(0,70,0,70));
        leaderboardFooter.add(addPlayerWrapper);

        BevelPanel addPlayer = new BevelPanel();
        addPlayer.setRoundTop(true);
        addPlayer.setRoundBottom(true);
        addPlayer.setBackground(new Color(175,175,175));
        addPlayerWrapper.add(addPlayer);

        // add text to header
        JLabel title = new JLabel("Leaderboard");
        title.setForeground(new Color(126,126,126));
        title.setFont(new Font("Helvetica",Font.PLAIN,72));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        header.add(title,BorderLayout.CENTER);

        // add text to button
        JLabel button = new JLabel("Add Friend...");
        button.setForeground(new Color(126,126,126));
        button.setFont(new Font("Helvetica",Font.PLAIN,32));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        addPlayer.add(button,BorderLayout.CENTER);

        // add table content
        leaderboardHeader.add(tableEntry("Rank","Name","Coins"));
        for (int i = 0; i < 4; i++)
            content.add(tableEntry(""+(i+1),"Player"+(i+1),""+(int)(Math.random()*100)));
        content.add(tableEntry("?","Player5 (Outbound Request...)","0"));

        return background;
    }

    private JPanel tableEntry (String item1, String item2, String item3) {
        JPanel entry = new JPanel();
        entry.setOpaque(false);
        entry.setLayout(new BorderLayout());
        entry.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
        Color textColor = new Color(124,124,124);
        Font font = new Font("Helvetica",Font.PLAIN,32);

        JLabel label1 = new JLabel(item1);
        label1.setHorizontalAlignment(SwingConstants.RIGHT);
        label1.setForeground(textColor);
        label1.setFont(font);
        label1.setPreferredSize(new Dimension(100,50));
        JLabel label2 = new JLabel(item2);
        label2.setHorizontalAlignment(SwingConstants.LEFT);
        label2.setForeground(textColor);
        label2.setFont(font);
        label2.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        JLabel label3 = new JLabel(item3);
        label3.setHorizontalAlignment(SwingConstants.RIGHT);
        label3.setForeground(textColor);
        label3.setFont(font);
        label3.setBorder(BorderFactory.createEmptyBorder(0,0,0,30));

        entry.add(label1,BorderLayout.WEST);
        entry.add(label2,BorderLayout.CENTER);

        JPanel coins = new JPanel();
        coins.setOpaque(false);
        coins.setLayout(new BorderLayout());
        coins.setPreferredSize(new Dimension(100,50));
        coins.setMaximumSize(new Dimension(100,50));
        int num = -1;
        try { num = Integer.parseInt(item3); } catch (Exception e) {}
        if (num == -1) {
            entry.add(label3, BorderLayout.EAST);
        } else {
            coins.add(setupCoins(num));
            coins.setBorder(BorderFactory.createEmptyBorder(0,0,0,25));
            entry.add(coins, BorderLayout.EAST);
        }

        return entry;
    }

    private JLabel setupCoins (int coins) {
        JLabel label = new JLabel(coins+" ⛁");
        label.setFont(new Font("Helvetica",Font.PLAIN,32));
        label.setForeground(new Color(124,124,124));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }
    @Override
    public void save(FileHandler fileHandler) {

    }

    @Override
    public void load(FileHandler fileHandler) {
    }
}
