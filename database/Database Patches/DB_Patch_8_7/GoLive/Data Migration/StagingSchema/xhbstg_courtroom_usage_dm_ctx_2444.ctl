OPTIONS (SKIP=1, READSIZE=5000000, BINDSIZE=5000000, ROWS=1000)

LOAD DATA
INFILE '$xhb_in_file'
APPEND
INTO TABLE DATA_MIG.XHBSTG_COURTROOM_USAGE_DM
WHEN (CREST_COURT_ID != '')
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
CRU_ID                                  ,
SITTING_DATE                            ,
PM_TIME_MINS                            ,
AM_TIME_CIV_HOURS                       ,
PM_TIME_CIV_MINS                        ,
SITE_CODE                               ,
PM_TIME_CIV_HOURS                       ,
AM_TIME_CIV_MINS                        ,
PM_TIME_HOURS                           ,
AM_TIME_MINS                            ,
CTL_ID                                  ,
AM_TIME_HOURS                           ,
COURTROOM_NO                            ,
CREST_COURT_ID                         
)
