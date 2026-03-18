installation instructions 
~~~~~~~~~~~~~~~~~~~~~~~~~

1)

The SQL script

  Xhibit_DB_Patch_8_3_11.sql 

should be run as the Oracle xhibit user to update the XHIBIT DB schema.

2) Under the Database patch folder 8.3.0,run the SQL script 
   
   xhb_housekeeping_pkg_b.sql

should be run as the Oracle xhibit user, this replaces the existing package body

3) 
The SQL script 

     EXI_database_objects.sql

shouldbe run as the Oracle Exiss user to update some records in the Exiss database 
      
4) 
The SQL script 

     CJIT_database_stylesheet_reversioning.sql

shouldbe run as the Oracle CJIT user to update some records in the CJIT database 
