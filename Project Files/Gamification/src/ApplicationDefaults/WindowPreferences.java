package ApplicationDefaults;

import WindowStates.WindowStateName;

/**
 * Hold information about a given window state so the GUI and Manager knows what to do with it.
 * */
public abstract class WindowPreferences {
    private WindowState windowState;
    private WindowStateName nextWindow;
    private WindowStateEvent closeEvent;
    private boolean isStartupScreen;

    /**
     * Default constructor with windowstate, and next window, with isStartupScreen is false.
     * @param windowState the window state these preferences apply to.
     * @param nextWindow the name of the window state that appears when this is closed. May be null
     * */
    public WindowPreferences (WindowState windowState, WindowStateName nextWindow) {
        this(windowState,nextWindow,false);
    }

    /**
     * Overloaded constructor with a window state, and next window, plus isStartupScreen.
     * @param windowState the window state these preferences apply to.
     * @param nextWindow the name of the window state that appears when this is closed. May be null
     * @param isStartupScreen if this WindowState appears at the start of the GUI's instantiation.
     * */
    public WindowPreferences (WindowState windowState, WindowStateName nextWindow, boolean isStartupScreen) {
        this.windowState = windowState;
        this.nextWindow = nextWindow;
        this.isStartupScreen = isStartupScreen;
    }

    /**
     * Get the window state these preferences apply to.
     * @return the WindowState these preferences apply to.
     * */
    public WindowState getWindowState () {
        return windowState;
    }

    /**
     * Get the event passed to the manager when the window state is closing.
     * @return the event passed to the manager when the window state is closing.
     * */
    public WindowStateEvent getCloseEvent () {
        return closeEvent;
    }

    /**
     * Get the name of the WindowState these preferences apply to.
     * @return WindowStateName for the WindowState these preferences apply to.
     * */
    public WindowStateName getWindowStateName () {
        return windowState.getStateName();
    }

    /**
     * Gets the name of the nextWindow opened after this window state closes. may be null.
     * @return WindowStateName for the window state preferred after this one closes, or null.
     * */
    public WindowStateName getNextWindow () {
        return nextWindow;
    }

    /**
     * @return if this prefers to be the startup screen.
     * */
    public boolean isStartupScreen () {
        return isStartupScreen;
    }
}
