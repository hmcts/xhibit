package uk.gov.courtservice.xhibit.client.results.NTRSF;

import java.awt.Dimension;

import javax.swing.JDialog;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class NTRSFCaseSearchDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	
	public NTRSFCaseSearchDialog(XhibitApplicationController xac, NTRSFCaseSearchModel caseSearchModel) throws CSRecoverableException { 
		super(xac, "Search For Case", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		setPreferredSize(new Dimension(400,200));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		NTRSFCaseSearchPanel caseSearchPanel = new NTRSFCaseSearchPanel(this, caseSearchModel);
		addBodyPanel(caseSearchPanel);
		pack();
	}

	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}
