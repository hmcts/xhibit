CREATE TABLE XHBSTG_COURTROOM_USAGE_DM
(
CRU_ID                                  NUMBER (8) NOT NULL,
SITTING_DATE                            DATE     ,
PM_TIME_MINS                            NUMBER (2)   ,
AM_TIME_CIV_HOURS                       NUMBER (2)   ,
PM_TIME_CIV_MINS                        NUMBER (2)   ,
SITE_CODE                               VARCHAR2(1)   ,
PM_TIME_CIV_HOURS                       NUMBER (2)   ,
AM_TIME_CIV_MINS                        NUMBER (2)   ,
PM_TIME_HOURS                           NUMBER (2)   ,
AM_TIME_MINS                            NUMBER (2)   ,
CTL_ID                                  NUMBER (8)   ,
AM_TIME_HOURS                           NUMBER (2)   ,
COURTROOM_NO                            NUMBER (2)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
