package uk.gov.courtservice.xhibit.business.entities.shjudge;

// JDK
//import java.util.Date;
//import java.sql.Timestamp;

// J2EE
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
import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeComplexValue;

/**
 * <p>
 * Title: ShJudgeMaintainer
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
 * @author unascribed
 * @version $Id: ShJudgeMaintainer.java,v 1.12 2014/06/20 17:12:46 atwells Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 13/02/03 - PDF - First Issued
 * </P>
 */

public class ShJudgeMaintainer extends AbstractEntityMaintainer {

    private ShJudgeHome home = null;

    private static Logger log = CSServices.getLogger(ShJudgeMaintainer.class);

    public ShJudgeMaintainer() {
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
    public ShJudge findByPrimaryKey(Integer id) throws ObjectNotFoundException {

        try {
            ShJudge judge = home.findByPrimaryKey(id);
            return judge;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Find the entity using the shAttendeeID
     * 
     * @param shAttendeeID -
     *            foreign key to ScheduledHearingAttendee
     * @return The local interface of the returned entity
     */
    public ShJudge findByShAttendeeId(Integer shAttID) throws ObjectNotFoundException {
        try {
            ShJudge judge = home.findByShAttendeeId(shAttID);
            return judge;
        } catch (ObjectNotFoundException e) {
            // this is an expected condition (as stated in
            // HRSHJudgeValueHelper.findJudge
            // which calls it), so don't call handleError (don't want stack
            // dumped)
            // just log a warning instead - JonP
            log.warn("ShJudge with ShAttendeeId " + shAttID.intValue() + " not found");
            // CSServices.getDefaultErrorHandler().handleError(e,
            // getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    // ----------------
    // Public Methods
    // ----------------

    public CSEntityLocal create(CSAbstractValue shJudgeVO, String userDisplayName) {

        String methodName = "create() - ";
        log.debug(methodName + "entry :: shJudgeVO " + shJudgeVO);

        if (!(shJudgeVO instanceof SHJudgeBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(methodName + "Unexpected type:" + shJudgeVO.getClass());
            throw new IllegalArgumentException("Unexpected type:" + shJudgeVO.getClass());
        } else {
            try {
                log.debug(methodName + "Position 1");

                SHJudgeBasicValue shJudgeBasicVO = (SHJudgeBasicValue) shJudgeVO;

                log.debug(methodName + "Position 2");

                ShJudge shJudgeLocal = home.create(shJudgeBasicVO.getDeputyHCJ(), shJudgeBasicVO.getRefJudgeID(),
                        shJudgeBasicVO.getShAttendeeID(), userDisplayName);

                log.debug(methodName + "Position 3");

                log.debug(methodName + "exit - new entry successfully created");

                return shJudgeLocal;
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
            ShJudge judgelocal = home.findByPrimaryKey(primaryKey);

            if (!judgelocal.getVersion().equals(version)) {
            	throw new OptimisticLockException("Optimistic Lock Error");
            } else {

                judgelocal.remove();
                log.debug(methodName + "exit :: delete successful");
            }
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        } catch (RemoveException re) {
            CSServices.getDefaultErrorHandler().handleError(re, getClass(), re.toString());
            throw new EJBException(re);
        }
    }

    /**
     * Updates the entry in the database that corresponds to the supplied VO.
     * 
     * @param shJudgeVO
     *            Value Object containing required changes
     */
    public void update(CSAbstractValue shJudgeVO, String userDisplayName) throws ObjectNotFoundException {

        String methodName = "update() - ";
        try {
            log.debug(methodName + " entry :: shJudgeVO  = " + shJudgeVO);
            SHJudgeBasicValue value = (SHJudgeBasicValue) shJudgeVO;
            ShJudge judge = findByPrimaryKey(value.getId());

            if (isValidVO(shJudgeVO, judge)) {
                copyVOToEntity((SHJudgeBasicValue) shJudgeVO, judge);
                judge.setUpdated(userDisplayName);
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
     * 
     * @param judgeLocal
     * @return
     */
    public SHJudgeBasicValue getShJudgeBasicValue(ShJudge judgeLocal) {

        String methodName = "getShJudgeBasicValue() - ";
        log.debug(methodName + " entry :: creating basic VO for entity. PK = " + judgeLocal.getPrimaryKey());

        SHJudgeBasicValue judgeBasicValue = createBasicVOFromEntity(judgeLocal);

        log.debug(methodName + " exit :: created basic VO  = " + judgeBasicValue);

        return judgeBasicValue;
    }

    /**
     * 
     * @param judgeLocal
     * @return
     */
    public SHJudgeComplexValue getShJudgeComplexValue(ShJudge judgeLocal) {

        String methodName = "getShJudgeComplexValue() - ";
        log.debug(methodName + " entry :: creating complex VO for entity. PK = " + judgeLocal.getPrimaryKey());

        SHJudgeComplexValue judgeComplexValue = createComplexVOFromEntity(judgeLocal);

        log.debug(methodName + " exit :: created complex VO  = " + judgeComplexValue);

        return judgeComplexValue;
    }

    // -----------------
    // Private Methods
    // -----------------

    /**
     * Return the home interface for the entity
     * 
     * @return Reference to home interface
     */
    private ShJudgeHome getHome() {

        if (home == null) {
            home = (ShJudgeHome) CSServices.getServiceLocator().getLocalHome(ShJudgeHome.class);
        }
        return home;
    }

    /**
     * Copies the contents of the supplied VO to the entity.
     * 
     * @param judgeValue
     * @param judgeLocal
     */
    private void copyVOToEntity(SHJudgeBasicValue judgeValue, ShJudge judgeLocal) {

        String methodName = "copyVOToEntity() - ";
        log.debug(methodName + "entry");

        judgeLocal.setDeputyHcj(judgeValue.getDeputyHCJ());
        // judgeLocal.setHearingId( judgeValue.getHearingID() );
        judgeLocal.setRefJudgeId(judgeValue.getRefJudgeID());
        judgeLocal.setShAttendeeId(judgeValue.getShAttendeeID());

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
     * @param judgeLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxBasicValue
     */
    private SHJudgeBasicValue createBasicVOFromEntity(ShJudge judgeLocal) {

        String methodName = "createBasicVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxBasicValue supplying version number
        SHJudgeBasicValue judgeBasicValue = new SHJudgeBasicValue(judgeLocal.getShJudgeId(), judgeLocal.getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(judgeLocal, judgeBasicValue);

        log.debug(methodName + "exit");

        return judgeBasicValue;
    }

    /**
     * Create an xxxComplexValue from a local reference.
     * 
     * @param judgeLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxComplexValue
     */
    private SHJudgeComplexValue createComplexVOFromEntity(ShJudge judgeLocal) {

        String methodName = "createComplexVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxComplexValue supplying version number
        SHJudgeComplexValue judgeComplexValue = new SHJudgeComplexValue(judgeLocal.getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(judgeLocal, judgeComplexValue);

        // Set any necessary CMR relationship values.
        // judgeComplexValue.setXXXX( judgeLocal.getXXX );

        log.debug(methodName + "exit");

        return judgeComplexValue;
    }

    /**
     * Copy the properties of a xxxBasicValue from a local reference. This
     * method can also be used to populate the basic properties of a
     * xxxComplexValue by simple casting of the xxxComplexValue.
     * 
     * @param judgeLocal
     *            A reference to the local interface of the entity
     * @param judgeValue
     *            The xxxBasicValue or xxxComplexValue to populate basic
     *            properites.
     * @return
     */
    private SHJudgeBasicValue copyBasicEntityPropertiesToBasicVO(ShJudge judgeLocal, SHJudgeBasicValue judgeValue) {

        String methodName = "copyBasicEntityPropertiesToBasicVO() - ";
        log.debug(methodName + "entry");

        judgeValue.setDeputyHCJ(judgeLocal.getDeputyHcj());
        // judgeValue.setHearingID( judgeLocal.getHearingId() );
        judgeValue.setRefJudgeID(judgeLocal.getRefJudgeId());
        judgeValue.setShAttendeeID(judgeLocal.getShAttendeeId());
        // judgeValue.setShJudgeID( judgeLocal.getShJudgeId() );

        log.debug(methodName + "exit");

        return judgeValue;
    }

}