package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;


/**
 * Used to sort received from dropdown based on their full names
 * 
 * @author waltersn
 *
 */
public class MagistrateCourtDetailsComparator implements Comparator<RefCourtBasicValue>{

	@Override
	public int compare(RefCourtBasicValue o1, RefCourtBasicValue o2) {
		// TODO Auto-generated method stub
		return o1.getCourtFullName().compareTo(o2.getCourtFullName());
	}

}
