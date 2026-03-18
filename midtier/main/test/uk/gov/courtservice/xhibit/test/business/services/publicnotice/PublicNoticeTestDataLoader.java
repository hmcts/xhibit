package uk.gov.courtservice.xhibit.test.business.services.publicnotice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>Title: Util class used for Loading Data Only</p><p>Description: Loads
 * and cleans data required for publicNotice unit tests</p><p>Copyright:
 * Copyright (c) 2003</p><p>Company: Electronic Data Systems</p>
 * 
 * @author Pat Fox
 * @version 1.0
 */
public class PublicNoticeTestDataLoader
{

    private static final Logger log = CSServices.getLogger(PublicNoticeTestDataLoader.class);

    final static String[] LOAD_DATA = {
                    "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME ) VALUES ( 9999, 'Funky Court', 'WEST', 'BATCOURT', '999', 'PAT COURT', 'PFCOU', 80, 'CSA00110:90', 'Y','N', 'BAT COURT OFFICE', 'BAT CROWN COURT','BAT CROWN COURT')",
                    "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME ) VALUES (99999 , 'BATFORD', 'A', 9999, 80,  'BAT ford Court Site A' )",
                    "INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES (999990, 'Court 0', 'Court Room 0', 1, 99999, 'N', 'Court Room 0')",
                    "INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES (999991, 'Court 1', 'Court Room 1', 1, 99999, 'N', 'Court Room 1')",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8001  ,9999,   'Reporting restrictions. For details please see Court Manager.', 100)",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8002  ,9999,   'In chambers, no entry.' , 200)                                      ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8003  ,9999,   'Members of the public are requested not to enter.' , 300)           ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8004  ,9999,   'TV link in progress - please enter quietly.' , 400)                 ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8005  ,9999,   'Video being played - please enter quietly.' , 500)                  ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8006  ,9999,   'If you wish to enter, please do so quietly.' , 600)                 ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8007  ,9999,   'Reporting restrictions lifted.' , 700)                              ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8008  ,9999,   'Please switch off mobile phones.' , 800)                            ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8009  ,9999,   'Food and drink must not be consumed in the courtrooms.', 900)       ",
                    "insert into XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values (8010  ,9999,   'Bench warrant in progress.' , 1000)                                 ",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120001, 999990, 0 ,8001)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120002, 999990, 0 ,8002)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120003, 999990, 0 ,8003)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120004, 999990, 0 ,8004)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120005, 999990, 0 ,8005)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120006, 999990, 0 ,8006)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120007, 999990, 0 ,8007)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120008, 999990, 0 ,8008)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120009, 999990, 0 ,8009)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (120010, 999990, 0 ,8010)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130001, 999991, 0 ,8001)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130002, 999991, 0 ,8002)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130003, 999991, 0 ,8003)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130004, 999991, 0 ,8004)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130005, 999991, 0 ,8005)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130006, 999991, 0 ,8006)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130007, 999991, 0 ,8007)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130008, 999991, 0 ,8008)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130009, 999991, 0 ,8009)",
                    "insert into xhb_configured_public_notice(CONFIGURED_PUBLIC_NOTICE_ID,COURT_ROOM_ID, IS_ACTIVE,PUBLIC_NOTICE_ID) values (130010, 999991, 0 ,8010)" };

    final static String[] REMOVE_DATA = {
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130001",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130002",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130003",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130004",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130005",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130006",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130007",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130008",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130009",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 130010",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120001",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120002",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120003",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120004",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120005",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120006",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120007",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120008",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120009",
                    "DELETE FROM xhb_configured_public_notice WHERE CONFIGURED_PUBLIC_NOTICE_ID= 120010",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8001",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8002",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8003",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8004",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8005",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8006",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8007",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8008",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8009",
                    "DELETE XHB_PUBLIC_NOTICE WHERE PUBLIC_NOTICE_ID = 8010",
                    "DELETE FROM XHB_COURT_ROOM WHERE COURT_ROOM_ID =  999991",
                    "DELETE FROM XHB_COURT_ROOM WHERE COURT_ROOM_ID =  999990 ",
                    "DELETE FROM XHB_COURT_SITE WHERE COURT_SITE_ID = 99999 ",
                    "DELETE FROM XHB_COURT WHERE COURT_ID = 9999 "

    };

    public static void load(Connection connection) throws Exception
    {
        log.debug("LOAD DATA");

        Statement stmt = connection.createStatement();
        ResultSet rset = null;
        try {
            // load the data
            for (int i = 0; i < LOAD_DATA.length; i++) {
                log.debug("Running SQL statement number[" + i + "]" + LOAD_DATA[i]);
                rset = stmt.executeQuery(LOAD_DATA[i]);
                rset.close();
            }
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (Exception e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
        }

    }

    public static void clean(Connection connection) throws Exception
    {
        log.debug("CLEANING DATA");

        Statement stmt = connection.createStatement();
        ResultSet rset = null;
        try {
            // load the data
            for (int i = 0; i < REMOVE_DATA.length; i++) {
                log.debug("Running SQL statement number[" + i + "]" + REMOVE_DATA[i]);
                rset = stmt.executeQuery(REMOVE_DATA[i]);
                rset.close();
            }
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (Exception e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
        }
    }

}