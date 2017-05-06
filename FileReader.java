/*
 grayscale from https://www.youtube.com/watch?v=cq80Itgs5Lw
 */

import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
	    Graphics2D ga = newImg.createGraphics();
	    ga.drawImage(image, 0, 0, null);
	    ga.dispose();
            
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    Color c = new Color(image.getRGB(j, i));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int a = c.getAlpha();
                    
                    // convert to grayscale
                    int gr = (r + g + b) / 3;
                    Color gColor = new Color(gr, gr, gr, 10);
		    newImg.setRGB(j, i, gColor.getRGB());
		    Color newC = new Color(newImg.getRGB(j, i), true);
		    int alpha = newC.getAlpha();
	  	    System.out.println("newImg alpha is " + alpha);
                    grayScale.setRGB(j, i, gColor.getRGB());
                    
                    pixels[j][i] = gColor;
                }
            }
            
            // write to file
            ImageIO.write(newImg, "jpg", new File("./gray.jpg"));
            
        } catch (Exception e) {
            System.out.println("Error reading file!");
            System.exit(1);
        }
    }
}
