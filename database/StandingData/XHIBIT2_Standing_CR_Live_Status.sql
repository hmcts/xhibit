
--  FILE_NAME:         XHIBIT2_Standing_CR_Live_Status.sql                                                       
--  ENVIRONMENT:                
--  DESCRIPTION:        
--                     
--  STATUS DESCRIPTION:Script is suitable for running initially on set-up of the database on any environment.
--  DEPENDENCIES:      THIS SCRIPT SHOULD ONLY BE RUN ONCE ON INITIAL SET-UP OF THE DATABASE AFTER MERCATOR MAPS HAVE IMPORTED THE CREST REFERENCE                  
--  OWNER:             Nick Sawyer
--

INSERT INTO xhb_cr_live_status (court_room_id, time_status_set, internet_status)
select court_room_id,
       SYSDATE,
       'No Information to display'
FROM   xhb_court_room;

COMMIT;
