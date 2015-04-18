# WarehouseManager

## Description
--------------------------

Project for university. Goal is to create a java program that handles XML files. It takes in one company type .xml configuration file, then prints out desired output on a comma separated values file (.csv) based on given actions in the company file.

## Command line options:
--------------------------

* -s <file> 	Sets a config file to be used. Must be company type XML file.
* -v			Sets verbose messages on. Prints more messages
* -d			Sets debug messages on. Prints debug info
* -w <path>		Sets workspace. All files will be searched from this location, including config file
* -o <filename>	Sets output file. Default is "output.csv"

## Actions
--------------------------

Following actions are can be placed into config file. Unkown operations will be ignored.

* MERGE		Will merge all warehouse stocks into one file. Param tells which stock (INBOUND/OUTBOUND/STORAGE) are merged
* SORT			Sorts merged stock. Param tells whenever we want list to be ascending or decending based on id

## Notes
--------------------------

This software does few things  in unoptimal way, such as using custom created merge-sort to sort out vectors, instead of in-built sort function. Reason for this is because I wanted to practice creating sorting algorithms.