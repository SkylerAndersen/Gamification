package WindowStates;

import ApplicationDefaults.BevelPanel;
import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;
import DataStructures.PlayerData;
import DataStructures.Task;
import DataStructures.TaskData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * ToDos window: displays the user’s task list and finished tasks,
 * persists them via TaskData, and awards XP/level via PlayerData.
 */
public class ToDos extends WindowState {

    // --- UI MODELS & COMPONENTS ---
    private DefaultListModel<Task> tasklistModel;
    private JList<Task>            tasklist;
    private DefaultListModel<Task> finishedTasklistModel;
    private JList<Task>            finishedTasklist;
    private JLabel                 xpLabel;
    private PlayerXP               playerXP;
    private FileHandler            fileHandler;

    public ToDos() {
        super(WindowStateName.TODOS);
        setStartupScreen(false);

        // Background panel
        BevelPanel background = new BevelPanel();
        background.setLayout(new GridBagLayout());
        background.setRounding(20);
        background.setRoundTop(true);
        background.setRoundBottom(true);
        background.setBackground(new Color(240, 240, 240));
        background.setOpaque(true);
        getContentPane().add(background);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        // XP & Level Label
        playerXP = new PlayerXP(0,1);
        xpLabel = new JLabel("XP: 0 / 1000 | Level: 1");
        xpLabel.setFont(new Font("Helvetica", Font.PLAIN, 32));
        xpLabel.setForeground(new Color(116,116,116));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; gbc.anchor=GridBagConstraints.CENTER;
        add(xpLabel, gbc);

        // Tasklist header
        BevelPanel taskLabelPanel = new BevelPanel();
        taskLabelPanel.setBackground(new Color(220,220,220));
        taskLabelPanel.setPreferredSize(new Dimension(250,30));
        taskLabelPanel.setRoundTop(true);
        taskLabelPanel.setRoundBottom(true);
        taskLabelPanel.setRounding(20);
        taskLabelPanel.setLayout(new BorderLayout());
        JLabel taskLabel = new JLabel("Tasklist", SwingConstants.CENTER);
        taskLabel.setFont(new Font("Helvetica", Font.PLAIN, 18));
        taskLabel.setForeground(new Color(116,116,116));
        taskLabelPanel.add(taskLabel, BorderLayout.CENTER);
        gbc.gridx=0; gbc.gridy=1; gbc.gridwidth=1; gbc.fill=GridBagConstraints.HORIZONTAL;
        add(taskLabelPanel, gbc);

        // Tasklist
        tasklistModel = new DefaultListModel<>();
        tasklist = new JList<>(tasklistModel);
        tasklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasklist.setFont(new Font("Helvetica", Font.PLAIN, 16));
        tasklist.setCellRenderer(new TaskRenderer());
        JScrollPane scroll1 = new JScrollPane(tasklist);
        scroll1.setPreferredSize(new Dimension(250,250));
        scroll1.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        gbc.gridx=0; gbc.gridy=2; gbc.fill=GridBagConstraints.BOTH; gbc.weightx=1; gbc.weighty=1;
        add(scroll1, gbc);

        // Finished list header
        BevelPanel finishedLabelPanel = new BevelPanel();
        finishedLabelPanel.setBackground(new Color(220,220,220));
        finishedLabelPanel.setPreferredSize(new Dimension(250,30));
        finishedLabelPanel.setRoundTop(true);
        finishedLabelPanel.setRoundBottom(true);
        finishedLabelPanel.setRounding(20);
        finishedLabelPanel.setLayout(new BorderLayout());
        JLabel finishedLabel = new JLabel("Finished Tasks", SwingConstants.CENTER);
        finishedLabel.setFont(new Font("Helvetica", Font.PLAIN, 18));
        finishedLabel.setForeground(new Color(116,116,116));
        finishedLabelPanel.add(finishedLabel, BorderLayout.CENTER);
        gbc.gridx=1; gbc.gridy=1; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=0;
        add(finishedLabelPanel, gbc);

        // Finished list
        finishedTasklistModel = new DefaultListModel<>();
        finishedTasklist = new JList<>(finishedTasklistModel);
        finishedTasklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        finishedTasklist.setFont(new Font("Helvetica", Font.PLAIN, 16));
        finishedTasklist.setCellRenderer(new TaskRenderer());
        JScrollPane scroll2 = new JScrollPane(finishedTasklist);
        scroll2.setPreferredSize(new Dimension(250,250));
        scroll2.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        gbc.gridx=1; gbc.gridy=2; gbc.fill=GridBagConstraints.BOTH; gbc.weightx=1; gbc.weighty=1;
        add(scroll2, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        Font btnFont = new Font("Helvetica", Font.PLAIN, 16);
        JButton addButton = new JButton("Add Task");     addButton.setFont(btnFont);
        JButton removeButton = new JButton("Complete Task"); removeButton.setFont(btnFont);
        JButton backButton = new JButton("Back to Mission Select"); backButton.setFont(btnFont);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);
        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2; gbc.fill=GridBagConstraints.NONE; gbc.anchor=GridBagConstraints.CENTER;
        gbc.weighty=0;
        add(buttonPanel, gbc);

        // Button Actions
        addButton.addActionListener(e -> AddTask());
        removeButton.addActionListener(e -> RemoveTask());
        backButton.addActionListener(e -> {
            setNextWindow(WindowStateName.MISSION_SELECT);
            setCloseEvent(WindowStateEvent.SWITCH_STATE);
            close();
        });
    }

