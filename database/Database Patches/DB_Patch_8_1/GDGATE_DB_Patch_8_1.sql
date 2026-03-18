/*
 * Filename:    GDGATE_DB_Patch_8_1.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in gdg_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE     WHO CHANGE ID   COMMENT
 * ----         ---     ---------       -------
 *  15/08/2006  PG          Initial DDL for Guaranteed Delivery Gateway schema 
 *  16/10/2006  K Shah	    Rewrite to match standards and add 1669 changes + others
 *  19/10/2006  K Shah	    Altered the calling order so that pkgs are created before triggers   
 *  31/10/2006  K Shah	    Updated with Wiki changes 37 
 *  29/11/2006  K Shah	    Updated with Wiki changes 39
 *  29/11/2006  K Shah	    Added sysdate as default on column PROPERTY_TIMESTAMP in table GDG_CONFIG_PROPERTIES
 *  06/12/2006  K Shah	    Removed duplicate enteries for gdg_config_properties_sequence
 *
 */ 



set echo on
set term off
column filename new_value spool_filename
  select 'GDGATE_DB_Patch__8_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/

set term on
spool &&spool_filename

/*
 * Changes to gdg_ table definitions, indexes and foreign keys
 */

 /*
 * Additions or deletion of gdg_ tables, indexes and foreign keys
 */

CREATE TABLE gdg_outbound_statuses
(outbound_status_id           NUMBER(16)   NOT NULL,
 internal_code                VARCHAR2(255) NOT NULL,
 internal_name                VARCHAR2(255) NOT NULL )
TABLESPACE  GDGATED;

ALTER TABLE gdg_outbound_statuses ADD (
  CONSTRAINT outbound_status_id_pk PRIMARY KEY ( outbound_status_id )
    USING INDEX 
    TABLESPACE GDGATEX); 

create public synonym gdg_outbound_statuses for gdgate.gdg_outbound_statuses;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_outbound_statuses TO PUBLIC;

CREATE TABLE gdg_outbound_clobs
( request_id NUMBER(16)    NOT NULL,
  CLOB_DATA       CLOB     NOT NULL)
TABLESPACE  GDGATED;

ALTER TABLE gdg_outbound_clobs ADD (
  CONSTRAINT request_id_pk PRIMARY KEY ( request_id )
    USING INDEX 
    TABLESPACE GDGATEX); 

create public synonym gdg_outbound_clobs for gdgate.gdg_outbound_clobs;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_outbound_clobs TO PUBLIC;

CREATE TABLE gdg_outbound_messages (
request_id               NUMBER(16)    NOT NULL,
source_identifier        VARCHAR2(255) DEFAULT 'C00CourtServiceHub' NOT NULL,
destination_identifier   VARCHAR2(255) DEFAULT  'Z00CJSE' NOT NULL,
exec_mode                VARCHAR2(10)           DEFAULT 'ASYNCH',
request_timestamp        DATE,
send_attempts            NUMBER(8)    DEFAULT 1 NOT NULL,
outbound_status_id       NUMBER(16)   NOT NULL)
TABLESPACE GDGATED;
    
ALTER TABLE gdg_outbound_messages ADD (
  CONSTRAINT msg_request_id_pk PRIMARY KEY ( request_id )
    USING INDEX 
    TABLESPACE GDGATEX);

ALTER TABLE gdg_outbound_messages ADD (
  CONSTRAINT outbound_msg_request_id_fk FOREIGN KEY (request_id ) 
    REFERENCES gdg_outbound_clobs (request_id ));

CREATE INDEX outbound_msg_status_fk
  ON gdg_outbound_messages ( outbound_status_id)
TABLESPACE GDGATEX;

ALTER TABLE gdg_outbound_messages ADD (
  CONSTRAINT outbound_msg_status_id_fk FOREIGN KEY (outbound_status_id) 
    REFERENCES gdg_outbound_statuses (outbound_status_id));

create public synonym gdg_outbound_messages for gdgate.gdg_outbound_messages;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_outbound_messages TO PUBLIC;

