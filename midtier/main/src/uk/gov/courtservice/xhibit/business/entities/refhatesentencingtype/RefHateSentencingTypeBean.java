package uk.gov.courtservice.xhibit.business.entities.refhatesentencingtype;

import java.util.Date;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

abstract public class RefHateSentencingTypeBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(String hateSentType, String title, String description, String cjsQualifier, String obsInd) throws CreateException {

		setHateSentType(hateSentType);
		setTitle(title);
		setDescription(description);
		setCjsQualifier(cjsQualifier);
		setObsInd(obsInd);
        return null;
    }

    public void ejbPostCreate(String hateSentType, String title, String description, String cjsQualifier, String obsInd) throws CreateException {
    }
    

    public abstract java.lang.Integer getRefHateSentencingTypeId(  ) ;
	   public abstract void setRefHateSentencingTypeId( java.lang.Integer refHateSentencingTypeId ) ;

	   public abstract java.lang.String getHateSentType(  ) ;
	   public abstract void setHateSentType( java.lang.String hateSentType ) ;

	   public abstract java.lang.String getTitle(  ) ;
	   public abstract void setTitle( java.lang.String title ) ;

	   public abstract java.lang.String getDescription(  ) ;
	   public abstract void setDescription( java.lang.String description ) ;

	   public abstract java.lang.String getCjsQualifier(  ) ;
	   public abstract void setCjsQualifier( java.lang.String cjsQualifier ) ;

	   public abstract java.lang.String getObsInd(  ) ;
	   public abstract void setObsInd( java.lang.String obsInd ) ;
	   
	   public abstract Integer getCourtId();
	   public abstract void setCourtId(Integer courtId);
	    
	   public abstract Court getCourt();
       public abstract void setCourt(Court courtId);

       public abstract java.lang.String getCreatedBy(  ) ;
	   public abstract void setCreatedBy( java.lang.String createdBy ) ;

	   public abstract java.lang.String getLastUpdatedBy(  ) ;
	   public abstract void setLastUpdatedBy( java.lang.String lastUpdatedBy ) ;

	   public abstract java.util.Date getCreationDate(  ) ;
	   public abstract void setCreationDate( java.util.Date creationDate ) ;

	   public abstract java.util.Date getLastUpdateDate(  ) ;
	   public abstract void setLastUpdateDate( java.util.Date lastUpdateDate ) ;
	   
	   public abstract java.lang.Integer getVersion(  ) ;
	   public abstract void setVersion( java.lang.Integer version ) ;
	
}
