package component1;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents a Node (customer) of the VRP problem.
 */
public class Node implements Cloneable {

    /**
     * The X-axis coordinate in a theoretical 2-D space for the specific customer.
     */
    private int x;

    /**
     * The Y-axis coordinate in a theoretical 2-D space for the specific customer.
     */
    private int y;

    /**
     * A unique identifier for the customer
     */
    private int id;

    /**
     * The current customer's demand.
     */
    private int demand;

    /**
     * A boolean attribute indicating whether or not the customer has been inserted in any route.
     */
    private boolean isRouted;

    /**
     * Empty default constructor.
     */
    public Node() {
        this.isRouted = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public boolean isRouted() {
        return isRouted;
    }

    public void setRouted(boolean routed) {
        isRouted = routed;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", demand=" + demand +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