CREATE TABLE gdg_outbound_failures (
outbound_failure_id     NUMBER(16)    NOT NULL,
request_id              NUMBER(16)    NOT NULL,
failure_code            VARCHAR2(25)  DEFAULT '-9999' NOT NULL,
failure_text            VARCHAR2(255) NOT NULL,
failure_timestamp       DATE          NOT NULL )
TABLESPACE GDGATED;

ALTER TABLE gdg_outbound_failures ADD (
  CONSTRAINT outbound_failure_id_pk PRIMARY KEY ( outbound_failure_id )
    USING INDEX 
    TABLESPACE GDGATEX);

CREATE INDEX outbound_fail_request_fk
  ON gdg_outbound_failures ( request_id)
TABLESPACE GDGATEX;

ALTER TABLE gdg_outbound_failures ADD (
  CONSTRAINT outbound_fail_request_id_fk FOREIGN KEY (request_id) 
    REFERENCES gdg_outbound_messages (request_id));

create public synonym gdg_outbound_failures for gdgate.gdg_outbound_failures;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_outbound_failures TO PUBLIC;

CREATE TABLE gdg_inbound_clobs ( 
inbound_message_id NUMBER(16)    NOT NULL,
clob_data          CLOB          NOT NULL)
TABLESPACE  GDGATED;

ALTER TABLE gdg_inbound_clobs ADD (
  CONSTRAINT inbnd_clob_msg_id_pk PRIMARY KEY ( inbound_message_id )
    USING INDEX 
    TABLESPACE GDGATEX); 

create public synonym gdg_inbound_clobs for gdgate.gdg_inbound_clobs;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_inbound_clobs TO PUBLIC;

CREATE TABLE gdg_inbound_messages (
inbound_message_id       NUMBER(16)    NOT NULL,
request_identifier       VARCHAR2(128) NOT NULL,
source_identifier        VARCHAR2(255) NOT NULL,
destination_identifier   VARCHAR2(255) NOT NULL,
exec_mode                VARCHAR2(10)  DEFAULT 'ASYNCH' NOT NULL,
request_timestamp        DATE )
TABLESPACE GDGATED;

ALTER TABLE gdg_inbound_messages ADD (
  CONSTRAINT inbound_msg_ri_si_idx UNIQUE ( request_identifier, source_identifier )
    USING INDEX 
    TABLESPACE GDGATEX);

ALTER TABLE gdg_inbound_messages ADD (
  CONSTRAINT inbound_msg_id_pk PRIMARY KEY ( inbound_message_id )
    USING INDEX 
    TABLESPACE GDGATEX); 

ALTER TABLE gdg_inbound_messages ADD (
  CONSTRAINT inbound_msg_id_fk FOREIGN KEY (inbound_message_id  ) 
    REFERENCES gdg_inbound_clobs (inbound_message_id  ));

ALTER TABLE gdg_inbound_messages
 ADD ( CONSTRAINT exec_mode_chk CHECK                                      
 (exec_mode IN ('ASYNCH','SYNCH', 'ROUTE') ));    

create public synonym gdg_inbound_messages for gdgate.gdg_inbound_messages;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_inbound_messages TO PUBLIC;

CREATE TABLE gdg_USAGES (
usage_id                 NUMBER (16)       NOT NULL,
usage_description        VARCHAR2 (255)    NOT NULL,
usage_timestamp          DATE              NOT NULL )
TABLESPACE GDGATED;

ALTER TABLE gdg_USAGES ADD (
  CONSTRAINT usages_usage_id_pk PRIMARY KEY ( usage_id )
    USING INDEX 
    TABLESPACE GDGATEX); 

create public synonym gdg_USAGES for gdgate.gdg_USAGES;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_USAGES TO PUBLIC;

CREATE TABLE gdg_CONFIG_PROPERTIES (
property_id        NUMBER (16)       NOT NULL,
property_code      VARCHAR2 (100)    NOT NULL,
property_name      VARCHAR2 (255)    NOT NULL,
property_value     VARCHAR2 (255)    NOT NULL,
property_timestamp DATE   DEFAULT SYSDATE           NOT NULL)
TABLESPACE GDGATED;

