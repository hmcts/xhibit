/*
 * Filename:    Xhibit_DB_Patch_8_2.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 *  13/09/2007	D RAI			Changes for release 8.2
 
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename





/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

 rem XHB_DEFENDANT_ON_OFFENCE
rem ========================
alter table
xhb_defendant_on_offence add
(
    seq_no                 number(3),
    arrest_date            date,
    charge_date            date,
    is_committed_on_bail   varchar2(1)
);

rem DON'T DO THE FOLLOWING UNTIL THE DATA MIGRATION IS COMPLETE
rem alter table
rem xhb_defendant_on_offence
rem set unused column crn_id;



rem XHB_OFFENCE
rem ===========
alter table
xhb_offence add
(
    start_date            date,
    end_date              date,
    force_location_code   varchar2(2),
    location_address_id   number(8)
);

alter table
xhb_offence add
(
    constraint location_address_id_fk foreign key (location_address_id) 
    references xhb_address (address_id)
);



rem XHB_CASE
rem ========
alter table
xhb_case add
(
    ccc_trans_from_ref_court_id   number(8),
    date_trans_from               date,
    retrial                       varchar2(1),
    original_case_number          varchar2(9),
    lc_sent_date                  date,
    no_cb_pros_witness            number(3),
    no_other_pros_witness         number(3)
);

alter table
xhb_case add
(
    constraint case_ccc_trans_fr_ref_ct_id_fk foreign key (ccc_trans_from_ref_court_id) 
    references xhb_ref_court (ref_court_id)
);



rem XHB_DEFENDANT_ON_CASE
rem =====================
alter table
xhb_defendant_on_case add
(
    asn   varchar2(20)
);

alter table
xhb_defendant_on_case add
(
    bench_warrant_exec_date   date,
    comm_bc_status            varchar2(1),
    bc_status_bw_executed     varchar2(1),
    special_cir_found         varchar2(1)
);









 /*
  * Additions or deletion of XHB_ tables, indexes and foreign keys
  */


rem XHB_INDICTMENT_HISTORY
rem ======================
create table
xhb_indictment_history
(
    indictment_history_id     number(8)       not null,
    extension_date            date,
    extended_to_date          date,
    crest_ihi_id              number(9),
    case_id                   number(8),
    last_update_date          date            not null,
    creation_date             date            not null,
    created_by                varchar2(30)    not null,
    last_updated_by           varchar2(30)    not null,
    version                   number(5)
)
tablespace xhibitd;

create unique index
indictment_history_pk
on xhb_indictment_history (indictment_history_id)
tablespace xhibitx;

alter table
xhb_indictment_history add
(
    constraint indictment_history_pk primary key (indictment_history_id)
    using index
    tablespace xhibitx
);

alter table
xhb_indictment_history add
(
    constraint case_id_fk foreign key (case_id) 
    references xhb_case (case_id)
);




rem XHB_BAIL_APPLICATION
rem ====================
create table
xhb_bail_application
(
    bail_application_id    number(8)        not null,
    defendant_on_case_id   number(8),
    result_date            date,
    result                 varchar2(1),
    applicant              varchar2(1),
    bail_type              varchar2(1),
    applic_type            varchar2(1),
    bail_no                number(8),
    bad_bail_type          varchar2(1),
    last_update_date       date             not null,
    creation_date          date             not null,
    created_by             varchar2(30)     not null,
    last_updated_by        varchar2(30)     not null,
    version                number(5)
)
tablespace xhibitd;

create unique index
bail_application_pk
on xhb_bail_application (bail_application_id)
tablespace xhibitx;

alter table
xhb_bail_application add
(
    constraint bail_application_pk primary key (bail_application_id)
    using index
    tablespace xhibitx
);

alter table
xhb_bail_application add
(
    constraint defendant_on_case_id_fk foreign key (defendant_on_case_id) 
    references xhb_defendant_on_case (defendant_on_case_id)
);



rem XHB_RS_CASE
rem ===========
create table
xhb_rs_case
(
    rs_case_id             number(8)        not null,
    case_id                number(8),
    rs_case_type           varchar2(1),
    rs_case_number         varchar2(8),
    last_update_date       date             not null,
    creation_date          date             not null,
    created_by             varchar2(30)     not null,
    last_updated_by        varchar2(30)     not null,
    version                number(5)
)
tablespace xhibitd;

create unique index
rs_case_pk
on xhb_rs_case (rs_case_id)
tablespace xhibitx;

