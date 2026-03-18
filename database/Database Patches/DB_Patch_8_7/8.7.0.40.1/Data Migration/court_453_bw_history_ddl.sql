CREATE TABLE XHBSTG_BW_HISTORY_DM
(
CASE_TYPE                               VARCHAR2(1)   ,
CASE_NO                                 NUMBER (8)   ,
SUB_ID                                  NUMBER (8)   ,
BC_STATUS_BW_ISSUED                     VARCHAR2(1)   ,
BC_STATUS_BW_EXECUTED                   VARCHAR2(1)   ,
BW_ISSUE_DATE                           DATE     ,
BW_EXEC_DATE                            DATE     ,
BW_INPUT_DATE                           DATE     ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
