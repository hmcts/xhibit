package uk.gov.courtservice.xhibit.business.entities.reflistingdata;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * Ref Listing Data interface.
 */
public interface RefListingData extends CSEntityLocal {

	public Integer getRefListingDataId();
	
    public String getRefDataType();

    public String getRefDataValue();
    
    public String getObsInd();
    
    public void setRefListingDataId(Integer refListingDataId);

    public void setRefDataType(String refDataType);

    public void setRefDataValue(String refDataValue);
    
    public void setObsInd(String obsInd);

}