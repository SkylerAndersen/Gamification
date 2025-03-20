package WindowStates;

import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


public class ToDos extends WindowState {

    //list models and list initialize for the task list and finished task list
    private DefaultListModel<mission> tasklistModel;
    private JList<mission> tasklist;
    private DefaultListModel<mission> finishedTasklistModel;
    private JList<mission> finishedTasklist;

    //labels for the xp and level
    private JLabel xpLabel;
    private PlayerXP playerXP;

    //main ui
    public ToDos() {
        super(WindowStateName.TODOS);
        super.setStartupScreen(false);

        //gridbag layout similar to login page
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        /**
         * Player xp and level labels
         * */

        //player xp and level label on ui
        playerXP = new PlayerXP(0, 1);
        xpLabel = new JLabel("XP: " + playerXP.getXp() + " / 1000 | Level: " + playerXP.getLevel());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        getContentPane().add(xpLabel, gbc);


        /**
         * Task list section
         * */

        //tasklist label gridbag layout
        JLabel taskLabel = new JLabel("Tasklist");
        taskLabel.setFont(new Font("ARIEL", Font.PLAIN, 24));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        getContentPane().add(taskLabel, gbc);

        //actual tasklist layout and grid as well as adding functionality (scroll and selection)
        tasklistModel = new DefaultListModel<>();
        tasklist = new JList<>(tasklistModel);
        tasklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasklist.setFont(new Font("ARIEL", Font.PLAIN, 16));
        tasklist.setCellRenderer(new TaskRenderer());

        //new scroll pane for task list scrollability
        JScrollPane scrollPane = new JScrollPane(tasklist);
        scrollPane.setPreferredSize(new Dimension(250, 250));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        getContentPane().add(scrollPane, gbc);

        /**
         * Default tasks and layout for adding new tasks
         * */

        //adding default tasks to the tasklist as well as their xp
        tasklistModel.addElement(new mission("Dishes", 100));
        tasklistModel.addElement(new mission("Laundry", 150));
        tasklistModel.addElement(new mission("Cleaning", 200));
        tasklistModel.addElement(new mission("Eating", 250));
        tasklistModel.addElement(new mission("Sleeping", 300));
        tasklistModel.addElement(new mission("Homework", 500));
        tasklistModel.addElement(new mission("Task1", 500));
        tasklistModel.addElement(new mission("Task2", 500));
        tasklistModel.addElement(new mission("Task3", 500));


        /**
         * Buttons for adding and removing tasks
         * */

        //add and remove task button gridbag layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");

        //adding buttons to button panel
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        //button panel frame layout
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        getContentPane().add(buttonPanel, gbc);

        //functionality for the buttons
        addButton.addActionListener(e -> AddTask());
        removeButton.addActionListener(e -> RemoveTask());

        /**
         * Button for going back to main menu
         * */

        JButton missionSelectButton = new JButton("Back to Mission Select");
        buttonPanel.add(missionSelectButton);
        missionSelectButton.addActionListener(e -> {
            setCloseEvent(WindowStateEvent.SWITCH_STATE);
            setNextWindow(WindowStateName.MISSION_SELECT);
            close();
        });



        /**
        * Finished task section
        * */
        //label for the finished tasklist and its gridbag layout
        JLabel finishedTaskLabel = new JLabel("Finished Tasklist");
        finishedTaskLabel.setFont(new Font("ARIEL", Font.PLAIN, 24));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        getContentPane().add(finishedTaskLabel, gbc);

        //Finished task list layout as well as scroll pane and selection
        finishedTasklistModel = new DefaultListModel<>();
        finishedTasklist = new JList<>(finishedTasklistModel);
        finishedTasklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        finishedTasklist.setFont(new Font("ARIEL", Font.PLAIN, 16));
        finishedTasklist.setCellRenderer(new TaskRenderer());

        //second scroll pane for finished task lsit
        JScrollPane scrollPane2 = new JScrollPane(finishedTasklist);
        scrollPane2.setPreferredSize(new Dimension(250, 250));

        //gbc frame for finished tasklist
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        getContentPane().add(scrollPane2, gbc);


    }

    // mission class creates variables for adding and removing mission
    private class mission {
        public int taskXp;
        public String task;

        public mission(String task, int xp){
            this.taskXp = xp;
            this.task = task;
        }

        public String getTask(){
            return task;
        }

        public int getXp(){
            return taskXp;
        }

        //this is how the tasks show up in the list
        public String toString(){
            return task + " " + taskXp + " XP";
        }
    }

    //player xp class creates method for leveling up as well as tracking xp
    private class PlayerXP {
        private int xp;
        private int level;

        public PlayerXP(int xp, int level){
            this.xp = xp;
            this.level = level;
        }

        public void addXp(int amount){
            xp += amount;
            levelUp();
        }
        //new level threshold
        public void levelUp(){
            int threshhold = 1000;
            while (xp >= threshhold){
                this.level += 1;
                this.xp -= threshhold;
            }
        }

        public int getXp() {
            return xp;
        }

        public int getLevel(){
            return level;
        }
    }

    //task renderer makes sure all tasks are the same font despite which list they are in, sets border
    private static class TaskRenderer extends JPanel implements ListCellRenderer<mission> {
        private JLabel label;

        public TaskRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            label.setFont(new Font("Monospaced", Font.PLAIN, 16));
            label.setBorder(new EmptyBorder(5, 10, 5, 10));
            add(label, BorderLayout.CENTER);
            setBorder(new LineBorder(Color.BLACK, 1));
        }

        //cell renderer changes task background color when selecting task
        @Override
        public Component getListCellRendererComponent(
                JList<? extends mission> list, mission value, int index, boolean isSelected, boolean cellHasFocus) {
            label.setText(value.toString());
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            return this;
        }
    }

    //method to add task to the task list, default xp is 100 for added tasks
    private void AddTask(){
        String task = JOptionPane.showInputDialog("Please enter a task name");
        if (task == null || task.trim().isEmpty()) {
            return;
        }
        tasklistModel.addElement(new mission(task, 100));
    }

    //removes task from tasklist, places in finished list, also grants xp based on task
    private void RemoveTask() {
        int selectedIndex = tasklist.getSelectedIndex();
        if (selectedIndex != -1) {
            mission task = tasklistModel.getElementAt(selectedIndex);
            tasklistModel.remove(selectedIndex);
            finishedTasklistModel.addElement(task);
            playerXP.addXp(task.getXp());
            xpLabel.setText("XP: " + playerXP.getXp() + " / 1000 | Level: " + playerXP.getLevel());
        }
    }

    @Override
    public void save(FileHandler fileHandler) {}

    @Override
    public void load(FileHandler fileHandler) {}
}
