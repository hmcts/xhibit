CREATE TABLE XHBSTG_SUBJECT_HISTORY_DM
(
SUB_ID                                  NUMBER (8) NOT NULL,
SURNAME                                 VARCHAR2(35) NOT NULL,
FORENAME1                               VARCHAR2(35)   ,
FORENAME2                               VARCHAR2(35)   ,
DOB                                     DATE     ,
SEX                                     VARCHAR2(1)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
