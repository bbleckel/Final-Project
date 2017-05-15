import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Individual {
    Triangle[] t;
    BufferedImage img;
    double fitness;
//    BufferedImage img;
    public Individual(Triangle[] t, BufferedImage source, double fitness) {
        this.t = t;
        this.fitness = fitness;

        // source will be a blank image for writing: make a copy
        img = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);

// http://docs.oracle.com/javase/7/docs/api/constant-values.html#java.awt.image.BufferedImage.TYPE_INT_ARGB
        // for what each type int means

        Graphics2D srcG = img.createGraphics();

        // remove image (white background)
        srcG.setBackground(new Color(255, 255, 255, 0));
        srcG.clearRect(0, 0, source.getWidth(), source.getHeight());


        // actually draw triangles to image
        for(int i = 0; i < t.length; i++) {
            // set opacity
            srcG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t[i].alpha));

            Color c = new Color(t[i].color[0], t[i].color[1], t[i].color[2]);
            srcG.setColor(c);
            srcG.fillPolygon(new int[] {t[i].a.X, t[i].b.X, t[i].c.X}, new int[] {t[i].a.Y, t[i].b.Y, t[i].c.Y}, 3);
        }
        srcG.dispose();
    }

    public BufferedImage rescale(int size) {
        BufferedImage rescaled = new BufferedImage(img.getWidth()*size, img.getHeight()*size, BufferedImage.TYPE_INT_RGB);

        Graphics2D srcG = rescaled.createGraphics();

        srcG.setBackground(new Color(255, 255, 255, 0));
        srcG.clearRect(0, 0, img.getWidth()*size, img.getHeight()*size);

        // redraw triangles to image
        for(int i = 0; i < t.length; i++) {
            // set opacity
            srcG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t[i].alpha));

            Color c = new Color(t[i].color[0], t[i].color[1], t[i].color[2]);
            srcG.setColor(c);
            srcG.fillPolygon(new int[] {t[i].a.X*size, t[i].b.X*size, t[i].c.X*size}, new int[] {t[i].a.Y*size, t[i].b.Y*size, t[i].c.Y*size}, 3);
        }
        srcG.dispose();

        return rescaled;
    }

    public void update() {

        Graphics2D srcG = img.createGraphics();
        // remove image (white background)
        srcG.setBackground(new Color(255, 255, 255, 0));
        srcG.clearRect(0, 0, img.getWidth(), img.getHeight());

        // redraw triangles to image
        for(int i = 0; i < t.length; i++) {
            // set opacity
            srcG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t[i].alpha));

            Color c = new Color(t[i].color[0], t[i].color[1], t[i].color[2]);
            srcG.setColor(c);
            srcG.fillPolygon(new int[] {t[i].a.X, t[i].b.X, t[i].c.X}, new int[] {t[i].a.Y, t[i].b.Y, t[i].c.Y}, 3);
        }
        srcG.dispose();
    }
}
