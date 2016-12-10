package component6;

import component1.Node;
import component1.Solution;
import component4.IntraRelocationMove;
import component5.InterRelocationMove;

/**
 * @author Stamatis Pitsios
 *
 * This class contains all the necessary functionality in order to find a better solution than the initial one, by
 * using tabu search method.
 */
class TabuSearchVRP {

    /**
     * The number of iterations that a move will be labeled as tabu.
     */
    private final int tabuHorizon;

    /**
     * The distance matrix
     */
    private double[][] distanceMatrix;

    /**
     * A 2D matrix that will keep the tabu arcs.
     */
    private int[][] tabuMatrix;

    TabuSearchVRP(int horizon, double[][] distanceMatrix) {
        this.tabuHorizon = horizon;
        this.distanceMatrix = distanceMatrix;

        this.tabuMatrix = new int[this.distanceMatrix.length][this.distanceMatrix.length];

        for (int i = 0; i < this.tabuMatrix.length; i++) {
            for (int j = 0; j < this.tabuMatrix.length; j++) {
                this.tabuMatrix[i][j] = -1;
            }
        }
    }

    /**
     * Finds the best possible neighbor of a given solution, by checking all possible intra-route relocation moves.
     *
     * @param solution The solution which we want to improve
     * @return IntraRelocationMove the best possible intra relocation move
     */
    IntraRelocationMove findBestIntraRelocationMove(Solution solution, int iteration) {

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

                    // Create the tabu move
                    TabuArc arc1 = new TabuArc(relocatedCustomer.getId(), afterSuccessor.getId());
                    TabuArc arc2 = new TabuArc(predecessor.getId(), successor.getId());
                    TabuArc arc3 = new TabuArc(after.getId(), relocatedCustomer.getId());

                    // If the move is the best found so far, store it
                    if (newCost < relocationMove.getCost() && !isTabuMove(arc1, arc2, arc3, iteration)) {
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
     * @param iteration The number of tabu search iteration
     */
    void applyIntraRelocationMove(Solution solution, IntraRelocationMove move, int iteration) {

        // Store the Tabu move
        Node relocatedCustomer = solution.getRoutes().get(move.getRoute()).getRoute().get(move.getCustomerPosition());
        Node predecessor = solution.getRoutes().get(move.getRoute()).getRoute().get(move.getCustomerPosition() - 1);
        Node successor = solution.getRoutes().get(move.getRoute()).getRoute().get(move.getCustomerPosition() + 1);
        Node after = solution.getRoutes().get(move.getRoute()).getRoute().get(move.getRelocationPosition());
        Node afterSuccessor = solution.getRoutes().get(move.getRoute()).getRoute().get(move.getRelocationPosition() + 1);
        TabuArc arc1 = new TabuArc(predecessor.getId(), relocatedCustomer.getId());
        TabuArc arc2 = new TabuArc(relocatedCustomer.getId(), successor.getId());
        TabuArc arc3 = new TabuArc(after.getId(), afterSuccessor.getId());
        this.tabuMatrix[arc1.getFrom()][arc1.getTo()] = iteration + this.tabuHorizon;
        this.tabuMatrix[arc2.getFrom()][arc2.getTo()] = iteration + this.tabuHorizon;
        this.tabuMatrix[arc3.getFrom()][arc3.getTo()] = iteration + this.tabuHorizon;

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

    /**
     * Finds the best possible neighbor of a given solution, by checking all possible inter-route relocation moves.
     *
     * @param solution The solution which we want to improve
     * @return InterRelocationMove the best possible inter relocation move
     */
    InterRelocationMove findBestInterRelocationMove(Solution solution, int iteration) {

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

                        // Create the tabu move
                        TabuArc arc1 = new TabuArc(relocatedCustomer.getId(), afterSuccessor.getId());
                        TabuArc arc2 = new TabuArc(predecessor.getId(), successor.getId());
                        TabuArc arc3 = new TabuArc(after.getId(), relocatedCustomer.getId());

                        // If the move is the best found so far and it is not tabu, store the move.
                        if (newCost < move.getCost() && !isTabuMove(arc1, arc2, arc3, iteration)) {
                            move.setRouteFrom(i);
                            move.setRouteTo(k);
                            move.setCustomerPosition(j);
                            move.setRelocationPosition(l);
                            move.setOriginCost(costI);
                            move.setDestinationCost(costL);
                            move.setCost(newCost);
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
    void applyInterRelocationMove(Solution solution, InterRelocationMove move, int iteration) {

        // Store the Tabu move
        Node relocatedCustomer = solution.getRoutes().get(move.getRouteFrom()).getRoute().get(move.getCustomerPosition());
        Node predecessor = solution.getRoutes().get(move.getRouteFrom()).getRoute().get(move.getCustomerPosition() - 1);
        Node successor = solution.getRoutes().get(move.getRouteFrom()).getRoute().get(move.getCustomerPosition() + 1);
        Node after = solution.getRoutes().get(move.getRouteTo()).getRoute().get(move.getRelocationPosition());
        Node afterSuccessor = solution.getRoutes().get(move.getRouteTo()).getRoute().get(move.getRelocationPosition() + 1);
        TabuArc arc1 = new TabuArc(predecessor.getId(), relocatedCustomer.getId());
        TabuArc arc2 = new TabuArc(relocatedCustomer.getId(), successor.getId());
        TabuArc arc3 = new TabuArc(after.getId(), afterSuccessor.getId());
        this.tabuMatrix[arc1.getFrom()][arc1.getTo()] = iteration + this.tabuHorizon;
        this.tabuMatrix[arc2.getFrom()][arc2.getTo()] = iteration + this.tabuHorizon;
        this.tabuMatrix[arc3.getFrom()][arc3.getTo()] = iteration + this.tabuHorizon;

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

    /**
     * Checks if a move is Tabu.
     *
     * @param arc1 The first arc
     * @param arc2 The second arc
     * @param arc3 The third arc
     * @param iteration The number of iteration
     * @return True of False
     */
    private boolean isTabuMove(TabuArc arc1, TabuArc arc2, TabuArc arc3, int iteration) {
        return (iteration <= tabuMatrix[arc1.getFrom()][arc1.getTo()]) &&
                (iteration <= tabuMatrix[arc2.getFrom()][arc2.getTo()]) &&
                (iteration <= tabuMatrix[arc3.getFrom()][arc3.getTo()]);
    }
}
