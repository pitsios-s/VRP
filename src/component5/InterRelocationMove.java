package component5;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents a relocation move of a specific customer after another customer, within 2
 * different routes
 */
public class InterRelocationMove {

    /**
     * The route from which a customer will be taken
     */
    private int routeFrom;

    /**
     * The route into which customer is going to be placed
     */
    private int routeTo;

    /**
     * The position of the customer within its initial route
     */
    private int customerPosition;

    /**
     * The position of the new route into which the customer is going to be placed
     */
    private int relocationPosition;

    /**
     * The cost of the move
     */
    private double cost;

    /**
     * The new cost within the origin route
     */
    private double originCost;

    /**
     * The new cost within destination route
     */
    private double destinationCost;

    /**
     * Default constructor
     */
    public InterRelocationMove() {
        this.routeFrom = -1;
        this.routeTo = -1;
        this.customerPosition = -1;
        this.relocationPosition = -1;
        this.cost = Double.MAX_VALUE;
        this.originCost = 0;
        this.destinationCost = 0;
    }

    public int getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(int routeFrom) {
        this.routeFrom = routeFrom;
    }

    public int getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(int routeTo) {
        this.routeTo = routeTo;
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

    public double getOriginCost() {
        return originCost;
    }

    public void setOriginCost(double originCost) {
        this.originCost = originCost;
    }

    public double getDestinationCost() {
        return destinationCost;
    }

    public void setDestinationCost(double destinationCost) {
        this.destinationCost = destinationCost;
    }
}
