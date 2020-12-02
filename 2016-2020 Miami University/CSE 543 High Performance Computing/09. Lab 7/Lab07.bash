#!/usr/bin/env bash

print_usage() {
    echo "Usage: $0 [run|export|extra]"
    exit 1
}

# ASSIGNMENT_NAME='Lab06'
GCC_FLAGS='-O3 -Wall -std=c++17'
PERF_STAT_EVENTS='task-clock,branches,branch-misses,L1-dcache-loads,L1-dcache-load-misses'
TIME_FORMAT=$'%U\t%S\t%e'

repeat() {
    TRIALS=$1
    shift
    for TRIAL_NUM in $(seq 1 "${TRIALS}"); do
        echo "Running $*... (${TRIAL_NUM} of ${TRIALS})"
        "$@"
    done
}

copy() {
    FILE=$1

    echo "${FILE}"
    awk '{OFS="\t"; print $3, $1 + $2}' "${FILE}" | xclip -selection clipboard
    read -rp 'Press ENTER to continue ...'
}

run_test() {
    echo 'Running test'
    echo
    grep 'model name' /proc/cpuinfo | uniq | head -n 1
    grep 'MemTotal' /proc/meminfo | uniq | head -n 1

    cpplint.py workhorse_for.cpp
    if ! g++ ${GCC_FLAGS} workhorse_for.cpp -o workhorse_for; then
        exit "$?"
    fi

    cpplint.py workhorse_for_omp.cpp
    if ! g++ ${GCC_FLAGS} -fopenmp workhorse_for_omp.cpp -o workhorse_for_omp; then
        exit "$?"
    fi

    echo 'Running preliminary workhorse_for test ...'
    ./workhorse_for 1000000

    echo 'Running preliminary workhorse_for_omp test ...'
    ./workhorse_for_omp 1000000

    echo 'Running workhorse_for ...'
    repeat 2 env time -f "${TIME_FORMAT}" ./workhorse_for 40000000 2> workhorse_for.timing

    for THREADS in {1..8}; do
        echo "Running workhorse_explicit with ${THREADS} threads ..."
        repeat 2 env "OMP_NUM_THREADS=${THREADS}" time -f "${TIME_FORMAT}" ./workhorse_for_omp 40000000 2> "workhorse_for_omp_${THREADS}.timing"
    done

    echo 'Done'
}

export_results() {
    echo 'Exporting results'
    echo
    copy workhorse_for.timing
    for THREADS in {1..8}; do
        copy "workhorse_for_omp_${THREADS}.timing"
    done
}

extra_analysis() {
    perf stat -e "${PERF_STAT_EVENTS}" ./workhorse 40000000 2> workhorse.perf_stat
    perf stat -e "${PERF_STAT_EVENTS}" ./workhorse_for 40000000 2> workhorse_for.perf_stat
}

if (( $# < 1 )); then
    print_usage
fi
MODE=$1

if [[ ${MODE} == 'run' ]]; then
    run_test
elif [[ ${MODE} == 'export' ]]; then
    export_results
elif [[ ${MODE} == 'extra' ]]; then
    extra_analysis
else
    print_usage
fi
