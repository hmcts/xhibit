--  FILE_NAME:         XHIBIT2_Standing_PublicNotice_Ref.sql                                                              
--  ENVIRONMENT:       Live, System, Pre-production                                                                    
--  DESCRIPTION:       This script populates XHB_DEFINITIVE_PUBLIC_NOTICE, XHB_PUBLIC_NOTICE, xhb_configured_public_notice  
--                     to insert meta-data for public notices for all courts.                                                 
--  STATUS DESCRIPTION:Script is suitable for running initially on set-up of the database on any environment.
--                     Adds data for three courts with court_id = 1, 2 or 3.
--                     Further enhancement will be required to allow data to be added on setting up a new court
--  DEPENDENCIES:      It assumes there is court data in XHB_COURT and XHB_COURT_ROOM and 
--                     there is no data in XHB_PUBLIC_NOTICE or XHB_DEFINITIVE_PUBLIC_NOTICE  
--                     Note. THIS SCRIPT SHOULD ONLY BE RUN ONCE ON INITIAL SET-UP OF THE DATABASE                   
--  OWNER:             Pat Fox
--

insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 100 , 'Reporting restrictions. For details please see Court Manager.',10);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 200 , 'In chambers, no entry.', 20);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 300 , 'Members of the public are requested not to enter.', 30);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 400 , 'TV link in progress - please enter quietly.', 40);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 500 , 'Video being played - please enter quietly.', 50 );
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 600 , 'If you wish to enter, please do so quietly.', 60);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 700 , 'Reporting restrictions lifted.', 70);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 800 , 'Please switch off mobile phones.', 80);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 900 , 'Food and drink must not be consumed in the courtrooms.', 90);
insert into XHB_DEFINITIVE_PUBLIC_NOTICE( DEFINITIVE_PN_ID, DEFINITIVE_PN_DESC ,PRIORITY) values ( 1000 , 'Bench warrant in progress.', 100);
commit;

--COURT 1
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'Reporting restrictions. For details please see Court Manager.', 100);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'In chambers, no entry.' , 200);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'Members of the public are requested not to enter.' , 300);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'TV link in progress - please enter quietly.' , 400);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'Video being played - please enter quietly.' , 500);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'If you wish to enter, please do so quietly.' , 600);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'Reporting restrictions lifted.' , 700);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'Please switch off mobile phones.' , 800);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'Food and drink must not be consumed in the courtrooms.', 900);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 1,   'Bench warrant in progress.' , 1000);
--COURT 2
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'Reporting restrictions. For details please see Court Manager.', 100);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'In chambers, no entry.' , 200);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'Members of the public are requested not to enter.' , 300);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'TV link in progress - please enter quietly.' , 400);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'Video being played - please enter quietly.' , 500);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'If you wish to enter, please do so quietly.' , 600);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'Reporting restrictions lifted.' , 700);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'Please switch off mobile phones.' , 800);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'Food and drink must not be consumed in the courtrooms.', 900);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 2,   'Bench warrant in progress.' , 1000);

--COURT 3
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'Reporting restrictions. For details please see Court Manager.', 100);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'In chambers, no entry.' , 200);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'Members of the public are requested not to enter.' , 300);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'TV link in progress - please enter quietly.' , 400);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'Video being played - please enter quietly.' , 500);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'If you wish to enter, please do so quietly.' , 600);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'Reporting restrictions lifted.' , 700);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'Please switch off mobile phones.' , 800);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'Food and drink must not be consumed in the courtrooms.', 900);
insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( 3,   'Bench warrant in progress.' , 1000);

commit;


delete from xhb_configured_public_notice 

/* insert a cartesian set of xhb_court_room & xhb_public_notice */ 
insert into xhb_configured_public_notice  ( IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID)
select 0, court_room_id, public_notice_id from xhb_public_notice pn, xhb_court_room cr
where pn.court_id = 1
and cr.court_room_id <= 20;

insert into xhb_configured_public_notice  ( IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID)
select 0, court_room_id, public_notice_id from xhb_public_notice pn, xhb_court_room cr
where pn.court_id = 2
and cr.court_room_id > 20
and cr.court_room_id <=30;

insert into xhb_configured_public_notice  ( IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID)
select 0, court_room_id, public_notice_id from xhb_public_notice pn, xhb_court_room cr
where pn.court_id = 3
and cr.court_room_id > 30
and cr.court_room_id <= 38;




commit;