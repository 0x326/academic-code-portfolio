cmake_minimum_required(VERSION 3.17)
project(2020_11_15_Homework_04)

set(CMAKE_CXX_STANDARD 17)

SET(CMAKE_C_COMPILER mpicc)
SET(CMAKE_CXX_COMPILER mpicxx)

add_executable(SERIAL
        fourier_serial.cpp)

add_executable(PARALLEL
        fourier_mpi.cpp)
