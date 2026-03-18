package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefDarRetentionPoliciesHome extends javax.ejb.EJBLocalHome {
    public RefDarRetentionPolicies create(Integer refDarRetentionPolicyId, Integer policyNo, String policyDescription,
			String userDisplayName) throws CreateException;

    public RefDarRetentionPolicies findByPrimaryKey(Integer id) throws FinderException;
    
    public Collection findAllPolicies() throws FinderException;
}