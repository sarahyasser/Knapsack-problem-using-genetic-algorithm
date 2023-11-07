import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class KnapsackGeneticAlgorithm {
     int knapsackSize;
     int numItems;
     List<Item> items; // Array of items with weight and value
     int populationSize = 50;
     int maxGenerations = 100;
     double mutationRate = 0.1;
     static int testCaseNum=0;


    public KnapsackGeneticAlgorithm(int knapsackSize, int numItems, List<Item> items) {
        this.knapsackSize = knapsackSize;
        this.numItems = numItems;
        this.items = items;
        this.testCaseNum++;
        
    }
    // Initialize a population with random solutions
    public int[][] initializePopulation(int popSize) 
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
    public int fitness(int[] chromosome) {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < numItems; i++) {
            if (chromosome[i] == 1)
            {
                totalWeight += items.get(i).weight; // Weight
                totalValue += items.get(i).value; // Value
            }
        }
        if (totalWeight > knapsackSize) {
            return 0; // Infeasible solution
        }
        return totalValue;
    }

    // Rank-based selection
    public int[][] selectParents(int[][] population) {
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
    public int[][] crossover(int[] parent1, int[] parent2) {
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
    public void mutate(int[] chromosome) {
        for (int i = 0; i < numItems; i++) {
            if (Math.random() < mutationRate) {
                chromosome[i] = 1 - chromosome[i];
            }
        }
    }

    // Main genetic algorithm
    public void geneticAlgorithm() {
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

        System.out.println("\n \nTest Case Index: "+testCaseNum);
        System.out.println("Number of Selected Items: " + countOnes(bestSolution));
        System.out.println("Total Value: " + bestValue);
        System.out.println("Total Weight: " + calculateTotalWeight(bestSolution));
        System.out.println("Selected Items:");
        for (int i = 0; i < numItems; i++) {
            if (bestSolution[i] == 1) {
                System.out.println("Item " + (i + 1) + " - Weight: " + items.get(i).weight + ", Value: " + items.get(i).value);
            }
        }
    }

    public int countOnes(int[] array) //calculate value
    {
        int count = 0;
        for (int value : array) {
            count += value;
        }
        return count;
    }

    public int calculateTotalWeight(int[] solution)  //calculate total weight
    {
        int totalWeight = 0;
        for (int i = 0; i < numItems; i++) {
            if (solution[i] == 1) {
                totalWeight += items.get(i).weight;
            }
        }
        return totalWeight;
    }

}
