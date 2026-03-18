package uk.gov.courtservice.xhibit.business.entities.legalaidamendment;

import java.util.Date;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrder;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface LegalAidAmendment extends CSEntityLocal {
	
	// class constants (interface variables are implicitly public static final)
	String AMENDMENT_TYPE_SOLICITOR = "SOLICITOR";
	String AMENDMENT_TYPE_COUNSEL = "COUNSEL";
    
   public Integer getLegalAidAmendmentId(  ) ;
   public void setLegalAidAmendmentId( Integer legalAidAmendmentId ) ;

   public Integer getLegalAidOrderId(  ) ;
   public void setLegalAidOrderId(Integer legalAidOrderId);

   public Date getAmendmentDate(  ) ;
   public void setAmendmentDate( Date amendmentDate ) ;

   public String getCreatedBy(  ) ;
   public void setCreatedBy( String createdBy ) ;

   public String getLastUpdatedBy(  ) ;
   public void setLastUpdatedBy(String lastUpdatedBy ) ;

   public String getObsInd(  ) ;
   public void setObsInd(String obsInd ) ;

   public String getAmendmentType(  ) ;  
   public void setAmendmentType(String amendmentType ) ;
   
   public LegalAidOrder getLegalAidOrder(  ) ;
   public void setLegalAidOrder(LegalAidOrder legalAidOrder ) ;
   
    
}
