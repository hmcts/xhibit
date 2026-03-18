CREATE TABLE XHBSTG_COMMITTAL_CHARGE_DM
(
CCH_ID                                  NUMBER (8) NOT NULL,
CASE_TYPE                               VARCHAR2(1) NOT NULL,
CASE_NO                                 NUMBER (8) NOT NULL,
CHARGE_LINE                             VARCHAR2(80)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
