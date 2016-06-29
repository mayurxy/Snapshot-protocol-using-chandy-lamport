README:

Author: Mayur Talole (mnt150230)
for any corrections or suggestions contact me at mnt150230@utdallas.edu
--------------------------------------------------------------------------


*files submitted: 

1. launcher.sh :logs into machines and runs the main program. 	
2. cleanup.sh  : kills the processes started by netid specified in configuration file
3. deleteandcompile.sh : deletes .msg.out(output files), .class files anf recompiles all .java files
4. Aosmt.java : main program handling whole algorithm
5. chandy.java
6. configread.java
7. convergespan.java
8. Message.java
9. Node.java
---------------------------------------------------------------------
*Steps to run the program:

1. Unzip the submitted the folder
2. put all files including .java, config.txt,launcher.sh,cleanup.sh,deleteandcompile.sh in current working directory.
3. For running script  deleteandcompile.sh use command: 
	$ sh deleteandcompile.sh
4. For running script launcher.sh   use command:
	$ sh launcher.sh [config file name with extension .txt] [netId]

	for ex., $ sh launcher.sh config.txt mnt150230
	
5. For running script cleanup.sh    use command:
	$ sh cleanup.sh [config file name with extension .txt][netId]

	for ex., $ sh cleanup.sh config.txt mnt150230
-----------------------------------------------------------------------
