#!/usr/bin/env bash

grep 'model name' /proc/cpuinfo | uniq | head -n 1
grep 'MemTotal' /proc/meminfo | uniq | head -n 1

ASSIGNMENT_NAME='Lab05'
GCC_FLAGS='-g -O3 -Wall -std=c++17'
PERF_STAT_EVENTS='task-clock,branches,branch-misses,L1-dcache-loads,L1-dcache-load-misses'
TIME_FORMAT=$'%U\t%S\t%e'

SOURCE_FILE="${ASSIGNMENT_NAME}.cpp"
EXECUTABLE="${ASSIGNMENT_NAME}"
OPENMP_EXECUTABLE="${ASSIGNMENT_NAME}_omp"

repeat() {
    TRIALS=$1
    shift
    for TRIAL_NUM in $(seq 1 "${TRIALS}"); do
        echo "Running $@... (${TRIAL_NUM} of ${TRIALS})"
        "$@"
    done
}

# Normal executable
cpplint.py "${SOURCE_FILE}"
if ! g++ ${GCC_FLAGS} "${SOURCE_FILE}" -o "${EXECUTABLE}"; then
    exit $?
fi

repeat 2 env time -f "${TIME_FORMAT}" "./${EXECUTABLE}" 2> "${EXECUTABLE}.timing"

# OpenMP executable
if ! g++ ${GCC_FLAGS} -fopenmp "${SOURCE_FILE}" -o "${OPENMP_EXECUTABLE}"; then
    exit $?
fi

for THREADS in {1..8}; do
    repeat 2 env "OMP_NUM_THREADS=${THREADS}" time -f "${TIME_FORMAT}" "./${OPENMP_EXECUTABLE}" 2> "${OPENMP_EXECUTABLE}_${THREADS}.timing"
done

echo "${ASSIGNMENT_NAME} done"
