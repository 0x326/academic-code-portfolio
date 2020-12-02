#!/usr/bin/env -S bash -l

cat /proc/cpuinfo | grep 'model name' | uniq | head -n 1
cat /proc/meminfo | grep 'MemTotal' | uniq | head -n 1

ASSIGNMENT_NAME='Homework01'
GCC_FLAGS='-g -O3 -Wall -std=c++17'
PERF_STAT_EVENTS='task-clock,branches,branch-misses,L1-dcache-loads,L1-dcache-load-misses'

cpplint.py ackermann.cpp
g++ ${GCC_FLAGS} ackermann.cpp -o ackermann

if [[ $? -ne 0 ]]; then
    exit $?
fi

repeat() {
    TRIALS=$1
    shift
    for TRIAL_NUM in $(seq 1 "${TRIALS}"); do
        echo "Running $@... (${TRIAL_NUM} of ${TRIALS})"
        "$@"
    done
}

ulimit -s unlimited
repeat 6 perf stat -e "${PERF_STAT_EVENTS}" ./ackermann 16 2> "${ASSIGNMENT_NAME}.perfstat"

g++ ${GCC_FLAGS} -fprofile-generate ackermann.cpp -o ackermann_pgo
rm *.gcda

repeat 4 ./ackermann_pgo 9
repeat 4 ./ackermann_pgo 11
repeat 4 ./ackermann_pgo 13
repeat 4 ./ackermann_pgo 15

g++ ${GCC_FLAGS} -fprofile-use ackermann.cpp -o ackermann_pgo

repeat 6 perf stat -e "${PERF_STAT_EVENTS}" ./ackermann_pgo 16 2> "${ASSIGNMENT_NAME}_pgo.perfstat"
