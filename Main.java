import java.util.*;

// Abstract class for all organisms
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

// Class representing an individual organism
class Individual extends Organism {
    protected int[] chromosome;
    protected int fitness;

    public Individual(int[] chromosome) {
        super("Individual");
        this.chromosome = chromosome;
    }

    public int[] getChromosome() {
        return this.chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public int getFitness() {
        return this.fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}

// Interface for genetic operations
interface GeneticOperation {
    void perform(Individual individual);
}

// Concrete class for fitness calculation
class FitnessCalculation implements GeneticOperation {
    @Override
    public void perform(Individual individual) {
        int fitness = 0;
        for (int gene : individual.getChromosome()) {
            if (gene == 1)
                fitness++;
        }
        individual.setFitness(fitness);
    }
}

// Concrete class for mutation
class Mutation implements GeneticOperation {
    @Override
    public void perform(Individual individual) {
        int[] chromosome = individual.getChromosome();
        int mutationPoint = (int) (Math.random() * chromosome.length);
        chromosome[mutationPoint] = chromosome[mutationPoint] == 1 ? 0 : 1;
    }
}

// Concrete class for crossover
class Crossover implements GeneticOperation {
    private final Individual parent1;
    private final Individual parent2;

    public Crossover(Individual parent1, Individual parent2) {
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    @Override
    public void perform(Individual individual) {
        int length = parent1.getChromosome().length;
        int crossoverPoint = (int) (Math.random() * length);
        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                individual.getChromosome()[i] = parent1.getChromosome()[i];
            } else {
                individual.getChromosome()[i] = parent2.getChromosome()[i];
            }
        }
    }
}

// Class that manages genetic operations
class GeneticOperations {
    private final List<GeneticOperation> operations;

    public GeneticOperations(List<GeneticOperation> operations) {
        this.operations = operations;
    }

    public void applyOperations(Individual individual) {
        for (GeneticOperation operation : operations) {
            operation.perform(individual);
        }
    }
}

// Class representing a population of organisms
class Population {
    protected final int populationSize;
    protected final Individual[] individuals;
    private final GeneticOperations geneticOperations;

    public Population(int populationSize, int chromosomeLength, GeneticOperations geneticOperations) {
        this.populationSize = populationSize;
        this.individuals = new Individual[populationSize];
        this.geneticOperations = geneticOperations;
        initializePopulation(chromosomeLength);
    }

    private void initializePopulation(int chromosomeLength) {
        for (int i = 0; i < populationSize; i++) {
            int[] chromosome = new int[chromosomeLength];
            for (int j = 0; j < chromosomeLength; j++) {
                chromosome[j] = (Math.random() > 0.5) ? 1 : 0;
            }
            individuals[i] = new Individual(chromosome);
            geneticOperations.applyOperations(individuals[i]); // Apply operations on initialization
        }
    }

    public Individual[] getIndividuals() {
        return this.individuals;
    }

    public GeneticOperations getGeneticOperations() {
        return this.geneticOperations;
    }
}

// Class for evolving the population
class PopulationEvolution {
    private final Population population;

    public PopulationEvolution(Population population) {
        this.population = population;
    }

    public void evolve() {
        for (Individual individual : population.getIndividuals()) {
            population.getGeneticOperations().applyOperations(individual);
        }
    }
}

// Class for the user interface
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
                    break;
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
            GeneticOperations geneticOperations = population.getGeneticOperations();
            geneticOperations.applyOperations(population.getIndividuals()[individualNumber - 1]);
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
            Crossover crossover = new Crossover(parent1, parent2);
            crossover.perform(parent1); // Modify parent1 as the offspring
            crossover.perform(parent2); // Modify parent2 as the offspring

            System.out.println("Crossover complete. Offspring fitness levels:");
            System.out.println("Offspring 1: " + parent1.getFitness());
            System.out.println("Offspring 2: " + parent2.getFitness());
        } else {
            System.out.println("Invalid individual numbers.");
        }
    }

    private void evolvePopulation() {
        evolution.evolve();
        System.out.println("Population evolved!");
    }
}

// Main class to run the program
public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the population size: ");
        int populationSize = input.nextInt();

        System.out.print("Enter the chromosome length: ");
        int chromosomeLength = input.nextInt();

        // Add operations to the list
        List<GeneticOperation> operations = new ArrayList<>();
        operations.add(new FitnessCalculation());
        operations.add(new Mutation()); // Add mutation to the operations list

        GeneticOperations geneticOperations = new GeneticOperations(operations);

        Population population = new Population(populationSize, chromosomeLength, geneticOperations);
        PopulationEvolution evolution = new PopulationEvolution(population);
        PopulationUI ui = new PopulationUI(evolution, population);
        ui.displayMenu();

        input.close();
    }
}
