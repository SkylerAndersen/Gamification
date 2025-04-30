package ApplicationDefaults;

import WindowStates.WindowStateName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Window/JFrame/GUI for the Java application. Run on a separate thread.
 * */
public class ApplicationGUI {
    private NotifierRelay debug;
    private JFrame frame;
    private WindowStateName activeState;
    private UserInputListener inputListener;
    /**
     * Setup the object with a notifier for multithreaded communication, but don't create the JFrame.
     * @param notifier multithreaded notifier that can communicate with DebugHandler and WindowStateManager.
     * */
    public ApplicationGUI (NotifierRelay notifier) {
        this.debug = notifier;
        this.frame = null;
        this.inputListener = new UserInputListener();
    }

    /**
     * create JFrame, setup UI, make it visible, then notify WindowStateManager to start the app
     * and pick the startup screen from preferences.
     * */
    public void start () {
        frame = new JFrame();
        frame.setSize(new Dimension(1200,800));
        frame.setTitle("Questify ToDo");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                debug.notify(WindowStateEvent.CLOSE_APP);
                System.out.println("Closing app");
            }
        });
        frame.setVisible(true);
        debug.notify(WindowStateEvent.START_APP);
        System.out.println("Starting App.");
    }

    /**
     * Checks if JFrame UI is declared and initialized. Used by Manager and notification system
     * to handle thread shutdowns by checking if users closed certain windows.
     * @return boolean indicating window has been created.
     * */
    public synchronized boolean isInitialized () {
        return frame != null;
    }

    /**
     * Accessor for JFrame.
     * @return JFrame for the GUI
     * */
    public synchronized JFrame getFrame () {
        return frame;
    }

    /**
     * Check which WindowState is currently displayed to users. Useful for manager on another thread.
     * @return WindowStateName, currently visible, reserved for a unique WindowState, useful for finding and changing states in the GUI.
     * */
    public synchronized WindowStateName getActiveState () {
        return activeState;
    }

    /**
     * When called, forces GUI to switch to a provided window state.
     * */
    public synchronized void takeState (WindowState state) {
        System.out.println("Taking state: " + state.getStateName().name());
        frame.setContentPane(state.getContentPane());
        frame.setVisible(true);
        for (KeyListener listener : frame.getKeyListeners()) {
            frame.removeKeyListener(listener);
            frame.getContentPane().removeKeyListener(listener);
        }
        frame.getContentPane().addKeyListener(inputListener);
        frame.getContentPane().requestFocusInWindow();
        frame.addKeyListener(inputListener);
        activeState = state.getStateName();
        frame.setTitle(state.getTitle());
        inputListener.setWindowState(state);
    }

    /**
     * When called, forces GUI to close.
     * */
    public synchronized void close () {
        frame.setVisible(false);
        frame.dispose();
    }
}
