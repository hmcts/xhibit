OPTIONS (SKIP=1, READSIZE=5000000, BINDSIZE=5000000, ROWS=1000)

LOAD DATA
INFILE '$xhb_in_file'
APPEND
INTO TABLE DATA_MIG.XHBSTG_JUDGE_USAGE_DM
WHEN (CREST_COURT_ID != '')
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
JUD_ID                                  ,
JUU_ID                                  ,
SITTING_DATE                            ,
COURTROOM_NO                            ,
SITE_CODE                               ,
MAIN_WORK_TYPE                          ,
TYPE_OF_WORK                            ,
COURT_CHAMBERS_IND                      ,
CREST_COURT_ID                         
)
