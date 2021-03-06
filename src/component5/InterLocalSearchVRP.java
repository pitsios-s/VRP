package component5;

import component1.Node;
import component1.Solution;

/**
 * @author Stamatis Pitsios
 *
 * This class contains all the necessary functionality in order to find a better solution than the one we got with
 * greedy approch, by using local search method.
 */
class InterLocalSearchVRP {

    /**
     * The distance matrix
     */
    private double[][] distanceMatrix;

    /**
     * Default Constructor
     */
    InterLocalSearchVRP(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    /**
     * Finds the best possible neighbor of a given solution, by checking all possible inter-route relocation moves.
     *
     * @param solution The solution which we want to improve
     * @return InterRelocationMove the best possible inter relocation move
     */
    InterRelocationMove findBestInterRelocationMove(Solution solution) {

        // The best inter-relocation move found so far.
        InterRelocationMove move = new InterRelocationMove();

        // Loop for every route of the solution
        for (int i = 0; i < solution.getRoutes().size(); i++) {

            // Loop for every customer within the i-th route
            for (int j = 1; j < solution.getRoutes().get(i).getRoute().size() - 1; j++) {

                // The customer to be relocated
                Node relocatedCustomer = solution.getRoutes().get(i).getRoute().get(j);

                // Predecessor of "relocatedCustomer"
                Node predecessor = solution.getRoutes().get(i).getRoute().get(j - 1);

                // Successor of "relocatedCustomer"
                Node successor = solution.getRoutes().get(i).getRoute().get(j + 1);

                // Loop for every other possible route of the solution
                for (int k = 0; k < solution.getRoutes().size(); k++) {

                    // If the 2 routes are the same, ignore them, since this is the case of intra relocation
                    if (i == k)
                        continue;

                    // If the demand of the relocated customer cannot be served by the new vehicle, continue with the next one
                    if (solution.getRoutes().get(k).getLoad() + relocatedCustomer.getDemand() >
                            solution.getRoutes().get(k).getCapacity())
                        continue;

                    // Loop for every customer within the second route
                    for (int l = 0; l < solution.getRoutes().get(k).getRoute().size() - 1; l++) {
                        // The node after which "relocatedCustomer" is going to be inserted.
                        Node after = solution.getRoutes().get(k).getRoute().get(l);

                        // The successor node of node "after"
                        Node afterSuccessor = solution.getRoutes().get(k).getRoute().get(l + 1);

                        // Calculate the cost of the solution, if we apply the specific relocation move.
                        double costRemoved = this.distanceMatrix[predecessor.getId()][relocatedCustomer.getId()] +
                                this.distanceMatrix[relocatedCustomer.getId()][successor.getId()] +
                                this.distanceMatrix[after.getId()][afterSuccessor.getId()];

                        double costAdded = this.distanceMatrix[after.getId()][relocatedCustomer.getId()] +
                                this.distanceMatrix[relocatedCustomer.getId()][afterSuccessor.getId()] +
                                this.distanceMatrix[predecessor.getId()][successor.getId()];

                        // The new cost within route i
                        double costI = this.distanceMatrix[predecessor.getId()][successor.getId()] -
                                this.distanceMatrix[predecessor.getId()][relocatedCustomer.getId()] -
                                this.distanceMatrix[relocatedCustomer.getId()][successor.getId()];

                        // The new cost within route l
                        double costL = this.distanceMatrix[after.getId()][relocatedCustomer.getId()] +
                                this.distanceMatrix[relocatedCustomer.getId()][afterSuccessor.getId()] -
                                this.distanceMatrix[after.getId()][afterSuccessor.getId()];

                        // The difference in the cost of the final solution
                        double newCost = costAdded - costRemoved;

                        // If the move is the best found so far, store the move.
                        if (newCost < move.getCost()) {
                            move.setCost(newCost);
                            move.setRouteFrom(i);
                            move.setRouteTo(k);
                            move.setCustomerPosition(j);
                            move.setRelocationPosition(l);
                            move.setOriginCost(costI);
                            move.setDestinationCost(costL);
                        }
                    }
                }
            }
        }

        // Return the move.
        return move;
    }

    /**
     * Applies an inter-route relocation move to a given solution
     *
     * @param solution The solution to improve
     * @param move The relocation move to be applied
     */
    void applyInterRelocationMove(Solution solution, InterRelocationMove move) {

        // Update the cost of the whole solution
        solution.setTotalCost(solution.getTotalCost() + move.getCost());

        // Update the cost within the origin route.
        solution.getRoutes().get(move.getRouteFrom()).setCost(solution.getRoutes().get(move.getRouteFrom()).getCost() + move.getOriginCost());

        // Update the cost within the destination route.
        solution.getRoutes().get(move.getRouteTo()).setCost(solution.getRoutes().get(move.getRouteTo()).getCost() + move.getDestinationCost());

        // The node to be relocated
        Node relocatedNode = solution.getRoutes().get(move.getRouteFrom()).removeNode(move.getCustomerPosition());

        // Decrease the load of the origin route
        solution.getRoutes().get(move.getRouteFrom()).setLoad(solution.getRoutes().get(move.getRouteFrom()).getLoad() - relocatedNode.getDemand());

        // Increase the load of the destination route
        solution.getRoutes().get(move.getRouteTo()).setLoad(solution.getRoutes().get(move.getRouteTo()).getLoad() + relocatedNode.getDemand());

        // Relocate the node into the new route
        solution.getRoutes().get(move.getRouteTo()).addNodeToRouteWithIndex(relocatedNode, move.getRelocationPosition() + 1);
    }
}
