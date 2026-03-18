CREATE TABLE XHBSTG_COURTROOM_DM
(
COURTROOM_NO                            NUMBER (2)   ,
OBS_IND                                 VARCHAR2(1)   ,
CTL_ID                                  NUMBER (8)   ,
SECURITY_IND                            VARCHAR2(1)   ,
VIDEO_IND                               VARCHAR2(1)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
