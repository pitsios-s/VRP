package component4;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents a relocation move of a specific customer after another customer, within the
 * same route.
 */
class IntraRelocationMove {

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
    IntraRelocationMove() {
        this.route = -1;
        this.customerPosition = -1;
        this.relocationPosition = -1;
        this.cost = Double.MAX_VALUE;
    }

    int getRoute() {
        return route;
    }

    void setRoute(int route) {
        this.route = route;
    }

    int getCustomerPosition() {
        return customerPosition;
    }

    void setCustomerPosition(int customerPosition) {
        this.customerPosition = customerPosition;
    }

    int getRelocationPosition() {
        return relocationPosition;
    }

    void setRelocationPosition(int relocationPosition) {
        this.relocationPosition = relocationPosition;
    }

    double getCost() {
        return cost;
    }

    void setCost(double cost) {
        this.cost = cost;
    }
}
