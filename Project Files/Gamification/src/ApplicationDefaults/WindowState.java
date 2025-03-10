package ApplicationDefaults;

import DataStructures.FileHandler;
import WindowStates.WindowStateName;

import javax.swing.*;

/**
 * A window state that the program may take on. Extend this to make new WindowStates.
 * */
public abstract class WindowState extends JWindow {
    private NotifierRelay relay;
    private WindowStateName name;

    /**
     * Set up state with a name.
     * @param name WindowStateName that should be unique to this new window state.
     * */
    public WindowState (WindowStateName name) {
        this.name = name;
    }

    /**
     * get the name of this state
     * @return WindowStateName corresponding to this state
     * */
    public WindowStateName getStateName () {
        return name;
    }

    /**
     * Attach a notifier that is connected to a DebugHandler to send messages to the handler.
     * @param relay A NotifierRelay that should be connected to a DebugHandler.
     * */
    public void attachNotifierRelay (NotifierRelay relay) {
        this.relay = relay;
    }

    /**
     * Log error messages to the debugHandler
     * @param debugMessages messages to be passed on to the DebugHandler through the relay.
     * */
    public void logMessages (String debugMessages) {
        if (relay == null)
            return;
        relay.notify(debugMessages);
    }

    /**
     * WindowStates must implement a method to save data when closed.
     * */
    public abstract void save (FileHandler fileHandler);
    /**
     * WindowStates must implement a method to load data when opened.
     * */
    public abstract void load (FileHandler fileHandler);
}
