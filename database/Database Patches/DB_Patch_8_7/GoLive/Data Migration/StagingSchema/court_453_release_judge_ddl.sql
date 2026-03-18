CREATE TABLE XHBSTG_RELEASE_JUDGE_DM
(
CASE_TYPE								VARCHAR2(1),
CASE_NO									NUMBER(8,0),
JUD_ID									NUMBER(8,0),
REQ_JUD_IND								VARCHAR2(1),
CREST_COURT_ID                          VARCHAR2(3),
XHIBIT_COURT_ID                         VARCHAR2(3),
XHIBIT_ETL_STATUS                       CHAR(1),
XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,
XHIBIT_ENRICH_DATE                      DATE,
XHIBIT_ETL_DATE                         DATE,
XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)
)
/
