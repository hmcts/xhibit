package uk.gov.courtservice.xhibit.client.listings.createlist;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;

public class CreateListOptionsModel {
    
    public static interface Action {
    	public static final String OPEN = "Open";
    	public static final String CREATE = "Create";
    	public static final String CANCEL = "Cancel";
    }
	
	private ListTypeEnum listType;
	private Date startDate;
	private Date endDate;
	private ListBasicValue previousDailyList;
	private ListBasicValue previousFirmList;
	private ListBasicValue previousWarnList;
	private String action = Action.CANCEL; 
	private Integer selectedListId;

	public CreateListOptionsModel(ListTypeEnum listType, Date startDate, Date endDate) {
		setListType(listType);
		setStartDate(startDate);
		setEndDate(endDate);
		clearmodel();
	}

	public void clearmodel() {
		setSelectedListId(null);
        setPreviousDailyList(null);
        setPreviousFirmList(null);
        setPreviousWarnList(null);
	}

	public ListTypeEnum getListType() {
		return listType;
	}

	private void setListType(ListTypeEnum listType) {
		this.listType = listType;
	}
	
	public ListBasicValue getPreviousDailyList() {
		return previousDailyList;
	}

	public void setPreviousDailyList(ListBasicValue previousDailyList) {
		this.previousDailyList = previousDailyList;
	}

	public ListBasicValue getPreviousFirmList() {
		return previousFirmList;
	}

	public void setPreviousFirmList(ListBasicValue previousFirmList) {
		this.previousFirmList = previousFirmList;
	}

	public ListBasicValue getPreviousWarnList() {
		return previousWarnList;
	}

	public void setPreviousWarnList(ListBasicValue previousWarnList) {
		this.previousWarnList = previousWarnList;
	}

	public boolean isDailyListForDate() {
		return getListType().isDaily() && getPreviousDailyList() != null
				&& getStartDate().equals(getPreviousDailyList().getListStartDate());
	}
	
	public boolean isRadioButtonDisplay() {
		return getListType().isDaily() &&!isDailyListForDate();
	}
	
	public boolean isOptional() {
		if (getListType().isDaily()) {
			return (isDailyListForDate() ||
					getPreviousDailyList() != null || 
					getPreviousFirmList() != null);
		} else if (getListType().isFirm()) {
			return getPreviousFirmList() != null;
		} else if (getListType().isWarned()) {
			return getPreviousWarnList() != null;
		}
		return false;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getSelectedListId() {
		return selectedListId;
	}

	public void setSelectedListId(Integer selectedListId) {
		this.selectedListId = selectedListId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
