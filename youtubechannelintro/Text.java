package youtubechannelintro;

import java.awt.Color;
import java.awt.Font;

public class Text {
    Font font;
    String str;
    int x, y;
    Color color;
    double z;

    public Text(Font font, String str, int x, int y, Color color, double z) {
        this.font = font;
        this.str = str;
        this.x = x;
        this.y = y;
        this.color = color;
        this.z = z;
    }
}
