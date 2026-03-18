
package uk.gov.courtservice.xhibit.business.services.darts;

import java.util.HashMap;
import java.util.Map;

public class DartsCaseData {
	
	protected static final String NO = "N";
	
	Integer refDarRetentionPolicyId;
	Map<Integer,DartsDefendantTotals> defendantTotalsMap = new HashMap<Integer, DartsDefendantTotals>();
	Map<String, Integer> durationMap = new HashMap<String,Integer>();
	String hasLife = NO;
	String isConsecutive = NO;

	DartsCaseData() {
		
	}
	
	public void resetPolicyId() {
		this.refDarRetentionPolicyId = null;
	}
	
	public void resetDefendantTotals() {
		this.defendantTotalsMap = new HashMap<Integer, DartsDefendantTotals>();
	}
}