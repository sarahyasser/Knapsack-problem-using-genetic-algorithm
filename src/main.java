import java.util.ArrayList;
import java.util.List;

public class main {

	public static void main(String[] args) {
		String filename = "knapsack_input.txt"; 

        FileReader reader = new FileReader(filename);

        if (!reader.isOpen()) {
            System.err.println("Failed to open the input file.");
            return;
        }
        int numOfTestCases = 0;
        try {
			numOfTestCases = reader.readNumOfTestCases();
			System.out.println("Number of test cases: "+ numOfTestCases);
		} catch (Exception e) {
			e.printStackTrace();
		}
        for(int i = 0; i<numOfTestCases;i++)
        {
        	int[] knapsackSize = new int[1];
            List<Item> items = new ArrayList<>();

            if (reader.readTestCase(knapsackSize, items)) {
                System.out.println("Knapsack Size: " + knapsackSize[0]);
                System.out.println("Items:");
                for (Item item : items) {
                    System.out.println("Weight: " + item.weight + " Value: " + item.value);
                }
            }
        }
        

        reader.close();
	}

}
