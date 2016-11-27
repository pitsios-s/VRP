package component6;

/**
 * @author Stamatis Pitsios
 *
 * An instance of this class represents a move that is forbidden to be applied, since it may lead to a solution that we
 * already met before.
 *
 * Every relocation move, either intra of inter, creates and destroys 3 edges. In order to forbid a move, we should
 * store the 3 edges that were destroyed and then prevent the method to apply a move that creates those 3 edges.
 *
 * Since we also have multiple routes, we also store the id of the route that those 3 edges are forbidden.
 */
public class TabuMove {

    /**
     * The first arc to forbid.
     */
    private TabuArc arc1;

    /**
     * The second arc to forbid.
     */
    private TabuArc arc2;

    /**
     * The third arc to forbid.
     */
    private TabuArc arc3;

    /**
     * The route in which this move is forbidden.
     */
    private int routeID;

    TabuMove(TabuArc arc1, TabuArc arc2, TabuArc arc3, int routeID) {
        this.arc1 = arc1;
        this.arc2 = arc2;
        this.arc3 = arc3;
        this.routeID = routeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabuMove tabuMove = (TabuMove) o;

        if (routeID != tabuMove.routeID) return false;

        boolean condition1 = arc1.equals(tabuMove.arc1) || arc1.equals(tabuMove.arc2) || arc1.equals(tabuMove.arc3);
        boolean condition2 = arc2.equals(tabuMove.arc1) || arc2.equals(tabuMove.arc2) || arc2.equals(tabuMove.arc3);
        boolean condition3 = arc3.equals(tabuMove.arc1) || arc3.equals(tabuMove.arc2) || arc3.equals(tabuMove.arc3);

        return  condition1 && condition2 && condition3;
    }

    @Override
    public int hashCode() {
        int result = arc1.hashCode();
        result = 31 * result + arc2.hashCode();
        result = 31 * result + arc3.hashCode();
        result = 31 * result + routeID;
        return result;
    }
}
