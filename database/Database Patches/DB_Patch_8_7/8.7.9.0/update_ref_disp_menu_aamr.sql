/* SSMORE change parent id to 212 from 2644 */
UPDATE XHB_REF_DISPOSAL_MENU SET PARENT= 212 where DISPOSAL_CODE in ('MHTRTSS','DRGRQSS','ALTRTSS','SPVRQSS','ATCRQSS','EXCURSS','FTPRSS','REHARSS') and OBS_IND = 'N';

/* CSMORE change parent id to 2619 from 2666 */
UPDATE XHB_REF_DISPOSAL_MENU SET PARENT= 2619 where DISPOSAL_CODE in ('EXCURCS','FTPRCS','REHARCS') and OBS_IND = 'N';


/* SSMORE change to set the more folder to obsolete */
UPDATE XHB_REF_DISPOSAL_MENU SET OBS_IND='Y' where ABBREV = 'SSMORE';

/* SSMORE change to set the more folder to obsolete */
UPDATE XHB_REF_DISPOSAL_MENU SET OBS_IND='Y' where ABBREV = 'CSMORE';

COMMIT;