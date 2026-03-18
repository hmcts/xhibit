CREATE TABLE XHBSTG_JUDGE_USAGE_DM
(
JUD_ID                                  NUMBER (8) NOT NULL,
JUU_ID                                  NUMBER (8) NOT NULL,
SITTING_DATE                            DATE     ,
COURTROOM_NO                            NUMBER (2)   ,
SITE_CODE                               VARCHAR2(1)   ,
MAIN_WORK_TYPE                          VARCHAR2(2)   ,
TYPE_OF_WORK                            VARCHAR2(2)   ,
COURT_CHAMBERS_IND                      VARCHAR2(3)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
