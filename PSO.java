import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PSO {
    // need to be able to access this from the solver, thus not private
	double gBestValue;

	private int swarmSize;
	private int iterations;
	private int dimension;
	// position of all-time best particle?
	private Vector<Double> gBest;
	private Vector<Neighborhood> neighborhoodList;

	private final static double constrict = 0.7298;
	private double nBest;
	private int neighborhood;
	private Vector<Particle> swarm;

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

    private double PHI_1 = 2.05;
    private double PHI_2 = 2.05;

	private static final int selection = 2;
	private static final int crossover = 1;
	private static final int generations = 50;
	private static String fileName;
	private static FileReader file;

    public PSO(int neighborhood, int swarmSize, int iterations, String fileName) {
        this.neighborhood = neighborhood;
    	this.swarmSize = swarmSize;
    	this.iterations = iterations;
    	this.dimension = 7;
		this.fileName = fileName;
		file = new FileReader(fileName);
    	gBestValue = Integer.MAX_VALUE;

		gBest = new Vector<Double>(0);
		swarm = new Vector<Particle>(0);
		neighborhoodList = new Vector<Neighborhood>(0);

        //Individuals
        double total = individualInitList[1] - individualInitList[0];
        double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        double posRandom = total * ratio + individualInitList[0];
        gBest.add(posRandom);
        //Triangles
        total = triangleInitList[1] - triangleInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + triangleInitList[0];
        gBest.add(posRandom);
        //Probability of Crossover
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
        gBest.add(posRandom);
        //Probability of Mutation
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
        gBest.add(posRandom);
        //Alpha Mutation Amount
        posRandom = ThreadLocalRandom.current().nextDouble(0, 1);
        gBest.add(posRandom);
        //Color Mutation Amount
        total = colorInitList[1] - colorInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + colorInitList[0];
        gBest.add(posRandom);
        //Point (x,y) Mutation Amount
        total = pointInitList[1] - pointInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + pointInitList[0];
        gBest.add(posRandom);
    }

	/* veloctiy and position updates */
	public void updateVelocity(int index) {
        Particle p = swarm.get(index);
    	// iterate through dimensions, updating respective velocities
    	for(int i = 0; i < dimension; i++) {
    		// attraction from personal best
            double pAttract = ThreadLocalRandom.current().nextDouble(0, 1) * PHI_1 * (swarm.get(index).pBest.get(i) - swarm.get(index).position.get(i));

    		// get this particle's neighborhood best
    		Vector<Double> partBestPosition = neighborhoodList.get(index).bestPos;
    		// attraction from neighborhood best
    		double nAttract = ThreadLocalRandom.current().nextDouble(0, 1) * PHI_2 * (partBestPosition.get(i) - swarm.get(index).position.get(i));
    		double velChange = pAttract + nAttract;
    		swarm.get(index).velocity.set(i, swarm.get(index).velocity.get(i) + velChange);
            swarm.get(index).velocity.set(i, swarm.get(index).velocity.get(i) * constrict);
    	}
    }

	public void updatePosition(int index) {
        // iterate through dimensions, updating respective positions based on velocity
    	for(int i = 0; i < dimension; i++) {
    		swarm.get(index).position.set(i, swarm.get(index).position.get(i) + swarm.get(index).velocity.get(i));
    	}
    }

	/* neighborhoods */
	public void initializeNeighborhoods() {
		initializeRandomNeighborhood();
    }
	// public void global();
	// public void ring();
	// public void vonNeumann();
	public void initializeRandomNeighborhood() {
        int k = 5;

        for (int i = 0; i < swarmSize; i++) {
            Neighborhood temp = new Neighborhood(dimension);
            // particle is in its own neighborhood
            temp.add(swarm.get(i));
            for (int j = 0; j < k - 1; j++) {
                // get non-duplicate index
                int randIndex = getNewRandIndex(temp);
                // add the index that is not a duplicate
                temp.add(swarm.get(randIndex));
            }
            neighborhoodList.add(temp);
        }
    }

	public void updateRandomNeighborhood() {
        int k = 5;
    	double minProb = 0.2;

    	for (int i = 0; i < swarmSize; i++) {
    		double probability = ThreadLocalRandom.current().nextDouble(0, 1);
    		if (probability < minProb) {
    			// clear neighborhood
    			neighborhoodList.get(i).reset();
    			neighborhoodList.get(i).add(swarm.get(i));
    			for (int j = 0; j < k - 1; j++) {
    				// get non-duplicate index
    				int randIndex = getNewRandIndex(neighborhoodList.get(i));
    				// add the index that isn't a duplicate to the neighborhood
    				neighborhoodList.get(i).add(swarm.get(randIndex));
    			}
    		}
    	}
    }

	public int getNewRandIndex(Neighborhood h) {
        int randIndex = ThreadLocalRandom.current().nextInt(0, swarmSize);

    	// check to make sure that this index isn't a duplicate
    	boolean indexAlreadySelected = false;
    	while (!indexAlreadySelected) {
    		indexAlreadySelected = true;
    		// iterate through particle swarm[i]'s neighborhood
    		for (int n = 0; n < h.neighborhood.size(); n++) {
    			Particle randPart = swarm.get(randIndex);
    			if (h.neighborhood.get(n).isEqual(randPart)){
    				// if you've added the index already
    				// break, get a new number, and repeat
    				indexAlreadySelected = false;
    				break;
    			}
    		}
    		if (indexAlreadySelected == false) {
    			randIndex = ThreadLocalRandom.current().nextInt(0, swarmSize);
    		}
    	}

    	return randIndex;
    }

	/* function evaluation */
	public void eval(int index) {
        //save the particle that we're looking at
    	Particle p = swarm.get(index);
    	double pVal = 200;

        /******************************************************/
        /* CALL GA HERE AND RETURN VALUE THAT YOU GET TO PVAL */
        /******************************************************/
		// individuals
        // triangles
        // pC
        // pM
        // alpha mutate amount
        // color mutate amount
        // point (x,y) mutate amount
		int individuals = p.position.get(0).intValue();
		int triangles = p.position.get(1).intValue();
		double pC = p.position.get(2);
		double pM = p.position.get(3);
		double alpha = p.position.get(4);
		int color =  p.position.get(5).intValue();
		double point = p.position.get(6);
        GA alg = new GA(individuals, triangles, selection, crossover, pC, pM, generations, file.width, file.height, alpha, color, point);

		pVal = 1 - alg.solveGA();

    	//update the personal best value if necessary
    	if (pVal < p.pBestValue) {
    		swarm.get(index).pBest = p.position;
    		swarm.get(index).pBestValue = pVal;
    	}
    	//update the best overall best if necessary
    	if(pVal < gBestValue) {
    		gBestValue = pVal;
    		gBest = p.pBest;
    	}
    	//update all neighborhood bests
    	updateNeighborhoodBest();
    }

	public void updateNeighborhoodBest(){
        // iterate through neighborhoods, updating bests
        for(int i = 0; i < swarmSize; i++) {
            // update each particle's neighborhood
            neighborhoodList.get(i).updateBest();
        }
    }

	/* initialization */
	public void initializeSwarm() {
        // create swarm of 'swarmSize' particles
    	for (int i = 0; i < swarmSize; i++) {
    		Particle newParticle = new Particle(dimension);
    		swarm.add(newParticle);
    	}
    }

	/* general algorithm controller */
	public Vector<Double> solvePSO() {
		System.out.println("Solving PSO...");
    	Vector<Double> vect = new Vector<Double>();

    	initializeSwarm();
    	initializeNeighborhoods();

    	int iterRemaining = iterations;

    	while(iterRemaining >= 0) {

    		// iterate through particles, updating velocity & position
    		for(int i = 0; i < swarmSize; i++) {
    			updateVelocity(i);
    			updatePosition(i);
    			// evaluate at position so later particles benefit from moves before
    			eval(i);
    		}

    		// if (neighborhood == RANDOM_NEIGHBORHOOD_INT) {
    			updateRandomNeighborhood();
    		// }
    		// when testing a single input, use this
    		// cout << "gBest = " << gBestValue << endl;
			System.out.println("gBest = :" + (1 - gBestValue));
    		if (iterRemaining != 10000) {
    			if (iterRemaining % 1000 == 0 || iterRemaining == 9500) {
    				vect.add(gBestValue);
    			}
    		}

    		iterRemaining--;
    	}

    	// when running the test cases, use this
    	//cout << "gBest = " << gBestValue << endl;

    	return vect;
    }
}
