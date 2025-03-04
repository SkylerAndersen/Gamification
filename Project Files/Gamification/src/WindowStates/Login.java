package WindowStates;

import ApplicationDefaults.WindowState;

import javax.swing.*;
import java.awt.*;

public class Login extends WindowState {
    public Login () {
        super(WindowStateName.LOGIN);
        getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel("HELLO WORLD!");
        getContentPane().add(label,BorderLayout.CENTER);
    }
}
