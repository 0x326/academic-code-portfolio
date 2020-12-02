//
// Created by john on 11/14/20.
//

#include <iostream>
#include <mpi.h>
#include <cmath>
#include <unistd.h>

#ifdef DEBUG_INPUT
#define DEBUG
#endif

#ifdef DEBUG_OUTPUT
#define DEBUG
#endif

#define MANAGER 0
#define STOP_COMMAND -1

#define COMMAND_TAG 1

#define REAL 0
#define IMAGINARY 1

void initializeMPI(int &argc, char *argv[], int &rank, int &total_ranks) {
    int error_code;

    error_code = MPI_Init(&argc, &argv);
    if (error_code != 0) {
        std::cerr << "Error during MPI_Init" << std::endl;
        exit(error_code);
    }

    error_code = MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    if (error_code != 0) {
        std::cerr << "Error during MPI_Comm_rank" << std::endl;
        exit(error_code);
    }

    error_code = MPI_Comm_size(MPI_COMM_WORLD, &total_ranks);
    if (error_code != 0) {
        std::cerr << "Error during MPI_Comm_size" << std::endl;
        exit(error_code);
    }
}

void initializeInputArray(const int n, double *input_real, double *input_imag) {
    int rank;
    int total_ranks;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &total_ranks);

    if (rank == MANAGER) {
        // Manager
        bool worker_stopped[total_ranks];
        worker_stopped[0] = true;  // The manager is not a worker
        for (int worker_rank = 1; worker_rank < total_ranks; worker_rank++) {
            worker_stopped[worker_rank] = false;
        }

        // Send initial x-values
        int x = 0;
        int x_response_tally = 0;
        do {
            MPI_Send(&x, 1, MPI_INT, (x % (total_ranks - 1)) + 1, COMMAND_TAG, MPI_COMM_WORLD);
            x++;
        } while (x < 2 * total_ranks && x < n); // Send an extra set of x-values to the workers to make sure they can stay busy during delays

        // Receive y-values
        while (x_response_tally < n) {
            double y_real_imag[2];
            MPI_Status status;
            MPI_Recv(y_real_imag, 2, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            x_response_tally++;
#ifdef DEBUG_INPUT
            std::cerr << "Rank " << rank << ": Got (" << y_real_imag[REAL] << ") + i(" << y_real_imag[IMAGINARY] << ")" << std::endl;

            int message_count;
            MPI_Get_count(&status, MPI_DOUBLE, &message_count);
            if (message_count == MPI_UNDEFINED) {
                std::cerr << "Rank " << rank << ": Message size is MPI_UNDEFINED" << std::endl;
            }
#endif

            // Send worker a new x-value
            if (x < n) {
                MPI_Send(&x, 1, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD);
                x++;
            } else {
                if (!worker_stopped[status.MPI_SOURCE]) {
#ifdef DEBUG_INPUT
                    std::cerr << "Rank " << rank << ": Sending STOP_COMMAND" << std::endl;
#endif
                    const int command = STOP_COMMAND;
                    MPI_Send(&command, 1, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD);
                    worker_stopped[status.MPI_SOURCE] = true;
                }
            }

            // Save y-value
            const int corresponding_x = status.MPI_TAG;
            input_real[corresponding_x] = y_real_imag[REAL];
            input_imag[corresponding_x] = y_real_imag[IMAGINARY];
        }
    } else {
        // Worker
        const double a = 2;

        do {
            int x;
            // Get x-value
            MPI_Recv(&x, 1, MPI_INT, MANAGER, MPI_ANY_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            if (x == STOP_COMMAND) {
#ifdef DEBUG_INPUT
                std::cerr << "Rank " << rank << ": Got STOP_COMMAND" << std::endl;
#endif
                break;
            }

            // Compute input function: sinc(x) = sin(ax)/x
            const double y_real_imag[2] = {x != 0 ? sin(a * x) / x : a, 0.0} ;
#ifdef DEBUG_INPUT
            std::cerr << "Rank " << rank << ": Got x = " << x << ". Computed: (" << y_real_imag[REAL] << ") + i(" << y_real_imag[IMAGINARY] << ")" << std::endl;
#endif

            // Send y-value
            MPI_Send(y_real_imag, 2, MPI_DOUBLE, MANAGER, x, MPI_COMM_WORLD);
        } while (true);
    }

    // Broadcast input array to workers
    MPI_Bcast(input_real, n, MPI_DOUBLE, MANAGER, MPI_COMM_WORLD);
    MPI_Bcast(input_imag, n, MPI_DOUBLE, MANAGER, MPI_COMM_WORLD);
}

void initializeOutputArray(const int n, double *output_real, double *output_imag) {
    // Initialize output function: Evaluated at a later point
    for (int x = 0; x < n; x++) {
        output_real[x] = 0.0;
        output_imag[x] = 0.0;
    }
}

// See https://youtu.be/nl9TZanwbBk
void computeFourierTransform(const int n, const double input_real[], const double input_imag[], double output_real[], double output_imag[]) {
    int rank;
    int total_ranks;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &total_ranks);

    if (rank == MANAGER) {
        // Manager
        bool worker_stopped[total_ranks];
        worker_stopped[0] = true;  // The manager is not a worker
        for (int worker_rank = 1; worker_rank < total_ranks; worker_rank++) {
            worker_stopped[worker_rank] = false;
        }

        // Send initial k-values
        int k = 0;
        int k_response_tally = 0;
        do {
            MPI_Send(&k, 1, MPI_INT, (k % (total_ranks - 1)) + 1, 0, MPI_COMM_WORLD);
            k++;
        } while (k < 2 * total_ranks && k < n); // Send an extra set of k-values to the workers to make sure they can stay busy during delays

        // Receive y-values
        while (k_response_tally < n) {
            double output_real_imag[2];
            MPI_Status status;
            MPI_Recv(output_real_imag, 2, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            k_response_tally++;
#ifdef DEBUG_OUTPUT
            int message_count;
            MPI_Get_count(&status, MPI_DOUBLE, &message_count);
            if (message_count == MPI_UNDEFINED) {
                std::cerr << "Rank " << rank << ": Message size is MPI_UNDEFINED" << std::endl;
            }
#endif

            // Send worker a new k-value
            if (k < n) {
                MPI_Send(&k, 1, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD);
                k++;
            } else {
                if (!worker_stopped[status.MPI_SOURCE]) {
#ifdef DEBUG_OUTPUT
                    std::cerr << "Rank " << rank << ": Sending STOP_COMMAND" << std::endl;
#endif
                    const int command = STOP_COMMAND;
                    MPI_Send(&command, 1, MPI_INT, status.MPI_SOURCE, 0, MPI_COMM_WORLD);
                    worker_stopped[status.MPI_SOURCE] = true;
                }
            }

            // Save y-value
            const int corresponding_k = status.MPI_TAG;
            output_real[corresponding_k] = output_real_imag[REAL];
            output_imag[corresponding_k] = output_real_imag[IMAGINARY];
        }
    } else {
        // Worker
        do {
            int k;
            // Get x-value
            MPI_Recv(&k, 1, MPI_INT, MANAGER, MPI_ANY_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            if (k == STOP_COMMAND) {
#ifdef DEBUG_OUTPUT
                std::cerr << "Rank " << rank << ": Got STOP_COMMAND" << std::endl;
#endif
                break;
            }

            // Compute output function:
            // DFT(n)
            //   = Sigma^{n-1}_{j=0}((a + bi) * e^{-i * 2(PI) * j * k / n})
            //     Let Θ = -2(PI) * j * k / n
            //   = Sigma^{n-1}_{j=0}((a + bi) * (cos(Θ) + i sin(Θ))
            //   = Sigma^{n-1}_{j=0}([a cos(Θ) - b sin(Θ)] + i[a sin(Θ) + b cos(Θ)])
            double output_real_imag[2] = {0, 0};
            const double theta_partial = -2 * M_PI * k / n;
            for (int j = 0; j < n; j++) {
                const double theta = theta_partial * j;
                const double sin_theta = sin(theta);
                const double cos_theta = cos(theta);

                // TODO: Check this!
                output_real_imag[REAL] += input_real[j] * cos_theta - input_imag[j] * sin_theta;
                output_real_imag[IMAGINARY] += input_real[j] * sin_theta + input_imag[j] * cos_theta;
            }

            // Send y-value
            MPI_Send(output_real_imag, 2, MPI_DOUBLE, MANAGER, k, MPI_COMM_WORLD);
        } while (true);
    }
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        std::cerr << argv[0] << " NUMBER_OF_SAMPLES" << std::endl;
        return 1;
    }

    int rank;
    int total_ranks;
    initializeMPI(argc, argv, rank, total_ranks);

    if (total_ranks < 2) {
        std::cerr << "Must be run with at least two ranks (processes)" << std::endl;
        MPI_Finalize();
        return 1;
    }

    const double mpi_start_time = MPI_Wtime();
    const clock_t start_time = clock();

#ifdef DEBUG
    if (rank == MANAGER) {
        std::cerr << "Running with " << total_ranks << " ranks" << std::endl;
    }

    std::cerr << "Rank " << rank << " has PID " << getpid() << std::endl;
    sleep(30);
#endif

    // Parse args
    const int n = std::stoi(argv[1]);

    // Initialize input arrays
#ifdef DEBUG
    if (rank == MANAGER) {
        std::cerr << "\n==================== Input arrays ====================\n" << std::endl;
    }
#endif

    auto *input_real = new double[n];
    auto *input_imag = new double[n];
    initializeInputArray(n, input_real, input_imag);
    // const double a = 2;
    // input_real[0] = a;
    // input_imag[0] = 0;
    // for (int x = 1; x < n; x++) {
    //     input_real[x] = sin(a * x) / x;
    //     input_imag[x] = 0;
    // }

#ifdef DEBUG
    if (rank == MANAGER) {
        for (int x = 0; x < n; x++) {
            std::cerr << x << '\t' << input_real[x] << '\t' << input_imag[x] << std::endl;
        }
    }
    MPI_Barrier(MPI_COMM_WORLD);
    if (rank == 1) {
        std::cerr << "\n--------------------\n" << std::endl;
        for (int x = 0; x < n; x++) {
            std::cerr << x << '\t' << input_real[x] << '\t' << input_imag[x] << std::endl;
        }
    }
    MPI_Barrier(MPI_COMM_WORLD);

    std::cerr << "--------------------" << std::endl;
#endif

    // Initialize output arrays
    double *output_real = nullptr;
    double *output_imag = nullptr;

    if (rank == MANAGER) {
        output_real = new double[n];
        output_imag = new double[n];
        initializeOutputArray(n, output_real, output_imag);
    }

    // Computer Discrete Fourier Transform
#ifdef DEBUG
    if (rank == MANAGER) {
        std::cerr << "\n==================== Discrete Fourier Transform ====================\n" << std::endl;
    }
#endif
    computeFourierTransform(n, input_real, input_imag, output_real, output_imag);

    const clock_t end_time = clock();
    const double mpi_end_time = MPI_Wtime();
    const double process_cpu_time = static_cast<double>(end_time - start_time) / static_cast<double>(CLOCKS_PER_SEC);
    double total_cpu_time;
    MPI_Reduce(&process_cpu_time, &total_cpu_time, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
    const double elapsed_time = mpi_end_time - mpi_start_time;

    if (rank == MANAGER && output_real != nullptr) {
        for (int k = 0; k < n; k++) {
            std::cout << k << '\t' << output_real[k] << '\t' << output_imag[k] << std::endl;
        }
    }

    if (rank == MANAGER) {
        std::cerr << total_cpu_time << '\t' << "0" << '\t' << elapsed_time << std::endl;
    }

    MPI_Finalize();
    return 0;
}
