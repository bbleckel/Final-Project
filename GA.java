import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GA {
    // class variables
    int individuals;
    double pC;
    double pM;
    int selection;
    int crossover;
    int generations;
    
    public GA(int individuals, int selection, int crossover, double pC, double pM, int generations) {
        this.individuals = individuals;
        this.selection = selection;
        this.crossover = crossover;
        this.pC = pC;
        this.pM = pM;
        this.generations = generations;
    }
    public void addPolygon() {
	// for testing: add a polygon
	System.out.println("Adding line!");
	int point1X = 20;
	int point1Y = 20;
	int point2X = 20;
	int point2Y = 50;
	int point3X = 50;
	int point3Y = 50;
	BufferedImage b = Solver.file.image;
	Graphics2D g = b.createGraphics();
	g.setColor(Color.WHITE);
	BasicStroke stroke = new BasicStroke(30);
	g.setStroke(stroke);
	g.drawLine(point1X, point1Y, point2X, point2Y);
	g.dispose();
	try {
		ImageIO.write(b, "jpg", new File("./line.jpg"));
	} catch (Exception e) {
		System.out.println("Error writing to file!");
		System.exit(1);
	}

   }
}
