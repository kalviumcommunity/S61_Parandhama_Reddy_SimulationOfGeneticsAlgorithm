import java.util.Scanner;

class Organism {
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
    private int[] chromosome;
    private int fitness;
    private static int totalMutations = 0;

    public Individual() {
        super("Basic Individual");
        this.chromosome = new int[10];
        for (int i = 0; i < this.chromosome.length; i++) {
            this.chromosome[i] = (Math.random() > 0.5) ? 1 : 0;
        }
        this.calculateFitness();
    }

    public Individual(int[] chromosome) {
        super("Basic Individual");
        this.chromosome = chromosome;
        this.calculateFitness();
    }

    public int[] getChromosome() {
        return this.chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
        this.calculateFitness();
    }

    public int getFitness() {
        return this.fitness;
    }

    public void calculateFitness() {
        this.fitness = 0;
        for (int i : this.chromosome) {
            if (i == 1) {
                this.fitness++;
            }
        }
    }

    public void mutate() {
        int val = (int) (Math.random() * this.chromosome.length);
        this.chromosome[val] = (this.chromosome[val] == 1) ? 0 : 1;
        this.calculateFitness();
        totalMutations++;
    }

    public static int getTotalMutations() {
        return totalMutations;
    }
}


class Population {
    protected final int populationSize;
    protected final Individual[] individuals;
    private static int totalIndividuals = 0;

    public Population(int populationSize, int chromosomeLength) {
        this.populationSize = populationSize;
        this.individuals = new Individual[populationSize];

        for (int i = 0; i < populationSize; i++) {
            int[] chromosome = new int[chromosomeLength];
            for (int j = 0; j < chromosomeLength; j++) {
                chromosome[j] = (Math.random() > 0.5) ? 1 : 0;
            }
            individuals[i] = new Individual(chromosome);
            totalIndividuals++;
        }
    }

    public Individual[] getIndividuals() {
        return this.individuals;
    }

    public static int getTotalIndividuals() {
        return totalIndividuals;
    }

    public Individual selectParent() {
        int randomIndex = (int) (Math.random() * populationSize);
        return individuals[randomIndex];
    }


    public Individual[] crossover(Individual parent1, Individual parent2) {
        int length = parent1.getChromosome().length;
        Individual[] offspring = new Individual[2];
        int crossoverPoint = (int) (Math.random() * length);

        offspring[0] = new Individual(new int[length]);
        offspring[1] = new Individual(new int[length]);

        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                offspring[0].getChromosome()[i] = parent1.getChromosome()[i];
                offspring[1].getChromosome()[i] = parent2.getChromosome()[i];
            } else {
                offspring[0].getChromosome()[i] = parent2.getChromosome()[i];
                offspring[1].getChromosome()[i] = parent1.getChromosome()[i];
            }
        }

        offspring[0].calculateFitness();
        offspring[1].calculateFitness();

        return offspring;
    }
}


class EvolvedPopulation extends Population {

    public EvolvedPopulation(int populationSize, int chromosomeLength) {
        super(populationSize, chromosomeLength);
    }

    public void evolve() {
        System.out.println("Evolving population...");
        for (Individual individual : this.individuals) {
            individual.mutate();
        }
    }
    
}

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the population size: ");
        int populationSize = input.nextInt();

        System.out.print("Enter the chromosome length: ");
        int chromosomeLength = input.nextInt();

        EvolvedPopulation population = new EvolvedPopulation(populationSize, chromosomeLength);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. View all individual fitness levels");
            System.out.println("2. Mutate an individual");
            System.out.println("3. Perform crossover between two individuals");
            System.out.println("4. Evolve the population");
            System.out.println("5. View total individuals created");
            System.out.println("6. View total mutations performed");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    for (int i = 0; i < populationSize; i++) {
                        System.out.println("Fitness of Individual " + (i + 1) + ": "
                                + population.getIndividuals()[i].getFitness());
                    }
                    break;

                case 2:
                    System.out.print("Enter the individual number to mutate (1-" + populationSize + "): ");
                    int individualNumber = input.nextInt();
                    if (individualNumber >= 1 && individualNumber <= populationSize) {
                        population.getIndividuals()[individualNumber - 1].mutate();
                        System.out.println("Mutation complete. New fitness: "
                                + population.getIndividuals()[individualNumber - 1].getFitness());
                    } else {
                        System.out.println("Invalid individual number.");
                    }
                    break;

                case 3:
                    System.out.print("Enter the first individual number (1-" + populationSize + "): ");
                    int parent1Index = input.nextInt() - 1;
                    System.out.print("Enter the second individual number (1-" + populationSize + "): ");
                    int parent2Index = input.nextInt() - 1;

                    if (parent1Index >= 0 && parent1Index < populationSize && parent2Index >= 0
                            && parent2Index < populationSize) {
                        Individual parent1 = population.getIndividuals()[parent1Index];
                        Individual parent2 = population.getIndividuals()[parent2Index];
                        Individual[] offspring = population.crossover(parent1, parent2);

                        System.out.println("Crossover complete. Offspring fitness levels:");
                        System.out.println("Offspring 1: " + offspring[0].getFitness());
                        System.out.println("Offspring 2: " + offspring[1].getFitness());
                    } else {
                        System.out.println("Invalid individual numbers.");
                    }
                    break;

                case 4:
                    population.evolve();
                    System.out.println("Population evolved!");
                    break;

                case 5:
                    System.out.println("Total individuals created: " + Population.getTotalIndividuals());
                    break;

                case 6:
                    System.out.println("Total mutations performed: " + Individual.getTotalMutations());
                    break;

                case 7:
                    exit = true;
                    System.out.println("Exiting the program.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        input.close();
    }
}
