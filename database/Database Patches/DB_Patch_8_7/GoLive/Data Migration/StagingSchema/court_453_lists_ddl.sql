CREATE TABLE XHBSTG_LISTS_DM
(
LST_ID                                  NUMBER (8) NOT NULL,
START_DATE                              DATE     ,
END_DATE                                DATE     ,
DATE_PUBLISHED                          DATE     ,
LIST_TYPE                               VARCHAR2(1)   ,
CC_IND                                  VARCHAR2(2)   ,
LIST_STATUS                             VARCHAR2(5)   ,
EDITION_NO                              NUMBER (2)   ,
FCL_START_TIME                          VARCHAR2(8)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
