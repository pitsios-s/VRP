package component2;

import component1.Node;
import component1.Route;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Stamatis Pitsios
 *
 * This factory class contains all the necessary functionality in order to create instances of the VRP problem.
 */
public class ProblemInitializer {

    /**
     * The random generator for the problem.
     */
    private Random ran;

    /**
     * This list will keep all the nodes of the problem.
     * NOTE: position 0 of the list contains the depot.
     */
    private ArrayList<Node> customers;

    /**
     * All the available vehicles.
     */
    private ArrayList<Route> vehicles;

    /**
     * A 2-D matrix that will keep the distances of every node to each other.
     */
    private double[][] distanceMatrix;

    /**
     * The total number of customers.
     */
    private int numberOfNodes;

    public ArrayList<Node> getCustomers() {
        return this.customers;
    }

    public double[][] getDistanceMatrix() {
        return this.distanceMatrix;
    }

    public ArrayList<Route> getVehicles() {
        return this.vehicles;
    }

    /**
     * Constructor
     *
     * @param seed The seed for the random generator.
     * @param numOfNodes The number of customer that we will need for the VRP.
     */
    public ProblemInitializer(int seed, int numOfNodes, int numOfVehicles) {
        this.ran = new Random(seed);
        this.customers = new ArrayList<>();
        this.vehicles = new ArrayList<>();
        this.numberOfNodes = numOfNodes + 1;
        this.distanceMatrix = new double[this.numberOfNodes][this.numberOfNodes];

        this.initializeCustomers();
        this.initializeVehicles(numOfVehicles);
        this.createDistanceMatrix();
    }

    private void initializeCustomers() {
        // First of all, add the depot.
        Node depot = new Node();
        depot.setId(0);
        depot.setDemand(0);
        depot.setX(50);
        depot.setY(50);

        this.customers.add(depot);

        for (int i = 1; i < this.numberOfNodes; i++) {
            Node customer = new Node();
            customer.setId(i);
            customer.setX(ran.nextInt(100));
            customer.setY(ran.nextInt(100));
            customer.setDemand(4 + ran.nextInt(7));

            this.customers.add(customer);
        }
    }

    private void initializeVehicles(int numOfVehicles) {
        for (int i = 0; i < numOfVehicles; i++) {
            this.vehicles.add(new Route(50));
        }
    }

    private void createDistanceMatrix() {
        for (int i = 0; i < this.numberOfNodes; i++) {
            Node n1 = this.customers.get(i);

            for (int j = 0; j < this.numberOfNodes; j++) {
                Node n2 = this.customers.get(j);

                this.distanceMatrix[i][j] = Math.round(Math.sqrt(Math.pow(n1.getX() - n2.getX(), 2) +
                        Math.pow(n1.getY() - n2.getY(), 2)));
            }
        }
    }
}
