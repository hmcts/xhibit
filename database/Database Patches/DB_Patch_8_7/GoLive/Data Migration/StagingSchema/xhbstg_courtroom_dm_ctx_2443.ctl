OPTIONS (SKIP=1, READSIZE=5000000, BINDSIZE=5000000, ROWS=1000)

LOAD DATA
INFILE '$xhb_in_file'
APPEND
INTO TABLE DATA_MIG.XHBSTG_COURTROOM_DM
WHEN (CREST_COURT_ID != '')
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
COURTROOM_NO                            ,
OBS_IND                                 ,
CTL_ID                                  ,
SECURITY_IND                            ,
VIDEO_IND                               ,
CREST_COURT_ID                         
)
