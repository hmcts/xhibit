package uk.gov.courtservice.xhibit.business.entities.refhatesentencingtype;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

public interface RefHateSentencingType extends CSEntityLocal {
	
	   
	   public java.lang.Integer getRefHateSentencingTypeId(  ) ;
	   public void setRefHateSentencingTypeId( java.lang.Integer refHateSentencingTypeId ) ;

	   public java.lang.String getHateSentType(  ) ;
	   public void setHateSentType( java.lang.String hateSentType ) ;

	   public java.lang.String getTitle(  ) ;
	   public void setTitle( java.lang.String title ) ;

	   public java.lang.String getDescription(  ) ;
	   public void setDescription( java.lang.String description ) ;

	   public java.lang.String getCjsQualifier(  ) ;
	   public void setCjsQualifier( java.lang.String cjsQualifier ) ;

	   public java.lang.String getObsInd(  ) ;
	   public void setObsInd( java.lang.String obsInd ) ;
	   
	   public Integer getCourtId();
	    public void setCourtId(Integer courtId);
	    
	   public Court getCourt();
       public void setCourt(Court courtId);
       
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
