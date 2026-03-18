#!/bin/bash
# script to swap the schemas
# default is the .common-variables which is read in by all deleteXXXX.sh
# this is the .common-variables.xhibit file
# .common-variables.other needs to be used for other schemas

# to run: ./swap_schemas.sh xhibit
# or run: ./swap_schemas.sh other 
# ./swap_schemas on its own will list the current values.


if [[ $1 == "xhibit" ]]
then
	cp .common-variables.xhibit .common-variables
	echo -e "Schema is now set to: \n`egrep "SQLDB=|SQLDB2=" .common-variables| cut -d '=' -f2`"
elif [[ $1 == "other" ]]
then
	cp .common-variables.other .common-variables
	echo -e "Schema is now set to: \n`egrep "SQLDB=|SQLDB2=" .common-variables| cut -d '=' -f2`"
else
	
	echo -e "Current default schemas are: \n`egrep "SQLDB=|SQLDB2=" .common-variables| cut -d '=' -f2`"
fi

exit 
