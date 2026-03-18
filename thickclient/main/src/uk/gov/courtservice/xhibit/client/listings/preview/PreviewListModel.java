package uk.gov.courtservice.xhibit.client.listings.preview;

import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;

public class PreviewListModel {

	private Integer listId;
	private ListTypeEnum listTypeEnum;
	
	public PreviewListModel(Integer listId, ListTypeEnum listTypeEnum){
		this.listId = listId;
		this.setListTypeEnum(listTypeEnum);
	}

	public Integer getListID(){
		return this.listId;
	}
	
	public void setListID(Integer listId) {
		this.listId = listId;
	}

	public ListTypeEnum getListTypeEnum() {
		return listTypeEnum;
	}

	public void setListTypeEnum(ListTypeEnum listTypeEnum) {
		this.listTypeEnum = listTypeEnum;
	}
	
}