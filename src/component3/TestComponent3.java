package component3;

/**
 * @author Stamatis Pitsios
 */
public class TestComponent3 {

    public static void main(String[] args) {

        // Initialize an instance of the GreedyVRP class
        GreedyVRP greedyVRP = new GreedyVRP();

        // Find a greedy solution and print it
        System.out.print(greedyVRP.findSolution());
    }
}
