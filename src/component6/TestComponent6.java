package component6;

import component1.Solution;
import component3.GreedyVRP;
import component4.IntraRelocationMove;
import component5.InterRelocationMove;

/**
 * @author Stamatis Pitsios
 */
class TestComponent6 {

    public static void main(String[] args) {

        // Initialize a GreedyVRP instance
        GreedyVRP greedyVRP = new GreedyVRP();

        // Find a greedy solution
        Solution solution = greedyVRP.findSolution();

        // The best solution found
        Solution bestSolution = solution.cloneSolution();

        // Initialize a LocalSearchVRP instance
        TabuSearchVRP tabuSearchVRP = new TabuSearchVRP(20, greedyVRP.getDistanceMatrix());

        // The number of iterations that the tabu search algorithm will perform
        int tabuIterations = 200;

        // The iteration into which the best Solution was found.
        int bestIteration = -1;

        // Print the initial cost found using greedy method
        System.out.println("Initial Cost: " + solution.getTotalCost());

        // Repeat until no better solution found
        for (int i = 1; i <= tabuIterations; i++) {

            // Find the best possible intra-relocation move
            IntraRelocationMove intraRelocationMove = tabuSearchVRP.findBestIntraRelocationMove(solution, i, bestSolution);

            // Find the best possible inter-relocation move
            InterRelocationMove interRelocationMove = tabuSearchVRP.findBestInterRelocationMove(solution, i, bestSolution);

            // Apply the best move between inter and intra
            if (intraRelocationMove.getCost() < interRelocationMove.getCost())
                tabuSearchVRP.applyIntraRelocationMove(solution, intraRelocationMove, i);
            else
                tabuSearchVRP.applyInterRelocationMove(solution, interRelocationMove, i);

            // If the current solution that came up is the best solution ever seen, store it
            if (solution.getTotalCost() < bestSolution.getTotalCost()) {
                bestSolution = solution.cloneSolution();
                bestIteration = i;
            }

            // Print the new total cost
            //System.out.println("Iteration " + i + " - New Total Cost: " + solution.getTotalCost());
        }

        // Print the best solution found
        System.out.println("Best solution found at iteration " + bestIteration);
        System.out.println(bestSolution);
    }
}
