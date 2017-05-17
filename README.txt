Final Project - PSO/GA for Image Reproduction
By: Bo Bleckel, Jasper Houston, and Dylan Parsons
Nature Inspired Computation - CSCI 3445
Bowdoin College

/* This program is implemented in Java */

This program uses Particle Swarm Optimization (PSO) to optimize the parameters on a
    Genetic Algorithm (GA) performing Image Reproduction (IR).
The goal of this program is to implement 
A detailed explanation of digit recognition, Neural Networks, our methods, and our results are in the
    included paper entitled NN.pdf.
The main.cpp file deals with the testing and parsing the command line.
The command line arguments are as follows, in the order presented:

For Digit Recognition with a Neural Network:
./main trainingFile testFile type inputNodes outputNodes epochs learningRate
    trainingFile   = name of training file from which to read (string):
    testFile       = name of test file from which to read (string)
    type           = 32x32 bitmap or 8x8 down-sampled image (32 or 8, int)
    inputNodes     = number of input nodes (1024 or 64, int)
    outputNodes    = number of output nodes (1 or 10, int)
    epochs         = number of epochs (iterations) to perform (int)
    learningRate   = learning rate for network (double)
SAMPLE INPUTS:
./main 32x32-bitmaps/optdigits-32x32.tra 32x32-bitmaps/optdigits-32x32.tes 32 1024 10 50 0.01
./main 8x8-integer-inputs/optdigits-8x8-int.tra 8x8-integer-inputs/optdigits-8x8-int.tes 8 64 10 50 0.01

The arguments should be entered in the correct order after the executable for the file,
which is ./main and can be compiled using the included Makefile (simply type make to compile).
Similarly, type “make clean” to remove all executables created by the make file.

The algorithms are implemented using a large class for the Neural Network and smaller
classes for input nodes, output nodes, and digit maps, which are the 8x8 or 32x32 map
and the value they contain.

All parameters are set by command line arguments.

There is a test() function in main.cpp that can be used to loop through the different input representations,
the different output representations, and a variety of learning rates. Due to the small size of the
problems we were looking at, this testing runs incredibly quickly and is a straightforward way of
testing a variety of parameters and discovering the optimal parameters.
