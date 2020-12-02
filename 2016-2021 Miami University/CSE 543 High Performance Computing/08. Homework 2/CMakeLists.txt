cmake_minimum_required(VERSION 3.17)
project(2020_10_16_Homework_02)

set(CMAKE_CXX_STANDARD 17)

find_package(OpenMP)
if (OPENMP_FOUND)
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
    set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
    set (CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${OpenMP_EXE_LINKER_FLAGS}")
endif()

add_executable(SERIAL_BOX_MULLER box-muller.cpp)
add_executable(SERIAL_MARSAGLIA marsaglia.cpp)
add_executable(PARALLEL_BOX_MULLER box-muller_omp.cpp)
add_executable(PARALLEL_MARSAGLIA marsaglia_omp.cpp)
