package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search.SolicitorFirmSearch;
import uk.gov.courtservice.xhibit.client.casemanagement.privaterep.PrivateRepresentationPanel;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationPanel;
import uk.gov.courtservice.xhibit.client.counselfacilities.AddLegalRepresentativePanel;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.solicitorfirm.SearchSolicitorFirmCriteria;
import uk.gov.courtservice.xhibit.client.search.solicitorfirm.SearchSolicitorFirmDetails;
import uk.gov.courtservice.xhibit.client.search.solicitorfirm.SearchSolicitorFirmResults;
import uk.gov.courtservice.xhibit.client.search.solicitorfirm.SearchSolicitorFirmUpdate;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author unascribed
 * @version 1.0
 */

public class OpenSearchSolicitorFirmAction extends AbstractSearchAction {

	public OpenSearchSolicitorFirmAction() {
		super();
		populateFromBundle(XhibitActions.OpenSearchSolicitorFirm);
	}

	public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
		XhibitApplicationController xac;

		xac = (XhibitApplicationController) getController();

		addSearchSpecification(new SearchSolicitorFirmCriteria());
		addSearchSpecification(new SearchSolicitorFirmResults());

		if (this.getCaller() != null) {
			XHIBITConstant.debug("OpenSearchSolicitorFirmAction: caller object present");
			Class callerClass = this.getCaller().getClass();
			log.debug("OpenSearchSolicitorFirmAction: caller class is : " + callerClass);
			if (callerClass.isAssignableFrom(AddLegalRepresentativePanel.class)) {
				addSearchSpecification(new SearchSolicitorFirmDetails());
				new XHIBITSearch(this);
				log.debug("OpenSearchSolicitorFirmAction: caller is AddLegalRepresentativePanel");
				((AddLegalRepresentativePanel) this.getCaller()).processSearchSolicitorFirm(this);
			} else if (this.getCaller() instanceof PrivateRepresentationPanel) {
				addSearchSpecification(new SearchSolicitorFirmDetails());
				new XHIBITSearch(this);
				log.debug("OpenSearchSolicitorFirmAction: caller is PrivateRepresentationPanel");
				((PrivateRepresentationPanel) this.getCaller()).processSearchSolicitorFirm(this);
			} else if (this.getCaller() instanceof PublicRepresentationPanel) {
				addSearchSpecification(new SearchSolicitorFirmDetails());
				new XHIBITSearch(this);
				log.debug("OpenSearchSolicitorFirmAction: caller is Public representation");
				((PublicRepresentationPanel) this.getCaller()).processSearchSolicitorFirm(this);
			} else {
				log.debug("OpenSearchSolicitorFirmAction: caller is SolicitorFirmDetailsAction");
				addSearchSpecification(new SearchSolicitorFirmUpdate());
				new SolicitorFirmSearch(this, xac);
			}
		}
	}
}
