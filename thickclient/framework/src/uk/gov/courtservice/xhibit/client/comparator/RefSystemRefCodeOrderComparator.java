package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;


/**
 * Used to sort Ref System Codes by Ref Code Order
 * 
 * @author waltersn
 *
 */
public class RefSystemRefCodeOrderComparator implements Comparator<RefSystemCodeBasicValue>{

	@Override
	public int compare(RefSystemCodeBasicValue o1, RefSystemCodeBasicValue o2) {
		Integer i1 = o1.getRefCodeOrder();
		Integer i2 = o2.getRefCodeOrder();
		
		// Deal with null values.
		if ( i1 == null ) i1 = 0;
		if ( i2 == null ) i2 = 0;
		
		return i1.compareTo(i2);
	}

}
