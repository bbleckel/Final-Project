import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class GA {
    // GA variables
    int individuals;
    double pC;
    double pM;
    int selection;
    int crossover;
    int generations;
    
    private final int MUT_AMNT = 2;
    
    // Image variables
    int imageWidth;
    int imageHeight;
    Individual[] population;
    Individual[] breedingPool;
    double[] fitnessList;
    Individual[] unselected;
    
    public GA(int individuals, int selection, int crossover, double pC, double pM, int generations, int width, int height) {
        this.individuals = individuals;
        this.selection = selection;
        this.crossover = crossover;
        this.pC = pC;
        this.pM = pM;
        this.generations = generations;
        
        imageWidth = width;
        imageHeight = height;
        population = new Individual[individuals];
        fitnessList = new double[individuals];
        breedingPool = new Individual[individuals];
    }
    
    public Triangle getRandomTriangle() {
        // returns a triangle with random coordinates and color
        
        int randomWidth = ThreadLocalRandom.current().nextInt(0, imageWidth + 1);
        int randomHeight = ThreadLocalRandom.current().nextInt(0, imageHeight + 1);
        Point a = new Point(randomWidth, randomHeight);
        
        randomWidth = ThreadLocalRandom.current().nextInt(0, imageWidth + 1);
        randomHeight = ThreadLocalRandom.current().nextInt(0, imageHeight + 1);
        Point b = new Point(randomWidth, randomHeight);
        
        randomWidth = ThreadLocalRandom.current().nextInt(0, imageWidth + 1);
        randomHeight = ThreadLocalRandom.current().nextInt(0, imageHeight + 1);
        Point c = new Point(randomWidth, randomHeight);
        
        int color = ThreadLocalRandom.current().nextInt(0, 255 + 1);
        return new Triangle(a, b, c, color);
    }
    
    public void initPopulation() {
        for(int i = 0; i < individuals; i++) {
            Triangle t = getRandomTriangle();
//            Point a = new Point(50, 50);
//            Point b = new Point(275, 50);
//            Point c =  new Point(125, 275);
//            Triangle t = new Triangle(a, b, c, 200);
            population[i] = new Individual(t, Solver.file.blank);
//            double fitness = fitness(population[i]);
//            System.out.println("Fitness is " + fitness);
            
//            try {
//                ImageIO.write(population[i].img, "jpg", new File("./ind" + i + ".jpg"));
//            } catch (Exception e) {
//                System.out.println("Error writing fileeee");
//                System.exit(1);
//            }
            
        }
    }
    
    public void drawPopulation() {
        Graphics2D srcG = Solver.file.blank.createGraphics();
        // remove image (white background)
        srcG.setBackground(new Color(255, 255, 255, 0));
        srcG.clearRect(0, 0, imageWidth, imageHeight);

        for(int i = 0; i < individuals; i++) {
            addTriangle(population[i].t, Solver.file.blank);
        }
        
        try {
            ImageIO.write(Solver.file.blank, "jpg", new File("./triangles.jpg"));
            //            ImageIO.write(blank, "jpg", new File("./triangles2.jpg"));
        } catch (Exception e) {
            System.out.println("Error writing to file!");
            System.exit(1);
        }
    }
    
    public void addTriangle(Triangle t, BufferedImage img) {
        // for testing: add a triangle
        
        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
        
        Color c = new Color(t.color, t.color, t.color);
        
        g.setColor(c);
        
        g.fillPolygon(new int[] {t.a.X, t.b.X, t.c.X}, new int[] {t.a.Y, t.b.Y, t.c.Y}, 3);
        g.dispose();
        
    }
    
    public void printPopulation() {
        for(int i = 0; i < individuals; i++) {
            population[i].t.printSelf();
        }
    }
    
    public void uniformCross() {
        
        for(int i = 0; i < individuals; i++) {
            double rand = ThreadLocalRandom.current().nextDouble(0, 2);
            
            Individual parent1 = breedingPool[i];
            Individual parent2;
            if(rand < pC) {
                if(i + 1 >= individuals) {
                    parent2 = breedingPool[0];
                } else {
                    parent2 = breedingPool[i + 1];
                }
                
                // take points and color from parents randomly
                Point a;
                Point b;
                Point c;
                int color;
                
                double prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < 0.5) {
                    a = parent1.t.a;
                } else {
                    a = parent2.t.a;
                }
                
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < 0.5) {
                    
                    b = parent1.t.b;
                } else {
                    b = parent2.t.b;
                }
                
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < 0.5) {
                    c = parent1.t.c;
                } else {
                    c = parent2.t.c;
                }
                
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < 0.5) {
                    color = parent1.t.color;
                } else {
                    color = parent2.t.color;
                }
                
                Triangle t = new Triangle(a, b, c, color);
                Individual offspring = new Individual(t, Solver.file.blank);
                population[i] = offspring;
                
            } else {
                // if crossover does not occur, choose parent1
                population[i] = parent1;
            }
        }
    }
    
    public void tournamentSelect() {
        int randNum;
        for(int i = 0; i < individuals; i++) {
            int rand1 = (int) ThreadLocalRandom.current().nextInt(0, individuals);
            int rand2 = (int) ThreadLocalRandom.current().nextInt(0, individuals);
            Individual ind1 = population[rand1];
            Individual ind2 = population[rand2];
            double fit1 = fitnessList[rand1];
            double fit2 = fitnessList[rand2];
            
            
//            if(fit1 > fit2) {
//                breedingPool[i] = ind1;
//            } else {
//                breedingPool[i] = ind2;
//            }
            breedingPool = population.clone();
            
        }
        
    }
    
    public Point mutate(Point p) {
        double prob = ThreadLocalRandom.current().nextDouble(0, 1);
        
        // direction determines if the point is translated up or down
        double dir = ThreadLocalRandom.current().nextDouble(0, 1);
        int direction = 0;
        if(dir < 0.5) {
            direction = 1;
        } else {
            direction = -1;
        }
        // choose between varying just X, just Y, or both
        if(prob > 0.66) {
            // shift X
            
            // make sure mutation doesn't go out of range
            if(p.X + direction * MUT_AMNT >= imageWidth || p.X + direction * MUT_AMNT < 0) {
                // if out of range, flip the direction
                p.X += -direction * MUT_AMNT;
            } else {
                p.X += direction * MUT_AMNT;
            }
        } else if(prob > 0.33) {
            // shift Y
            if(p.Y + direction * MUT_AMNT >= imageHeight || p.Y + direction * MUT_AMNT < 0) {
                p.Y += -direction * MUT_AMNT;
            } else {
                p.Y += direction * MUT_AMNT;
            }
        } else {
            // shift both
            if(p.X + direction * MUT_AMNT >= imageWidth || p.X + direction * MUT_AMNT < 0) {
                p.X += -direction * MUT_AMNT;
            } else {
                p.X += direction * MUT_AMNT;
            }
            
            if(p.Y + direction * MUT_AMNT >= imageHeight || p.Y + direction * MUT_AMNT < 0) {
                p.Y += -direction * MUT_AMNT;
            } else {
                p.Y += direction * MUT_AMNT;
            }
        }
        
        return p;
    }
    
    public void mutatePopulation() {
        int mutRand;
        for(int i = 0; i < individuals; i++) {
            // with some probability, mutate points/color
            boolean mutated = false;
            // mutate a?
            double prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                mutated = true;
                population[i].t.a = mutate(population[i].t.a);
            }
            
            // mutate b?
            prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                mutated = true;
                population[i].t.b = mutate(population[i].t.b);
            }
            
            // mutate c?
            prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                mutated = true;
                population[i].t.c = mutate(population[i].t.c);
            }
            
            // mutate color?
            prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                mutated = true;
                int color = population[i].t.color;
                double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                // choose direction
                int direction = 0;
                if(dir < 0.5) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                // check out of bounds
                if(color + direction * MUT_AMNT > 255 || color + direction * MUT_AMNT < 0) {
                    color += -direction * MUT_AMNT;
                } else {
                    color += direction * MUT_AMNT;
                }
                population[i].t.color = color;
            }
            
            if(mutated) {
                population[i].update();
            }
        }
    }
    
    public int getMaxFitness() {
        double max = 0;
        int maxIndex = 0;
        for(int i = 0; i < individuals; i++) {
            if(fitnessList[i] > max) {
                max = fitnessList[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    public double fitness(Individual ind) {
        // careful not to bias towards giant triangles that just cover screen to get as much coverage as possible
        
        double sum = 0;
        // evaluate fitness of a single individual
        for(int i = 0; i < ind.img.getHeight(); i++) {
            for(int j = 0; j < ind.img.getWidth(); j++) {
                // pixel by pixel comparison of images
                Color indColor = new Color(ind.img.getRGB(j, i));
                int indRed = indColor.getRed();
                int sourceRed = Solver.pixels[j][i].getRed();
                
                /* note that both the individual and the source image will be in grayScale,
                 so comparing just the red values is sufficient (as would be just comparing
                 any color)
                 */
                double diff = Math.abs(indRed - sourceRed);
                
                sum += diff;
            }
        }
        
        // normalize?
        return sum / (imageWidth * imageHeight);
//        return sum;
    }
    
    public void evalFitness() {
        for(int i = 0; i < individuals; i++) {
            fitnessList[i] = fitness(population[i]);
//            System.out.println("Fitness of " + i + " is " + fitnessList[i]);
//            try {
//                ImageIO.write(population[i].img, "jpg", new File("./ind" + i + ".jpg"));
//            } catch (Exception e) {
//                System.out.println("Error writing fileeee");
//                System.exit(1);
//            }
            
        }
    }
    
    public void solveGA() {
        double bestValue = -1;
        int genFound = -1;
        initPopulation();

        for(int g = 0; g < generations; g++) {
            evalFitness();
            tournamentSelect();
            uniformCross();
            mutatePopulation();
//            evalFitness();
            
            int bestFitness = getMaxFitness();
            if(fitnessList[bestFitness] > bestValue) {
                // not really relevant to keep a 'best' individual -- just a triangle
                genFound = g + 1;
                bestValue = fitnessList[bestFitness];
            }
            
//            if((generations - g) % (generations / 10) == 0) {
                // print currnt solution each 20th of total generations
                System.out.println("(Generation " + g + ")");
            drawPopulation();

//            }
        }
    }
}
