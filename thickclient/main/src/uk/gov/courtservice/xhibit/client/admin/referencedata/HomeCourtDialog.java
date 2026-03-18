package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Frame;

import javax.swing.JButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;
import uk.gov.courtservice.xhibit.client.search.SearchCardStack;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class HomeCourtDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private HomeCourtPanel homeCourtPanel;
	private CourtSiteLocationPanel sitePanel;
	private HomeCourtModel homeCourtModel;
	private SearchCardStack panelStack;
	private JButton cancelButton;

	public HomeCourtDialog(Frame frame) throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.HomeCourt,
				"HomeCourt.mainTitle"), true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);

		this.homeCourtModel = new HomeCourtModel();		
		this.homeCourtPanel = new HomeCourtPanel(this, this.homeCourtModel);
		this.sitePanel = new CourtSiteLocationPanel(this, new CourtSiteLocationModel(), homeCourtModel);
		
		panelStack = new SearchCardStack(new XPanel[] { homeCourtPanel, sitePanel });
		addBodyPanel(panelStack);
		pack();
		showXSPanel(this.homeCourtPanel);
	}
	
	@Override
	protected OkCancelPanel createButtonPanel(int panelType, int defaultButton) {
		OkCancelPanel buttonPanel = super.createButtonPanel(panelType, defaultButton);
		this.cancelButton = buttonPanel.cancelButton;
		this.cancelButton.setMinimumSize(this.cancelButton.getPreferredSize());
		buttonPanel.okButton.setVisible(false);
		return buttonPanel;
	}
	
	/**
	 * @return the cancelButton
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * @param cancelButton
	 *            the cancelButton to set
	 */
	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}
	
	/**
	 * Display the Court Site Locations panel.
	 */
	public void showSitePanel(CourtSiteComplexValue value) {
		this.getCancelButton().setEnabled(false);
		showXSPanel(sitePanel);
		homeCourtPanel.setVisible(false);
		setTitle("Court Site Location Details");
		
		//only enable site code if adding and not updating
		if(value==null) {
			sitePanel.disableEnableSiteCode(true);
		} else {
			sitePanel.disableEnableSiteCode(false);
		}
		sitePanel.getCourtSiteModel().setCourtSite(value);
		sitePanel.setDefaultButtonForEnter(false);
		sitePanel.refreshData();
	}
	
	public void showHomeCourtPanel() {
		sitePanel.setVisible(false);
		setTitle(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
				"HomeCourt.mainTitle"));
		homeCourtPanel.setVisible(true);
	}
	
	/**
	 * Display the Home Court Centre panel.
	 */
	public void showHomeCourtPanelAgain(boolean refresh) {
		showXSPanel(homeCourtPanel);
		if (refresh) {
			homeCourtPanel.refreshData();
		}
	}

	private void showXSPanel(XPanel aPanel) {
		panelStack.show(aPanel);
	}
	
	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}
