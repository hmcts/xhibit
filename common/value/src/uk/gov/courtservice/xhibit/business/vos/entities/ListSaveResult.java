package uk.gov.courtservice.xhibit.business.vos.entities;

import java.io.Serializable;
/**
 * A class to store the status of saving a list.
 * Optimistic Lock Errors are no longer thrown in the back end.
 * The result would be stored here.
 *
 */
public class ListSaveResult implements Serializable{
	
	private static final long serialVersionUID = 6129130126079240009L;
	private boolean success = true;
	private String returnMessage;
	private Integer listId;
	
	public final boolean isSuccess() {
		return success;
	}
	public final void setSuccess(boolean success) {
		this.success = success;
	}
	public final String getReturnMessage() {
		return returnMessage;
	}
	public final void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public Integer getListId() {
		return listId;
	}
	public void setListId(Integer listId) {
		this.listId = listId;
	}
}
