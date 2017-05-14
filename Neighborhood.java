import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Neighborhood {
    int IDCounter;

    Vector<Double> bestPos;
    Vector<Particle> neighborhood;

    double bestValue;
    int size;
    int ID;
    int function;
    int dimension;

	public Neighborhood(int function, int dimension) {
        //assign respective class variables
    	size = 0;
    	bestValue = Integer.MAX_VALUE;
    	this.function = function;
    	this.dimension = dimension;
    	ID = IDCounter;
    	IDCounter++;

    	//initilalize best positions to random values
    	for(int i = 0; i < dimension; i++) {
    		double total = 30 - 15;
    		double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
    		double posRandom = total * ratio + 15;
    		bestPos.add(posRandom);
    	}
    }

	public void add(Particle x) {
        neighborhood.add(x);
    	size++;
    }

	public void updateBest() {
        for (int i = 0; i < size; i++) {
    		if (neighborhood.get(i).pBestValue < bestValue) {
    			// if particle's value better than neighborhood best, replace
    			bestValue = neighborhood.get(i).pBestValue;

    			bestPos = neighborhood.get(i).pBest;
    		}
    	}
    }

	public void reset() {
        bestPos.clear();
    	neighborhood.clear();
    	// reset vector values
    	for(int i = 0; i < dimension; i++) {
    		double total = 30 - 15;
    		double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
    		double posRandom = total * ratio + 15;
    		bestPos.add(posRandom);
    	}

    	bestValue = Integer.MAX_VALUE;
    	size = 0;
    }
}
