package WindowStates;

import ApplicationDefaults.WindowState;
import DataStructures.FileHandler;
import DataStructures.PlayerData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


public class ToDos extends WindowState {


    // creating the object to store into the tasks so that the task can contain things such as xp and items
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

        public String toString(){
            return task + " " + taskXp + " XP";
        }
    }

    private class playerXP{
        private int xp;
        private int level;
        //level starts at 1, xp starts at 0
        public playerXP(int xp, int level){
            this.xp = xp;
            this.level = level;
        }
        //method to add xp with actions
        public void addXp(int amount){
            xp += amount;
            levelUp();
        }
        //creates threshhold for levels (1000 xp for now) and checks whether level should increase then resets xp
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

    private DefaultListModel<mission> tasklistModel; // preferences for default model which holds the items in taskList
    private JList<mission> tasklist; //preferences for visual representation of task list
    private DefaultListModel<mission> finishedTasklistModel;
    private JList<mission> finishedTasklist;
    private JLabel xpLabel;
    private playerXP playerXP;

    //creates the window and layout
    public ToDos() {
        super(WindowStateName.TODOS);
        super.setStartupScreen(false);

        getContentPane().setLayout(new GridLayout(3, 3, 10, 10));

        JLabel label = new JLabel("To-Do List Page");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        playerXP = new playerXP(0, 1);
        xpLabel = new JLabel("XP: " + playerXP.getXp() + " / 1000 | Level: " + playerXP.getLevel());
        getContentPane().add(xpLabel);

//TASK LIST MAIN LAYOUT set up
        tasklistModel = new DefaultListModel<>(); //create main task storing model (model allows it to be dynamic)
        tasklist = new JList<>(tasklistModel); //create list
        tasklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //each item is individually selectable
        tasklist.setFont(new Font("ARIEL", Font.PLAIN, 16)); // changes font and font size
        tasklist.setCellRenderer(new taskRenderer());

//where the task list shows up on screen
        JScrollPane scrollPane = new JScrollPane(tasklist);
        scrollPane.setPreferredSize(new Dimension(250, 250));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

//font and size, making borders for visual aid
        JLabel taskLabel = new JLabel("Tasklist");
        taskLabel.setVerticalAlignment(SwingConstants.NORTH);
        getContentPane().add(taskLabel, BorderLayout.WEST);
        taskLabel.setFont(new Font("ARIEL", Font.PLAIN, 24));
        taskLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

//default elements in the task list
        tasklistModel.addElement(new mission("Dishes", 100));
        tasklistModel.addElement(new mission("Laundry", 150));
        tasklistModel.addElement(new mission("Cleaning", 200));
        tasklistModel.addElement(new mission("Eating", 250));
        tasklistModel.addElement(new mission("Sleeping", 300));
        tasklistModel.addElement(new mission("Homework", 500));
        tasklistModel.addElement(new mission("Task1", 500));
        tasklistModel.addElement(new mission("Task2", 500));
        tasklistModel.addElement(new mission("Task3", 500));

//Finished tasks list layout set up
        finishedTasklistModel = new DefaultListModel<>();
        finishedTasklist = new JList<>(finishedTasklistModel);
        finishedTasklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        finishedTasklist.setFont(new Font("ARIEL", Font.PLAIN, 16));
        finishedTasklist.setCellRenderer(new taskRenderer());
//scroll pane to implement scroll bar if there are too many tasks for finished tasks
        JScrollPane scrollPane2 = new JScrollPane(finishedTasklist);
        scrollPane2.setPreferredSize(new Dimension(250, 250));
        getContentPane().add(scrollPane2, BorderLayout.SOUTH);

        JLabel finishedTaskLabel = new JLabel("Finished Tasklist");
        getContentPane().add(finishedTaskLabel, BorderLayout.WEST);
        finishedTaskLabel.setFont(new Font("ARIEL", Font.PLAIN, 24));

//Buttons to add and remove tasks
        JPanel buttonPanel = new JPanel(); //panel to keep add and remove buttons together
        buttonPanel.setLayout(new FlowLayout());
        JButton addButton = new JButton("Add Task"); // button that allows user to add a task
        JButton removeButton = new JButton("Remove Task"); //button that allows users to remove tasks
        buttonPanel.add(addButton); //add tasks
        buttonPanel.add(removeButton); //remove tasks
        getContentPane().add(buttonPanel,BorderLayout.EAST); //buttons are to the right under the to do list

        addButton.addActionListener(e -> addTask()); //buttons are directed to their logic on click
        removeButton.addActionListener(e -> removeTask());
    }

    private static class taskRenderer extends JPanel implements ListCellRenderer<mission> {
        private JLabel label;

        public taskRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            label.setFont(new Font("Monospaced", Font.PLAIN, 16));
            label.setBorder(new EmptyBorder(5, 10, 5, 10));
            add(label, BorderLayout.CENTER);
            setBorder(new LineBorder(Color.BLACK, 1));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends mission> list, mission value, int index, boolean isSelected, boolean cellHasFocus) {
            label.setText(value.toString());
            if (isSelected) {
                setBackground(Color.LIGHT_GRAY);
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }

    private void addTask(){
        String task = JOptionPane.showInputDialog("Please enter a task name");
        if (task == null || task.trim().isEmpty()) {
            System.out.println("task is null");
            return;
        }
        tasklistModel.addElement(new mission(task, 100));
    }

    private void removeTask() {
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
    public void save(FileHandler fileHandler) {

    }
    @Override
    public void load(FileHandler fileHandler) {

    }
}
