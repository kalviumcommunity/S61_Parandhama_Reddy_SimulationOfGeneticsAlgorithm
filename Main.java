public class Main {

    static class Individual {
        int[] chromosome;
        int fitness;

        public Individual(int[] chromosome) {
            this.chromosome = chromosome;
            calculateFitness();
        }

        public void calculateFitness() {
            fitness = 0;
            for (int i : chromosome) {
                if (i == 1) {
                    fitness++;
                }
            }
        }

        public void mutate() {
            int val = (int) (Math.random() * chromosome.length);
            chromosome[val] = (chromosome[val] == 1) ? 0 : 1;
            calculateFitness();
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

    }
}