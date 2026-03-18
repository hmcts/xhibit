package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;

/**
 * Used to sort monitoring category in alphabetical order
 * 
 * @author waltersn
 *
 */
public class MonitoringCategoryComparator implements Comparator<RefMonitoringCategoryBasicValue>{

	@Override
	public int compare(RefMonitoringCategoryBasicValue o1, RefMonitoringCategoryBasicValue o2) {
		// TODO Auto-generated method stub
		return o1.getMonitoringCategoryName().compareTo(o2.getMonitoringCategoryName());
	}

}
