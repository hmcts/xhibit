create or replace package xhb_custom_pkg is

        function is_connection_pool_user return number;
 
 function is_audit_required(table_name IN varchar2) return number;

end;
/

create or replace package body xhb_custom_pkg is

        function is_connection_pool_user return number is

        l_conn_user     varchar2(255);
        l_curr_user     varchar2(255);
        no_data         exception;
        no_sess_user    exception;

        begin
                select sys_context('USERENV', 'SESSION_USER')
                into l_curr_user
                from dual;

                if (l_curr_user is null) then
                        raise no_sess_user;
                end if;

                select connection_pool_user_name
                into l_conn_user
                from xhb_sys_user_information;

                if (l_conn_user is null) then
                        raise no_data;
                end if;

                if ( l_conn_user != l_curr_user) then

                        /* Not Conection Pool User */
                        return 0;

                else
                        /* Connection Pool User */
                        return 1;
                end if;

        exception
                when no_sess_user then
                DBMS_OUTPUT.PUT_LINE('SYS_CONTEXT DID NOT RETURN SESSION_USER - '||sqlerrm);
                return 1;
                when no_data then
                DBMS_OUTPUT.PUT_LINE('XHB_SYS_USER_INFORMATION IS EMPTY - '||sqlerrm);
                return 1;
                when others then
                DBMS_OUTPUT.PUT_LINE('ERROR ENCOUNTERED IN IS_CONNECTION_POOL_USER - '||sqlerrm);
                return 1;
        end is_connection_pool_user;

        function is_audit_required (table_name IN varchar2) return number is

 l_audit  varchar2(1);

 cursor c_audit is
  select auditable 
  from xhb_sys_audit
  where   table_to_audit = table_name;
  

        begin

  if (c_audit%ISOPEN) then
   close c_audit;
  end if;

  open c_audit;
  fetch c_audit into l_audit;
  close c_audit;

  if l_audit = 'Y' then
   /* Audit is Required */
   return 1;
  else
   /* Audit is not Required */
   return 0;
  end if;

 end;
end;
/




create or replace trigger TMP_ERROR_LOG_BIR_TR
  BEFORE INSERT
  on TMP_ERROR_LOG
  
  for each row
BEGIN
SELECT TMP_ERROR_LOG_SEQ.NEXTVAL INTO               :NEW.SEQ_NO  FROM DUAL;


end;
/





create or replace trigger XHB_ADDRESS_BIR_TR
  BEFORE INSERT
  on XHB_ADDRESS
  
  for each row