ALTER TABLE gdg_CONFIG_PROPERTIES ADD (
  CONSTRAINT properties_property_id_pk PRIMARY KEY ( property_id )
    USING INDEX 
    TABLESPACE GDGATEX); 

CREATE UNIQUE INDEX config_prop_code_u 
  ON gdg_CONFIG_PROPERTIES ( property_code )
TABLESPACE GDGATEX; 

ALTER TABLE gdg_CONFIG_PROPERTIES ADD (
  CONSTRAINT cong_prop_code_idx UNIQUE ( property_code )
    USING INDEX 
    TABLESPACE GDGATEX);

create public synonym gdg_CONFIG_PROPERTIES for gdgate.gdg_CONFIG_PROPERTIES;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_CONFIG_PROPERTIES TO PUBLIC;

CREATE TABLE gdg_config_property_usages (
usage_id                NUMBER(16),
property_id             NUMBER(16),
prop_usage_timestamp    DATE          NOT NULL)
TABLESPACE GDGATED;

ALTER TABLE gdg_config_property_usages ADD (
  CONSTRAINT config_propusage_id_pk PRIMARY KEY ( usage_id, property_id )
    USING INDEX 
    TABLESPACE GDGATEX); 

ALTER TABLE gdg_config_property_usages ADD (
  CONSTRAINT config_propusage_usage_id_fk FOREIGN KEY (usage_id) 
    REFERENCES gdg_usages (usage_id));

CREATE INDEX config_propusage_prop_fk
  ON gdg_config_property_usages ( property_id)
TABLESPACE GDGATEX;

ALTER TABLE gdg_config_property_usages ADD (
  CONSTRAINT config_propusage_prop_id_fk FOREIGN KEY (property_id ) 
    REFERENCES gdg_config_properties (property_id ));

create public synonym gdg_config_property_usages for gdgate.gdg_config_property_usages;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_config_property_usages TO PUBLIC;

CREATE TABLE gdg_crest_org_unit_xref (
crest_org_unit_id              NUMBER(16),
crest_court_code               VARCHAR2(10)   NOT NULL,
org_unit_code                  VARCHAR2(10)   NOT NULL)
TABLESPACE GDGATED;

ALTER TABLE gdg_crest_org_unit_xref ADD (
  CONSTRAINT crest_org_unit_id_pk PRIMARY KEY ( crest_org_unit_id )
    USING INDEX 
    TABLESPACE GDGATEX);

create public synonym gdg_crest_org_unit_xref for gdgate.gdg_crest_org_unit_xref;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_crest_org_unit_xref TO PUBLIC;

CREATE TABLE gdg_jms_messages (
message_id              NUMBER(16),
message_type            VARCHAR2(50),
expiry_time             DATE )
TABLESPACE GDGATED;

alter table gdg_jms_messages add
(constraint gdg_jms_message_pk PRIMARY KEY (message_id, message_type)
using index
TABLESPACE GDGATEX);

create public synonym gdg_jms_messages for gdgate.gdg_jms_messages;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_jms_messages TO PUBLIC;

CREATE TABLE  gdg_table_timestamp
 ( NAME VARCHAR2(30) NOT NULL UNIQUE,
 last_updated  DATE DEFAULT SYSDATE )
TABLESPACE GDGATED;

create public synonym gdg_table_timestamp for gdgate.gdg_table_timestamp;

GRANT DELETE, INSERT, SELECT, UPDATE ON  gdgate.gdg_table_timestamp TO PUBLIC;

/*
 * Changes, additions or deletion of views
 */

/*
 * Changes to AUDIT tables (AUD_) as a result of any gdg_ table modifications
 */

