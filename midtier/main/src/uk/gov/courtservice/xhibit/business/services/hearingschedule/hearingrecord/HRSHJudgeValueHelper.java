package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudge;
import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHJudgeValue;

/**
 * <p>
 * Title: HRSHJudgeValueHelper
 * </p>
 * <p>
 * Description: This will transform HRSHJudgeValues to/from SHJudgeBasicValue
 * and SHJudgeComplexValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class HRSHJudgeValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRSHJudgeValueHelper.class);

    private ShJudgeMaintainer maintainer = null;

    public HRSHJudgeValueHelper() {
        maintainer = new ShJudgeMaintainer();
    }

    /**
     * This will build a SHJudgeBasicValue from a HRSHJudgeValue
     * 
     * @param hrSHJudgeValue
     *            HRSHJudgeValue
     * @return SHJudgeBasicValue
     */
    public SHJudgeBasicValue buildBasicValue(HRSHJudgeValue hrSHJudgeValue) {
        log.debug("HRSHJudgeValueHelper.buildBasicValue(HRSHJudgeValue hrSHJudgeValue) called");
        log.debug(">>>>>>>>>>>>>> HRSHJudgeValue before : " + hrSHJudgeValue.toString());

        SHJudgeBasicValue basicValue = new SHJudgeBasicValue(hrSHJudgeValue.getId(), hrSHJudgeValue.getVersion());
        basicValue.setDeputyHCJ(hrSHJudgeValue.getDeputyHCJ());
        basicValue.setRefJudgeID(hrSHJudgeValue.getRefJudgeID());
        basicValue.setShAttendeeID(hrSHJudgeValue.getSHAttendeeID());

        log.debug(">>>>>>>>>>>>>> SHJudgeBasicValue after : " + basicValue.toString());
        log.debug("HRSHJudgeValueHelper.buildBasicValue(HRSHJudgeValue hrSHJudgeValue) finsihed");
        return basicValue;
    }

    /**
     * This will build a HRSHJudgeValue from a SHJudgeBasicValue
     * 
     * @param basicValue
     *            SHJudgeBasicValue
     * @return HRSHJudgeValue
     */
    public HRSHJudgeValue buildHRSHJudgeValueFromBasic(SHJudgeBasicValue basicValue) {
        log.debug("HRSHJudgeValueHelper.buildHRSHJudgeValueFromBasic(SHJudgeBasicValue basicValue) called");
        log.debug(">>>>>>>>>>>>>> SHJudgeBasicValue before : " + basicValue.toString());

        HRSHJudgeValue hrShJudgeValue = new HRSHJudgeValue(basicValue.getId(), basicValue.getVersion());

        hrShJudgeValue.setDeputyHCJ(basicValue.getDeputyHCJ());
        hrShJudgeValue.setRefJudgeID(basicValue.getRefJudgeID());
        hrShJudgeValue.setSHAttendeeID(basicValue.getShAttendeeID());

        log.debug(">>>>>>>>>>>>>> HRSHJudgeValue after : " + hrShJudgeValue.toString());
        log.debug("HRSHJudgeValueHelper.buildHRSHJudgeValueFromBasic(SHJudgeBasicValue basicValue) finsihed");
        return hrShJudgeValue;
    }

    /**
     * This will build a HRSHJudgeValue from a SHJudgeComplexValue
     * 
     * @param complexValue
     *            SHJudgeComplexValue
     * @return HRSHJudgeValue
     */
    public HRSHJudgeValue buildHRSHJudgeValueFromComplex(SHJudgeComplexValue complexValue) {
        log.debug("HRSHJudgeValueHelper.buildHRSHJudgeValueFromComplex(SHJudgeComplexValue complexValue) called");
        log.debug(">>>>>>>>>>>>>> SHJudgeComplexValue before : " + complexValue.toString());
        HRSHJudgeValue hrShJudgeValue = new HRSHJudgeValue(complexValue.getId(), complexValue.getVersion());

        hrShJudgeValue.setDeputyHCJ(complexValue.getDeputyHCJ());
        hrShJudgeValue.setRefJudgeID(complexValue.getRefJudgeID());
        hrShJudgeValue.setSHAttendeeID(complexValue.getShAttendeeID());

        log.debug(">>>>>>>>>>>>>> HRSHJudgeValue after : " + hrShJudgeValue.toString());
        log.debug("HRSHJudgeValueHelper.buildHRSHJudgeValueFromComplex(SHJudgeComplexValue complexValue) finsihed");
        return hrShJudgeValue;
    }

    /**
     * This method will find a judge and transform it to a HRSHJudgeValue. It
     * will search for the scheuduled hearing Attednee id.
     * 
     * @param shAttJudgeID
     *            Integer
     * @return HRSHJudgeValue
     * @throws HearingRecordException
     */
    public HRSHJudgeValue getHRJudgeValue(Integer shAttJudgeID) throws HearingRecordException {
        log.debug("HRSHJudgeValueHelper.getHRJudgeValue(Integer shJudgeID) called");

        HRSHJudgeValue hrValue = null;

        SHJudgeBasicValue basicValue = this.getJudgeBasicValue(shAttJudgeID);
        hrValue = this.buildHRSHJudgeValueFromBasic(basicValue);

        log.debug("HRSHJudgeValueHelper.getHRJudgeValuelue(Integer shJudgeID) finished");
        return hrValue;
    }

    // -------------------------------- Private methods
    // -------------------------//

    /**
     * This will find the Judge from the database via the primary key.
     * 
     * @param shJudgeID
     *            Integer
     * @return ShJudge
     * @throws HearingRecordException
     */
    private ShJudge findJudge(Integer shJudgeID) throws HearingRecordException {
        log.debug("HRSHJudgeValueHelper.findJudge(Integer shJudgeID)called");

        ShJudge judge = null;
        try {
            judge = maintainer.findByShAttendeeId(shJudgeID);
        } catch (ObjectNotFoundException ex) {
            // this is not an error as such since this is expected and we
            // have to create
            // a new one.
            // CSServices.getDefaultErrorHandler().handleError(ex,
            // getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
            throw hex;
        }
        log.debug("HRSHJudgeValueHelper.findJudge(Integer shJudgeID) finsihed");
        return judge;
    }

    /**
     * This will find the ShJudge from the database
     * 
     * @param shJudgeID
     *            Integer
     * @return SHJudgeBasicValue
     * @throws HearingRecordException
     */
    private SHJudgeBasicValue getJudgeBasicValue(Integer shJudgeID) throws HearingRecordException {
        log.debug("HRSHJudgeValueHelper.getJudgeBasicValue(Integer shJudgeID) called");
        SHJudgeBasicValue value = null;
        ShJudge judge = this.findJudge(shJudgeID);
        value = maintainer.getShJudgeBasicValue(judge);
        log.debug("HRSHJudgeValueHelper.getJudgeBasicValue(Integer shJudgeID) finsihed");
        return value;
    }
}