package uk.gov.courtservice.xhibit.business.entities.refmonitoringcategory;

import java.util.Date;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefMonitoringCategoryBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer refMonitoringCategoryId, String monitoringCategoryCode, String monitoringCategoryName, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version) throws CreateException {

		setRefMonitoringCategoryId(refMonitoringCategoryId);
		setMonitoringCategoryCode(monitoringCategoryCode);
		setMonitoringCategoryName(monitoringCategoryName);
        setCreationDate(creationDate);
        setCreatedBy(createdBy);
        setLastUpdatedBy(lastUpdatedBy);
        setVersion(version);
        return null;
    }

    public void ejbPostCreate(Integer refMonitoringCategoryId, String monitoringCategoryCode, String monitoringCategoryName, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version) throws CreateException {
    }
    

    public abstract Integer getRefMonitoringCategoryId(  ) ;
	   public abstract void setRefMonitoringCategoryId( Integer refMonitoringCategoryId ) ;

	   public abstract String getMonitoringCategoryCode(  ) ;
	   public abstract void setMonitoringCategoryCode( String monitoringCategoryCode ) ;

	   public abstract String getMonitoringCategoryName(  ) ;
	   public abstract void setMonitoringCategoryName( String monitoringCategoryName ) ;

	   public abstract String getCreatedBy(  ) ;
	   public abstract void setCreatedBy( String createdBy ) ;

	   public abstract String getLastUpdatedBy(  ) ;
	   public abstract void setLastUpdatedBy( String lastUpdatedBy ) ;

	   public abstract Date getCreationDate(  ) ;
	   public abstract void setCreationDate( Date creationDate ) ;

	   public abstract Date getLastUpdateDate(  ) ;
	   public abstract void setLastUpdateDate( Date lastUpdateDate ) ;
	   
	   public abstract Integer getVersion(  ) ;
	   public abstract void setVersion( Integer version ) ;

	
}
