package uk.gov.courtservice.xhibit.client.listings.list.outline;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;

/**
 * Model class for data required for a case in the outline control.
 * 
 * @author uphillj
 *
 */
public class CaseTreeNodeModel extends AbstractTreeNodeModel {

	private static final long serialVersionUID = 1L;
	
	private CaseOnListComplexValue caseOnList;
	
	public CaseOnListComplexValue getCaseOnList() {
		return caseOnList;
	}

	public void setCaseOnList(CaseOnListComplexValue caseOnList) {
		this.caseOnList = caseOnList;
	}

	@Override
	public String getDisplayName() {
		final String emptyString = "";
		if (caseOnList != null && caseOnList.getCase() != null) {
			String caseType = caseOnList.getCase().getCaseType() == null ? emptyString : caseOnList.getCase().getCaseType();
			String caseNumber = caseOnList.getCase().getCaseNumber() == null ? emptyString : caseOnList.getCase().getCaseNumber().toString();
			return caseType+caseNumber;
		}
		return emptyString;
	}
	
}
