package uk.gov.courtservice.xhibit.business.entities.shlegrep;

// JDK
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepComplexValue;

/**
 * <p>
 * Title: ShLegRepMaintainer
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Faisal Shoukat
 * @version $Id: ShLegRepMaintainer.java,v 1.17 2014/06/20 17:13:39 atwells Exp $
 * 
 */

public class ShLegRepMaintainer extends AbstractEntityMaintainer {

    /**
     * ShLegRepHome home
     */
    private ShLegRepHome home = null;

    /**
     * Logger log
     */
    private static Logger log = CSServices.getLogger(ShLegRepMaintainer.class);

    /**
     * <init>
     */
    public ShLegRepMaintainer() {
        home = getHome();
    }

    // ----------------
    // Finder Methods
    // ----------------

    /**
     * Find the entity using the supplied primary key
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     */
    public ShLegRep findByPrimaryKey(Integer id) throws ObjectNotFoundException {

        try {
            ShLegRep legRep = home.findByPrimaryKey(id);
            return legRep;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Find the entities containing the supplied Scheduled Hearing Id.
     * 
     * @param scheduledHearingID
     *            The scheduled hearing Id to search by
     * @return A collection of ShLegRep local interfaces
     * @throws ObjectNotFoundException
     */
    public Collection findByScheduledHearingID(Integer scheduledHearingID) throws ObjectNotFoundException {

        try {
            Collection legRep = home.findByScheduledHearingId(scheduledHearingID);
            return legRep;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * findBySHIdRoleAndSolFirmOrRefLegRep
     * 
     * @param scheduledHearingId
     *            parameter for findBySHIdRoleAndSolFirmOrRefLegRep
     * @param legalRole
     *            parameter for findBySHIdRoleAndSolFirmOrRefLegRep
     * @param solFirmOrRefLegRep
     *            parameter for findBySHIdRoleAndSolFirmOrRefLegRep
     * @return the returned Collection
     */
    public Collection findBySHIdRoleAndSolFirmOrRefLegRep(Integer scheduledHearingId, String legalRole,
            String solFirmOrRefLegRep) {
        try {
            Collection shLegReps = home.findBySHIdRoleAndSolFirmOrRefLegRep(scheduledHearingId, legalRole,
                    solFirmOrRefLegRep);
            return shLegReps;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * findByRefLegRepID
     * 
     * @param refLegRepId
     *            parameter for findByRefLegRepID
     * @return the returned Collection
     */
    public Collection findByRefLegRepIDForDateHearingID(Integer refLegRepId, Timestamp startDate, Timestamp endDate,
            Integer hearingId) {
        try {
            Collection shLegReps = home.findByRefLegRepIDForDateHearingID(refLegRepId, startDate, endDate, hearingId);
            return shLegReps;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    // ----------------
    // Public Methods
    /**
     * // ----------------
     */

    public CSEntityLocal create(CSAbstractValue shLegRepVO, String userDisplayName) {

        String methodName = "create() - ";
        log.debug(methodName + "entry :: shJusticeVO " + shLegRepVO);

        if (!(shLegRepVO instanceof SHLegRepBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(methodName + "Unexpected type:" + shLegRepVO.getClass());
            throw new IllegalArgumentException("Unexpected type:" + shLegRepVO.getClass());
        } else {
            try {

                SHLegRepBasicValue shLegRepBasicVO = (SHLegRepBasicValue) shLegRepVO;

                ShLegRep shLegRepLocal = home.create(shLegRepBasicVO.getCrestSequenceNo(), shLegRepBasicVO
                        .getLegalRole(), shLegRepBasicVO.getIsSignIn(), shLegRepBasicVO.getSolFirmOrRefLegalRep(),
                        shLegRepBasicVO.getRefLegalRepID(), shLegRepBasicVO.getRefDefenceCategoryID(), shLegRepBasicVO
                                .getRefSolicitorFirmID(), shLegRepBasicVO.getSubstitutedRefLegalRepID(),
                                shLegRepBasicVO.getSubInst(), userDisplayName);

                log.debug(methodName + "exit - new entry successfully created");

                return shLegRepLocal;
            } catch (CreateException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
                throw new EJBException(e);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Deletes the database entry with the supplied primary key and version
     * 
     * @param primaryKey
     *            The primary key of the entry to be deleted
     * @param version
     *            The specific version number of the entry to check prior to
     *            deletion
     */
    public void delete(Integer primaryKey, Integer version) throws ObjectNotFoundException {

        String methodName = "delete() - ";

        try {
            log.debug(methodName + "entry :: primaryKey: " + primaryKey + ", Version: " + version);
            ShLegRep legReplocal = home.findByPrimaryKey(primaryKey);

            if (!legReplocal.getVersion().equals(version)) {
            	throw new OptimisticLockException("Optimistic Lock Error");
            } else {

                legReplocal.remove();
                log.debug(methodName + "exit :: delete successful");
            }
        } catch (FinderException fe) {
            CSServices.getDefaultErrorHandler().handleError(fe, getClass(), fe.toString());
            if (fe instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) fe;
            throw new EJBException(fe);
        } catch (RemoveException re) {
            CSServices.getDefaultErrorHandler().handleError(re, getClass(), re.toString());
            throw new EJBException(re);
        }
    }

    /**
     * Updates the entry in the database that corresponds to the supplied VO.
     * 
     * @param shLegRepVO
     *            Value Object containing required changes
     */
    public void update(CSAbstractValue shLegRepVO, String userDisplayName) throws ObjectNotFoundException {

        String methodName = "update() - ";
        try {
            log.debug(methodName + " entry :: shLegRepVO  = " + shLegRepVO);
            SHLegRepBasicValue value = (SHLegRepBasicValue) shLegRepVO;
            ShLegRep legRep = findByPrimaryKey(value.getId());

            if (isValidVO(shLegRepVO, legRep)) {
                copyVOToEntity((SHLegRepBasicValue) shLegRepVO, legRep);
                legRep.setUpdated(userDisplayName);
            } else {
            	throw new OptimisticLockException("Optimistic Lock Error");
            }
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }

        log.debug(methodName + " exit - update successful");
    }

    /**
     * @param legRepLocal
     */
    public SHLegRepBasicValue getShLegRepBasicValue(ShLegRep legRepLocal) {

        String methodName = "getShJudgeBasicValue() - ";
        log.debug(methodName + " entry :: creating basic VO for entity. PK = " + legRepLocal.getPrimaryKey());

        SHLegRepBasicValue legRepBasicValue = createBasicVOFromEntity(legRepLocal);

        log.debug(methodName + " exit :: created basic VO  = " + legRepBasicValue);

        return legRepBasicValue;
    }

    /**
     * @param legRepLocal
     */
    public SHLegRepComplexValue getShLegRepComplexValue(ShLegRep legRepLocal) {

        String methodName = "getShLegRepComplexValue() - ";
        log.debug(methodName + " entry :: creating complex VO for entity. PK = " + legRepLocal.getPrimaryKey());

        SHLegRepComplexValue legRepComplexValue = createComplexVOFromEntity(legRepLocal);

        log.debug(methodName + " exit :: created complex VO  = " + legRepComplexValue);

        return legRepComplexValue;
    }

    // -----------------
    // Private Methods
    // -----------------

    /**
     * Return the home interface for the entity
     * 
     * @return Reference to home interface
     */
    private ShLegRepHome getHome() {

        if (home == null) {
            home = (ShLegRepHome) CSServices.getServiceLocator().getLocalHome(ShLegRepHome.class);
        }
        return home;
    }

    /**
     * Copies the contents of the supplied VO to the entity.
     * 
     * @param legRepValue
     * @param legRepLocal
     */
    private void copyVOToEntity(SHLegRepBasicValue legRepValue, ShLegRep legRepLocal) {

        String methodName = "copyVOToEntity() - ";
        log.debug(methodName + "entry");

        // legRepLocal.setCcInfoId(legRepValue.getCcInfoID());
        legRepLocal.setCrestSequenceNo(legRepValue.getCrestSequenceNo());
        legRepLocal.setIsSignedIn(legRepValue.getIsSignIn());
        legRepLocal.setLegalRole(legRepValue.getLegalRole());
        legRepLocal.setRefDefenceCategoryId(legRepValue.getRefDefenceCategoryID());
        legRepLocal.setRefLegalRepId(legRepValue.getRefLegalRepID());
        legRepLocal.setRefSolicitorFirmId(legRepValue.getRefSolicitorFirmID());
        // legRepLocal.setSchedHearDefId(legRepValue.getSchedHearDefID());
        legRepLocal.setSolFirmOrRefLegalRep(legRepValue.getSolFirmOrRefLegalRep());
        legRepLocal.setSubstitutedRefLegalRepId(legRepValue.getSubstitutedRefLegalRepID());
        legRepLocal.setSubInst(legRepValue.getSubInst());

        /**
         * @todo Investigate why no setShJudgeId on entity when associated get
         *       method
         */
        // judgeLocal.setShJudgeId( judgeValue.getShJudgeID() );
        log.debug(methodName + "exit");
    }

    /**
     * Create an xxxBasicValue from a local reference.
     * 
     * @param legRepLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxBasicValue
     */
    public SHLegRepBasicValue createBasicVOFromEntity(ShLegRep legRepLocal) {

        String methodName = "createBasicVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxBasicValue supplying version number
        SHLegRepBasicValue legRepBasicValue = new SHLegRepBasicValue(legRepLocal.getShLegRepId(), legRepLocal
                .getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(legRepLocal, legRepBasicValue);

        log.debug(methodName + "exit");

        return legRepBasicValue;
    }

    /**
     * Create an xxxComplexValue from a local reference.
     * 
     * @param legRepLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxComplexValue
     */
    private SHLegRepComplexValue createComplexVOFromEntity(ShLegRep legRepLocal) {

        String methodName = "createComplexVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxComplexValue supplying version number
        SHLegRepComplexValue legRepComplexValue = new SHLegRepComplexValue(legRepLocal.getShLegRepId(), legRepLocal
                .getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(legRepLocal, legRepComplexValue);

        // Set any necessary CMR relationship values.
        // judgeComplexValue.setXXXX( judgeLocal.getXXX );
        /*
         * SchedHearingAttendee attendeeLocal =
         * legRepLocal.getScheduledHearingAttendee();
         * SchedHearingAttendeeMaintainer attendeeMaintainer = new
         * SchedHearingAttendeeMaintainer(); SchedHearingAttendeeBasicValue
         * attendeeValue = attendeeMaintainer.createBasicVO(attendeeLocal);
         * legRepComplexValue.setSchedHearingAttendee(attendeeValue);
         */

        log.debug(methodName + "exit");

        return legRepComplexValue;
    }

    /**
     * Copy the properties of a xxxBasicValue from a local reference. This
     * method can also be used to populate the basic properties of a
     * xxxComplexValue by simple casting of the xxxComplexValue.
     */
    private SHLegRepBasicValue copyBasicEntityPropertiesToBasicVO(ShLegRep legRepLocal, SHLegRepBasicValue legRepValue) {

        String methodName = "copyBasicEntityPropertiesToBasicVO() - ";
        log.debug(methodName + "entry");

        legRepValue.setCcInfoID(legRepLocal.getCcInfoId());
        legRepValue.setCrestSequenceNo(legRepLocal.getCrestSequenceNo());
        legRepValue.setIsSignIn(legRepLocal.getIsSignedIn());
        legRepValue.setLegalRole(legRepLocal.getLegalRole());
        legRepValue.setRefDefenceCategoryID(legRepLocal.getRefDefenceCategoryId());
        legRepValue.setRefLegalRepID(legRepLocal.getRefLegalRepId());
        legRepValue.setRefSolicitorFirmID(legRepLocal.getRefSolicitorFirmId());
        legRepValue.setSchedHearDefID(legRepLocal.getSchedHearDefId());
        legRepValue.setScheduledHearingID(legRepLocal.getScheduledHearingId());
        legRepValue.setSolFirmOrRefLegalRep(legRepLocal.getSolFirmOrRefLegalRep());
        legRepValue.setSubstitutedRefLegalRepID(legRepLocal.getSubstitutedRefLegalRepId());
        legRepValue.setSubInst(legRepLocal.getSubInst());

        log.debug(methodName + "exit");

        return legRepValue;
    }

}