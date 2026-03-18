package uk.gov.courtservice.xhibit.business.entities.legalaidorder;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;


abstract public class LegalAidOrderBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(Integer crestLeoId, Integer defendantOnCaseId, String userDisplayName)
    throws CreateException {
        setCrestLeoId(crestLeoId);
        setDefendantOnCaseId(defendantOnCaseId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }
    
    public void ejbPostCreate(
            Integer crestLeoId, 
            Integer defendantOnCaseId, String userDisplayName) throws CreateException {
    }
    
    
    /**
     * used for defendants/prosecutor in public rep
     */
    public Integer ejbCreate(Integer defendantOnCaseId, Integer caseProsAgencyId, Date orderDate, String grantedBy, String psdRoRef, Integer numberOfAdvocates, Integer numberOfQcs, Integer crestLeoId , String userDisplayName) throws CreateException {
    	setDefendantOnCaseId(defendantOnCaseId);
    	setCrestLeoId(crestLeoId);
    	setOrderDate(orderDate);
    	setGrantedBy(grantedBy);
    	setPsdRoRef(psdRoRef);
    	setNumberOfAdvocates(numberOfAdvocates);
    	setNumberOfQcs(numberOfQcs);
    	setLastUpdatedBy(userDisplayName);
    	setCreatedBy(userDisplayName);
    	setCaseProsAgencyId(caseProsAgencyId);
    	return null;
    }
    
    public void ejbPostCreate(Integer defendantOnCaseId, Integer caseProsAgencyId, Date orderDate, String grantedBy, String psdRoRef, Integer numberOfAdvocates, Integer numberOfQcs, Integer crestLeoId , String userDisplayName) throws CreateException {
    }
    
    

    
    public abstract void setLegalAidOrderId(Integer legalAidOrderId);
    
    public abstract void setCrestLeoId(Integer crestLeoId);
    
    public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
    
    public abstract Integer getLegalAidOrderId();
    
    public abstract Integer getCrestLeoId();
    
    public abstract Integer getDefendantOnCaseId();
    
    public abstract String getGrantedBy();
    public abstract void setGrantedBy(String grantedBy);
        
    public abstract Date getOrderDate(  ) ;
    public abstract void setOrderDate(Date orderDate ) ;
    
   
    public abstract java.lang.String getPsdRoRef(  ) ;
    public abstract void setPsdRoRef( java.lang.String psdRoRef ) ;

    public abstract Integer getNumberOfAdvocates(  ) ;
    public abstract void setNumberOfAdvocates(Integer numberOfAdvocates ) ;

    public abstract Integer getNumberOfQcs(  ) ;
    public abstract void setNumberOfQcs(Integer numberOfQcs ) ;
    
    public abstract Date getDateOfRevocation(  ) ;
    public abstract void setDateOfRevocation(Date dateOfRevocation ) ;

    public abstract Integer getReasonForRevocationId(  ) ;
    public abstract void setReasonForRevocationId(Integer reasonForRevocationId);
    
    public abstract String getObsInd(  ) ;
    public abstract void setObsInd(String obsInd);
	
	public abstract java.util.Collection getLegalAidAmendments();
	public abstract void setLegalAidAmendments(java.util.Collection legalAidAmendment);
	
	 public abstract Integer getCaseProsAgencyId();
	 public abstract void setCaseProsAgencyId(Integer caseProsAgencyId);
        
    
}
