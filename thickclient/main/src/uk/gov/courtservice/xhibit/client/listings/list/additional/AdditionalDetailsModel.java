package uk.gov.courtservice.xhibit.client.listings.list.additional;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class AdditionalDetailsModel {
	private CaseOnListComplexValue caseOnListComplexValue;
	private XhibitApplicationController xac;
	private boolean dirty;
	private ListModel listModel;
	
	
	public AdditionalDetailsModel() {
		super();
	}


	public final CaseOnListComplexValue getCaseOnList() {
		return caseOnListComplexValue;
	}


	public final void setCaseOnList(CaseOnListComplexValue caseOnListComplexValue) {
		this.caseOnListComplexValue = caseOnListComplexValue;
	}


	public final XhibitApplicationController getXac() {
		return xac;
	}


	public final void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}


	public final boolean isDirty() {
		return dirty;
	}


	public final void setDirty(boolean dirty) {
		this.dirty = dirty;
	}


	public ListModel getListModel() {
		return listModel;
	}


	public void setListModel(ListModel listModel) {
		this.listModel = listModel;
	}
	
	private String getCaseType() {
		return getCaseOnList() != null && getCaseOnList().getCase() != null ? 
				getCaseOnList().getCase().getCaseType() : "";
	}
	
	public boolean isBOrUCase() {
		return "B".equals(getCaseType()) || "U".equals(getCaseType());
	}
	
}
