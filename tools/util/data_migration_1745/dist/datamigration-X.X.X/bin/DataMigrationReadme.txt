The datamigration.bat is for the purpose of running the Data Migration for Req 1745.

Ensure the JAVA_HOME is set and the datamigration.bat references it correctly

Command Prompt Arguments

One mandatory command parameter must be passed in when running the bat file: this represents the Database Password
This must be the first parameter and is a command line argument for security reasons
This does not require a tag as it is mandatory

There are several other optional command line parameters mainly for development
These must have the tag (eg -s) before the actual property and each must be separated by a space

Data Migration Statisics (-s)
-s: Optional field can be true or false, default: false 

Database Connection Management Type
-t: Optional field can be FIXED or RECREATE, default: FIXED

Location of Data Migration initialization properties
-l: Optional field representing the full directory path to datamigration.ini file, default is ../reportbox

Connection Pool Max Size
-m: Optional (only used for FIXED connection) Numeric field, default is: 1

Report Format
-f: Optional field can be ALL or COURT, default ALL

Use -h or -? for help (the system will not run - do not include any other command line params including password)        
        

datamigration.ini file

The other properties must be specified in the properties/datamigration.ini file. 
The property name and value must be separated by a semi colon (;) and each property/value pair must be on a new line
The properties, options and default values are:

mode;
Default is R (produces a Report and Error File and no database changes are committed). The
For the real data migration it must be set to U (only an error file is produced and all database changes are committed)

directory;
Location of the report directory, default is:../reportbox

errordirectory;
Location of the error directory, default is:../errorbox

filename;
Name of the report file (default is YYYYMMDD_data_migration.xml)

errorfilename;
Name of the report file (default is YYYYMMDD_data_migration_error.xml) 

courts;
This is a comma separated list of court IDs. At least one must be present

database.driver;
This is mandatory (example:oracle.jdbc.xa.client.OracleXADataSource)

database.url;
This is mandatory (example:jdbc:oracle:thin:@130.177.4.44:1521:CSDBDEV3)

database.user;
This is mandatory (example:xhibit)






   
    