package uk.gov.courtservice.xhibit.business.entities.shjustice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeComplexValue;

/**
 * <p>
 * Title: ShJusticeMaintainer
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
 * @version $Id: ShJusticeMaintainer.java,v 1.12 2014/06/20 17:13:06 atwells Exp $
 * 
 * <Change History/>
 * 
 * 
 */

public class ShJusticeMaintainer extends AbstractEntityMaintainer {

    private ShJusticeHome home = null;

    private static Logger log = CSServices.getLogger(ShJusticeMaintainer.class);

    public ShJusticeMaintainer() {

        log.debug("in the constructor");
        home = getHome();
    }

    // ----------------
    // Finder Methods
    // ----------------

	public Collection findByHearingId(Integer hearingId) throws FinderException {
		try {
			ArrayList<SHJusticeBasicValue> results = new ArrayList<SHJusticeBasicValue>();
			Collection dbResults = home.findByHearingId(hearingId);
			Iterator dbIter = dbResults.iterator();
			
			while(dbIter.hasNext()) {
				results.add(getBasicValue((ShJustice) dbIter.next()));
			}
			
			return results;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw e;
		}
	}
	
	public SHJusticeBasicValue getBasicValue(ShJustice justice) {
		SHJusticeBasicValue val = new SHJusticeBasicValue();
		
		val.setHearingID(justice.getHearingId());
		val.setJusticeName(justice.getJusticeName());
		val.setId(justice.getShJusticeId());
		val.setVersion(justice.getVersion());
		
		return val;
	}

