#!/bin/bash
#
#Get current date/time
now="`date +'%d/%m/%Y %H:%M:%S'`"
#
#Get hostname 
host="`hostname`"
case "$host" in 
"sv01.mltk.dv.loccs") host="CREST";;
"px2pcs01") host="CSH1";;
"px2pcs02") host="CSH2";;
"px2pvs01") host="MIDM1";;
"px2pvs02") host="MIDM2";;
"px2pvs03") host="MIDM3";;
"px2pvs04") host="MIDM4";;
"px2pvs05") host="MIDM5";;
"px2wvs01") host="PRESM1";;
"px2wvs02") host="PRESM2";;
esac
#
#Clear old files
rm ./diskstats.txt ./diskstats0.txt ./diskstats1.txt
#
df -k | awk '{print $6 " " $5}' | tr -d "%" | awk '{print $1 " " $2}' > ./diskstats.txt
#
#Collate stats
#
sed 1d ./diskstats.txt > ./diskstats0.txt
#
while read line
do
   drive=`printf "$line" | awk '{print $1}'` 
   cap=`printf "$line" | awk '{print $2}'`
   if [ "$cap" -le 90 ]; then
     status=`printf "$cap,OK"` 
   elif [ "$cap" -le 95 ]; then
     status=`printf "$cap,WARN"` 
   else
     status=`printf "$cap,FAIL"` 
   fi
   printf  "$host,$drive,$now,$status\n" >> ./diskstats1.txt
done < ./diskstats0.txt
