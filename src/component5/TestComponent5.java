package component5;

import component1.Solution;
import component3.GreedyVRP;
import component4.IntraLocalSearchVRP;
import component4.IntraRelocationMove;

/**
 * @author Stamatis Pitsios
 */
class TestComponent5 {

    public static void main(String[] args) {

        // Initialize a GreedyVRP instance
        GreedyVRP greedyVRP = new GreedyVRP();

        // Find a greedy solution
        Solution solution = greedyVRP.findSolution();

        // Initialize an IntraLocalSearch and InterLocalSearch objects
        IntraLocalSearchVRP intraLocalSearchVRP = new IntraLocalSearchVRP(greedyVRP.getDistanceMatrix());
        InterLocalSearchVRP interLocalSearchVRP = new InterLocalSearchVRP(greedyVRP.getDistanceMatrix());


        // The number of iterations that the local search algorithm performed.
        int iterations = 0;

        // Print the initial cost found using greedy method
        System.out.println("Initial Cost: " + solution.getTotalCost());

        // Repeat until no better solution found
        while (true) {
            IntraRelocationMove intraRelocationMove = intraLocalSearchVRP.findBestIntraRelocationMove(solution);
            InterRelocationMove interRelocationMove = interLocalSearchVRP.findBestInterRelocationMove(solution);

            // Increase number of iterations
            iterations++;

            if (intraRelocationMove.getCost() >= 0 && interRelocationMove.getCost() >= 0)
                break;
            else {
                if (interRelocationMove.getCost() < intraRelocationMove.getCost())
                    interLocalSearchVRP.applyInterRelocationMove(solution, interRelocationMove);
                else
                    intraLocalSearchVRP.applyIntraRelocationMove(solution, intraRelocationMove);

                // Print the new total cost
                System.out.println("Iteration " + iterations + " - New Total Cost: " + solution.getTotalCost());
            }
        }

        // Print the best solution found
        System.out.println(solution);
    }
}
