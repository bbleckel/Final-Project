import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Individual {
    Triangle t;
    BufferedImage img;
    public Individual(Triangle t, BufferedImage source) {
        this.t = t;
        
        // source will be a blank image for writing: make a copy
        img = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D srcG = img.createGraphics();
        srcG.drawImage(source, 0, 0, null);
        
        // remove image (white background)
        srcG.setBackground(new Color(255, 255, 255, 0));
        srcG.clearRect(0, 0, source.getWidth(), source.getHeight());
        
        // set opacity
        srcG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .75f));
        
        // actually draw triangle to image
        Color c = new Color(t.color, t.color, t.color);
        srcG.setColor(c);
        srcG.fillPolygon(new int[] {t.a.X, t.b.X, t.c.X}, new int[] {t.a.Y, t.b.Y, t.c.Y}, 3);
        srcG.dispose();
        
        
    }
}
