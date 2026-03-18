	/**************************************************
* CCN0365_database_objects.sql
*
* Version Date       Author    Description
* 1.0     10/03/2009 D Field   Creation
* 1.1     13/03/2009 D Field   Removed Success_Log. Added two new temp tables. 
* 1.2     27/03/2009 D Field   Added primary key to mtbl_case_history
* 1.3     30/03/2009 D Field   Removed Case_id column, added Last_update_Date to MTBL_CASE_HISTORY.
* 1.3     30/03/2009 D Field   Removed Case_id column, added Last_update_Date to MTBL_CASE_HISTORY.
* 1.4     30/03/2009 J Powell  Added two commands for mercator part of CCN365
***************************************************/


CREATE TABLE xhibit.xhb_hk_results
(hk_run_id       NUMBER(10)
,run_type        VARCHAR2(1)
,run_start_date  DATE
,run_end_date    DATE
,case_start_date DATE
,case_end_date   DATE
,list_start_date DATE
,list_end_date   DATE
,case_status     VARCHAR2(1)
,case_error_message VARCHAR2(2000)
,list_status     VARCHAR2(1)
,list_error_message VARCHAR2(2000)
,cases_deleted   NUMBER(8)
,lists_deleted   NUMBER(8)
,cases_error     NUMBER(8)
,error_message   VARCHAR2(2000)
,case_limit      NUMBER
,running_list    NUMBER
,warned_list     NUMBER
,firm_list       NUMBER
,daily_list      NUMBER
)
TABLESPACE xhibitd;


CREATE TABLE xhibit.mtbl_case_history
(case_no          NUMBER(8)
,case_type        VARCHAR2(1)
,court_id         NUMBER(8)
,last_update_date DATE
,CONSTRAINT mtbl_case_historty_pk PRIMARY KEY (court_id,case_type,case_no)
)
TABLESPACE xhibitd;


CREATE TABLE xhibit.xhb_hk_error_log
(hk_run_id      NUMBER(10)
,case_no        NUMBER(8)
,case_type      VARCHAR2(1)
,court_id       NUMBER(8)
,case_id        NUMBER(8)
,error_message  VARCHAR2(500)
)
TABLESPACE xhibitd;


CREATE GLOBAL TEMPORARY TABLE xhb_hk_blob_id_temp 
(blob_id  NUMBER(10)
) ON COMMIT DELETE ROWS;

CREATE GLOBAL TEMPORARY TABLE xhb_hk_document_temp 
(clob_id          NUMBER(10)
,xml_document_id  NUMBER(8) 
) ON COMMIT DELETE ROWS;


CREATE INDEX xhb_hk_document_temp_i1 ON xhb_hk_document_temp (xml_document_id);

CREATE INDEX xhb_hk_blob_id_temp_i1 ON xhb_hk_blob_id_temp (blob_id);

CREATE SEQUENCE xhibit.hk_run_id_seq
START WITH 1
NOCACHE
NOCYCLE;

GRANT SELECT ON xhibit.hk_run_id_seq TO PUBLIC;


-- Following indexes are needed to stop deadlock and TM contention on child tables when deleting from parents.

CREATE INDEX xhb_bail_application_doc_fk ON xhb_bail_application (defendant_on_case_id) TABLESPACE xhibitx;

CREATE INDEX xhb_def_doc_fk ON xhb_def_on_case_ref_sol_firm (defendant_on_case_id) TABLESPACE xhibitx;

CREATE INDEX xhb_resynch_case_fk ON xhb_case_refresh_resynch (case_id) TABLESPACE xhibitx;

CREATE INDEX xhb_indict_hist_case_fk ON xhb_indictment_history (case_id) TABLESPACE xhibitx;

CREATE INDEX xhb_rc_case_fk ON xhb_rs_case (case_id) TABLESPACE xhibitx;

-- Following indexes are needed for performance when deleting from audit tables.

-- Indexes for Hearing

-- 5
CREATE INDEX aud_exporta_hearing_id ON aud_exporta (hearing_id) TABLESPACE xhibitx;

CREATE INDEX aud_hearing_hearing_id ON aud_hearing (case_id) TABLESPACE xhibitx;

--3 
CREATE INDEX aud_def_hear_record ON aud_def_hearing_record (hearing_id) TABLESPACE xhibitx;

-- 1.1
CREATE INDEX aud_sched_hearing ON aud_scheduled_hearing (hearing_id) TABLESPACE xhibitx;

CREATE INDEX aud_cr_live_disp_I1 ON aud_cr_live_display (scheduled_hearing_id) TABLESPACE xhibitx;

-- 1.2
CREATE INDEX aud_cr_live_internet_i1 ON aud_cr_live_internet (scheduled_hearing_id) TABLESPACE xhibitx;	

-- 1.3
CREATE INDEX aud_cr_live_status_i1 ON aud_cr_live_status (scheduled_hearing_id) TABLESPACE xhibitx;

-- 1.4
CREATE INDEX aud_court_log_entry_i1 ON aud_court_log_entry (scheduled_hearing_id) TABLESPACE xhibitx;

-- 1.7.1
CREATE INDEX aud_sched_hearing_attendee_1 ON aud_sched_hearing_attendee (scheduled_hearing_id) TABLESPACE xhibitx;

-- Indexes for Charge

-- 1
CREATE INDEX aud_offence_i1 ON aud_offence (charge_id) TABLESPACE xhibitx;

-- All
CREATE INDEX aud_charge_i1 ON aud_charge (case_id) TABLESPACE xhibitx;

-- Indexes for Defendant

-- 1 
CREATE INDEX aud_defendant_on_case_i1 ON aud_defendant_on_case (case_id) TABLESPACE xhibitx; 

-- 1.1   93
CREATE INDEX aud_court_log_entry_i2 ON aud_court_log_entry (defendant_on_offence_id) TABLESPACE xhibitx;

--1.2.1
CREATE INDEX aud_defendant_on_offence_i1 ON aud_defendant_on_offence (defendant_on_case_id) TABLESPACE xhibitx;

--1.5.1 
CREATE INDEX aud_disposal_line_i1 ON aud_disposal_line (disposal2_id) TABLESPACE xhibitx;

-- 5
CREATE INDEX aud_court_log_entry_i3 ON aud_court_log_entry (defendant_on_case_id) TABLESPACE xhibitx;

-- 11
CREATE INDEX aud_order_i1 ON aud_order (defendant_on_case_id) TABLESPACE xhibitx;

--13
CREATE INDEX aud_court_log_entry_i4 ON aud_court_log_entry (case_id) TABLESPACE xhibitx;

-- 0.1
CREATE INDEX aud_defendant_i1 ON aud_defendant (defendant_id) TABLESPACE xhibitx;


-- Indexes for Cases
-- 0
CREATE INDEX aud_case_i1 ON aud_case (case_id) TABLESPACE xhibitx;


-- Load the PL/SQL packages
@xhb_housekeeping_pkg_h
@xhb_housekeeping_pkg_b




--CREATE NEW IMPORT TYPE
insert into xhb_crest_import_type values ('HK','Housekeeping');

--CREATE NEW RECORDS IN XHB_CREST_IMPORT FOR EACH COURT
INSERT INTO xhb_crest_import (court_id,status,import_type,max_retry,current_retry)  
       SELECT c.court_id,'S' as Status,'HK'as type,1 as max_retry,0 as current_retry 
       FROM xhb_court c;

commit;
