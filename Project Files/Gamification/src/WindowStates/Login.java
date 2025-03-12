package WindowStates;

import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends WindowState {
    public Login () {
        super(WindowStateName.LOGIN);
        getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel("HELLO WORLD!");
        getContentPane().add(label,BorderLayout.CENTER);
    }

    @Override
    public void save(FileHandler fileHandler) {
        fileHandler.save("Username","Password123 For Example");
    }

    @Override
    public void load(FileHandler fileHandler) {

    }
}
