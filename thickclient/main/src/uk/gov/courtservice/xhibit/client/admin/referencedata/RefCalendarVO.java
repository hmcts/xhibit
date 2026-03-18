package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;

/**
 * Class containing update list for Calendar to be passed to midtier.
 * 
 * @author grewalg
 *
 */
public class RefCalendarVO {

	private List<RefCalendarBasicValue> updateList;
	
	public RefCalendarVO() {
	}

	public List<RefCalendarBasicValue> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<RefCalendarBasicValue> updateList) {
		this.updateList = updateList;
	}
}
