package component4;

import component1.Solution;
import component3.GreedyVRP;

/**
 * @author Stamatis Pitsios
 */
class TestComponent4 {

    public static void main(String[] args) {

        // Initialize a GreedyVRP instance
        GreedyVRP greedyVRP = new GreedyVRP();

        // Find a greedy solution
        Solution solution = greedyVRP.findSolution();

        // Initialize a LocalSearchVRP instance
        LocalSearchVRP localSearchVRP = new LocalSearchVRP(greedyVRP.getDistanceMatrix());

        // The number of iterations that the local search algorithm performed.
        int iterations = 0;

        // Print the initial cost found using greedy method
        System.out.println("Initial Cost: " + solution.getTotalCost());

        // Repeat until no better solution found
        while (true) {
            IntraRelocationMove intraRelocationMove = localSearchVRP.findBestIntraRelocationMove(solution);

            // Increase number of iterations
            iterations++;

            if (intraRelocationMove.getCost() >= 0)
                break;
            else {
                localSearchVRP.applyIntraRelocationMove(solution, intraRelocationMove);

                // Print the new total cost
                System.out.println("Iteration " + iterations + " - New Total Cost: " + solution.getTotalCost());
            }
        }

        // Print the best solution found
        System.out.println(solution);
    }
}