CREATE TABLE aud_CONFIG_PROPERTIES (
property_id        NUMBER (16)       NOT NULL,
property_code      VARCHAR2 (100)    NOT NULL,
property_name      VARCHAR2 (255)    NOT NULL,
property_value     VARCHAR2 (255)    NOT NULL,
property_timestamp DATE    DEFAULT SYSDATE          NOT NULL)
TABLESPACE AUDITD;



/*
 * Changes, additions or deletion of sequences
 */

CREATE SEQUENCE gdg_outbound_statuses_seq       START WITH 1000 INCREMENT BY 1 NOCACHE;

CREATE SEQUENCE gdg_outbound_failures_seq       START WITH 1000 INCREMENT BY 1 NOCACHE;

CREATE SEQUENCE gdg_inbound_clobs_seq           START WITH 1000 INCREMENT BY 1 NOCACHE;

CREATE SEQUENCE gdg_usages_seq                  START WITH 1000 INCREMENT BY 1 NOCACHE;

CREATE SEQUENCE gdg_crest_org_unit_xref_seq     START WITH 1000 INCREMENT BY 1 NOCACHE;

DECLARE
     nid number;
BEGIN
     select nvl(max(property_id),0)
     into nid
     from gdg_config_properties;
     nid:=nid+50;

  EXECUTE IMMEDIATE 'CREATE SEQUENCE GDGATE.gdg_config_properties_seq
  START WITH '|| nid||
  ' MAXVALUE 1E27
  MINVALUE 1
  NOCYCLE
  NOCACHE';
     
END;
/



GRANT SELECT ON  GDGATE.gdg_config_properties_seq TO public;


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@@gdg_code_release_control_81.sql


/*
 * Changes to gdg_ table triggers as a result of any gdg_ table modifications
 */

CREATE OR REPLACE TRIGGER gdg_outbound_msg_bir_tr
   BEFORE INSERT
   ON gdg_outbound_messages
   FOR EACH ROW
BEGIN
   IF :NEW.outbound_status_id IS NULL
   THEN
      SELECT outbound_status_id
        INTO :NEW.outbound_status_id
        FROM gdg_outbound_statuses
       WHERE internal_code = 'NEW';
   END IF;

   IF :NEW.request_timestamp IS NULL
   THEN
      :NEW.request_timestamp := SYSDATE;
   END IF;
END;
/
SHOW errors;

CREATE OR REPLACE TRIGGER gdg_outbound_stat_bir_tr
   BEFORE INSERT
   ON gdg_outbound_statuses
   FOR EACH ROW
BEGIN
   IF :NEW.outbound_status_id IS NULL
   THEN
      SELECT gdg_outbound_statuses_seq.NEXTVAL
        INTO :NEW.outbound_status_id
        FROM DUAL;
   END IF;
END;
/
SHOW errors;

CREATE OR REPLACE TRIGGER gdg_outbound_fail_bir_tr
   BEFORE INSERT
   ON gdg_outbound_failures
   FOR EACH ROW
BEGIN
   IF :NEW.outbound_failure_id IS NULL
   THEN
      SELECT gdg_outbound_failures_seq.NEXTVAL
        INTO :NEW.outbound_failure_id
        FROM DUAL;
   END IF;
END;
/
SHOW errors;

CREATE OR REPLACE TRIGGER gdg_inbound_msgs_bir_tr
   BEFORE INSERT
   ON gdg_inbound_messages
   FOR EACH ROW
BEGIN
   
   IF :NEW.request_timestamp IS NULL
   THEN
      :NEW.request_timestamp := SYSDATE;
   END IF;
END;
/
SHOW errors;

CREATE OR REPLACE TRIGGER gdg_inbound_clobs_bir_tr
   BEFORE INSERT
   ON gdg_inbound_clobs
   FOR EACH ROW
BEGIN
   IF :NEW.inbound_message_id IS NULL
   THEN
      SELECT gdg_inbound_clobs_seq.NEXTVAL
        INTO :NEW.inbound_message_id
        FROM DUAL;
   END IF;   
END;
/
show errors;

