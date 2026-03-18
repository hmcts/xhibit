CREATE TABLE XHBSTG_CASE_HISTORY_DM
(
CASE_TYPE                               VARCHAR2(1) NOT NULL,
CASE_NO                                 NUMBER (8) NOT NULL,
PSD_CT_CODE                             NUMBER (4)   ,
COMM_DATE                               DATE     ,
DATE_CLOSED                             DATE     ,
REASON_DELETED                          VARCHAR2(70)   ,
CASE_TITLE                              VARCHAR2(72)   ,
DATE_ARCHIVED                           DATE     ,
SENT_FOR_TRIAL_DATE                     DATE     ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
