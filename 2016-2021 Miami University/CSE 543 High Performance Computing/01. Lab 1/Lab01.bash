#!/usr/bin/env bash

grep 'model name' /proc/cpuinfo | head -n 1
grep 'MemTotal' /proc/meminfo | head -n 1

./cpplint.py Lab01.cpp
g++ -O3 --std=c++17 -Wall Lab01.cpp -o Lab01

for _ in {1..5}; do
    if ! env time -f '%U' ./Lab01; then
        exit $?
    fi
done

echo "$0 done"
