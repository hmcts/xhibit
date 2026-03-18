package uk.gov.courtservice.xhibit.business.services.darts;

import java.util.Map;

import uk.gov.courtservice.framework.util.DateTimeUtilities;

/**
 * <p>
 * Title: DartsRetentionCalcHelper
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2023
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class DartsRetentionCalcHelper {
	//private static final Logger LOG = CSServices.getLogger(DartsRetentionCalcHelper.class);

	public static Integer[] calculateDurations(Map<String,Integer> durationMap) {
		Integer[] daysMonthsYears = new Integer[] {0,0,0};
		if (durationMap != null){
			for (String durationUnit : durationMap.keySet()) {
				Integer durationValue = durationMap.get(durationUnit);
				daysMonthsYears = DateTimeUtilities.getDuration(durationValue, durationUnit, daysMonthsYears);
			}
		}
		return daysMonthsYears;
	}
	
	public static Integer getDurationInDays(Integer[] duration) {
		Integer yearsInDays = duration[2]*365;
		Integer monthsInDays = duration[1]*12;
		Integer days = duration[0];
		return days + monthsInDays + yearsInDays;
	}
	
	public static Integer getDurationInDays(Map<String,Integer> durationMap) {
		Integer[] duration = calculateDurations(durationMap);
		
		Integer yearsInDays = duration[2]*365;
		Integer monthsInDays = duration[1]*12;
		Integer days = duration[0];
		return days + monthsInDays + yearsInDays;
	}
}