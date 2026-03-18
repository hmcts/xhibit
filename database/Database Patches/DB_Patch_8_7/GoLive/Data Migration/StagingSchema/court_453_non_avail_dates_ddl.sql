CREATE TABLE XHBSTG_NON_AVAIL_DATES_DM
(
NAD_ID                                  NUMBER (8) NOT NULL,
CASE_NO                                 NUMBER (8) NOT NULL,
CASE_TYPE                               VARCHAR2(1) NOT NULL,
REASON                                  VARCHAR2(70)   ,
START_DATE                              DATE     ,
END_DATE                                DATE     ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
