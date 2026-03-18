package uk.gov.courtservice.xhibit.business.entities.listing;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Case Listing Entry bean.
 */
abstract public class CaseListingEntryBean extends CSEntityBean implements EntityBean {
	
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer caseId, Integer refJudgeTypeId, 
			Integer courtId, Integer courtSiteId, Integer judgeId, String obsInd, String userDisplayName) throws CreateException {
		setCaseId(caseId); 
		setRefJudgeTypeId(refJudgeTypeId);
		setCourtId(courtId);
		setCourtSiteId(courtSiteId);
		setJudgeId(judgeId);
		setObsInd(obsInd);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
		return null;
	}
    
    public void ejbPostCreate(Integer caseId, Integer refJudgeTypeId, 
    		Integer courtId, Integer courtSiteId, Integer judgeId,  String obsInd, String userDisplayName) throws CreateException {
    }
	
    public abstract Integer getCaseListingEntryId();
    public abstract Integer getCaseId();
    public abstract Integer getRefJudgeTypeId();
    public abstract Integer getCourtId();
    public abstract Integer getCourtSiteId();    
    public abstract Integer getJudgeId();   
    public abstract String getObsInd();
    public abstract void setCaseListingEntryId(Integer caseListingEntryId);
    public abstract void setCaseId(Integer caseId);
    public abstract void setRefJudgeTypeId(Integer refJudgeTypeId);
    public abstract void setCourtId(Integer courtId);
    public abstract void setCourtSiteId(Integer courtSiteId);
    public abstract void setJudgeId(Integer judgeId);
    public abstract void setObsInd(String ObsInd);
}