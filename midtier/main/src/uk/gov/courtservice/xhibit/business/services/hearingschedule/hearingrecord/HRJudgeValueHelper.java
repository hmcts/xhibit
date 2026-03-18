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
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJudgeValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJudgeCriteria;

/**
 * <p>
 * Title: HRJudgeValueHelper
 * </p>
 * <p>
 * Description: This class will search for the judge and return a HRJudgeValue
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
public class HRJudgeValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRJudgeValueHelper.class);

    private BisRefControllerLocal bisRefController;

    /**
     * Default constructor that will intantiate
     */
    public HRJudgeValueHelper() {
        log.debug("HRJudgeValueHelper() called");
        // Get the BisRefController
        this.bisRefController = ((BisRefControllerLocal) CSServices.getEJBServices().createLocalSession(
                BisRefControllerLocalHome.class));
        ;
        log.debug("HRJudgeValueHelper() finished");
    }

    /**
     * This will search for a judge via the BisRefController and build the
     * HRJudgeValue from the result. Even though the return type is a Collection
     * there will only be one entry since we are search for the primary key.
     * 
     * @param refJudgeID
     *            Integer
     * @return Collection of HRJudgeValues.
     * @throws HearingRecordException
     */
    public Collection buildHRJudge(Integer refJudgeID) throws HearingRecordException {
        log.debug("HRJudgeValueHelper.buildHRJudge(Integer refJudgeID :" + refJudgeID.intValue() + "+) called");

        Vector hrJudges = null;

        // set the search criteria
        RefJudgeCriteria criteria = this.getRefJudgeCriteria(refJudgeID);

        try {
            // get the judge and if no hit then return the Collection (null)
            Collection judges = bisRefController.findJudges(criteria);
            if (judges.isEmpty()) {
                return hrJudges;
            }

            hrJudges = new Vector();
            Iterator it = judges.iterator();

            while (it.hasNext()) {
                // get the basic value, transform it to a HR value and add to
                // the vector
                RefJudgeBasicValue basicValue = (RefJudgeBasicValue) it.next();
                HRJudgeValue hrValue = this.buildHrJudgeValue(basicValue);
                hrJudges.addElement(hrValue);
            }
        } catch (BisRefControllerException ex) {
            // problems in the BisRef
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
        log.debug("HRJudgeValueHelper.buildHRJudge(Integer refJudgeID :" + refJudgeID.intValue() + "+) finished");
        return hrJudges;
    }

    /**
     * This method will create RefJudgeCriteria and set it with the primary key
     * value - refJudgeID.
     * 
     * @param refJudgeID
     *            Integer
     * @return RefJudgeCriteria
     */
    private RefJudgeCriteria getRefJudgeCriteria(Integer refJudgeID) {
        log.debug("HRJudgeValueHelper.getRefJudgeCriteria(Integer refJudgeID :" + refJudgeID.intValue() + "+) called");

        RefJudgeCriteria criteria = new RefJudgeCriteria();
        criteria.setPrimaryKey(refJudgeID);

        log
                .debug("HRJudgeValueHelper.getRefJudgeCriteria(Integer refJudgeID :" + refJudgeID.intValue()
                        + "+) finished");
        return criteria;
    }

    /**
     * This will build a HRJudgeValue from a RefJudgeBasicValue
     * 
     * @param basicValue
     *            RefJudgeBasicValue
     * @return HRJudgeValue
     */
    private HRJudgeValue buildHrJudgeValue(RefJudgeBasicValue basicValue) {
        log.debug("HRJudgeValueHelper.buildHrJudgeValue(" + "RefJudgeBasicValue basicValue) called");

        HRJudgeValue hrValue = new HRJudgeValue(basicValue.getId());
        hrValue.setJudgeFirstName(basicValue.getFirstName());
        hrValue.setJudgeMiddleName(basicValue.getMiddleName());
        hrValue.setJudgeSurname(basicValue.getSurname());
        hrValue.setJudgeFullListTitle1(basicValue.getFullListTitle1());
        hrValue.setJudgeFullListTitle2(basicValue.getFullListTitle2());
        hrValue.setJudgeFullListTitle3(basicValue.getFullListTitle3());

        log.debug("HRJudgeValueHelper.buildHrJudgeValue(" + "RefJudgeBasicValue basicValue) finished");
        return hrValue;

    }
}