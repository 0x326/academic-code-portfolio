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

    # Serial executables
    echo 'Compiling monte_omp.cpp'
    cpplint.py monte_omp.cpp
    if ! g++ "${GCC_FLAGS[@]}" -fopenmp monte_omp.cpp -o monte_omp; then
        exit $?
    fi

    echo 'Running preliminary test (Expecting 3.141775)'
    OMP_NUM_THREADS=4 ./monte_omp 40 1234567

    for THREADS in {1..8}; do
        echo "Running tests with ${THREADS} threads ..."
        for TRIAL_NUM in {1..5}; do
            # echo "Running monte_omp ${THREADS}-threads trial ${TRIAL_NUM} ..."
            env "OMP_NUM_THREADS=${THREADS}" time -f "${TIME_FORMAT}" ./monte_omp 10000 "$(seed)"
        done > "monte_omp_${THREADS}.out" 2> "monte_omp_${THREADS}.timing"
    done

    echo 'Done'
}

export_results() {
    echo 'Exporting results'
    for THREADS in {1..8}; do
        paste "monte_omp_${THREADS}.timing" "monte_omp_${THREADS}.out" \
            | awk '{OFS="\t"; print $3, $1 + $2, $4}'
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
