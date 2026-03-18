/*
 * Filename:    Exiss_DB_Patch_8_1_1.sql
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in EXI_version updates.
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   07/07/2006	K SHAH			Created
   13/07/2006   K SHAH			database changes made to comprise 
                     			release 8.1
   31/07/2006   I LO    		Additional updates for 8.1 removed drop statements
   20/09/2006   S MILES                 Additional changes for 8.1 (items 27, 29, 32)
   11/10/2006   S MILES                 Additional changes for 8.1 (item 35)
   16/10/2006   K Shah			Added 1669 changes
   19/10/2006   K Shah	    		Altered the calling order so that pkgs are created before triggers
   15/12/2006   K Shah			Altered data load for exi_property table - Wiki 2
   15/12/2006   K shah			Altered data load for exi_jms_property table - Wiki 4 
   15/12/2006   K shah			Added new version of exi_jms_message_pkg_b -  Wiki 5 
   15/12/2006   K Shah			Update the version to 8.1.1 as per LW's mail
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Exiss_DB_Patch_8_1_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to EXI_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of EXI_tables, indexes and foreign keys
 */

create table exi_ref_operation (
operation_id    NUMBER(8)                    NOT NULL ,
internal_code   VARCHAR2(30)                 not null ,
internal_name   VARCHAR2(255),
external_name   VARCHAR2(255))
TABLESPACE EXISSD;

ALTER TABLE exi_ref_operation ADD (
  CONSTRAINT exi_ref_operation_id_pk PRIMARY KEY ( operation_id )
    USING INDEX 
    TABLESPACE EXISSX);

ALTER TABLE exi_ref_operation ADD (
  CONSTRAINT ref_oper_intern_code_idx UNIQUE ( internal_code )
    USING INDEX 
    TABLESPACE EXISSX);

create public synonym exi_ref_operation for EXISS.exi_ref_operation;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_ref_operation TO PUBLIC;


create table exi_ref_group (
group_id        NUMBER(8)                    NOT NULL ,
external_name   VARCHAR2(255))
TABLESPACE EXISSD;

ALTER TABLE exi_ref_group ADD (
  CONSTRAINT group_id_pk PRIMARY KEY ( group_id )
    USING INDEX 
    TABLESPACE EXISSX);  

create public synonym exi_ref_group for EXISS.exi_ref_group;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_ref_group TO PUBLIC;


create table exi_ref_type (
type_id         NUMBER(8)                     NOT NULL,
operation_id    NUMBER(8)                     NOT NULL,
group_id        NUMBER(8)                     NOT NULL,
internal_code   VARCHAR2(30)                  not null,
internal_name   VARCHAR2(255),
stylesheet_name VARCHAR2(255),
schema_name     VARCHAR2(255),
schema_location VARCHAR2(255),
schema_version  VARCHAR2(30),
security_classification varchar2(30),
version         number(8))
TABLESPACE EXISSD
CACHE;

/* Wiki request 35 */

ALTER TABLE EXI_REF_TYPE
  MODIFY (VERSION  VARCHAR2(30));

/* Wiki request 35 end */

ALTER TABLE exi_ref_type ADD (
  CONSTRAINT type_id_pk PRIMARY KEY ( type_id )
    USING INDEX 
    TABLESPACE EXISSX);

ALTER TABLE exi_ref_type ADD (
  CONSTRAINT exi_ref_int_code_idx UNIQUE ( internal_code )
    USING INDEX 
    TABLESPACE EXISSX);

CREATE INDEX ref_type_operation_fk
  ON exi_ref_type  (operation_id)
TABLESPACE EXISSX;

ALTER TABLE exi_ref_type ADD (
  CONSTRAINT ref_type_operation_id_fk FOREIGN KEY (operation_id) 
    REFERENCES exi_ref_operation (operation_id));

CREATE INDEX ref_type_group_fk
  ON exi_ref_type ( group_id )
TABLESPACE EXISSX;

ALTER TABLE exi_ref_type ADD (
  CONSTRAINT ref_type_group_id_fk FOREIGN KEY (group_id) 
    REFERENCES exi_ref_group (group_id));

create public synonym exi_ref_type for EXISS.exi_ref_type;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_ref_type TO PUBLIC;


create table exi_ref_tracking_status (
status_id       NUMBER(8)                    NOT NULL,
internal_code   VARCHAR2(30)                 not null,
internal_name   VARCHAR2(255))
TABLESPACE EXISSD;

