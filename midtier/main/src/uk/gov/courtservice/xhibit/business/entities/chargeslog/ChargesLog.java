package uk.gov.courtservice.xhibit.business.entities.chargeslog;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface ChargesLog extends CSEntityLocal {
  
  public Integer getChargesLogId();

  public void setChargesLogId(Integer chargesLogId);

  public Integer getCaseId();

  public void setCaseId(Integer caseId);

  public Integer getSequenceNo();

  public void setSequenceNo(Integer sequenceNo);

  public String getChargesInfo();

  public void setChargesInfo(String chargesInfo);

  public String getObsInd();

  public void setObsInd(String obsInd);

  public String getCreatedBy();

  public void setCreatedBy(String createdBy);

  public String getLastUpdatedBy();

  public void setLastUpdatedBy(String lastUpdatedBy);

  public Integer getVersion();

  public void setVersion(Integer version);
}