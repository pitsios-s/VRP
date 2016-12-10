package component4;

import component1.Node;
import component1.Solution;

/**
 * @author Stamatis Pitsios
 *
 * This class contains all the necessary functionality in order to find a better solution than the one we got with
 * greedy approch, by using local search method.
 */
public class IntraLocalSearchVRP {

    /**
     * The distance matrix
     */
    private double[][] distanceMatrix;

    /**
     * Default Constructor
     */
    public IntraLocalSearchVRP(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    /**
     * Finds the best possible neighbor of a given solution, by checking all possible intra-route relocation moves.
     *
     * @param solution The solution which we want to improve
     * @return IntraRelocationMove the best possible intra relocation move
     */
    public IntraRelocationMove findBestIntraRelocationMove(Solution solution) {

        // Create an IntraRelocationMove object
        IntraRelocationMove relocationMove = new IntraRelocationMove();

        // Loop for every vehicle that serves a set of customers
        for (int i = 0; i < solution.getRoutes().size(); i++) {

            // Loop for every customer in the current route
            for (int j = 1; j <solution.getRoutes().get(i).getRoute().size() - 1; j++) {

                // The customer to be relocated
                Node relocatedCustomer = solution.getRoutes().get(i).getRoute().get(j);

                // Predecessor of "relocatedCustomer"
                Node predecessor = solution.getRoutes().get(i).getRoute().get(j - 1);

                // Successor of "relocatedCustomer"
                Node successor = solution.getRoutes().get(i).getRoute().get(j + 1);

                // Loop for every possible relocation position
                for (int k = 0; k < solution.getRoutes().get(i).getRoute().size() - 1; k++) {

                    // If the 2 customers are the same in the current iteration, ignore this iteration
                    if (j == k || k == j - 1)
                        continue;

                    // The node after which "relocatedCustomer" is going to be inserted.
                    Node after = solution.getRoutes().get(i).getRoute().get(k);

                    // The successor node of node "after"
                    Node afterSuccessor = solution.getRoutes().get(i).getRoute().get(k + 1);

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

        return relocationMove;
    }

    /**
     * Applies an intra-route relocation move to a given solution
     *
     * @param solution The solution to improve
     * @param move The relocation move to be applied
     */
    public void applyIntraRelocationMove(Solution solution, IntraRelocationMove move) {
        // Update the cost of the whole solution
        solution.setTotalCost(solution.getTotalCost() + move.getCost());

        // Update the cost within the specific route.
        solution.getRoutes().get(move.getRoute()).setCost(solution.getRoutes().get(move.getRoute()).getCost() + move.getCost());

        // The node to be relocated
        Node relocatedNode = solution.getRoutes().get(move.getRoute()).removeNode(move.getCustomerPosition());

        // Relocate the necessary nodes.
        if (move.getCustomerPosition() < move.getRelocationPosition())
            solution.getRoutes().get(move.getRoute()).addNodeToRouteWithIndex(relocatedNode, move.getRelocationPosition());
        else
            solution.getRoutes().get(move.getRoute()).addNodeToRouteWithIndex(relocatedNode, move.getRelocationPosition() + 1);
    }
}
