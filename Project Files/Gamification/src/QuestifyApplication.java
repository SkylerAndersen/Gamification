import ApplicationDefaults.*;
import DataStructures.FileHandler;

import WindowStates.*;
import WindowStates.Character;

import java.nio.file.Paths;

/**
 * This is our application. It has a GUI, but isn't the gui. It runs on the main thread.
 * This creates the other threads.
 * */
public class QuestifyApplication {
    private Thread guiThread;
    private Thread debugThread;
    private Thread managerThread;
    private NotifierRelay notifier;
    private boolean debugMode;
    private FileHandler fileHandler;
    /**
     * Default constructor, debug is false. Debug gives access to debug handler
     * */
    public QuestifyApplication () {
        this(false);
    }
    /**
     * @param debugMode enter the debug mode and receive error messages.
     *                  When false, same as default constructor.
     * */
    public QuestifyApplication (boolean debugMode) {
        this.debugMode = debugMode;
        String projectPath = Paths.get(System.getProperty("user.dir"),"resources",".").toString();
        projectPath = projectPath.substring(0,projectPath.length()-1);
        this.fileHandler = new FileHandler(projectPath);
    }
    /**
     * openGui Opens the ApplicationGUI, and begins the debug thread if necessary.
     * */
    public void openGui () {
        notifier = new NotifierRelay(debugMode);
        ApplicationGUI gui = new ApplicationGUI(notifier);

        // start gui on a new thread
        guiThread = new Thread(gui::start);
        guiThread.start();

        // if debugging, start debug handler on a new thread
        if (debugMode) {
            debugThread = new Thread(new DebugHandler(notifier));
            debugThread.start();
        }

        // start manager on a new thread
        managerThread = new Thread(new WindowStateManager(gui,getAllWindowStates(),
                notifier,fileHandler));
        managerThread.start();
    }

    /**
     * Helper method. QuestifyGUI can only see and interact with WindowStates that it understands.
     * WindowStates must have a reserved, unique, WindowStateName.
     * All window states are added here, then switched to and managed in accordance with their preferences,
     * and the preferences of other WindowStates.
     * @return an array of all the WindowStates.
     * */
    private WindowState[] getAllWindowStates () {
        WindowState[] allWindowStates = new WindowState[WindowStateName.values().length];

        // initialize all window preferences


        allWindowStates[0] = new Login();
        allWindowStates[1] = new Leaderboard();
        allWindowStates[2] = new ToDos();
        allWindowStates[3] = new Character();
        allWindowStates[4] = new MissionSelect();
//        allWindowStates[2] = new LoginRewards(); // Whatever you need this to be
//        allWindowStates[3] = new Social(); // Whatever you need this to be


        return allWindowStates;
    }
}
