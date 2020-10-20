# Final Project - PSO/GA for Image Reproduction
By: Bo Bleckel, Jasper Houston, and Dylan Parsons
Nature Inspired Computation - CSCI 3445
Bowdoin College

Writeup can be found at [GA-PSO.pdf](GA-PSO.pdf)

#### This program is implemented in Java

This program uses Particle Swarm Optimization (PSO) to optimize the parameters on a
    Genetic Algorithm (GA) performing Image Reproduction (IR).
The goal of this program is to look at how effective PSO can be in optimizing parameters.
A detailed explanation of image reproduction, PSO, GAs, our methods, and our results are in the
    included paper entitled PSO-GA.pdf.
The Solver.java deals with parsing the command line and calling either PSO or GA.
The command line arguments are as follows, in the order presented:

### Instructions
For just running the GA:
java Solver fileName individuals triangles selection crossover pC pM generations
    fileName     = name of image file to re-create (string)
    individuals  = number of individuals in population (int)
    triangles    = number of triangles allotted to each individual (int)
    selection    = type of selection to use (int):
                     1     = tournament selection
                     2     = Boltzmann selection
    crossover    = crossover method (int):
                        1   = uniform crossover
    pC           = crossover probability (double)
    pM           = mutation probability (double)
    generations  = max number of generations to run (int)

### Examples
For running the PSO on the GA:
java Solver fileName particles iterations
    fileName     = name of image file to re-create (string)
    particles    = number of particles in swarm (int)
    iterations   = number of iterations for the PSO to run (int)

SAMPLE INPUTS:
For GA:
java Solver Images/hamburger.png 4 20 2 1 0.4 0.1 5000

For PSO/GA:
java Solver Images/hamburger.png 4 20

The arguments should be entered in the correct order after the executable for the file,
which is "java Solver" and can be compiled along with all other necessary files by
typing javac *.java at the command line.

The algorithms are implemented using large classes for GA and PSO as well as small helper
classes for managing triangles, points, particles, neighborhoods, and individuals.

Almost all parameters are set by command line arguments. When running GA, the mutation amounts
are not set via the command line and are found near the top of GA.java.
