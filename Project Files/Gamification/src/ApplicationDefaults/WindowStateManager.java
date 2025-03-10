package ApplicationDefaults;

import DataStructures.FileHandler;
import WindowStates.WindowStateName;

/**
 * Manager that manages the state of the GUI. Takes in events passed by the GUI, and synchronously
 * requests the GUI to respond. Runnable on a separate thread.
 * */
public class WindowStateManager implements Runnable {
    private ApplicationGUI gui;
    private WindowPreferences[] allWindowPreferences;
    private NotifierRelay notifier;
    private long startTime;
    private FileHandler fileHandler;

    /**
     * Create manager, given a gui, preferences, and a notifier to take cues from.
     * @param gui the GUI that is being managed
     * @param allWindowPreferences an array of all the WindowPreferences available to the gui.
     * @param notifier notifier relay that parks and un-parks this thread. Tells manager what GUI is doing.
     * */
    public WindowStateManager (ApplicationGUI gui, WindowPreferences[] allWindowPreferences,
                               NotifierRelay notifier, FileHandler fileHandler) {
        startTime = System.currentTimeMillis();
        this.gui = gui;
        this.allWindowPreferences = allWindowPreferences;
        this.notifier = notifier;
        this.fileHandler = fileHandler;
    }

    /**
     * Loop run on manager thread to respond to notifications and handle tasks.
     * */
    @Override
    public void run () {
        while (System.currentTimeMillis() - startTime < 5000 || (gui.isInitialized() && gui.getFrame().isActive())) {
            WindowStateEvent event = notifier.receiveEvent();
            System.out.println("Manager sees event: " + event.name());

            // this allows thread to terminate, and prevents it from parking
            // this is called by closing hooks from the GUI
            if (event == WindowStateEvent.CLOSE_APP)
                break;
            if (event == WindowStateEvent.SWITCH_STATE) {
                WindowStateName currentState = gui.getActiveState();
                for (WindowPreferences preferences : allWindowPreferences) {
                    if (preferences.getWindowStateName().equals(currentState)) {
                        preferences.getWindowState().save(fileHandler);
                        break;
                    }
                }
            }
            if (event == WindowStateEvent.START_APP) {
                System.out.println("Starting app");
                WindowPreferences startPreferences = null;
                for (WindowPreferences preferences : allWindowPreferences) {
                    if (preferences.isStartupScreen()) {
                        startPreferences = preferences;
                        break;
                    }
                }
                if (startPreferences == null)
                    return;
                System.out.println("Found window Preferences");
                gui.takeState(startPreferences.getWindowState());
            }

        }
    }
}
