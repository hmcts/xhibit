package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;



/**
 * Used to sort received from dropdown based on their name
 * 
 * @author waltersn
 *
 */
public class DropdownCodeStringComparator implements Comparator<DropdownCodeStringValue>{

	@Override
	public int compare(DropdownCodeStringValue o1, DropdownCodeStringValue o2) {
		// TODO Auto-generated method stub
		return o1.getDisplayName().compareTo(o2.getDisplayName());
	}

}
