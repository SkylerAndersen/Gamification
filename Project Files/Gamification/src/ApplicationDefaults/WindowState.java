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
    private WindowStateName nextWindow;
    private WindowStateEvent closeEvent;
    private boolean isStartupScreen;
    private String title;

    /**
     * Set up state with a name.
     * @param name WindowStateName that should be unique to this new window state.
     * */
    public WindowState (WindowStateName name) {
        this.name = name;
        nextWindow = null;
        isStartupScreen = false;
        closeEvent = WindowStateEvent.CLOSE_APP;
        title = "Questify ToDo";
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
     * Simply closes the WindowState. Allows GUI to choose next window state
     * based on nextWindowState and closeEvent
     * */
    public void close () {
        relay.notify(closeEvent);
    }

    /**
     * Sets the event the manager should be notified upon at close.
     * @param closeEvent WindowStateEvent that will be passed to the notifier relay.
     * */
    public void setCloseEvent (WindowStateEvent closeEvent) {
        this.closeEvent = closeEvent;
    }

    /**
     * Returns the event to be performed on close.
     * @return the current WindowStateEvent associated with this WindowState
     * */
    public WindowStateEvent getCloseEvent () {
        return closeEvent;
    }

    /**
     * Get the window state these preferences apply to.
     * @return the WindowState these preferences apply to.
     * */
    public WindowState getWindowState () {
        return this;
    }

    /**
     * Get the name of the WindowState these preferences apply to.
     * @return WindowStateName for the WindowState these preferences apply to.
     * */
    public WindowStateName getWindowStateName () {
        return name;
    }

    /**
     * Gets the name of the nextWindow opened after this window state closes. may be null.
     * @return WindowStateName for the window state preferred after this one closes, or null.
     * */
    public WindowStateName getNextWindow () {
        return nextWindow;
    }

    /**
     * Sets the next window to be displayed when this one closes.
     * @param name WindowStateName of the next WindowState to be displayed.
     * */
    public void setNextWindow (WindowStateName name) {
        this.nextWindow = name;
    }

    /**
     * @return if this prefers to be the startup screen.
     * */
    public boolean isStartupScreen () {
        return isStartupScreen;
    }

    /**
     * Changes startup status of window state
     * @param isStartupScreen if this window should be displayed on start
     * */
    public void setStartupScreen (boolean isStartupScreen) {
        this.isStartupScreen = isStartupScreen;
    }

    /**
     * Set the title of the window in the top bar of the JFrame, where you drag the window.
     * @param title the new title to be taken on by the application.
     * */
    public void setTitle (String title) {
        this.title = title;
    }

    /**
     * Get the title the WindowState wants for the top bar of the JFrame, where you drag the window.
     * @return the requested title to be taken on by the application.
     * */
    public String getTitle () {
        return this.title;
    }

    /**
     * An overridable method that runs if escape is pressed and if the window state is attached
     * to a UserInputListener.
     * */
    public void onEscapePressed () {

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
