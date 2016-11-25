package component2;

/**
 * @author Stamatis Pitsios
 */
public class TestComponent2 {

    public static void main(String[] args) {
        int customers = 30;

        ProblemInitializer initializer = new ProblemInitializer(61092, customers, 10);

        // Print the customers
        System.out.println(initializer.getCustomers());

        // Print the Vehicles
        System.out.println(initializer.getVehicles());

        // Print distance matrix
        double[][] distanceMatrix = initializer.getDistanceMatrix();
        for (int i = 0; i < customers + 1; i++) {
            for (int j = 0; j < customers + 1; j++) {
                System.out.print(distanceMatrix[i][j] + " \t");
            }
            System.out.println();
        }
    }

}
