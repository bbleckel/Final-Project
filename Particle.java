import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Particle {
    //static, thus accessible to all instances of the class
    static int IDCounter;

	Vector<Double> velocity;
	Vector<Double> position;
	// pBest stores the *position* of the best value achieved
	Vector<Double> pBest;
	// stores the actual value
	double pBestValue;

	// unique ID's allow isEqual
	int ID;

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
                point (x,y) mutate amount: 10%-20% of image size (width, height)
    */
    public int[] individualInitList = {2, 10};
    public int[] triangleInitList = {4, 100};
    public double[] pCInitList = {0.0, 1.0};
    public double[] pMInitList = {0.0, 1.0};
    public double[] alphaInitList = {0.0, 1.0};
    public int[] colorInitList = {1, 20};
    public double[] pointInitList = {0.1, 0.2};

    private int[] MAX_VEL_RAND_VALUE = {2, 4, 4};
    private int MIN_VEL_RAND_VALUE = -2;

    public Particle(int dimension) {
        pBestValue = Integer.MAX_VALUE;
    	ID = IDCounter;
    	IDCounter++;

        velocity = new Vector<Double>(0);
    	position = new Vector<Double>(0);

    	pBest = new Vector<Double>(0);

    	for (int i = 0; i < dimension; i++) {
    		// generate random velocity values
            double velRandom = ThreadLocalRandom.current().nextDouble(MIN_VEL_RAND_VALUE, 0.5);
    		velocity.add(velRandom);
    	}

        //Individuals
        double total = individualInitList[1] - individualInitList[0];
        double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        double posRandom = total * ratio + individualInitList[0];
		pBest.add(posRandom);
        position.add(posRandom);
        //Triangles
        total = triangleInitList[1] - triangleInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + triangleInitList[0];
		pBest.add(posRandom);
        position.add(posRandom);
        //Probability of Crossover
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
		pBest.add(posRandom);
        position.add(posRandom);
        //Probability of Mutation
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
		pBest.add(posRandom);
        position.add(posRandom);
        //Alpha Mutation Amount
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
		pBest.add(posRandom);
        position.add(posRandom);
        //Color Mutation Amount
        total = colorInitList[1] - colorInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + colorInitList[0];
		pBest.add(posRandom);
        position.add(posRandom);
        //Point (x,y) Mutation Amount
        total = pointInitList[1] - pointInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + pointInitList[0];
		pBest.add(posRandom);
        position.add(posRandom);

    }

    public boolean isEqual(Particle p) {
        return p.ID == ID;
    }
}
