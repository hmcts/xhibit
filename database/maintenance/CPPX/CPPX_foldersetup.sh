#!/bin/bash

#################################################################################
#
# Name: CPPX_foldersetup.sh
# Created: January 2020
# 
#        
#
# How to use: This script should be scheduled to run once to set up the required folder structure for NLE/LIVE
#
#################################################################################


# create directory structure if does not exists

mkdir -p CPPX/{logs,staging,sql,processed/{archived,valid,invalid,dealtWith},archived/{processed/invalid,processed/valid,processed/dealtWith,logs,acknowledgements},housekeeping,dssinbound,acknowledgements}

exit
