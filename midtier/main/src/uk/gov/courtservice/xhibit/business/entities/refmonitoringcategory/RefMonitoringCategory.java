package uk.gov.courtservice.xhibit.business.entities.refmonitoringcategory;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefMonitoringCategory extends CSEntityLocal {
	
	   
	   public java.lang.Integer getRefMonitoringCategoryId(  ) ;
	   public void setRefMonitoringCategoryId( java.lang.Integer refMonitoringCategoryId ) ;

	   public java.lang.String getMonitoringCategoryCode(  ) ;
	   public void setMonitoringCategoryCode( java.lang.String monitoringCategoryCode ) ;

	   public java.lang.String getMonitoringCategoryName(  ) ;
	   public void setMonitoringCategoryName( java.lang.String monitoringCategoryName ) ;

	   public java.lang.String getCreatedBy(  ) ;
	   public void setCreatedBy( java.lang.String createdBy ) ;

	   public java.lang.String getLastUpdatedBy(  ) ;
	   public void setLastUpdatedBy( java.lang.String lastUpdatedBy ) ;

	   public java.util.Date getCreationDate(  ) ;
	   public void setCreationDate( java.util.Date creationDate ) ;

	   public java.util.Date getLastUpdateDate(  ) ;
	   public void setLastUpdateDate( java.util.Date lastUpdateDate ) ;
	   
	   public java.lang.Integer getVersion(  ) ;
	   public void setVersion( java.lang.Integer version ) ;



}
