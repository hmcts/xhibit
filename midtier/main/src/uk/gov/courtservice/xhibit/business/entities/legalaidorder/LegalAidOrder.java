package uk.gov.courtservice.xhibit.business.entities.legalaidorder;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;


public interface LegalAidOrder extends CSEntityLocal {
    
    public Integer getLegalAidOrderId();
    
    public void setLegalAidOrderId(Integer legalAidOrderId);
    
    public Integer getCaseProsAgencyId();
    
    public void setCaseProsAgencyId(Integer caseProsAgencyId);
    
    public Integer getCrestLeoId();
    
    public void setCrestLeoId(Integer crestLeoId);
    
    public Integer getDefendantOnCaseId();
    
    public void setDefendantOnCaseId(Integer defendantOnCaseId);
    
    public String getGrantedBy();
    public void setGrantedBy(String grantedBy);
        
    public Date getOrderDate(  ) ;
    public void setOrderDate(Date orderDate ) ;
    
   
    public java.lang.String getPsdRoRef(  ) ;
    public void setPsdRoRef( java.lang.String psdRoRef ) ;

    public Integer getNumberOfAdvocates(  ) ;
    public void setNumberOfAdvocates(Integer numberOfAdvocates ) ;

    public Integer getNumberOfQcs(  ) ;
    public void setNumberOfQcs(Integer numberOfQcs ) ;
    
    public Date getDateOfRevocation(  ) ;
    public void setDateOfRevocation(Date dateOfRevocation ) ;

    public Integer getReasonForRevocationId(  ) ;
    public void setReasonForRevocationId(Integer reasonForRevocationId);
    
    public String getObsInd(  ) ;
    public void setObsInd(String obsInd);
	
	public java.util.Collection getLegalAidAmendments();
	public void setLegalAidAmendments(java.util.Collection legalAidAmendment);
    
}
