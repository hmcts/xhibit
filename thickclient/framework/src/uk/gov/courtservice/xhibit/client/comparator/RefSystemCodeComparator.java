package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;


/**
 * Used to sort Ref System Codes by Code
 * 
 * @author waltersn
 *
 */
public class RefSystemCodeComparator implements Comparator<RefSystemCodeBasicValue>{

	@Override
	public int compare(RefSystemCodeBasicValue o1, RefSystemCodeBasicValue o2) {
		// TODO Auto-generated method stub
		return o1.getCode().compareTo(o2.getCode());
	}

}
