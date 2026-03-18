package uk.gov.courtservice.xhibit.business.entities.casereference;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CaseReferenceBean extends CSEntityBean {
    public java.lang.Integer ejbCreate(Integer reportingRestrictions, String userDisplayName) throws CreateException {
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setReportingRestrictions(reportingRestrictions);
        return null;
    }

    public void ejbPostCreate(Integer reportingRestrictions, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setReportingRestrictions(java.lang.Integer reportingRestrictions);

    public abstract void setCaseReferenceId(java.lang.Integer caseReferenceId);

    public abstract void setCaseId(java.lang.Integer caseId);

    public abstract java.lang.Integer getCaseReferenceId();

    public abstract java.lang.Integer getReportingRestrictions();

    public abstract java.lang.Integer getCaseId();

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setCaze(uk.gov.courtservice.xhibit.business.entities.caze.Case caze);

    public abstract uk.gov.courtservice.xhibit.business.entities.caze.Case getCaze();

}