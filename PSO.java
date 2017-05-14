import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PSO {
    // need to be able to access this from the tester
	double gBestValue;

	private int swarmSize;
	private int iterations;
	private int dimension;
	// position of all-time best particle?
	private Vector<Double> gBest;
	private Vector<Neighborhood> neighborhoodList;

	private double constrict;
	private double nBest;
	private int neighborhood;
	private int function;
	private Vector<Particle> swarm;

    public PSO(int neighborhood, int swarmSize, int iterations, int function, int dimension) {
        this.neighborhood = neighborhood;
    	this.swarmSize = swarmSize;
    	this.iterations = iterations;
    	this.function = function;
    	this.dimension = dimension;
    	constrict = 0.7298;
    	gBestValue = Integer.MAX_VALUE;

    	for(int i = 0; i < dimension; i++) {
    		// fill gBest with random viable values
    		double total = 30 - 15;
    		double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
    		double posRandom = total * ratio + 15;
    		gBest.add(posRandom);
    	}
    }

	/* veloctiy and position updates */
	public void updateVelocity(int index) {
        Particle p = swarm[index];
    	// iterate through dimensions, updating respective velocities
    	for(int i = 0; i < dimension; i++) {
    		// attraction from personal best
            double pAttract = ThreadLocalRandom.current().nextDouble(0, 1) * PHI_1 * (swarm[index].pBest[i] - swarm[index].position[i]);

    		// get this particle's neighborhood best
    		Vector<double> partBestPosition = neighborhoodList[index].bestPos;
    		// attraction from neighborhood best
    		double nAttract = ThreadLocalRandom.current().nextDouble(0, 1) * PHI_2 * (partBestPosition[i] - swarm[index].position[i]);
    		double velChange = pAttract + nAttract;
    		swarm[index].velocity[i] += velChange;
    		swarm[index].velocity[i] *= constrict;
    	}
    }

	public void updatePosition(int index) {
        // iterate through dimensions, updating respective positions based on velocity
    	for(int i = 0; i < dimension; i++) {
    		swarm[index].position[i] += swarm[index].velocity[i];
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
            Neighborhood temp(function, dimension);
            // particle is in its own neighborhood
            temp.add(swarm[i]);
            for (int j = 0; j < k - 1; j++) {
                // get non-duplicate index
                int randIndex = getNewRandIndex(temp);
                // add the index that is not a duplicate
                temp.add(swarm[randIndex]);
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
    			neighborhoodList[i].reset();
    			neighborhoodList[i].add(swarm[i]);
    			for (int j = 0; j < k - 1; j++) {
    				// get non-duplicate index
    				int randIndex = getNewRandIndex(neighborhoodList[i]);
    				// add the index that isn't a duplicate to the neighborhood
    				neighborhoodList[i].add(swarm[randIndex]);
    			}
    		}
    	}
    }

	public int getNewRandIndex(Neighborhood h) {
        int randIndex = rand() % swarmSize;

    	// check to make sure that this index isn't a duplicate
    	boolean indexAlreadySelected = false;
    	while (!indexAlreadySelected) {
    		indexAlreadySelected = true;
    		// iterate through particle swarm[i]'s neighborhood
    		for (int n = 0; n < h.neighborhood.size(); n++) {
    			Particle randPart = swarm[randIndex];
    			if (h.neighborhood[n]->isEqual(randPart)){
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
    	Particle p = swarm[index];
    	double pVal = 200;

        /******************************************************/
        /* CALL GA HERE AND RETURN VALUE THAT YOU GET TO PVAL */
        /******************************************************/

    	//update the personal best value if necessary
    	if (pVal < p.pBestValue) {
    		swarm[index].pBest = p.position;
    		swarm[index].pBestValue = pVal;
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
            neighborhoodList[i].updateBest();
        }
    }
	public void printNeighborhoods() {
    	for(int i = 0; i < neighborhoodList.size(); i++) {
    		neighborhoodList[i].printNeighborhood();
    	}
    }

	/* initialization */
	public void initializeSwarm() {
        // create swarm of 'swarmSize' particles
    	for (int i = 0; i < swarmSize; i++) {
    		Particle newParticle = Particle(dimension, function);
    		swarm.add(newParticle);
    	}
    }

	/* general algorithm controller */
	public Vector<double> solvePSO() {
        srand(time(NULL));

    	Vector<double> vect;

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
