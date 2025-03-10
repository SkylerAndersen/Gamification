package ApplicationDefaults;

import javax.swing.*;
import java.awt.*;

/**
 * This class is a window that displays error messages printed to it at runtime.
 * This is a custom implementation to streamline error handling with multiple developers.
 * */
public class DebugHandler extends JFrame implements Runnable {
    private NotifierRelay notifier;
    private JLabel label;
    private long startTime;
    /**
     * Setup DebugHandler UI, and create notifier that instances will take inputs from.
     * */
    public DebugHandler (NotifierRelay notifier) {
        setSize(new Dimension(600,400));
        setTitle("Questify ToDo | Debug Console");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        label = new JLabel();
        add(label,BorderLayout.CENTER);
        setVisible(true);
        this.notifier = notifier;
        startTime = System.currentTimeMillis();
    }

    /**
     * run is run by the QuestifyApplication in the DebugThread. Waits five seconds for
     * the debug window to open, then for as long as the window remains open, it will
     * update the text displayed as it receives new messages. The notifier parks the
     * thread to avoid busy waiting.
     * */
    @Override
    public void run() {
        while (System.currentTimeMillis()-startTime < 5000 || this.isActive()) {
            String message = notifier.receiveMessage();
            label.setText(message);
            System.out.println(message);

            if (notifier.noticedTerminate())
                break;
        }
    }
}
