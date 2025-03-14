package WindowStates;

import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;

import javax.swing.*;
import java.awt.*;

public class MissionSelect extends WindowState {


    public MissionSelect () {
        super(WindowStateName.MISSION_SELECT);

        JPanel headerPanel = new JPanel();
        JLabel headerlabel = new JLabel("Mission Selection");
        headerPanel.add(headerlabel);
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        JPanel missionPanel = new JPanel();
        getContentPane().add(missionPanel, BorderLayout.CENTER);

        JButton character = new JButton("Character");
        character.addActionListener(e -> {
            setCloseEvent(WindowStateEvent.SWITCH_STATE);
            setNextWindow(WindowStateName.CHARACTER);
            close();
        });

        JButton toDo = new JButton("To-Do");
        toDo.addActionListener(e -> {
            setCloseEvent(WindowStateEvent.SWITCH_STATE);
            setNextWindow(WindowStateName.TODOS);
            close();
        });

        JButton leaderboard = new JButton("Leaderboard");
        leaderboard.addActionListener(e -> {
            setCloseEvent(WindowStateEvent.SWITCH_STATE);
            setNextWindow(WindowStateName.LEADERBOARD);
            close();
        });

        missionPanel.add(character);
        missionPanel.add(leaderboard);
        missionPanel.add(toDo);






    }





    @Override
    public void save(FileHandler fileHandler) {

    }

    @Override
    public void load(FileHandler fileHandler) {

    }
}
