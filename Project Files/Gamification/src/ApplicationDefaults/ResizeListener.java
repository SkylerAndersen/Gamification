package ApplicationDefaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeListener extends ComponentAdapter {
    private Container parent;
    private JPanel panel;
    private int offsetX;
    private int offsetY;
    private int xCrop;
    private int yCrop;
    private int translateX;
    private int translateY;
    private boolean ignoreY;
    public ResizeListener (Container parent, JPanel panel) {
        this.parent = parent;
        this.panel = panel;
        this.offsetX = 0;
        this.offsetY = 0;
        this.xCrop = 0;
        this.yCrop = 0;
        this.translateX = 0;
        this.translateY = 0;
        this.ignoreY = false;
    }
    public ResizeListener (Container parent, JPanel panel, int offsetX, int offsetY) {
        this.parent = parent;
        this.panel = panel;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.xCrop = 0;
        this.yCrop = 0;
        this.translateX = 0;
        this.translateY = 0;
        this.ignoreY = false;
    }
    public ResizeListener (Container parent, JPanel panel, int offsetX, int offsetY, int xCrop, int yCrop) {
        this.parent = parent;
        this.panel = panel;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.xCrop = xCrop;
        this.yCrop = yCrop;
        this.translateX = 0;
        this.translateY = 0;
        this.ignoreY = false;
    }

    public void setIgnoreY (boolean ignoreY) {
        this.ignoreY = ignoreY;
    }

    public void translate (int x, int y) {
        this.translateX = x;
        this.translateY = y;
    }

    @Override
    public void componentResized (ComponentEvent e) {
        int offX = offsetX >= 0 ? offsetX : parent.getWidth()+offsetX;
        int offY = offsetY >= 0 ? offsetY : parent.getHeight()+offsetY;

        if (!ignoreY)
            panel.setBounds(offX+translateX,offY+translateY,parent.getWidth()-offX-xCrop,
                    parent.getHeight()-offY-yCrop);
        else
            panel.setBounds(offX+translateX,offY+translateY,parent.getWidth()-offX-xCrop,
                    panel.getHeight());
        panel.repaint();
        panel.revalidate();
        parent.repaint();
        parent.revalidate();
    }
}