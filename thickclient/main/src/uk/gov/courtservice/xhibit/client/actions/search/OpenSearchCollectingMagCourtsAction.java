package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtCriteria;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtDetails;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtResults;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class OpenSearchCollectingMagCourtsAction extends AbstractSearchAction {

	public OpenSearchCollectingMagCourtsAction() {
		super();

		populateFromBundle(XhibitActions.OpenSearchCourt);
	}

	public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
		addSearchSpecification(new SearchCourtCriteria(SearchCourtCriteria.MAGISTRATES_ONLY));
		addSearchSpecification(new SearchCourtResults());
		addSearchSpecification(new SearchCourtDetails());

		XHIBITSearch xSearch = new XHIBITSearch(this);

		if (!this.isSearchCancelled()) {

			if (this.getCaller() != null) {
			}
		} else {
			log.debug("OpenSearchCourtAction was cancelled.");
		}
	}
}