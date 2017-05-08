import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class GA {
    // GA variables
    int individuals;
    int triangles;
    double pC;
    double pM;
    int selection;
    int crossover;
    int generations;

    private final int MUT_AMNT = 20;

    // Image variables
    int imageWidth;
    int imageHeight;
    Individual[] population;
    Individual[] breedingPool;
    double[] fitnessList;
    BufferedImage bestImg;
    boolean drawIndividuals;

    public GA(int individuals, int triangles, int selection, int crossover, double pC, double pM, int generations, int width, int height) {
        this.individuals = individuals;
        this.triangles = triangles;
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

        bestImg = Solver.file.blank;
        
        // change this to see (or not see) each individual drawn on their own canvas
        drawIndividuals = false;
    }

    public Triangle getRandomTriangle() {
        // returns a triangle with random coordinates and color

        int randomWidth = ThreadLocalRandom.current().nextInt(0, imageWidth);
        int randomHeight = ThreadLocalRandom.current().nextInt(0, imageHeight);
        Point a = new Point(randomWidth, randomHeight);

        randomWidth = ThreadLocalRandom.current().nextInt(0, imageWidth);
        randomHeight = ThreadLocalRandom.current().nextInt(0, imageHeight);
        Point b = new Point(randomWidth, randomHeight);

        randomWidth = ThreadLocalRandom.current().nextInt(0, imageWidth);
        randomHeight = ThreadLocalRandom.current().nextInt(0, imageHeight);
        Point c = new Point(randomWidth, randomHeight);

        int color = ThreadLocalRandom.current().nextInt(0, 255);
        System.out.println("Choosing color " + color);
        return new Triangle(a, b, c, color);
    }

    public void initPopulation() {
        for(int i = 0; i < individuals; i++) {
            Triangle[] triangleList = new Triangle[triangles];
            for (int j = 0; j < triangles; j++) {
                triangleList[j] = getRandomTriangle();
            }

            population[i] = new Individual(triangleList, Solver.file.blank);

//            try {
//                ImageIO.write(population[i].img, "jpg", new File("./ind" + i + ".jpg"));
//            } catch (Exception e) {
//                System.out.println("Error writing fileeee");
//                System.exit(1);
//            }

        }
        System.out.println("Created population with " + individuals + " individuals!");
    }

    public void drawPopulation(int generation) {
        
        Graphics2D srcG = Solver.file.blank.createGraphics();

        for(int i = 0; i < individuals; i++) {
            try {
                ImageIO.write(population[i].img, "jpg", new File("./triangles-" + generation + "-" + i + ".jpg"));
            } catch (Exception e) {
                System.out.println("Error writing to file!");
                System.exit(1);
            }
            
            // remove image (white background)
//            srcG.setBackground(new Color(255, 255, 255, 0));
//            srcG.clearRect(0, 0, imageWidth, imageHeight);
//            for(int j = 0; j < triangles; j++) {
//                addTriangle(population[i].t[j], Solver.file.blank);
//            }
//            try {
//                ImageIO.write(Solver.file.blank, "jpg", new File("./triangles-" + generation + "-" + i + ".jpg"));
//            } catch (Exception e) {
//                System.out.println("Error writing to file!");
//                System.exit(1);
//            }
        }
    }

    public void drawBest(int index, int generation) {
        try {
            ImageIO.write(population[index].img, "jpg", new File("./triangles-best-" + generation + ".jpg"));
        } catch (Exception e) {
            System.out.println("Error writing to file!");
            System.exit(1);
        }
//        Graphics2D srcG = Solver.file.blank.createGraphics();

        // remove image (white background)
//        srcG.setBackground(new Color(255, 255, 255, 0));
//        srcG.clearRect(0, 0, imageWidth, imageHeight);
//        for(int j = 0; j < triangles; j++) {
//            addTriangle(population[index].t[j], Solver.file.blank);
//        }
//        try {
//            ImageIO.write(Solver.file.blank, "jpg", new File("./triangles-best-" + generation + ".jpg"));
//        } catch (Exception e) {
//            System.out.println("Error writing to file!");
//            System.exit(1);
//        }
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
//            population[i].t.printSelf();
        }
    }

    public void uniformCross() {

        for(int i = 0; i < individuals; i++) {
            double rand = ThreadLocalRandom.current().nextDouble(0, 2);

            Individual parent1 = breedingPool[i];
            Individual parent2;
            
            Triangle[] tList = new Triangle[triangles];
            for (int j = 0; j < triangles; j++) {
                if(rand < pC) {
                    if(i + 1 >= individuals) {
                        parent2 = breedingPool[0];
                    } else {
                        parent2 = breedingPool[i + 1];
                    }

                    // take points and color from parents randomly
                    Triangle t;
                    int color;

                    double prob = ThreadLocalRandom.current().nextDouble(0, 1);
                    if(prob < 0.5) {
                        t = parent1.t[j];
                    } else {
                        t = parent2.t[j];
                    }

                    prob = ThreadLocalRandom.current().nextDouble(0, 1);
                    if(prob < 0.5) {
                        t.color = parent1.t[j].color;
                    } else {
                        t.color = parent2.t[j].color;
                    }
                    tList[j] = t;
                } else {
                    // if crossover does not occur, choose parent1
                    tList[j] = parent1.t[j];
                }
            }
            Individual offspring = new Individual(tList, Solver.file.blank);
            population[i] = offspring;

        }
    }
    
    public void tournamentSelect() {
        int randNum;
        for(int i = 0; i < individuals; i++) {
            int rand1 = (int) ThreadLocalRandom.current().nextInt(0, individuals);
            int rand2 = (int) ThreadLocalRandom.current().nextInt(0, individuals);
            Individual ind1 = population[i];
//            Individual ind1 = population[rand1];
            Individual ind2 = population[rand2];
            double fit1 = fitnessList[i];
            double fit2 = fitnessList[rand2];


            if(fit1 > fit2) {
                breedingPool[i] = ind1;
            } else {
                breedingPool[i] = ind2;
            }
//            breedingPool = population.clone();

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
            boolean mutated = false;
            for(int j = 0; j < triangles; j++) {
                // with some probability, mutate points/color
                // mutate a?
                double prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    population[i].t[j].a = mutate(population[i].t[j].a);
                }
                
                // mutate b?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    population[i].t[j].b = mutate(population[i].t[j].b);
                }
                
                // mutate c?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    population[i].t[j].c = mutate(population[i].t[j].c);
                }
                
                // mutate color?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    int color = population[i].t[j].color;
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
                    population[i].t[j].color = color;
                }
                
            }
            if(mutated) {
                population[i].update();
            }
        }
        
    }

    public int getMaxFitness() {
        double max = Integer.MIN_VALUE;
        int maxIndex = -1;
        for(int i = 0; i < individuals; i++) {
            if(fitnessList[i] > max) {
                max = fitnessList[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    public double fitness(Individual ind) {
        double sum = 0;
        // evaluate fitness of a single individual
        for(int i = 0; i < ind.img.getHeight(); i++) {
            for(int j = 0; j < ind.img.getWidth(); j++) {
                // pixel by pixel comparison of images
                Color c = new Color(ind.img.getRGB(j, i));
                int indRed = c.getRed();
                int sourceRed = Solver.pixels[j][i].getRed();
                double diff = Math.abs(indRed - sourceRed);
                
                sum += (255 - diff);
            }
        }
        
        // 255 - diff?
        
        return sum / (imageWidth * imageHeight);
    }
    
    public void evalFitness() {
        for(int i = 0; i < individuals; i++) {
            fitnessList[i] = fitness(population[i]);
            System.out.println("Individual " + i + " has fitness " + fitnessList[i]);
        }
    }

    public void solveGA() {
        System.out.println("Solving GA...");
        double bestValue = -1;
        int genFound = -1;
        initPopulation();
        drawPopulation(0);

        for(int g = 0; g < generations; g++) {
            // draw each individual first
            if(drawIndividuals) {
                drawPopulation(g);
            }

            evalFitness();
            tournamentSelect();
            uniformCross();
            mutatePopulation();
//            evalFitness();
            int bestFitness = getMaxFitness();
            drawBest(bestFitness, g);
            System.out.println("Best fitness is individual " + bestFitness);
            if(fitnessList[bestFitness] > bestValue) {
                // not really relevant to keep a 'best' individual -- just a triangle
                genFound = g + 1;
                bestValue = fitnessList[bestFitness];
            }

//            if((generations - g) % (generations / 10) == 0) {
                // print currnt solution each 20th of total generations
                System.out.println("(Generation " + g + ")");

//            }
        }
        drawPopulation(generations);

    }
}
