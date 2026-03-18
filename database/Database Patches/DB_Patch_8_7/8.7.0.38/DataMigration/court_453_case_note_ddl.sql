CREATE TABLE XHBSTG_CASE_NOTE_DM
(
CAN_ID                                  NUMBER (8) NOT NULL,
NOTE                                    VARCHAR2(56)   ,
CASE_NO                                 NUMBER (8)   ,
DIARY_DATE                              DATE     ,
NOTE_TYPE                               VARCHAR2(1)   ,
CASE_TYPE                               VARCHAR2(1)   ,
NOTE_DATE                               DATE     ,
NOTE_PRINT_IND                          VARCHAR2(1)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
