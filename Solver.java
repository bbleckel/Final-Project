import java.util.*;
import java.awt.*;

public class Solver {
    public static void main (String[] args) {
        
        // process command line arguments
        if (args.length != 1){
            //            System.out.println();
            //            System.out.println("java GARunner individuals selection elitism crossover pC pM generations disInterval");
            //            System.out.println("    individuals  = number of individuals in population (int)");
            //            System.out.println("    selection    = type of selection of breeding pool (string):");
            //            System.out.println("                     ts   = tournament selection - implies ts1");
            //            System.out.println("                            ts1 = same individual cannot compete against self");
            //            System.out.println("                            ts2 = same individual can compete against self");
            //            System.out.println("                     rs   = rank based selection");
            //            System.out.println("                     bs   = Boltzmann selection");
            //            System.out.println("    elitism      = use of elitism (string):");
            //            System.out.println("                     et   = use elitism");
            //            System.out.println("                     ef   = do not use elitism");
            //            System.out.println("    crossover    = crossover method (string):");
            //            System.out.println("                     1c   = 1-point crossover");
            //            System.out.println("                     2c   = 2-point crossover");
            //            System.out.println("                     uc   = uniform crossover");
            //            System.out.println("    pC           = crossover probability (double)");
            //            System.out.println("    pM           = mutation probability (double)");
            //            System.out.println("    generations  = max number of generations to run (int)");
            //            System.out.println("    disInterval  = show best interval (int)");
            //            System.out.println();
            //            System.exit(1); // prevent the program from continuing without the correct inputs
        } else {
            String fileName = args[0];
            System.out.println("fileName = " + fileName);
            FileReader obj = new FileReader(fileName);
            Color[][] pixels = obj.pixels;
            System.out.println("Found " + pixels.length * pixels[0].length + " pixels");

//            for(int i = 0; i < pixels.length; i++) {
//                for(int j = 0; j < pixels[i].length; j++) {
//                    System.out.println(pixels[i][j]);
//                }
//            }

        }
    } // end main
    
}
