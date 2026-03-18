
package uk.gov.courtservice.xhibit.business.services.darts;

import java.util.HashMap;
import java.util.Map;

public class DartsDefendantTotals {
	private Map<String,Integer> greatestDurationMap = new HashMap<String,Integer>();
	private Map<String,Integer> totalConsecutiveDurationMap = new HashMap<String,Integer>();
	
	public DartsDefendantTotals() {
	}
	
	public Map<String,Integer> getGreatestDurationMap() {
		return greatestDurationMap;
	}
	
	public Map<String,Integer> getTotalConsecutiveDurationMap() {
		return totalConsecutiveDurationMap;
	}
	
	public void addConsecutiveDuration(Map<String,Integer> durationMap) {
		for (String durationUnit : durationMap.keySet()) {
			Integer durationValue = durationMap.get(durationUnit);
			Integer durationValueTotal = totalConsecutiveDurationMap.get(durationUnit);
			durationValueTotal = (durationValueTotal == null ? 0 : durationValueTotal) + durationValue;
			totalConsecutiveDurationMap.put(durationUnit, durationValueTotal);
		}
	}
	
	public void addGreatestDuration(Map<String,Integer> durationMap) {
		Integer[] durations = DartsRetentionCalcHelper.calculateDurations(durationMap);
		Integer durationInDays = DartsRetentionCalcHelper.getDurationInDays(durations);
		Integer[] greatestDurations = DartsRetentionCalcHelper.calculateDurations(greatestDurationMap);
		Integer greatestDurationInDays = DartsRetentionCalcHelper.getDurationInDays(greatestDurations);
		if (greatestDurationInDays < durationInDays) {
			greatestDurationMap = durationMap;
		}
	}
}