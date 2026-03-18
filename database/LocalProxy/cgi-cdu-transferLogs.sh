#!/bin/bash
#
# This is a script which polls the localproxy for a CDU software
#  release. On availability of a new release, it downloads the release,
#  verifying the release against checksum and installs the release, rebooting
#  the CDU on completion.
#
# Created by: Jasvir Boparai
# Created on: 05/01/2017 (dd/mm/yyyy)
#
# Version History
# Date          Name                        Description
# 05/01/2017    Jasvir Boparai              First issue
# 22/02/2017    Mark Harris                 Add hostname -s
# 23/02/2017    Sean Bulley                 Add mkdir to allow script to run on fresh build
# 24/02/2017    Mark Harris                 Change ftp directory path to /var/log
# 27/02/2017    Mark Harris                 Limit logs to application,cdu* and display_network
# 10/03/2017    Mark Harris                 Change integer increments to be surrounded by brackets
# 13/03/2017    Mark Harris                 Change the way getFiles works to stop error
# 13/03/2017	Sean Bulley					Changed move to copy and updated fortransfer dir


DIRECTORY_FOR_FTP=/var/log/
DIRECTORY_FOR_TRANSFER=/home/xhibit/logsfortransfer/
DIRECTORY_FOR_ZIP=/var/log/logsforftp/
LOCK_FILE=/var/run/cgi-cdu-transferLogs.lck

LOG_TO_CONSOLE=0

function logInfo {
	MSG=$1
	LOGGER_COMMAND="/usr/bin/logger -p local5."

	`${LOGGER_COMMAND}info \"${MSG}\"`
	
	if [[ ${LOG_TO_CONSOLE} ]]; then
		echo ". ${MSG}"
	fi
}

function logError {
	MSG=$1
	LOGGER_COMMAND="/usr/bin/logger -p local5." 

	`${LOGGER_COMMAND}err \"${MSG}\"`
	if [[ ${LOG_TO_CONSOLE} ]]; then
		echo ". ERROR: ${MSG}"
	fi
}

function getFiles {
        files=`ls$searchString | xargs -n1 basename`
}

function moveTheFile {
	`cp $sourceFilename $destinationFilename`
} 

function buildSearchString {
        # Set the variables
        unset searchString
        ((appLogExists=0))
        ((cduLogExists=0))
        ((disLogExists=0))
        ((noOfLogsExist=0))
        if [[ -f ${DIRECTORY_FOR_FTP}"application.log" ]]; then
           ((appLogExists=1))
           ((noOfLogsExist+=1))  
        fi  
        for wildcardFile in ${DIRECTORY_FOR_FTP}cdu*.log ; do 
            if [[ -f ${wildcardFile} ]]; then
               ((cduLogExists=1))
               ((noOfLogsExist+=1))  
            fi  
            break 
        done
        if [[ -f ${DIRECTORY_FOR_FTP}"display-network.log" ]]; then
            ((disLogExists=1))
            ((noOfLogsExist+=1))  
        fi 

        # Build the searchString
        if [[ ${noOfLogsExist} > 0 ]]; then
           if [[ ${appLogExists} = 1 ]]; then
              searchString+=" ${DIRECTORY_FOR_FTP}application.log"
           fi
           if [ ${cduLogExists} = 1 ]; then
              searchString+=" ${DIRECTORY_FOR_FTP}cdu*.log"
           fi
           if [ ${disLogExists} = 1 ]; then
              searchString+=" ${DIRECTORY_FOR_FTP}display-network.log"
           fi
        fi
}

function processFTPFiles {
	logInfo "Processing new FTP files"
	 getFiles 
	 for processingFile in $files
	 do
	     newTmpFilename=$(sed 's/.\{4\}$//' <<< "$processingFile")
	     sourceFilename=$DIRECTORY_FOR_FTP$processingFile	
             destinationFilename=$DIRECTORY_FOR_ZIP$COURTSITE"_"$newTmpFilename"_"$DATESTRING".log"
	     moveTheFile
         done
}

function checkForFTPFiles {
	logInfo "Checking for new FTP files"
        buildSearchString
        if [[ ${searchString} = "" ]]; then 
           logInfo "No files found";
        else
           processFTPFiles 
        fi
}

function moveZipToTransfer {
	logInfo "Transfer zip file"
	
        sourceFilename=${DIRECTORY_FOR_ZIP}${COURTSITE}"_logs.tar"
        destinationFilename=${DIRECTORY_FOR_TRANSFER}${COURTSITE}"_logs.tar"
        if [[ -f ${sourceFilename} ]]; then 
	   moveTheFile
        fi
}

function zipUpFTPFiles {
        zipFile=${DIRECTORY_FOR_ZIP}${COURTSITE}"_logs.tar"
	zipContent=${DIRECTORY_FOR_ZIP}*.log
	logInfo "Zipping up new FTP files"
        tar -zcf $zipFile $zipContent
	if [[ -f ${zipFile} ]]; then
	   echo "Removing zipped content"
	   rm -f $zipContent
	   moveZipToTransfer
	fi
}

# first check if this script is already running by checking for lock file
if [[ -f ${LOCK_FILE} ]]; then
    echo "Lock file exists - already running"
	exit 1
fi


# create lock file to prevent re-entry
touch ${LOCK_FILE}

# setup the local variables
COURTSITE=`hostname -s`
DATESTRING=$(date +%d%m%Y)

# Establish Directorys (if not already exists)
mkdir -p ${DIRECTORY_FOR_FTP}
mkdir -p ${DIRECTORY_FOR_TRANSFER}
mkdir -p ${DIRECTORY_FOR_ZIP}

if [[ "$2" == "--report" ]]; then
	LOG_TO_CONSOLE=1
fi

checkForFTPFiles

if [[ ${noOfLogsExist} > 0 ]]; then
   zipUpFTPFiles
fi

# clean up the lock file
rm -f ${LOCK_FILE}
