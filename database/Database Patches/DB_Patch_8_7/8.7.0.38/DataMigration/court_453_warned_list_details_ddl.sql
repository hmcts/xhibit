CREATE TABLE XHBSTG_WARNED_LIST_DETAILS_DM
(
CC_IND                                  VARCHAR2(2) NOT NULL,
WARNED_PERIOD                           NUMBER (2)   ,
WL_REP_PERIOD                           NUMBER (2)   ,
WL_REP_TIME                             VARCHAR2(8)   ,
WL_TEXT1                                VARCHAR2(80)   ,
WL_TEXT2                                VARCHAR2(80)   ,
WL_TEXT3                                VARCHAR2(80)   ,
WL_REP_SORT                             VARCHAR2(1)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
