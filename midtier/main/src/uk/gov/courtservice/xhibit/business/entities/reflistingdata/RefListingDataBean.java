package uk.gov.courtservice.xhibit.business.entities.reflistingdata;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Ref Listing Data bean.
 */
abstract public class RefListingDataBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(String refDataType, String refDataValue, String userDisplayName) throws CreateException {
        setRefDataType(refDataType);
        setRefDataValue(refDataValue);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String refDataType, String refDataValue, String userDisplayName) throws CreateException {
    }

    public abstract Integer getRefListingDataId();
    public abstract String getRefDataType();
    public abstract String getRefDataValue();
    public abstract String getObsInd();
    public abstract void setRefListingDataId(Integer refListingDataId);
    public abstract void setRefDataType(String refDataType);
    public abstract void setRefDataValue(String refDataValue);
    public abstract void setObsInd(String obsInd);
}