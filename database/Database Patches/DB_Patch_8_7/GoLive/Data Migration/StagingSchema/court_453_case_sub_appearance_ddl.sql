CREATE TABLE XHBSTG_CASE_SUB_APPEARANCE_DM
(
CASE_TYPE                               VARCHAR2(1) NOT NULL,
CASE_NO                                 NUMBER (8) NOT NULL,
CHD_ID                                  NUMBER (8) NOT NULL,
SUB_ID                                  NUMBER (8) NOT NULL,
DEFENDANT_NO                            NUMBER (3)   ,
COURT_LIST_IND                          VARCHAR2(1)   ,
NOTIF_JOB_ID                            NUMBER (8)   ,
COPY_DW_DEFT                            VARCHAR2(1)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
