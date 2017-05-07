/*
 grayscale from https://www.youtube.com/watch?v=cq80Itgs5Lw
 */

import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class FileReader {
    BufferedImage image;
    int width;
    int height;
    Color[][] pixels;
    
    public FileReader(String fileName) {
        try {
            File input = new File(fileName);
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new Color[width][height];
            
            BufferedImage grayScale = image;

            BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//            // copy to a BufferedImage that can take alpha values
//            Graphics2D ga = newImg.createGraphics();
//            ga.drawImage(image, 0, 0, null);
//            ga.dispose();
            
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    Color c = new Color(image.getRGB(j, i));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int a = c.getAlpha();
                    
//                    System.out.println("Alpha is " + a);

                    // convert to grayscale
                    int gr = (r + g + b) / 3;
                    Color gColor = new Color(gr, gr, gr, 10);
                    newImg.setRGB(j, i, gColor.getRGB());
                    Color newC = new Color(newImg.getRGB(j, i), true);
                    grayScale.setRGB(j, i, gColor.getRGB());
                    bi.setRGB(j, i, gColor.getRGB());
//                    System.out.println("Set to " + gColor.getAlpha());
                    
                    pixels[j][i] = gColor;
                }
            }
            
//            for(int i = 50; i < 100; i++) {
//                for(int j = 50; j < 100; j++) {
//                    Color d = new Color(1f, 0f, 0f, .1f);
//                    int rgb = d.getRGB();
//                    int alpha = (rgb >> 24) & 0xff;
//                    System.out.println(d.getRGB() + ", " + alpha);
//                    System.out.println("d has alpha " + d.getAlpha());
////                    grayScale.setRGB(j, i, d.getRGB());
//                    bi.setRGB(j, i, d.getRGB());
//                    Color c = new Color(bi.getRGB(j, i), true);
//                    int r = c.getRed();
//                    int g = c.getGreen();
//                    int b = c.getBlue();
//                    int a = c.getAlpha();
//                    System.out.println("New has r " + r + ", g " + g + ", b " + b + ", a " + a);
//                }
//            }
            
            // write to file
//            ImageIO.write(grayScale, "jpg", new File("./gray2.jpg"));
            ImageIO.write(newImg, "jpg", new File("./gray.jpg"));
            
        } catch (Exception e) {
            System.out.println("Error reading file!");
            System.exit(1);
        }
    }
}
