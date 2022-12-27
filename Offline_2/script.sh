#!/bin/bash

# Set the directory containing all the input files

input_dir=data

# Set the directory containing all the src java files
src_dir=src

# Set the directory containing all the output files
output_dir=out/production/Offline_2

# for all files in the src directory compile them
for file in $src_dir/*.java
do
    javac $file -d $output_dir
done