    /**
     * Find the entity using the supplied primary key
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     */
    public ShJustice findByPrimaryKey(Integer id) throws ObjectNotFoundException {

        try {
            ShJustice justice = home.findByPrimaryKey(id);
            return justice;
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

    public CSEntityLocal create(CSAbstractValue shJusticeVO, String userDisplayName) {

        String methodName = "create() - ";
        log.debug(methodName + "entry :: shJusticeVO " + shJusticeVO);

        if (!(shJusticeVO instanceof SHJusticeBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(methodName + "Unexpected type:" + shJusticeVO.getClass());
            throw new IllegalArgumentException("Unexpected type:" + shJusticeVO.getClass());
        } else {
            try {
                log.debug(methodName + "Position 1");

                SHJusticeBasicValue shJusticeBasicVO = (SHJusticeBasicValue) shJusticeVO;

                log.debug(methodName + "Position 2");

                if (shJusticeBasicVO.getJusticeName() != null) {
                    // TODO we currently truncate the name before it is stored in XHB_SH_JUSTICE
                    // We need to increase the size of the XHB_SH_JUSTICE field to be large enough
                    // to hold (XHB_REF_JUSTICE.justice_name + XHB_REF_JUSTICE.title + 
                    // XHB_REF_JUSTICE.initials + 2) characters.  Then we need to do the
                    // truncation in Mercator Form A processing when writing to CREST.
                    final int maxJusticeNameSize = java.lang.Math.min(50, shJusticeBasicVO.getJusticeName().length());
                    String justiceName = shJusticeBasicVO.getJusticeName().substring(0, maxJusticeNameSize);
                    ShJustice shJusticeLocal = home.create(justiceName, shJusticeBasicVO.getHearingID(), userDisplayName);

                    log.debug(methodName + "Position 3");
                    log.debug(methodName + "exit - new entry successfully created");
                    return shJusticeLocal;
                }

                return null;
            } catch (CreateException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
                throw new EJBException(e);
            } catch (Exception e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
                throw new EJBException(e);
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

        String methodName = "delete() - with PKey and version ";

        try {
            log.debug(methodName + "entry :: primaryKey: " + primaryKey + ", Version: " + version);
            ShJustice justicelocal = home.findByPrimaryKey(primaryKey);
            log.debug("Pkey of entity = " + justicelocal.getShJusticeId() + "version = " + justicelocal.getVersion());

            if (!justicelocal.getVersion().equals(version)) {
            	throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug("deleting entity");
                justicelocal.remove();
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
     * @param shJusticeVO
     *            Value Object containing required changes
     */
    public void update(CSAbstractValue shJusticeVO, String userDisplayName) throws ObjectNotFoundException {

        String methodName = "update() - ";
        try {
            log.debug(methodName + " entry :: shJusticeVO  = " + shJusticeVO);
            SHJusticeBasicValue value = (SHJusticeBasicValue) shJusticeVO;
            ShJustice justice = findByPrimaryKey(value.getId());

            if (isValidVO(shJusticeVO, justice)) {
                copyVOToEntity((SHJusticeBasicValue) shJusticeVO, justice);
                justice.setUpdated(userDisplayName);
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
     * @param justiceLocal
     * @return
     */
    public SHJusticeBasicValue getShJusticeBasicValue(ShJustice justiceLocal) {

        String methodName = "getShJudgeBasicValue() - ";
        log.debug(methodName + " entry :: creating basic VO for entity. PK = " + justiceLocal.getPrimaryKey());

        SHJusticeBasicValue justiceBasicValue = createBasicVOFromEntity(justiceLocal);

        log.debug(methodName + " exit :: created basic VO  = " + justiceBasicValue);

        return justiceBasicValue;
    }

    /**
     * 
     * @param justiceLocal
     * @return
     */
    public SHJusticeComplexValue getShJusticeComplexValue(ShJustice justiceLocal) {

        String methodName = "getShJudgeComplexValue() - ";
        log.debug(methodName + " entry :: creating complex VO for entity. PK = " + justiceLocal.getPrimaryKey());

        SHJusticeComplexValue justiceComplexValue = createComplexVOFromEntity(justiceLocal);

        log.debug(methodName + " exit :: created complex VO  = " + justiceComplexValue);

        return justiceComplexValue;
    }

    // -----------------
    // Private Methods
    // -----------------

    /**
     * Return the home interface for the entity
     * 
     * @return Reference to home interface
     */
    private ShJusticeHome getHome() {

        if (home == null) {
            home = (ShJusticeHome) CSServices.getServiceLocator().getLocalHome(ShJusticeHome.class);
        }
        return home;
    }

    /**
     * Copies the contents of the supplied VO to the entity.
     * 
     * @param justiceValue
     * @param justiceLocal
     */
    private void copyVOToEntity(SHJusticeBasicValue justiceValue, ShJustice justiceLocal) {

        String methodName = "copyVOToEntity() - ";
        log.debug(methodName + "entry");

        justiceLocal.setJusticeName(justiceValue.getJusticeName());
        justiceLocal.setHearingId(justiceValue.getHearingID());

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
     * @param justiceLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxBasicValue
     */
    public SHJusticeBasicValue createBasicVOFromEntity(ShJustice justiceLocal) {

        String methodName = "createBasicVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxBasicValue supplying version number
        SHJusticeBasicValue justiceBasicValue = new SHJusticeBasicValue(justiceLocal.getShJusticeId(), justiceLocal
                .getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(justiceLocal, justiceBasicValue);

        log.debug(methodName + "exit");

        return justiceBasicValue;
    }

    /**
     * Create an xxxComplexValue from a local reference.
     * 
     * @param justiceLocal
     *            A reference to the local interface of the entity
     * @return Populated xxxComplexValue
     */
    private SHJusticeComplexValue createComplexVOFromEntity(ShJustice justiceLocal) {

        String methodName = "createComplexVOFromEntity() - ";
        log.debug(methodName + "entry");

        // Create xxxComplexValue supplying version number
        SHJusticeComplexValue justiceComplexValue = new SHJusticeComplexValue(justiceLocal.getShJusticeId(),
                justiceLocal.getVersion());

        // Populate basic properties of the VO
        copyBasicEntityPropertiesToBasicVO(justiceLocal, justiceComplexValue);

        // Set any necessary CMR relationship values.
        // judgeComplexValue.setXXXX( judgeLocal.getXXX );
        /*
         * SchedHearingAttendee attendeeLocal =
         * justiceLocal.getScheduledHearingAttendee();
         * SchedHearingAttendeeMaintainer attendeeMaintainer = new
         * SchedHearingAttendeeMaintainer(); SchedHearingAttendeeBasicValue
         * attendeeValue = attendeeMaintainer.createBasicVO(attendeeLocal);
         * justiceComplexValue.setSchedHearingAttendee(attendeeValue);
         */

        log.debug(methodName + "exit");

        return justiceComplexValue;
    }

    /**
     * Copy the properties of a xxxBasicValue from a local reference. This
     * method can also be used to populate the basic properties of a
     * xxxComplexValue by simple casting of the xxxComplexValue.
     * 
     * @param justiceLocal
     *            A reference to the local interface of the entity
     * @param justiceValue
     *            The xxxBasicValue or xxxComplexValue to populate basic
     *            properites.
     * @return
     */
    private SHJusticeBasicValue copyBasicEntityPropertiesToBasicVO(ShJustice justiceLocal,
            SHJusticeBasicValue justiceValue) {

        String methodName = "copyBasicEntityPropertiesToBasicVO() - ";
        log.debug(methodName + "entry");

        justiceValue.setJusticeName(justiceLocal.getJusticeName());
        justiceValue.setHearingID(justiceLocal.getHearingId());
        // justiceValue.setShJusticeID( justiceLocal.getShJusticeId());

        log.debug(methodName + "exit");

        return justiceValue;
    }

}