SOURCE_FOLDER=/var/mqsi/WebPages
DESTINATION_FOLDER=/var/mqsi/WebPages/Send
LOG_FILE=$SOURCE_FOLDER/zipsftp.log
BOARDING_SERVER=10.224.251.4
BOARDING_USER_ID=xhibit_s3_html
CURRENT_TIMESTAMP=`date +"%d%m%y%H%M%S"`
echo $CURRENT_TIMESTAMP >> $LOG_FILE

echo "Moving htm file.." >> $LOG_FILE
#Move all htm files to Send folder
mv $SOURCE_FOLDER/*.htm $DESTINATION_FOLDER/


echo "Zipping htm files" >> $LOG_FILE
# Create new zip file
zip -j $DESTINATION_FOLDER/XHIBIT_HTM.zip $DESTINATION_FOLDER/*.htm

echo "Removing htm files" >> $LOG_FILE
rm $DESTINATION_FOLDER/*.htm

echo "sftping zip file.." >> $LOG_FILE
sftp $BOARDING_USER_ID@$BOARDING_SERVER <<EOF
lcd $DESTINATION_FOLDER
put $DESTINATION_FOLDER/XHIBIT_HTM.zip
EOF

echo "sftp complete.." >> $LOG_FILE

echo "Removing zip file" >> $LOG_FILE
rm $DESTINATION_FOLDER/XHIBIT_HTM.zip