ALTER TABLE EXI_REF_TRACKING_STATUS
  ADD TRACKING_ENABLED VARCHAR2(1) DEFAULT 'Y';

ALTER TABLE EXI_REF_TRACKING_STATUS                                                           
 ADD ( CONSTRAINT REF_TRACKING_ENABLED_CHK CHECK                                       
 (TRACKING_ENABLED IN ('Y','N') AND TRACKING_ENABLED IS NOT NULL));
 
ALTER TABLE exi_ref_tracking_status ADD (
  CONSTRAINT status_id_pk PRIMARY KEY ( status_id )
    USING INDEX 
    TABLESPACE EXISSX);

ALTER TABLE exi_ref_tracking_status ADD (
  CONSTRAINT exi_ref_trk_int_code_idx UNIQUE ( internal_code )
    USING INDEX 
    TABLESPACE EXISSX);


create public synonym exi_ref_tracking_status for EXISS.exi_ref_tracking_status;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_ref_tracking_status TO PUBLIC;


create table exi_item_outbound (
ITEM_ID         NUMBER(16)                    NOT NULL,
TYPE_ID         NUMBER(8)                     NOT NULL,
IDENTIFIER      VARCHAR2(255),
CREST_COURT_ID  VARCHAR2(3),
DESCRIPTION     VARCHAR2(255),
CLOB_DATA       CLOB                          NOT NULL,
ITEM_CREATED    DATE                          NOT NULL,
ITEM_EXPIRES    DATE)
TABLESPACE EXISSD;

ALTER TABLE exi_item_outbound ADD (
  CONSTRAINT item_id_pk PRIMARY KEY ( item_id )
    USING INDEX 
    TABLESPACE EXISSX);

CREATE INDEX item_outbound_type_fk 
  ON exi_item_outbound (type_id)
  TABLESPACE EXISSX;

ALTER TABLE exi_item_outbound ADD (
  CONSTRAINT item_outbound_type_id_fk FOREIGN KEY (type_id) 
    REFERENCES exi_ref_type (type_id));

create public synonym exi_item_outbound for EXISS.exi_item_outbound;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_item_outbound TO PUBLIC;


create table exi_item_outbound_tracking (
tracking_id     NUMBER(16)                   NOT NULL,
item_id         number(16)                   not null,
status_id       number(8)                    not null,
tracking_date   date                         not null)
TABLESPACE EXISSD;

ALTER TABLE EXI_ITEM_OUTBOUND_TRACKING
modify (tracking_date   timestamp);

ALTER TABLE exi_item_outbound_tracking ADD (
  CONSTRAINT tracking_id_pk PRIMARY KEY ( tracking_id )
    USING INDEX 
    TABLESPACE EXISSX);

CREATE INDEX item_outb_tracking_item_fk 
  ON exi_item_outbound_tracking (item_id)
  TABLESPACE EXISSX;


ALTER TABLE exi_item_outbound_tracking ADD (
  CONSTRAINT item_outb_tracking_item_id_fk FOREIGN KEY (item_id) 
    REFERENCES exi_item_outbound (item_id));

CREATE INDEX item_outb_tracking_stat_fk 
  ON exi_item_outbound_tracking (status_id)
  TABLESPACE EXISSX;


ALTER TABLE exi_item_outbound_tracking ADD (
  CONSTRAINT item_outb_tracking_stat_id_fk FOREIGN KEY (status_id) 
    REFERENCES exi_ref_tracking_status (status_id));

create public synonym exi_item_outbound_tracking for EXISS.exi_item_outbound_tracking;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_item_outbound_tracking TO PUBLIC;


create table exi_property (
property_id     NUMBER(8)                   NOT NULL,
property_type   varchar2(30)                not null,
property_code   varchar2(30)                not null,
decode_property varchar2(255)               not null,
property_title  varchar2(255),
property_order  number(8),
last_update_date date not null,
creation_date    date not null,
created_by      varchar2(30) not null,
last_updated_by varchar2(30) not null,
version         number(3),
obs_ind         varchar2(1 byte)   default 'N')
TABLESPACE EXISSD
CACHE;

ALTER TABLE exi_property                                                          
 ADD ( CONSTRAINT property_obs_ind_chk CHECK                                      
 (obs_ind IN ('Y','N') AND obs_ind IS NOT NULL));    

