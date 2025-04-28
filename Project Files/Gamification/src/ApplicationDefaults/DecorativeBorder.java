package ApplicationDefaults;

import java.awt.*;

public class DecorativeBorder {
    private Color color;
    private int width;
    public DecorativeBorder() {
        this.color = Color.BLACK;
        this.width = 1;
    }
    public DecorativeBorder(Color color) {
        this.color = color;
        this.width = 1;
    }

    public DecorativeBorder(Color color, int width) {
        this.color = color;
        this.width = width;
    }

    public Color getColor () {
        return color;
    }

    public int getWidth () {
        return width;
    }
}