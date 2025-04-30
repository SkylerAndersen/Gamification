package ApplicationDefaults;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserInputListener implements KeyListener {
    private WindowState windowState;
    public UserInputListener () {
    }
    public void setWindowState (WindowState windowState) {
        this.windowState = windowState;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESCAPE");
            if (windowState != null)
                SwingUtilities.invokeLater(windowState::onEscapePressed);
        }
    }
}