BEGIN
IF :NEW.ADDRESS_ID IS NULL THEN
  SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO               :NEW.ADDRESS_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_ADDRESS_BUR_TR
  BEFORE UPDATE
  on XHB_ADDRESS
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ADDRESS_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ADDRESS') = 1) THEN
insert into AUD_ADDRESS 
values (:old.ADDRESS_ID, 
        :old.ADDRESS_1, 
        :old.ADDRESS_2, 
        :old.ADDRESS_3, 
        :old.ADDRESS_4, 
        :old.TOWN, 
        :old.COUNTY, 
        :old.POSTCODE, 
        :old.COUNTRY, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/







create or replace trigger XHB_BREACH_BIR_TR
  BEFORE INSERT
  on XHB_BREACH
  
  for each row
BEGIN
IF :NEW.BREACH_ID IS NULL THEN
  SELECT XHB_BREACH_SEQ.NEXTVAL INTO                 :NEW.BREACH_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_BREACH_BUR_TR
  BEFORE UPDATE
  on XHB_BREACH
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_BREACH_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_BREACH') = 1) THEN
insert into AUD_BREACH 
values (:old.BREACH_ID, 
        :old.ORIGINAL_SENTENCE, 
        :old.ORIGINAL_SENTENCE_DATE, 
        :old.ORIGINAL_COURT_TYPE, 
        :old.DATE_PUT, 
        :old.BREACH_TYPE, 
        :old.BRING_BACK, 
        :old.CHARGE_ID, 
        :old.REF_COURT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.OBS_IND );
END IF;

end;
/








create or replace trigger XHB_CASE_BIR_TR
  BEFORE INSERT
  on XHB_CASE
  
  for each row
BEGIN
IF :NEW.CASE_ID IS NULL THEN
  SELECT XHB_CASE_SEQ.NEXTVAL INTO                 :NEW.CASE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_CASE_BUR_TR
  BEFORE UPDATE
  on XHB_CASE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CASE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE') = 1) THEN
insert into AUD_CASE 
values (:old.CASE_ID, 
        :old.CASE_NUMBER, 
        :old.CASE_TYPE, 
        :old.MAG_CONVICTION_DATE, 
        :old.CASE_SUB_TYPE, 
        :old.CASE_TITLE, 
        :old.CASE_DESCRIPTION, 
        :old.LINKED_CASE_ID, 
        :old.BAIL_MAG_CODE, 
        :old.REF_COURT_ID, 
        :old.COURT_ID, 
        :old.CHARGE_IMPORT_INDICATOR, 
        :old.SEVERED_IND, 
        :old.INDICT_RESP, 
        :old.DATE_IND_REC, 
        :old.PROS_AGENCY_REFERENCE, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.CASE_CLASS, 
        :old.LEASE_TIME, 
        :old.JUDGE_REASON_FOR_APPEAL, 
        :old.RESULTS_VERIFIED, 
        :old.LENGTH_TAPE, 
        :old.NO_PAGE_PROS_EVIDENCE, 
        :old.NO_PROS_WITNESS, 
        :old.EST_PDH_TRIAL_LENGTH, 
        :old.indictment_info_1, 
        :old.indictment_info_2, 
        :old.indictment_info_3, 
        :old.indictment_info_4, 
        :old.indictment_info_5, 
        :old.indictment_info_6 );
END IF;

end;
/








create or replace trigger XHB_CASEPROSECUTORAGENC_BIR_TR
  BEFORE INSERT
  on XHB_CASE_PROSECUTOR_AGENCY
  
  for each row
BEGIN

IF :NEW.CASE_PROS_AGENCY_ID IS NULL THEN
  SELECT XHB_CASE_PROSECTOR_AGENCY_SEQ.NEXTVAL   INTO               :NEW.CASE_PROS_AGENCY_ID    FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/








create or replace trigger XHB_CASEPROSECUTORAGENC_BUR_TR
  BEFORE UPDATE
  on XHB_CASE_PROSECUTOR_AGENCY
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CASEPROSECUTORAGENC_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE_PROSECUTOR_AGENCY') = 1) THEN
insert into AUD_CASE_PROSECUTOR_AGENCY 
values (:old.CASE_PROS_AGENCY_ID, 
        :old.PROSECUTOR_TYPE, 
        :old.CASE_ID, 
        :old.REF_PROSECUTOR_AGENCY_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_CC_INFO_BUR_TR
  BEFORE UPDATE
  on XHB_CC_INFO
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CC_INFO_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CC_INFO') = 1) THEN
insert into AUD_CC_INFO 
values (:old.CC_INFO_ID, 
        :old.CC_INFO_TEXT, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/








create or replace trigger XHB_CC_INFO_BIR_TR
  BEFORE INSERT
  on XHB_CC_INFO
  
  for each row
BEGIN
IF :NEW.CC_INFO_ID IS NULL THEN
  SELECT XHB_CC_INFO_SEQ.NEXTVAL INTO               :NEW.CC_INFO_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_CHARGE_BIR_TR
  BEFORE INSERT
  on XHB_CHARGE
  
  for each row
BEGIN

IF :NEW.CHARGE_ID IS NULL THEN
  SELECT XHB_CHARGE_SEQ.NEXTVAL INTO                 :NEW.CHARGE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_CHARGE_BUR_TR
  BEFORE UPDATE
  on XHB_CHARGE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CHARGE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CHARGE') = 1) THEN
insert into AUD_CHARGE 
values (:old.CHARGE_ID, 
        :old.CHARGE_TYPE, 
        :old.PROS_PAPER_SERVED_DATE, 
        :old.CREST_CHARGE_ID, 
        :old.CREST_CHARGE_SEQ_NO, 
        :old.REF_SYSTEM_CODE_ID, 
        :old.CASE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.OBS_IND, 
        :old.IND_SIGNED_DATE );
END IF;

end;
/








create or replace trigger XHB_CHARGE_DIFFERENCES_BIR_TR
  BEFORE INSERT
  on XHB_CHARGE_DIFFERENCES
  
  for each row
BEGIN

IF :NEW.CHARGE_DIFF_ID IS NULL THEN
  SELECT XHB_CHARGE_DIFFERENCES_SEQ.NEXTVAL   INTO                 :NEW.CHARGE_DIFF_ID FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/








create or replace trigger XHB_CHARGE_DIFFERENCES_BUR_TR
  BEFORE UPDATE
  on XHB_CHARGE_DIFFERENCES
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CHARGE_DIFFERENCES_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CHARGE_DIFFERENCES') = 1) THEN
insert into AUD_CHARGE_DIFFERENCES 
values (:old.CHARGE_DIFF_ID, 
        :old.REPORT, 
        :old.DIFF_TIME, 
        :old.CASE_ID, 
        :old.COURT_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_CONFIGUREDPUBLICNOT_BIR_TR
  BEFORE INSERT
  on XHB_CONFIGURED_PUBLIC_NOTICE
  
  for each row
BEGIN

IF :NEW.CONFIGURED_PUBLIC_NOTICE_ID IS NULL THEN
  SELECT XHB_CONFIGURED_PUBLIC_NOT_SEQ.NEXTVAL   INTO                 :NEW.CONFIGURED_PUBLIC_NOTICE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_CONFIGUREDPUBLICNOT_BUR_TR
  BEFORE UPDATE
  on XHB_CONFIGURED_PUBLIC_NOTICE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CONFIGUREDPUBLICNOT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CONFIGURED_PUBLIC_NOTICE') = 1) THEN
insert into AUD_CONFIGURED_PUBLIC_NOTICE 
values (:old.CONFIGURED_PUBLIC_NOTICE_ID, 
        :old.IS_ACTIVE, 
        :old.COURT_ROOM_ID, 
        :old.PUBLIC_NOTICE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_CONTACTDETAIL_BIR_TR
  BEFORE INSERT
  on XHB_CONTACT_DETAIL
  
  for each row
BEGIN

IF :NEW.CONTACT_ID IS NULL THEN
  SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO               :NEW.CONTACT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_CONTACTDETAIL_BUR_TR
  BEFORE UPDATE
  on XHB_CONTACT_DETAIL
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CONTACTDETAIL_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CONTACT_DETAIL') = 1) THEN
insert into AUD_CONTACT_DETAIL 
values (:old.CONTACT_ID, 
        :old.CONTACT_TYPE, 
        :old.CONTACT_VALUE, 
        :old.EMAIL_FORMAT, 
        :old.PAGER_NET, 
        :old.ADDRESS_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_COURT_BIR_TR
  BEFORE INSERT
  on XHB_COURT
  
  for each row
BEGIN

IF :NEW.COURT_ID IS NULL THEN
  SELECT XHB_COURT_SEQ.NEXTVAL INTO                 :NEW.COURT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_COURT_BUR_TR
  BEFORE UPDATE
  on XHB_COURT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_COURT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT') = 1) THEN
insert into AUD_COURT 
values (:old.COURT_ID, 
        :old.COURT_TYPE, 
        :old.CIRCUIT, 
        :old.COURT_NAME, 
        :old.CREST_COURT_ID, 
        :old.COURT_PREFIX, 
        :old.SHORT_NAME, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.ADDRESS_ID, 
        :old.CREST_IP_ADDRESS, 
        :old.in_service_flag, 
        :old.obs_ind );
END IF;

end;
/








create or replace trigger XHB_COURT_LOG_CATEGORY_BIR_TR
  BEFORE INSERT
  on XHB_COURT_LOG_CATEGORY
  
  for each row
BEGIN

IF :NEW.CATEGORY_ID IS NULL THEN
  SELECT XHB_COURT_LOG_CATEGORY_SEQ.NEXTVAL INTO                 :NEW.CATEGORY_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/









create or replace trigger XHB_COURT_LOG_CATEGORY_BUR_TR
  BEFORE UPDATE
  on XHB_COURT_LOG_CATEGORY
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_COURT_LOG_CATEGORY_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_CATEGORY') = 1) THEN
insert into AUD_COURT_LOG_CATEGORY 
values (:old.CATEGORY_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.CATEGORY_DESC_ID, 
        :old.EVENT_DESC_ID );
END IF;

end;
/









create or replace trigger XHB_COURT_LOG_CAT_DESC_BIR_TR
  BEFORE INSERT
  on XHB_COURT_LOG_CATEGORY_DESC
  
  for each row
BEGIN

IF :NEW.CATEGORY_DESC_ID IS NULL THEN
  SELECT XHB_COURT_LOG_CAT_DESC_SEQ.NEXTVAL INTO                 :NEW.CATEGORY_DESC_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/











create or replace trigger XHB_COURT_LOG_CAT_DESC_BUR_TR
  BEFORE UPDATE
  on XHB_COURT_LOG_CATEGORY_DESC
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_COURT_LOG_CAT_DESC_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_CATEGORY_DESC') = 1) THEN
insert into AUD_COURT_LOG_CATEGORY_DESC 
values (:old.CATEGORY_DESC_ID, 
        :old.CATEGORY_DESCRIPTION, 
        :old.CATEGORY_TYPE, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_COURT_LOG_ENTRY_BIR_TR
  BEFORE INSERT
  on XHB_COURT_LOG_ENTRY
  
  for each row
BEGIN

IF :NEW.ENTRY_ID IS NULL THEN
  SELECT XHB_COURT_LOG_ENTRY_SEQ.NEXTVAL INTO                 :NEW.ENTRY_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/









create or replace trigger XHB_COURT_LOG_ENTRY_BUR_TR
  BEFORE UPDATE
  on XHB_COURT_LOG_ENTRY
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_COURT_LOG_ENTRY_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_ENTRY') = 1) THEN
insert into AUD_COURT_LOG_ENTRY 
values (:old.ENTRY_ID, 
        :old.LOG_ENTRY_XML, 
        :old.CASE_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.DATE_TIME, 
        :old.EVENT_DESC_ID );
END IF;

end;
/









create or replace trigger XHB_COURT_LOG_EVENT_DSC_BIR_TR
  BEFORE INSERT
  on XHB_COURT_LOG_EVENT_DESC
  
  for each row
BEGIN

IF :NEW.EVENT_DESC_ID IS NULL THEN
  SELECT XHB_COURT_LOG_EVENT_DESC_SEQ.NEXTVAL INTO                 :NEW.EVENT_DESC_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/








create or replace trigger XHB_COURT_LOG_EVENT_DSC_BUR_TR
  BEFORE UPDATE
  on XHB_COURT_LOG_EVENT_DESC
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_COURT_LOG_EVENT_DSC_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_EVENT_DESC') = 1) THEN
insert into AUD_COURT_LOG_EVENT_DESC 
values (:old.EVENT_DESC_ID, 
        :old.FLAGGED_EVENT, 
        :old.EDITABLE, 
        :old.SEND_TO_MERCATOR, 
        :old.UPDATE_LINKED_CASES, 
        :old.PUBLISH_TO_SUBSCRIBERS, 
        :old.CLEAR_PUBLIC_DISPLAYS, 
        :old.E_INFORM, 
        :old.PUBLIC_DISPLAY, 
        :old.LINKED_CASE_TEXT, 
        :old.EVENT_DESCRIPTION, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.event_id );
END IF;

end;
/









create or replace trigger XHB_COURTROOM_BIR_TR
  BEFORE INSERT
  on XHB_COURT_ROOM
  
  for each row
BEGIN
IF :NEW.COURT_ROOM_ID IS NULL THEN
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO               :NEW.COURT_ROOM_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_COURTROOM_BUR_TR
  BEFORE UPDATE
  on XHB_COURT_ROOM
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_COURTROOM_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_ROOM') = 1) THEN
insert into AUD_COURT_ROOM 
values (:old.COURT_ROOM_ID, 
        :old.COURT_ROOM_NAME, 
        :old.DESCRIPTION, 
        :old.LOCATION, 
        :old.CREST_COURT_ROOM_NO, 
        :old.COURT_SITE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.obs_ind );
END IF;

end;
/








create or replace trigger XHB_COURTSITE_BIR_TR
  BEFORE INSERT
  on XHB_COURT_SITE
  
  for each row
BEGIN

IF :NEW.COURT_SITE_ID IS NULL THEN
  SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO               :NEW.COURT_SITE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_COURTSITE_BUR_TR
  BEFORE UPDATE
  on XHB_COURT_SITE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_COURTSITE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_SITE') = 1) THEN
insert into AUD_COURT_SITE 
values (:old.COURT_SITE_ID, 
        :old.COURT_SITE_NAME, 
        :old.COURT_SITE_CODE, 
        :old.COURT_ID, 
        :old.ADDRESS_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.obs_ind );
END IF;

end;
/








create or replace trigger XHB_CR_LIVE_STATUS_BUR_TR
  BEFORE UPDATE
  on XHB_CR_LIVE_STATUS

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CR_LIVE_STATUS_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CR_LIVE_STATUS') = 1) THEN
insert into AUD_CR_LIVE_STATUS 
values (:old.cr_live_status_id, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.cr_live_status_desc_id, 
        :old.COURT_ROOM_ID, 
        :old.SCHEDULED_HEARING_ID );
END IF;

end;
/



create or replace trigger XHB_CR_LIVE_STATUS_BIR_TR
  BEFORE INSERT
  on XHB_CR_LIVE_STATUS
  
  for each row
BEGIN
IF :NEW.CR_LIVE_STATUS_ID IS NULL THEN
  SELECT XHB_CR_LIVE_STATUS_SEQ.NEXTVAL INTO                 :NEW.CR_LIVE_STATUS_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/








create or replace trigger XHB_CR_LIVE_STATUS_DESC_BIR_TR
  BEFORE INSERT
  on XHB_CR_LIVE_STATUS_DESC
  
  for each row
BEGIN
IF :NEW.CR_LIVE_STATUS_DESC_ID IS NULL THEN
  SELECT XHB_CR_LIVE_STATUS_DESC_SEQ.NEXTVAL INTO                 :NEW.CR_LIVE_STATUS_DESC_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_CR_LIVE_STATUS_DESC_BUR_TR
  BEFORE UPDATE
  on XHB_CR_LIVE_STATUS_DESC

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_CR_LIVE_STATUS_DESC_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CR_LIVE_STATUS_DESC') = 1) THEN
insert into AUD_CR_LIVE_STATUS_DESC 
values (:old.cr_live_status_desc_id, 
        :old.internet_desc, 
        :old.public_display_desc, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_DAILYLIST_BIR_TR
  BEFORE INSERT
  on XHB_DAILY_LIST_XML
  
  for each row
BEGIN
IF :NEW.DAILY_LIST_XML_ID IS NULL THEN
  SELECT XHB_DAILY_LIST_XML_SEQ.NEXTVAL INTO               :NEW.DAILY_LIST_XML_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_DAILYLIST_BUR_TR
  BEFORE UPDATE
  on XHB_DAILY_LIST_XML
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DAILYLIST_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DAILY_LIST_XML') = 1) THEN
insert into AUD_DAILY_LIST_XML 
values (:old.DAILY_LIST_XML_ID, 
        :old.DAILY_LIST_DATE, 
        :old.DAILY_LIST_XML, 
        :old.COURT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_DEF_HEARING_RECORD_BUR_TR
  BEFORE UPDATE
  on XHB_DEF_HEARING_RECORD
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DEF_HEARING_RECORD_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEF_HEARING_RECORD') = 1) THEN
insert into AUD_DEF_HEARING_RECORD 
values (:old.HEARING_RECORD_ID, 
        :old.REF_ADJOURNMENT_ID, 
        :old.ADJOURNED_DATE, 
        :old.IS_ADJOURNED, 
        :old.COLLECT_MAGISTRATE_COURT_ID, 
        :old.START_DATE_NEW_BAIL_STATUS, 
        :old.NEW_BAIL_STATUS, 
        :old.DATE_BAIL_APPLICATION, 
        :old.SUBST_BAIL_APPLICATION, 
        :old.ORAL_EVIDENCE, 
        :old.RESULT_BAIL_APPLICATION, 
        :old.IS_HRA_APPLICATION, 
        :old.REF_DEF_HEARING_TYPE_ID, 
        :old.END_BAIL_STATUS, 
        :old.START_BAIL_STATUS, 
        :old.DATE_OF_COMMITTAL, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.HEARING_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.hearing_dates_freetext_1, 
        :old.hearing_dates_freetext_2, 
        :old.hearing_dates_freetext_3 );
END IF;

end;
/








create or replace trigger XHB_DEF_HEARING_RECORD_BIR_TR
  BEFORE INSERT
  on XHB_DEF_HEARING_RECORD
  
  for each row
BEGIN
IF :NEW.HEARING_RECORD_ID IS NULL THEN
  SELECT XHB_DEF_HEARING_RECORD_SEQ.NEXTVAL INTO               :NEW.HEARING_RECORD_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_DEFENDANT_BIR_TR
  BEFORE INSERT
  on XHB_DEFENDANT
  
  for each row
BEGIN
IF :NEW.DEFENDANT_ID IS NULL THEN
  SELECT XHB_DEFENDANT_SEQ.NEXTVAL INTO               :NEW.DEFENDANT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_DEFENDANT_BUR_TR
  BEFORE UPDATE
  on XHB_DEFENDANT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DEFENDANT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT') = 1) THEN
insert into AUD_DEFENDANT 
values (:old.DEFENDANT_ID, 
        :old.CREST_DEFENDANT_ID, 
        :old.FIRST_NAME, 
        :old.MIDDLE_NAME, 
        :old.SURNAME, 
        :old.INITIALS, 
        :old.DATE_OF_BIRTH, 
        :old.GENDER, 
        :old.LAST_CONVICTION_DATE, 
        :old.IS_COMPANY, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.ADDRESS_ID, 
        :old.COURT_ID );
END IF;

end;
/








create or replace trigger XHB_DEFENDANT_CHARGE_BIR_TR
  BEFORE INSERT
  on XHB_DEFENDANT_CHARGE
  
  for each row
BEGIN
IF :NEW.DEFENDANT_CHARGE_ID IS NULL THEN
  SELECT XHB_DEFENDANT_CHARGE_SEQ.NEXTVAL INTO               :NEW.DEFENDANT_CHARGE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/








create or replace trigger XHB_DEFENDANT_CHARGE_BUR_TR
  BEFORE UPDATE
  on XHB_DEFENDANT_CHARGE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DEFENDANT_CHARGE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_CHARGE') = 1) THEN
insert into AUD_DEFENDANT_CHARGE 
values (:old.DEFENDANT_CHARGE_ID, 
        :old.CHARGE_ID, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.VERSION, 
        :old.OBS_IND );
END IF;

end;
/








create or replace trigger XHB_DEFENDANTONCASE_BIR_TR
  BEFORE INSERT
  on XHB_DEFENDANT_ON_CASE
  
  for each row
BEGIN
IF :NEW.DEFENDANT_ON_CASE_ID IS NULL THEN
  SELECT XHB_DEFENDANT_ON_CASE_SEQ.NEXTVAL INTO               :NEW.DEFENDANT_ON_CASE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_DEFENDANTONCASE_BUR_TR
  BEFORE UPDATE
  on XHB_DEFENDANT_ON_CASE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DEFENDANTONCASE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_CASE') = 1) THEN
insert into AUD_DEFENDANT_ON_CASE 
values (:old.DEFENDANT_ON_CASE_ID, 
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
        :old.date_of_committal );
END IF;

end;
/








create or replace trigger XHB_DEFENDANTONOFFENCE_BIR_TR
  BEFORE INSERT
  on XHB_DEFENDANT_ON_OFFENCE
  
  for each row
BEGIN

IF :NEW.DEFENDANT_ON_OFFENCE_ID IS NULL THEN
  SELECT XHB_DEFENDANT_ON_OFFENCE_SEQ.NEXTVAL INTO               :NEW.DEFENDANT_ON_OFFENCE_ID  FROM   DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_DEFENDANTONOFFENCE_BUR_TR
  BEFORE UPDATE
  on XHB_DEFENDANT_ON_OFFENCE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DEFENDANTONOFFENCE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_OFFENCE') = 1) THEN
insert into AUD_DEFENDANT_ON_OFFENCE 
values (:old.DEFENDANT_ON_OFFENCE_ID, 
        :old.APPEAL_AGAINST_TYPE, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.OFFENCE_ID, 
        :old.ORIGINAL_RESULT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.OBS_IND, 
        :old.IS_SENTENCE_APPEAL, 
        :old.IS_CONVICTION_APPEAL, 
        :old.ALT_REF_OFFENCE_ID, 
        :old.HAS_OTHER, 
        :old.is_stayed, 
        :old.HAS_CONVICTION, 
        :old.HAS_VERDICT );
END IF;

end;
/








create or replace trigger XHB_DEFENDANTREFERENCE_BIR_TR
  BEFORE INSERT
  on XHB_DEFENDANT_REFERENCE
  
  for each row
BEGIN

IF :NEW.DEF_REF_ID IS NULL THEN
  SELECT XHB_DEFENDANT_REFERENCE_SEQ.NEXTVAL INTO               :NEW.DEF_REF_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_DEFENDANTREFERENCE_BUR_TR
  BEFORE UPDATE
  on XHB_DEFENDANT_REFERENCE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DEFENDANTREFERENCE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_REFERENCE') = 1) THEN
insert into AUD_DEFENDANT_REFERENCE 
values (:old.DEF_REF_ID, 
        :old.REFERENCE_VALUE, 
        :old.REFERENCE_NAME, 
        :old.CATEGORY, 
        :old.DEFENDANT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_DEFINITIVE_PUBLIC_N_BUR_TR
  BEFORE UPDATE
  on XHB_DEFINITIVE_PUBLIC_NOTICE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DEFINITIVE_PUBLIC_N_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFINITIVE_PUBLIC_NOTICE') = 1) THEN
insert into AUD_DEFINITIVE_PUBLIC_NOTICE 
values (:old.definitive_pn_id, 
        :old.definitive_pn_desc, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.priority );
END IF;

end;
/









create or replace trigger XHB_DEFINITIVE_PUBLIC_N_BIR_TR
  BEFORE INSERT
  on XHB_DEFINITIVE_PUBLIC_NOTICE
  
  for each row
BEGIN
IF :NEW.DEFINITIVE_PN_ID IS NULL THEN
  SELECT XHB_DEFINITIVE_PUB_NOTICE_SEQ.NEXTVAL INTO                 :NEW.DEFINITIVE_PN_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/





create or replace trigger XHB_DIRECTION_ATTEND_BUR_TR
  BEFORE UPDATE
  on XHB_DIRECTION_ATTEND
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DIRECTION_ATTEND_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DIRECTION_ATTEND') = 1) THEN
insert into AUD_DIRECTION_ATTEND 
values (:old.direction_attend_id, 
        :old.DIRECTIONS_FOR_CASE_ID, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/









create or replace trigger XHB_DIRECTION_ATTEND_BIR_TR
  BEFORE INSERT
  on XHB_DIRECTION_ATTEND
  
  for each row
BEGIN
IF :NEW.DIRECTION_ATTEND_ID IS NULL THEN
  SELECT XHB_DIRECTION_ATTEND_SEQ.NEXTVAL INTO                 :NEW.DIRECTION_ATTEND_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_DIRECTIONS_FOR_CASE_BUR_TR
  BEFORE UPDATE
  on XHB_DIRECTIONS_FOR_CASE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DIRECTIONS_FOR_CASE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DIRECTIONS_FOR_CASE') = 1) THEN
insert into AUD_DIRECTIONS_FOR_CASE 
values (:old.DIRECTIONS_FOR_CASE_ID, 
        :old.FREETEXT, 
        :old.DATE_TIME, 
        :old.LIST_DATE, 
        :old.LIST_TYPE, 
        :old.LISTED_AS, 
        :old.DIRECTIONS_TEXT, 
        :old.TRIAL_TIME_UNIT, 
        :old.TRIAL_TIME_ESTIMATE, 
        :old.HAS_PANDD_FORM, 
        :old.CASE_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/








create or replace trigger XHB_DIRECTIONS_FOR_CASE_BIR_TR
  BEFORE INSERT
  on XHB_DIRECTIONS_FOR_CASE
  
  for each row
BEGIN
IF :NEW.DIRECTIONS_FOR_CASE_ID IS NULL THEN
  SELECT XHB_DIR_FOR_CASE_SEQ.NEXTVAL INTO               :NEW.DIRECTIONS_FOR_CASE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_DIRECTIONS_FOR_DEF_BUR_TR
  BEFORE UPDATE
  on XHB_DIRECTIONS_FOR_DEFENDANT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DIRECTIONS_FOR_DEF_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DIRECTIONS_FOR_DEFENDANT') = 1) THEN
insert into AUD_DIRECTIONS_FOR_DEFENDANT 
values (:old.DIRECTIONS_FOR_DEFENDANT_ID, 
        :old.FREETEXT, 
        :old.DATE_TIME, 
        :old.is_identified, 
        :old.TO_BE_FILED_BY, 
        :old.FILED_FORM_B, 
        :old.CERT_ATTENDANCE, 
        :old.NEW_BAIL_CONDITIONS, 
        :old.BAIL_STATUS, 
        :old.ARRAIGNED, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/








create or replace trigger XHB_DIRECTIONS_FOR_DEF_BIR_TR
  BEFORE INSERT
  on XHB_DIRECTIONS_FOR_DEFENDANT
  
  for each row
BEGIN
IF :NEW.DIRECTIONS_FOR_DEFENDANT_ID IS NULL THEN
  SELECT XHB_DIR_FOR_DEFENDANT_SEQ.NEXTVAL INTO               :NEW.DIRECTIONS_FOR_DEFENDANT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_DISPOSAL_BUR_TR
  BEFORE UPDATE
  on XHB_DISPOSAL
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DISPOSAL_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL') = 1) THEN
insert into AUD_DISPOSAL 
values (:old.DISPOSAL_ID, 
        :old.DEF_ON_CASE_OR_OFFENCE, 
        :old.FREETEXT, 
        :old.DATE_TIME, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.REF_DISPOSAL_ID, 
        :old.VERSION, 
        :old.DEFENDANT_ON_OFFENCE_ID, 
        :old.category, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_DISPOSAL_BIR_TR
  BEFORE INSERT
  on XHB_DISPOSAL
  
  for each row
BEGIN
IF :NEW.DISPOSAL_ID IS NULL THEN
  SELECT XHB_DISPOSAL_SEQ.NEXTVAL INTO               :NEW.DISPOSAL_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_DISPOSALDETAIL_BIR_TR
  BEFORE INSERT
  on XHB_DISPOSAL_DETAIL
  
  for each row
BEGIN

IF :NEW.DISPOSAL_DETAIL_ID IS NULL THEN
  SELECT XHB_DISPOSAL_DETAIL_SEQ.NEXTVAL INTO               :NEW.DISPOSAL_DETAIL_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_DISPOSALDETAIL_BUR_TR
  BEFORE UPDATE
  on XHB_DISPOSAL_DETAIL
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DISPOSALDETAIL_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL_DETAIL') = 1) THEN
insert into AUD_DISPOSAL_DETAIL 
values (:old.DISPOSAL_DETAIL_ID, 
        :old.UNIT, 
        :old.AMOUNT, 
        :old.FREE_TEXT_DESC, 
        :old.OBS_IND, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.ORIGINAL_RESULT_ID );
END IF;

end;
/








create or replace trigger XHB_DISPOSAL_REFERENCE_BUR_TR
  BEFORE UPDATE
  on XHB_DISPOSAL_REFERENCE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DISPOSAL_REFERENCE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL_REFERENCE') = 1) THEN
insert into AUD_DISPOSAL_REFERENCE 
values (:old.DIS_REF_ID, 
        :old.DISPOSAL_ID, 
        :old.REFERENCE_NAME, 
        :old.REFERENCE_VALUE, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_DISPOSAL_REFERENCE_BIR_TR
  BEFORE INSERT
  on XHB_DISPOSAL_REFERENCE
  
  for each row
BEGIN
IF :NEW.DIS_REF_ID IS NULL THEN
  SELECT XHB_DISPOSAL_REFERENCE_SEQ.NEXTVAL INTO               :NEW.DIS_REF_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_DOCUMENT_TYPE_BUR_TR
  BEFORE UPDATE
  on XHB_DOCUMENT_TYPE

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_DOCUMENT_TYPE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_TYPE') = 1) THEN
insert into AUD_DOCUMENT_TYPE 
values (:old.document_type_id, 
        :old.description, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_DOCUMENT_TYPE_BIR_TR
  BEFORE INSERT
  on XHB_DOCUMENT_TYPE
  
  for each row
BEGIN
IF :NEW.DOCUMENT_TYPE_ID IS NULL THEN
  SELECT XHB_DOCUMENT_TYPE_SEQ.NEXTVAL INTO                 :NEW.DOCUMENT_TYPE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_ERROR_DEF_BIR_TR
  BEFORE INSERT
  on XHB_ERROR_DEF
  
  for each row
BEGIN
IF :NEW.ERROR_DEF_ID IS NULL THEN
  SELECT XHB_ERROR_DEF_SEQ.NEXTVAL INTO               :NEW.ERROR_DEF_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_ERROR_DEF_BUR_TR
  BEFORE UPDATE
  on XHB_ERROR_DEF
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ERROR_DEF_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ERROR_DEF') = 1) THEN
insert into AUD_ERROR_DEF 
values (:old.ERROR_DEF_ID, 
        :old.DESCRIPTION, 
        :old.SEVERITY, 
        :old.ERROR_TYPE, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_EXPORTA_BUR_TR
  BEFORE UPDATE
  on XHB_EXPORTA
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_EXPORTA_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EXPORTA') = 1) THEN
insert into AUD_EXPORTA 
values (:old.EXPORT_A_ID, 
        :old.COURT_CLERK_EXPORT, 
        :old.STATUS_FLAG, 
        :old.LINKED_HEARING_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.HEARING_ID );
END IF;

end;
/









create or replace trigger XHB_EXPORTA_BIR_TR
  BEFORE INSERT
  on XHB_EXPORTA
  
  for each row
BEGIN
IF :NEW.EXPORT_A_ID IS NULL THEN
  SELECT XHB_EXPORTA_SEQ.NEXTVAL INTO               :NEW.EXPORT_A_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_FORMATTING_IN_BUR_TR
  BEFORE UPDATE
  on XHB_FORMATTING_IN

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_FORMATTING_IN_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_FORMATTING_IN') = 1) THEN
insert into AUD_FORMATTING_IN 
values (:old.formatting_in_id, 
        :old.xml_document, 
        :old.date_in, 
        :old.court_id, 
        :old.format_status, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_FORMATTING_IN_BIR_TR
  BEFORE INSERT
  on XHB_FORMATTING_IN
  
  for each row
BEGIN
IF :NEW.FORMATTING_IN_ID IS NULL THEN
  SELECT XHB_FORMATTING_IN_SEQ.NEXTVAL INTO                 :NEW.FORMATTING_IN_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_FORMATTING_OUT_BUR_TR
  BEFORE UPDATE
  on XHB_FORMATTING_OUT

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_FORMATTING_OUT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_FORMATTING_OUT') = 1) THEN
insert into AUD_FORMATTING_OUT 
values (:old.formatting_out_id, 
        :old.mime_type, 
        :old.status, 
        :old.court_id, 
        :old.formatted_document, 
        :old.date_created, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_FORMATTING_OUT_BIR_TR
  BEFORE INSERT
  on XHB_FORMATTING_OUT
  
  for each row
BEGIN
IF :NEW.FORMATTING_OUT_ID IS NULL THEN
  SELECT XHB_FORMATTING_OUT_SEQ.NEXTVAL INTO                 :NEW.FORMATTING_OUT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_HEARING_BIR_TR
  BEFORE INSERT
  on XHB_HEARING
  
  for each row
BEGIN

IF :NEW.HEARING_ID IS NULL THEN
  SELECT XHB_HEARING_SEQ.NEXTVAL INTO                :NEW.HEARING_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_HEARING_BUR_TR
  BEFORE UPDATE
  on XHB_HEARING
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_HEARING_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_HEARING') = 1) THEN
insert into AUD_HEARING 
values (:old.HEARING_ID, 
        :old.CASE_ID, 
        :old.REF_HEARING_TYPE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID, 
        :old.MP_HEARING_TYPE, 
        :old.last_calculated_duration, 
        :old.hearing_start_date, 
        :old.hearing_end_date, 
        :old.linked_hearing_id );
END IF;

end;
/








create or replace trigger XHB_HEARINGLIST_BIR_TR
  BEFORE INSERT
  on XHB_HEARING_LIST
  
  for each row
BEGIN

IF :NEW.LIST_ID IS NULL THEN
  SELECT XHB_HEARING_LIST_SEQ.NEXTVAL INTO               :NEW.LIST_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_HEARINGLIST_BUR_TR
  BEFORE UPDATE
  on XHB_HEARING_LIST
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_HEARINGLIST_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_HEARING_LIST') = 1) THEN
insert into AUD_HEARING_LIST 
values (:old.LIST_ID, 
        :old.LIST_TYPE, 
        :old.START_DATE, 
        :old.END_DATE, 
        :old.STATUS, 
        :old.EDITION_NO, 
        :old.PUBLISHED_TIME, 
        :old.PRINT_REFERENCE, 
        :old.CREST_LIST_ID, 
        :old.COURT_ID, 
        :old.LIST_COURT_TYPE, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_INTERNET_XML_HTML_BIR_TR
  BEFORE INSERT
  on XHB_INTERNET_XML_HTML
  
  for each row
BEGIN
IF :NEW.INTERNET_XML_HTML_ID IS NULL THEN
  SELECT XHB_INTERNET_XML_HTML_SEQ.NEXTVAL INTO                 :NEW.INTERNET_XML_HTML_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_INTERNET_XML_HTML_BUR_TR
  BEFORE UPDATE
  on XHB_INTERNET_XML_HTML

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_INTERNET_XML_HTML_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_INTERNET_XML_HTML') = 1) THEN
insert into AUD_INTERNET_XML_HTML 
values (:old.internet_xml_html_id, 
        :old.xml, 
        :old.html, 
        :old.status, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID );
END IF;

end;
/

create or replace trigger XHB_LINKEDCASE_BIR_TR
  BEFORE INSERT
  on XHB_LINKED_CASE
  
  for each row
BEGIN

IF :NEW.LINKED_CASE_ID IS NULL THEN
  SELECT XHB_LINKED_CASE_SEQ.NEXTVAL INTO               :NEW.LINKED_CASE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_LINKEDCASE_BUR_TR
  BEFORE UPDATE
  on XHB_LINKED_CASE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_LINKEDCASE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LINKED_CASE') = 1) THEN
insert into AUD_LINKED_CASE 
values (:old.LINKED_CASE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_LINKED_HEARING_BUR_TR
  BEFORE UPDATE
  on XHB_LINKED_HEARING
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_LINKED_HEARING_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LINKED_HEARING') = 1) THEN
insert into AUD_LINKED_HEARING 
values (:old.LINKED_HEARING_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_LINKED_HEARING_BIR_TR
  BEFORE INSERT
  on XHB_LINKED_HEARING
  
  for each row
BEGIN
IF :NEW.LINKED_HEARING_ID IS NULL THEN
  SELECT XHB_LINKED_HEARING_SEQ.NEXTVAL INTO               :NEW.LINKED_HEARING_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_LINKED_SH_BUR_TR
  BEFORE UPDATE
  on XHB_LINKED_SH
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_LINKED_SH_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LINKED_SH') = 1) THEN
insert into AUD_LINKED_SH 
values (:old.LINKED_SH_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_LINKED_SH_BIR_TR
  BEFORE INSERT
  on XHB_LINKED_SH
  
  for each row
BEGIN
IF :NEW.LINKED_SH_ID IS NULL THEN
  SELECT XHB_LINKED_SH_SEQ.NEXTVAL INTO               :NEW.LINKED_SH_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_LIST_DIST_STATUS_BUR_TR
  BEFORE UPDATE
  on XHB_LIST_DIST_STATUS

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_LIST_DIST_STATUS_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LIST_DIST_STATUS') = 1) THEN
insert into AUD_LIST_DIST_STATUS 
values (:old.list_dist_status_id, 
        :old.status, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.list_distribution_id );
END IF;

end;
/

create or replace trigger XHB_LIST_DIST_STATUS_BIR_TR
  BEFORE INSERT
  on XHB_LIST_DIST_STATUS
  
  for each row
BEGIN
IF :NEW.LIST_DIST_STATUS_ID IS NULL THEN
  SELECT XHB_LIST_DIST_STATUS_SEQ.NEXTVAL INTO                 :NEW.LIST_DIST_STATUS_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/



create or replace trigger XHB_LIST_DISTRIBUTION_BUR_TR
  BEFORE UPDATE
  on XHB_LIST_DISTRIBUTION

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_LIST_DISTRIBUTION_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LIST_DISTRIBUTION') = 1) THEN
insert into AUD_LIST_DISTRIBUTION 
values (:old.list_distribution_id, 
        :old.distribution_type, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_LIST_DISTRIBUTION_BIR_TR
  BEFORE INSERT
  on XHB_LIST_DISTRIBUTION
  
  for each row
BEGIN
IF :NEW.LIST_DISTRIBUTION_ID IS NULL THEN
  SELECT XHB_LIST_DISTRIBUTION_SEQ.NEXTVAL INTO                 :NEW.LIST_DISTRIBUTION_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/




create or replace trigger XHB_MESSAGE_BIR_TR
  BEFORE INSERT
  on XHB_MESSAGE
  
  for each row
BEGIN

IF :NEW.MESSAGE_ID IS NULL THEN
  SELECT XHB_MESSAGE_SEQ.NEXTVAL INTO                :NEW.MESSAGE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_MESSAGE_BUR_TR
  BEFORE UPDATE
  on XHB_MESSAGE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_MESSAGE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_MESSAGE') = 1) THEN
insert into AUD_MESSAGE 
values (:old.MESSAGE_ID, 
        :old.MESSAGE_FROM, 
        :old.MESSAGE_TO, 
        :old.MESSAGE, 
        :old.TIME_SENT, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_OBJECTSTATUS_BIR_TR
  BEFORE INSERT
  on XHB_OBJECT_STATUS
  
  for each row
BEGIN

IF :NEW.OBJECT_STATUS_ID  IS NULL THEN
  SELECT XHB_OBJECT_STATUS_SEQ.NEXTVAL INTO               :NEW.OBJECT_STATUS_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_OBJECTSTATUS_BUR_TR
  BEFORE UPDATE
  on XHB_OBJECT_STATUS
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_OBJECTSTATUS_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_OBJECT_STATUS') = 1) THEN
insert into AUD_OBJECT_STATUS 
values (:old.OBJECT_STATUS_ID, 
        :old.OBJECT_NAME, 
        :old.OBJECT_ID, 
        :old.CREST_OBJECT_ID, 
        :old.COURT_ID, 
        :old.ACTION, 
        :old.STATUS, 
        :old.TIMESTAMP, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_OFFENCE_BIR_TR
  BEFORE INSERT
  on XHB_OFFENCE
  
  for each row
BEGIN

IF :NEW.OFFENCE_ID IS NULL THEN
  SELECT XHB_OFFENCE_SEQ.NEXTVAL INTO                :NEW.OFFENCE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_OFFENCE_BUR_TR
  BEFORE UPDATE
  on XHB_OFFENCE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_OFFENCE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_OFFENCE') = 1) THEN
insert into AUD_OFFENCE 
values (:old.OFFENCE_ID, 
        :old.CREST_OFFENCE_ID, 
        :old.CREST_OFFENCE_SEQ_NO, 
        :old.CREST_OFFENCE_FREETEXT, 
        :old.MULTIPLE, 
        :old.CREST_HOO_CLASS_FREETEXT, 
        :old.CREST_HOO_SUBCLASS_FREETEXT, 
        :old.REF_OFFENCE_ID, 
        :old.CHARGE_ID, 
        :old.OBS_IND, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.REF_SYSTEM_CODE_ID );
END IF;

end;
/








create or replace trigger XHB_ORDER_BUR_TR
  BEFORE UPDATE
  on XHB_ORDER
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ORDER_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER') = 1) THEN
insert into AUD_ORDER 
values (:old.ORDER_ID, 
        :old.DELIVERY_DATE, 
        :old.SIGNED_BY, 
        :old.SIGNING_DATE, 
        :old.DATA_XML, 
        :old.ORDER_DELIVERY_STATUS_ID, 
        :old.ORDER_STATUS_ID, 
        :old.ORDER_TEMPLATE_ID, 
        :old.VERSION, 
        :old.LAST_UPDATE_DATE, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY );
END IF;

end;
/









create or replace trigger XHB_ORDER_BIR_TR
  BEFORE INSERT
  on XHB_ORDER
  
  for each row
BEGIN
IF :NEW.ORDER_ID IS NULL THEN
  SELECT XHB_ORDER_SEQ.NEXTVAL INTO               :NEW.ORDER_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_ORDER_DELIVERY_STAT_BUR_TR
  BEFORE UPDATE
  on XHB_ORDER_DELIVERY_STATUS
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ORDER_DELIVERY_STAT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_DELIVERY_STATUS') = 1) THEN
insert into AUD_ORDER_DELIVERY_STATUS 
values (:old.ORDER_DELIVERY_STATUS_ID, 
        :old.CODE, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_ORDER_DELIVERY_STAT_BIR_TR
  BEFORE INSERT
  on XHB_ORDER_DELIVERY_STATUS
  
  for each row
BEGIN
IF :NEW.ORDER_DELIVERY_STATUS_ID IS NULL THEN
  SELECT XHB_ORD_DELIVERY_STATUS_SEQ.NEXTVAL INTO               :NEW.ORDER_DELIVERY_STATUS_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_ORDER_STATUS_BUR_TR
  BEFORE UPDATE
  on XHB_ORDER_STATUS
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ORDER_STATUS_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_STATUS') = 1) THEN
insert into AUD_ORDER_STATUS 
values (:old.ORDER_STATUS_ID, 
        :old.CODE, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_ORDER_STATUS_BIR_TR
  BEFORE INSERT
  on XHB_ORDER_STATUS
  
  for each row
BEGIN
IF :NEW.ORDER_STATUS_ID IS NULL THEN
  SELECT XHB_ORD_STATUS_SEQ.NEXTVAL INTO               :NEW.ORDER_STATUS_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_ORDER_TEMPLATE_BUR_TR
  BEFORE UPDATE
  on XHB_ORDER_TEMPLATE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ORDER_TEMPLATE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_TEMPLATE') = 1) THEN
insert into AUD_ORDER_TEMPLATE 
values (:old.ORDER_TEMPLATE_ID, 
        :old.DISPLAY_TRANSFORM_NAME, 
        :old.NARRATIVE_TEMPLATE_NAME, 
        :old.EDITOR_TEMPLATE_NAME, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.ORDER_TYPE_ID, 
        :old.obs_ind );
END IF;

end;
/









create or replace trigger XHB_ORDER_TEMPLATE_BIR_TR
  BEFORE INSERT
  on XHB_ORDER_TEMPLATE
  
  for each row
BEGIN
IF :NEW.ORDER_TEMPLATE_ID IS NULL THEN
  SELECT XHB_ORD_TEMPLATE_SEQ.NEXTVAL INTO               :NEW.ORDER_TEMPLATE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_ORDER_TYPE_BUR_TR
  BEFORE UPDATE
  on XHB_ORDER_TYPE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ORDER_TYPE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_TYPE') = 1) THEN
insert into AUD_ORDER_TYPE 
values (:old.ORDER_TYPE_ID, 
        :old.CODE, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.REF_DISPOSAL_ID );
END IF;

end;
/









create or replace trigger XHB_ORDER_TYPE_BIR_TR
  BEFORE INSERT
  on XHB_ORDER_TYPE
  
  for each row
BEGIN
IF :NEW.ORDER_TYPE_ID IS NULL THEN
  SELECT XHB_ORD_TYPE_SEQ.NEXTVAL INTO               :NEW.ORDER_TYPE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_ORIGINAL_RESULT_BIR_TR
  BEFORE INSERT
  on XHB_ORIGINAL_RESULT
  
  for each row
BEGIN

IF :NEW.ORIGINAL_RESULT_ID IS NULL THEN
  SELECT XHB_ORIGINAL_RESULT_SEQ.NEXTVAL INTO               :NEW.ORIGINAL_RESULT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_ORIGINAL_RESULT_BUR_TR
  BEFORE UPDATE
  on XHB_ORIGINAL_RESULT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_ORIGINAL_RESULT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORIGINAL_RESULT') = 1) THEN
insert into AUD_ORIGINAL_RESULT 
values (:old.ORIGINAL_RESULT_ID, 
        :old.COURT_TYPE, 
        :old.CREST_DIS_ID, 
        :old.DEF_ON_CHARGE_OR_OFFENCE, 
        :old.REF_COURT_ID, 
        :old.DEF_ON_CHARGE_OR_OFFENCE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.OBS_IND, 
        :old.REF_DISPOSAL_ID );
END IF;

end;
/








create or replace trigger XHB_PLEA_BUR_TR
  BEFORE UPDATE
  on XHB_PLEA

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_PLEA_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PLEA') = 1) THEN
insert into AUD_PLEA 
values (:old.plea_id, 
        :old.ref_plea_id, 
        :old.other_plea_text, 
        :old.breach_admitted, 
        :old.alt_ref_offence_id, 
        :old.arraignment_date, 
        :old.def_on_charge_or_offence, 
        :old.obs_ind, 
        :old.DEFENDANT_CHARGE_ID, 
        :old.DEFENDANT_ON_OFFENCE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/

create or replace trigger XHB_PLEA_BIR_TR
  BEFORE INSERT
  on XHB_PLEA
  
  for each row
BEGIN
IF :NEW.PLEA_ID IS NULL THEN
  SELECT XHB_PLEA_SEQ.NEXTVAL INTO                 :NEW.PLEA_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_PUBLICNOTICE_BIR_TR
  BEFORE INSERT
  on XHB_PUBLIC_NOTICE
  
  for each row
BEGIN

IF  :NEW.PUBLIC_NOTICE_ID  IS NULL THEN
  SELECT XHB_PUBLIC_NOTICE_SEQ.NEXTVAL INTO               :NEW.PUBLIC_NOTICE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_PUBLICNOTICE_BUR_TR
  BEFORE UPDATE
  on XHB_PUBLIC_NOTICE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_PUBLICNOTICE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PUBLIC_NOTICE') = 1) THEN
insert into AUD_PUBLIC_NOTICE 
values (:old.PUBLIC_NOTICE_ID, 
        :old.PUBLIC_NOTICE_DESC, 
        :old.COURT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.definitive_pn_id );
END IF;

end;
/








create or replace trigger XHB_RECIPIENT_BUR_TR
  BEFORE UPDATE
  on XHB_RECIPIENT

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_RECIPIENT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_RECIPIENT') = 1) THEN
insert into AUD_RECIPIENT 
values (:old.recipient_id, 
        :old.recipient_name, 
        :old.fax_number, 
        :old.email_address, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.REF_COURT_ID );
END IF;

end;
/


create or replace trigger XHB_RECIPIENT_BIR_TR
  BEFORE INSERT
  on XHB_RECIPIENT
  
  for each row
BEGIN
IF :NEW.RECIPIENT_ID IS NULL THEN
  SELECT XHB_RECIPIENT_SEQ.NEXTVAL INTO                 :NEW.RECIPIENT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/




create or replace trigger XHB_REFADVOCATE_BIR_TR
  BEFORE INSERT
  on XHB_REF_ADVOCATE
  
  for each row
BEGIN

IF :NEW.REF_ADVOCATE_ID IS NULL THEN
  SELECT XHB_REF_ADVOCATE_SEQ.NEXTVAL INTO               :NEW.REF_ADVOCATE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFADVOCATE_BUR_TR
  BEFORE UPDATE
  on XHB_REF_ADVOCATE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFADVOCATE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_ADVOCATE') = 1) THEN
insert into AUD_REF_ADVOCATE 
values (:old.REF_ADVOCATE_ID, 
        :old.IS_GLOBAL, 
        :old.CREST_ADVOCATE_ID, 
        :old.CREST_CHAMBER_ID, 
        :old.OBS_IND, 
        :old.INITIALS, 
        :old.YEAR_OF_CALL, 
        :old.VAT_NO, 
        :old.BAR_NO, 
        :old.HONOURS, 
        :old.ADV_TYPE_IND, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.REF_LEGAL_REP_ID, 
        :old.REF_CHAMBER_ID );
END IF;

end;
/








create or replace trigger XHB_REF_CHAMBER_BIR_TR
  BEFORE INSERT
  on XHB_REF_CHAMBER
  
  for each row
BEGIN

IF :NEW.REF_CHAMBER_ID IS NULL THEN
  SELECT XHB_REF_CHAMBER_SEQ.NEXTVAL INTO               :NEW.REF_CHAMBER_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_REF_CHAMBER_BUR_TR
  BEFORE UPDATE
  on XHB_REF_CHAMBER
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REF_CHAMBER_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_CHAMBER') = 1) THEN
insert into AUD_REF_CHAMBER 
values (:old.REF_CHAMBER_ID, 
        :old.OBS_IND, 
        :old.IS_GLOBAL, 
        :old.DX_REF, 
        :old.LOCATION_CODE, 
        :old.CREST_CHAMBER_ID, 
        :old.FIRM_NAME, 
        :old.ADDRESS_ID, 
        :old.COURT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_REFCOURT_BIR_TR
  BEFORE INSERT
  on XHB_REF_COURT
  
  for each row
BEGIN
IF :NEW.REF_COURT_ID IS NULL THEN
  SELECT XHB_REF_COURT_SEQ.NEXTVAL INTO               :NEW.REF_COURT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFCOURT_BUR_TR
  BEFORE UPDATE
  on XHB_REF_COURT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFCOURT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_COURT') = 1) THEN
insert into AUD_REF_COURT 
values (:old.REF_COURT_ID, 
        :old.COURT_FULL_NAME, 
        :old.COURT_SHORT_NAME, 
        :old.NAME_PREFIX, 
        :old.COURT_TYPE, 
        :old.CREST_CODE, 
        :old.OBS_IND, 
        :old.IS_PSD, 
        :old.DX_REF, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.ADDRESS_ID, 
        :old.COURT_ID );
END IF;

end;
/








create or replace trigger XHB_REFCOURTREPORTER_BIR_TR
  BEFORE INSERT
  on XHB_REF_COURT_REPORTER
  
  for each row
BEGIN

IF :NEW.REF_COURT_REPORTER_ID IS NULL THEN
  SELECT XHB_REF_COURT_REPORTER_SEQ.NEXTVAL INTO               :NEW.REF_COURT_REPORTER_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFCOURTREPORTER_BUR_TR
  BEFORE UPDATE
  on XHB_REF_COURT_REPORTER
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFCOURTREPORTER_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_COURT_REPORTER') = 1) THEN
insert into AUD_REF_COURT_REPORTER 
values (:old.REF_COURT_REPORTER_ID, 
        :old.FIRST_NAME, 
        :old.MIDDLE_NAME, 
        :old.SURNAME, 
        :old.CREST_COURT_REPORTER_ID, 
        :old.INITIALS, 
        :old.REPORT_METHOD, 
        :old.OBS_IND, 
        :old.REF_COURT_REPORTER_FIRM_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID );
END IF;

end;
/








create or replace trigger XHB_REF_COURT_REPORT_F_BIR_TR
  BEFORE INSERT
  on XHB_REF_COURT_REPORTER_FIRM
  
  for each row
BEGIN

IF :NEW.REF_COURT_REPORTER_FIRM_ID IS NULL THEN
  SELECT XHB_REF_COURT_REPORT_F_SEQ.NEXTVAL INTO               :NEW.REF_COURT_REPORTER_FIRM_ID  FROM   DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_REF_COURT_REPORT_F_BUR_TR
  BEFORE UPDATE
  on XHB_REF_COURT_REPORTER_FIRM
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REF_COURT_REPORT_F_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_COURT_REPORTER_FIRM') = 1) THEN
insert into AUD_REF_COURT_REPORTER_FIRM 
values (:old.REF_COURT_REPORTER_FIRM_ID, 
        :old.OBS_IND, 
        :old.DISPLAY_FIRST, 
        :old.DX_REF, 
        :old.VAT_NO, 
        :old.FIRM_NAME, 
        :old.ADDRESS_ID, 
        :old.COURT_ID, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.VERSION, 
        :old.CREST_COURT_REPORTER_FIRM_ID );
END IF;

end;
/








create or replace trigger XHB_REFDISPOSAL_BIR_TR
  BEFORE INSERT
  on XHB_REF_DISPOSAL
  
  for each row
BEGIN

IF :NEW.REF_DISPOSAL_ID IS NULL THEN
  SELECT XHB_REF_DISPOSAL_SEQ.NEXTVAL INTO               :NEW.REF_DISPOSAL_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFDISPOSAL_BUR_TR
  BEFORE UPDATE
  on XHB_REF_DISPOSAL
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFDISPOSAL_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISPOSAL') = 1) THEN
insert into AUD_REF_DISPOSAL 
values (:old.REF_DISPOSAL_ID, 
        :old.DISPOSAL_CODE, 
        :old.DISPOSAL_TITLE, 
        :old.CREST_MENU_GROUP, 
        :old.CREST_TEMPLATE_VERSION, 
        :old.DISP_TITLE1, 
        :old.DISP_TITLE2, 
        :old.DVLC_CODE, 
        :old.OBS_IND, 
        :old.COURT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_REF_DISPOSAL_MENU_BIR_TR
  BEFORE INSERT
  on XHB_REF_DISPOSAL_MENU
  
  for each row
BEGIN

IF :NEW.REF_DISPOSAL_MENU_ID IS NULL THEN
  SELECT XHB_REF_DISPOSAL_MENU_SEQ.NEXTVAL INTO               :NEW.REF_DISPOSAL_MENU_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_REF_DISPOSAL_MENU_BUR_TR
  BEFORE UPDATE
  on XHB_REF_DISPOSAL_MENU
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REF_DISPOSAL_MENU_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISPOSAL_MENU') = 1) THEN
insert into AUD_REF_DISPOSAL_MENU 
values (:old.REF_DISPOSAL_MENU_ID, 
        :old.TITLE, 
        :old.DISPOSAL_CODE, 
        :old.PARENT, 
        :old.ABBREV, 
        :old.MENU_GROUP, 
        :old.SEQ_NO, 
        :old.CREST_MENU_ITEM_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID, 
        :old.OBS_IND );
END IF;

end;
/








create or replace trigger XHB_REFHEARINGTYPE_BIR_TR
  BEFORE INSERT
  on XHB_REF_HEARING_TYPE
  
  for each row
BEGIN

IF :NEW.REF_HEARING_TYPE_ID IS NULL THEN
  SELECT XHB_REF_HEARING_TYPE_SEQ.NEXTVAL INTO               :NEW.REF_HEARING_TYPE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFHEARINGTYPE_BUR_TR
  BEFORE UPDATE
  on XHB_REF_HEARING_TYPE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFHEARINGTYPE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_HEARING_TYPE') = 1) THEN
insert into AUD_REF_HEARING_TYPE 
values (:old.REF_HEARING_TYPE_ID, 
        :old.HEARING_TYPE_CODE, 
        :old.HEARING_TYPE_DESC, 
        :old.CATEGORY, 
        :old.SEQ_NO, 
        :old.LIST_SEQUENCE, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID, 
        :old.OBS_IND );
END IF;

end;
/








create or replace trigger XHB_REFJUDGE_BIR_TR
  BEFORE INSERT
  on XHB_REF_JUDGE
  
  for each row
BEGIN

IF :NEW.REF_JUDGE_ID IS NULL THEN
  SELECT XHB_REF_JUDGE_SEQ.NEXTVAL INTO               :NEW.REF_JUDGE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFJUDGE_BUR_TR
  BEFORE UPDATE
  on XHB_REF_JUDGE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFJUDGE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_JUDGE') = 1) THEN
insert into AUD_REF_JUDGE 
values (:old.REF_JUDGE_ID, 
        :old.JUDGE_TYPE, 
        :old.CREST_JUDGE_ID, 
        :old.TITLE, 
        :old.FIRST_NAME, 
        :old.MIDDLE_NAME, 
        :old.SURNAME, 
        :old.FULL_LIST_TITLE1, 
        :old.FULL_LIST_TITLE2, 
        :old.FULL_LIST_TITLE3, 
        :old.STATS_CODE, 
        :old.INITIALS, 
        :old.HONOURS, 
        :old.JUD_VERS, 
        :old.OBS_IND, 
        :old.SOURCE_TABLE, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID );
END IF;

end;
/








create or replace trigger XHB_REFJUSTICE_BIR_TR
  BEFORE INSERT
  on XHB_REF_JUSTICE
  
  for each row
BEGIN

IF :NEW.REF_JUSTICE_ID IS NULL THEN
  SELECT XHB_REF_JUSTICE_SEQ.NEXTVAL INTO               :NEW.REF_JUSTICE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFJUSTICE_BUR_TR
  BEFORE UPDATE
  on XHB_REF_JUSTICE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFJUSTICE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_JUSTICE') = 1) THEN
insert into AUD_REF_JUSTICE 
values (:old.REF_JUSTICE_ID, 
        :old.JUSTICE_NAME, 
        :old.CREST_JUSTICE_ID, 
        :old.COURT_ID, 
        :old.PSD_COURT_CODE, 
        :old.TITLE, 
        :old.INITIALS, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.OBS_IND );
END IF;

end;
/








create or replace trigger XHB_REFLEGALREPRESENTAT_BIR_TR
  BEFORE INSERT
  on XHB_REF_LEGAL_REPRESENTATIVE
  
  for each row
BEGIN

IF :NEW.REF_LEGAL_REP_ID IS NULL THEN
  SELECT XHB_REF_LEGAL_REP_SEQ.NEXTVAL INTO               :NEW.REF_LEGAL_REP_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFLEGALREPRESENTAT_BUR_TR
  BEFORE UPDATE
  on XHB_REF_LEGAL_REPRESENTATIVE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFLEGALREPRESENTAT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_LEGAL_REPRESENTATIVE') = 1) THEN
insert into AUD_REF_LEGAL_REPRESENTATIVE 
values (:old.REF_LEGAL_REP_ID, 
        :old.FIRST_NAME, 
        :old.MIDDLE_NAME, 
        :old.SURNAME, 
        :old.TITLE, 
        :old.INITIALS, 
        :old.LEGAL_REP_TYPE, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID, 
        :old.OBS_IND );
END IF;

end;
/








create or replace trigger XHB_REFOFFENCE_BIR_TR
  BEFORE INSERT
  on XHB_REF_OFFENCE
  
  for each row
BEGIN

IF :NEW.REF_OFFENCE_ID IS NULL THEN
  SELECT XHB_REF_OFFENCE_SEQ.NEXTVAL INTO               :NEW.REF_OFFENCE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFOFFENCE_BUR_TR
  BEFORE UPDATE
  on XHB_REF_OFFENCE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFOFFENCE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_OFFENCE') = 1) THEN
insert into AUD_REF_OFFENCE 
values (:old.REF_OFFENCE_ID, 
        :old.OFFENCE_CODE, 
        :old.OFFENCE_DESC, 
        :old.HO_PROC_TYPE, 
        :old.HO_CLASS, 
        :old.HO_SUB_CLASS, 
        :old.DVLC_CODE, 
        :old.OFFENCE_TYPE, 
        :old.STATUTE, 
        :old.OFFENCE_CLASS, 
        :old.ACT_SECTION, 
        :old.OBS_IND, 
        :old.OFFENCE_DESC2, 
        :old.OFFENCE_GROUP, 
        :old.COURT_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_REFPROSECUTORAGENCY_BIR_TR
  BEFORE INSERT
  on XHB_REF_PROSECUTOR_AGENCY
  
  for each row
BEGIN

IF :NEW.REF_PROSECUTOR_AGENCY_ID IS NULL THEN
  SELECT XHB_REF_PROSECUTOR_AGENCY_SEQ.NEXTVAL   INTO               :NEW.REF_PROSECUTOR_AGENCY_ID   FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_REFPROSECUTORAGENCY_BUR_TR
  BEFORE UPDATE
  on XHB_REF_PROSECUTOR_AGENCY
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFPROSECUTORAGENCY_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_PROSECUTOR_AGENCY') = 1) THEN
insert into AUD_REF_PROSECUTOR_AGENCY 
values (:old.REF_PROSECUTOR_AGENCY_ID, 
        :old.TITLE, 
        :old.PROSECUTOR_NAME_1, 
        :old.PROSECUTOR_NAME_2, 
        :old.PROSECUTOR_NAME_3, 
        :old.INITIALS, 
        :old.ADDRESS_ID, 
        :old.CREST_OPPOSER_ID, 
        :old.COURT_ID, 
        :old.CPS_CODE, 
        :old.DX_REF, 
        :old.OBS_IND, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_SOLICITOR_BIR_TR
  BEFORE INSERT
  on XHB_REF_SOLICITOR
  
  for each row
BEGIN

IF :NEW.SOLICITOR_ID IS NULL THEN
  SELECT XHB_SOLICITOR_SEQ.NEXTVAL INTO              :NEW.SOLICITOR_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_SOLICITOR_BUR_TR
  BEFORE UPDATE
  on XHB_REF_SOLICITOR
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SOLICITOR_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_SOLICITOR') = 1) THEN
insert into AUD_REF_SOLICITOR 
values (:old.SOLICITOR_ID, 
        :old.CREST_SOLICITOR_NAME, 
        :old.IS_IN_CREST, 
        :old.REF_LEGAL_REP_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.OBS_IND, 
        :old.ref_solicitor_firm_id );
END IF;

end;
/








create or replace trigger XHB_REFSOLICITORFIRM_BIR_TR
  BEFORE INSERT
  on XHB_REF_SOLICITOR_FIRM
  
  for each row
BEGIN

IF :NEW.REF_SOLICITOR_FIRM_ID IS NULL THEN
  SELECT XHB_REF_SOLICITOR_FIRM_SEQ.NEXTVAL INTO               :NEW.REF_SOLICITOR_FIRM_ID  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

end;
/






create or replace trigger XHB_REFSOLICITORFIRM_BUR_TR
  BEFORE UPDATE
  on XHB_REF_SOLICITOR_FIRM
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REFSOLICITORFIRM_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_SOLICITOR_FIRM') = 1) THEN
insert into AUD_REF_SOLICITOR_FIRM 
values (:old.REF_SOLICITOR_FIRM_ID, 
        :old.SOLICITOR_FIRM_NAME, 
        :old.CREST_SOF_ID, 
        :old.COURT_ID, 
        :old.OBS_IND, 
        :old.SHORT_NAME, 
        :old.DX_REF, 
        :old.VAT_NO, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.ADDRESS_ID );
END IF;

end;
/








create or replace trigger XHB_REF_SYSTEM_CODE_BIR_TR
  BEFORE INSERT
  on XHB_REF_SYSTEM_CODE
  
  for each row
BEGIN
IF :NEW.REF_SYSTEM_CODE_ID IS NULL THEN
  SELECT XHB_REF_SYSTEM_CODE_SEQ.NEXTVAL INTO               :NEW.REF_SYSTEM_CODE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/








create or replace trigger XHB_REF_SYSTEM_CODE_BUR_TR
  BEFORE UPDATE
  on XHB_REF_SYSTEM_CODE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_REF_SYSTEM_CODE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_SYSTEM_CODE') = 1) THEN
insert into AUD_REF_SYSTEM_CODE 
values (:old.REF_SYSTEM_CODE_ID, 
        :old.CODE, 
        :old.CODE_TYPE, 
        :old.CODE_TITLE, 
        :old.DE_CODE, 
        :old.REF_CODE_ORDER, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.COURT_ID, 
        :old.OBS_IND );
END IF;

end;
/








create or replace trigger XHB_SCHED_HEARING_ATT_BUR_TR
  BEFORE UPDATE
  on XHB_SCHED_HEARING_ATTENDEE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SCHED_HEARING_ATT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SCHED_HEARING_ATTENDEE') = 1) THEN
insert into AUD_SCHED_HEARING_ATTENDEE 
values (:old.SH_ATTENDEE_ID, 
        :old.ATTENDEE_TYPE, 
        :old.SCHEDULED_HEARING_ID, 
        :old.VERSION, 
        :old.SH_STAFF_ID, 
        :old.SH_LEG_REP_ID, 
        :old.SH_JUSTICE_ID, 
        :old.REF_JUDGE_ID, 
        :old.REF_COURT_REPORTER_ID, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_SCHED_HEARING_ATT_BIR_TR
  BEFORE INSERT
  on XHB_SCHED_HEARING_ATTENDEE
  
  for each row
BEGIN
IF :NEW.SH_ATTENDEE_ID IS NULL THEN
  SELECT XHB_SCHED_HEARING_ATTEND_SEQ.NEXTVAL INTO               :NEW.SH_ATTENDEE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_SCHEDHEARDEFEND_BIR_TR
  BEFORE INSERT
  on XHB_SCHED_HEARING_DEFENDANT
  
  for each row
BEGIN

IF :NEW.SCHED_HEAR_DEF_ID IS NULL THEN
  SELECT XHB_SCHEDULED_HEARING_DEF_SEQ.NEXTVAL   INTO               :NEW.SCHED_HEAR_DEF_ID  FROM   DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_SCHEDHEARDEFEND_BUR_TR
  BEFORE UPDATE
  on XHB_SCHED_HEARING_DEFENDANT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SCHEDHEARDEFEND_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SCHED_HEARING_DEFENDANT') = 1) THEN
insert into AUD_SCHED_HEARING_DEFENDANT 
values (:old.SCHED_HEAR_DEF_ID, 
        :old.SCHEDULED_HEARING_ID, 
        :old.DEFENDANT_ON_CASE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_SCHEDULEDHEARING_BIR_TR
  BEFORE INSERT
  on XHB_SCHEDULED_HEARING
  
  for each row
BEGIN
IF :NEW.SCHEDULED_HEARING_ID IS NULL THEN
  SELECT XHB_SCHEDULED_HEARING_SEQ.NEXTVAL INTO               :NEW.SCHEDULED_HEARING_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_SCHEDULEDHEARING_BUR_TR
  BEFORE UPDATE
  on XHB_SCHEDULED_HEARING
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SCHEDULEDHEARING_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SCHEDULED_HEARING') = 1) THEN
insert into AUD_SCHEDULED_HEARING 
values (:old.SCHEDULED_HEARING_ID, 
        :old.SEQUENCE_NO, 
        :old.NOT_BEFORE_TIME, 
        :old.ORIGINAL_TIME, 
        :old.LISTING_NOTE, 
        :old.HEARING_PROGRESS, 
        :old.SITTING_ID, 
        :old.HEARING_ID, 
        :old.MOVED_FROM, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.LINKED_SH_ID, 
        :old.END_TIME, 
        :old.START_TIME, 
        :old.DATE_OF_HEARING, 
        :old.is_case_active );
END IF;

end;
/








create or replace trigger XHB_SH_JUDGE_BUR_TR
  BEFORE UPDATE
  on XHB_SH_JUDGE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SH_JUDGE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_JUDGE') = 1) THEN
insert into AUD_SH_JUDGE 
values (:old.SH_JUDGE_ID, 
        :old.DEPUTY_HCJ, 
        :old.REF_JUDGE_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.SH_ATTENDEE_ID );
END IF;

end;
/









create or replace trigger XHB_SH_JUDGE_BIR_TR
  BEFORE INSERT
  on XHB_SH_JUDGE
  
  for each row
BEGIN
IF :NEW.SH_JUDGE_ID IS NULL THEN
  SELECT XHB_SH_JUDGE_SEQ.NEXTVAL INTO               :NEW.SH_JUDGE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_SH_JUSTICE_BUR_TR
  BEFORE UPDATE
  on XHB_SH_JUSTICE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SH_JUSTICE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_JUSTICE') = 1) THEN
insert into AUD_SH_JUSTICE 
values (:old.SH_JUSTICE_ID, 
        :old.JUSTICE_NAME, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.HEARING_ID );
END IF;

end;
/









create or replace trigger XHB_SH_JUSTICE_BIR_TR
  BEFORE INSERT
  on XHB_SH_JUSTICE
  
  for each row
BEGIN
IF :NEW.SH_JUSTICE_ID IS NULL THEN
  SELECT XHB_SH_JUSTICE_SEQ.NEXTVAL INTO               :NEW.SH_JUSTICE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_SH_LEG_REP_BUR_TR
  BEFORE UPDATE
  on XHB_SH_LEG_REP
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SH_LEG_REP_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_LEG_REP') = 1) THEN
insert into AUD_SH_LEG_REP 
values (:old.SH_LEG_REP_ID, 
        :old.CREST_SEQUENCE_NO, 
        :old.LEGAL_ROLE, 
        :old.IS_SIGNED_IN, 
        :old.SOL_FIRM_OR_REF_LEGAL_REP, 
        :old.SCHED_HEAR_DEF_ID, 
        :old.REF_LEGAL_REP_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.CC_INFO_ID, 
        :old.REF_SOLICITOR_FIRM_ID, 
        :old.REF_DEFENCE_CATEGORY_ID );
END IF;

end;
/









create or replace trigger XHB_SH_LEG_REP_BIR_TR
  BEFORE INSERT
  on XHB_SH_LEG_REP
  
  for each row
BEGIN
IF :NEW.SH_LEG_REP_ID IS NULL THEN
  SELECT XHB_SH_LEG_REP_SEQ.NEXTVAL INTO               :NEW.SH_LEG_REP_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/


create or replace trigger XHB_SH_STAFF_BUR_TR
  BEFORE UPDATE
  on XHB_SH_STAFF
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SH_STAFF_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_STAFF') = 1) THEN
insert into AUD_SH_STAFF 
values (:old.SH_STAFF_ID, 
        :old.STAFF_ROLE, 
        :old.STAFF_NAME, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE );
END IF;

end;
/









create or replace trigger XHB_SH_STAFF_BIR_TR
  BEFORE INSERT
  on XHB_SH_STAFF
  
  for each row
BEGIN
IF :NEW.SH_STAFF_ID IS NULL THEN
  SELECT XHB_SH_STAFF_SEQ.NEXTVAL INTO               :NEW.SH_STAFF_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_SITTING_BIR_TR
  BEFORE INSERT
  on XHB_SITTING
  
  for each row
BEGIN

IF :NEW.SITTING_ID IS NULL THEN
  SELECT XHB_SITTING_SEQ.NEXTVAL INTO                :NEW.SITTING_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/






create or replace trigger XHB_SITTING_BUR_TR
  BEFORE UPDATE
  on XHB_SITTING
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SITTING_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SITTING') = 1) THEN
insert into AUD_SITTING 
values (:old.SITTING_ID, 
        :old.SITTING_SEQUENCE_NO, 
        :old.IS_SITTING_JUDGE, 
        :old.SITTING_TIME, 
        :old.SITTING_NOTE, 
        :old.REF_JUSTICE1_ID, 
        :old.REF_JUSTICE2_ID, 
        :old.REF_JUSTICE3_ID, 
        :old.REF_JUSTICE4_ID, 
        :old.IS_FLOATING, 
        :old.LIST_ID, 
        :old.REF_JUDGE_ID, 
        :old.COURT_ROOM_ID, 
        :old.COURT_SITE_ID, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION, 
        :old.JUSTICENAME4, 
        :old.JUSTICENAME3, 
        :old.JUSTICENAME2, 
        :old.JUSTICENAME1 );
END IF;

end;
/








create trigger XHB_SYS_AUDIT_BIR_TR
  BEFORE INSERT
  on XHB_SYS_AUDIT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_SYS_AUDIT_BIR_TR */
declare numrows INTEGER;
begin


IF :NEW.SYS_AUDIT_ID IS NULL THEN
  SELECT XHB_SYS_AUDIT_SEQ.NEXTVAL INTO                :NEW.SYS_AUDIT_ID  FROM DUAL;
END IF;
end;
/



create or replace trigger XHB_TERMINAL_BIR_TR
  BEFORE INSERT
  on XHB_TERMINAL
  
  for each row
BEGIN

IF :NEW.TERMINAL_ID IS NULL THEN
  SELECT XHB_TERMINAL_SEQ.NEXTVAL INTO                :NEW.TERMINAL_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/












create or replace trigger XHB_TERMINAL_BUR_TR
  BEFORE UPDATE
  on XHB_TERMINAL
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_TERMINAL_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_TERMINAL') = 1) THEN
insert into AUD_TERMINAL 
values (:old.TERMINAL_ID, 
        :old.LOCATION, 
        :old.DESCRIPTION, 
        :old.TERMINAL_IP, 
        :old.TERMINAL_NAME, 
        :old.COURT_ROOM_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.courtroom_or_site, 
        :old.COURT_SITE_ID );
END IF;

end;
/









create or replace trigger XHB_TERMINAL_REFERENCE_BUR_TR
  BEFORE UPDATE
  on XHB_TERMINAL_REFERENCE

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_TERMINAL_REFERENCE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_TERMINAL_REFERENCE') = 1) THEN
insert into AUD_TERMINAL_REFERENCE 
values (:old.ter_ref_id, 
        :old.reference_value, 
        :old.reference_name, 
        :old.category, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_TERMINAL_REFERENCE_BIR_TR
  BEFORE INSERT
  on XHB_TERMINAL_REFERENCE
  
  for each row
BEGIN
IF :NEW.ter_ref_ID IS NULL THEN
  SELECT XHB_TERMINAL_REFERENCE_SEQ.NEXTVAL INTO                 :NEW.ter_ref_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/







create or replace trigger XHB_VERDICT_BUR_TR
  BEFORE UPDATE
  on XHB_VERDICT
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_VERDICT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_VERDICT') = 1) THEN
insert into AUD_VERDICT 
values (:old.VERDICT_ID, 
        :old.OBS_IND, 
        :old.DEF_ON_CHARGE_OR_OFFENCE, 
        :old.JURORS_DISSENTING, 
        :old.JURORS_ASSENTING, 
        :old.ALT_REF_OFFENCE_ID, 
        :old.VERDICT_DATE, 
        :old.REF_VERDICT_ID, 
        :old.VERSION, 
        :old.LAST_UPDATED_BY, 
        :old.other_verdict_text, 
        :old.CREATED_BY, 
        :old.CREATION_DATE, 
        :old.LAST_UPDATE_DATE, 
        :old.DEFENDANT_ON_OFFENCE_ID, 
        :old.DEFENDANT_CHARGE_ID );
END IF;

end;
/









create or replace trigger XHB_VERDICT_BIR_TR
  BEFORE INSERT
  on XHB_VERDICT
  
  for each row
BEGIN
IF :NEW.VERDICT_ID IS NULL THEN
  SELECT XHB_VERDICT_SEQ.NEXTVAL INTO               :NEW.VERDICT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/

create or replace trigger XHB_WLL_DIST_STATUS_BUR_TR
  BEFORE UPDATE
  on XHB_WLL_DIST_STATUS

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_WLL_DIST_STATUS_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_WLL_DIST_STATUS') = 1) THEN
insert into AUD_WLL_DIST_STATUS 
values (:old.wll_dist_status_id, 
        :old.status, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_WLL_DIST_STATUS_BIR_TR
  BEFORE INSERT
  on XHB_WLL_DIST_STATUS
  
  for each row
BEGIN
IF :NEW.WLL_DIST_STATUS_ID IS NULL THEN
  SELECT XHB_WLL_DIST_STATUS_SEQ.NEXTVAL INTO                 :NEW.WLL_DIST_STATUS_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/




create or replace trigger XHB_WLL_RECIPIENT_BIR_TR
  BEFORE INSERT
  on XHB_WLL_RECIPIENT
  
  for each row
BEGIN
IF :NEW.WLL_RECIPIENT_ID IS NULL THEN
  SELECT XHB_WLL_RECIPIENT_SEQ.NEXTVAL INTO                 :NEW.WLL_RECIPIENT_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/




create or replace trigger XHB_WLL_RECIPIENT_BUR_TR
  BEFORE UPDATE
  on XHB_WLL_RECIPIENT

  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_WLL_RECIPIENT_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_WLL_RECIPIENT') = 1) THEN
insert into AUD_WLL_RECIPIENT 
values (:old.wll_recipient_id, 
        :old.crest_solicitor_firm_id, 
        :old.crest_court_id, 
        :old.solicitor_firm_name, 
        :old.solictior_firm_address, 
        :old.solicitor_firm_fax, 
        :old.solicitor_firm_email, 
        :old.distribution_type, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/


create or replace trigger XHB_XGROUP_BIR_TR
  BEFORE INSERT
  on XHB_XGROUP
  
  for each row
BEGIN
IF :NEW.XGROUP_ID IS NULL THEN
  SELECT XHB_XGROUP_SEQ.NEXTVAL INTO               :NEW.XGROUP_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/








create or replace trigger XHB_XGROUP_BUR_TR
  BEFORE UPDATE
  on XHB_XGROUP
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_XGROUP_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_XGROUP') = 1) THEN
insert into AUD_XGROUP 
values (:old.XGROUP_ID, 
        :old.ROLES, 
        :old.TYPE, 
        :old.DESCRIPTION, 
        :old.NAME, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








create or replace trigger XHB_XROLE_BIR_TR
  BEFORE INSERT
  on XHB_XROLE
  
  for each row
BEGIN
IF :NEW.XROLE_ID IS NULL THEN
  SELECT XHB_XROLE_SEQ.NEXTVAL INTO               :NEW.XROLE_ID  FROM DUAL;
END IF;

IF ( (:NEW.LAST_UPDATED_BY IS NULL)
   OR (:NEW.CREATED_BY IS NULL) ) THEN
  SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
         SYS_CONTEXT('USERENV', 'SESSION_USER')
  INTO   :NEW.LAST_UPDATED_BY,
         :NEW.CREATED_BY 
  FROM DUAL;
END IF;

SELECT SYSDATE, SYSDATE,1 INTO 
:NEW.LAST_UPDATE_DATE, 
:NEW.CREATION_DATE, 
:NEW.VERSION 
FROM DUAL;

end;
/









create or replace trigger XHB_XROLE_BUR_TR
  BEFORE UPDATE
  on XHB_XROLE
  
  for each row
/* ERwin Builtin Wed Feb 12 13:00:31 2003 */
/* default body for XHB_XROLE_BUR_TR */
declare numrows INTEGER;
begin


SELECT :OLD.VERSION + 1 
INTO :NEW.VERSION FROM DUAL;

/* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') INTO :NEW.LAST_UPDATED_BY
        FROM DUAL;
END IF;

/* Is Auditing on this table required */
IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_XROLE') = 1) THEN
insert into AUD_XROLE 
values (:old.XROLE_ID, 
        :old.GROUPS, 
        :old.TYPE, 
        :old.DESCRIPTION, 
        :old.NAME, 
        :old.LAST_UPDATE_DATE, 
        :old.CREATION_DATE, 
        :old.CREATED_BY, 
        :old.LAST_UPDATED_BY, 
        :old.VERSION );
END IF;

end;
/








insert into XHB_SYS_AUDIT 
select null, table_name, replace(table_name, 'XHB', 'AUD'), 'N' 
from   user_tables
where  table_name like 'XHB%'
and    table_name not in ('XHB_SYS_AUDIT', 'XHB_SYS_USER_INFORMATION')