alter table
xhb_rs_case add
(
    constraint rs_case_pk primary key (rs_case_id)
    using index
    tablespace xhibitx
);

alter table
xhb_rs_case add
(
    constraint rs_case_id_fk foreign key (case_id) 
    references xhb_case (case_id)
);



rem XHB_PROSECUTOR_REF_SOL_FIRM
rem ===========================
create table
xhb_prosecutor_ref_sol_firm
(
    prosecutor_ref_sol_firm_id   number(8)        not null,
    crest_cpf_id                 number(8),
    rep_type                     varchar2(1),
    rep_st_date                  date,
    rep_end_date                 date,
    ref_solicitor_firm_id        number(8),
    case_pros_agency_id          number(8),
    last_update_date             date             not null,
    creation_date                date             not null,
    created_by                   varchar2(30)     not null,
    last_updated_by              varchar2(30)     not null,
    version                      number(5)
)
tablespace xhibitd;

create unique index
prosecutor_ref_sol_firm_pk
on xhb_prosecutor_ref_sol_firm (prosecutor_ref_sol_firm_id)
tablespace xhibitx;

alter table
xhb_prosecutor_ref_sol_firm add
(
    constraint prosecutor_ref_sol_firm_pk primary key (prosecutor_ref_sol_firm_id)
    using index
    tablespace xhibitx
);

alter table
xhb_prosecutor_ref_sol_firm add
(
    constraint ref_solicitor_firm_id foreign key (ref_solicitor_firm_id) 
    references xhb_ref_solicitor_firm (ref_solicitor_firm_id)
);

alter table
xhb_prosecutor_ref_sol_firm add
(
    constraint case_pros_agency_id_fk foreign key (case_pros_agency_id) 
    references xhb_case_prosecutor_agency (case_pros_agency_id)
);



rem XHB_1745_DATA_MIGRATION_TOTALS
rem ==============================
create table
xhb_1745_data_migration_totals
(
    court_id                     number(8)        not null,
    total_defendants_on_case     number(8),
    total_defendants_on_offence  number(8),
    status                       varchar2(1)
)
tablespace xhibitd;

create unique index
data_migration_totals_pk
on xhb_1745_data_migration_totals (court_id)
tablespace xhibitx;

alter table
xhb_1745_data_migration_totals add
(
    constraint data_migration_totals_pk primary key (court_id)
    using index
    tablespace xhibitx
);

alter table
xhb_1745_data_migration_totals add
(
    constraint data_migration_totals_fk foreign key (court_id) 
    references xhb_court (court_id)
);








/*
 * Changes, additions or deletion of views
 */

create or replace view xhb_original_charges_chgs_v as
select doo.defendant_on_case_id,
       doo.defendant_on_offence_id,
       doo.seq_no,
       o.offence_id,
       o.crest_offence_freetext,
       o.crest_offence_seq_no,
       ro.ref_offence_id,
       ro.offence_desc,
       ro.offence_code,
       c.charge_id,
       c.charge_type,
       c.crest_charge_seq_no
from   xhb_defendant_on_offence    doo,
       xhb_offence                 o,
       xhb_ref_offence             ro,
       xhb_charge                  c
where  doo.offence_id           =  o.offence_id
and    o.ref_offence_id         =  ro.ref_offence_id
and    o.charge_id              =  c.charge_id
and   (doo.obs_ind   is null or doo.obs_ind = 'N')
and   (o.obs_ind     is null or o.obs_ind   = 'N')
and   (ro.obs_ind    is null or ro.obs_ind  = 'N')
and   (c.obs_ind     is null or c.obs_ind   = 'N');



create or replace view xhb_original_charges_defs_v as
select case.court_id,
       case.case_id,
       case.case_type,
       case.case_number,
       doc.defendant_on_case_id,
       doc.asn,
       d.defendant_id,
       d.first_name,
       d.middle_name,
       d.surname,
       count(decode(c.charge_type,'I',1,null)) total_indictments,
       count(decode(c.charge_type,'G',1,null)) total_original_charges
from   xhb_case                 case,
       xhb_defendant            d,
       xhb_defendant_on_case    doc,
       xhb_defendant_on_offence doo,
       xhb_offence              o,
       xhb_charge               c