ALTER TABLE exi_property ADD (
  CONSTRAINT property_id_pk PRIMARY KEY ( property_id )
    USING INDEX 
    TABLESPACE EXISSX);

create public synonym exi_property for EXISS.exi_property;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_property TO PUBLIC;



create table exi_jms_property (
jms_property_id NUMBER(8)                   NOT NULL,
name            varchar2(255)               not null,
value           varchar2(255),
message_type    varchar2(255)               not null)
TABLESPACE EXISSD
CACHE;

ALTER TABLE exi_jms_property ADD (
  CONSTRAINT jms_property_id_pk PRIMARY KEY ( jms_property_id )
    USING INDEX 
    TABLESPACE EXISSX);

create public synonym exi_jms_property for EXISS.exi_jms_property;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_jms_property TO PUBLIC;



create table exi_jms_message (
item_id         NUMBER(16)                  NOT NULL,
target          varchar2(30)                not null,
item_type       varchar2(30)                not null)
TABLESPACE EXISSD;

ALTER TABLE exi_jms_message ADD (
  CONSTRAINT jms_message_item_id_pk PRIMARY KEY ( item_id )
    USING INDEX 
    TABLESPACE EXISSX);

ALTER TABLE exi_jms_message ADD (
  CONSTRAINT jms_message_item_id_fk FOREIGN KEY (item_id) 
    REFERENCES exi_item_outbound (item_id));

create public synonym exi_jms_message for EXISS.exi_jms_message;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_jms_message TO PUBLIC;


CREATE TABLE exi_sys_user_information
(
  mercator_user_name         VARCHAR2(255 BYTE) NOT NULL,
  connection_pool_user_name  VARCHAR2(255 BYTE) NOT NULL
)
TABLESPACE EXISSD;

create public synonym exi_sys_user_information for EXISS.exi_sys_user_information;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_sys_user_information TO PUBLIC;


CREATE TABLE exi_sys_audit
(
  sys_audit_id    NUMBER                        NOT NULL,
  table_to_audit  VARCHAR2(255 BYTE)            NOT NULL,
  audit_table     VARCHAR2(255 BYTE)            NOT NULL,
  auditable       VARCHAR2(1 BYTE)              NOT NULL
)
TABLESPACE EXISSD;

ALTER TABLE exi_sys_audit ADD (
  CONSTRAINT sys_audit_pk PRIMARY KEY (sys_audit_id)
    USING INDEX
    TABLESPACE EXISSX);


CREATE UNIQUE INDEX sys_audit_table_to_aud_idx ON exi_sys_audit
(table_to_audit)
TABLESPACE EXISSX;

create public synonym exi_sys_audit for EXISS.exi_sys_audit;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_sys_audit TO PUBLIC;


CREATE TABLE EXISS.EXI_VERSION
(
  SCHEMA_NAME       VARCHAR2(10 BYTE)           NOT NULL,
  SCHEMA_VERSION    VARCHAR2(12 BYTE),
  LAST_UPDATE_DATE  DATE,
  UPDATED_BY        VARCHAR2(30 BYTE),
  DISPLAY_NAME      VARCHAR2(30 BYTE),
  DISPLAY_SEQ       NUMBER(2)
)
TABLESPACE EXISSD;

create public synonym EXI_VERSION for EXISS.EXI_VERSION;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.EXI_VERSION TO PUBLIC;


CREATE TABLE exi_item_inbound (
item_id                NUMBER(16)     NOT NULL,
message                CLOB,
date_created           DATE)
TABLESPACE exissd;

ALTER TABLE exi_item_inbound ADD (
  CONSTRAINT inbound_item_id_pk PRIMARY KEY ( item_id )
    USING INDEX 
    TABLESPACE exissx);

CREATE PUBLIC SYNONYM exi_item_inbound for EXISS.exi_item_inbound;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_item_inbound TO PUBLIC;


CREATE TABLE exi_item_inbound_properties (
item_property_id  NUMBER(16)                    NOT NULL,
item_id           NUMBER(16)                    NOT NULL,
property_name     VARCHAR2(255)                 not null,
property_value    VARCHAR2(255))
TABLESPACE exissd;

ALTER TABLE exi_item_inbound_properties ADD (
  CONSTRAINT item_property_id_pk PRIMARY KEY ( item_property_id )
    USING INDEX 
    TABLESPACE exissx);

CREATE INDEX item_inbound_fk_idx 
  ON exi_item_inbound_properties  (item_id)
