package uk.gov.courtservice.xhibit.test.business.entities.defhearingrecord;

import junit.framework.*;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import java.util.Date;



public class DefHearingRecordValueTest extends TestCase
{
    private Logger log =  CSServices.getLogger(DefHearingRecordValueTest.class);

    public DefHearingRecordValueTest(String s)
    {
        super(s);
    }

    protected void setUp()
    {
    }

    protected void tearDown()
    {
    }

    public void testBasic()
    {
        try
        {
            Integer hearingRecordID = new Integer(1);
            Integer refAdjournmentID = new Integer(1);
            Integer collectMagistrateCourtID = new Integer(1);
            Integer defendantOnCaseID = new Integer(1);
            Integer hearingID = new Integer(1);
            Integer refDefHearingTypeID = new Integer(1);
            Integer version = new Integer(1);
            Date adjournedDate = new Date();
            Date dateOfCommittal = new Date();
            Date hearingEndDate = new Date();
            Date hearingStartDate = new Date();
            Date startDateNewBailStatus = new Date();
            Date dateBailApplication = new Date();
            String substBailApplication = "substBailApplication";
            String oralEvidence = "oralEvidence";
            String newBailStatus = "newBailStatus";
            String hearingDatesFreetext = "hearingDatesFreetext";
            String hearingRecordStatus = "hearingRecordStatus";
            String endBailStatus = "endBailStatus";
            String startBailStatus = "startBailStatus";
            String resultBailApplication = "resultBailApplication";
            String isAdjourned = "Y";
            String isHRAApplication = "N";

            DefHearingRecordBasicValue basic = new DefHearingRecordBasicValue(hearingRecordID, version);

            basic.setRefAdjournmentID(refAdjournmentID);
            basic.setResultBailApplication(resultBailApplication);
            basic.setDefendantOnCaseID(defendantOnCaseID);
            basic.setHearingID(hearingID);
            basic.setRefDefHearingTypeID(refDefHearingTypeID);
            basic.setAdjournedDate(adjournedDate);
            //basic.setDateOfCommittal(dateOfCommittal);
            //basic.setHearingEndDate(hearingEndDate);
            //basic.setHearingStartDate(hearingStartDate);
            basic.setStartDateNewBailStatus(startDateNewBailStatus);
            basic.setDateBailApplication(dateBailApplication);
            basic.setSubstBailApplication(substBailApplication);
            basic.setOralEvidence(oralEvidence);
            basic.setNewBailStatus(newBailStatus);
            //basic.setHearingDatesFreetext(hearingDatesFreetext);
            //basic.setHearingRecordStatus(hearingRecordStatus);
            basic.setEndBailStatus(endBailStatus);
            basic.setStartBailStatus(startBailStatus);
            basic.setIsAdjourned(isAdjourned);
            basic.setIsHraApplication(isHRAApplication);

            log.debug("hearingRecordID");
            assertEquals(hearingRecordID, basic.getId());
            log.debug("refAdjournmentID");
            assertEquals(refAdjournmentID, basic.getRefAdjournmentID());
            log.debug("resultBailApplication");
            assertEquals(resultBailApplication, basic.getResultBailApplication());
            log.debug("defendantOnCaseID");
            assertEquals(defendantOnCaseID, basic.getDefendantOnCaseID());
            log.debug("hearingID");
            assertEquals(hearingID, basic.getHearingID());
            log.debug("refDefHearingTypeID");
            assertEquals(refDefHearingTypeID, basic.getRefDefHearingTypeID());
            log.debug("adjournedDate");
            assertEquals(adjournedDate, basic.getAdjournedDate());
            //log.debug("dateOfCommittal");
            //assertEquals(dateOfCommittal, basic.getDateOfCommittal());
            log.debug("startDateNewBailStatus");
            assertEquals(startDateNewBailStatus, basic.getStartDateNewBailStatus());
            log.debug("dateBailApplication");
            assertEquals(dateBailApplication, basic.getDateBailApplication());
            log.debug("substBailApplication");
            assertEquals(substBailApplication, basic.getSubstBailApplication());
            log.debug("oralEvidence");
            assertEquals(oralEvidence, basic.getOralEvidence());
            log.debug("newBailStatus");
            assertEquals(newBailStatus, basic.getNewBailStatus());
            log.debug("endBailStatus");
            assertEquals(endBailStatus, basic.getEndBailStatus());
            log.debug("startBailStatus");
            assertEquals(startBailStatus, basic.getStartBailStatus());
            log.debug("isAdjourned");
            assertEquals(isAdjourned, basic.getIsAdjourned());
            log.debug("isHRAApplication");
            assertEquals(isHRAApplication, basic.getIsHraApplication());
            log.debug("version");
            assertEquals(version, basic.getVersion());
        }
        catch(Exception e)
        {
            log.debug("testBasic() is failed");
            e.printStackTrace();
            fail();
        }
    }
}