package uk.gov.courtservice.xhibit.business.vos.services.listing;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;


/**
* <p>
* Title: CreateListOptionsValue
* </p>
* <p>
* Description: Get all the applicable lists to decide if the options screen is required 
* </p>
* <p>
* Company: CGI
* </p>
* 
* @author Mark Harris 
* @version 1.0
*/

public class CreateListOptionsValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 1L;

	public static final String DAILY = "Daily";
	public static final String FIRM = "Firm";
	public static final String WARNED = "Warned";
	
	private String listType;
	private ListBasicValue previousDailyList;
	private ListBasicValue previousFirmList;
	private ListBasicValue previousWarnList;
	
	public CreateListOptionsValue(String listType){
		setListType(listType);
	}		

	public String getListType() {
		return listType;
	}

	private void setListType(String listType) {
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
	
	public boolean isDaily() {
		return DAILY.equals(getListType());
	}
	
	public boolean isFirm() {
		return FIRM.equals(getListType());
	}
	
	public boolean isWarned() {
		return WARNED.equals(getListType());
	}
}