TABLESPACE exissx;

ALTER TABLE exi_item_inbound_properties ADD (
  CONSTRAINT item_id_fk FOREIGN KEY (item_id) 
    REFERENCES exi_item_inbound (item_id));

CREATE PUBLIC SYNONYM exi_item_inbound_properties for EXISS.exi_item_inbound_properties;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_item_inbound_properties TO PUBLIC;


CREATE TABLE exi_crest_org_unit_lookup (
  crest_org_unit_id  NUMBER(16),
  crest_court_code   VARCHAR2(10)          NOT NULL,
  org_unit_code      VARCHAR2(10)          NOT NULL)
TABLESPACE exissd;

ALTER TABLE exi_crest_org_unit_lookup ADD (
  CONSTRAINT crest_org_unit_id_pk PRIMARY KEY (crest_org_unit_id)
    USING INDEX 
    TABLESPACE exissx);
    
CREATE UNIQUE INDEX crest_court_code_idx
ON exi_crest_org_unit_lookup (crest_court_code)
TABLESPACE exissx;

CREATE UNIQUE INDEX org_unit_code_idx
ON exi_crest_org_unit_lookup (org_unit_code)
TABLESPACE exissx;

CREATE PUBLIC SYNONYM exi_crest_org_unit_lookup for EXISS.exi_crest_org_unit_lookup;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_crest_org_unit_lookup TO PUBLIC;

CREATE TABLE  exi_table_timestamp
 ( name VARCHAR2(30) NOT NULL UNIQUE,
 last_updated  DATE DEFAULT SYSDATE );

CREATE PUBLIC SYNONYM exi_table_timestamp for EXISS.exi_table_timestamp;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.exi_table_timestamp TO PUBLIC;



/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any EXI_ table modifications
 */

create table aud_property (
property_id     NUMBER(8),
property_type   varchar2(30),
property_code   varchar2(30),
decode_property varchar2(255),
property_title  varchar2(255),
property_order  number(8),
last_update_date date,
creation_date    date,
created_by      varchar2(30),
last_updated_by varchar2(30),
version         number(5),
insert_event    varchar2(1) not null)
tablespace AUDIEXI
;

create public synonym aud_property for EXISS.aud_property;

GRANT DELETE, INSERT, SELECT, UPDATE ON  EXISS.aud_property TO PUBLIC;


/*
 * Changes, additions or deletion of sequences
 */


create sequence exi_ref_operation_seq start with 1 increment by 1 nocache;
create sequence exi_ref_group_seq start with 1 increment by 1 nocache;
create sequence exi_ref_type_seq start with 1 increment by 1 nocache;
create sequence exi_ref_tracking_status_seq start with 1 increment by 1 nocache;
create sequence exi_item_outbound_seq start with 1 increment by 1 nocache;
create sequence exi_item_outbound_tracking_seq start with 1 increment by 1 nocache;
create sequence exi_property_seq start with 1 increment by 1 nocache;
create sequence exi_jms_property_seq start with 1 increment by 1 nocache;
create sequence exi_sys_audit_seq start with 1 increment by 1 nocache;
create sequence exi_item_inbound_props_seq start with 1 increment by 1 nocache;
create sequence exi_item_inbound_seq start with 1 increment by 1 nocache;
create sequence exi_crest_org_unit_lookup_seq start with 1 increment by 1 nocache;


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@@exi_code_release_control_811.sql


/*
 * Changes to EXI_ table triggers as a result of any EXI_ table modifications
 */

create or replace trigger exi_ref_operation_bir_tr
    before insert on exi_ref_operation
    for each row
begin
    if :new.operation_id is null then
        select exi_ref_operation_seq.nextval
        into  :new.operation_id
        from   dual;
    end if;
end;
/

show errors;


create or replace trigger exi_ref_group_bir_tr
    before insert on exi_ref_group
    for each row
begin
    if :new.group_id is null then
        select exi_ref_group_seq.nextval
        into  :new.group_id
        from   dual;
    end if;
end;
/

show errors;


create or replace trigger exi_ref_type_bir_tr
    before insert on exi_ref_type
    for each row
begin
    if :new.type_id is null then
        select exi_ref_type_seq.nextval
        into  :new.type_id
        from   dual;
    end if;
end;
/

show errors;


create or replace trigger exi_ref_tracking_status_bir_tr
    before insert on exi_ref_tracking_status
    for each row
