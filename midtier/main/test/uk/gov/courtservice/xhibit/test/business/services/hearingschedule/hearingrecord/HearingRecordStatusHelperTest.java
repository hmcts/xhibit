//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
//import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordStatusHelper;
//import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
///**
// * <p>Title: HearingRecordStatusHelperTest </p>
// * <p>Description: This will test the HearingRecordStatusHelper. The test data
// * is for testing with different scenarios, linked, not linked, exported, not
// * exported etc. </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author unascribed
// * @version $Id: HearingRecordStatusHelperTest.java,v 1.6 2006/07/11 14:17:00 xzfdtb Exp $
// */
//public class HearingRecordStatusHelperTest extends TransactionTestCase
//{
//
//    private static Logger log = CSServices.getLogger(HearingRecordStatusHelperTest.class);
//    private HearingRecordStatusHelper hearingrecordstatushelper = new HearingRecordStatusHelper();
//
//    private Integer insertSingleHearingExportedInt = new Integer(-900);
//    private Integer insertLinkedHearing1Int = new Integer(-901);
//    private Integer insertLinkedHearing2Int = new Integer(-902);
//    private Integer insertSingleHearingNotExportedInt = new Integer(-903);
//    private Integer insertLinkedHearing1NotExportedInt = new Integer(-904);
//    private Integer insertLinkedHearing1ExpInt = new Integer(-906);
//    private Integer insertLinkedHearing2NotExpInt = new Integer(-907);
//    private Integer insertHearingNotEndedInt = new Integer(-908);
//    private Integer maxHearingID = new Integer(-910);
//
//
//
//  //SQL for inserting/removing test data
//  String removeAllExportAs = "delete from xhb_exporta where hearing_id >= "+
//                             insertSingleHearingExportedInt.intValue()+" and hearing_id < "+
//                             maxHearingID.intValue()+" or " +"linked_hearing_id = 999";
//
//  String removeHearings = "delete from xhb_hearing where hearing_id >= "+
//                          insertSingleHearingExportedInt.intValue()+" and hearing_id < "+
//                             maxHearingID.intValue();
//
//  //single hearing that has been exported
//  String insertSingleHearingExported = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                      " court_id, hearing_end_date, linked_hearing_id) values ("+
//                      insertSingleHearingExportedInt.intValue()+", "+TestConstants.caseID.intValue()+", "
//                      +TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+
//                      ", TO_Date( '02/21/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//  String insertExportAExportedHearing = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                       "hearing_id) values ('Marie', 'S', "+
//                                        insertSingleHearingExportedInt.intValue()+")";
//
//  //linked hearings that have been exported
//  String insertLinkedHearing1 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+
//                                insertLinkedHearing1Int.intValue()+", "+TestConstants.caseID.intValue()+", "
//                              +TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+
//                                ", TO_Date( '02/22/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 999)";
//  String insertLinkedHearing2 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+
//                        insertLinkedHearing2Int.intValue()+", "+TestConstants.caseID.intValue()+", "+
//                        TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/23/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 999)";
//  String insertExportALinkedHearing = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                       "linked_hearing_id) values ('Marie', 'S', 999)";
//
//  //single hearing that has not been exported
//  String insertSingleHearingNotExported = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                      " court_id, hearing_end_date, linked_hearing_id) values ("+
//                       insertSingleHearingNotExportedInt.intValue()+", "+TestConstants.caseID.intValue()+
//                       ", "+TestConstants.refHrgTypeID.intValue()+","+TestConstants.courtID.intValue()+", " +
//                       "TO_Date( '02/21/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
// //linked hearings that have NOT been exported
//  String insertLinkedHearing1NotExported = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+
//                        insertLinkedHearing1NotExportedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+
//                         TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/22/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 998)";
//  String insertLinkedHearing2NotExported = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values (-905, "+
//                        TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/23/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 998)";
//
//  //linked hearings where one of the hearings have been exported and an attempt is done
//  //to export the linked hearings
//  String insertLinkedHearing1Exp = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+
//                        insertLinkedHearing1ExpInt.intValue()+", "+TestConstants.caseID.intValue()+", "+
//                        TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/22/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 997)";
//  String insertLinkedHearing2NotExp = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+
//                        insertLinkedHearing2NotExpInt.intValue()+", "+TestConstants.caseID.intValue()+", "+
//                        TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/23/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 997)";
//  String insertExportALinkedHearing1Exp = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                       "hearing_id) values ('Marie', 'S', "+insertLinkedHearing1ExpInt.intValue()+")";
//
//  //hearing that has not ended
//  String insertHearingNotEnded = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+
//                        insertHearingNotEndedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+
//                        TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", null, null)";
//
//
//    /**
//     *
//     * @param s
//     */
//    public HearingRecordStatusHelperTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    /**
//     * setUp
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        try
//        {
//            TestUtils.execSql(removeAllExportAs);
//            TestUtils.execSql(removeHearings);
//            TestUtils.execSql(TestConstants.delRefHrgType);
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(TestConstants.delRefCourt);
//            TestUtils.execSql(TestConstants.delCourtRoom);
//            TestUtils.execSql(TestConstants.delCourtSite);
//            TestUtils.execSql(TestConstants.delAddr);
//            TestUtils.execSql(TestConstants.delCourt);
//
//            TestUtils.execSql(TestConstants.insCourt);
//            TestUtils.execSql(TestConstants.insAddr);
//            TestUtils.execSql(TestConstants.insCourtSite);
//            TestUtils.execSql(TestConstants.insCourtRoom);
//            TestUtils.execSql(TestConstants.insRefCourt);
//            TestUtils.execSql(TestConstants.insCase);
//            TestUtils.execSql(TestConstants.insRefHrgType);
//            TestUtils.execSql(insertSingleHearingExported);
//            TestUtils.execSql(insertExportAExportedHearing);
//            TestUtils.execSql(insertSingleHearingNotExported);
//            TestUtils.execSql(insertLinkedHearing1);
//            TestUtils.execSql(insertLinkedHearing2);
//            TestUtils.execSql(insertExportALinkedHearing);
//            TestUtils.execSql(insertLinkedHearing1NotExported);
//            TestUtils.execSql(insertLinkedHearing2NotExported);
//            TestUtils.execSql(insertLinkedHearing1Exp);
//            TestUtils.execSql(insertLinkedHearing2NotExp);
//            TestUtils.execSql(insertExportALinkedHearing1Exp);
//            TestUtils.execSql(insertHearingNotEnded);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    /**
//     * testGetExportState1a
//     * Test where a hearing has been exported and have an entry in the database.
//     */
//    public void testGetExportState1a()
//    {
//        log.debug("####################### testGetExportState1a start ########################");
//        try
//        {
//            String stringRet =
//                hearingrecordstatushelper.getExportState(insertSingleHearingExportedInt);
//            log.debug("The status flag is: " + stringRet);
//            assertEquals(stringRet, "S");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportState1a Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportState1b
//     * Test where a hearing has not been exported and do not have an entry in the database.
//     */
//    public void testGetExportState1b()
//    {
//        log.debug("####################### testGetExportState1b start ########################");
//        try
//        {
//            String stringRet =
//                hearingrecordstatushelper.getExportState(insertSingleHearingNotExportedInt);
//
//            if (stringRet != null)
//                log.debug("The status flag is: " + stringRet);
//            else
//                log.debug("The status flag is NULL as it should be");
//
//            assertEquals(stringRet, null);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportState1b Finish ########################");
//
//        }
//    }
//    /**
//     * testGetExportState1c
//     *  Test where there is an entry for the linked hearing id in the export a table
//     */
//    public void testGetExportState1c()
//    {
//        log.debug("####################### testGetExportState1c start ########################");
//        try
//        {
//            String stringRet = hearingrecordstatushelper.getExportState(insertLinkedHearing2Int);
//            log.debug("Status : " + stringRet);
//            assertEquals(stringRet, "S");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportState1c Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportState1d
//     * Test where a hearing has not been exported and do not have an entry in the database.
//     */
//    public void testGetExportState1d()
//    {
//        log.debug("####################### testGetExportState1d start ########################");
//        try
//        {
//            String stringRet =
//                hearingrecordstatushelper.getExportState(insertSingleHearingNotExportedInt);
//            if (stringRet != null)
//                log.debug("The status flag is: " + stringRet);
//            else
//                log.debug("The status flag is NULL as it should be");
//
//            assertEquals(stringRet, null);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportState1d Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportState1e
//     * Test where one of the linked hearings has been exported.
//     */
//    public void testGetExportState1e()
//    {
//        log.debug("####################### testGetExportState1e start ########################");
//        try
//        {
//            String stringRet = hearingrecordstatushelper.getExportState(insertLinkedHearing1ExpInt);
//            log.debug("Status : " + stringRet);
//            assertEquals(stringRet, "S");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportState1e Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue1a
//     * Test where there is an entry for the hearing id in the export a table
//     */
//    public void testGetExportValue1a()
//    {
//        log.debug("####################### testGetExportValue1a start ########################");
//        try
//        {
//            //find a hearing and convert to basic vo
//            HearingMaintainer maintainer = new HearingMaintainer();
//            Hearing hearing = maintainer.findByPK(insertSingleHearingExportedInt);
//            HearingBasicValue value = maintainer.getHearingBasicValue(hearing);
//
//            ExportAValue exportavalueRet = hearingrecordstatushelper.getExportValue(value);
//            log.debug("ExportA : " + exportavalueRet.toString());
//            assertEquals(exportavalueRet.getHearingID(), value.getId());
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue1a Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue1b
//     * Test where there is NOT an entry for the hearing id in the export a table
//     */
//    public void testGetExportValue1b()
//    {
//        log.debug("####################### testGetExportValue1b start ########################");
//        try
//        {
//            //find a hearing and convert to basic vo
//            HearingMaintainer maintainer = new HearingMaintainer();
//            Hearing hearing = maintainer.findByPK(insertSingleHearingNotExportedInt);
//            HearingBasicValue value = maintainer.getHearingBasicValue(hearing);
//
//            ExportAValue exportavalueRet = hearingrecordstatushelper.getExportValue(value);
//            if (exportavalueRet != null)
//            {
//                log.debug("ExportA : " + exportavalueRet.toString());
//            }
//            else
//            {
//                log.debug("exportavalueRet.toString() is NULL as it should be!");
//            }
//            assertEquals(exportavalueRet, null);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue1b Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue1c
//     * Test where there is an entry for the linked hearing id in the export a table
//     */
//    public void testGetExportValue1c()
//    {
//        log.debug("####################### testGetExportValue1c start ########################");
//        try
//        {
//            //find a hearing and convert to basic vo
//            HearingMaintainer maintainer = new HearingMaintainer();
//            Hearing hearing = maintainer.findByPK(insertLinkedHearing2Int);
//            HearingBasicValue value = maintainer.getHearingBasicValue(hearing);
//
//            ExportAValue exportavalueRet = hearingrecordstatushelper.getExportValue(value);
//            log.debug("ExportA : " + exportavalueRet.toString());
//
//            if (exportavalueRet.getLinkedHearingID() != null)
//                assertEquals(exportavalueRet.getLinkedHearingID(), value.getLinkedHearingID());
//            else
//                fail();
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue1c Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue1d
//     * Test where there is NOT an entry for the linked hearing id in the export a table
//     */
//    public void testGetExportValue1d()
//    {
//        log.debug("####################### testGetExportValue1d start ########################");
//        try
//        {
//            //find a hearing and convert to basic vo
//            HearingMaintainer maintainer = new HearingMaintainer();
//            Hearing hearing = maintainer.findByPK(insertLinkedHearing1NotExportedInt);
//            HearingBasicValue value = maintainer.getHearingBasicValue(hearing);
//
//            ExportAValue exportavalueRet = hearingrecordstatushelper.getExportValue(value);
//            if (exportavalueRet != null)
//            {
//                log.debug("ExportA : " + exportavalueRet.toString());
//            }
//            else
//            {
//                log.debug("exportavalueRet.toString() is NULL as it should be!");
//            }
//
//            assertEquals(exportavalueRet, null);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue1d Finish ########################");
//        }
//    }
//
//    /**
//       * testGetExportValue1e
//       * Test where there is an entry for the hearing id in the export a table
//       */
//    public void testGetExportValue1e()
//    {
//        log.debug("####################### testGetExportValue1e start ########################");
//        try
//        {
//            //find a hearing and convert to basic vo
//            HearingMaintainer maintainer = new HearingMaintainer();
//            Hearing hearing = maintainer.findByPK(insertSingleHearingExportedInt);
//            HearingBasicValue value = maintainer.getHearingBasicValue(hearing);
//
//            ExportAValue exportavalueRet = hearingrecordstatushelper.getExportValue(value);
//            log.debug("ExportA : " + exportavalueRet.toString());
//            assertEquals(exportavalueRet.getHearingID(), value.getId());
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue1e Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue2a
//     * Test where there is an entry for the hearing id in the export a table
//     *
//     * this is the same as testGetExportValue1a but this one takes
//     * only an integer but they will call the same underlaying private methods.
//     */
//    public void testGetExportValue2a()
//    {
//        log.debug("####################### testGetExportValue2a start ########################");
//        try
//        {
//            ExportAValue exportavalueRet =
//                hearingrecordstatushelper.getExportValue(insertSingleHearingExportedInt);
//            log.debug("ExportA : " + exportavalueRet.toString());
//
//            assertEquals(exportavalueRet.getHearingID(), insertSingleHearingExportedInt);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue2a Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue2b
//     * Test where there is NOT an entry for the hearing id in the export a table
//     *
//     * this is the same as testGetExportValue1b but this one takes
//     * only an integer but they will call the same underlaying private methods.
//     */
//    public void testGetExportValue2b()
//    {
//        log.debug("####################### testGetExportValue2b start ########################");
//        try
//        {
//            ExportAValue exportavalueRet =
//                hearingrecordstatushelper.getExportValue(insertSingleHearingNotExportedInt);
//
//            if (exportavalueRet != null)
//            {
//                log.debug("ExportA : " + exportavalueRet.toString());
//            }
//            else
//            {
//                log.debug("exportavalueRet is NULL as it should be!");
//            }
//
//            assertEquals(exportavalueRet, null);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue2b Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue2c
//     * Test where there is an entry for the linked hearing id in the export a table
//     *
//     * this is the same as testGetExportValue1c but this one takes
//     * only an integer but they will call the same underlaying private methods.
//     */
//    public void testGetExportValue2c()
//    {
//        log.debug("####################### testGetExportValue2c start ########################");
//        try
//        {
//            ExportAValue exportavalueRet =
//                hearingrecordstatushelper.getExportValue(insertLinkedHearing2Int);
//            log.debug("ExportA : " + exportavalueRet.toString());
//
//            if (exportavalueRet.getLinkedHearingID() != null)
//            {
//                //find the hearing and convert to basic vo
//                HearingMaintainer maintainer = new HearingMaintainer();
//                Hearing hearing = maintainer.findByPK(insertLinkedHearing2Int);
//                HearingBasicValue value = maintainer.getHearingBasicValue(hearing);
//                assertEquals(exportavalueRet.getLinkedHearingID(), value.getLinkedHearingID());
//            }
//            else
//            {
//                fail();
//            }
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue2c Finish ########################");
//        }
//    }
//
//    /**
//     * testGetExportValue2d
//     *
//     * Test where there is NOT an entry for the linked hearing id in the export a table
//     *
//     * this is the same as testGetExportValue1d but this one takes
//     * only an integer but they will call the same underlaying private methods.
//     */
//    public void testGetExportValue2d()
//    {
//        log.debug("####################### testGetExportValue2d start ########################");
//        try
//        {
//            ExportAValue exportavalueRet =
//                hearingrecordstatushelper.getExportValue(insertLinkedHearing1NotExportedInt);
//            if (exportavalueRet != null)
//            {
//                log.debug("ExportA : " + exportavalueRet.toString());
//            }
//            else
//            {
//                log.debug("exportavalueRet.toString() is NULL as it should be!");
//            }
//
//            assertEquals(exportavalueRet, null);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testGetExportValue2d Finish ########################");
//        }
//    }
//
//    /**
//     * testHasHearingEnded
//     */
//    public void testHasHearingEnded()
//    {
//        log.debug("####################### testHasHearingEnded Start ########################");
//        try
//        {
//            //hearing has ended
//            Boolean booleanRet1 =
//                hearingrecordstatushelper.hasHearingEnded(insertSingleHearingExportedInt);
//
//            //hearing has not ended
//            Boolean booleanRet2 =
//                hearingrecordstatushelper.hasHearingEnded(insertHearingNotEndedInt);
//
//            log.debug("booleanRet1 (should be true): " + booleanRet1.toString());
//            log.debug("booleanRet2 (should be false): " + booleanRet2.toString());
//
//            if (booleanRet1.booleanValue() && !booleanRet2.booleanValue())
//            {
//                assertTrue(true);
//            }
//            else
//            {
//                fail();
//            }
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(
//                "####################### testHasHearingEnded Finish ########################");
//        }
//    }
//
//    /**
//     * testIsExporte1a
//     * Test where there is an entry for the hearing id in the export a table
//     */
//    public void testIsExported1a()
//    {
//        log.debug("####################### testIsExporte1a Start ########################");
//        try
//        {
//            Boolean booleanRet =
//                hearingrecordstatushelper.isExported(insertSingleHearingExportedInt);
//
//            log.debug("booleanRet : " + booleanRet.toString());
//
//            assertEquals(booleanRet.booleanValue(), true);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug("####################### testIsExporte1a Finish ########################");
//        }
//    }
//
//    /**
//    * testIsExporte1b
//    * Test where there is NOT an entry for the hearing id in the export a table
//    */
//    public void testIsExported1b()
//    {
//        log.debug("####################### testIsExporte1b Start ########################");
//        try
//        {
//            Boolean booleanRet =
//                hearingrecordstatushelper.isExported(insertSingleHearingNotExportedInt);
//
//            log.debug("booleanRet : " + booleanRet.toString());
//
//            assertEquals(booleanRet.booleanValue(), false);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug("####################### testIsExporte1b Finish ########################");
//        }
//    }
//
//    /**
//     * testIsExporte1c
//     * Test where there is an entry for the linked hearing id in the export a table
//     */
//    public void testIsExported1c()
//    {
//        log.debug("####################### testIsExporte1c Start ########################");
//        try
//        {
//            Boolean booleanRet = hearingrecordstatushelper.isExported(insertLinkedHearing2Int);
//
//            log.debug("booleanRet : " + booleanRet.toString());
//
//            assertEquals(booleanRet.booleanValue(), true);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug("####################### testIsExporte1c Finish ########################");
//        }
//    }
//
//    /**
//     * testIsExported1d
//     * Test where there is NOT an entry for the linked hearing id in the export a table
//     */
//    public void testIsExported1d()
//    {
//        log.debug("####################### testIsExported1d Start ########################");
//        try
//        {
//            Boolean booleanRet =
//                hearingrecordstatushelper.isExported(insertLinkedHearing1NotExportedInt);
//
//            log.debug("booleanRet : " + booleanRet.toString());
//
//            assertEquals(booleanRet.booleanValue(), false);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug("####################### testIsExported1d Finish ########################");
//        }
//    }
//}