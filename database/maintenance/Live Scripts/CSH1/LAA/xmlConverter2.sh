#!/bin/bash

cd /opt/moj/home/wmbroker/bin/cron/LAA/workspace/

ns=2
nestedAPD=0
str1=' xmlns:'
str2='="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"'
closeTag=1
Apos="'"
#echo $Apos
apos="&apos;"
#echo $apos
Pound="£"
pound="&#xA3;"

#sed "s/'/&pos;/g" $1 > temp
#cat $1 | tr "'" "&apos;" > temp
xmllint --format $2$1 > XML1.out
sed 's/cs:/NS1:/g' XML1.out > temp
mv temp XML1.out

if [ `grep "/>" XML1.out | wc -l` = 0 ]
then
	closeTag=0
fi

if [ `grep ?xml XML1.out | wc -l` = 1 ]
then
        sed 1d XML1.out > XML2.out
        mv XML2.out XML1.out
fi
caseType='Appeal'
if [ `echo $1 | grep -i trlrs | wc -l` = 1 ]
then
        caseType="Trial"
fi
if [ `echo $1 | grep -i aplrs | wc -l` = 1 ]
then
        caseType="Appeal"
fi

if [ `echo $1 | grep -i comrs | wc -l` = 1 ]
then
        caseType="Committal"
fi

echo '<?xml version="1.0" encoding="iso-8859-1"?>' > XMLOutput.xml
echo '<NS1:'$caseType'RecordSheet xmlns:NS1="http://www.courtservice.gov.uk/schemas/courtservice">' >> XMLOutput.xml

sed 1d XML1.out > XML2.out
mv XML2.out XML1.out

if [ $closeTag = 0 ]
then
	while IFS= read line
        	do
                	if [ `echo $line | grep "apd:" | wc -l` = 1 ]
                	then
                        	NS=NS$ns
                        	if [ $nestedAPD = 0 ]
                        	then
                        	        line=${line/>/$str1$NS$str2>}
                        	fi
                        	if [ `echo $line | grep "/apd:" | wc -l` = 1 ]
                        	then
                        	        if [ `echo $line | grep "<apd:" | wc -l` = 0 ]
                        	        then
                        	                nestedAPD=0
                        	        fi
                        	else
                        	        nestedAPD=1
                        	fi
                        	line=${line//apd:/$NS:}

                        	if [ $nestedAPD = 0 ]
                        	then
                        	        ns=$(($ns+1))
                        	fi
                	fi
                	echo $line >> XMLOutput.xml
        done < XML1.out
else
	while IFS= read line
                do
                        if [ `echo $line | grep "apd:" | wc -l` = 1 ]
                        then
                                NS=NS$ns
                                if [ $nestedAPD = 0 ]
                                then
                                        line=${line/>/$str1$NS$str2>}
                                fi
                                if [ `echo $line | grep "/apd:" | wc -l` = 1 ]
                                then
                                        if [ `echo $line | grep "<apd:" | wc -l` = 0 ]
                                        then
                                                nestedAPD=0
                                        fi
                                else
                                        nestedAPD=1
                                fi
                                line=${line//apd:/$NS:}

                                if [ $nestedAPD = 0 ]
                                then
                                        ns=$(($ns+1))
                                fi
                        fi
			if [ `echo $line | grep "/>" | wc -l` = 1 ]
			then
				#echo $line
				line=${line//\/>/>}
				#echo $line
				line2=${line//</<\/}
				#echo $line2
				line=$line$line2
				#echo $line
			fi
                        echo $line >> XMLOutput.xml
        done < XML1.out
fi


sed 1d XMLOutput.xml > temp
less temp | tr -ds '\r\n' '' > $1.edited

convertApos=`cat $1.edited`
convertApos=${convertApos//$Apos/$apos}
convertApos=${convertApos//$pound/$Pound}
echo $convertApos > temp

less temp | tr -ds '\r\n' '' > $1.xml
mv $1.xml $1.edited
if [ `less $1.edited | wc -c` -gt 150 ]
then
        mv $1.edited $2$1
        #rm $1.edited
else
	cp $2$1 .
	rm $2$1
fi

rm XMLOutput.xml
rm XML1.out
rm temp
