#!/usr/bin/env bash

cat /proc/cpuinfo | grep 'model name' | uniq | head -n 1
cat /proc/meminfo | grep 'MemTotal' | uniq | head -n 1

LAB_NUM=Lab02
TIME_FORMAT=$'%U\t%S\t%e'

cpplint.py "${LAB_NUM}.cpp"
g++ -O3 --std=c++17 -Wall "${LAB_NUM}.cpp" -o "${LAB_NUM}"

for _ in {1..8}; do
    env time -f "${TIME_FORMAT}" "./${LAB_NUM}" switch 2000000000
    if [[ $? -ne 0 ]]; then
        exit $?
    fi
done

for _ in {1..8}; do
    env time -f "${TIME_FORMAT}" "./${LAB_NUM}" 'if' 2000000000
    if [[ $? -ne 0 ]]; then
        exit $?
    fi
done

echo "${LAB_NUM} done"