where  case.case_id             =  doc.case_id
and    doc.defendant_id         =  d.defendant_id
and    doc.defendant_on_case_id =  doo.defendant_on_case_id
and    doo.offence_id           =  o.offence_id
and    o.charge_id              =  c.charge_id
and   (doc.obs_ind is null or doc.obs_ind = 'N')
and   (doo.obs_ind is null or doo.obs_ind = 'N')
and   (o.obs_ind   is null or o.obs_ind   = 'N')
and   (c.obs_ind   is null or c.obs_ind   = 'N')
group by case.court_id,
         case.case_id,
         case.case_type,
         case.case_number,
         doc.defendant_on_case_id,
         doc.asn,
         d.defendant_id,
         d.first_name,
         d.middle_name,
         d.surname;





/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */

rem AUD_DEFENDANT_ON_OFFENCE
rem ========================
alter table
aud_defendant_on_offence add
(
    seq_no                 number(3),
    arrest_date            date,
    charge_date            date,
    is_committed_on_bail   varchar2(1)
);




rem AUD_OFFENCE
rem ===========
alter table
aud_offence add
(
    start_date            date,
    end_date              date,
    force_location_code   varchar2(2),
    location_address_id   number(8)
);




rem AUD_CASE
rem ========
alter table
aud_case add
(
    ccc_trans_from_ref_court_id   number(8),
    date_trans_from               date,
    retrial                       varchar2(1),
    original_case_number          varchar2(9),
    lc_sent_date                  date,
    no_cb_pros_witness            number(3),
    no_other_pros_witness         number(3)
);




rem AUD_DEFENDANT_ON_CASE
rem =====================
alter table
aud_defendant_on_case add
(
    asn   varchar2(20)
);

alter table
aud_defendant_on_case add
(
    bench_warrant_exec_date   date,
    comm_bc_status            varchar2(1),
    bc_status_bw_executed     varchar2(1),
    special_cir_found         varchar2(1)
);


rem AUD_INDICTMENT_HISTORY
rem ======================
create table
AUD_indictment_history
(
    indictment_history_id     number(8)       not null,
    extension_date            date,
    extended_to_date          date,
    crest_ihi_id              number(9),
    case_id                   number(8),
    last_update_date          date            not null,
    creation_date             date            not null,
    created_by                varchar2(30)    not null,
    last_updated_by           varchar2(30)    not null,
    version                   number(5),
    insert_event              varchar2(1)
)
tablespace auditd;






rem AUD_BAIL_APPLICATION
rem ====================
create table
aud_bail_application
(
    bail_application_id    number(8)        not null,
    defendant_on_case_id   number(8),
    result_date            date,
    result                 varchar2(1),
    applicant              varchar2(1),
    bail_type              varchar2(1),
    applic_type            varchar2(1),
    bail_no                number(8),
    bad_bail_type          varchar2(1),
    last_update_date       date             not null,
    creation_date          date             not null,
    created_by             varchar2(30)     not null,
    last_updated_by        varchar2(30)     not null,
    version                number(5),
    insert_event           varchar2(1)
)
tablespace auditd;




rem AUD_RS_CASE
rem ===========
create table
aud_rs_case
(
    rs_case_id             number(8)        not null,
    case_id                number(8),
    rs_case_type           varchar2(1),
    rs_case_number         varchar2(8),
    last_update_date       date             not null,
    creation_date          date             not null,
    created_by             varchar2(30)     not null,
    last_updated_by        varchar2(30)     not null,
    version                number(5),
    insert_event           varchar2(1)
)
tablespace auditd;



rem AUD_PROSECUTOR_REF_SOL_FIRM
rem ===========================
create table
aud_prosecutor_ref_sol_firm
(
    prosecutor_ref_sol_firm_id   number(8)        not null,
    crest_cpf_id                 number(8),
    rep_type                     varchar2(1),
    rep_st_date                  date,
    rep_end_date                 date,
    ref_solicitor_firm_id        number(8),
    case_pros_agency_id          number(8),
    last_update_date             date             not null,
    creation_date                date             not null,
    created_by                   varchar2(30)     not null,
    last_updated_by              varchar2(30)     not null,
    version                      number(5),
    insert_event                 varchar2(1)
)
tablespace auditd;






/*
 * Changes, additions or deletion of sequences
 */

create sequence xhb_pros_ref_sol_firm_seq start with 1 increment by 1 nocache;
create sequence xhb_rs_case_seq start with 1 increment by 1 nocache;
create sequence xhb_bail_application_seq start with 1 increment by 1 nocache;
create sequence xhb_indictment_history_seq start with 1 increment by 1 nocache;






/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@@xhb_code_release_control_82.sql







/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

CREATE OR REPLACE TRIGGER xhb_pros_ref_sol_firm_bir_tr
    before insert on xhb_prosecutor_ref_sol_firm
    for each row
