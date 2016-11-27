package component4;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents a relocation move of a specific customer after another customer, within the
 * same route.
 */
public class IntraRelocationMove {

    /**
     * The route in which the relocation move refers.
     */
    private int route;

    /**
     * The customer that we will move.
     */
    private int customerPosition;

    /**
     * The position that we will relocate the customer.
     */
    private int relocationPosition;

    /**
     * The cost of the whole solution, if we apply this relocation move.
     */
    private double cost;

    /**
     * Default Constructor
     */
    public IntraRelocationMove() {
        this.route = -1;
        this.customerPosition = -1;
        this.relocationPosition = -1;
        this.cost = Double.MAX_VALUE;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getCustomerPosition() {
        return customerPosition;
    }

    public void setCustomerPosition(int customerPosition) {
        this.customerPosition = customerPosition;
    }

    public int getRelocationPosition() {
        return relocationPosition;
    }

    public void setRelocationPosition(int relocationPosition) {
        this.relocationPosition = relocationPosition;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
