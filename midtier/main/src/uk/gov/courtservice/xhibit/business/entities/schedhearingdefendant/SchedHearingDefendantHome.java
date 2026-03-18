package uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface SchedHearingDefendantHome extends javax.ejb.EJBLocalHome {
    public SchedHearingDefendant create(Integer scheduledHearingId, Integer defOnCaseID, CSEntityLocal scheduledHearing, String userDisplayName)
            throws CreateException;

    public SchedHearingDefendant findByPrimaryKey(Integer schedHearDefId) throws FinderException;

    public Collection findByDefOnCaseId(Integer defOnCaseId) throws FinderException;

    public Collection findUnrepresentedDefendants(Integer scheduledHearingId) throws FinderException;

    public Collection findByScheduledHearingId(Integer scheduledHearingId) throws FinderException;

    public SchedHearingDefendant findBySchedHearingIdAndDefOnCaseId(Integer schedHearingDefId, Integer defOnCaseId)
            throws FinderException;
    // findByScheduledHearingId
}