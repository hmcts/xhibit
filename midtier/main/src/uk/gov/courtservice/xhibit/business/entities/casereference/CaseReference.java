package uk.gov.courtservice.xhibit.business.entities.casereference;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;

public interface CaseReference extends CSEntityLocal {
    public Integer getCaseReferenceId();

    public void setReportingRestrictions(Integer reportingRestrictions);

    public Integer getReportingRestrictions();

    public Integer getCaseId();

    public void setCaze(Case caze);

    public Case getCaze();

}