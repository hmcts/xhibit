package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.refsolicitorfirm.RefSolicitorFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.search.SearchCardStack;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchResults;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Xhibitit Search class to contain Reference Data specific functionality by
 * subclassing standard XHIBITSearch class.
 * 
 * @author grewalg
 *
 */
public class SolicitorFirmSearch extends RefSearch {

	private static final long serialVersionUID = 8843326619352184397L;
	private static final Logger log = CSServices.getLogger(SolicitorFirmSearch.class);

	public SolicitorFirmSearch(AbstractSearchAction anOpenSearchAction, XhibitApplicationController xac) {
		super(anOpenSearchAction, xac);
	}

	@Override
	public void jbInit(AbstractSearchAction anOpenSearchAction) {
		Vector xHIBITSearchSpecifications = (Vector) anOpenSearchAction.getSearchSpecification();
		if (isInternalDebug()) {
			log.debug("Got xHIBITSearchSpecifications:" + xHIBITSearchSpecifications);
		}

		Iterator xHIBITSearchSpecificationIterator = xHIBITSearchSpecifications.iterator();
		while (xHIBITSearchSpecificationIterator.hasNext()) {
			Object xHIBITSearchSpecification = xHIBITSearchSpecificationIterator.next();
			if (isInternalDebug())
				log.debug("Analysing specifcation of class: " + xHIBITSearchSpecification);

			if (xHIBITSearchSpecification instanceof XHIBITSearchCriteria) {
				xsCriteria = (XHIBITSearchCriteria) xHIBITSearchSpecification;
				xsCriteria.setCriteria();
				xsCriteriaPanel = new SolicitorFirmSearchCriteriaPanel(xsCriteria, this);
				if (isInternalDebug()) {
					log.debug("created xsCriteriaPanel " + xsCriteriaPanel);
				}
			} else if (xHIBITSearchSpecification instanceof XHIBITSearchResults) {
				xsResults = (XHIBITSearchResults) xHIBITSearchSpecification;
				xsResults.setResultFields();

				xsResultsPanel = new SolicitorFirmSearchResultsPanel(xsResults, this);

				if (isInternalDebug()) {
					log.debug("created xsResultsPanel " + xsResultsPanel);
				}
			} else if (xHIBITSearchSpecification instanceof XHIBITSearchDetails) {
				xsDetails = (XHIBITSearchDetails) xHIBITSearchSpecification;
				xsDetailsPanel = new SolicitorFirmSearchUpdatePanel(xsDetails, this);
				if (isInternalDebug())
					log.debug("created xsDetailsPanel " + xsDetailsPanel);
			}
		}
		setTitle(XHIBITConstant.getResource(XhibitBundles.XhibitSearch, "xs.gen.name"));
		setResizable(true);
		
		// Hide the OK button
		buttonPanel = (OkCancelPanel) getButtonPanel();
		buttonPanel.okButton.setVisible(false);

		panelStack = new SearchCardStack(new XPanel[] { xsCriteriaPanel, xsResultsPanel, xsDetailsPanel });
		addBodyPanel(panelStack);
		pack();

		panelStack.show(this.xsCriteriaPanel);
		setVisible(true);
	}

	@Override
	protected void doSearch() {
		super.doSearch();
	}

	@Override
	protected void showXSDetailsPanel(CSValueObject valueObject) {
		boolean isUpdate = false;
		try {
			if (valueObject != null) {
				if (hasOneResult() || valueObject.getId() > 0) {
					isUpdate = true;
				}
				RefSolicitorFirmComplexValue result = new RefSolicitorFirmComplexValue();
				if (isUpdate) {
					RefSolicitorFirmControllerBeanBusinessDelegate refSolDelegate = XhibitDelegateHelper.getRefSolicitorFirmController();
					result = refSolDelegate.findByPK(((RefSolicitorFirmComplexValue) valueObject).getId());
				}
				
				updateDialog = new SolicitorFirmUpdateDialog(getXAC(), xsDetailsPanel, isUpdate);
				((RefSearchUpdatePanel) xsDetailsPanel).createUpdatePanel(updateDialog, isUpdate);
				super.showXSDetailsPanel(result);
	            ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(false);
				updateDialog.setVisible(true);
				try {
					//if the results pane is visible we need to redo the search so that it removes stuff we may have deleted/updated that doesn't fit the category anymore
					if(this.xsResultsPanel.isVisible()){
						doSearch();
					}
				} catch (Exception e) {
					CSRecoverableException csre = new CSRecoverableException("gui.user.search.genericFailedMessage",
							"myParentSeachControl.doSearch() returned exception to XHIBITSearchCriteriaPanel's SearcAction.",
							e);
					throw csre;
				}
			}
		} catch (CSRecoverableException e) {
			XHIBITConstant.handleError(e, this.getClass());
		}
	}

	@Override
	public void showXSResultsPanelAgain() {
		super.showXSResultsPanelAgain();
        ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(false);
		updateDialog.dispose();
	}

	protected void showAddSolicitorFirmPanel(CSValueObject valueObject) {
		try {
			RefSolicitorFirmComplexValue result = new RefSolicitorFirmComplexValue();

			updateDialog = new SolicitorFirmUpdateDialog(getXAC(), xsDetailsPanel, false);
			((RefSearchUpdatePanel) xsDetailsPanel).createUpdatePanel(updateDialog, false);
			super.showXSDetailsPanel(result);
            ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(false);
			updateDialog.setVisible(true);
		} catch (CSRecoverableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the updateDialog
	 */
	public XDialog getUpdateDialog() {
		return updateDialog;
	}
}
