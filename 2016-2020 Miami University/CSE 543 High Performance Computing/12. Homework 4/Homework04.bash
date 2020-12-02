#!/usr/bin/env bash

print_usage() {
    echo "Usage: $0 [run|export]"
    exit 1
}

# ASSIGNMENT_NAME='Homework02'
GCC_FLAGS=(-O3 -Wall '-std=c++17')
# PERF_STAT_EVENTS='task-clock,branches,branch-misses,L1-dcache-loads,L1-dcache-load-misses'
TIME_FORMAT=$'%U\t%S\t%e'

repeat() {
    TRIALS=$1
    shift
    for TRIAL_NUM in $(seq 1 "${TRIALS}"); do
        echo "Running $*... (${TRIAL_NUM} of ${TRIALS})"
        "$@"
    done
}

seed() {
    RANDOM_1=$RANDOM
    RANDOM_2=$RANDOM
    if [ $RANDOM_1 -eq 0 ]; then
        RANDOM_1=$((RANDOM_1 + 1))
    fi
    if [ $RANDOM_2 -eq 0 ]; then
        RANDOM_2=$((RANDOM_2 + 1))
    fi

    echo "$((RANDOM_1 * RANDOM_2))"
}

run_test() {
    grep 'model name' /proc/cpuinfo | uniq | head -n 1
    grep 'MemTotal' /proc/meminfo | uniq | head -n 1

    # Serial executable
    echo 'Compiling fourier_mpi.cpp'
    cpplint.py fourier_serial.cpp
    if ! mpic++ "${GCC_FLAGS[@]}" fourier_serial.cpp -o fourier_serial; then
        exit $?
    fi

    # MPI executable
    echo 'Compiling fourier_mpi.cpp'
    cpplint.py fourier_mpi.cpp
    if ! mpic++ "${GCC_FLAGS[@]}" fourier_mpi.cpp -o fourier_mpi; then
        exit $?
    fi

    echo 'Running preliminary test with fourier_serial ...'
    if ! ./fourier_serial 15; then
        exit $?
    fi
    echo 'Running preliminary test with fourier_mpi ...'
    if ! mpirun -n "$(nproc)" --use-hwthread-cpus ./fourier_mpi 15; then
        exit $?
    fi

    for TASKS in {1..8}; do
        echo "Running tests with ${TASKS} tasks ..."
        for TRIAL_NUM in {1..5}; do
            echo '{'
            if ((TASKS == 1)); then
                env time -f "${TIME_FORMAT}" ./fourier_serial 75000
            else
                mpirun -n "${TASKS}" --use-hwthread-cpus ./fourier_mpi 75000
            fi
            echo '}'
        done > "fourier_${TASKS}.out" 2> "fourier_${TASKS}.timing"
    done

    echo 'Done'
}

export_results() {
    echo 'Exporting results'
    for TASKS in {1..8}; do
        grep '^[0-9]' "fourier_${TASKS}.timing" | awk '{OFS="\t"; print $2 + $3, $1}'
    done | xclip -selection clipboard
}

if (( $# < 1 )); then
    print_usage
fi
MODE=$1

if [[ ${MODE} == 'run' ]]; then
    run_test
elif [[ ${MODE} == 'export' ]]; then
    export_results
else
    print_usage
fi
