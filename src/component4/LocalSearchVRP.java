package component4;

import component1.Node;
import component1.Solution;
import component3.GreedyVRP;

/**
 * @author Stamatis Pitsios
 *
 * This class contains all the necessary functionality in order to find a better solution than the one we got with
 * greedy approch, by using local search method.
 */
class LocalSearchVRP {

    /**
     * The solution of the VRP using greedy method
     */
    private Solution greedySolution;

    /**
     * The distance matrix
     */
    private double[][] distanceMatrix;

    /**
     * Default Constructor
     */
    LocalSearchVRP() {
        // Get the solution of greedy method.
        GreedyVRP greedyVRP = new GreedyVRP();
        this.greedySolution = greedyVRP.findSolution();

        this.distanceMatrix = greedyVRP.getDistanceMatrix();
    }

    Solution findSolution() {

        // The number of iterations that the local search algorithm performed.
        int iterations = 0;

        // Print the initial cost found using greedy method
        System.out.println("Initial Cost: " + this.greedySolution.getTotalCost());

        // Repeat until no better solution found
        while (true) {

            // Create an IntraRelocationMove object
            IntraRelocationMove relocationMove = new IntraRelocationMove();

            // Loop for every vehicle that serves a set of customers
            for (int i = 0; i < greedySolution.getRoutes().size(); i++) {

                // Loop for every customer in the current route
                for (int j = 1; j < greedySolution.getRoutes().get(i).getRoute().size() - 1; j++) {

                    // The customer to be relocated
                    Node relocatedCustomer = greedySolution.getRoutes().get(i).getRoute().get(j);

                    // Predecessor of "relocatedCustomer"
                    Node predecessor = greedySolution.getRoutes().get(i).getRoute().get(j - 1);

                    // Successor of "relocatedCustomer"
                    Node successor = greedySolution.getRoutes().get(i).getRoute().get(j + 1);

                    // Loop for every possible relocation position
                    for (int k = 1; k < greedySolution.getRoutes().get(i).getRoute().size() - 1; k++) {

                        // If the 2 customers are the same in the current iteration, ignore this iteration
                        if (j == k || k == j - 1)
                            continue;

                        // The node after which "relocatedCustomer" is going to be inserted.
                        Node after = greedySolution.getRoutes().get(i).getRoute().get(k);

                        // The successor node of node "after"
                        Node afterSuccessor = greedySolution.getRoutes().get(i).getRoute().get(k + 1);

                        // Calculate the cost of the solution, if we apply the specific relocation move.
                        double costRemoved = this.distanceMatrix[predecessor.getId()][relocatedCustomer.getId()] +
                                this.distanceMatrix[relocatedCustomer.getId()][successor.getId()] +
                                this.distanceMatrix[after.getId()][afterSuccessor.getId()];

                        double costAdded = this.distanceMatrix[after.getId()][relocatedCustomer.getId()] +
                                this.distanceMatrix[relocatedCustomer.getId()][afterSuccessor.getId()] +
                                this.distanceMatrix[predecessor.getId()][successor.getId()];

                        double newCost = costAdded - costRemoved;

                        // If the move is the best found so far, store it
                        if (newCost < relocationMove.getCost()) {
                            relocationMove.setCost(newCost);
                            relocationMove.setRoute(i);
                            relocationMove.setCustomerPosition(j);
                            relocationMove.setRelocationPosition(k);
                        }
                    }
                }
            }

            // Increase number of iterations
            iterations++;

            if (relocationMove.getCost() >= 0)
                break;
            else {
                applyIntraRelocationMove(relocationMove);

                // Print the new total cost
                System.out.println("Iteration " + iterations + " - New Total Cost: " + this.greedySolution.getTotalCost());
            }
        }

        return this.greedySolution;
    }

    private void applyIntraRelocationMove(IntraRelocationMove move) {
        // Update the cost of the whole solution
        this.greedySolution.setTotalCost(this.greedySolution.getTotalCost() + move.getCost());

        // Update the cost within the specific route.
        this.greedySolution.getRoutes().get(move.getRoute()).setCost(this.greedySolution.getRoutes().
                get(move.getRoute()).getCost() + move.getCost());

        // The node to be relocated
        Node relocatedNode = this.greedySolution.getRoutes().get(move.getRoute()).removeNode(move.getCustomerPosition());

        // Relocate the necessary nodes.
        if (move.getCustomerPosition() < move.getRelocationPosition())
            this.greedySolution.getRoutes().get(move.getRoute()).addNodeToRouteWithIndex(relocatedNode, move.getRelocationPosition());
        else
            this.greedySolution.getRoutes().get(move.getRoute()).addNodeToRouteWithIndex(relocatedNode, move.getRelocationPosition() + 1);
    }
}
