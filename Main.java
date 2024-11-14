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

    // Abstract method for organisms to perform an action
    public abstract void performAction();
}

// Class representing an individual organism
class Individual extends Organism {
    protected int[] chromosome;
    protected int fitness;

    public Individual(int[] chromosome) {
        super("Individual");
        this.chromosome = chromosome;
    }

    @Override
    public void performAction() {
        System.out.println("Individual is performing an action.");
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

// New subclass representing an Animal organism
class Animal extends Organism {
    public Animal() {
        super("Animal");
    }

    @Override
    public void performAction() {
        System.out.println("Animal is performing an action.");
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
    protected final Organism[] individuals;
    private final GeneticOperations geneticOperations;

    public Population(int populationSize, int chromosomeLength, GeneticOperations geneticOperations) {
        this.populationSize = populationSize;
        this.individuals = new Organism[populationSize];
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
            geneticOperations.applyOperations((Individual) individuals[i]); // Apply operations on initialization
        }
    }

    public Organism[] getIndividuals() {
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
        for (Organism organism : population.getIndividuals()) {
            population.getGeneticOperations().applyOperations((Individual) organism);
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
            System.out.println("1. View all organism types");
            System.out.println("2. Perform action for an organism");
            System.out.println("3. Evolve the population");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    viewOrganismTypes();
                    break;
                case 2:
                    performActionForOrganism(input);
                    break;
                case 3:
                    evolvePopulation();
                    break;
                case 4:
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

    private void viewOrganismTypes() {
        for (Organism organism : population.getIndividuals()) {
            System.out.println("Organism type: " + organism.getType());
        }
    }

    private void performActionForOrganism(Scanner input) {
        System.out
                .print("Enter the organism number to perform action (1-" + population.getIndividuals().length + "): ");
        int organismNumber = input.nextInt();
        if (organismNumber >= 1 && organismNumber <= population.getIndividuals().length) {
            Organism organism = population.getIndividuals()[organismNumber - 1];
            organism.performAction(); // Liskov Substitution: individual can be substituted with any subclass
        } else {
            System.out.println("Invalid organism index.");
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
