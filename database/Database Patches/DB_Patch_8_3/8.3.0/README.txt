instructions for the installation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1)

These instructions assume the file HS_dba.sql defines HK_LOGS as /home/oracle/housekeeping/logs.

As the oracle unix user configure the logs directory and archive script:

  mkdir -p /home/oracle/housekeeping/logs
  mkdir -p /home/oracle/housekeeping/scripts
  dos2unix xhb_archive.sh /home/oracle/housekeeping/scripts/xhb_archive.sh
  chown oracle /home/oracle/housekeeping/scripts/xhb_archive.sh
  chmod u+wrx /home/oracle/housekeeping/scripts/xhb_archive.sh

As the oracle unix user configure the crontab entry to run archiving:

  export EDITOR=vi
  crontab -e

Add the following line

30 1 * * * /home/oracle/housekeeping/scripts/xhb_archive.sh A 0 6 6 6 12 TRUE

then save the crontab and quit vi.


2)

The SQL script 

  HS_dba.sql

should be run as the Oracle SYS user to create the directory object used by xhb_housekeeping_pkg_b.sql.


3)

The SQL script

  Xhibit_DB_Patch_8_3.sql 

should be run as the Oracle xhibit user to update the XHIBIT DB schema.


4)

The SQL script

  Exiss_DB_update.sql

should be run as the Oracle exiss user to update the EXISS DB schema.
