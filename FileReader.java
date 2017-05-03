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
            
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    Color c = new Color(image.getRGB(j, i));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int a = c.getAlpha();
                    
                    // convert to grayscale
                    int gr = (r + g + b) / 3;
                    Color gColor = new Color(gr, gr, gr, a);

                    grayScale.setRGB(j, i, gColor.getRGB());
                    
                    pixels[j][i] = gColor;
                }
            }
            
            // write to file
            ImageIO.write(grayScale, "jpg", new File("./gray.jpg"));
            
        } catch (Exception e) {
            System.out.println("Error reading file!");
            System.exit(1);
        }
    }
}
