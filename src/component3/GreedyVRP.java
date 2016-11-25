package component3;

import component1.Node;
import component1.Route;
import component1.Solution;
import component2.ProblemInitializer;

import java.util.List;

/**
 * This class contains all the necessary functionality in order to solve the VRP problem using a greedy approach.
 */
public class GreedyVRP {

    /**
     * All the customers
     */
    private List<Node> customers;

    /**
     * All the vehicles.
     */
    private List<Route> vehicles;

    /**
     * The distance matrix for the customers
     */
    private double[][] distanceMatrix;

    /**
     * Constructor
     */
    public GreedyVRP() {
        ProblemInitializer initializer = new ProblemInitializer(61092, 30, 10);
        this.customers = initializer.getCustomers();
        this.vehicles = initializer.getVehicles();
        this.distanceMatrix = initializer.getDistanceMatrix();
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    /**
     * Finds and returns a solution to the VRP using greedy algorithm approach
     *
     * @return Solution
     */
    public Solution findSolution() {
        // The final Solution
        Solution solution = new Solution();

        // Fetch the depot node.
        Node depot = this.customers.remove(0);

        // Fetch the first available vehicle
        Route currentVehicle = this.vehicles.remove(0);

        // Add the depot to the vehicle.
        currentVehicle.addNodeToRoute(depot);

        // Repeat until all customers are routed or if we run out vehicles.
        while (true) {

            // If we served all customers, exit.
            if (this.customers.size() == 0)
                break;

            // Get the last node of the current route. We will try to find the closest node to it that also satisfies the capacity constraint.
            Node lastInTheCurrentRoute = currentVehicle.getLastNodeOfTheRoute();

            // The distance of the closest node, if any, to the last node in the route.
            double smallestDistance = Double.MAX_VALUE;

            // The closest node, if any, to the last node in the route that also satisfies the capacity constraint.
            Node closestNode = null;

            // Find the nearest neighbor based on distance
            for (Node n: this.customers) {
                double distance = this.distanceMatrix[lastInTheCurrentRoute.getId()][n.getId()];

                // If we found a customer with closer that the value of "smallestDistance" that also satisfies the capacity constraint, store him temporarily
                if ( (distance < smallestDistance) && (currentVehicle.getLoad() + n.getDemand() <= currentVehicle.getCapacity()) ) {
                    smallestDistance = distance;
                    closestNode = n;
                }
            }

            // A node that satisfies the capacity constraint found
            if (closestNode != null) {

                // Add the closest node to the route
                currentVehicle.addNodeToRoute(closestNode);

                // Increase the cost of the current route by the distance of the previous final node to the new one
                currentVehicle.setCost(currentVehicle.getCost() + smallestDistance);

                // Increase the load of the vehicle by the demand of the new node-customer
                currentVehicle.setLoad(currentVehicle.getLoad() + closestNode.getDemand());

                // Remove customer from the non-served customers list.
                this.customers.remove(closestNode);

            // We didn't find any node that satisfies the condition.
            } else {

                // Increase cost by the distance to travel from the last node back to depot
                currentVehicle.setCost(currentVehicle.getCost() + this.distanceMatrix[lastInTheCurrentRoute.getId()][depot.getId()]);

                // Terminate current route by adding the depot as a final destination
                currentVehicle.addNodeToRoute(depot);

                // Add the finalized route to the solution
                solution.addRoute(currentVehicle);

                // Increase the solution's total cost by the cost of the finalized route
                solution.setTotalCost(solution.getTotalCost() + currentVehicle.getCost());

                // Recruit a new vehicle.
                currentVehicle = this.vehicles.remove(0);

                // Add the depot as a starting point to the new route
                currentVehicle.addNodeToRoute(depot);
            }

        }

        // Now add the final route to the solution
        currentVehicle.setCost(currentVehicle.getCost() + this.distanceMatrix[currentVehicle.getLastNodeOfTheRoute().getId()][depot.getId()]);
        currentVehicle.addNodeToRoute(depot);
        solution.addRoute(currentVehicle);
        solution.setTotalCost(solution.getTotalCost() + currentVehicle.getCost());

        return solution;
    }
}
