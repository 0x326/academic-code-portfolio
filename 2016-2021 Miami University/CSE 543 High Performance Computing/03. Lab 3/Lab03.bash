#!/usr/bin/env bash

grep 'model name' /proc/cpuinfo | uniq | head -n 1
grep 'MemTotal' /proc/meminfo | uniq | head -n 1

LAB_NUM=Lab03
TIME_FORMAT=$'%U\t%S\t%e'

cpplint.py "${LAB_NUM}-good.cpp"
cpplint.py "${LAB_NUM}-bad.cpp"

g++ -g -O2 --std=c++17 -Wall "${LAB_NUM}-good.cpp" -o "${LAB_NUM}-good"
g++ -g -O2 --std=c++17 -Wall "${LAB_NUM}-bad.cpp" -o "${LAB_NUM}-bad"

echo "Running ${LAB_NUM}-good..."
perf stat -d -d -d "./${LAB_NUM}-good" 2> "${LAB_NUM}-good.perfstat"

echo "Running ${LAB_NUM}-bad..."
perf stat -d -d -d "./${LAB_NUM}-bad" 2> "${LAB_NUM}-bad.perfstat"

echo "${LAB_NUM} done"
