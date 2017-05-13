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
    
    public static final float TRANSLUCENCY = .5f; // NOTE: CAN SET TRIANGLES INDIVIDUALLY Individual.java

    private final int COLOR_MUT_AMNT = 10;
    private final float ALPHA_MUT_AMNT = .05f;
    private final int MUT_AMNT = 5;

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
        drawIndividuals = true;
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

        int[] colorRGB = new int[3];
        for (int i = 0; i < 3; i++) {
            colorRGB[i] = ThreadLocalRandom.current().nextInt(0, 255);
        }
        float alpha = ThreadLocalRandom.current().nextFloat();
        return new Triangle(a, b, c, colorRGB, alpha);
    }

    public void initPopulation() {
        for(int i = 0; i < individuals; i++) {
            Triangle[] triangleList = new Triangle[triangles];
            for (int j = 0; j < triangles; j++) {
                triangleList[j] = getRandomTriangle();
            }

            population[i] = new Individual(triangleList, Solver.file.blank, 0);

        //    try {
        //        ImageIO.write(population[i].img, "jpg", new File("./ind" + i + ".jpg"));
        //    } catch (Exception e) {
        //        System.out.println("Error writing fileeee");
        //        System.exit(1);
        //    }

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
        //    srcG.setBackground(new Color(255, 255, 255, 0));
        //    srcG.clearRect(0, 0, imageWidth, imageHeight);
        //    for(int j = 0; j < triangles; j++) {
        //        addTriangle(population[i].t[j], Solver.file.blank);
        //    }
        //    try {
        //        ImageIO.write(Solver.file.blank, "jpg", new File("./triangles-" + generation + "-" + i + ".jpg"));
        //    } catch (Exception e) {
        //        System.out.println("Error writing to file!");
        //        System.exit(1);
        //    }
        }
    }

    public void drawBreedingPool(int generation) {
        
        Graphics2D srcG = Solver.file.blank.createGraphics();
        
        for(int i = 0; i < individuals; i++) {
            try {
                ImageIO.write(breedingPool[i].img, "jpg", new File("./triangles-breeding-" + generation + "-" + i + ".jpg"));
            } catch (Exception e) {
                System.out.println("Error writing to file!");
                System.exit(1);
            }
        }
    }

    
    public void drawBest(int index, int generation) {
        try {
            ImageIO.write(population[index].img, "jpg", new File("./triangles-best-" + generation + ".jpg"));
        } catch (Exception e) {
            System.out.println("Error writing to file!");
            System.exit(1);
        }
    //    Graphics2D srcG = Solver.file.blank.createGraphics();
    //
    //     remove image (white background)
    //    srcG.setBackground(new Color(255, 255, 255, 0));
    //    srcG.clearRect(0, 0, imageWidth, imageHeight);
    //    for(int j = 0; j < triangles; j++) {
    //        addTriangle(population[index].t[j], Solver.file.blank);
    //    }
    //    try {
    //        ImageIO.write(Solver.file.blank, "jpg", new File("./triangles-best-" + generation + ".jpg"));
    //    } catch (Exception e) {
    //        System.out.println("Error writing to file!");
    //        System.exit(1);
    //    }
    }
    
    public void drawIndividual(Individual ind) {
        try {
            ImageIO.write(ind.img, "jpg", new File("./triangles-individual.jpg"));
        } catch (Exception e) {
            System.out.println("Error writing to file!");
            System.exit(1);
        }
    }

    public void addTriangle(Triangle t, BufferedImage img) {
        // for testing: add a triangle

        Graphics2D g = img.createGraphics();

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, TRANSLUCENCY));

        Color c = new Color(t.color[0], t.color[1], t.color[2]);

        g.setColor(c);

        g.fillPolygon(new int[] {t.a.X, t.b.X, t.c.X}, new int[] {t.a.Y, t.b.Y, t.c.Y}, 3);
        g.dispose();

    }

    public void printPopulation() {
        for(int i = 0; i < individuals; i++) {
        //    population[i].t.printSelf();
        }
    }

    // public void onePCross() {
    //
    //     for(int i = 0; i < individuals; i++) {
    //         double rand = ThreadLocalRandom.current().nextDouble(0, 1);
    //
    //         Individual parent1 = breedingPool[i];
    //         Individual parent2;
    //
    //         Triangle[] tList = new Triangle[triangles];
    //         if(rand < pC) { // doing crossover
    //
    //         } else {
    //
    //         }
    //
    //     }
    // }

    public void uniformCross() {
//            System.out.println("Breeding pool has " + )
        
        for(int i = 0; i < individuals; i++) {
            double rand = ThreadLocalRandom.current().nextDouble(0, 1);
            
            Individual parent1 = breedingPool[i];
            Individual parent2;
            
            Triangle[] tList = new Triangle[triangles];
            if(rand < pC) { // doing crossover
                if(i + 1 >= individuals) {
                    parent2 = breedingPool[0];
                    // System.out.println("breeding " + i + " and 0");
                } else {
                    parent2 = breedingPool[i + 1];
                    // System.out.println("breeding " + i + " and that plus 1");
                }
                
                // rearrange so parent 1 is the one with the higher fitness
//                System.out.println("First, 1 " + parent1.fitness + ", 2 " + parent2.fitness);
                if(parent1.fitness < parent2.fitness) {
                    Individual temp = parent1;
                    parent1 = parent2;
                    parent2 = temp;
                }
//                System.out.println("Second, 1 " + parent1.fitness + ", 2 " + parent2.fitness);

                
                for (int j = 0; j < triangles; j++) {
                    // take points and color from parents randomly
                    Triangle t;
                    int color;
                    
                    double prob = ThreadLocalRandom.current().nextDouble(0, 1);
                    // bias toward taking from parent1 (more fit)
                    
                    // select points
                    if(prob < 0.7) {
                        t = parent1.t[j];
                    } else {
                        t = parent2.t[j];
                    }
                    
                    // select color
                    prob = ThreadLocalRandom.current().nextDouble(0, 1);
                    if(prob < 0.7) {
                        t.color = parent1.t[j].color;
                    } else {
                        t.color = parent2.t[j].color;
                    }
                    
                    // select alpha
                    prob = ThreadLocalRandom.current().nextDouble(0, 1);
                    if(prob < 0.7) {
                        t.alpha = parent1.t[j].alpha;
                    } else {
                        t.alpha = parent2.t[j].alpha;
                    }
                    
                    tList[j] = t;
                }
                
                Individual offspring = new Individual(tList, Solver.file.blank, 0);
                population[i] = offspring;
            } else { // crossover will not occur
                Individual offspring = breedingPool[i];
                population[i] = offspring;
            }
        }
    }

    // old uniform crossover function
    // public void uniformCross() {
    //
    //     for(int i = 0; i < individuals; i++) {
    //         double rand = ThreadLocalRandom.current().nextDouble(0, 1);
    //
    //         Individual parent1 = breedingPool[i];
    //         Individual parent2;
    //
    //         Triangle[] tList = new Triangle[triangles];
    //         for (int j = 0; j < triangles; j++) {
    //             if(rand < pC) {
    //                 if(i + 1 >= individuals) {
    //                     parent2 = breedingPool[0];
    //                 } else {
    //                     parent2 = breedingPool[i + 1];
    //                 }
    //
    //                 // take points and color from parents randomly
    //                 Triangle t;
    //                 int color;
    //
    //                 double prob = ThreadLocalRandom.current().nextDouble(0, 1);
    //                 if(prob < 0.5) {
    //                     t = parent1.t[j];
    //                 } else {
    //                     t = parent2.t[j];
    //                 }
    //
    //                 prob = ThreadLocalRandom.current().nextDouble(0, 1);
    //                 if(prob < 0.5) {
    //                     t.color = parent1.t[j].color;
    //                 } else {
    //                     t.color = parent2.t[j].color;
    //                 }
    //                 tList[j] = t;
    //             } else {
    //                 // if crossover does not occur, choose parent1
    //                 tList[j] = parent1.t[j];
    //             }
    //         }
    //         Individual offspring = new Individual(tList, Solver.file.blank);
    //         population[i] = offspring;
    //
    //     }
    // }
    
    public void boltzmannSelect() {
        double total = 0;
        for(int i = 0; i < individuals; i++) {
            total += Math.exp(fitnessList[i]);
            // System.out.println("Individual " + i + " has fitness " + fitnessList[i]);
        }
        
        for(int i = 0; i < individuals; i++) {
            double prob = 0;
            float rand = ThreadLocalRandom.current().nextFloat();
            for(int j = 0; j < individuals; j++) {
                prob += Math.exp(fitnessList[j]) / total;
                if(prob > rand) {
                    breedingPool[i] = population[j];
                    break;
                }
            }
        }
    }

    public void tournamentSelect() {
//        Individual[] tempPopulation = population.clone();
        Vector<Individual> tempPopulation = new Vector<Individual>();
        for(int i = 0; i < individuals; i++) {
            tempPopulation.add(population[i]);
        }
        
        for(int i = 0; i < individuals / 2; i++) {
            int rand = (int) ThreadLocalRandom.current().nextInt(0, tempPopulation.size());
//            int rand2 = (int) ThreadLocalRandom.current().nextInt(0, tempPopulation.size());
            // System.out.println(i + " " + rand2);
            Individual ind1 = population[i];
//            Individual ind1 = population[rand1];
            Individual ind2 = population[rand];
            // double fit1 = fitnessList[i];
            double fit1 = fitnessList[i];
            double fit2 = fitnessList[rand];
            
            System.out.println("Rand is " + rand + ", so " + fit1 + ", " + fit2);
            
            if(fit1 > fit2) {
                System.out.println("Selecting 1");
                breedingPool[i] = ind1;
                // selected ind1, so remove i from tempPopulation
                tempPopulation.remove(i);
                // System.out.println("Individual " + i + " with fitness " + fit1 + " beats individual " + rand2 + " with fitness " + fit2);
            } else {
                System.out.println("Selecting 2");

                breedingPool[i] = ind2;
                tempPopulation.remove(rand);
                // System.out.println("Individual " + rand2 + " with fitness " + fit2 + " beats individual " + i + " with fitness " + fit1);
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
//        // choose between varying just X, just Y, or both
//        if(prob > 0.66) {
//            // shift X
//            // make sure mutation doesn't go out of range
//            if(p.X + direction * MUT_AMNT >= imageWidth || p.X + direction * MUT_AMNT < 0) {
//                // if out of range, flip the direction
//                p.X += -direction * MUT_AMNT;
//            } else {
//                p.X += direction * MUT_AMNT;
//            }
//        } else if(prob > 0.33) {
//            // shift Y
//            if(p.Y + direction * MUT_AMNT >= imageHeight || p.Y + direction * MUT_AMNT < 0) {
//                p.Y += -direction * MUT_AMNT;
//            } else {
//                p.Y += direction * MUT_AMNT;
//            }
//        } else {
            // shift both X and Y
            if(p.X + direction * MUT_AMNT >= imageWidth || p.X + direction * MUT_AMNT < 0) {
                p.X += -direction * MUT_AMNT;
            } else {
                p.X += direction * MUT_AMNT;
            }

            // re-determine direction for Y
            dir = ThreadLocalRandom.current().nextDouble(0, 1);
            if(dir < 0.5) {
                direction = 1;
            } else {
                direction = -1;
            }

            if(p.Y + direction * MUT_AMNT >= imageHeight || p.Y + direction * MUT_AMNT < 0) {
                p.Y += -direction * MUT_AMNT;
            } else {
                p.Y += direction * MUT_AMNT;
            }
//        }

        return p;
    }

    public void mutatePopulation() {
        int mutRand;
        for(int i = 0; i < individuals; i++) {
            boolean mutated = false;
            for(int j = 0; j < triangles; j++) {
                // with some probability, mutate points/color
                // mutate vertex a?
                double prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    population[i].t[j].a = mutate(population[i].t[j].a);
                }

                // mutate vertex b?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    population[i].t[j].b = mutate(population[i].t[j].b);
                }

                // mutate vertex c?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    population[i].t[j].c = mutate(population[i].t[j].c);
                }

                // mutate color?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    int[] color = population[i].t[j].color;
                    for (int c = 0; c < 3; c++) {
                    double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                        // choose direction
                        int direction = 0;
                        if(dir < 0.5) {
                            direction = 1;
                        } else {
                            direction = -1;
                        }
                        // check out of bounds
                        if(color[c] + direction * COLOR_MUT_AMNT > 255 || color[c] + direction * COLOR_MUT_AMNT < 0) {
                            color[c] += -direction * COLOR_MUT_AMNT;
                        } else {
                            color[c] += direction * COLOR_MUT_AMNT;
                        }
                    }
                    population[i].t[j].color = color;
                }
                
                // mutate alpha?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    mutated = true;
                    double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                    // choose direction
                    int direction = 0;
                    if(dir < 0.5) {
                        direction = 1;
                    } else {
                        direction = -1;
                    }
                    if(population[i].t[j].alpha + direction * ALPHA_MUT_AMNT > 1f || population[i].t[j].alpha + direction * ALPHA_MUT_AMNT < 0) {
                        population[i].t[j].alpha += -direction * ALPHA_MUT_AMNT;
                    } else {
                        population[i].t[j].alpha += direction * ALPHA_MUT_AMNT;
                    }
                }

            }
            if(mutated) {
                population[i].update();
            }
        }

    }

    public void newMutatePopulation() {
        int mutRand;
        for(int i = 0; i < individuals; i++) {
            Triangle[] tList = population[i].t.clone();
            for(int j = 0; j < triangles; j++) {
                // with some probability, mutate points/color/alpha
                // mutate vertex a?
                double prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    tList[j].a = mutate(tList[j].a);
                }
                
                // mutate vertex b?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    tList[j].b = mutate(tList[j].b);
                }
                
                // mutate vertex c?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    tList[j].c = mutate(tList[j].c);
                }
                
                // mutate color?
                
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    int[] color = tList[j].color;
                    for (int c = 0; c < 3; c++) {
                        double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                        // choose direction
                        int direction = 0;
                        if(dir < 0.5) {
                            direction = 1;
                        } else {
                            direction = -1;
                        }
                        // check out of bounds
                        if(color[c] + direction * COLOR_MUT_AMNT > 255 || color[c] + direction * COLOR_MUT_AMNT < 0) {
                            color[c] += -direction * COLOR_MUT_AMNT;
                        } else {
                            color[c] += direction * COLOR_MUT_AMNT;
                        }
                    }
                    tList[j].color = color;
                }
                
                // mutate alpha?
                prob = ThreadLocalRandom.current().nextDouble(0, 1);
                if(prob < pM) {
                    double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                    // choose direction
                    int direction = 0;
                    if(dir < 0.5) {
                        direction = 1;
                    } else {
                        direction = -1;
                    }
                    if(tList[j].alpha + direction * ALPHA_MUT_AMNT > 1f || tList[j].alpha + direction * ALPHA_MUT_AMNT < 0) {
                        tList[j].alpha += -direction * ALPHA_MUT_AMNT;
                    } else {
                        tList[j].alpha += direction * ALPHA_MUT_AMNT;
                    }
                }
                
            }
            Individual newInd = new Individual(tList, Solver.file.blank, 0);
            population[i] = newInd;
            //        newInd.update();
        }
    }

    
    public Individual mutateIndividual(Individual ind) {
        int mutRand;
        Triangle[] tList = ind.t.clone();
        for(int j = 0; j < triangles; j++) {
            // with some probability, mutate points/color/alpha
            // mutate vertex a?
            double prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                tList[j].a = mutate(tList[j].a);
            }
            
            // mutate vertex b?
            prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                tList[j].b = mutate(tList[j].b);
            }
            
            // mutate vertex c?
            prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                tList[j].c = mutate(tList[j].c);
            }
            
            // mutate color?
            
            prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                int[] color = tList[j].color;
                for (int c = 0; c < 3; c++) {
                    double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                    // choose direction
                    int direction = 0;
                    if(dir < 0.5) {
                        direction = 1;
                    } else {
                        direction = -1;
                    }
                    // check out of bounds
                    if(color[c] + direction * COLOR_MUT_AMNT > 255 || color[c] + direction * COLOR_MUT_AMNT < 0) {
                        color[c] += -direction * COLOR_MUT_AMNT;
                    } else {
                        color[c] += direction * COLOR_MUT_AMNT;
                    }
                }
                tList[j].color = color;
            }
            
            // mutate alpha?
            prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                // choose direction
                int direction = 0;
                if(dir < 0.5) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                if(tList[j].alpha + direction * ALPHA_MUT_AMNT > 1f || tList[j].alpha + direction * ALPHA_MUT_AMNT < 0) {
                    tList[j].alpha += -direction * ALPHA_MUT_AMNT;
                } else {
                    tList[j].alpha += direction * ALPHA_MUT_AMNT;
                }
            }
            
        }
        Individual newInd = new Individual(tList, Solver.file.blank, 0);

