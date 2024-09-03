import java.util.Scanner;

public class Main {

    static class Individual {
        int[] chromosome;
        int fitness;

        static int totalMutations = 0;

        public Individual(int[] chromosome) {
            this.chromosome = chromosome;
            this.calculateFitness();
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
    }

    public static class Population {
        private final int populationSize;
        private final Individual[] individuals;

        static int totalIndividuals = 0;

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

        public Individual selectParent() {
            int randomIndex = (int) (Math.random() * populationSize);
            return individuals[randomIndex];
        }

        public Individual[] crossover(Individual parent1, Individual parent2) {
            int length = parent1.chromosome.length;
            Individual[] offspring = new Individual[2];
            int crossoverPoint = (int) (Math.random() * length);

            offspring[0] = new Individual(new int[length]);
            offspring[1] = new Individual(new int[length]);

            for (int i = 0; i < length; i++) {
                if (i < crossoverPoint) {
                    offspring[0].chromosome[i] = parent1.chromosome[i];
                    offspring[1].chromosome[i] = parent2.chromosome[i];
                } else {
                    offspring[0].chromosome[i] = parent2.chromosome[i];
                    offspring[1].chromosome[i] = parent1.chromosome[i];
                }
            }

            offspring[0].calculateFitness();
            offspring[1].calculateFitness();

            return offspring;
        }
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the population size: ");
        int populationSize = input.nextInt();

        System.out.print("Enter the chromosome length: ");
        int chromosomeLength = input.nextInt();

        Population population = new Population(populationSize, chromosomeLength);

        for (int i = 0; i < populationSize; i++) {
            System.out.println("Fitness of Individual " + (i + 1) + ": " + population.individuals[i].fitness);
        }

        System.out.println("Total individuals created: " + Population.totalIndividuals);
        System.out.println("Total mutations performed: " + Individual.totalMutations);
    }
}
