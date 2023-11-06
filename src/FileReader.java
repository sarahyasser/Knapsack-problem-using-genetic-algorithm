import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class FileReader {
    private Scanner scanner;

    public FileReader(String filename) {
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.err.println("Error: Cannot open file " + filename);
            scanner = null;
        }
    }

    public boolean isOpen() {
        return scanner != null;
    }
    
    public int readNumOfTestCases() throws Exception {
    	if (scanner.hasNextInt()) {
    		return scanner.nextInt();
    	}
    	else throw new Exception();
    }

    public boolean readTestCase(int[] knapsackSize, List<Item> items) {
        if (!isOpen()) {
            System.err.println("Error: File is not open.");
            return false;
        }

        if (scanner.hasNextInt()) {
            knapsackSize[0] = scanner.nextInt();
        } else {
            System.err.println("Error reading knapsack size.");
            return false;
        }
    	if (scanner.hasNextInt()) {
            int numItems = scanner.nextInt();
            items.clear();
            for (int i = 0; i < numItems; i++) {
                if (scanner.hasNextInt()) {
                    int weight = scanner.nextInt();
                    if (scanner.hasNextInt()) {
                        int value = scanner.nextInt();
                        items.add(new Item(weight, value));
                    } else {
                        System.err.println("Error reading item data.");
                        return false;
                    }
                } else {
                    System.err.println("Error reading item data.");
                    return false;
                }
            }
            return true;
        } else {
            System.err.println("Error reading the number of items.");
            return false;
        }
       
       
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}