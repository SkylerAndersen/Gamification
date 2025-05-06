package WindowStates;

import ApplicationDefaults.ApplicationGUI;
import ApplicationDefaults.BevelPanel;
import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MissionSelect extends WindowState {

    public MissionSelect() {
        super(WindowStateName.MISSION_SELECT);

        JPanel background = new JPanel();
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.setBackground(new Color(223, 223, 223));
        background.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        getContentPane().add(background);

        // Header Panel
        BevelPanel headerPanel = new BevelPanel();
        headerPanel.setRoundTop(true);
        headerPanel.setRoundBottom(true);
        headerPanel.setBackground(ApplicationGUI.accentColor(new Color(201, 201, 201)));
        headerPanel.setPreferredSize(new Dimension(800, 100));
        headerPanel.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Mission Selection");
        headerLabel.setFont(new Font("Helvetica", Font.PLAIN, 72));
        headerLabel.setForeground(new Color(124, 124, 124));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        background.add(headerPanel);
        background.add(Box.createRigidArea(new Dimension(0, 40)));

        // Mission Panel
        JPanel missionPanel = new JPanel();
        missionPanel.setLayout(new GridLayout(5, 1, 0, 20));
        missionPanel.setOpaque(false);
        missionPanel.setMaximumSize(new Dimension(600, 400));
        background.add(missionPanel);
        missionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        missionPanel.add(createButton("Character", WindowStateName.CHARACTER));
        missionPanel.add(createButton("Leaderboard", WindowStateName.LEADERBOARD));
        missionPanel.add(createButton("To-Do", WindowStateName.TODOS));
        missionPanel.add(createButton("Item Shop", WindowStateName.ITEM_SHOP));
        missionPanel.add(createButton("Calendar", WindowStateName.CALENDAR));
    }

    private BevelPanel createButton(String label, WindowStateName destination) {
        BevelPanel buttonPanel = new BevelPanel();
        buttonPanel.setRoundTop(true);
        buttonPanel.setRoundBottom(true);
        buttonPanel.setRounding(300);
        buttonPanel.setBackground(new Color(201, 201, 201));
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setPreferredSize(new Dimension(400, 60));
        buttonPanel.setMaximumSize(new Dimension(400, 60));
        buttonPanel.setMinimumSize(new Dimension(400, 60));

        JLabel text = new JLabel(label);
        text.setFont(new Font("Helvetica", Font.PLAIN, 32));
        text.setForeground(new Color(124, 124, 124));
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setVerticalAlignment(SwingConstants.CENTER);
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(text, BorderLayout.CENTER);

        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setNextWindow(destination);
                setCloseEvent(WindowStateEvent.SWITCH_STATE);
                close();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonPanel.setBackground(new Color(180, 180, 180));
                buttonPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonPanel.setBackground(new Color(201, 201, 201));
                buttonPanel.repaint();
            }
        });

        return buttonPanel;
    }

    @Override
    public void onEscapePressed() {
        setCloseEvent(WindowStateEvent.CLOSE_APP);
        close();
    }

    @Override
    public void save(FileHandler fileHandler) {}

    @Override
    public void load(FileHandler fileHandler) {}
}
