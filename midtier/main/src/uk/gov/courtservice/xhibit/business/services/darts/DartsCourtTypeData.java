
package uk.gov.courtservice.xhibit.business.services.darts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DartsCourtTypeData {
	Integer caseId; 
	String caseType;
	Integer highestPolicyId = null;
	DartsAppealRetentionRulesEnum applicableRuleEnum;
	List<Integer> countNoList = new ArrayList<Integer>();
	Map<Integer,List<DartsAppealRetentionRulesEnum>> appealRuleEnums;
	Map<Integer, Map<String, Integer>> countCourtTypeRefDarRetentionPolicyIdMap = new HashMap<Integer, Map<String,Integer>>();
	Map<Integer, Map<String, DartsCaseData>> countCourtTypeCaseDataMap = new HashMap<Integer, Map<String, DartsCaseData>>();
	
	DartsCourtTypeData(Integer caseId, String caseType, Map<Integer,List<DartsAppealRetentionRulesEnum>> appealRuleEnums) {
		this.caseId = caseId;
		this.caseType = caseType;
		this.appealRuleEnums = appealRuleEnums;
	}
	
	public Map<String, DartsCaseData> getCourtTypeCaseDataMap(Integer countNo) {
		if (countCourtTypeCaseDataMap.containsKey(countNo)) {
			return countCourtTypeCaseDataMap.get(countNo);
		} 
		return new HashMap<String, DartsCaseData>();
	}
	
	public DartsCaseData getCourtTypeCaseData(Integer countNo, String courtType) {
		Map<String, DartsCaseData> map = getCourtTypeCaseDataMap(countNo);
		if (map.containsKey(courtType)) {
			return map.get(courtType);
		} 
		return null;
	}
	
	public void setCourtTypeCaseData(Integer countNo, String courtType, DartsCaseData caseData) {
		if (!countCourtTypeCaseDataMap.containsKey(countNo)) {
			countCourtTypeCaseDataMap.put(countNo, new HashMap<String, DartsCaseData>());
		}
		Map<String, DartsCaseData> map = getCourtTypeCaseDataMap(countNo);
		map.put(courtType, caseData);
		countCourtTypeCaseDataMap.put(countNo, map);
	}
	
	private Map<String, Integer> getCourtTypeRefDarRetentionPolicyIdMap(Integer countNo) {
		if (countCourtTypeRefDarRetentionPolicyIdMap.containsKey(countNo)) {
			return countCourtTypeRefDarRetentionPolicyIdMap.get(countNo);
		} 
		return new HashMap<String, Integer>();
	}
	
	public Integer getCourtTypePolicyId(Integer countNo, String courtType) {
		Map<String, Integer> map = getCourtTypeRefDarRetentionPolicyIdMap(countNo);
		if (map.containsKey(courtType)) {
			return map.get(courtType);
		} 
		return null;
	}
	
	public void setCourtTypePolicyId(Integer countNo, String courtType, Integer policyId) {
		if (!countCourtTypeRefDarRetentionPolicyIdMap.containsKey(countNo)) {
			countCourtTypeRefDarRetentionPolicyIdMap.put(countNo, new HashMap<String, Integer>());
		}
		Map<String, Integer> map = getCourtTypeRefDarRetentionPolicyIdMap(countNo);
		map.put(courtType, policyId);
		countCourtTypeRefDarRetentionPolicyIdMap.put(countNo, map);
	}
	
	public boolean useAppealRules() {
		return appealRuleEnums != null && appealRuleEnums.size() > 0;
	}
	
	public void addCountNo(Integer countNo) {
		if (!countNoList.contains(countNo)) {
			countNoList.add(countNo);
		}
	}
	
	public List<Integer> getCountNoList() {
		return countNoList;
	}
	
	public String getCaseType() {
		return caseType;
	}
}