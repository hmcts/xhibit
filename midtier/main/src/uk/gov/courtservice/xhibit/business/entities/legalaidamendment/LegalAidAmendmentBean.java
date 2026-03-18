package uk.gov.courtservice.xhibit.business.entities.legalaidamendment;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrder;

abstract public class LegalAidAmendmentBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(Date amendmentDate, LegalAidOrder legalAidOrder, String amendmentType,
             String userDisplayName) throws CreateException {
        setAmendmentDate(amendmentDate);
		setAmendmentType(amendmentType);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }
    
    public void ejbPostCreate(Date amendmentDate, LegalAidOrder legalAidOrder, String amendmentType,
             String userDisplayName) throws CreateException {
				 setLegalAidOrder(legalAidOrder);
    }
    
    
   public abstract LegalAidOrder getLegalAidOrder(  ) ;
   public abstract void setLegalAidOrder(LegalAidOrder legalAidOrder ) ;

    
   public abstract Integer getLegalAidAmendmentId(  ) ;
   public abstract void setLegalAidAmendmentId( Integer legalAidAmendmentId ) ;

   public abstract Integer getLegalAidOrderId(  ) ;
   public abstract void setLegalAidOrderId(Integer legalAidOrderId);

   public abstract Date getAmendmentDate(  ) ;
   public abstract void setAmendmentDate( Date amendmentDate ) ;

   public abstract String getCreatedBy(  ) ;
   public abstract void setCreatedBy( String createdBy ) ;

   public abstract String getLastUpdatedBy(  ) ;
   public abstract void setLastUpdatedBy(String lastUpdatedBy ) ;

   public abstract String getObsInd(  ) ;
   public abstract void setObsInd(String obsInd ) ;

   public abstract String getAmendmentType(  ) ;  
   public abstract void setAmendmentType(String amendmentType ) ;
   
    
}
