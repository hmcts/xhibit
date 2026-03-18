package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Result;

/**
 * <p/> Title: Utility class for populating Charge Information in a Crest Form
 * Schema.
 * </p>
 * <p/> Description: Charge information population + verdicts + pleas
 * </p>
 * <p/> This class populates castor bound xml objects from entity beans.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */
public class CrestFormBFMiscAppealChargeHelper2 extends CrestFormBFChargeHelper2 {
    // set up logger
    protected static Logger log = CSServices.getLogger(CrestFormBFMiscAppealChargeHelper2.class);

    public static uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.MiscAppealCharges populateMiscAppealCharges(
            uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.MiscAppealCharges charges, XhbDefendantOnCase doc,
            String chargeType, Integer scheduledHearingID) throws CrestFormBFXMLException {

        log.debug("******************* CREST FORMS BF MISC APPEAL CHARGE HELPER ENTERED *********************** ");

        Integer caseId = doc.getXhbCase().getCaseId();
        log.debug("Case ID : " + caseId);
        log.debug("Scheduled Hearing ID :: " + scheduledHearingID);

        ResultsCompositeValue rcv = null;
        try {
            rcv = Results2WorkFlow.getResults(caseId, scheduledHearingID);
            VerdictValue v = rcv.getCaseVerdict(caseId);

            if (charges == null) {
                charges = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.MiscAppealCharges();
            }

            Result resultInfo = new Result(); // set up castor object
            // check to ensure that there is a verdict to pick up result
            // information
            if (v != null) {
                String verdictCode = v.getRefVerdictCode();
                String verdictDesc = v.getRefVerdictDesc();

                if (verdictCode != null && verdictDesc != null) {
                    resultInfo.setResult(verdictCode + " - " + verdictDesc);
                }

                java.util.Date verdictDate = v.getVerdictDate();
                log.debug("verdict date :: " + verdictDate);
                if (verdictDate != null) {
                    resultInfo.setResultDate(new org.exolab.castor.types.Date(verdictDate));
                }

                java.util.Date hearingDate = v.getHearingDate();
                log.debug("hearing date :: " + hearingDate);
                if (hearingDate != null) {
                    resultInfo.setHearingDate(new org.exolab.castor.types.Date(hearingDate));
                }

                Long duration = v.getLastCalculatedDuration();
                log.debug("duration :: " + duration);
                if (duration != null) {
                    resultInfo.setHearingDuration(getDuration(duration.longValue()));
                }

                String transCourtCode = v.getCccTransToRefCourtCode();
                String transCourtDesc = v.getCccTransToRefCourtDesc();
                if (transCourtCode != null && transCourtDesc != null) {
                    resultInfo.setTransferCourt(transCourtCode + " - " + transCourtDesc);
                }
            }
            // set up the result info
            charges.setResult(resultInfo);

        } catch (ResultsControllerException e) {
            e.printStackTrace();
        }
        return charges;
    }

    /**
     * Util method to convert duration from milliseconds to hours and minutes
     * 
     * @param duration
     *            in milliseconds
     */
    private static String getDuration(long duration) {
        long durationMinutes = duration / (60 * 1000);
        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;
        StringBuffer durationText = new StringBuffer();
        durationText.append(hours);
        durationText.append(" hour(s) ");
        durationText.append(minutes);
        durationText.append(" minute(s) ");
        return durationText.toString();
    }
}