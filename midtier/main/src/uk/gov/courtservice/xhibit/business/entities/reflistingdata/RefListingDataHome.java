package uk.gov.courtservice.xhibit.business.entities.reflistingdata;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;


public interface RefListingDataHome extends javax.ejb.EJBLocalHome
{
	public RefListingData create(String refDataType, String refDataValue, String userDisplayName)
			throws CreateException;

	public RefListingData findByPrimaryKey(Integer pk) 
			throws FinderException;

	public Collection<RefListingData> findByRefDataType(String refDataType) 
			throws FinderException;

	public Collection<RefListingData> findByRefDataTypeAndDataValue(String refDataType, String refDataValue) 
			throws FinderException;
}
