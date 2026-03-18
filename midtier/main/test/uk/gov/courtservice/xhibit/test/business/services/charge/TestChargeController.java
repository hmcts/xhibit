//package uk.gov.courtservice.xhibit.test.business.services.charge;
//
//import java.sql.ResultSet;
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Vector;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.services.charge.ChargeController;
//import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerHome;
//import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
//import uk.gov.courtservice.xhibit.business.services.charge.OutOfTimeException;
//import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
//import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
//
//public class TestChargeController extends TestCase
//{
//  private static final Logger log = CSServices.getLogger( TestChargeController.class );
//  private ChargeController controller = null;
//
//  public TestChargeController(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp() throws Exception
//  {
//    controller = (ChargeController)CSServices.getEJBServices().
//                   createRemoteSession(ChargeControllerHome.class);
//    TestUtils.execSql("insert into xhb_original_result (ORIGINAL_RESULT_ID, REF_COURT_ID, REF_DISPOSAL_ID) values (1, 321, 1)");
//    TestUtils.execSql("insert into xhb_disposal_detail (DISPOSAL_DETAIL_ID, ORIGINAL_RESULT_ID) values (1, 1)");
//    TestUtils.execSql("insert into xhb_defendant_on_offence (DEFENDANT_ON_OFFENCE_ID, DEFENDANT_ON_CASE_ID, OFFENCE_ID, ORIGINAL_RESULT_ID) values (30, 16, 11, 1)");
//    TestUtils.execSql("update xhb_case set judge_reason_for_appeal = 'Test Reason For Appeal' where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_1 = 'Info 1' where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_2 = 'Info 2' where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_3 = 'Info 3' where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_4 = 'Info 4' where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_5 = 'Info 5' where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_6 = 'Info 6' where case_id = 18");
//  }
//
//  protected void tearDown() throws Exception
//  {
//    TestUtils.execSql("delete from xhb_defendant_on_offence where defendant_on_offence_id = 30");
//    TestUtils.execSql("delete from xhb_disposal_detail where DISPOSAL_DETAIL_ID = 1");
//    TestUtils.execSql("delete from xhb_original_result where ORIGINAL_RESULT_ID = 1");
//    TestUtils.execSql("update xhb_case set judge_reason_for_appeal = null where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_1 = null where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_2 = null where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_3 = null where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_4 = null where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_5 = null where case_id = 18");
//    TestUtils.execSql("update xhb_case set indictment_info_6 = null where case_id = 18");
//  }
//
//  // updated this method to use iteration 2 test data
//  public void testGetCharges() throws Exception
//  {
//      Integer caseID =  new Integer(18);
//      ChargeCompositeValue chargeCompositeValueRet = controller.getCharges(caseID, true);
//      assertNotNull(chargeCompositeValueRet);
//
//      CaseBasicValue caseBasicVal = chargeCompositeValueRet.getCaseBasicValue();
//      assertEquals("Test Reason For Appeal", caseBasicVal.getJudgeReasonForAppeal());
//
//      Collection charges = chargeCompositeValueRet.getCharges();
//      assertNotNull(charges);
//
//      Iterator it = charges.iterator();
//
//      while (it.hasNext())
//      {
//        ChargeValue chargeValue = (ChargeValue)it.next();
//
//        // we're only going to check charge 12
//        if (chargeValue.getChargeID().intValue() != 12)
//          continue;
//
//        assertEquals(12, chargeValue.getChargeID().intValue());
//        assertEquals("O", chargeValue.getChargeType());
//        assertEquals("Summary Offence (section 41)", chargeValue.getChargeTypeDescription());
//        assertEquals(24948, chargeValue.getCrestChargeID().intValue());
//        assertEquals(1, chargeValue.getCrestChargeSeqNo().intValue());
//        assertEquals(18, chargeValue.getCaseID().intValue());
//        assertNull(chargeValue.getProsPaperServedDate()); // this is null in new test data
////        assertEquals(6, chargeValue.getProsPaperServedDate().get(Calendar.DAY_OF_MONTH));
////        assertEquals(6, chargeValue.getProsPaperServedDate().get(Calendar.MONTH)+1);
////        assertEquals(2002, chargeValue.getProsPaperServedDate().get(Calendar.YEAR));
//        assertNull(chargeValue.getDateIndRec()); // this is null in new test data
////        assertEquals(6, chargeValue.getDateIndRec().get(Calendar.DAY_OF_MONTH));
////        assertEquals(6, chargeValue.getDateIndRec().get(Calendar.MONTH)+1);
////        assertEquals(2002, chargeValue.getDateIndRec().get(Calendar.YEAR));
//        assertNull(chargeValue.getDefendantID()); // not a breach
//        assertNull(chargeValue.getIndResp());
//        assertNull(chargeValue.getIndSignedDate()); // this is null in new test data
////        assertEquals(6, chargeValue.getIndSignedDate().get(Calendar.DAY_OF_MONTH));
////        assertEquals(6, chargeValue.getIndSignedDate().get(Calendar.MONTH)+1);
////        assertEquals(2002, chargeValue.getIndSignedDate().get(Calendar.YEAR));
//
//        // Breach
//        BreachValue breachValue = chargeValue.getBreachValue();
//        assertNull(breachValue); // this is null in new test data
//
////        assertEquals(381, breachValue.getBreachID().intValue());
////        assertEquals("B", breachValue.getBreachType());
////        assertEquals("1", breachValue.getBringBack());
////        assertEquals("O", breachValue.getOriginalCourtType());
////        assertEquals("O", breachValue.getOriginalSentence());
////        assertEquals(6, breachValue.getDatePut().get(Calendar.DAY_OF_MONTH));
////        assertEquals(6, breachValue.getDatePut().get(Calendar.MONTH)+1);
////        assertEquals(2002, breachValue.getDatePut().get(Calendar.YEAR));
////        assertEquals(1, breachValue.getCaseID().intValue());
////        assertEquals("C", breachValue.getHoCode());
////        assertEquals("D", breachValue.getHoDescription());
////        assertEquals(9613, breachValue.getOriginalCourtID().intValue());
////        assertEquals("O", breachValue.getOriginalCourtType());
////        assertEquals("O", breachValue.getOriginalSentence());
////        assertEquals(6, breachValue.getOriginalSentenceDate().get(Calendar.DAY_OF_MONTH));
////        assertEquals(6, breachValue.getOriginalSentenceDate().get(Calendar.MONTH)+1);
////        assertEquals(2002, breachValue.getOriginalSentenceDate().get(Calendar.YEAR));
////        assertEquals(31, breachValue.getChargeID().intValue());
////        assertEquals("C", breachValue.getOriginalCourtName());
////        assertEquals("", breachValue.getPlea());
////        assertEquals(9561, breachValue.getRefSystemCodeID().intValue());
//
//        // Offence
//        Collection offenceValues = chargeValue.getOffenceValues();
//        assertNotNull(offenceValues);
//
//        Iterator it2 = offenceValues.iterator();
//        while (it2.hasNext()) // loop is only executed once with current test data 25/02/2003
//        {
//          OffenceValue offenceValue = (OffenceValue)it2.next();
//          if (offenceValue.getOffenceID().intValue() != 11)
//            continue;
//          assertEquals(18, offenceValue.getCaseID().intValue());
//          assertEquals(12, offenceValue.getChargeID().intValue());
//          assertNull(offenceValue.getCrestOffenceFreeText());
//          assertEquals(1, offenceValue.getCrestOffenceSeqNo().intValue());
//          assertNull(offenceValue.getMultiple());
//          assertEquals("Person having charge procuring abandonment of animal", offenceValue.getOffenceDescription());
//          assertEquals(3, offenceValue.getRefOffenceID().intValue());
//          assertEquals(24948, offenceValue.getCrestOffenceID().intValue());
//          assertEquals("", offenceValue.getPlea());
//          assertEquals("AA60004", offenceValue.getOffenceCode());
//
//          // check the defendant ids
//          Collection defendantIds = offenceValue.getDefendantIDs();
//          assertNotNull(defendantIds);
//          Iterator it3 = defendantIds.iterator();
//          while (it3.hasNext())
//          {
//            Integer defendantId = (Integer)it3.next();
//            log.debug("**** defendantId = " + defendantId);
//            if (!(defendantId.intValue() == 15) && !(defendantId.intValue() == 16))
//            {
//              fail();
//            }
//          }
//
//          // check the DefendantOnOffenceBasicValues
//          /* not this should really say complex */
//          HashMap dooBasicValues = offenceValue.getDefOnOffenceBasicValues();
//          assertTrue(dooBasicValues.containsKey(new Integer(15)));
//          assertTrue(dooBasicValues.containsKey(new Integer(16)));
//          DefendantOnOffenceBasicValue dooBasicValue1
//            = (DefendantOnOffenceBasicValue)(dooBasicValues.get(new Integer(15)));
//          assertEquals(10, dooBasicValue1.getId().intValue());
//          DefendantOnOffenceComplexValue dooBasicValue2
//            = (DefendantOnOffenceComplexValue)(dooBasicValues.get(new Integer(16)));
//          assertEquals(30, dooBasicValue2.getId().intValue());
//
//          /*
//
//           // WDF: original Results Now Stored in Disposal Table
//
//           assertTrue("original result wasn't found", dooBasicValue2.getOriginalResultComplexValue() != null);
//           assertTrue("disposal details wasn't populated", ((OriginalResultComplexValue)dooBasicValue2.getOriginalResultComplexValue()).getDisposalDetails().size() == 1);
//
//          */
//        }
//      }
//
//      // check the defendants on case
//      Collection allDefendants = chargeCompositeValueRet.getAllDefendants();
//      assertNotNull(allDefendants);
//      assertEquals(3, allDefendants.size());
//      Iterator allDefendantsIt = allDefendants.iterator();
//      while (allDefendantsIt.hasNext())
//      {
//        DefendantValue defVal = (DefendantValue)allDefendantsIt.next();
//        int defID = defVal.getDefendantID().intValue();
//        switch (defID)
//        {
//          case 14:
//            break;
//          case 15:
//            break;
//          case 16:
//            break;
//          default:
//            fail();
//        }
//      }
//  }
//
//  // updated this method to use iteration 2 test data
//  public void testGetBreach() throws Exception
//  {
//      BreachValue breachRet = controller.getBreachValue(new Integer(2));
//      assertNotNull(breachRet);
//      assertEquals(new Integer(2), breachRet.getBreachID());
//      assertEquals("B", breachRet.getBreachType());
//      assertEquals("B", breachRet.getBringBack());
//      assertEquals(new Integer(13), breachRet.getCaseID());
//      // Test datePut day then month then year
//      assertNull(breachRet.getDatePut()); // this is null in new test data
////      assertEquals(6, breachRet.getDatePut().get(Calendar.DAY_OF_MONTH));
////      assertEquals(6, (breachRet.getDatePut().get(Calendar.MONTH)+1)); // MONTH has vos 0-11
////      assertEquals(2002, breachRet.getDatePut().get(Calendar.YEAR));
//      assertEquals("21", breachRet.getHoCode());
//      assertEquals("Breach of Conditional Discharge", breachRet.getHoDescription());
//      assertEquals(81, breachRet.getOriginalCourtID().intValue());
//      assertEquals("C", breachRet.getOriginalCourtType());
//      assertEquals("case 8", breachRet.getOriginalSentence());
//      // Test originalSentenceDate day then month then year
//      assertNull(breachRet.getOriginalSentenceDate()); // this is null in new test data
////      assertEquals(6, breachRet.getOriginalSentenceDate().get(Calendar.DAY_OF_MONTH));
////      assertEquals(6, (breachRet.getOriginalSentenceDate().get(Calendar.MONTH)+1)); // MONTH has vos 0-11
////      assertEquals(2002, breachRet.getOriginalSentenceDate().get(Calendar.YEAR));
//  }
//
//  public void testLinkCountsAndDefendants() throws Exception
//  {
//      Vector DefendantOnOffenceValues = new Vector();
//
//      // link defendant 52 with offence 66
//      DefendantOnOffenceValue dofVal = new DefendantOnOffenceValue(new Integer(66), new Integer(52),"");
//      DefendantOnOffenceValues.add(dofVal);
//
//      LinkCountDefValue  linkCountDefValue = new LinkCountDefValue(new Integer(1),
//                                                                   new Integer(333),
//                                                                   DefendantOnOffenceValues,
//                                                                   true,
//                                                                   true,
//                                                                   "I");
//      controller.linkCountsAndDefendants(linkCountDefValue);
//
//      // now check the database to see that defendant has in fact been added to count
//      StringBuffer sqlString = new StringBuffer("select DEFENDANT_ON_OFFENCE_ID from XHB_DEFENDANT_ON_OFFENCE ");
//      sqlString.append("where  OFFENCE_ID = 66 ");
//      sqlString.append("and    DEFENDANT_ON_CASE_ID = ");
//      sqlString.append("(select DEFENDANT_ON_CASE_ID from XHB_DEFENDANT_ON_CASE where ");
//      sqlString.append("DEFENDANT_ID = 52 )");
//
//      ResultSet rs = TestUtils.execSql(sqlString.toString());
//      assertTrue(rs.next());
//  }
//
//
//  public void testAddDeleteChargeToCase() throws Exception
//  {
//      // test adding an indictment
//      // set up dates
//      Calendar prosPaperServedDate = Calendar.getInstance();
//      prosPaperServedDate.set(15,11,2002);
//      Calendar dateIndRec = Calendar.getInstance();
//      dateIndRec.set(10,12,2002);
//      Calendar indSignedDate = Calendar.getInstance();
//      indSignedDate.set(16,12,2002);
//      // set up the defendant IDs
//      Vector defendantIDs = new Vector();
//      defendantIDs.add(new Integer(51));
//      // set up the offence value
//      OffenceValue offenceValue = new OffenceValue(null,             // offenceID - will be set on insert
//                                                   null,             // chargeID - will be set on insert
//                                                   new Integer(901), // refOffenceID
//                                                   defendantIDs,
//                                                   null,             // crestOffenceFreeText - not set in XHIBIT
//                                                   null,             // crestOffenceID - will be set on insert
//                                                   null,             // crestOffenceSeqNo - will be set on insert
//                                                   null,             // multiple - don't know what this is
//                                                   "O");             // offenceDescription
//      Vector offenceValues = new Vector();
//      offenceValues.add(offenceValue);
//      ChargeValue chargeValue =  new ChargeValue(null,                   // chargeID - will be set on insert
//                                                 new Integer(1),         // caseID
//                                                 ChargeTypes.INDICTMENT, // chargeType
//                                                 null,                   // crestChargeID - will be set on insert
//                                                 null,                   // crestChargeSeqNo - will be set on insert
//                                                 prosPaperServedDate,
//                                                 null,                   // BreachValue - not required for indictment
//                                                 offenceValues,
//                                                 null,                   // defendantID - not required for indictment
//                                                 new Integer(333),       // courtID
//                                                 dateIndRec,
//                                                 indSignedDate,
//                                                 "Responsibility" );     // indictmentResponsibility
//
//      // find the existing charges
//      ChargeCompositeValue chargeCompVal = controller.getCharges(new Integer(1), true);
//      Collection charges = chargeCompVal.getCharges();
//      assertNotNull(charges);
//      int numCharges = charges.size();
//      Vector existingCharges = new Vector();
//      Iterator chargesIt = charges.iterator();
//      while (chargesIt.hasNext())
//      {
//        ChargeValue charge = (ChargeValue)chargesIt.next();
//        existingCharges.add(charge.getChargeID());
//      }
//
//      // add the new charge
//      controller.addChargeToCase(chargeValue);
//
//      // find the new charge
//      //ChargeCompositeValue chargeCompVal2 = controller.getCharges(new Integer(1));
//      //Collection charges2 = chargeCompVal.getCharges();
//      assertNotNull(charges);
//      int numCharges2 = charges.size();
//      // check a new charge has been added
//      assertEquals(numCharges2, ++numCharges);
//      Iterator chargesIt2 = charges.iterator();
//      boolean foundNewCharge = false;
//      Integer newChargeID = null;
//      Integer newCrestSeqNo = null;
//      Integer newCrestChargeID = null;
//      while (chargesIt2.hasNext())
//      {
//        ChargeValue charge = (ChargeValue)chargesIt.next();
//        if (!existingCharges.contains(charge.getChargeID()))
//        {
//          // this is the new charge
//          foundNewCharge = true;
//          newChargeID = charge.getChargeID();
//          newCrestSeqNo = charge.getCrestChargeSeqNo();
//          newCrestChargeID = charge.getCrestChargeID();
//          assertEquals(1, charge.getCaseID().intValue());
//          assertEquals(ChargeTypes.INDICTMENT, charge.getChargeType());
//          assertEquals(prosPaperServedDate, charge.getProsPaperServedDate());
//          assertEquals(indSignedDate, charge.getIndSignedDate());
//          assertEquals(dateIndRec, charge.getDateIndRec());
//          assertEquals("Responsibility", charge.getIndResp());
//          // check the offence
//          Collection offences = charge.getOffenceValues();
//          assertEquals(1, offences.size());
//          OffenceValue offenceVal = (OffenceValue)offences.iterator().next();
//          assertEquals(901, offenceValue.getOffenceID().intValue());
//          assertEquals("O", offenceValue.getOffenceDescription());
//          // check the defendant
//          Collection defendants = offenceVal.getDefendantIDs();
//          assertEquals(1, defendants.size());
//          assertEquals(51, ((Integer)defendants.iterator().next()).intValue());
//        }
//      }
//      assertTrue(foundNewCharge);
//
//      // now try to delete this charge
//      DelChargeValue delChargeVal = new DelChargeValue(newChargeID,
//                                                       new Integer(333),  // court ID
//                                                       new Integer(1),    // case ID
//                                                       newCrestSeqNo,
//                                                       newCrestChargeID,
//                                                       true);             // inCourt
//
//     controller.deleteCharge(delChargeVal);
//
//     ResultSet rs = TestUtils.execSql("select charge_id from xhb_charge where charge_id = " + newChargeID);
//     assertTrue(!rs.next());
//  }
//
//  public void testSignIndictment() throws Exception
//  {
//    StringBuffer sqlString = null;
//    //String prosPaperServedDate = null;
//    Calendar dateProsPaperServedDate = Calendar.getInstance();
//
//      // this test needs rewriting based on the new definition of the method
//      SignIndValue signIndValue = new SignIndValue(new Integer(31),   // chargeID
//                                                   null,              // numberOfDays
//                                                   new Integer(333),  // courtID
//                                                   new Integer(1),    // caseID
//                                                   Calendar.getInstance(), // indSignedDate
//                                                   true,              // inCourt
//                                                   false);            // signOutOfTime
//      // first test signing 'in time'
//      sqlString = new StringBuffer("select to_char(pros_paper_served_date, 'DD/MM/YYYY'), pros_paper_served_date from XHB_CHARGE ");
//      sqlString.append("where charge_id = 31");
//      ResultSet rs = TestUtils.execSql(sqlString.toString());
//      rs.next();
//      //prosPaperServedDate = rs.getString(1);
//      dateProsPaperServedDate.setTime(rs.getDate(2));
//
//      sqlString = new StringBuffer("update XHB_CHARGE set ");
//      sqlString.append("pros_paper_served_date = (sysdate-14) where charge_id = 31");
//      TestUtils.execSql(sqlString.toString());
//
//      sqlString = new StringBuffer("select pros_paper_served_date from xhb_charge ");
//      sqlString.append("where charge_id = 31");
//      ResultSet rs2 = TestUtils.execSql(sqlString.toString());
//      rs2.next();
//      dateProsPaperServedDate.setTime(rs2.getDate(1));
//
//      log.debug("\n*** prosPaperServedDate set to - 14 = " + dateProsPaperServedDate);
//
//      // sign
//      controller.signIndictment(signIndValue);
//
//      sqlString = new StringBuffer("select ind_signed_date from XHB_CHARGE ");
//      sqlString.append("where charge_id = 31");
//      ResultSet rs3 = TestUtils.execSql(sqlString.toString());
//      rs3.next();
//      Timestamp indSignedDate = rs3.getTimestamp("ind_signed_date");
//      Calendar indSignedCal = Calendar.getInstance();
//      indSignedCal.setTime(indSignedDate);
//      Calendar today = Calendar.getInstance();
//      assertEquals(today.get(Calendar.YEAR), indSignedCal.get(Calendar.YEAR));
//      assertEquals(today.get(Calendar.MONTH), indSignedCal.get(Calendar.MONTH));
//      assertEquals(today.get(Calendar.DAY_OF_MONTH), indSignedCal.get(Calendar.DAY_OF_MONTH));
//
//      // now try out of time, with permission
//      sqlString = new StringBuffer("update XHB_CHARGE set ");
//      sqlString.append("pros_paper_served_date = (sysdate - 29) where charge_id = 31");
//      TestUtils.execSql(sqlString.toString());
//
//      sqlString = new StringBuffer("select pros_paper_served_date from XHB_CHARGE ");
//      sqlString.append("where charge_id = 31");
//      ResultSet rs4 = TestUtils.execSql(sqlString.toString());
//      rs4.next();
//      dateProsPaperServedDate.setTime(rs4.getDate(1));
//      log.debug("\n*** dateProsPaperServedDate (-29) = " + dateProsPaperServedDate);
//
//      signIndValue.setSignOutOfTime(true);
//
//      // sign
//      controller.signIndictment(signIndValue);
//
//      // now try really out of time, with permission
//      sqlString = new StringBuffer("update XHB_CHARGE set ");
//      sqlString.append("pros_paper_served_date = (sysdate - 85) where charge_id = 31");
//      TestUtils.execSql(sqlString.toString());
//
//      sqlString = new StringBuffer("select pros_paper_served_date from XHB_CHARGE ");
//      sqlString.append("where charge_id = 31");
//      ResultSet rs5 = TestUtils.execSql(sqlString.toString());
//      rs5.next();
//      dateProsPaperServedDate.setTime(rs5.getDate(1));
//      log.debug("\n*** dateProsPaperServedDate (- 85) = " + dateProsPaperServedDate);
//
//      signIndValue.setSignOutOfTime(true);
//
//      // sign
//      controller.signIndictment(signIndValue);
//
//      // now try out of time, without permission
//      boolean outOfTimeException = false;
//      signIndValue.setSignOutOfTime(false);
//      try
//      {
//        // sign
//        controller.signIndictment(signIndValue);
//      }
//      catch(OutOfTimeException e)
//      {
//        outOfTimeException = true;
//      }
//      assertTrue(outOfTimeException);
//  }
//
//  /**public void testCheckResultsForCharge()
//  {
//    try
//    {
//      Integer chargeID = new Integer(1)  /** @todo fill in 'real' value ;
//      Boolean booleanRet = delegate.checkResultsForCharge(chargeID);
//      /** @todo:  Insert test code here.  Use assertEquals(), for example.
//      fail();
//    }
//    catch (ChargeControllerException e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }*/
//
//  /**public void testCheckResultsForOffence()
//  {
//    try
//    {
//      Integer offencCountID = new Integer(1)  /** @todo fill in 'real' value ;
//      Boolean booleanRet = delegate.checkResultsForOffence(offencCountID);
//      /** @todo:  Insert test code here.  Use assertEquals(), for example.
//      fail();
//    }
//    catch (ChargeControllerException e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }*/
//
//  public void testAddDeleteOffence() throws Exception
//  {
//      Integer newOffenceID;
//      // set up the defendant IDs
//      Vector defendantIDs = new Vector();
//      defendantIDs.add(new Integer(51));
//      // set up the offence value
//      OffenceValue offenceValue = new OffenceValue(null,             // offenceID - will be set on insert
//                                                   new Integer(31),  // chargeID
//                                                   new Integer(901), // refOffenceID
//                                                   defendantIDs,
//                                                   null,             // crestOffenceFreeText - not set in XHIBIT
//                                                   null,             // crestOffenceID - will be set on insert
//                                                   null,             // crestOffenceSeqNo - will be set on insert
//                                                   null,             // multiple - don't know what this is
//                                                   "O");             // offenceDescription
//
//      // find the existing offences
//      StringBuffer sqlString = new StringBuffer("select offence_id from xhb_offence");
//      Vector existingOffences = new Vector();
//      ResultSet rs = TestUtils.execSql(sqlString.toString());
//      boolean foundNewOffence = false;
//      while (rs.next())
//      {
//        existingOffences.add(new Integer(rs.getInt(1)));
//      }
//
//      controller.addOffence(offenceValue);
//
//      // find the new offence
//      ResultSet rs2 = TestUtils.execSql(sqlString.toString());
//      while (rs2.next())
//      {
//        if (!existingOffences.contains(new Integer(rs2.getInt(1))))
//        {
//          // this is the new offence
//          foundNewOffence = true;
//          // get the details to check
//          sqlString = new StringBuffer("select xhb_offence.ref_offence_id, " +
//                                       "       xhb_ref_offence.offence_desc, " +
//                                       "       xhb_defendant_on_case.defendant_id, " +
//                                       "       xhb_offence.offence_id " +
//                                       "from xhb_offence, " +
//                                       "     xhb_ref_offence, " +
//                                       "     xhb_defendant_on_case, " +
//                                       "     xhb_defendant_on_offence " +
//                                       "where xhb_offence.offence_id = " + rs.getInt(1) +
//                                       "and   xhb_defendant_on_offence.defendant_on_case_id = xhb_defendant_on_case.defendant_on_case_id " +
//                                       "and   xhb_defendant_on_case.defendant_id = 51");
//          ResultSet rs3 = TestUtils.execSql(sqlString.toString());
//          rs3.next();
//          assertEquals(901,rs.getInt(1));
//          assertEquals("O",rs.getString(2));
//          assertEquals(51,rs.getInt(3));
//          newOffenceID = new Integer(rs.getInt(4));
//
//          // now try to delete this offence
//          DelOffenceValue delOffenceVal = new DelOffenceValue(newOffenceID,
//                                                              new Integer(333),    // courtID
//                                                              new Integer(1),      // caseID
//                                                              new Integer(31),
//                                                              defendantIDs,        // inCourt
//                                                              true);
//          //delegate.deleteOffence(delOffenceVal);
//          ResultSet rs4 = TestUtils.execSql("select offence_id from xhb_offence where offence_id = " + newOffenceID);
//          assertTrue(!rs.next());
//       }
//     }
//     assertTrue(foundNewOffence);
// }
//
//  public void testUpdateBreach() throws Exception
//  {
//      BreachValue breachRet = controller.getBreachValue(new Integer(381));
//      BreachValue breachSave = controller.getBreachValue(new Integer(381));
//      // the vos the client can update are: ho code and description,
//      // original sentence and date, original court, committal for breach,
//      // bring back and date put
//      breachRet.setBringBack("Y");
//      Calendar date = Calendar.getInstance();
//      date.set(15,10,2002);
//      breachRet.setDatePut(date);
//      breachRet.setHoCode("Ho Code");
//      breachRet.setHoDescription("Ho Description");
//      breachRet.setOriginalCourtID(new Integer(9614));
//      breachRet.setOriginalCourtType("CM");
//      breachRet.setOriginalSentence("Original Sent");
//      date.set(11,01,2002);
//      breachRet.setOriginalSentenceDate(date);
//
//      controller.updateBreach(breachRet);
//
//      // get the updated breach
//      BreachValue updatedBreach = controller.getBreachValue(new Integer(381));
//
//      // now check the data base to see that vos have been updated
//
//      assertEquals(breachRet.getBreachType(),           updatedBreach.getBreachType()          );
//      assertEquals(breachRet.getBringBack(),            updatedBreach.getBringBack()           );
//      assertEquals(breachRet.getDatePut(),              updatedBreach.getDatePut()             );
//      assertEquals(breachRet.getHoCode(),               updatedBreach.getHoCode()              );
//      assertEquals(breachRet.getHoDescription(),        updatedBreach.getHoDescription()       );
//      assertEquals(breachRet.getOriginalCourtType(),    updatedBreach.getOriginalCourtType()   );
//      assertEquals(breachRet.getOriginalSentence(),     updatedBreach.getOriginalSentence()    );
//      assertEquals(breachRet.getOriginalSentenceDate(), updatedBreach.getOriginalSentenceDate());
//
//      // now change vos back to original state before update
//      controller.updateBreach(breachSave);
//  }
//
//  public void testUpdateOffence() throws Exception
//  {
//      // set up the defendant IDs
//      Vector defendantIDs = new Vector();
//      defendantIDs.add(new Integer(51));
//
//      // set up the offence value
//      // the only thing the client should be updating on an offence is
//      // the offence code
//      OffenceValue offenceValue = new OffenceValue(new Integer(66),   // offenceID
//                                                   new Integer(31),   // chargeID
//                                                   new Integer(902),  // refOffenceID (was 901)
//                                                   defendantIDs,
//                                                   "C",               // crestOffenceFreeText
//                                                   new Integer(601),  // crestOffenceID
//                                                   new Integer(6057), // crestOffenceSeqNo
//                                                   new Integer(1),    // multiple
//                                                   "O");              // offenceDescription
//      controller.updateOffence(offenceValue);
//
//      // get the details to check
//      StringBuffer sqlString = new StringBuffer("select ref_offence_id " +
//                                                "from xhb_offence " +
//                                                "where offence_id = 66");
//      ResultSet rs = TestUtils.execSql(sqlString.toString());
//      rs.next();
//      assertEquals(902, rs.getInt(1));
//
//      // reset the details
//      offenceValue.setRefOffenceID(new Integer(901));
//
//      controller.updateOffence(offenceValue);
//  }
//}
//