CREATE OR REPLACE TRIGGER gdg_usages_bir_tr
   BEFORE INSERT
   ON gdg_usages
   FOR EACH ROW
BEGIN
   IF :NEW.usage_id IS NULL
   THEN
      SELECT gdg_usages_seq.NEXTVAL
        INTO :NEW.usage_id
        FROM DUAL;
   END IF;

   IF :NEW.usage_timestamp IS NULL
   THEN
      :NEW.usage_timestamp := SYSDATE;
   END IF;
END;
/
SHOW errors;

create or replace trigger gdg_config_properties_bir_tr
    before insert on gdg_config_properties
    for each row
begin
    if :new.property_id is null then
        select gdg_config_properties_seq.nextval
        into  :new.property_id
        from   dual;
    end if;
end;

/
SHOW errors;


CREATE OR REPLACE TRIGGER gdg_crest_org_unit_xref_bir_tr
   BEFORE INSERT
   ON gdg_crest_org_unit_xref
   FOR EACH ROW
BEGIN
   IF :NEW.crest_org_unit_id IS NULL
   THEN
      SELECT gdg_crest_org_unit_xref_seq.NEXTVAL
        INTO :NEW.crest_org_unit_id 
        FROM DUAL;
   END IF;
   
END;
/
SHOW errors;

CREATE OR REPLACE TRIGGER gdg_inbound_msgs_air_tr
    AFTER INSERT ON GDG_INBOUND_MESSAGES
    FOR EACH ROW
BEGIN   
   gdg_scjse_gateway_jms_pkg.process_inbound_message(
          :NEW.inbound_message_id);
END;
/ 
show errors;

CREATE OR REPLACE TRIGGER gdg_outbound_msgs_aur_tr
   AFTER UPDATE OF outbound_status_id
   ON gdg_outbound_messages
   FOR EACH ROW
BEGIN
       gdg_scjse_gateway_jms_pkg.process_outbound_message(
             :NEW.request_id,
             :NEW.outbound_status_id,
             :NEW.send_attempts);
END;
/
show errors;

CREATE OR REPLACE TRIGGER gdg_outbound_msgs_air_tr
    AFTER INSERT ON GDG_OUTBOUND_MESSAGES
    FOR EACH ROW
BEGIN   
   gdg_scjse_gateway_jms_pkg.process_outbound_message(
          :NEW.request_id,
          :NEW.outbound_status_id,
          :NEW.send_attempts);
END;
/ 
show errors;

CREATE OR REPLACE TRIGGER gdg_config_prop_au_tr
   AFTER UPDATE
   ON gdg_config_properties
BEGIN
   UPDATE gdg_table_timestamp
      SET last_updated = SYSDATE
    WHERE NAME = 'GDG_CONFIG_PROPERTIES';
END;
/
show errors;

CREATE OR REPLACE TRIGGER gdg_config_properties_bur_tr
   BEFORE UPDATE
   ON gdg_config_properties
   FOR EACH ROW
BEGIN

   /*
   ** The timestamp for MSG_CONSEC_FAIL_COUNT is
   ** maintained separately
   */

   IF :NEW.property_timestamp IS NULL AND
      :NEW.property_code != 'MSG_CONSEC_FAIL_COUNT'
   THEN
      :NEW.property_timestamp := SYSDATE;
   END IF;
END;
/
show errors;


/*
 * Changes, additions or deletion of standing data
 */

@@GDGATE_data_load.sql

/*
 * Updating of table gdg_VERSION
 */

CREATE TABLE gdg_version
(
  SCHEMA_NAME       VARCHAR2(10),
  SCHEMA_VERSION    VARCHAR2(10),
  LAST_UPDATE_DATE  DATE,
  UPDATED_BY        VARCHAR2(30),
  DISPLAY_NAME      VARCHAR2(30),
  DISPLAY_SEQ       NUMBER(2)
)
TABLESPACE GDGATED;

INSERT INTO GDG_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('GDGATE','8_1',sysdate,'GDGATE','GDGATE Database Schema 8_1',1);


COMMIT;

spool off
