import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Neighborhood {
    //static, thus accessible to all instances of the class
    static int IDCounter;

    Vector<Double> bestPos;
    Vector<Particle> neighborhood;

    double bestValue;
    int size;
    int ID;
    int dimension;

    /****   Dimension order:
                individuals
                triangles
                pC
                pM
                alpha mutate amount
                color mutate amount
                point (x,y) mutate amount
    */
    /****   Initial value ranges:
                individuals: 2-10
                triangles: 4-100
                pC: 0-1 (double)
                pM: 0-1 (double)
                alpha mutate amount: 0-1
                color mutate amount: 1-20
                point (x,y) mutate amount: 10%-20% of image siz (width, height)
    */
    public int[] individualInitList = {2, 10};
    public int[] triangleInitList = {4, 100};
    public double[] pCInitList = {0.0, 1.0};
    public double[] pMInitList = {0.0, 1.0};
    public double[] alphaInitList = {0.0, 1.0};
    public int[] colorInitList = {1, 20};
    public double[] pointInitList = {0.1, 0.2};

	public Neighborhood(int dimension) {
        //assign respective class variables
    	size = 0;
    	bestValue = Integer.MAX_VALUE;
    	this.dimension = dimension;
    	ID = IDCounter;
    	IDCounter++;

        bestPos = new Vector<Double>(0);
        neighborhood = new Vector<Particle>(0);

        //Individuals
        double total = individualInitList[1] - individualInitList[0];
        double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        double posRandom = total * ratio + individualInitList[0];
        bestPos.add(posRandom);
        //Triangles
        total = triangleInitList[1] - triangleInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + triangleInitList[0];
        bestPos.add(posRandom);
        //Probability of Crossover
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
        bestPos.add(posRandom);
        //Probability of Mutation
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
        bestPos.add(posRandom);
        //Alpha Mutation Amount
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
        bestPos.add(posRandom);
        //Color Mutation Amount
        total = colorInitList[1] - colorInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + colorInitList[0];
        bestPos.add(posRandom);
        //Point (x,y) Mutation Amount
        total = pointInitList[1] - pointInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + pointInitList[0];
        bestPos.add(posRandom);
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