//        newInd.update();
        return newInd;
    }
  
    public int[] getNewColor(int[] color) {
        for(int c = 0; c < color.length; c++) {
            double prob = ThreadLocalRandom.current().nextDouble(0, 1);
            if(prob < pM) {
                double dir = ThreadLocalRandom.current().nextDouble(0, 1);
                // choose direction
                int direction = 0;
                if(dir < 0.5) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                // check out of bounds
                if(color[c] + direction * COLOR_MUT_AMNT > 255 || color[c] + direction * COLOR_MUT_AMNT < 0) {
                    color[c] += -direction * COLOR_MUT_AMNT;
                } else {
                    color[c] += direction * COLOR_MUT_AMNT;
                }
            }
        }
        return color;
    }
    
    public Individual copyIndividual(Individual ind) {
        System.out.println("COPY\n\n");
        System.out.println("ind fitness " + fitness(ind));
        Triangle[] newList = ind.t.clone();
        Individual newInd = new Individual(newList, ind.img, ind.fitness);
        System.out.println("newInd fitness " + fitness(newInd));
        System.out.println("DONE\n\n");
        return newInd;
    }

    public int getBestFitness() {
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
    
    public int getWorstFitness() {
        double min = Integer.MAX_VALUE;
        int minIndex = -1;
        for(int i = 0; i < individuals; i++) {
            if(fitnessList[i] < min) {
                min = fitnessList[i];
                minIndex = i;
            }
        }
        return minIndex;

    }

    public double fitness(Individual ind) {
        double sum = 0;
        // evaluate fitness of a single individual
        for(int i = 0; i < ind.img.getHeight(); i++) {
            for(int j = 0; j < ind.img.getWidth(); j++) {
                // pixel by pixel comparison of images
                Color c = new Color(ind.img.getRGB(j, i));
                int indRed = c.getRed();
                int indGreen = c.getGreen();
                int indBlue = c.getBlue();
                int sourceRed = Solver.pixels[j][i].getRed();
                int sourceGreen = Solver.pixels[j][i].getGreen();
                int sourceBlue = Solver.pixels[j][i].getBlue();
                
                double rDiff = Math.abs(indRed - sourceRed);
                double gDiff = Math.abs(indGreen - sourceGreen);
                double bDiff = Math.abs(indBlue - sourceBlue);
                
                double avg = ((double) rDiff + gDiff + bDiff) / 3;

                sum += (255 - avg);
            }
        }

        // 255 - diff?

        return (sum / ((double) imageWidth * imageHeight * 255));
    }

    public void evalFitness() {
        double total = 0;
        for(int i = 0; i < individuals; i++) {
            fitnessList[i] = fitness(population[i]);
            // tell the individual what its fitness is (for crossover)
            population[i].fitness = fitnessList[i];
            total += fitnessList[i];
            // System.out.println("Individual " + i + " has fitness " + fitnessList[i]);
        }
        
        // normalize values
        for(int i = 0; i < individuals; i++) {
//            fitnessList[i] = fitnessList[i] / total;
        }
    }

    public void solveGA() {
        System.out.println("Solving GA...");
        double bestValue = -1;
        int genFound = -1;
        initPopulation();
        drawPopulation(0);
        
        Individual store = population[0];
        Individual bestInd = population[0];
        
        int bestFitness = 0;
        
        for(int g = 0; g < generations; g++) {
            // draw each individual first
            if(drawIndividuals) {
//                drawPopulation(g);
            }
            
//            double rand = ThreadLocalRandom.current().nextDouble(0, 1);
//            
//            Individual parent1 = population[0];
//            Individual parent2;
//            
//            Triangle[] tList = new Triangle[triangles];
//            if(rand < pC) { // doing crossover
//                System.out.println("Crossover!");
//                parent2 = population[1];
//                    // System.out.println("breeding " + i + " and 0");
//                for (int j = 0; j < triangles; j++) {
//                    // take points and color from parents randomly
//                    Triangle t;
//                    int color;
//                    
//                    double prob = ThreadLocalRandom.current().nextDouble(0, 1);
//                    if(prob < 0.5) {
//                        t = parent1.t[j];
//                    } else {
//                        t = parent2.t[j];
//                    }
//                    
//                    prob = ThreadLocalRandom.current().nextDouble(0, 1);
//                    if(prob < 0.5) {
//                        t.color = parent1.t[j].color;
//                    } else {
//                        t.color = parent2.t[j].color;
//                    }
//                    tList[j] = t;
//                }
//                newInd = new Individual(tList, Solver.file.blank);
//            } else { // crossover will not occur
//            }
        
//            Point a = new Point(125, 125);
//            Point b = new Point(180, 275);
//            Point c = new Point(250, 125);
//            Point a2 = new Point(105, 125);
//            Point b2 = new Point(160, 275);
//            Point c2 = new Point(230, 125);
//            
//            int[] list = {0, 100, 200};
//            Triangle tri = new Triangle(a, b, c, list, .5f);
//            Triangle[] tList = {tri};
//            newInd = new Individual(tList, Solver.file.blank);
//            
//            int[] list2 = {200, 175, 150};
//            Triangle tri2 = new Triangle(a, b, c, list2, .5f);
//            Triangle tri3 = new Triangle(a2, b2, c2, list2, .5f);
//            Triangle[] tList2 = {tri, tri3};
//            Individual newInd2 = new Individual(tList2, Solver.file.blank);
//
//            population[0] = newInd;
//            population[1] = newInd2;
//            drawPopulation(0);
//            
//            
//            Color color = new Color(newInd.img.getRGB(180, 180));
//            int sRed = color.getRed();
//            int sGreen = color.getGreen();
//            int sBlue = color.getBlue();
//            System.out.println(sRed + ", " + sGreen + ", " + sBlue);
//            
//            Color color2 = new Color(newInd2.img.getRGB(180, 180));
//            int sRed2 = color2.getRed();
//            int sGreen2 = color2.getGreen();
//            int sBlue2 = color2.getBlue();
//            System.out.println(sRed2 + ", " + sGreen2 + ", " + sBlue2);

            
            
            
            
//            Triangle[] triangleList = new Triangle[triangles];
//            for (int j = 0; j < triangles; j++) {
//                triangleList[j] = getRandomTriangle();
//            }

        
//            Individual newInd = mutateIndividual(population[0]);
//            double fit = fitness(newInd);
//            System.out.println("New fitness " + fit);
//            if(fit > fitnessList[0]) {
//                population[0] = newInd;
//            }
            
            //                    population[0] = mutateIndividual(population[1]);

//            if(fitnessList[0] > fitnessList[1]) {
//                population[1] = mutateIndividual(population[0]);
//
//                // keep first, get new random for second
//                for(int t = 0; t < population[1].t.length; t++) {
//                    int[] newC = getNewColor(population[0].t[t].color);
//                    population[1].t[t].color = newC;
//                }
//                population[1].update();
//
//            } else {
//                population[0] = mutateIndividual(population[1]);
//
//                // keep second, get new random for first
//                for(int t = 0; t < population[0].t.length; t++) {
//                    int[] newC = getNewColor(population[1].t[t].color);
//                    population[0].t[t].color = newC;
//                }
//                population[0].update();
//            }
            // somehow introduce a gradient towards the best color at a given pixel?

            
//            tournamentSelect();
//            uniformCross();
//            mutatePopulation();
        //    evalFitness();
            
//            drawPopulation(g);
            store.fitness = fitness(store);
            System.out.println("HIHIStore's fitness is " + store.fitness);

            System.out.println("FIRST ARE " + g + ":");
            for(int i = 0; i < individuals; i++) {
                System.out.println(fitnessList[i]);
            }
            
            evalFitness();
            System.out.println("Fitnesses at beginning of " + g + ":");
            for(int i = 0; i < individuals; i++) {
                System.out.println(fitnessList[i]);
            }
            
            bestFitness = getBestFitness();
            System.out.println("Best is " + bestFitness + "(" + fitnessList[bestFitness] + ")");
            store = copyIndividual(population[bestFitness]);
            store.fitness = fitness(store);
            System.out.println("Store's fitness is " + store.fitness);
            
//            tournamentSelect();
            boltzmannSelect();
////            drawBreedingPool(g);
            uniformCross();
            newMutatePopulation();
            evalFitness();

            System.out.println("Fitnesses AFTER " + g + ":");
            for(int i = 0; i < individuals; i++) {
                System.out.println(fitnessList[i]);
            }
            // re-insert previous best fitness into worst-fitness position
            int worst = getWorstFitness();
            System.out.println("Worst is " + worst + "(" + fitnessList[worst] + ")");
            population[worst] = copyIndividual(store);
            System.out.println("Just set, but now fitness is " + fitness(population[worst]) + " vs " + fitness(store));
            fitnessList[worst] = store.fitness;
//            population[worst].update();
            store.fitness = fitness(store);
            System.out.println("Store's AFTER is " + store.fitness);

            
//            drawPopulation(888);
            
            bestFitness = getBestFitness();
            
            if(fitnessList[bestFitness] > bestValue) {
                genFound = g + 1;
                bestValue = fitnessList[bestFitness];
                bestInd = population[0];
            }

            if((generations - g) % 100 == 0) {
                // draw current solution
                System.out.println("Best fitness is individual " + bestFitness + " with " + fitnessList[bestFitness]);

                System.out.println("(Generation " + g + ")");
                drawBest(bestFitness, g);
            }
        }
        
        System.out.println("Found on " + genFound);
        drawIndividual(bestInd);
        
//        Color color = new Color(Solver.file.image.getRGB(125, 170));
//        int sRed = color.getRed();
//        int sGreen = color.getGreen();
//        int sBlue = color.getBlue();
//        System.out.println(sRed + ", " + sGreen + ", " + sBlue);
//        
//        Color color2 = new Color(population[bestFitness].img.getRGB(125, 170));
//        int red = color2.getRed();
//        int green = color2.getGreen();
//        int blue = color2.getBlue();
//        System.out.println(red + ", " + green + ", " + blue);
        
//        drawPopulation(generations);

    }
}
