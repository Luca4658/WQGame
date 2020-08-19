#!/bin/bash

CONFIG_FILE=default.json

###############################################################################
#																																							#
#												DO NOT MODIFY THE FOLLOWING														#
#																																							#
###############################################################################
CONFPATH=${PWD}/config/$CONFIG_FILE
JARPATH=$PWD/out/WQGame_server.jar
SERVER="java -jar $JARPATH $CONFPATH"


function start( )
	{
		if [ $(pgrep -f "$SERVER") ]; then
				echo -e "\nServer already running!\n"
				exit
			else
				echo -e "\nStarting...\n"
				exec $SERVER &
		fi
	}

function stop( )
	{
		PID=$(pgrep -f "$SERVER")
		if [ $PID ]; then
				echo -e "\nStopping...\n"
				kill -SIGINT $PID
			else
				echo -e "\nServer already stopped\n"
				exit
		fi
	}

function usage()
	{
		echo -e "Usage: $0 [OPTION]"
		echo -e "Control the WordQuizzleGame server process \n"
		echo -e "Needs one option to use this script\n"
		echo -e "  -s \t start the server"
		echo -e "  -S \t stop the server"
		echo -e "  -h \t display this help"
		echo -e "\n\n"
		echo -e "To change the server setting copy and rename the file 'server.json.example' in the config directory\nand modify the fields in the angular brackets to your liking keeping the quotation marks. \nThen, to use this configuration modify the 'CONFIG_FILE' variable\nin the top of this file with the name of your config file"
		echo -e "\n"
		echo -e "Examples:\n"
		echo -e "  $0 -s"
		echo -e "  $0 -S"
		echo -e "\n"
		echo -e "  (config file field)"
		echo -e '  FROM: "words": "<numero-di-parole-da-inviare-agli-utenti>" TO: "words": "10"'
 	}

if [ $# -eq 1 ]; then
		if [ -n "$1" ]; then
			case "$1" in
				-s) start
				;;
				-S) stop
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