package ApplicationDefaults;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RoundingResize extends ComponentAdapter {
    private BevelPanel panel;
    private int radius;
    private int diagonal;
    private JPanel alternatePanel;
    public RoundingResize (BevelPanel panel, int borderRadius, int width, int height) {
        this.panel = panel;
        this.radius = borderRadius;
        this.diagonal = (int)Math.sqrt(width*width+height*height);
    }

    public RoundingResize (BevelPanel panel, int borderRadius, int width, int height, JPanel alternatePanel) {
        this(panel,borderRadius,width,height);
        this.alternatePanel = alternatePanel;
    }

    public void setAlternatePanel (JPanel panel) {
        this.alternatePanel = panel;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        JPanel panel = alternatePanel == null ? this.panel : this.alternatePanel;
        int currentDiagonal = (int)Math.sqrt(panel.getWidth()*panel.getWidth()+
                panel.getHeight()*panel.getHeight());
        if (currentDiagonal == 0) {
            double height = panel.getPreferredSize().getHeight();
            double width = panel.getPreferredSize().getWidth();
            currentDiagonal = (int)Math.sqrt(width*width+height*height);
        }
        double scaleFactor = 1.0*currentDiagonal*currentDiagonal/diagonal/diagonal;
        this.panel.setRounding((int)(radius*scaleFactor));
        this.panel.repaint();
        this.panel.revalidate();
    }
}
