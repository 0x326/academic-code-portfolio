#!/usr/bin/env bash

print_usage() {
    echo "Usage: $0 [run|export]"
    exit 1
}

ASSIGNMENT_NAME='Homework02'
GCC_FLAGS='-O3 -Wall -std=c++17'
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

copy() {
    FILE=$1

    echo "${FILE}"
    awk '{OFS="\t"; print $1 + $2, $3}' "${FILE}" | xclip -selection clipboard
    read -rp 'Press ENTER to continue ...'
}

run_test() {
    grep 'model name' /proc/cpuinfo | uniq | head -n 1
    grep 'MemTotal' /proc/meminfo | uniq | head -n 1

    # Serial executables
    echo 'Compiling box-muller.cpp'
    cpplint.py box-muller.cpp
    if ! g++ ${GCC_FLAGS} box-muller.cpp -o box-muller; then
        exit $?
    fi

    echo 'Compiling marsaglia.cpp'
    cpplint.py marsaglia.cpp
    if ! g++ ${GCC_FLAGS} marsaglia.cpp -o marsaglia; then
        exit $?
    fi

    for TRIAL_NUM in {1..5}; do
        echo "Running box-muller trial ${TRIAL_NUM} ..."
        env time -f "${TIME_FORMAT}" ./box-muller "$(seed)"
    done 2> box-muller.timing

    for TRIAL_NUM in {1..5}; do
        echo "Running marsaglia trial ${TRIAL_NUM} ..."
        env time -f "${TIME_FORMAT}" ./marsaglia "$(seed)"
    done 2> marsaglia.timing

    # Parallel executables
    echo 'Compiling box-muller_omp.cpp'
    cpplint.py box-muller_omp.cpp
    if ! g++ ${GCC_FLAGS} -fopenmp box-muller_omp.cpp -o box-muller_omp; then
        exit $?
    fi

    echo 'Compiling marsaglia_omp.cpp'
    cpplint.py marsaglia_omp.cpp
    if ! g++ ${GCC_FLAGS} -fopenmp marsaglia_omp.cpp -o marsaglia_omp; then
        exit $?
    fi

    for THREADS in {1..8}; do
        for TRIAL_NUM in {1..5}; do
            echo "Running box-muller OMP ${THREADS}-threads trial ${TRIAL_NUM} ..."
            env "OMP_NUM_THREADS=${THREADS}" time -f "${TIME_FORMAT}" ./box-muller_omp "$(seed)"
        done 2> "box-muller_omp_${THREADS}.timing"

        for TRIAL_NUM in {1..5}; do
            echo "Running marsaglia OMP ${THREADS}-threads trial ${TRIAL_NUM} ..."
            env "OMP_NUM_THREADS=${THREADS}" time -f "${TIME_FORMAT}" ./marsaglia_omp "$(seed)"
        done 2> "marsaglia_omp_${THREADS}.timing"
    done

    echo "${ASSIGNMENT_NAME} done"
}

export_results() {
    echo 'Exporting results'
    echo
    copy box-muller.timing
    copy marsaglia.timing
    for THREADS in {1..8}; do
        copy "box-muller_omp_${THREADS}.timing"
    done
    for THREADS in {1..8}; do
        copy "marsaglia_omp_${THREADS}.timing"
    done
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