begin
    if :new.status_id is null then
        select exi_ref_tracking_status_seq.nextval
        into  :new.status_id
        from   dual;
    end if;
end;
/

show errors;

create or replace trigger exi_item_outbound_bir_tr
    before insert on exi_item_outbound
    for each row
begin
    if :new.item_id is null then
        select exi_item_outbound_seq.nextval
        into  :new.item_id
        from   dual;
    end if;
end;
/

show errors;


create or replace trigger exi_item_outbound_air_tr
    after insert on exi_item_outbound
    for each row
declare
  l_item_type    exi_ref_type.internal_code%TYPE;  
begin

   SELECT DECODE( ero.internal_code, 'DOCUMENT', ert.internal_code, 'EVENT' )
   INTO   l_item_type
   FROM   exi_ref_type ert,
          exi_ref_operation ero
   WHERE  ert.type_id = :NEW.TYPE_ID
   AND    ert.operation_id = ero.operation_id;

   exi_jms_message_pkg.create_jms_message( 
          :NEW.item_id,
          'EXISS',
           l_item_type);

end;
/

show errors;

CREATE OR REPLACE TRIGGER exi_item_outbound_air_tr2
    after insert on exi_item_outbound
    for each row
declare
     l_status_id exi_ref_tracking_status.status_id%TYPE;
begin
    SELECT status_id
    INTO   l_status_id
    FROM   exi_ref_tracking_status
    WHERE  internal_code = 'NEW_ITEM';

    INSERT INTO exi_item_outbound_tracking (item_id, status_id, tracking_date ) 
	VALUES ( :NEW.item_id, l_status_id, SYSTIMESTAMP );
end;
/

show errors;


create or replace trigger exi_item_outbnd_trckng_bir_tr
    before insert on exi_item_outbound_tracking
    for each row
begin
    if :new.tracking_id is null then
        select exi_item_outbound_tracking_seq.nextval
        into  :new.tracking_id
        from   dual;
    end if;
end;
/

show errors;


create or replace trigger exi_property_bir_tr
    before insert on exi_property
    for each row
begin
    if :new.property_id is null then
        select exi_property_seq.nextval
        into  :new.property_id
        from   dual;
    end if;

    if ((:new.last_updated_by IS NULL)  OR
        (:new.created_by      IS NULL)) THEN

        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
               SYS_CONTEXT('USERENV', 'SESSION_USER')
        INTO   :new.last_updated_by,
               :new.created_by
        FROM   DUAL;

    end if;

    SELECT SYSDATE,
           SYSDATE,
           1
    INTO  :new.last_update_date,
          :new.creation_date,
          :new.version
    FROM   DUAL;
end;
/

show errors;


create or replace trigger exi_property_bur_tr
    before update or delete
    on exi_property
    for each row
declare
    l_trig_event varchar2(1) := null;
    
    optimistic_lock_prob exception;
    pragma exception_init(optimistic_lock_prob, -20101);
