package uk.gov.courtservice.xhibit.business.entities.refusedbroadcastcase;


import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;

abstract public class RefusedBroadcastCaseBean extends CSEntityBean implements EntityBean {

    /**
	 * default serialversionuid.
	 */
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Case thisCase, Integer teleAppRefusedReasonId, String userDisplayName) throws CreateException {

        setTeleAppRefusedReasonId(teleAppRefusedReasonId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Case thisCase, Integer teleAppRefusedReasonId, String userDisplayName) throws CreateException {
    	setCaze(thisCase);
    }

    public abstract Integer getRefusedBroadcastCaseId();
	public abstract void setRefusedBroadcastCaseId(Integer refBroadcastCaseId);

	public abstract Integer getTeleAppRefusedReasonId();
	public abstract void setTeleAppRefusedReasonId(Integer teleAppRefusedReasonId);

	public abstract String getObsInd();
	public abstract void setObsInd(String obsInd);
	
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy);

	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	
	public abstract void setCaseId(Integer caseId);
    public abstract Integer getCaseId();


    // -----------------------CMR---------------------------------------------------------------------

	 public abstract void setCaze(uk.gov.courtservice.xhibit.business.entities.caze.Case caze);
	 public abstract uk.gov.courtservice.xhibit.business.entities.caze.Case getCaze();

}