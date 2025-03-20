package ApplicationDefaults;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class BevelPanel extends JPanel {
    private Color color;
    private int padding;
    private int rounding;
    private boolean topEnabled;
    private boolean bottomEnabled;
    private boolean hasBorder;
    private int borderPadding;
    private Color borderColor;
    public BevelPanel() {
        padding = 0;
        rounding = 50;
        topEnabled = false;
        bottomEnabled = false;
        color = Color.WHITE;
        setOpaque(false);
    }

    public void setDecorativeBorder (DecorativeBorder border) {
        if (border == null) {
            hasBorder = false;
            return;
        }
        hasBorder = true;
        borderColor = border.getColor();
        borderPadding = 0;
        padding = border.getWidth();
    }

    @Override
    public void setBackground (Color color) {
        this.color = color;
    }

    public Color getColor () {
        return color;
    }

    public void setRounding (int rounding) {
        this.rounding = rounding;
    }

    public void setRoundTop (boolean flag) {
        topEnabled = flag;
    }

    public void setRoundBottom (boolean flag) {
        bottomEnabled = flag;
    }

    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) (g.create());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if (hasBorder) {
            g2d.setPaint(borderColor);
            g2d.fill(new RoundRectangle2D.Double(borderPadding, borderPadding, getWidth() - 2 * borderPadding, getHeight() - 2 * borderPadding, rounding, rounding));
            if (!bottomEnabled)
                g2d.fillRect(borderPadding,getHeight()/2,getWidth()-2*borderPadding,getHeight()/2-borderPadding+1);
            if (!topEnabled)
                g2d.fillRect(borderPadding,borderPadding,getWidth()-2*borderPadding,getHeight()/2-borderPadding-1);
        }
        g2d.setPaint(getColor());
        g2d.fill(new RoundRectangle2D.Double(padding,padding,getWidth()-2*padding,getHeight()-2*padding,rounding,rounding));
        if (!bottomEnabled)
            g2d.fillRect(padding,getHeight()/2,getWidth()-2*padding,getHeight()/2-padding+1);
        if (!topEnabled)
            g2d.fillRect(padding,padding,getWidth()-2*padding,getHeight()/2-padding-1);
        g2d.dispose();
    }
}