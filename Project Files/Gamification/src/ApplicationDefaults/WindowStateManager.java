package ApplicationDefaults;

import DataStructures.FileHandler;
import WindowStates.WindowStateName;

import java.util.HashMap;

/**
 * Manager that manages the state of the GUI. Takes in events passed by the GUI, and synchronously
 * requests the GUI to respond. Runnable on a separate thread.
 * */
public class WindowStateManager implements Runnable {
    private ApplicationGUI gui;
    private HashMap<WindowStateName,WindowState> windowStates;
    private NotifierRelay notifier;
    private long startTime;
    private FileHandler fileHandler;

    /**
     * Create manager, given a gui, preferences, and a notifier to take cues from.
     * @param gui the GUI that is being managed
     * @param allWindowStates an array of all the WindowPreferences available to the gui.
     * @param notifier notifier relay that parks and un-parks this thread. Tells manager what GUI is doing.
     * */
    public WindowStateManager (ApplicationGUI gui, WindowState[] allWindowStates,
                               NotifierRelay notifier, FileHandler fileHandler) {
        startTime = System.currentTimeMillis();
        this.gui = gui;
        this.notifier = notifier;
        this.fileHandler = fileHandler;
        windowStates = new HashMap<>(allWindowStates.length);
        for (WindowState windowState : allWindowStates) {
            if (windowState == null)
                continue;
            windowStates.put(windowState.getStateName(),windowState);
            windowState.attachNotifierRelay(notifier);
        }
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
            if (event == WindowStateEvent.CLOSE_APP) {
                windowStates.get(gui.getActiveState()).save(fileHandler);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            if (event == WindowStateEvent.SWITCH_STATE) {
                WindowState currentState = windowStates.get(gui.getActiveState());
                WindowState nextState = windowStates.get(currentState.getNextWindow());
                currentState.save(fileHandler);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gui.takeState(nextState);
                nextState.load(fileHandler);
            }
            if (event == WindowStateEvent.START_APP) {
                System.out.println("Starting app");
                WindowState startState = null;
                for (WindowStateName state : windowStates.keySet()) {
                    if (windowStates.get(state).isStartupScreen()) {
                        startState = windowStates.get(state);
                        break;
                    }
                }
                if (startState == null)
                    continue;
                System.out.println("Found window Preferences");
                gui.takeState(startState);
                startState.load(fileHandler);
            }
        }
    }
}
