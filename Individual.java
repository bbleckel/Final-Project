import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Individual {
    Triangle[] t;
    Vector<Point> points;
    int imgHeight;
    BufferedImage img;
//    BufferedImage img;
    public Individual(Triangle[] t, BufferedImage source) {
//        this.t = new Triangle[t.length];
        this.t = t;
        // imgHeight = source.getHeight();
        // points = Geom.getPointsInTriangle(t, imgHeight);

        
        // source will be a blank image for writing: make a copy
        img = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
//        img = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

        System.out.println("Type " + source.getType());
        Graphics2D srcG = img.createGraphics();
//        srcG.drawImage(source, 0, 0, null);

        // remove image (white background)
        srcG.setBackground(new Color(255, 255, 255, 0));
        srcG.clearRect(0, 0, source.getWidth(), source.getHeight());

        // set opacity
        srcG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // actually draw triangles to image
        for(int i = 0; i < t.length; i++) {
            Color c = new Color(t[i].color, t[i].color, t[i].color);
            srcG.setColor(c);
            srcG.fillPolygon(new int[] {t[i].a.X, t[i].b.X, t[i].c.X}, new int[] {t[i].a.Y, t[i].b.Y, t[i].c.Y}, 3);
        }
        srcG.dispose();
    }

    public void update() {
        
        Graphics2D srcG = img.createGraphics();
        // remove image (white background)
        srcG.setBackground(new Color(255, 255, 255, 0));
        srcG.clearRect(0, 0, img.getWidth(), img.getHeight());

        // set opacity
        srcG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));

        // redraw triangles to image
        for(int i = 0; i < t.length; i++) {
            Color c = new Color(t[i].color, t[i].color, t[i].color);
            srcG.setColor(c);
            srcG.fillPolygon(new int[] {t[i].a.X, t[i].b.X, t[i].c.X}, new int[] {t[i].a.Y, t[i].b.Y, t[i].c.Y}, 3);
        }
        srcG.dispose();
    }
}
