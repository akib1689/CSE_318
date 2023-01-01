#!/bin/bash

# Set the directory containing all the input files

input_dir=data

# Set the directory containing all the output files
output_dir=test

# Set the directory containing all the class files
class_dir=out/production/Offline_2

output_file=$output_dir/test-result.txt

# clear the output file
echo "" > $output_file

# make the list of heuristics
variable_heuristics="VAH1 VAH3 VAH4 VAH2 VAH5"


# for all files in the input directory compile them that don't have (test) in front of them
for file in $input_dir/*
do
    # for all heuristics
    for heuristic in $variable_heuristics
    do
        echo "$file with $heuristic heuristic"
        java -classpath $class_dir  com.test.Main $file $heuristic >> $output_file
        echo "Successfully processed $file file..."
    done
done

