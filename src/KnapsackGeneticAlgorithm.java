import java.util.Scanner;
import java.util.Random;

public class KnapsackGeneticAlgorithm {
    static int knapsackSize;
    static int numItems;
    static Item[] items; // Array of items with weight and value
    static int populationSize = 50;
    static int maxGenerations = 100;
    static double mutationRate = 0.1;



    // Initialize a population with random solutions
    public static int[][] initializePopulation(int popSize) //hnwza3 el zeroes w el ones
    {
        int[][] population = new int[popSize][numItems];
        Random random = new Random();
        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < numItems; j++) {
                population[i][j] = random.nextInt(2); // Random 0 or 1
            }
        }
        return population;
    }

    // Calculate the fitness of a chromosome (total value)
    public static int fitness(int[] chromosome) {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < numItems; i++) {
            if (chromosome[i] == 1) //lw el bit be one bnakhod el item m3ana
            {
                totalWeight += items[i].weight; // Weight
                totalValue += items[i].value; // Value
            }
        }
        if (totalWeight > knapsackSize) {
            return 0; // Infeasible solution
        }
        return totalValue;
    }

    // Rank-based selection
    public static int[][] selectParents(int[][] population) {
        int[][] parents = new int[2][numItems];
        int[] selectedIndices = new int[2];

        for (int i = 0; i < 2; i++) {
            int totalFitness = 0;
            int[] sortedIndices = new int[populationSize];
            for (int j = 0; j < populationSize; j++) {
                sortedIndices[j] = j;
                totalFitness += fitness(population[j]);
            }

            // Sort the population by fitness in descending order
            for (int j = 0; j < populationSize - 1; j++) {
                for (int k = j + 1; k < populationSize; k++) {
                    if (fitness(population[sortedIndices[j]]) < fitness(population[sortedIndices[k]])) {
                        int temp = sortedIndices[j];
                        sortedIndices[j] = sortedIndices[k];
                        sortedIndices[k] = temp;
                    }
                }
            }

            // Select a parent using rank-based selection
            double randomValue = Math.random();
            double cumulativeProbability = 0;
            int selectedIndex = 0;
            while (cumulativeProbability < randomValue && selectedIndex < populationSize) {
                cumulativeProbability += (double) fitness(population[sortedIndices[selectedIndex]]) / totalFitness;
                selectedIndex++;
            }
            selectedIndex--;

            // Copy the selected parent to the parents array
            System.arraycopy(population[sortedIndices[selectedIndex]], 0, parents[i], 0, numItems);
            selectedIndices[i] = selectedIndex;
        }

        return parents;
    }

    // One-point crossover
    public static int[][] crossover(int[] parent1, int[] parent2) {
        int[] child1 = new int[numItems];
        int[] child2 = new int[numItems];
        int crossoverPoint = new Random().nextInt(numItems);

        for (int i = 0; i < numItems; i++) {
            if (i < crossoverPoint) {
                child1[i] = parent1[i];
                child2[i] = parent2[i];
            } else {
                child1[i] = parent2[i];
                child2[i] = parent1[i];
            }
        }

        return new int[][] { child1, child2 };
    }

    // Mutation
    public static void mutate(int[] chromosome) {
        for (int i = 0; i < numItems; i++) {
            if (Math.random() < mutationRate) {
                chromosome[i] = 1 - chromosome[i];
            }
        }
    }

    // Main genetic algorithm
    public static void geneticAlgorithm() {
        int[][] population = initializePopulation(populationSize);

        for (int generation = 0; generation < maxGenerations; generation++) {
            int[][] newPopulation = new int[populationSize][numItems];

            for (int i = 0; i < populationSize / 2; i++) {
                int[][] parents = selectParents(population);
                int[][] children = crossover(parents[0], parents[1]);
                mutate(children[0]);
                mutate(children[1]);
                System.arraycopy(children[0], 0, newPopulation[i * 2], 0, numItems);
                System.arraycopy(children[1], 0, newPopulation[i * 2 + 1], 0, numItems);
            }

            population = newPopulation;
        }

        // Find the best solution in the final population
        int bestValue = 0;
        int bestIndex = -1;
        for (int i = 0; i < populationSize; i++) {
            int currentValue = fitness(population[i]);
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestIndex = i;
            }
        }

        int[] bestSolution = population[bestIndex];

        System.out.println("Test Case Index: 1");
        System.out.println("Number of Selected Items: " + countOnes(bestSolution));
        System.out.println("Total Value: " + bestValue);
        System.out.println("Total Weight: " + calculateTotalWeight(bestSolution));
        System.out.println("Selected Items:");
        for (int i = 0; i < numItems; i++) {
            if (bestSolution[i] == 1) {
                System.out.println("Item " + (i + 1) + " - Weight: " + items[i].weight + ", Value: " + items[i].value);
            }
        }
    }

    public static int countOnes(int[] array) //calculate value
    {
        int count = 0;
        for (int value : array) {
            count += value;
        }
        return count;
    }

    public static int calculateTotalWeight(int[] solution)  //calculate total weight
    {
        int totalWeight = 0;
        for (int i = 0; i < numItems; i++) {
            if (solution[i] == 1) {
                totalWeight += items[i].weight;
            }
        }
        return totalWeight;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the knapsack size:");
        knapsackSize = scanner.nextInt();

        System.out.println("Enter the number of items:");
        numItems = scanner.nextInt();

        items = new Item[numItems];
        System.out.println("Enter the weight and value of each item:");
        for (int i = 0; i < numItems; i++) {
            System.out.println("Item " + (i + 1) + ":");
            int weight = scanner.nextInt();
            int value = scanner.nextInt();
            items[i] = new Item(weight, value);
        }
        geneticAlgorithm();
        scanner.close();
    }
}
