package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Darts Retention Policy Complex Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2021
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class DarRetentionPolicyComplexValue extends DarRetentionPolicyBasicValue {

	private static final long serialVersionUID = 1L;
	
	private RefDarRetentionPoliciesBasicValue refDarRetentionPoliciesBasicValue;
	
    public DarRetentionPolicyComplexValue() {
    }
    
    public DarRetentionPolicyComplexValue(Integer id, Integer version) {
        super(id, version);
    }
	
	public RefDarRetentionPoliciesBasicValue getRefDarRetentionPoliciesBasicValue() {
		return refDarRetentionPoliciesBasicValue;
	}

	public void setRefDarRetentionPoliciesBasicValue(RefDarRetentionPoliciesBasicValue refDarRetentionPoliciesBasicValue) {
		this.refDarRetentionPoliciesBasicValue = refDarRetentionPoliciesBasicValue;
	}
}