    @Override
    public void load(FileHandler fileHandler) {
        this.fileHandler = fileHandler;

        // 1) Load & display player stats
        PlayerData.load(fileHandler);
        playerXP = new PlayerXP(PlayerData.xp, PlayerData.level);
        xpLabel.setText("XP: " + playerXP.getXp() + " / 1000 | Level: " + playerXP.getLevel());

        // 2) Load tasks (this will auto-seed daily defaults if needed)
        TaskData taskData = TaskData.load(fileHandler);

        // 3) Populate the two list models
        tasklistModel.clear();
        finishedTasklistModel.clear();
        for (Task t : taskData.getTasks()) {
            if (t.getStatus() == Task.Status.IN_PROGRESS) {
                tasklistModel.addElement(t);
            } else {
                finishedTasklistModel.addElement(t);
            }
        }
    }


    @Override
    public void save(FileHandler fileHandler) {
        // Persist tasks
        TaskData taskData = TaskData.load(fileHandler);
        taskData.getTasks().clear();
        for (int i = 0; i < tasklistModel.size(); i++) {
            taskData.getTasks().add(tasklistModel.get(i));
        }
        for (int i = 0; i < finishedTasklistModel.size(); i++) {
            taskData.getTasks().add(finishedTasklistModel.get(i));
        }
        taskData.save(fileHandler);
        // Persist player stats
        PlayerData.save(fileHandler);
    }

    @Override
    public void onEscapePressed() {
        setNextWindow(WindowStateName.MISSION_SELECT);
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        close();
    }

    private void AddTask() {
        String desc = JOptionPane.showInputDialog("Enter a task name:");
        if (desc == null || desc.isBlank()) return;
        Task t = new Task(desc, 100, Task.Status.IN_PROGRESS);
        tasklistModel.addElement(t);
        save(fileHandler);
    }

    private void RemoveTask() {
        int idx = tasklist.getSelectedIndex();
        if (idx == -1) return;
        Task t = tasklistModel.remove(idx);
        t.setStatus(Task.Status.COMPLETED);
        finishedTasklistModel.addElement(t);

        // Award XP
        playerXP.addXp(t.getXp());
        PlayerData.xp    = playerXP.getXp();
        PlayerData.level = playerXP.getLevel();
        PlayerData.save(fileHandler);
        xpLabel.setText("XP: " + playerXP.getXp() + " / 1000 | Level: " + playerXP.getLevel());

        save(fileHandler);
    }

    private static class TaskRenderer extends JPanel implements ListCellRenderer<Task> {
        private final JLabel label;
        public TaskRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            label.setFont(new Font("Helvetica", Font.PLAIN, 16));
            label.setBorder(new EmptyBorder(5,10,5,10));
            add(label, BorderLayout.CENTER);
            setBorder(new LineBorder(Color.BLACK,1));
        }
        @Override
        public Component getListCellRendererComponent(
                JList<? extends Task> list, Task value, int index,
                boolean isSelected, boolean cellHasFocus) {
            label.setText(value.toString());
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            return this;
        }
    }

    private static class PlayerXP {
        private int xp, level;
        public PlayerXP(int xp, int level) { this.xp = xp; this.level = level; }
        public void addXp(int amt) {
            xp += amt;
            while (xp >= 1000) {
                level++;
                xp -= 1000;
            }
        }
        public int getXp() { return xp; }
        public int getLevel() { return level; }
    }
}
