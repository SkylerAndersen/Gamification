package WindowStates;

import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends WindowState {
    /* This is an ActionListener that closes this WindowState.
    the reason I made it an action listener is so it can be triggered by a timer
    */
    static class SpecialActionListener implements ActionListener {
        private Login login;
        public SpecialActionListener (Login login) {
            this.login = login;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            // This is how you close a window state
            login.close();
        }
    }
    private Timer timer;
    public Login () {
        // basic setup for a window state
        super(WindowStateName.LOGIN);
        super.setStartupScreen(true);
        getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel("HELLO WORLD!");
        getContentPane().add(label,BorderLayout.CENTER);

        /* create a timer to trigger switching the window state
        usually, this will probably be a button or something else.
        */
        timer = new Timer(5000, new SpecialActionListener(this));
        timer.setRepeats(false);

        // setup behavior telling app how to switch
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        setNextWindow(WindowStateName.LEADERBOARD);
    }

    @Override
    public void save(FileHandler fileHandler) {
        // saving some data
        fileHandler.save("4",10);
    }

    @Override
    public void load(FileHandler fileHandler) {
        // 5-second timer that switches the windowstate starts when it is loaded
        timer.start();
    }
}