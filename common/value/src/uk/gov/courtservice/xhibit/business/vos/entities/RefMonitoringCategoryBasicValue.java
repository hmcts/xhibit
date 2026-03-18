package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

import javax.ejb.CreateException;

/**
 * RefMonitoringCategoryBasicValue.
 * 
 */
public class RefMonitoringCategoryBasicValue extends CSAbstractValue {
	
	   private java.lang.Integer refMonitoringCategoryId;
	   private java.lang.String monitoringCategoryCode;
	   private java.lang.String monitoringCategoryName;
	   private java.lang.String createdBy;
	   private java.lang.String lastUpdatedBy;
	   private java.util.Date creationDate;
	   private java.util.Date lastUpdateDate;
	   private java.lang.Integer version;


    /**
     * Default constructor.
     */
    public RefMonitoringCategoryBasicValue() {
    }

    public java.lang.Integer getPrimaryKey() {
        return  getRefMonitoringCategoryId();
      }

      public java.lang.Integer getRefMonitoringCategoryId()
      {
         return this.refMonitoringCategoryId;
      }
      public void setRefMonitoringCategoryId( java.lang.Integer refMonitoringCategoryId )
      {
         this.refMonitoringCategoryId = refMonitoringCategoryId;
      }

      public java.lang.String getMonitoringCategoryCode()
      {
         return this.monitoringCategoryCode;
      }
      public void setMonitoringCategoryCode( java.lang.String monitoringCategoryCode )
      {
         this.monitoringCategoryCode = monitoringCategoryCode;
      }

      public java.lang.String getMonitoringCategoryName()
      {
         return this.monitoringCategoryName;
      }
      public void setMonitoringCategoryName( java.lang.String monitoringCategoryName )
      {
         this.monitoringCategoryName = monitoringCategoryName;
      }

      public java.lang.String getCreatedBy()
      {
         return this.createdBy;
      }
      public void setCreatedBy( java.lang.String createdBy )
      {
         this.createdBy = createdBy;
      }

      public java.lang.String getLastUpdatedBy()
      {
         return this.lastUpdatedBy;
      }
      public void setLastUpdatedBy( java.lang.String lastUpdatedBy )
      {
         this.lastUpdatedBy = lastUpdatedBy;
      }

      public java.util.Date getCreationDate()
      {
         return this.creationDate;
      }
      public void setCreationDate( java.util.Date creationDate )
      {
         this.creationDate = creationDate;
      }

      public java.util.Date getLastUpdateDate()
      {
         return this.lastUpdateDate;
      }
      public void setLastUpdateDate( java.util.Date lastUpdateDate )
      {
         this.lastUpdateDate = lastUpdateDate;
      }

      public java.lang.Integer getVersion()
      {
         return this.version;
      }
      public void setVersion( java.lang.Integer version )
      {
         this.version = version;
      }



}
