#!/bin/bash


# Change this to your netid

#
# Root directory of your project
#PROJDIR=$HOME/CS6378/Project1

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
#
#PROG=Project1


CONFIG=$PWD/$1

NETID=$2



n=1

cat $CONFIG | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
echo "cleaning begins"
read line 
 echo line
numhosts=$( echo $line | awk '{ print $1 }' ) 
#netid=$( echo $line | awk '{ print $2 }' )

#host=$( echo $line | awk '{ print $2 }' )
#	port=$( echo $line | awk '{ print $3 }' )
	
	
while [[ $n -le numhosts ]] 
do
# n=$(( n + 1 ))
	read line
	#node1=$( echo $line | awk '{ print $1 }' )
	host=$( echo $line | awk '{ print $2 }' )
	#portno=$( echo $line | awk '{ print $3 }' )
	echo $host
	if [[ $host == dc* ]]		
	then
		n=$(( n + 1 ))
		
		ssh -o StricthostKeyChecking=no $NETID@$host killall -u $NETID &
		#ssh $netid@$host killall -u $netid &
        #sleep 1
	fi
	sleep 1
done
)
echo "Cleaning complete"
