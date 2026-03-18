CREATE TABLE XHBSTG_CASE_PARTY_SOF_DM
(
CPF_ID                                  NUMBER (8) NOT NULL,
CASE_TYPE                               VARCHAR2(1) NOT NULL,
CASE_NO                                 NUMBER (8) NOT NULL,
REP_ST_DATE                             DATE     ,
REP_END_DATE                            DATE     ,
LEO_SEQ                                 NUMBER (3)   ,
LIF_REC_DATE                            DATE     ,
LIF_SENT_DATE                           DATE     ,
LEO_ID                                  NUMBER (8)   ,
REP_TYPE                                VARCHAR2(1)   ,
SOL_REF                                 VARCHAR2(10)   ,
SUB_OPP_ID                              NUMBER (8)   ,
PARTY_TYPE                              VARCHAR2(1)   ,
SOF_ID                                  NUMBER (8)   ,
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
