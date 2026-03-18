SET TERM OFF
/*
 * Filename:    XHIBIT2_Misc_Inserts.sql
 *
 * Author:      Nick Sawyer
 *
 * Description: Inserts data into various tables that do not really
 *              fall under Standing Data
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 * 0.2         Nick Sawyer            Added insert into XHB_VERSION
 * 0.3         Nick Sawyer            Changed schema_version to '5.11'
 * 0.4         Nick Sawyer            Changed schema_version to '5.12'
 * 0.5         Nick Sawyer            Changed schema_version to '6.0'
 * 0.6         Nick Sawyer            Changed schema_version to '6.1'
 * 0.7         Nick Sawyer            Changed schema_version to '6.1.1'
 * 0.8         Nick Sawyer            Changed schema_version to '6.1.2'
 * 0.9         Nick Sawyer            Changed schema_version to '6.1.3'
 * 1.0         Nick Sawyer            Changed schema_version to '6.1.4' for Mercator
 * 1.1         Nick Sawyer            Changed schema_version to '6.1.5' for Java
 *
 */
SET TERM ON

INSERT INTO XHB_SYS_USER_INFORMATION (CONNECTION_POOL_USER_NAME,
                                      MERCATOR_USER_NAME)
                                      (SELECT NVL(sys_context('USERENV', 'SESSION_USER'),'XHIBIT'),
                                              'MERCATOR'
                                       FROM   dual);

INSERT INTO XHB_SYS_AUDIT 
  SELECT NULL,
         table_name,
         replace(table_name, 'XHB', 'AUD'),
         'Y'
  FROM   user_tables
  WHERE  table_name like 'XHB%'
  AND    table_name not in ('XHB_SYS_AUDIT',
                            'XHB_SYS_USER_INFORMATION',
                            'XHB_VERSION');

INSERT INTO XHB_VERSION (SCHEMA_NAME,
                         SCHEMA_VERSION,
                         LAST_UPDATE_DATE,
                         UPDATED_BY,
                         DISPLAY_NAME,
                         DISPLAY_SEQ)
                 VALUES ('JAVA',
                         '6.1.5',
                         SYSDATE,
                         'RELEASE',
                         'Java Application',
                         1);

INSERT INTO XHB_VERSION (SCHEMA_NAME,
                         SCHEMA_VERSION,
                         LAST_UPDATE_DATE,
                         UPDATED_BY,
                         DISPLAY_NAME,
                         DISPLAY_SEQ)
                 VALUES ('XHIBIT',
                         '6.1.2',
                         SYSDATE,
                         'RELEASE',
                         'Database',
                         2);

INSERT INTO XHB_VERSION (SCHEMA_NAME,
                         SCHEMA_VERSION,
                         LAST_UPDATE_DATE,
                         UPDATED_BY,
                         DISPLAY_NAME,
                         DISPLAY_SEQ)
                 VALUES ('MERCATOR',
                         '6.1.4',
                         SYSDATE,
                         'RELEASE',
                         'Mercator',
                          3); 

COMMIT;
