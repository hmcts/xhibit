package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//JDK
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRep;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHLegRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;

/**
 * <p>
 * Title: HRSHLegRepValueHelper
 * </p>
 * <p>
 * Description: This class will transform HRSHLegRepValueHelper to/from
 * SHLegRepBasicValue and SHLegRepComplexValue
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
 * <p>
 * history
 * </p>
 * <p>
 * MH - 20030524 - make sure we don't search for ref_system_codes for the
 * advocate category description when there is no id. since that will give us
 * the last refcode if no id.
 * 
 * </p>
 * 
 */
public class HRSHLegRepValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRSHLegRepValueHelper.class);

    private ShLegRepMaintainer maintainer = null;

    private BisRefControllerLocal bisRefController;

    public HRSHLegRepValueHelper() {
        maintainer = new ShLegRepMaintainer();

        bisRefController = (BisRefControllerLocal) CSServices.getEJBServices().createLocalSession(
                BisRefControllerLocalHome.class);
    }

    /**
     * This will build a HRSHLegRepValue from a SHLegRepBasicValue
     * 
     * @param basicValue
     *            SHLegRepBasicValue
     * @return HRSHLegRepValue
     * @throws HearingRecordException
     */
    public HRSHLegRepValue buildHRSHLegRepValueFromBasic(SHLegRepBasicValue basicValue) throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.buildHRSHLegRepValueFromBasic(SHLegRepBasicValue basicValue) called");
        log.debug(">>>>>>>>>>>>>> SHLegRepBasicValue before : " + basicValue.toString());

        HRSHLegRepValue legRepValue = new HRSHLegRepValue(basicValue.getId(), basicValue.getVersion());
        legRepValue.setRefDefenceCategoryID(basicValue.getRefDefenceCategoryID());
        legRepValue.setRefLegRepID(basicValue.getRefLegalRepID());

        // if we have a category id we want to find the description for it.
        if (legRepValue.getRefDefenceCategoryID() != null) {
            // added to support printing
            try {
                RefSystemCodeCriteria sysCodeCriteria = new RefSystemCodeCriteria();

                sysCodeCriteria.setPrimaryKey(basicValue.getRefDefenceCategoryID());
                Collection values = bisRefController.findSystemCodes(sysCodeCriteria);
                // should only be one as we've searched on primary key
                RefSystemCodeBasicValue sysCodeBasicValue = (RefSystemCodeBasicValue) values.iterator().next();
                legRepValue.setRefDefenceCategoryDesc(sysCodeBasicValue.getDecode());
            } catch (BisRefControllerException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                Object[] parameters = e.getUserMessageAsMessage().getParameters();

                throw new HearingRecordException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
            }
        }

        log.debug(">>>>>>>>>>>>>> HRSHLegRepValue after : " + legRepValue.toString());
        log.debug("HRSHLegRepValueHelper.buildHRSHLegRepValueFromBasic(SHLegRepBasicValue basicValue) finsihed");
        return legRepValue;
    }

    /**
     * This will build a HRSHLegRepValue from a SHLegRepComplexValue
     * 
     * @param complexValue
     *            SHLegRepComplexValue
     * @return HRSHLegRepValue
     * @throws HearingRecordException
     */
    public HRSHLegRepValue buildHRSHLegRepValueFromComplex(SHLegRepComplexValue complexValue)
            throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.buildHRSHLegRepValueFromComplex(SHLegRepComplexValue basicValue) called");
        log.debug(">>>>>>>>>>>>>> SHLegRepComplexValue before : " + complexValue.toString());

        HRSHLegRepValue legRepValue = new HRSHLegRepValue(complexValue.getId(), complexValue.getVersion());
        legRepValue.setRefDefenceCategoryID(complexValue.getRefDefenceCategoryID());
        legRepValue.setRefLegRepID(complexValue.getRefLegalRepID());

        // if we have a category id we want to find the description for it.
        if (legRepValue.getRefDefenceCategoryID() != null) {
            // added to support printing
            try {
                RefSystemCodeCriteria sysCodeCriteria = new RefSystemCodeCriteria();

                sysCodeCriteria.setPrimaryKey(complexValue.getRefDefenceCategoryID());
                Collection values = bisRefController.findSystemCodes(sysCodeCriteria);
                // should only be one as we've searched on primary key
                RefSystemCodeBasicValue sysCodeBasicValue = (RefSystemCodeBasicValue) values.iterator().next();
                legRepValue.setRefDefenceCategoryDesc(sysCodeBasicValue.getDecode());
            } catch (BisRefControllerException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                Object[] parameters = e.getUserMessageAsMessage().getParameters();

                throw new HearingRecordException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
            }
        }

        log.debug(">>>>>>>>>>>>>> HRSHLegRepValue after : " + legRepValue.toString());
        log.debug("HRSHLegRepValueHelper.buildHRSHLegRepValueFromComplex(SHLegRepComplexValue complexValue) finsihed");
        return legRepValue;
    }

    /**
     * This will build a SHLegRepBasicValue from a HRSHLegRepValue
     * 
     * @param hrSHLegRepValue
     *            HRSHLegRepValue
     * @return SHLegRepBasicValue
     * @throws HearingRecordException
     */
    public SHLegRepBasicValue buildBasicValue(HRSHLegRepValue hrSHLegRepValue) throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.buildBasicValue(HRSHLegRepValue hrSHLegRepValue) called");
        log.debug(">>>>>>>>>>>>>> HRSHLegRepValue before : " + hrSHLegRepValue.toString());

        // get the existing basiv value from database.
        SHLegRepBasicValue existingBasicValue = this.getSHLegRepBasicValue(hrSHLegRepValue.getId());

        try {
            // check the versions and throw OptimisticLockException if not
            // the same
            if (!existingBasicValue.getVersion().equals(hrSHLegRepValue.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            }
            // change the value that was part of the hearing record value
            // object.
            existingBasicValue.setRefDefenceCategoryID(hrSHLegRepValue.getRefDefenceCategoryID());
            existingBasicValue.setSubInst(hrSHLegRepValue.getSubInst());
            existingBasicValue.setSubstitutedRefLegalRepID(hrSHLegRepValue.getSubstitutedRefLegRepId());
        } catch (OptimisticLockException ex) {
            // the versions were not the same.
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
        log.debug(">>>>>>>>>>>>>> SHLegRepBasicValue after : " + existingBasicValue.toString());
        log.debug("HRSHLegRepValueHelper.buildBasicValue(HRSHLegRepValue hrSHLegRepValue) finished");
        return existingBasicValue;
    }

    /**
     * This method will get the ShLegReps for a particular ScheduledHearing. It
     * will add the ShLegRep to the returning Collection if the representation
     * didn't defened the defendant. It will also add the ShLegRep to the
     * Collection if he/she represented the defendant(defendantOnCaseID) passed
     * in.
     * 
     * @param scheduledHearingID
     *            Integer
     * @param defendantOnCaseID
     *            Integer
     * @return Collection of complex values.
     * @throws HearingRecordException
     */
    public Collection getSHLegReps(Integer scheduledHearingID, Integer defendantOnCaseID) throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.getHRSHLegRepValues(Integer scheduledHearingID, "
                + "Integer defendantOnCaseID) called");

        Vector shLegRepComplexValues = null;

        // get all the ShLegRep entities.
        Collection shLegReps = this.findByScheduledHearings(scheduledHearingID);

        if (shLegReps != null) {
            log.debug("The ArrayList is not null - we have some shLegReps");
            shLegRepComplexValues = new Vector();

            Iterator it = shLegReps.iterator();
            while (it.hasNext()) {
                log.debug("Get the ShLegRep entity");
                ShLegRep entity = (ShLegRep) it.next();

                // convert the entity to a complexValue
                log.debug("Get the complex value");
                SHLegRepComplexValue complexValue = maintainer.getShLegRepComplexValue(entity);

                // Get all scheduled hearing defendants for that scheduled
                // hearing where
                // there is a scheduled defendant
                if (complexValue.getSchedHearDefID() != null) {
                    complexValue.setSchedHearingDefendant(this
                            .findScheduledHearingDef(complexValue.getSchedHearDefID()));
                }
                // else if the shLegRep is NOT signed in for a defendant, he/she
                // is on the
                // prosecution side - which is for the case. All these should be
                // fetched.
                else {
                    log.debug("DefOnCaseID is null so add the ShLegRep");
                    shLegRepComplexValues.addElement(complexValue);
                }

                if (complexValue.getSchedHearingDefendant() != null) {
                    // Get the key for the scheduled hearing defendant and
                    // the defendant on case id.
                    Integer schedHearingDefendantID = complexValue.getSchedHearingDefendant().getId();
                    Integer defOnCase = complexValue.getSchedHearingDefendant().getDefendantOnCaseID();

                    // log.debug(">>>>>>>>>> defendantOnCaseID method arg: "
                    // + defendantOnCaseID.toString());
                    // log.debug(">>>>>>>>>> schedHearingDefendantID: " +
                    // schedHearingDefendantID.toString());
                    // log.debug(">>>>>>>>>> defOnCase local: " +
                    // defOnCase.toString());

                    log.debug("Validate if we want to add the ShLegRep to the returning vector");
                    // we ONLY need to fetch all the SHLegReps for the
                    // defendant that the
                    // hearing record is for. All other defendants are for
                    // other hearing
                    // records and we don't want to show other defendants
                    // advocates/solicitors.
                    if (defOnCase.equals(defendantOnCaseID)) {
                        log.debug("The ShLegRep has defended the defendant so add to Vector");
                        shLegRepComplexValues.addElement(complexValue);
                    }
                }// end if

            }// end while
        }// end if
        log.debug("HRSHLegRepValueHelper.getHRSHLegRepValues(Integer scheduledHearingID, "
                + "Integer defendantOnCaseID) finished");
        return shLegRepComplexValues;
    }

    /**
     * This method will find a SchedHearingDefendant by primary key.
     * 
     * @param schedHearingDefID
     *            Integer
     * @return SchedHearingDefendantBasicValue
     * @throws HearingRecordException
     */
    private SchedHearingDefendantBasicValue findScheduledHearingDef(Integer schedHearingDefID)
            throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.getScheduledHearingDef(Integer schedHearingDefID) called");
        SchedHearingDefendantBasicValue value = null;
        SchedHearingDefendantMaintainer maintainer = new SchedHearingDefendantMaintainer();

        try {
            SchedHearingDefendant entity = maintainer.findByPrimaryKey(schedHearingDefID);
            value = maintainer.getSchedHearingDefendantBasicValue(entity);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
            throw hex;
        }
        log.debug("HRSHLegRepValueHelper.getScheduledHearingDef(Integer schedHearingDefID) finsihed");
        return value;
    }

    // ---------------------------- Private Methods
    // -----------------------------//

    /**
     * This implementes the call to the maintainer to find the ShLegRep from the
     * database via the primary key.
     * 
     * @param shLegRepID
     *            Integer
     * @return ShLegRep
     * @throws HearingRecordException
     */
    private ShLegRep findShLegRep(Integer shLegRepID) throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.findShLegRep(Integer shLegRepID) called");
        ShLegRep shLegRep = null;

        try {
            shLegRep = maintainer.findByPrimaryKey(shLegRepID);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
            throw hex;
        }
        log.debug("HRSHLegRepValueHelper.findShLegRep(Integer shLegRepID) finsihed");
        return shLegRep;
    }

    /**
     * This will call the finder method (shLegRepID) and transform the response
     * to a shLegRepID
     * 
     * @param shLegRepID
     *            Integer
     * @return SHLegRepBasicValue
     * @throws HearingRecordException
     */
    private SHLegRepBasicValue getSHLegRepBasicValue(Integer shLegRepID) throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.getSHLegRepBasicValue(Integer shLegRepID) called");
        SHLegRepBasicValue value = null;
        ShLegRep shLegRep = this.findShLegRep(shLegRepID);
        value = maintainer.getShLegRepBasicValue(shLegRep);
        log.debug("HRSHLegRepValueHelper.getSHLegRepBasicValue(Integer shLegRepID) finsihed");
        return value;
    }

    /**
     * This will search for all ShLegReps for a particular scheudled hering from
     * the database. It will return a Collection of ShLegRep entities.
     * 
     * @param scheduledHearingID
     *            shLegRepID
     * @return Collection of ShLegRep
     * @throws HearingRecordException
     */
    private Collection findByScheduledHearings(Integer scheduledHearingID) throws HearingRecordException {
        log.debug("HRSHLegRepValueHelper.findByScheduledHearings(Integer scheduledHearingID) called");
        Collection shLegReps = null;
        try {
            shLegReps = maintainer.findByScheduledHearingID(scheduledHearingID);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
            throw hex;
        }

        log.debug("HRSHLegRepValueHelper.findByScheduledHearings(Integer scheduledHearingID) finsihed");
        return shLegReps;
    }

}