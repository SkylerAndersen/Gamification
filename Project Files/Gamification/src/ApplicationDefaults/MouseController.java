package ApplicationDefaults;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController {
    private AcceptMouseResponse subject;
    private MouseMotionListener motionListener;
    private MouseAdapter mouseListener;

    public MouseController(AcceptMouseResponse subject) {
        this.subject = subject;
        this.mouseListener = new ClickListener(this);
        this.motionListener = new MotionListener(this);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
            SwingUtilities.invokeLater(() -> {
                subject.onRightClick(e.getX(), e.getY() - 30);
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                subject.onLeftClick(e.getX(), e.getY() - 30);
            });
        }
    }

    public void mouseDragged(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            subject.onDrag(e.getX(), e.getY() - 30);
        });
    }

    public MouseListener getMouseListener () {
        return mouseListener;
    }

    public MouseMotionListener getMouseMotionListener () {
        return motionListener;
    }

    private class MotionListener implements MouseMotionListener {
        private final MouseController controller;

        public MotionListener(MouseController controller) {
            this.controller = controller;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            controller.mouseDragged(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // pass
        }
    }

    private class ClickListener extends MouseAdapter {
        private final MouseController controller;
        public ClickListener (MouseController controller) {
            this.controller = controller;
        }

        @Override
        public void mouseReleased (MouseEvent e) {
            controller.mouseReleased(e);
        }
    }
}