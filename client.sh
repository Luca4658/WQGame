#!/bin/bash

CONFIG_FILE=client.json

###############################################################################
#																																							#
#												DO NOT MODIFY THE FOLLOWING														#
#																																							#
###############################################################################
CONFPATH=${PWD}/config/$CONFIG_FILE
JARPATH=$PWD/out/WQGame_client.jar
SERVER="java -jar $JARPATH $CONFPATH"


function start( )
	{
		echo -e "\nStarting...\n"
		exec $SERVER &
	}

function usage()
	{
		echo -e "Usage: $0 -s"
		echo -e "Start the WordQuizzleGame client process \n"
		echo -e "  -s \t start the client"
		echo -e "\n\n"
		echo -e "To change the client setting copy and rename the file 'client.json.example' in the config directory\nand modify the fields in the angular brackets to your liking keeping the quotation marks. \nThen, to use this configuration modify the 'CONFIG_FILE' variable\nin the top of this file with the name of your config file"
		echo -e '\n!!! WARNING !!!\nAll fields in the client configuration file MUST BE EQUAL to the same fields in the server configuration file\n'
		echo -e "Examples:\n"
		echo -e "  $0"
		echo -e "\n"
		echo -e "  (config file field)"
		echo -e '  FROM: "timeoutreq":"<tempo-massimo-di-attesa-della-risposta-alla-sfida>" TO: "timeoutreq":"5000"'
 	}

if [ $# -eq 1 ]; then
		if [ -n "$1" ]; then
			case "$1" in
				-s) start
				;;
				-h) usage
				;;
				*) 	echo -e "Option $1 not recognized"
						echo "Try '$0 -h' for more information."
				;;
			esac	
		fi
	else
		echo "missing arguments" 
		echo "Try '$0 -h' for more information."
fi