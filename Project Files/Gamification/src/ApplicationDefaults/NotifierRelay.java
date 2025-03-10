package ApplicationDefaults;

import DataStructures.FixedContinuousQueue;

import java.util.concurrent.locks.LockSupport;

/**
 * DebugHandler exits on a parked thread. The notifier receives messages from the application
 * for debug purposes, and unparks the DebugHandler, waking it up. The DebugHandler then
 * calls receiveMessage() to receive the message from the buffer. WindowStateManager handled
 * similarly. The notify method adds WindowStateEvents to the buffer, and unparks the manager
 * which recieves it with recieveEvent().
 * */
public class NotifierRelay {
    private Thread debugThread;
    private Thread managerThread;
    private StringBuffer debugBuffer;
    private FixedContinuousQueue<WindowStateEvent> eventBuffer;
    private boolean noticedTerminate;
    private boolean debugMode;

    public NotifierRelay() {
        this(false);
    }
    public NotifierRelay(boolean debugMode) {
        debugBuffer = new StringBuffer();
        eventBuffer = new FixedContinuousQueue<>(50);
        this.debugMode = debugMode;
        this.noticedTerminate = false;
    }
    /**
     * Notify should be used by the application GUI and subclasses like WindowState.
     * @param message Message from application used for debugging.
     * */
    public void notify (String message) {
        if (!debugMode)
            return;
        debugBuffer.append(message);
        if (debugThread != null)
            LockSupport.unpark(debugThread);
    }
    /**
     * Notify should be used by the application GUI and subclasses like WindowState.
     * @param event Event that the QuestifyApplication should request the application to perform.
     * */
    public void notify (WindowStateEvent event) {
        System.out.println("notifying of event: " + event.name());
        if (event == WindowStateEvent.CLOSE_APP) {
            noticedTerminate = true;
            System.out.println("notifying debug to terminate");
            notify("terminate program");
        }
        try {
            eventBuffer.enqueue(event);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        if (managerThread != null)
            LockSupport.unpark(managerThread);
    }
    /**
     * Receive should be used by the DebugHandler.
     * @return Message from application used for debugging.
     * */
    public String receiveMessage () {
        if (debugThread == null)
            debugThread = Thread.currentThread();
        while (debugBuffer.isEmpty()) {
            LockSupport.park();
        }
        String message = debugBuffer.toString();
        debugBuffer.setLength(0);
        return message;
    }
    /**
     * Receive should be used by the WindowStateManager.
     * @return Event from application used for managing WindowStates.
     * */
    public WindowStateEvent receiveEvent () {
        if (managerThread == null)
            managerThread = Thread.currentThread();
        while (!eventBuffer.hasNext()) {
            LockSupport.park();
        }
        WindowStateEvent event = null;
        try {
            event = eventBuffer.dequeue();
        } catch (Exception e) {e.printStackTrace();}

        System.out.println(event == null ? "failed" : "receiving event: " + event.name());
        return event;
    }
    /**
     * Checks if we noticed a terminate event. Used by DebugHandler to safely detect
     * when the program is terminating without relying on receiveMessage(), which could
     * lead to unsafe injection conditions, (i.e. you want to print the message "terminate."
     * to the DebugHandler, and end up terminating the DebugHandler instead).
     * @return boolean on if the relay noticed that the manager has been notified of the app closing.
     * */
    public boolean noticedTerminate () {
        return noticedTerminate;
    }
}
