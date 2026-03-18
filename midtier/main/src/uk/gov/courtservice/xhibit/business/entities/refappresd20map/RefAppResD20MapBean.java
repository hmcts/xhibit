package uk.gov.courtservice.xhibit.business.entities.refappresd20map;

import java.util.Date;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefAppResD20MapBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer refAppResD20MapId, String appResultCode, String d20Result, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version) throws CreateException {

		setRefAppResD20MapId(refAppResD20MapId);
		setAppResultCode(appResultCode);
		setD20Result(d20Result);
        setCreationDate(creationDate);
        setCreatedBy(createdBy);
        setLastUpdatedBy(lastUpdatedBy);
        setVersion(version);
        return null;
    }

    public void ejbPostCreate(Integer refAppResD20MapId, String appResultCode, String d20Result, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version) throws CreateException {
    }
    

    public abstract Integer getRefAppResD20MapId(  ) ;
	   public abstract void setRefAppResD20MapId( Integer refAppResD20MapId ) ;

	   public abstract String getAppResultCode(  ) ;
	   public abstract void setAppResultCode( String appResultCode ) ;

	   public abstract String getD20Result(  ) ;
	   public abstract void setD20Result( String d20Result ) ;

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
	   
	   public abstract String getObsInd(  ) ;
	   public abstract void setObsInd( String obsInd ) ;

	
}
