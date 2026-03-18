package uk.gov.courtservice.framework.business.entities;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: AbstractEntityMaintainer
 * </p>
 * <p>
 * Description: Abstract Maintainer class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version 1.0
 * 
 * <Change History/>
 * 
 * <P>
 * 22/11/02 - JB - First issue.
 * </P>
 * <P>
 * 25/11/02 - PDF - isValidVO method added for version checking.
 * </P>
 * <P>
 * 25/04/18 - JU - Added conversion methods between date and calendar.
 * </P>
 */
public abstract class AbstractEntityMaintainer // extends CSAbstractValue
{

    protected Logger log = CSServices.getLogger(getClass());

	protected static final String ENTER_METHOD = "Entered: ";

	protected static final String EXIT_METHOD = "Exited: ";

    /**
     * Creates an entity from a VO.
     * 
     * @param value
     * @return The local reference to the entity created.
     */
    public abstract CSEntityLocal create(CSAbstractValue value, String userDisplayName);

    /**
     * Updates an entity with the values in the VO.
     * 
     * @param value
     */
    public abstract void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException;

    /**
     * Deletes an entity based on ID and Version.
     * 
     * @param id
     * @param version
     */
    public abstract void delete(Integer id, Integer version) throws ObjectNotFoundException;

    /**
     * Validates that the contents of a Value Object were generated from the
     * same version of the data held in the entity.
     * 
     * @param voToValidate
     *            the Value Object to validate
     * @param entityLocal
     *            the local ref of the entity to validate the VO against
     * @return
     */
    protected boolean isValidVO(CSAbstractValue voToValidate, CSEntityLocal entityLocal) {
        String methodName = "isValidVO() - ";

        Integer entityVersion = entityLocal.getVersion();
        Integer voVersion = voToValidate.getVersion();

        if (log.isDebugEnabled()) {
            log.debug(methodName + " entityVersion = " + entityVersion);
            log.debug(methodName + " voVersion = " + voVersion);
        }

        if (entityVersion == null || voVersion == null)
            throw new EJBException("Version obtained from VO or Entity was NULL");

        if (voVersion.intValue() != entityVersion.intValue())
            return false;

        return true;
    }

    protected Calendar convertToCalendar(Timestamp timestamp) {
        Calendar cal = null;

        if (timestamp != null) {
            cal = Calendar.getInstance();
            cal.setTime(new java.util.Date(timestamp.getTime()));
        }

        return cal;
    }

    protected Calendar convertToCalendar(Date date) {
        Calendar cal = null;

        if (date != null) {
            cal = Calendar.getInstance();
            cal.setTime(date);
        }

        return cal;
    }

    protected Timestamp convertToTimestamp(Calendar cal) {
        Timestamp ts = null;

        if (cal != null) {
            ts = new Timestamp(cal.getTime().getTime());
        }

        return ts;
    }

    protected Date convertToDate(Calendar cal) {
        Date date = null;

        if (cal != null) {
            date = cal.getTime();
        }

        return date;
    }
}