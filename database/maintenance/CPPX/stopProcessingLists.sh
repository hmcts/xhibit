#!/bin/bash

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live

touch $SCRIPT_HOME/stopProcessingCPLists.file
