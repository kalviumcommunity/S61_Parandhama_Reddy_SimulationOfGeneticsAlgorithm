import java.util.Scanner;

abstract class Organism {
    protected String type;

    public Organism() {
        this.type = "Unknown";
    }

    public Organism(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class Individual extends Organism {
    protected int[] chromosome;
    protected int fitness;

    public Individual(int[] chromosome) {
        super("Individual");
        this.chromosome = chromosome;
        GeneticOperations.calculateFitness(this);
    }

    public int[] getChromosome() {
        return this.chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
        GeneticOperations.calculateFitness(this);
    }

    public int getFitness() {
        return this.fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}

class GeneticOperations {
    public static void calculateFitness(Individual individual) {
        int fitness = 0;
        for (int gene : individual.getChromosome()) {
            if (gene == 1)
                fitness++;
        }
        individual.setFitness(fitness);
    }

    public static void mutate(Individual individual) {
        int[] chromosome = individual.getChromosome();
        int mutationPoint = (int) (Math.random() * chromosome.length);
        chromosome[mutationPoint] = chromosome[mutationPoint] == 1 ? 0 : 1;
        calculateFitness(individual);
    }

    public static Individual[] crossover(Individual parent1, Individual parent2) {
        int length = parent1.getChromosome().length;
        Individual[] offspring = { new Individual(new int[length]), new Individual(new int[length]) };
        int crossoverPoint = (int) (Math.random() * length);

        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                offspring[0].getChromosome()[i] = parent1.getChromosome()[i];
                offspring[1].getChromosome()[i] = parent2.getChromosome()[i];
            } else {
                offspring[0].getChromosome()[i] = parent2.getChromosome()[i];
                offspring[1].getChromosome()[i] = parent1.getChromosome()[i];
            }
        }
        calculateFitness(offspring[0]);
        calculateFitness(offspring[1]);

        return offspring;
    }
}

class Population {
    protected final int populationSize;
    protected final Individual[] individuals;

    public Population(int populationSize, int chromosomeLength) {
        this.populationSize = populationSize;
        this.individuals = new Individual[populationSize];
        initializePopulation(chromosomeLength);
    }

    private void initializePopulation(int chromosomeLength) {
        for (int i = 0; i < populationSize; i++) {
            int[] chromosome = new int[chromosomeLength];
            for (int j = 0; j < chromosomeLength; j++) {
                chromosome[j] = (Math.random() > 0.5) ? 1 : 0;
            }
            individuals[i] = new Individual(chromosome);
        }
    }

    public Individual[] getIndividuals() {
        return this.individuals;
    }

    public Individual selectParent() {
        int randomIndex = (int) (Math.random() * populationSize);
        return individuals[randomIndex];
    }
}

class PopulationEvolution {
    private final Population population;

    public PopulationEvolution(Population population) {
        this.population = population;
    }

    public void evolve() {
        for (Individual individual : population.getIndividuals()) {
            GeneticOperations.mutate(individual);
        }
    }
}

class PopulationUI {
    private final PopulationEvolution evolution;
    private final Population population;

    public PopulationUI(PopulationEvolution evolution, Population population) {
        this.evolution = evolution;
        this.population = population;
    }

    public void displayMenu() {
        Scanner input = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. View all individual fitness levels");
            System.out.println("2. Mutate an individual");
            System.out.println("3. Perform crossover between two individuals");
            System.out.println("4. Evolve the population");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    viewFitnessLevels();
                    break;
                case 2:
                    mutateIndividual(input);
                    break;
                case 3:
                    performCrossover(input);
                    break;
                case 4:
                    evolvePopulation();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        input.close();
    }

    private void viewFitnessLevels() {
        for (int i = 0; i < population.getIndividuals().length; i++) {
            System.out.println("Fitness of Individual " + (i + 1) + ": " + population.getIndividuals()[i].getFitness());
        }
    }

    private void mutateIndividual(Scanner input) {
        System.out.print("Enter the individual number to mutate (1-" + population.getIndividuals().length + "): ");
        int individualNumber = input.nextInt();
        if (individualNumber >= 1 && individualNumber <= population.getIndividuals().length) {
            GeneticOperations.mutate(population.getIndividuals()[individualNumber - 1]);
            System.out.println("Mutation complete. New fitness: "
                    + population.getIndividuals()[individualNumber - 1].getFitness());
        } else {
            System.out.println("Invalid index.");
        }
    }

    private void performCrossover(Scanner input) {
        System.out.print("Enter the first individual number (1-" + population.getIndividuals().length + "): ");
        int parent1Index = input.nextInt() - 1;
        System.out.print("Enter the second individual number (1-" + population.getIndividuals().length + "): ");
        int parent2Index = input.nextInt() - 1;

        if (parent1Index >= 0 && parent1Index < population.getIndividuals().length &&
                parent2Index >= 0 && parent2Index < population.getIndividuals().length) {
            Individual parent1 = population.getIndividuals()[parent1Index];
            Individual parent2 = population.getIndividuals()[parent2Index];
            Individual[] offspring = GeneticOperations.crossover(parent1, parent2);

            System.out.println("Crossover complete. Offspring fitness levels:");
            System.out.println("Offspring 1: " + offspring[0].getFitness());
            System.out.println("Offspring 2: " + offspring[1].getFitness());
        } else {
            System.out.println("Invalid individual numbers.");
        }
    }

    private void evolvePopulation() {
        evolution.evolve();
        System.out.println("Population evolved!");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the population size: ");
        int populationSize = input.nextInt();

        System.out.print("Enter the chromosome length: ");
        int chromosomeLength = input.nextInt();

        Population population = new Population(populationSize, chromosomeLength);
        PopulationEvolution evolution = new PopulationEvolution(population);
        PopulationUI ui = new PopulationUI(evolution, population);
        ui.displayMenu();

        input.close();
    }
}
