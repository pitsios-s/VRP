package component6;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents an arc that we will not allow it to be created during our tabu search method.
 */
class TabuArc {

    /**
     * The id of the node that the arc starts
     */
    private int from;

    /**
     * The id of the node that the arc ends
     */
    private int to;

    TabuArc(int from, int to) {
        this.from = from;
        this.to = to;
    }

    int getFrom() {
        return from;
    }

    int getTo() {
        return to;
    }
}
