#!/bin/bash
# Root directory of your project
#PROJDIR=$HOME/AOS/Project1
#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#
#CONFIG=$PROJDIR/config.txt
#
# Directory your java classes are in
#
#BINDIR=$PROJDIR/bin
#
# Your main project class
PROG=Aosmt
NETID=$2
PROGRAM_PATH=$(pwd)

echo "you file path"
echo "path"
	echo $PROGRAM_PATH

echo "begining read file and algorithm"	
	
	cat $1 | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i    
    netId=$( echo $i | awk '{ print $2 }' )
    totalnodes=$( echo $i | awk '{ print $1 }' )
	echo "netID:"
    echo $netId	
    
    for ((k=1; k <= $totalnodes ; k++))
    do
    	read line
	echo k		
		#nodeId=$( echo $line | awk '{ print $1 }' )
	echo $line
	nodeId=$( echo $line | awk '{ print $1 }' )
	
	#do
     #   host=$( echo $line | awk '{ print $1 }' )

      #  ssh $netid@$host java $BINDIR/$PROG $n &
		
		       	host=$( echo $line | awk '{ print $2 }' )
	echo "nodeID"
	echo $nodeId
	echo "host"
	echo $host
	# the actual command:
	
	ssh -o StrictHostKeyChecking=no -l "$NETID" "$host" "cd $PROGRAM_PATH;java $PROG $nodeId $1" &
	
	# n=$(( n + 1 ))
    done
   
)


