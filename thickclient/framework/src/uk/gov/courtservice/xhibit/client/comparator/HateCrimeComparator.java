package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.RefHateSentencingTypeBasicValue;



/**
 * Used to sort received from dropdown based on their name
 * 
 * @author waltersn
 *
 */
public class HateCrimeComparator implements Comparator<RefHateSentencingTypeBasicValue>{

	@Override
	public int compare(RefHateSentencingTypeBasicValue o1, RefHateSentencingTypeBasicValue o2) {
		// TODO Auto-generated method stub
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
