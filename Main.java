public class Main {

    static class Individual {
        int[] chromosome;
        int fitness;

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
        }
    }

    public static class Population {
        private final int populationSize;
        private final Individual[] individuals;

        public Population(int populationSize, int[] chromosomeLength) {
            this.populationSize = populationSize;
            individuals = new Individual[populationSize];

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

    public static void main(String[] args) {
        int[] chromosome1 = { 1, 0, 1, 0, 1 };
        int[] chromosome2 = { 0, 1, 0, 1, 0 };
        int[] chromosome3 = { 1, 1, 0, 0, 1 };

        Individual[] individuals = new Individual[3];

        individuals[0] = new Individual(chromosome1);
        individuals[1] = new Individual(chromosome2);
        individuals[2] = new Individual(chromosome3);

        for (Individual individual : individuals) {
            System.out.println("Fitness: " + individual.fitness);
        }
    }
}
