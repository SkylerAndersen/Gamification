package ApplicationDefaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ItemResizeListener extends ComponentAdapter {
    private JPanel wrapper;
    private JPanel contentArea;
    private JPanel spacerX;
    private JPanel spacerY;
    private JPanel spacerX2;
    private JPanel spacerY2;
    public ItemResizeListener(JPanel wrapper, JPanel contentArea, JPanel spacerX, JPanel spacerX2,
                              JPanel spacerY, JPanel spacerY2) {
        this.wrapper = wrapper;
        this.contentArea = contentArea;
        this.spacerX = spacerX;
        this.spacerY = spacerY;
        this.spacerX2 = spacerX2;
        this.spacerY2 = spacerY2;
    }

    @Override
    public void componentResized (ComponentEvent e) {
        int wrapperWidth = wrapper.getWidth();
        int wrapperHeight = wrapper.getHeight();
        int sideLength = Math.min(wrapperWidth, wrapperHeight);
        int xMargin = (wrapperWidth-sideLength)/2;
        int yMargin = (wrapperHeight-sideLength)/2;
        spacerX.setPreferredSize(new Dimension(xMargin,40));
        spacerX2.setPreferredSize(new Dimension(wrapperWidth-xMargin-sideLength,40));
        spacerY.setPreferredSize(new Dimension(40,yMargin));
        spacerY2.setPreferredSize(new Dimension(40,wrapperHeight-yMargin-sideLength));
        ((BevelPanel)(contentArea.getComponent(0))).setRounding(sideLength*sideLength/200);
        wrapper.repaint();
        wrapper.revalidate();
    }
}