begin
    if :new.prosecutor_ref_sol_firm_id is null then
        select xhb_pros_ref_sol_firm_seq.nextval
        into  :new.prosecutor_ref_sol_firm_id
        from   dual;
    end if;

    IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
        (:NEW.CREATED_BY IS NULL)) THEN

        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
               SYS_CONTEXT('USERENV', 'SESSION_USER')
        INTO   :NEW.LAST_UPDATED_BY,
               :NEW.CREATED_BY
        FROM   DUAL;

    END IF;

    SELECT SYSDATE,
           SYSDATE,
           1
    INTO   :NEW.LAST_UPDATE_DATE,
           :NEW.CREATION_DATE,
           :NEW.VERSION
    FROM   DUAL;
end;
/

CREATE OR REPLACE TRIGGER xhb_rs_case_bir_tr
    before insert on xhb_rs_case
    for each row
begin
    if :new.rs_case_id is null then
        select xhb_rs_case_seq.nextval
        into  :new.rs_case_id
        from   dual;
    end if;


    IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
        (:NEW.CREATED_BY IS NULL)) THEN

        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
               SYS_CONTEXT('USERENV', 'SESSION_USER')
        INTO   :NEW.LAST_UPDATED_BY,
               :NEW.CREATED_BY
        FROM   DUAL;

    END IF;

    SELECT SYSDATE,
           SYSDATE,
           1
    INTO   :NEW.LAST_UPDATE_DATE,
           :NEW.CREATION_DATE,
           :NEW.VERSION
    FROM   DUAL;

end;
/

CREATE OR REPLACE TRIGGER xhb_bail_application_bir_tr
    before insert on xhb_bail_application
    for each row
begin
    if :new.bail_application_id is null then
        select xhb_bail_application_seq.nextval
        into  :new.bail_application_id
        from   dual;
    end if;

    IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
        (:NEW.CREATED_BY IS NULL)) THEN

        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
               SYS_CONTEXT('USERENV', 'SESSION_USER')
        INTO   :NEW.LAST_UPDATED_BY,
               :NEW.CREATED_BY
        FROM   DUAL;

    END IF;

    SELECT SYSDATE,
           SYSDATE,
           1
    INTO   :NEW.LAST_UPDATE_DATE,
           :NEW.CREATION_DATE,
           :NEW.VERSION
    FROM   DUAL;
end;
/

CREATE OR REPLACE TRIGGER xhb_indictment_history_bir_tr
    before insert on xhb_indictment_history
    for each row
begin
    if :new.indictment_history_id is null then
        select xhb_indictment_history_seq.nextval
        into  :new.indictment_history_id
        from   dual;
    end if;

    IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
        (:NEW.CREATED_BY IS NULL)) THEN

        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
               SYS_CONTEXT('USERENV', 'SESSION_USER')
        INTO   :NEW.LAST_UPDATED_BY,
               :NEW.CREATED_BY
        FROM   DUAL;

    END IF;

    SELECT SYSDATE,
           SYSDATE,
           1
    INTO   :NEW.LAST_UPDATE_DATE,
           :NEW.CREATION_DATE,
           :NEW.VERSION
    FROM   DUAL;
end;
/



DROP TRIGGER XHIBIT.XHB_DEFENDANTONOFFENCE_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_DEFENDANTONOFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_DEFENDANT_ON_OFFENCE   FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_OFFENCE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_OFFENCE
    VALUES (:OLD.DEFENDANT_ON_OFFENCE_ID,
            :OLD.APPEAL_AGAINST_TYPE,
            :OLD.DEFENDANT_ON_CASE_ID,
            :OLD.OFFENCE_ID,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            :OLD.OBS_IND,
            :OLD.IS_STAYED,
            :OLD.CRN_ID,
            :OLD.VCO_FLAG,
            :OLD.VCO_DATE,
            l_trig_event,
            :OLD.seq_no,
            :OLD.arrest_date,
            :OLD.charge_date,
            :OLD.is_committed_on_bail);

  END IF;

END;
/


DROP TRIGGER XHIBIT.XHB_OFFENCE_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_OFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_OFFENCE   FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_OFFENCE') = 1) THEN

    INSERT INTO AUD_OFFENCE
    VALUES (:OLD.OFFENCE_ID,
            :OLD.CREST_OFFENCE_ID,
            :OLD.CREST_OFFENCE_SEQ_NO,
            :OLD.CREST_OFFENCE_FREETEXT,
            :OLD.MULTIPLE,
            :OLD.CREST_HOO_CLASS_FREETEXT,
            :OLD.CREST_HOO_SUBCLASS_FREETEXT,
            :OLD.REF_OFFENCE_ID,
            :OLD.CHARGE_ID,
            :OLD.OBS_IND,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            :OLD.REF_SYSTEM_CODE_ID,
            l_trig_event,
            :OLD.start_date,
            :OLD.end_date,
            :OLD.force_location_code,
            :OLD.location_address_id);

  END IF;

