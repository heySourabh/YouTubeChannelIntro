package youtubechannelintro;

import java.awt.Color;

public class Shape {
    int x, y, width, height;
    Color color;
    double z;
    ShapeType type;

    public Shape(int x, int y, int width, int height, Color color, double z, ShapeType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.z = z;
        this.type = type;
    }
}
