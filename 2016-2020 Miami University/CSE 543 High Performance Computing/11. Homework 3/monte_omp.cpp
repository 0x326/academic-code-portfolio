// Created by John Meyer

#include <iostream>
#include <iomanip>
#include <cmath>
#include "omp.h"

#define DARTS_NUM 1000000

struct drand48_data state;
#pragma omp threadprivate(state)

double monteCarloIntegration(const unsigned int n) {
    double pi_sum = 0;
#pragma omp parallel for default(none) shared(n) reduction(+:pi_sum)
    for (unsigned int i = 0; i < n; i++) {
        int in_circle_tally = 0;
#pragma omp parallel for default(none) reduction(+:in_circle_tally)
        for (unsigned int j = 0; j < DARTS_NUM; j++) {
            // Generate uniform random numbers
            double x, y;
            drand48_r(&state, &x);
            drand48_r(&state, &y);

            // Sampling [0, 1) is equivalent to sampling [-1, 1)
            // Ignoring transform to [-1, 1)
            // x = 2 * x - 1;
            // y = 2 * y - 1;

            const double r = sqrt(x * x + y * y);
            if (r <= 1) {
                in_circle_tally++;
            }
        }

        const double pi = 4.0 * in_circle_tally / DARTS_NUM;
        pi_sum += pi;
    }

    const double pi_average = pi_sum / n;
    return pi_average;
}

int main(const int argc, const char *argv[]) {
    // Parse args
    if (argc != 3) {
        std::cerr << "Usage: " << argv[0] << " N SEED" << std::endl;
        std::cerr << "  N: How many times to sample pi" << std::endl;
        std::cerr << "  SEED: The seed to use for the random number generator" << std::endl;
        return 1;
    }
    const unsigned int n = std::stoi(argv[1]);
    const unsigned int seed = std::stoi(argv[2]);

#pragma omp parallel default(none) shared(seed)
    srand48_r(seed + 23 * omp_get_thread_num(), &state);

    const double pi = monteCarloIntegration(n);
    std::cout << std::setprecision(12) << pi << std::endl;

    return 0;
}