END;
/


DROP TRIGGER XHIBIT.XHB_CASE_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_CASE   FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    IF :NEW.CASE_ID = :OLD.CASE_ID AND
       :NEW.COURT_ID = :OLD.COURT_ID AND
      (:NEW.CASE_NUMBER = :OLD.CASE_NUMBER OR
       (:NEW.CASE_NUMBER IS NULL AND :OLD.CASE_NUMBER IS NULL)) AND
      (:NEW.CASE_TYPE = :OLD.CASE_TYPE OR
       (:NEW.CASE_TYPE IS NULL AND :OLD.CASE_TYPE IS NULL)) AND
      (:NEW.MAG_CONVICTION_DATE = :OLD.MAG_CONVICTION_DATE OR
       (:NEW.MAG_CONVICTION_DATE IS NULL AND :OLD.MAG_CONVICTION_DATE IS NULL)) AND
      (:NEW.CASE_SUB_TYPE = :OLD.CASE_SUB_TYPE OR
       (:NEW.CASE_SUB_TYPE IS NULL AND :OLD.CASE_SUB_TYPE IS NULL)) AND
      (:NEW.CASE_TITLE = :OLD.CASE_TITLE OR
       (:NEW.CASE_TITLE IS NULL AND :OLD.CASE_TITLE IS NULL)) AND
      (:NEW.CASE_DESCRIPTION = :OLD.CASE_DESCRIPTION OR
       (:NEW.CASE_DESCRIPTION IS NULL AND :OLD.CASE_DESCRIPTION IS NULL)) AND
      (:NEW.LINKED_CASE_ID = :OLD.LINKED_CASE_ID OR
       (:NEW.LINKED_CASE_ID IS NULL AND :OLD.LINKED_CASE_ID IS NULL)) AND
      (:NEW.BAIL_MAG_CODE = :OLD.BAIL_MAG_CODE OR
       (:NEW.BAIL_MAG_CODE IS NULL AND :OLD.BAIL_MAG_CODE IS NULL)) AND
      (:NEW.REF_COURT_ID = :OLD.REF_COURT_ID OR
       (:NEW.REF_COURT_ID IS NULL AND :OLD.REF_COURT_ID IS NULL)) AND
      (:NEW.SEVERED_IND = :OLD.SEVERED_IND OR
       (:NEW.SEVERED_IND IS NULL AND :OLD.SEVERED_IND IS NULL)) AND
      (:NEW.INDICT_RESP = :OLD.INDICT_RESP OR
       (:NEW.INDICT_RESP IS NULL AND :OLD.INDICT_RESP IS NULL)) AND
      (:NEW.DATE_IND_REC = :OLD.DATE_IND_REC OR
       (:NEW.DATE_IND_REC IS NULL AND :OLD.DATE_IND_REC IS NULL)) AND
      (:NEW.PROS_AGENCY_REFERENCE = :OLD.PROS_AGENCY_REFERENCE OR
       (:NEW.PROS_AGENCY_REFERENCE IS NULL AND :OLD.PROS_AGENCY_REFERENCE IS NULL)) AND
      (:NEW.CASE_CLASS = :OLD.CASE_CLASS OR
       (:NEW.CASE_CLASS IS NULL AND :OLD.CASE_CLASS IS NULL)) AND
      (:NEW.JUDGE_REASON_FOR_APPEAL = :OLD.JUDGE_REASON_FOR_APPEAL OR
       (:NEW.JUDGE_REASON_FOR_APPEAL IS NULL AND :OLD.JUDGE_REASON_FOR_APPEAL IS NULL)) AND
      (:NEW.RESULTS_VERIFIED = :OLD.RESULTS_VERIFIED OR
       (:NEW.RESULTS_VERIFIED IS NULL AND :OLD.RESULTS_VERIFIED IS NULL)) AND
      (:NEW.LENGTH_TAPE = :OLD.LENGTH_TAPE OR
       (:NEW.LENGTH_TAPE IS NULL AND :OLD.LENGTH_TAPE IS NULL)) AND
      (:NEW.NO_PAGE_PROS_EVIDENCE = :OLD.NO_PAGE_PROS_EVIDENCE OR
       (:NEW.NO_PAGE_PROS_EVIDENCE IS NULL AND :OLD.NO_PAGE_PROS_EVIDENCE IS NULL)) AND
      (:NEW.NO_PROS_WITNESS = :OLD.NO_PROS_WITNESS OR
       (:NEW.NO_PROS_WITNESS IS NULL AND :OLD.NO_PROS_WITNESS IS NULL)) AND
      (:NEW.EST_PDH_TRIAL_LENGTH = :OLD.EST_PDH_TRIAL_LENGTH OR
       (:NEW.EST_PDH_TRIAL_LENGTH IS NULL AND :OLD.EST_PDH_TRIAL_LENGTH IS NULL)) AND
      (:NEW.INDICTMENT_INFO_1 = :OLD.INDICTMENT_INFO_1 OR
       (:NEW.INDICTMENT_INFO_1 IS NULL AND :OLD.INDICTMENT_INFO_1 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_2 = :OLD.INDICTMENT_INFO_2 OR
       (:NEW.INDICTMENT_INFO_2 IS NULL AND :OLD.INDICTMENT_INFO_2 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_3 = :OLD.INDICTMENT_INFO_3 OR
       (:NEW.INDICTMENT_INFO_3 IS NULL AND :OLD.INDICTMENT_INFO_3 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_4 = :OLD.INDICTMENT_INFO_4 OR
       (:NEW.INDICTMENT_INFO_4 IS NULL AND :OLD.INDICTMENT_INFO_4 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_5 = :OLD.INDICTMENT_INFO_5 OR
       (:NEW.INDICTMENT_INFO_5 IS NULL AND :OLD.INDICTMENT_INFO_5 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_6 = :OLD.INDICTMENT_INFO_6 OR
       (:NEW.INDICTMENT_INFO_6 IS NULL AND :OLD.INDICTMENT_INFO_6 IS NULL)) AND
      (:NEW.POLICE_OFFICER_ATTENDING = :OLD.POLICE_OFFICER_ATTENDING OR
       (:NEW.POLICE_OFFICER_ATTENDING IS NULL AND :OLD.POLICE_OFFICER_ATTENDING IS NULL)) AND
      (:NEW.CPS_CASE_WORKER = :OLD.CPS_CASE_WORKER OR
       (:NEW.CPS_CASE_WORKER IS NULL AND :OLD.CPS_CASE_WORKER IS NULL)) AND
      (:NEW.EXPORT_CHARGES = :OLD.EXPORT_CHARGES OR
       (:NEW.EXPORT_CHARGES IS NULL AND :OLD.EXPORT_CHARGES IS NULL)) AND
      (:NEW.MAGISTRATES_CASE_REF = :OLD.MAGISTRATES_CASE_REF OR
       (:NEW.MAGISTRATES_CASE_REF IS NULL AND :OLD.MAGISTRATES_CASE_REF IS NULL)) AND
      (:NEW.CLASS_CODE = :OLD.CLASS_CODE OR
       (:NEW.CLASS_CODE IS NULL AND :OLD.CLASS_CODE IS NULL)) AND
      (:NEW.OFFENCE_GROUP_UPDATE = :OLD.OFFENCE_GROUP_UPDATE OR
       (:NEW.OFFENCE_GROUP_UPDATE IS NULL AND :OLD.OFFENCE_GROUP_UPDATE IS NULL)) AND
      (:NEW.CCC_TRANS_TO_REF_COURT_ID = :OLD.CCC_TRANS_TO_REF_COURT_ID OR
       (:NEW.CCC_TRANS_TO_REF_COURT_ID IS NULL AND :OLD.CCC_TRANS_TO_REF_COURT_ID IS NULL)) AND
      (:NEW.RECEIPT_TYPE = :OLD.RECEIPT_TYPE OR
       (:NEW.RECEIPT_TYPE IS NULL AND :OLD.RECEIPT_TYPE IS NULL)) AND
      (:NEW.ccc_trans_from_ref_court_id = :OLD.ccc_trans_from_ref_court_id OR
       (:NEW.ccc_trans_from_ref_court_id IS NULL AND :OLD.ccc_trans_from_ref_court_id IS NULL)) AND
      (:NEW.date_trans_from = :OLD.date_trans_from OR
       (:NEW.date_trans_from IS NULL AND :OLD.date_trans_from IS NULL)) AND
      (:NEW.retrial = :OLD.retrial OR
       (:NEW.retrial IS NULL AND :OLD.retrial IS NULL)) AND
      (:NEW.original_case_number = :OLD.original_case_number OR
       (:NEW.original_case_number IS NULL AND :OLD.original_case_number IS NULL)) AND
      (:NEW.lc_sent_date = :OLD.lc_sent_date OR
       (:NEW.lc_sent_date IS NULL AND :OLD.lc_sent_date IS NULL)) AND
      (:NEW.no_cb_pros_witness = :OLD.no_cb_pros_witness OR
       (:NEW.no_cb_pros_witness IS NULL AND :OLD.no_cb_pros_witness IS NULL)) AND
      (:NEW.no_other_pros_witness = :OLD.no_other_pros_witness OR
       (:NEW.no_other_pros_witness IS NULL AND :OLD.no_other_pros_witness IS NULL)) THEN



      -- Only the CHARGE_IMPORT_INDICATOR has changed so do not increase VERSION
      -- This will even come into this section if the CHARGE_IMPORT_INDICATOR column is
      -- updated to the same value with all others staying the same

      SELECT SYSDATE
      INTO   :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    ELSE

      -- Other fields have changes so increase VERSION

      SELECT :OLD.VERSION + 1,
             SYSDATE
      INTO   :NEW.VERSION,
             :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    END IF;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE') = 1) THEN

    INSERT INTO AUD_CASE
    VALUES (:OLD.CASE_ID,
            :OLD.CASE_NUMBER,
            :OLD.CASE_TYPE,
            :OLD.MAG_CONVICTION_DATE,
            :OLD.CASE_SUB_TYPE,
            :OLD.CASE_TITLE,
            :OLD.CASE_DESCRIPTION,
            :OLD.LINKED_CASE_ID,
            :OLD.BAIL_MAG_CODE,
            :OLD.REF_COURT_ID,
            :OLD.COURT_ID,
            :OLD.CHARGE_IMPORT_INDICATOR,
            :OLD.SEVERED_IND,
            :OLD.INDICT_RESP,
            :OLD.DATE_IND_REC,
            :OLD.PROS_AGENCY_REFERENCE,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            :OLD.CASE_CLASS,
            :OLD.JUDGE_REASON_FOR_APPEAL,
            :OLD.RESULTS_VERIFIED,
            :OLD.LENGTH_TAPE,
            :OLD.NO_PAGE_PROS_EVIDENCE,
            :OLD.NO_PROS_WITNESS,
            :OLD.EST_PDH_TRIAL_LENGTH,
            :OLD.indictment_info_1,
            :OLD.indictment_info_2,
            :OLD.indictment_info_3,
            :OLD.indictment_info_4,
            :OLD.indictment_info_5,
            :OLD.indictment_info_6,
            :OLD.POLICE_OFFICER_ATTENDING,
            :OLD.CPS_CASE_WORKER,
            :OLD.EXPORT_CHARGES,
            :OLD.IND_CHANGE_STATUS,
            :OLD.MAGISTRATES_CASE_REF,
            :OLD.CLASS_CODE,
            :OLD.OFFENCE_GROUP_UPDATE,
            :OLD.CCC_TRANS_TO_REF_COURT_ID,
            :OLD.RECEIPT_TYPE,
	    l_trig_event,
	    :OLD.ccc_trans_from_ref_court_id,
	    :OLD.date_trans_from,
	    :OLD.retrial,
	    :OLD.original_case_number,
	    :OLD.lc_sent_date,
	    :OLD.no_cb_pros_witness,
	    :OLD.no_other_pros_witness);

  END IF;

END;
/



DROP TRIGGER XHIBIT.XHB_DEFENDANTONCASE_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_DEFENDANTONCASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_DEFENDANT_ON_CASE   FOR EACH ROW
/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEFENDANTONCASE_BUR_TR */
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_CASE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_CASE
	   (DEFENDANT_ON_CASE_ID,
            NO_OF_TICS,
            FINAL_DRIVING_LICENCE_STATUS,
            PTIURN,
            IS_JUVENILE,
            IS_MASKED,
            MASKED_NAME,
            CASE_ID,
            DEFENDANT_ID,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
            OBS_IND,
            results_verified,
            defendant_number,
            date_of_committal,
            PNC_ID,
            COLLECT_MAGISTRATE_COURT_ID,
            INSERT_EVENT,
	    current_BC_status,
	    asn,
            bench_warrant_exec_date,
            comm_bc_status,
            bc_status_bw_executed,
            special_cir_found)
    VALUES (:old.DEFENDANT_ON_CASE_ID,
            :old.NO_OF_TICS,
            :old.FINAL_DRIVING_LICENCE_STATUS,
            :old.PTIURN,
            :old.IS_JUVENILE,
            :old.IS_MASKED,
            :old.MASKED_NAME,
            :old.CASE_ID,
            :old.DEFENDANT_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.OBS_IND,
            :old.results_verified,
            :old.defendant_number,
            :old.date_of_committal,
            :old.PNC_ID,
            :old.COLLECT_MAGISTRATE_COURT_ID,
            l_trig_event,
	    :old.current_BC_status,
	    :old.asn,
            :old.bench_warrant_exec_date,
            :old.comm_bc_status,
            :old.bc_status_bw_executed,
            :old.special_cir_found);

  END IF;

END;
/




DROP TRIGGER XHIBIT.XHB_INDICTMENTHISTORY_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_INDICTMENTHISTORY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_INDICTMENT_HISTORY FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_INDICTMENT_HISTORY') = 1) THEN

    INSERT INTO AUD_INDICTMENT_HISTORY
    VALUES (:OLD.indictment_history_id,    
            :OLD.extension_date, 
            :OLD.extended_to_date, 
            :OLD.crest_ihi_id, 
            :OLd.case_id,
            :OLD.last_update_date,
            :OLD.creation_date, 
            :OLD.created_by,
            :OLD.last_updated_by, 
            :OLD.version,
            l_trig_event);

  END IF;

END;
/


DROP TRIGGER XHIBIT.XHB_BAILAPPLICATION_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_BAILAPPLICATION_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_BAIL_APPLICATION FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_BAIL_APPLICATION') = 1) THEN

    INSERT INTO AUD_BAIL_APPLICATION
    VALUES (:OLD.bail_application_id,
            :OLD.defendant_on_case_id,
            :OLD.result_date,
            :OLD.result,
            :OLD.applicant,
            :OLD.bail_type,
            :OLD.applic_type,
            :OLD.bail_no,
            :OLD.bad_bail_type,
            :OLD.last_update_date,
            :OLD.creation_date,
            :OLD.created_by,
            :OLD.last_updated_by,
            :OLD.version,
            l_trig_event);

  END IF;

END;
/


DROP TRIGGER XHIBIT.XHB_RSCASE_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_RSCASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_RS_CASE FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_RS_CASE') = 1) THEN

    INSERT INTO AUD_RS_CASE
    VALUES (:OLD.rs_case_id,
            :OLD.case_id,
            :OLD.rs_case_type,
            :OLD.rs_case_number,
            :OLD.last_update_date,
            :OLD.creation_date,
            :OLD.created_by,
            :OLD.last_updated_by,
            :OLD.version,
            l_trig_event);

  END IF;

END;
/


DROP TRIGGER XHIBIT.XHB_PROSECUTORREFSOLFRM_BUR_TR;

CREATE OR REPLACE TRIGGER XHIBIT.XHB_PROSECUTORREFSOLFRM_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PROSECUTOR_REF_SOL_FIRM') = 1) THEN

    INSERT INTO AUD_PROSECUTOR_REF_SOL_FIRM
    VALUES (:OLD.prosecutor_ref_sol_firm_id,
            :OLD.crest_cpf_id,
            :OLD.rep_type,
            :OLD.rep_st_date,
            :OLD.rep_end_date,
            :OLD.ref_solicitor_firm_id,
            :OLD.case_pros_agency_id,
            :OLD.last_update_date,
            :OLD.creation_date,
            :OLD.created_by,
            :OLD.last_updated_by,
            :OLD.version,
            l_trig_event);

  END IF;

END;
/



	



/*
 * Changes, additions or deletion of standing data
 */


@@xhb_data_load.sql






/*
 * Updating of table XHB_SYS_AUDIT
 */

insert into XHB_SYS_AUDIT (TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) VALUES ('XHB_INDICTMENT_HISTORY','AUD_INDICTMENT_HISTORY','Y');
insert into XHB_SYS_AUDIT (TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) VALUES ('XHB_BAIL_APPLICATION','AUD_BAIL_APPLICATION','Y');
insert into XHB_SYS_AUDIT (TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) VALUES ('XHB_RS_CASE','AUD_RS_CASE','Y');
insert into XHB_SYS_AUDIT (TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) VALUES ('XHB_PROSECUTOR_REF_SOL_FIRM','AUD_PROSECUTOR_REF_SOL_FIRM','Y');



   

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.2', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.2', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off
