package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DarRetentionPolicyHome extends javax.ejb.EJBLocalHome {
    public DarRetentionPolicy create(Integer darRetentionPolicyId, Integer disposal2Id,
			Integer caseId, Integer defendantOnCaseId, Integer defendantOnOffenceId,
			Integer refDispRetentionPolicyId, Integer refDarRetentionPolicyId,
			Integer durationDays, Integer durationMonths, Integer durationYears,
			String hasLife, String isConsecutive, String isUpdated, String obsInd, 
			String userDisplayName) throws CreateException;

    public DarRetentionPolicy findByPrimaryKey(Integer id) throws FinderException;
    
    public Collection<DarRetentionPolicy> findByDisposal2Id(Integer disposal2Id) throws FinderException;
    public Collection<DarRetentionPolicy> findByCaseId(Integer caseId) throws FinderException;
}