--  FILE_NAME:         XHIBIT2_Standing_PublicNotice_Ref_TAA.sql                                                              
--  ENVIRONMENT:       CTE                                                                    
--  DESCRIPTION:       This script populates XHB_DEFINITIVE_PUBLIC_NOTICE, XHB_PUBLIC_NOTICE, xhb_configured_public_notice  
--                     to insert meta-data for public notices for all courts.                                                 
--  STATUS DESCRIPTION:Script is suitable for running initially on set-up of the database on any environment.
--                     Adds data for all courts in XHB_COURT table.
--  DEPENDENCIES:      It assumes there is court data in XHB_COURT and XHB_COURT_ROOM and 
--                     there is no data in XHB_PUBLIC_NOTICE or XHB_DEFINITIVE_PUBLIC_NOTICE  
--                     Note. THIS SCRIPT SHOULD ONLY BE RUN ONCE ON INITIAL SET-UP OF THE DATABASE                   
--  OWNER:             Pat Fox
--

delete from XHB_CONFIGURED_PUBLIC_NOTICE;
delete from XHB_PUBLIC_NOTICE;
delete from XHB_DEFINITIVE_PUBLIC_NOTICE;

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

DECLARE 

  var_court_id NUMBER;

  CURSOR c_court_id IS
    SELECT court_id
    FROM   xhb_court;

BEGIN
  OPEN c_court_id;

  LOOP

    FETCH c_court_id
    INTO  var_court_id;

    EXIT WHEN c_court_id%NOTFOUND;

	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Reporting restrictions. For details please see Court Manager.', 100);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'In chambers, no entry.' , 200);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Members of the public are requested not to enter.' , 300);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'TV link in progress - please enter quietly.' , 400);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Video being played - please enter quietly.' , 500);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'If you wish to enter, please do so quietly.' , 600);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Reporting restrictions lifted.' , 700);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Please switch off mobile phones.' , 800);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Food and drink must not be consumed in the courtrooms.', 900);
	insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Bench warrant in progress.' , 1000);

	insert into xhb_configured_public_notice  ( IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID)
	select 0, court_room_id, public_notice_id from xhb_public_notice pn, xhb_court_room cr
	where pn.court_id = var_court_id
	and cr.court_room_id in ( select court_room_id 
	                          from   XHB_COURT_ROOM cr2, XHB_COURT_SITE cs
	                          where  cr2.court_site_id = cs.court_site_id
	                          and    cs.COURT_ID = var_court_id );

    COMMIT;

  END LOOP;

  CLOSE c_court_id;

END;
/
