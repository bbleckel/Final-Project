import java.util.*;
import java.awt.*;

public class Solver {
    static FileReader file;
    static Color[][] pixels;
    public static void main (String[] args) {

        // process command line arguments
        if (args.length == 3) {
            String fileName = args[0];
            int neighborhood = 3;
            int particles = Integer.parseInt(args[1]);
            int iterations = Integer.parseInt(args[2]);

            System.out.println("fileName = " + fileName);
            file = new FileReader(fileName);
            pixels = file.pixels;

            PSO alg = new PSO(neighborhood, particles, iterations, fileName);
            Vector<Double> results = alg.solvePSO();
        } else  if (args.length == 8) {
            String fileName = args[0];
            int individuals = Integer.parseInt(args[1]);
            int triangles = Integer.parseInt(args[2]);
            int selection = Integer.parseInt(args[3]);
            int crossover = Integer.parseInt(args[4]);
            double pC = Double.parseDouble(args[5]);
            double pM =Double.parseDouble(args[6]);
            int generations = Integer.parseInt(args[7]);

            System.out.println("fileName = " + fileName);
            file = new FileReader(fileName);
            pixels = file.pixels;

            GA alg = new GA(individuals, triangles, selection, crossover, pC, pM, generations, file.width, file.height, 0.1, 15, 0.2, 0);
            alg.solveGA();
        } else if (args.length != 8){
            System.out.println();

            System.out.println("For just running the GA:");
            System.out.println("java Solver fileName individuals triangles selection crossover pC pM generations");
            System.out.println("    fileName     = name of image file to re-create (string)");
            System.out.println("    individuals  = number of individuals in population (int)");
            System.out.println("    triangles    = number of triangles allotted to each individual (int)");
            System.out.println("    selection    = type of selection to use (int):");
            System.out.println("                     1     = tournament selection");
            System.out.println("                     2     = Boltzmann selection");
            System.out.println("    crossover    = crossover method (int):");
            System.out.println("                        1   = uniform crossover");
            System.out.println("    pC           = crossover probability (double)");
            System.out.println("    pM           = mutation probability (double)");
            System.out.println("    generations  = max number of generations to run (int)");
            System.out.println();

            System.out.println("For running the PSO on the GA:");
            System.out.println("java Solver fileName particles iterations");
            System.out.println("    fileName     = name of image file to re-create (string)");
            System.out.println("    particles    = number of particles in swarm (int)");
            System.out.println("    iterations   = number of iterations for the PSO to run (int)");
            System.exit(1); // prevent the program from continuing without the correct inputs
        }
    } // end main
}
