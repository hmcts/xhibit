package uk.gov.courtservice.xhibit.business.entities.defendantonoffence;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DefendantOnOffenceHome extends javax.ejb.EJBLocalHome {

	public DefendantOnOffence create(Integer defendantOnOffenceId, String userDisplayName) throws CreateException;
	 
    public DefendantOnOffence findByPrimaryKey(Integer id) throws FinderException;  
}
