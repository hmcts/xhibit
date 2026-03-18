package uk.gov.courtservice.xhibit.business.vos.listdistribution;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: DistributionStatusComplexValue
 * </p>
 * <p>
 * Description: Complex values which contain data about distribution status
 * implement this class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DistributionStatusComplexValue.java,v 1.1 2005/02/23 14:55:33
 *          bzjrnl Exp $
 */
public abstract class DistributionStatusComplexValue implements Serializable {
	
	static final long serialVersionUID = -3553404549141654748L;
	
    /**
     * Get the type of the distributed documents
     */
    public abstract String getDocumentType();

    /**
     * Get the creation timestamp
     */
    public abstract Date getCreationDate();

    /**
     * Get the date time sent
     */
    public Date getDistributionDate() {
        return null;
    }

    /**
     * Get the status of the distributed documents
     */
    public abstract String getStatus();

    /**
     * Get the distribution method
     */
    public abstract String getDistributionType();

    /**
     * Get the type of distribution
     */
    public abstract int getRecipientCount();

    /**
     * Return true if recipient names are available
     */
    public boolean hasRecipientNames() {
        return false;
    }

    /**
     * Return the name of the numbered recipient
     */
    public String getRecipientName(int index) {
        throw new IllegalArgumentException("index: " + index);
    }

    /**
     * Return true if the object can be deleted.
     */
    public abstract boolean canDelete();

    /**
     * Mark the object as deleted
     */
    public abstract void delete();
}
