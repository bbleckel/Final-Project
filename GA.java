import java.util.*;


public class GA {
    // class variables
    int individuals;
    double pC;
    double pM;
    int selection;
    int crossover;
    int generations;
    
    public GA(int individuals, int selection, int crossover, double pC, double pM, int generations) {
        this->individuals = individuals;
        this->selection = selection;
        this->crossover = crossover;
        this->pC = pC;
        this->pM = pM;
        this->generations = generations;
        this->bestValue = 0;
    }
}
