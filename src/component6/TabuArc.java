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

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabuArc tabuArc = (TabuArc) o;

        if (from != tabuArc.from) return false;
        return to == tabuArc.to;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }
}
