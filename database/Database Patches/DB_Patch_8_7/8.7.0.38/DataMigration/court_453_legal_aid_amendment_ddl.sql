CREATE TABLE XHBSTG_LEGAL_AID_AMENDMENT_DM
(
SEQ_NO                                  NUMBER (7) NOT NULL,
LEO_ID                                  NUMBER (8) NOT NULL,
CHANGE_TYPE                             VARCHAR2(6)   ,
AMENDMENT_DATE                          DATE     ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
