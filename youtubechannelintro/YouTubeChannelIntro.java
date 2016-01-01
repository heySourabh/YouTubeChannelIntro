package youtubechannelintro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class YouTubeChannelIntro {

    static final int FRAMES_PER_SEC = 30;
    static final double TOTAL_SECS = 5;
    static int SCR_VELOCITY = 80; // Pixels per second

    static BufferedImage img;
    static Graphics2D gr;

    public static void main(String[] args) throws IOException {
        int width = 1280;
        int height = 800;

        img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        gr = img.createGraphics();

        ArrayList<Shape> shapes = new ArrayList<>();
        final int NUM_SHAPES = 1000;

        Random rand = new Random(5);
        int offset = (int) (SCR_VELOCITY * TOTAL_SECS);
        for (int count = 0; count < NUM_SHAPES; count++) {
            int i = rand.nextInt(width + 2 * offset) - offset;
            int j = rand.nextInt(height + 2 * offset) - offset;
            Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.5f);

            float p = rand.nextFloat();

            int objectSize = width / 3;
            int w = rand.nextInt(objectSize);
            int h = rand.nextInt(objectSize);
            double z = (NUM_SHAPES - 1.0 - count) / (NUM_SHAPES - 1.0);
            if (p < 0.33) {
                // Oval
                shapes.add(new Shape(i - w / 2, j - h / 2, w, h, color, z, ShapeType.OVAL));
            } else if (p < 0.66) {
                // rect
                shapes.add(new Shape(i - w / 2, j - h / 2, w, h, color, z, ShapeType.RECT));
            } else {
                // rounded rect
                shapes.add(new Shape(i - w / 2, j - h / 2, w, h, color, z, ShapeType.ROUND_RECT));
            }
        }

        final int NUM_TEXTS = 50;
        ArrayList<Text> texts = new ArrayList<>();
        for (int count = 0; count < NUM_TEXTS; count++) {
            Font font = new Font("Serif", Font.BOLD | Font.ITALIC, 10 + rand.nextInt(height / 10));
            int i = rand.nextInt(width + 2 * offset) - offset;
            int j = rand.nextInt(height + 2 * offset) - offset;
            Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.3f);
            double z = (NUM_TEXTS - 1.0 - count) / (NUM_TEXTS - 1.0);
            texts.add(new Text(font, "Code4Lyfe", i, j, color, z));
        }

        long totalFrames = Math.round(TOTAL_SECS * FRAMES_PER_SEC);
        double dt = 2.0 * TOTAL_SECS / totalFrames;
        double time = 0;

        for (int frame = 0; frame < totalFrames; frame++) {
            System.out.println("Frame: " + frame);
            gr.setColor(Color.BLACK);
            gr.fillRect(0, 0, width, height);
            String fileName = String.format("image%05d.png", frame);
            for (int shapeIndex = 0; shapeIndex < shapes.size(); shapeIndex++) {
                Shape shape = shapes.get(shapeIndex);
                int dx = getPixelsMoved(time, shape.z);
                gr.setColor(shape.color);
                if (shape.type == ShapeType.OVAL) {
                    gr.fillOval(shape.x + dx, shape.y, shape.width, shape.height);
                } else if (shape.type == ShapeType.RECT) {
                    gr.fillRect(shape.x + dx, shape.y, shape.width, shape.height);
                } else {
                    gr.fillRoundRect(shape.x + dx, shape.y, shape.width, shape.height, shape.width / 4, shape.height / 4);
                }
                if ((shapeIndex + 1) % (NUM_SHAPES / 10) == 0) {
                    blur();
                }
            }
            for (int textIndex = 0; textIndex < texts.size(); textIndex++) {
                Text text = texts.get(textIndex);
                int dx = getPixelsMoved(time, text.z);
                gr.setColor(text.color);
                gr.setFont(text.font);
                gr.drawString(text.str, text.x + dx, text.y);
                if ((textIndex + 1) % (NUM_TEXTS / 5) == 0) {
                    blur();
                }
            }

            double titleZ = -2.0;
            int dx = getPixelsMoved(time, titleZ);
            double titleVel = SCR_VELOCITY * (1.0 - titleZ);
            int distanceMoved = (int) (titleVel * TOTAL_SECS);
            String titleText = "Code4Lyfe";
            Font titleFont = new Font("Te X Gyre Chorus", Font.BOLD | Font.ITALIC, height / 5);
            int titleWidth = (int)titleFont.getStringBounds(titleText, gr.getFontRenderContext()).getWidth();
            int titleX = (width - titleWidth) / 2 - distanceMoved;
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gr.setColor(Color.BLACK);
            gr.setFont(titleFont);
            gr.drawString(titleText, titleX + dx, height / 2);
            gr.setColor(Color.BLUE);
            gr.drawString(titleText, titleX + 2 + dx, height / 2 + 2);

            ImageIO.write(img, "png", new File("./out", fileName));
            time += dt;
            dt /= 1.01;
        }
    }

    private static int getPixelsMoved(double time, double z) {
        return (int) Math.round(time * SCR_VELOCITY * (1.0 - z));
    }

    private static void blur() {
        int kernelX = 3;
        int kernelY = 3;
        float[] matrix = new float[kernelX * kernelY];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = (float) (1.0 / matrix.length);
        }

        BufferedImageOp op = new ConvolveOp(new Kernel(kernelX, kernelY, matrix), ConvolveOp.EDGE_NO_OP, null);
        img = op.filter(img, null);
        gr = img.createGraphics();
    }
}
