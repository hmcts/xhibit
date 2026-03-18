//package uk.gov.courtservice.xhibit.test.business.entities.defhearingrecord;
//
//import java.util.Date;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordComplexValue;
//
//
///**
// *
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Khanh Tran
// * @version $Id: DefHearingRecordMaintainerTest.java,v 1.9 2006/07/11 14:16:55 xzfdtb Exp $
// */
//
//public class DefHearingRecordMaintainerTest extends TransactionTestCase
//{
//    private static Logger log = CSServices.getLogger(DefHearingRecordMaintainerTest.class);
//
//    public DefHearingRecordMaintainerTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//
//    protected void setUp()
//    {
//        try
//        {
//            TestUtils.execSql("delete from xhb_def_hearing_record");
//        }
//        catch(Exception e)
//        {
//            log.debug ("setUp Error: " + e.toString());
//        }
//    }
//
//
//    public void testCreate()
//    {
//        try
//        {
//            DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//            DefHearingRecordBasicValue actual = createBasicVO();
//
//            DefHearingRecord entity = (DefHearingRecord)maintainer.create(actual);
//
//            log.debug ("defendantOnCaseId");
//            assertEquals(actual.getDefendantOnCaseID(), entity.getDefendantOnCaseId());
//            log.debug ("refAdjournmentId");
//            assertEquals(actual.getRefAdjournmentID(), entity.getRefAdjournmentId());
//            log.debug ("refDefHearingTypeId");
//            assertEquals(actual.getRefDefHearingTypeID(), entity.getRefDefHearingTypeId());
//            log.debug ("endBailStatus");
//            assertEquals(actual.getEndBailStatus(), entity.getEndBailStatus());
//            log.debug ("hearingDateFreeTxt1");
//            assertEquals(actual.getHearingDateFreetext1(), entity.getHearingDateFreeTxt1());
//            log.debug ("hearingDateFreeTxt2");
//            assertEquals(actual.getHearingDateFreetext2(), entity.getHearingDateFreeTxt2());
//            log.debug ("hearingDateFreeTxt3");
//            assertEquals(actual.getHearingDateFreetext3(), entity.getHearingDateFreeTxt3());
//            log.debug ("isAdjourned");
//            assertEquals(actual.getIsAdjourned(), entity.getIsAdjourned());
//            log.debug ("isHraApplication");
//            assertEquals(actual.getIsHraApplication(), entity.getIsHraApplication());
//            log.debug ("newBailStatus");
//            assertEquals(actual.getNewBailStatus(), entity.getNewBailStatus());
//            log.debug ("oralEvidence");
//            assertEquals(actual.getOralEvidence(), entity.getOralEvidence());
//            log.debug ("resultBailApplication");
//            assertEquals(actual.getResultBailApplication(), entity.getResultBailApplication());
//            log.debug ("startBailStatus");
//            assertEquals(actual.getStartBailStatus(), entity.getStartBailStatus());
//            log.debug ("substBailApplication");
//            assertEquals(actual.getSubstBailApplication(), entity.getSubstBailApplication());
//            log.debug ("adjournedDate");
//            assertEquals(actual.getAdjournedDate().getTime(), entity.getAdjournedDate().getTime());
//            log.debug ("dateBailApplication");
//            assertEquals(actual.getDateBailApplication().getTime(), entity.getDateBailApplication().getTime());
//            log.debug ("dateOfCommittal");
//    //        assertEquals(actual.getDateOfCommittal().getTime(), entity.getDateOfCommittal().getTime());
//            log.debug ("startDateNewBailStatus");
//            assertEquals(actual.getStartDateNewBailStatus().getTime(), entity.getStartDateNewBailStatus().getTime());
//
//        }
//        catch(Exception e)
//        {
//            log.debug("testCreate() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testDelete() {
//
//        try
//        {
//            DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//            DefHearingRecordBasicValue actual = createBasicVO();
//
//            DefHearingRecord entity = (DefHearingRecord)maintainer.create(actual);
//
//            Integer id = entity.getHearingRecordId();
//            Integer version = entity.getVersion();
//
//            log.debug("hearingRecordId: " + id + "    version: " + version);
//            log.debug("version: " + version);
//
//            maintainer.delete(id, version);
//            assertTrue(true);
//        }
//        catch(Exception e)
//        {
//            log.debug("testDelete() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testGetDefHearingRecordBasicValue()
//    {
//        try
//        {
//            DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//            DefHearingRecordBasicValue actual = createBasicVO();
//
//            DefHearingRecord entity = (DefHearingRecord)maintainer.create(actual);
//
//            DefHearingRecordBasicValue expected = maintainer.getDefHearingRecordBasicValue(entity);
//
//            log.debug ("defendantOnCaseId");
//            assertEquals(actual.getDefendantOnCaseID(), expected.getDefendantOnCaseID());
//            log.debug ("refAdjournmentId");
//            assertEquals(actual.getRefAdjournmentID(), expected.getRefAdjournmentID());
//            log.debug ("refDefHearingTypeId");
//            assertEquals(actual.getRefDefHearingTypeID(), expected.getRefDefHearingTypeID());
//            log.debug ("endBailStatus");
//            assertEquals(actual.getEndBailStatus(), expected.getEndBailStatus());
//            log.debug ("hearingDateFreeTxt1");
//            assertEquals(actual.getHearingDateFreetext1(), expected.getHearingDateFreetext1());
//            log.debug ("hearingDateFreeTxt2");
//            assertEquals(actual.getHearingDateFreetext2(), expected.getHearingDateFreetext2());
//            log.debug ("hearingDateFreeTxt3");
//            assertEquals(actual.getHearingDateFreetext3(), expected.getHearingDateFreetext3());
//            log.debug ("isAdjourned");
//            assertEquals(actual.getIsAdjourned(), expected.getIsAdjourned());
//            log.debug ("isHraApplication");
//            assertEquals(actual.getIsHraApplication(), expected.getIsHraApplication());
//            log.debug ("newBailStatus");
//            assertEquals(actual.getNewBailStatus(), expected.getNewBailStatus());
//            log.debug ("oralEvidence");
//            assertEquals(actual.getOralEvidence(), expected.getOralEvidence());
//            log.debug ("resultBailApplication");
//            assertEquals(actual.getResultBailApplication(), expected.getResultBailApplication());
//            log.debug ("startBailStatus");
//            assertEquals(actual.getStartBailStatus(), expected.getStartBailStatus());
//            log.debug ("substBailApplication");
//            assertEquals(actual.getSubstBailApplication(), expected.getSubstBailApplication());
//            log.debug ("adjournedDate");
//            assertEquals(actual.getAdjournedDate().getTime(), expected.getAdjournedDate().getTime());
//            log.debug ("dateBailApplication");
//            assertEquals(actual.getDateBailApplication().getTime(), expected.getDateBailApplication().getTime());
//            //log.debug ("dateOfCommittal");
//            //assertEquals(actual.getDateOfCommittal().getTime(), expected.getDateOfCommittal().getTime());
//            log.debug ("startDateNewBailStatus");
//            assertEquals(actual.getStartDateNewBailStatus().getTime(), expected.getStartDateNewBailStatus().getTime());
//
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetDefHearingRecordBasicValue() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testGetDefHearingComplexBasicValue()
//    {
//        try
//        {
//            DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//            DefHearingRecordBasicValue actual = createBasicVO();
//
//            DefHearingRecord entity = (DefHearingRecord)maintainer.create(actual);
//
//            DefHearingRecordComplexValue expected = maintainer.getDefHearingRecordComplexValue(entity);
//            log.debug ("defendantOnCaseId");
//            assertEquals(actual.getDefendantOnCaseID(), expected.getDefendantOnCaseID());
//            log.debug ("refAdjournmentId");
//            assertEquals(actual.getRefAdjournmentID(), expected.getRefAdjournmentID());
//            log.debug ("refDefHearingTypeId");
//            assertEquals(actual.getRefDefHearingTypeID(), expected.getRefDefHearingTypeID());
//            log.debug ("endBailStatus");
//            assertEquals(actual.getEndBailStatus(), expected.getEndBailStatus());
//            log.debug ("hearingDateFreeTxt1");
//            assertEquals(actual.getHearingDateFreetext1(), expected.getHearingDateFreetext1());
//            log.debug ("hearingDateFreeTxt2");
//            assertEquals(actual.getHearingDateFreetext2(), expected.getHearingDateFreetext2());
//            log.debug ("hearingDateFreeTxt3");
//            assertEquals(actual.getHearingDateFreetext3(), expected.getHearingDateFreetext3());
//            log.debug ("isAdjourned");
//            assertEquals(actual.getIsAdjourned(), expected.getIsAdjourned());
//            log.debug ("isHraApplication");
//            assertEquals(actual.getIsHraApplication(), expected.getIsHraApplication());
//            log.debug ("newBailStatus");
//            assertEquals(actual.getNewBailStatus(), expected.getNewBailStatus());
//            log.debug ("oralEvidence");
//            assertEquals(actual.getOralEvidence(), expected.getOralEvidence());
//            log.debug ("resultBailApplication");
//            assertEquals(actual.getResultBailApplication(), expected.getResultBailApplication());
//            log.debug ("startBailStatus");
//            assertEquals(actual.getStartBailStatus(), expected.getStartBailStatus());
//            log.debug ("substBailApplication");
//            assertEquals(actual.getSubstBailApplication(), expected.getSubstBailApplication());
//            log.debug ("adjournedDate");
//            assertEquals(actual.getAdjournedDate().getTime(), expected.getAdjournedDate().getTime());
//            log.debug ("dateBailApplication");
//            assertEquals(actual.getDateBailApplication().getTime(), expected.getDateBailApplication().getTime());
//            //log.debug ("dateOfCommittal");
//            //assertEquals(actual.getDateOfCommittal().getTime(), expected.getDateOfCommittal().getTime());
//            log.debug ("startDateNewBailStatus");
//            assertEquals(actual.getStartDateNewBailStatus().getTime(), expected.getStartDateNewBailStatus().getTime());
//
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetDefHearingRecordComplexValue() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testUpdate()
//    {
//        try
//        {
//            DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//            DefHearingRecordBasicValue actual = createBasicVO();
//
//            DefHearingRecord entity = (DefHearingRecord)maintainer.create(actual);
//
//            DefHearingRecordBasicValue update = maintainer.getDefHearingRecordBasicValue(entity);
//            update.setHearingDateFreetext1("Hearing_Date_Freetext1");
//            update.setHearingDateFreetext1("Hearing_Date_Freetext2");
//            update.setHearingDateFreetext1("Hearing_Date_Freetext3");
//            maintainer.update(update);
//
//            DefHearingRecordBasicValue expected = maintainer.getDefHearingRecordBasicValue(entity);
//            log.debug ("hearingDateFreeTxt1");
//            assertEquals(update.getHearingDateFreetext1(), expected.getHearingDateFreetext1());
//            log.debug ("hearingDateFreeTxt2");
//            assertEquals(update.getHearingDateFreetext2(), expected.getHearingDateFreetext2());
//            log.debug ("hearingDateFreeTxt3");
//            assertEquals(update.getHearingDateFreetext3(), expected.getHearingDateFreetext3());
//        }
//        catch(Exception e)
//        {
//            log.debug("testUpdate() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    private DefHearingRecordBasicValue createBasicVO()
//    {
//        DefHearingRecordBasicValue bv = new DefHearingRecordBasicValue();
//        // As the database does not record time in millisecond...
//        long currentTime = (System.currentTimeMillis()/1000)*1000;
//
//        bv.setAdjournedDate(new Date(currentTime));
//        bv.setDateBailApplication(new Date(currentTime));
//        //bv.setDateOfCommittal(new Date(currentTime));
//        bv.setStartDateNewBailStatus(new Date(currentTime));
//        bv.setDefendantOnCaseID(new Integer(1));
//        bv.setHearingID(new Integer(1));
//        bv.setRefAdjournmentID(new Integer(1));
//        bv.setRefDefHearingTypeID(new Integer(1));
//        bv.setEndBailStatus("e");
//        bv.setHearingDateFreetext1("hearingDateFreetext1");
//        bv.setHearingDateFreetext2("hearingDateFreetext2");
//        bv.setHearingDateFreetext3("hearingDateFreetext3");
//        bv.setIsAdjourned("a");
//        bv.setIsHraApplication("h");
//        bv.setNewBailStatus("n");
//        bv.setOralEvidence("o");
//        bv.setResultBailApplication("r");
//        bv.setStartBailStatus("s");
//        bv.setSubstBailApplication("s");
//
//        return bv;
//    }
//}