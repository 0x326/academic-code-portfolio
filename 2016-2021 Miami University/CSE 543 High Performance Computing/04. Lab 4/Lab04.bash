#!/usr/bin/env bash

cat /proc/cpuinfo | grep 'model name' | uniq | head -n 1
cat /proc/meminfo | grep 'MemTotal' | uniq | head -n 1

LAB_NUM=Lab04
TIME_FORMAT=$'%U\t%S\t%e'
PERF_STAT_EVENTS='task-clock,branches,branch-misses,L1-dcache-load-misses'

cpplint.py unroll.cpp

g++ -g -O2 --std=c++17 -Wall unroll.cpp -o unroll
g++ -g -O2 --std=c++17 -funroll-loops -Wall unroll.cpp -o unroll-with-funroll

for ITERATION_COUNT in {1..5}; do
    echo "Run ${ITERATION_COUNT}: ./unroll standard"
    perf stat -e "${PERF_STAT_EVENTS}" ./unroll standard
done 2> "${LAB_NUM}-standard.perfstat"

for ITERATION_COUNT in {1..5}; do
    echo "Run ${ITERATION_COUNT}: ./unroll unrolled"
    perf stat -e "${PERF_STAT_EVENTS}" ./unroll unrolled
done 2> "${LAB_NUM}-unrolled.perfstat"

for ITERATION_COUNT in {1..5}; do
    echo "Run ${ITERATION_COUNT}: ./unroll-with-funroll standard"
    perf stat -e "${PERF_STAT_EVENTS}" ./unroll-with-funroll standard
done 2> "${LAB_NUM}-standard-with-funroll.perfstat"

for ITERATION_COUNT in {1..5}; do
    echo "Run ${ITERATION_COUNT}: ./unroll auto_unrolled"
    perf stat -e "${PERF_STAT_EVENTS}" ./unroll auto_unrolled
done 2> "${LAB_NUM}-auto_unrolled.perfstat"

echo "${LAB_NUM} done"
