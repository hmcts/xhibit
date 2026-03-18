package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefDispRetentionPolicyHome extends javax.ejb.EJBLocalHome {
    public RefDispRetentionPolicy create(Integer refDispRetentionPolicyId, String disposalCode,
			Integer refDarRetentionPolicyId, String allowsConsecutive,
			String hasLife, String hasDuration,
			String userDisplayName) throws CreateException;

    public RefDispRetentionPolicy findByPrimaryKey(Integer id) throws FinderException;

	public Collection<RefDispRetentionPolicy> findByDisposalCode(String disposalCode)  throws FinderException;
}