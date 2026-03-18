package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.search.SearchCardStack;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchResults;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Xhibit Search class to contain Reference Data specific functionality by
 * extending XHIBITSearch class.
 * 
 * @author grewalg
 *
 */
public class JudgeSearch extends RefSearch {

	private static final long serialVersionUID = 8843326619352184397L;

	public JudgeSearch(AbstractSearchAction anOpenSearchAction, XhibitApplicationController xac) {
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
				xsCriteriaPanel = new JudgeSearchCriteriaPanel(xsCriteria, this);
				if (isInternalDebug()) {
					log.debug("created xsCriteriaPanel " + xsCriteriaPanel);
				}
			} else if (xHIBITSearchSpecification instanceof XHIBITSearchResults) {
				xsResults = (XHIBITSearchResults) xHIBITSearchSpecification;
				xsResults.setResultFields();

				xsResultsPanel = new JudgeSearchResultsPanel(xsResults, this);

				if (isInternalDebug()) {
					log.debug("created xsResultsPanel " + xsResultsPanel);
				}
			} else if (xHIBITSearchSpecification instanceof XHIBITSearchDetails) {
				xsDetails = (XHIBITSearchDetails) xHIBITSearchSpecification;
				xsDetailsPanel = new JudgeSearchUpdatePanel(xsDetails, this);
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
	public void showXSDetailsPanel(CSValueObject valueObject) {
		try {
			boolean isUpdate = false;

			// If one result found then we need to set the update Flag so the
			// Details screen is entered instead of the Add New Judge screen
			if (valueObject != null) {
				if (hasOneResult() || valueObject.getId() > 0) {
					isUpdate = true;
				}
				RefJudgeComplexValue result = new RefJudgeComplexValue();

				if (isUpdate) {
					BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
					result = bizRefDelegate.findJudgeById(((RefJudgeBasicValue) valueObject).getId());
				}
				updateDialog = new JudgeUpdateDialog(getXAC(), xsDetailsPanel, isUpdate);
				((RefSearchUpdatePanel) xsDetailsPanel).createUpdatePanel(updateDialog, isUpdate);
				super.showXSDetailsPanel(result);
	            ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(false);
				updateDialog.setVisible(true);
			}
		} catch (CSRecoverableException e) {
			XHIBITConstant.handleError(e);
		}
	}

	protected void showAddJudgePanel(CSValueObject valueObject) {
		try {
			RefJudgeComplexValue result = new RefJudgeComplexValue();

			updateDialog = new JudgeUpdateDialog(getXAC(), xsDetailsPanel, false);
			((RefSearchUpdatePanel) xsDetailsPanel).createUpdatePanel(updateDialog, false);
			super.showXSDetailsPanel(result);
            ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(false);
			updateDialog.setVisible(true);
		} catch (CSRecoverableException e) {
			e.printStackTrace();
		}
	}
}
