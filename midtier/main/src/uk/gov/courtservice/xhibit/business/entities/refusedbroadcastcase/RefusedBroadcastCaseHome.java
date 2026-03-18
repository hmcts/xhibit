package uk.gov.courtservice.xhibit.business.entities.refusedbroadcastcase;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;

public interface RefusedBroadcastCaseHome extends javax.ejb.EJBLocalHome {
    public RefusedBroadcastCase create(Case thisCase, Integer teleAppRefusedReasonId, String userDisplayName) throws CreateException;

    public RefusedBroadcastCase findByPrimaryKey(Integer refusedBroadcastCaseId) throws FinderException;
    
    public java.util.Collection<RefusedBroadcastCase> findByCaseId(Integer caseId) throws FinderException;
}