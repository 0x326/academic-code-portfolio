#!/usr/bin/env bash

cat /proc/cpuinfo | grep 'model name' | head -n 1
cat /proc/meminfo | grep 'MemTotal' | head -n 1

./cpplint.py Lab01.cpp
g++ -O3 --std=c++17 -Wall Lab01.cpp -o Lab01

for _ in {1..5}; do
    env time -f '%U' ./Lab01
    if [[ $? -ne 0 ]]; then
        exit $?
    fi
done
