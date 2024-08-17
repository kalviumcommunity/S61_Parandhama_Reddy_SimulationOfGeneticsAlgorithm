public class Main {

    static class Individual {
        int[] chromosome;
        int fitness;

        public Individual() {

        }

        public void calculateFitness() {
            fitness = 0;
            for (int i : this.chromosome) {
                if (i == 1) {
                    fitness++;
                }
            }
        }

        public void mutate() {
            int val = (int) (Math.random() * chromosome.length);
            chromosome[val] = (chromosome[val] == 1) ? 0 : 1;
        }
    }

    public static class Population {
        private final int populationSize;
        private final Individual[] individuals;

        public Population() {

        }

        public Individual selectParent() {
            int randomIndex = (int) (Math.random() * populationSize);
            return individuals[randomIndex];
        }

        public Individual[] crossover(Individual parent1, Individual parent2) {
            int length = parent1.chromosome.length;
            Individual[] offspring = new Individual[2];
            int crossoverPoint = (int) (Math.random() * length);

            offspring[0] = new Individual();
            offspring[1] = new Individual();

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