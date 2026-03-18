package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//JDK
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCourtReporterValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;

/**
 * <p>
 * Title: HRCourtReporterValueHelper
 * </p>
 * <p>
 * Description: This class will query the BisRef to get the CourtReporter data.
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
 * 
 * <Change History/>
 * 
 * <P>
 * 02/06/03 - MH - Added Initials.
 * </P>
 */
public class HRCourtReporterValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRCounselValueHelper.class);

    private BisRefControllerLocal bisRefController;

    /**
     * Default constructor that will instantiate the BisRefController
     */
    public HRCourtReporterValueHelper() {
        log.debug("HRCourtReporterValueHelper() called");
        // Get the BisRefController
        this.bisRefController = ((BisRefControllerLocal) CSServices.getEJBServices().createLocalSession(
                BisRefControllerLocalHome.class));
        log.debug("HRCourtReporterValueHelper() finished");
    }

    /**
     * This will look up the CourtReporters for a scheduled hearing and build a
     * HRCourtReporter for each found court reporter.
     * 
     * @param refCourtReporterID
     *            Integer
     * @return Collection of HRCourtReporters
     * @throws HearingRecordException
     */
    public Collection buildHRCourtReporters(Integer refCourtReporterID) throws HearingRecordException {
        log.debug("HRCourtReporterValueHelper.buildHRCourtReporters(Integer refCourtReporterID :"
                + refCourtReporterID.intValue() + "+) called");

        Vector hrCourtReporters = null;

        // set the search criteria
        RefCourtReporterCriteria criteria = this.getRefCourtReporterCriteria(refCourtReporterID);

        try {
            // get the reporters and if no hit then return the Collection
            // (null)
            Collection courtReps = bisRefController.findCourtReporters(criteria);

            if (courtReps.isEmpty()) {
                return hrCourtReporters;
            }
            hrCourtReporters = new Vector();

            Iterator it = courtReps.iterator();
            while (it.hasNext()) {
                // get the basic value, transform it to a HR value and add to
                // the vector
                RefCourtReporterBasicValue basicValue = (RefCourtReporterBasicValue) it.next();
                HRCourtReporterValue hrValue = this.buildHrCourtReporterValue(basicValue);
                hrCourtReporters.addElement(hrValue);
            }
        } catch (BisRefControllerException ex) {
            // problems in the BisRef
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
        log.debug("HRCourtReporterValueHelper.buildHRCourtReporters(Integer refCourtReporterID) finished");
        return hrCourtReporters;
    }

    /**
     * Method to set the search criteria for the RefCourtReporterCriteria It
     * will set the criteria to search for the primary key.
     * 
     * @param refCourtReporterID
     *            Integer
     * @return RefCourtReporterCriteria
     */
    private RefCourtReporterCriteria getRefCourtReporterCriteria(Integer refCourtReporterID) {
        log.debug("HRCourtReporterValueHelper.getRefLegRepCriteria(Integer refCourtReporterID) finished");

        RefCourtReporterCriteria criteria = new RefCourtReporterCriteria();
        criteria.setPrimaryKey(refCourtReporterID);

        log.debug("HRCourtReporterValueHelper.getRefLegRepCriteria(Integer refCourtReporterID) finished");
        return criteria;
    }

    /**
     * This will take in a RefCourtReporterBasicValue and set a
     * HRCourtReporterValue with the values from the basic value.
     * 
     * @param basicValue
     *            RefCourtRepor
     * @return HRCourtReporterValue
     */
    private HRCourtReporterValue buildHrCourtReporterValue(RefCourtReporterBasicValue basicValue) {
        log.debug("HRCourtReporterValueHelper.buildHrCourtReporterValue("
                + "RefCourtReporterBasicValue basicValue finished");

        HRCourtReporterValue hrValue = new HRCourtReporterValue(basicValue.getId());
        hrValue.setFirstName(basicValue.getFirstName());
        hrValue.setMiddleName(basicValue.getMiddleName());
        hrValue.setSurname(basicValue.getSurname());
        hrValue.setInitials(basicValue.getInitials());
        hrValue.setCrestCourtReporterId(basicValue.getCrestCourtReporterId());

        log.debug("HRCourtReporterValueHelper.buildHrCourtReporterValue("
                + "RefCourtReporterBasicValue basicValue) finished");
        return hrValue;
    }

}