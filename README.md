# WarehouseManager
Project for university. Goal is to create a java program that handles XML files.

So far, it is still very much barebones. I am also learning using git for version control.

Command line options:

-s <file> 	Sets a config file to be used. Must be company type XML file.
-v			Sets verbose messages on. Prints more messages
-d			Sets debug messages on. Prints debug info
-w <path>	Sets workspace. All files will be searched from this location, including config file

Actions

Following actions are can be placed into config file. Unkown operations will be ignored.

MERGE		Will merge all warehouse stocks into one file.
