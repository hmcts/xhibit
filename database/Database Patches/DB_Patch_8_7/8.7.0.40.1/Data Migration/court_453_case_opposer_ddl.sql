CREATE TABLE XHBSTG_CASE_OPPOSER_DM
(
CASE_TYPE                               VARCHAR2(1) NOT NULL,
OPP_ID                                  NUMBER (8) NOT NULL,
CASE_NO                                 NUMBER (8) NOT NULL,
OPPOSER_TYPE                            VARCHAR2(1)   ,
OPPOSER_CASE_REF                        VARCHAR2(11)   ,
COLLECTING_COURT                        VARCHAR2(5)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
