package WindowStates;

import ApplicationDefaults.WindowState;
import DataStructures.FileHandler;

import java.awt.*;

public class Calendar extends WindowState {
    public Calendar () {
        super(WindowStateName.CALENDAR);
        setStartupScreen(true);
        getContentPane().setBackground(Color.black);
    }

    @Override
    public void save(FileHandler fileHandler) {

    }

    @Override
    public void load(FileHandler fileHandler) {

    }
}
