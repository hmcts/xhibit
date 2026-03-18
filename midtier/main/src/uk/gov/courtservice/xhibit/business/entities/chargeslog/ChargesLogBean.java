package uk.gov.courtservice.xhibit.business.entities.chargeslog;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ChargesLogBean extends CSEntityBean {

    public Integer ejbCreate(Integer caseId, Integer sequenceNo, String chargesInfo, String userDisplayName) throws CreateException {
        setCaseId(caseId);
        setSequenceNo(sequenceNo);
		setChargesInfo(chargesInfo);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Integer caseId, Integer sequenceNo, String chargesInfo, String userDisplayName) throws CreateException {
    }

  public abstract Integer getChargesLogId();

  public abstract void setChargesLogId(Integer chargesLogId);

  public abstract Integer getCaseId();

  public abstract void setCaseId(Integer caseId);

  public abstract Integer getSequenceNo();

  public abstract void setSequenceNo(Integer sequenceNo);

  public abstract String getChargesInfo();

  public abstract void setChargesInfo(String chargesInfo);

  public abstract String getObsInd();

  public abstract void setObsInd(String obsInd);

  public abstract String getCreatedBy();

  public abstract void setCreatedBy(String createdBy);

  public abstract String getLastUpdatedBy();

  public abstract void setLastUpdatedBy(String lastUpdatedBy);

  public abstract Integer getVersion();

  public abstract void setVersion(Integer version);

}