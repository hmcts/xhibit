package uk.gov.courtservice.xhibit.client.listings.list.common;

/**
 * Interface for a Case Removal Listener.
 * 
 * @author westalll
 *
 */
public interface CaseRemovalListener {
	
	/**
	 * Called when a case is removed from the list.
	 * 
	 * @param caseId
	 * @param caseDiaryFixtureId
	 * @param parentCaseOnListId
	 */
	public void actionPerformed(final Integer caseId, final Integer caseDiaryFixtureId, final Integer parentCaseOnListId);

}


