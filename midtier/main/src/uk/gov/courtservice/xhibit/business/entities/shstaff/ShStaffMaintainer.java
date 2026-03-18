package uk.gov.courtservice.xhibit.business.entities.shstaff;

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
import uk.gov.courtservice.xhibit.business.vos.entities.SHStaffBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHStaffComplexValue;

/**
 * <p>
 * Title: ShStaffMaintainer
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
 * @version $Id: ShStaffMaintainer.java,v 1.12 2014/06/20 17:13:59 atwells Exp $
 * 
 * <Change History/>
 * 
 * 
 */

public class ShStaffMaintainer extends AbstractEntityMaintainer {

    private ShStaffHome home = null;

    private static Logger log = CSServices.getLogger(ShStaffMaintainer.class);

    public ShStaffMaintainer() {

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
    public ShStaff findByPrimaryKey(Integer id) throws ObjectNotFoundException {

        try {
            ShStaff staff = home.findByPrimaryKey(id);
            return staff;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    // ----------------
    // Public Methods
    // ----------------

    public CSEntityLocal create(CSAbstractValue shStaffVO, String userDisplayName) {

        String methodName = "create() - ";
        log.debug(methodName + "entry :: shStaffVO " + shStaffVO);

        if (!(shStaffVO instanceof SHStaffBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(methodName + "Unexpected type:" + shStaffVO.getClass());
            throw new IllegalArgumentException("Unexpected type:" + shStaffVO.getClass());
        } else {
            try {
                log.debug(methodName + "Position 1");

                SHStaffBasicValue shStaffBasicVO = (SHStaffBasicValue) shStaffVO;

                log.debug(methodName + "Position 2");

                ShStaff shStaffLocal = home.create(shStaffBasicVO.getStaffRole(), shStaffBasicVO.getStaffName(), userDisplayName);

                log.debug(methodName + "Position 3");

                log.debug(methodName + "exit - new entry successfully created");

                return shStaffLocal;
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
            ShStaff stafflocal = home.findByPrimaryKey(primaryKey);

            if (!stafflocal.getVersion().equals(version)) {
            	throw new OptimisticLockException("Optimistic Lock Error");
            } else {

                stafflocal.remove();
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
     * @param shStaffVO
     *            Value Object containing required changes
     */
    public void update(CSAbstractValue shStaffVO, String userDisplayName) throws ObjectNotFoundException {

        String methodName = "update() - ";
        try {
            log.debug(methodName + " entry :: shStaffVO  = " + shStaffVO);
            SHStaffBasicValue value = (SHStaffBasicValue) shStaffVO;
            ShStaff staff = findByPrimaryKey(value.getId());

            if (isValidVO(shStaffVO, staff)) {
                copyVOToEntity((SHStaffBasicValue) shStaffVO, staff);
                staff.setUpdated(userDisplayName);
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
     * @param staffLocal
     * @return
     */
    public SHStaffBasicValue getShStaffBasicValue(ShStaff staffLocal) {

        String methodName = "getShSTaffBasicValue() - ";
        log.debug(methodName + " entry :: creating basic VO for entity. PK = " + staffLocal.getPrimaryKey());

        SHStaffBasicValue staffBasicValue = createBasicVOFromEntity(staffLocal);

        log.debug(methodName + " exit :: created basic VO  = " + staffBasicValue);

        return staffBasicValue;
    }

    /**
     * 
     * @param staffLocal
     * @return
     */
    public SHStaffComplexValue getShStaffComplexValue(ShStaff staffLocal) {

        String methodName = "getShSTaffComplexValue() - ";
        log.debug(methodName + " entry :: creating complex VO for entity. PK = " + staffLocal.getPrimaryKey());

        SHStaffComplexValue staffComplexValue = createComplexVOFromEntity(staffLocal);

        log.debug(methodName + " exit :: created complex VO  = " + staffComplexValue);

        return staffComplexValue;
    }

    // -----------------
    // Private Methods
    // -----------------

    /**
     * Return the home interface for the entity
     * 
     * @return Reference to home interface
     */
    private ShStaffHome getHome() {

        if (home == null) {
            home = (ShStaffHome) CSServices.getServiceLocator().getLocalHome(ShStaffHome.class);
        }
        return home;
    }

    /**
     * Copies the contents of the supplied VO to the entity.
     * 
     * @param staffValue
     * @param staffLocal
     */
    private void copyVOToEntity(SHStaffBasicValue staffValue, ShStaff staffLocal) {

        String methodName = "copyVOToEntity() - ";
        log.debug(methodName + "entry");

        staffLocal.setStaffName(staffValue.getStaffName());
        staffLocal.setStaffRole(staffValue.getStaffRole());

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
     * @param staffLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxBasicValue
     */
    public SHStaffBasicValue createBasicVOFromEntity(ShStaff staffLocal) {

        String methodName = "createBasicVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxBasicValue supplying version number
        SHStaffBasicValue staffBasicValue = new SHStaffBasicValue(staffLocal.getShStaffId(), staffLocal.getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(staffLocal, staffBasicValue);

        log.debug(methodName + "exit");

        return staffBasicValue;
    }

    /**
     * Create an xxxComplexValue from a local reference.
     * 
     * @param staffLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxComplexValue
     */
    private SHStaffComplexValue createComplexVOFromEntity(ShStaff staffLocal) {

        String methodName = "createComplexVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxComplexValue supplying version number
        SHStaffComplexValue staffComplexValue = new SHStaffComplexValue(staffLocal.getShStaffId(), staffLocal
                .getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(staffLocal, staffComplexValue);

        // Set any necessary CMR relationship values.
        // judgeComplexValue.setXXXX( judgeLocal.getXXX );
        /*
         * SchedHearingAttendee attendeeLocal =
         * staffLocal.getScheduledHearingAttendee();
         * SchedHearingAttendeeMaintainer attendeeMaintainer = new
         * SchedHearingAttendeeMaintainer(); SchedHearingAttendeeBasicValue
         * attendeeValue = attendeeMaintainer.createBasicVO(attendeeLocal);
         * staffComplexValue.setSchedHearingAttendee(attendeeValue);
         */

        log.debug(methodName + "exit");

        return staffComplexValue;
    }

    /**
     * Copy the properties of a xxxBasicValue from a local reference. This
     * method can also be used to populate the basic properties of a
     * xxxComplexValue by simple casting of the xxxComplexValue.
     * 
     * @param staffLocal
     *            A reference to the local interface of the entity
     * @param staffValue
     *            The xxxBasicValue or xxxComplexValue to populate basic
     *            properites.
     * @return
     */
    private SHStaffBasicValue copyBasicEntityPropertiesToBasicVO(ShStaff staffLocal, SHStaffBasicValue staffValue) {

        String methodName = "copyBasicEntityPropertiesToBasicVO() - ";
        log.debug(methodName + "entry");

        staffValue.setStaffName(staffLocal.getStaffName());
        staffValue.setStaffRole(staffLocal.getStaffRole());
        // staffValue.setShStaffID( staffLocal.getShStaffId());

        log.debug(methodName + "exit");

        return staffValue;
    }

}