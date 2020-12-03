#!/usr/bin/env bash

print_usage() {
    echo "Usage: $0 [run|export]"
    exit 1
}

ASSIGNMENT_NAME='Lab06'
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

    cpplint.py workhorse.cpp
    if ! g++ ${GCC_FLAGS} workhorse.cpp -o workhorse; then
        exit "$?"
    fi

    cpplint.py workhorse_explicit.cpp
    if ! g++ ${GCC_FLAGS} -fopenmp workhorse_explicit.cpp -o workhorse_explicit; then
        exit "$?"
    fi

    echo 'Running preliminary workhorse test ...'
    ./workhorse 1000000

    echo "Running workhorse ..."
    repeat 2 env time -f "${TIME_FORMAT}" ./workhorse 40000000 2> workhorse.timing

    for THREADS in {1..8}; do
        echo "Running workhorse_explicit with ${THREADS} threads ..."
        repeat 2 env "OMP_NUM_THREADS=${THREADS}" time -f "${TIME_FORMAT}" ./workhorse_explicit 40000000 2> "workhorse_explicit_${THREADS}.timing"
    done

    echo "${ASSIGNMENT_NAME} done"
}

export_results() {
    echo 'Exporting results'
    echo
    copy workhorse.timing
    for THREADS in {1..8}; do
        copy "workhorse_explicit_${THREADS}.timing"
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
