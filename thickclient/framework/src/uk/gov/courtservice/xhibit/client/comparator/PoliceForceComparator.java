package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;


/**
 * Used to sort police forces in alphabetical order of their court site code
 * 
 * @author waltersn
 *
 */
public class PoliceForceComparator implements Comparator<RefSystemCodeBasicValue>{

	@Override
	public int compare(RefSystemCodeBasicValue o1, RefSystemCodeBasicValue o2) {
		// TODO Auto-generated method stub
		return o1.getDecode().compareTo(o2.getDecode());
	}

}
