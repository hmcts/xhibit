package uk.gov.courtservice.xhibit.business.entities.casereference;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface CaseReferenceHome extends EJBLocalHome {
    public CaseReference create(Integer reportingRestrictions, String userDisplayName) throws CreateException;

    public CaseReference findByPrimaryKey(Integer caseReferenceId) throws FinderException;

    public CaseReference findByCaseId(Integer caseId) throws FinderException;
}