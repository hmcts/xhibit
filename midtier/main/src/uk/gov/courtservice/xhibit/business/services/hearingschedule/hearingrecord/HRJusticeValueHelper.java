package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
//JDK
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJusticeMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJusticeValue;

/**
 * <p>
 * Title: HRJusticeValueHelper
 * </p>
 * <p>
 * Description: This will search for justices for a hearing and transform them
 * to HRJusticeValues.
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
public class HRJusticeValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRJusticeValueHelper.class);

    private ShJusticeMaintainer maintainer;

    /**
     * Default constructor that will instantiate a ShJusticeMaintainer.
     */
    public HRJusticeValueHelper() {
        maintainer = new ShJusticeMaintainer();
    }

    /**
     * Method to find a justice and transform it to HRJusticeValue. It takes a
     * shJusticeID as an argument which is the primary key.
     * 
     * @param shJusticeID
     *            Integer
     * @return HRJusticeValue
     * @throws HearingRecordException
     */
    public HRJusticeValue getHrJusticeValue(Integer shJusticeID) throws HearingRecordException {
        log.debug("HRJusticeValueHelper.getHrJusticeValue(Integer hearingID :" + shJusticeID.intValue() + "+) called");
        SHJusticeBasicValue basicValue = null;
        HRJusticeValue hrValue = null;

        basicValue = this.findByShJusticeID(shJusticeID);
        hrValue = this.buildHrJusticeValue(basicValue);

        log
                .debug("HRJusticeValueHelper.getHrJusticeValue(Integer hearingID :" + shJusticeID.intValue()
                        + "+) finished");
        return hrValue;
    }

    /**
     * The method will find a justice ID by the primary key and transform it to
     * a SHJusticeBasicValue, which is the return type.
     * 
     * @param shJusticeID
     *            Integer
     * @return SHJusticeBasicValue
     * @throws HearingRecordException
     */
    private SHJusticeBasicValue findByShJusticeID(Integer shJusticeID) throws HearingRecordException {
        log
                .debug("HRJusticeValueHelper.findByShJusticeID(Integer shJusticeID :" + shJusticeID.intValue()
                        + "+) called");

        ShJustice justice = null;
        SHJusticeBasicValue basicValue = null;

        try {
            justice = maintainer.findByPrimaryKey(shJusticeID);
            basicValue = maintainer.getShJusticeBasicValue(justice);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
            throw hex;
        }
        log.debug("HRJusticeValueHelper.findByShJusticeID(Integer shJusticeID :" + shJusticeID.intValue()
                + "+) finished");
        return basicValue;
    }

	public Collection findByHearingId(Integer hearingId) throws FinderException {
		Collection maintainerResults = maintainer.findByHearingId(hearingId);
		return maintainerResults;
	}


    /**
     * This will transform a SHJusticeBasicValue to a HRJusticeValue.
     * 
     * @param basicValue
     *            SHJusticeBasicValue
     * @return HRJusticeValue
     */
    public HRJusticeValue buildHrJusticeValue(SHJusticeBasicValue basicValue) {
        log.debug("HRJusticeValueHelper.buildHrJusticeValue(SHJusticeBasicValue" + " basicValue) called");

        if (basicValue == null)
            return null;

        HRJusticeValue hrValue = new HRJusticeValue(basicValue.getId());
        hrValue.setJusticeName(basicValue.getJusticeName());
        hrValue.setHearingID(basicValue.getHearingID());

        log.debug("HRJusticeValueHelper.buildHrJusticeValue(SHJusticeBasicValue" + " basicValue) finished");
        return hrValue;
    }

}
