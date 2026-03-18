package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class CaseSearchDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private CaseSearchPanel caseSearchPanel;
	private CaseSearchModel caseSearchModel;
	
	public CaseSearchDialog(Frame frame, CaseSearchModel caseSearchModel) throws CSRecoverableException { 
		super(frame, "Search For Case", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		setPreferredSize(new Dimension(400,200));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.caseSearchModel = caseSearchModel;
		caseSearchPanel = new CaseSearchPanel(this, caseSearchModel);
		addBodyPanel(caseSearchPanel);
		pack();
		this.setLocationRelativeTo(this.getParentFrame());
	}

	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}

}
