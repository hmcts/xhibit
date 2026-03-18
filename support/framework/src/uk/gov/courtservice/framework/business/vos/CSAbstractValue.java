package uk.gov.courtservice.framework.business.vos;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.conversion.BasicConverter;
import uk.gov.courtservice.framework.services.conversion.StringMap;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @author Ian Hannaford
 * @version $Id: CSAbstractValue.java,v 1.9 2006/06/05 12:30:14 bzjrnl Exp $
 * @history 24/03/2003 Ian Hannaford Added null pointer check on constructor. If
 *          null then sets updateCount to 0 ;
 *          <P>
 *          04/04/03 - Joseph Babad - Added a null pointer check on the Version
 *          constructor. Altered both null pointer checks to set updateCount to
 *          1
 *          </P>
 *          
 */

public abstract class CSAbstractValue implements CSValueObject {
    int updateCount = -1;

    Integer id;
    
    private boolean dirty = false; // set to true if the details need to be updated

    public CSAbstractValue() {
    }

    public CSAbstractValue(Integer version) {
        // Null check added 04/04/2003 Joseph Babad.
        // Likely to happen is you create an entity and then immediately create
        // the VO in the SAME transaction. Version will not have been set yet,
        // because
        // database insert is set to "delay until commit". So, set the the
        // updateCount to
        // 1 to mimic the fact that the "before insert" trigger does the same.
        // (Joseph Babad)
        if (version != null) {
            this.updateCount = version.intValue();
        } else {
            // Set to one - this is likely to happen wh
            this.updateCount = 1;
        }
    }

    public CSAbstractValue(Integer id, Integer version) {
        this.id = id;
        // Null check added 24/03/2003 Ian Hannaford.
        // Likely to happen is you create an entity and then immediately create
        // the VO in the SAME transaction. Version will not have been set yet,
        // because
        // database insert is set to "delay until commit". So, set the the
        // updateCount to
        // 1 to mimic the fact that the "before insert" trigger does the same.
        // (Joseph Babad)
        if (version != null) {
            this.updateCount = version.intValue();
        } else {
            this.updateCount = 1;
        }
    }

    // public abstract Integer getPrimaryKey();
    // public abstract void setPrimaryKey(Integer primaryKey);
    /**
     * @deprecated - the version should be set in the Constructor and from that
     *             point on be immutable
     * @param updateCount
     */
    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    /**
     * @deprecated - use getVersion() method
     * 
     */
    public int getUpdateCount() {
        return updateCount;
    }

    /**
     * Returns the version number for the entity this Value Object represents.
     * Used to implement an optimistic locking strategy.
     */
    public Integer getVersion() {
        return new Integer(updateCount);
    }

    /**
     * Sets the id.
     */
    public void setVersion(Integer val) {
        if (val != null) {
            updateCount = val.intValue();
        }
    }

    /**
     * Returns the primary key for the entity this Value Object represents.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     */
    public void setId(Integer val) {
        id = val;
    }

    /**
     * Generate a toString value for all sub classes using reflection code in
     * the StringMap and associated converter classes.
     */
    public String toString() {
        String str = "";
        try {
            StringMap map = new StringMap(new BasicConverter());
            map.copyValueToMap(this);
            str = map.toString();
        } catch (CSUnrecoverableException e) {
            CSServices.getDefaultErrorHandler().handleError(e, CSAbstractValue.class);
        }
        return str;
    }
    
    /**
     * Return whether the object is flagged as amended
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Set the flag to signify the object has been amended or not
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
