CREATE TABLE XHBSTG_COURTROOM_DAY_DM
(
CTD_ID                                  NUMBER (8) NOT NULL,
JP1_NAME                                VARCHAR2(35)   ,
JUD_ID                                  NUMBER (8)   ,
JP2_NAME                                VARCHAR2(35)   ,
JP4_NAME                                VARCHAR2(35)   ,
SITTING_NOTE                            VARCHAR2(80)   ,
LIST_TYPE                               VARCHAR2(1)   ,
COMMIT_FLAG                             VARCHAR2(1)   ,
START_TIME                              VARCHAR2(8)   ,
OLD_CTD_ID                              NUMBER (8)   ,
JUD_SEQ_NO                              NUMBER (1)   ,
JUD_SIT_TYPE                            VARCHAR2(2)   ,
JP3_NAME                                VARCHAR2(35)   ,
LIST_DATE                               DATE     ,
COURTROOM_NO                            NUMBER (2)   ,
SITE_CODE                               VARCHAR2(1)   ,
JUD_SIT_IND                             VARCHAR2(2)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
