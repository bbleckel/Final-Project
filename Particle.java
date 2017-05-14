import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Particle {
    int IDCounter;

	Vector<Double> velocity;
	Vector<Double> position;
	// pBest stores the *position* of the best value achieved
	Vector<Double> pBest;
	// stores the actual value
	double pBestValue;

	// unique ID's allow isEqual
	int ID;

    public Particle(int dimension) {
        pBestValue = Integer.MAX_VALUE;
    	ID = IDCounter;
    	IDCounter++;

    	for (int i = 0; i < dimension; i++) {
    		// generate random value within specified range for each function
            // NEEDS TO BE CHANGED TO BE ADAPTED TO THIS PROBLEM

            double velRandom = ThreadLocalRandom.current().nextInt(15, 30);
    		// double velRandom = (rand() % (MAX_VEL_RAND_VALUE[function] - MIN_VEL_RAND_VALUE + 1)) + MIN_VEL_RAND_VALUE;

    		double total = 30 - 15;
    		double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
    		double posRandom = total * ratio + 15;

    		velocity.add(velRandom);
    		pBest.add(posRandom);
    		position.add(posRandom);
    	}
    }

    public boolean isEqual(Particle p) {
        return p.ID == ID;
    }
}
