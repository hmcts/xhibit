--  FILE_NAME:         XHIBIT2_Standing_CC_Info.sql                                                              
--  ENVIRONMENT:       Live, System, Pre-production                                                                    
--  DESCRIPTION:       This script populates xhb_cc_info  
--                     The CC Info table is used by xhb_sh_leg_rep to identify which message has been selected
--                     at sign in of defence/appleant counsel with the thin client counsel facilities. 
--                     Actual message text is in the resource bundles, 
--                     and what is recorded here as 'cc_info_text' is the key of the message 
--                     in the rsc bundle $Log: XHIBIT2_Standing_CC_Info.sql
--                     note. previously this script was called populate_cc_info. */
--  STATUS DESCRIPTION:Script is suitable for running initially on set-up of the database on any environment.
--  DEPENDENCIES:      THIS SCRIPT SHOULD ONLY BE RUN ONCE ON INITIAL SET-UP OF THE DATABASE                   
--  OWNER:             Frederik Vandendriessche
--



/* Insert new version of the data */
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  1,  '1' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  2,  '2' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  3,  '3' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  4,  '4' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  5,  '5' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  6,  '6' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  7,  '7' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  8,  '8' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values (  9,  '9' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values ( 10, '10' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values ( 11, '11' );
insert into xhb_cc_info (cc_info_id, cc_info_text) values ( 12, '12' );

/* Commit the D/B updates */
commit;

/* Alter the sequence to commence from a suitable value */
DROP SEQUENCE XHB_CC_INFO_SEQ;
CREATE SEQUENCE XHB_CC_INFO_SEQ START WITH 20 INCREMENT BY 1 MINVALUE 1
NOCACHE NOCYCLE NOORDER;
COMMIT;
