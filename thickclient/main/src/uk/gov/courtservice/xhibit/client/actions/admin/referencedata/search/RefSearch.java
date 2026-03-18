package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * 
 * @author grewalg
 *
 */
public class RefSearch extends XHIBITSearch {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5515279526480648841L;

	protected final Logger log = CSServices.getLogger(RefSearch.class);
	
	protected XDialog updateDialog;
	
	public RefSearch(AbstractSearchAction anOpenSearchAction, XhibitApplicationController xac) {
		super(anOpenSearchAction, xac, false);
	}
	
	@Override
	protected void showXSResultsPanel(Collection results) {
		// TODO Auto-generated method stub
		super.showXSResultsPanel(results);
	}

	@Override
	protected void showXSResultsPanelAgain() {
		super.showXSResultsPanelAgain();
		updateDialog.dispose();
	}
	
	@Override
	protected void doSearch() {
		super.doSearch();
	}
	
	/**
	 * @return the updateDialog
	 */
	public XDialog getUpdateDialog() {
		return updateDialog;
	}

	/**
	 * @param updateDialog the updateDialog to set
	 */
	public void setUpdateDialog(XDialog updateDialog) {
		this.updateDialog = updateDialog;
	}
}
