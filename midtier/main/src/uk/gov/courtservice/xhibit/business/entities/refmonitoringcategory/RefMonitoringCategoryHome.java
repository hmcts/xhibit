package uk.gov.courtservice.xhibit.business.entities.refmonitoringcategory;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;


public interface RefMonitoringCategoryHome extends javax.ejb.EJBLocalHome
{
	   public RefMonitoringCategory create(Integer refMonitoringCategoryId, String monitoringCategoryCode, String monitoringCategoryName, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version)
			      throws CreateException;


			   public RefMonitoringCategory findByPrimaryKey(Integer pk)
			      throws FinderException;

}