begin
    if updating then
        l_trig_event := 'U';

        /* If the user is the connection pool user as defined in EXI_SYS_USER_INFORMATION */
        IF (EXI_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN
            IF (:OLD.VERSION != :NEW.VERSION) THEN
                /* Someone has pulled the rug out from below! */
                RAISE OPTIMISTIC_LOCK_PROB;
            END IF;
        END IF;

        
        :NEW.VERSION := :OLD.VERSION + 1;
        :NEW.LAST_UPDATE_DATE := SYSDATE;
        

        /* If the user is not the connection pool user as defined in EXI_SYS_USER_INFORMATION */
        IF (EXI_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
            :NEW.LAST_UPDATED_BY := SYS_CONTEXT('USERENV', 'SESSION_USER');            
        END IF;
    ELSE -- Must be DELETING
        l_trig_event := 'D';
    END IF;

    /* Is Auditing on this table required */
    IF (EXI_CUSTOM_PKG.IS_AUDIT_REQUIRED('EXI_PROPERTY') = 1) THEN
        INSERT INTO AUD_PROPERTY
		(property_ID,
		property_TYPE,
		property_CODE,
		decode_property,
		property_title,
		property_ORDER,
		LAST_UPDATE_DATE,
		CREATION_DATE,
		CREATED_BY,
		LAST_UPDATED_BY,
		VERSION,
		insert_event)
        VALUES (:OLD.property_ID,
                :OLD.property_TYPE,
                :OLD.property_CODE,
                :OLD.decode_property,
                :OLD.property_title,
                :OLD.property_ORDER,
                :OLD.LAST_UPDATE_DATE,
                :OLD.CREATION_DATE,
                :OLD.CREATED_BY,
                :OLD.LAST_UPDATED_BY,
                :OLD.VERSION,
                 l_trig_event);
    END IF;  
end;
/

show errors;

create or replace trigger exi_jms_property_bir_tr
    before insert on exi_jms_property
    for each row
begin
    if :new.jms_property_id is null then
        select exi_jms_property_seq.nextval
        into  :new.jms_property_id
        from   dual;
    end if;
end;
/

show errors;


CREATE OR REPLACE TRIGGER exi_sys_audit_bir_tr
   BEFORE INSERT
   ON exi_sys_audit
   FOR EACH ROW
DECLARE
BEGIN
   IF :NEW.sys_audit_id IS NULL
   THEN
      SELECT exi_sys_audit_seq.NEXTVAL
        INTO :NEW.sys_audit_id
        FROM DUAL;
   END IF;
END;
/

show errors;


create or replace trigger exi_item_inbound_props_bir_tr
    before insert on exi_item_inbound_properties
    for each row
begin
    if :new.item_property_id is null then
        select exi_item_inbound_props_seq.nextval
        into  :new.item_property_id
        from   dual;
    end if;
end;
/

show errors;

create or replace trigger exi_item_inbound_bir_tr
    before insert on exi_item_inbound
    for each row
begin
    if :new.item_id is null then
        select exi_item_inbound_seq.nextval
        into  :new.item_id
        from   dual;
    end if;
end;
/

show errors;


CREATE OR REPLACE TRIGGER exi_item_outbound_air_tr
    AFTER INSERT ON EXI_ITEM_OUTBOUND
    FOR EACH ROW
DECLARE
  l_item_type    EXI_REF_TYPE.internal_code%TYPE;
BEGIN
   SELECT DECODE( ero.internal_code, 'DOCUMENT', ert.internal_code, 'DELIVERERROR', ert.internal_code, 'EVENT' )
   INTO   l_item_type
   FROM   EXI_REF_TYPE ert,
          EXI_REF_OPERATION ero
   WHERE  ert.type_id = :NEW.TYPE_ID
   AND    ert.operation_id = ero.operation_id;
   exi_jms_message_pkg.create_jms_message(
          :NEW.item_id,
          'EXISS',
           l_item_type);
END;
/

show errors;


CREATE OR REPLACE TRIGGER exi_property_au_tr
   AFTER UPDATE ON exi_property   
BEGIN
       
       update exi_table_timestamp
	   set last_updated = SYSDATE
	   where name = 'EXI_PROPERTY';
	   	 
END;
/

show errors;

CREATE OR REPLACE TRIGGER exi_property_bur_tr
   BEFORE UPDATE OF decode_property 
   ON exi_property   
   FOR EACH ROW
BEGIN
       IF :NEW.PROPERTY_CODE = 'DISABLE SCJSE GATEWAY'
	   THEN
	       :NEW.DECODE_PROPERTY := UPPER( LTRIM( RTRIM(:NEW.DECODE_PROPERTY)) );
	       IF :NEW.DECODE_PROPERTY NOT IN ('TRUE', 'FALSE') THEN
		       RAISE_APPLICATION_ERROR( -20000, 'Invalid value. Value must be TRUE or FALSE');
               END IF;	   
       END IF;	          	   	 
END;
/

show errors;

CREATE OR REPLACE TRIGGER EXI_CREST_ORG_UNIT_LOOK_BIR_TR
   BEFORE INSERT
   ON EXI_CREST_ORG_UNIT_LOOKUP
   FOR EACH ROW
BEGIN
   IF :NEW.crest_org_unit_id IS NULL
   THEN
      SELECT exi_crest_org_unit_lookup_seq.NEXTVAL
        INTO :NEW.crest_org_unit_id
        FROM DUAL;
   END IF;

END;
/

show errors;
/*
 * Changes, additions or deletion of standing data
 */

@@exi_data_load.sql

/*
 * Updating of table VERSION for EXISS
 */

DELETE FROM EXI_VERSION;


INSERT INTO EXI_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('EXISS','8_1_1',sysdate,'EXISS','EXISS Database Schema 8_1_1',1);

COMMIT;

spool off
