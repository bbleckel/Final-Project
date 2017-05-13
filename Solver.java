import java.util.*;
import java.awt.*;

public class Solver {
    static FileReader file;
    static Color[][] pixels;
    public static void main (String[] args) {

        // process command line arguments
        if (args.length != 8){
            System.out.println();

            System.out.println("java Solver fileName individuals triangles selection crossover pC pM generations");
            System.out.println("    fileName     = name of image file to re-create (string)");
            System.out.println("    individuals  = number of individuals in population (int)");
            System.out.println("    triangles    = number of triangles allotted to each individual (int)");
            System.out.println("    selection    = type of selection to use (int):");
            System.out.println("                     1     = tournament selection");
//            System.out.println("                            ts1 = same individual cannot compete against self");
//            System.out.println("                            ts2 = same individual can compete against self");
//            System.out.println("                     rs   = rank based selection");
//            System.out.println("                     bs   = Boltzmann selection");
//            System.out.println("    elitism      = use of elitism (string):");
//            System.out.println("                     et   = use elitism");
//            System.out.println("                     ef   = do not use elitism");
            System.out.println("    crossover    = crossover method (int):");
//            System.out.println("                     1c   = 1-point crossover");
//            System.out.println("                     2c   = 2-point crossover");
            System.out.println("                        1   = uniform crossover");
            System.out.println("    pC           = crossover probability (double)");
            System.out.println("    pM           = mutation probability (double)");
            System.out.println("    generations  = max number of generations to run (int)");
//            System.out.println("    disInterval  = show best interval (int)");
            System.out.println();
            System.exit(1); // prevent the program from continuing without the correct inputs
        } else {
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
            
            System.out.println("Solving with:\n" + individuals + " individuals\n" + triangles + " triangles\n" + pC + " pC\n" + pM + " pM\n" + generations + " generations\n");

            GA alg = new GA(individuals, triangles, selection, crossover, pC, pM, generations, file.width, file.height);
            alg.solveGA();


//            System.out.println("Found " + pixels.length * pixels[0].length + " pixels");

            //            for(int i = 0; i < pixels.length; i++) {
            //                for(int j = 0; j < pixels[i].length; j++) {
            //                    System.out.println(pixels[i][j]);
            //                }
            //            }

        }
    } // end main
}
