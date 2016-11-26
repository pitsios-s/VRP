package component1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents a Route (Vehicle) that will be used in order to serve a set of customers
 */
public class Route {

    /**
     * The total capacity of the current vehicle.
     */
    private final int capacity;

    /**
     * The current load of the vehicle
     */
    private int load;

    /**
     * A sequence of Customers, that will be served from the current Vehicle.
     */
    private List<Node> route;

    /**
     * The cost of the current Route. It is calculated as the sum of the distances of every next node from the previous one.
     */
    private double cost;

    /**
     * Constructor
     *
     * @param capacity The capacity for this Vehicle
     */
    public Route(int capacity) {
        this.capacity = capacity;
        this.load = 0;
        this.cost = 0;
        this.route = new ArrayList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public List<Node> getRoute() {
        return route;
    }

    /**
     * Returns the last node in the route
     */
    public Node getLastNodeOfTheRoute() {
        return this.route.get(this.route.size() - 1);
    }

    /**
     * Adds a customer in the end of the route.
     *
     * @param node The new customer to be inserted.
     */
    public void addNodeToRoute(Node node) {
        this.route.add(node);
    }

    /**
     * Adds a customer in the route in a specific position in the sequence.
     *
     * @param node The new customer to be inserted
     * @param index The position in which the customer will be inserted.
     */
    public void addNodeToRouteWithIndex(Node node, int index) {
        this.route.add(index, node);
    }

    /**
     * Removes a customer from a specific position in the route.
     *
     * @param index The index from which the customer will be removed
     * @return The removed customer.
     */
    public Node removeNode(int index) {
        return this.route.remove(index);
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        String result =  "Route{" +
                "capacity=" + capacity +
                ", load=" + load +
                ", cost=" + cost +
                ", route=[";

        for (Node customer: this.route) {
            result += "\n\t\t" + customer;
        }

        return result + "]}";
    }
}
