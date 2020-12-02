//
// Created by john on 10/9/20.
//

#include <iostream>
#include <cstdlib>
#include <cmath>

#define RUNLENGTH 20000000

#define N 100

struct drand48_data state;

void doMarsaglia(double &sample_mean, double &sample_standard_deviation,
                 const double population_mean, const double population_standard_deviation) {
    double sample_sum = 0;
    double sample_sum_squared = 0;  // Not sum of squares. Equal to: pow(x1, 2) + pow(x2, 2) + ...

    for (int i = 0; i < RUNLENGTH / 2; i++) {
        double uniform_random_1, uniform_random_2;
        double random_1, random_2;
        double combined_random;

        do {
            // Generate uniform random numbers
            drand48_r(&state, &uniform_random_1);
            drand48_r(&state, &uniform_random_2);

            // Test uniform random numbers
            random_1 = 2 * uniform_random_1 - 1;
            random_2 = 2 * uniform_random_2 - 1;
            combined_random = random_1 * random_1 + random_2 * random_2;
        } while (combined_random > 1);

        // Transform into Normal distribution
        double gaussian_random_1 = random_1 * sqrt(-2 * log(combined_random) / combined_random);
        double gaussian_random_2 = random_2 * sqrt(-2 * log(combined_random) / combined_random);

        // Transform Gaussian distribution based on mu and sigma
        gaussian_random_1 = population_mean + population_standard_deviation * gaussian_random_1;
        gaussian_random_2 = population_mean + population_standard_deviation * gaussian_random_2;

        // Save numbers
        sample_sum += gaussian_random_1 + gaussian_random_2;
        sample_sum_squared += (gaussian_random_1 * gaussian_random_1) + (gaussian_random_2 * gaussian_random_2);
    }

    sample_mean = sample_sum / RUNLENGTH;
    // I believe we should be dividing by RUNLENGTH - 1
    sample_standard_deviation = sqrt(sample_sum_squared / RUNLENGTH - (sample_mean * sample_mean));
}

int main(const int argc, const char *argv[]) {
    // Parse args
    if (argc != 2) {
        std::cerr << "Usage: " << argv[0] << " SEED" << std::endl;
        std::cerr << "  SEED: The seed to use for the random number generator" << std::endl;
        return -1;
    }
    const int seed = std::stoi(argv[1]);

    srand48_r(seed, &state);

    const double population_mean = 42;
    const double population_standard_deviation = 5;
    double sample_mean, sample_standard_deviation;

    for (int i = 1; i <= N; i++) {
        doMarsaglia(sample_mean, sample_standard_deviation, population_mean, population_standard_deviation);
        std::cout << "Run " << i << ":"
                  << " sample_mean = " << sample_mean
                  << " sample_standard_deviation = " << sample_standard_deviation << std::endl;
    }

    return 0;
}
