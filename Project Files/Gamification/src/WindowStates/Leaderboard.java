package WindowStates;

import ApplicationDefaults.WindowState;
import DataStructures.FileHandler;

import javax.swing.*;
import java.awt.*;

public class Leaderboard extends WindowState {
    public Leaderboard () {
        super(WindowStateName.LEADERBOARD);
        getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel("HELLO WORLD2!");
        getContentPane().add(label,BorderLayout.CENTER);
    }
    @Override
    public void save(FileHandler fileHandler) {

    }

    @Override
    public void load(FileHandler fileHandler) {
        logMessages("We loaded from data: \"4\": " + fileHandler.retrieveInt("4"));
    }
}
