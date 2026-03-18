REM
REM Filename:    Training_Purge.bat
REM
REM Author:      Nick Sawyer
REM
REM Description: DOS batch file that will be run by the training instructor to
REM              clear the XHIBIT database of all training data.  It calls an
REM              SQL script which requires a court id to pass into a stored
REM              procedure to perform the deletes.
REM
REM Version Information:
REM
REM Revision    Author                 Notes
REM
REM 0.1         Nick Sawyer            Initial revision
REM

sqlplus xhibit/xhibit@dev06 @purge_training_data.sql
