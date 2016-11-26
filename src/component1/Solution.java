package component1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stamatis Pitsios
 *
 * An instance of this class repserents a solution to the VRP problem.
 */
public class Solution {

    /**
     * All the routes of the current solution.
     */
    private List<Route> routes;

    /**
     * The total cost of the solution. It is calculated as the sum of the costs of all routes.
     */
    private double totalCost;

    /**
     * Default constructor
     */
    public Solution() {
        this.routes = new ArrayList<>();
        this.totalCost = 0;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        String result = "Solution{" +
                "totalCost=" + totalCost +
                ", routes=[";

        for (Route vehicle: this.routes) {
            result += "\n\t" + vehicle;
        }

        return result + "]}";
    }
}
