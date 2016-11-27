package component6;

import component1.Node;
import component1.Solution;
import component4.IntraRelocationMove;

import java.util.ArrayList;
import java.util.List;

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
     * A list of the tabu moves
     */
    private List<TabuMove> tabuMoves;

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
        this.tabuMoves = new ArrayList<>();
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

                    // Create the tabu move object
                    TabuArc arc1 = new TabuArc(relocatedCustomer.getId(), afterSuccessor.getId());
                    TabuArc arc2 = new TabuArc(predecessor.getId(), successor.getId());
                    TabuArc arc3 = new TabuArc(after.getId(), relocatedCustomer.getId());
                    TabuMove tabuMove = new TabuMove(arc1, arc2, arc3, i);

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
     * Checks if a move is Tabu.
     *
     * @param arc1 The first arc
     * @param arc2 The second arc
     * @param arc3 The third arc
     * @param iteration The number of iteration
     * @return True of False
     */
    private boolean isTabuMove(TabuArc arc1, TabuArc arc2, TabuArc arc3, int iteration) {
        return (iteration < tabuMatrix[arc1.getFrom()][arc2.getTo()]) &&
                (iteration < tabuMatrix[arc2.getFrom()][arc2.getTo()]) &&
                (iteration < tabuMatrix[arc3.getFrom()][arc3.getTo()]);
    }
}
