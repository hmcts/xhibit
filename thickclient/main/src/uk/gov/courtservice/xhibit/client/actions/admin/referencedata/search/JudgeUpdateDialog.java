package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.Dimension;
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * The Judge Details Screen.
 * 
 * @author grewalg
 *
 */
public class JudgeUpdateDialog extends XDialog {

	private static final long serialVersionUID = 6142669445493190674L;
	private static final String resources = XhibitBundles.XhibitSearch;

	public JudgeUpdateDialog(Frame frame, XPanel updatePanel, boolean isUpdate) throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(resources, "judge.ref.details.xxtitle"), true, XDialog.CUSTOM,
				XDialog.DEFAULTCANCEL);
		this.setPreferredSize(new Dimension(650, 475));
		this.setMinimumSize(this.getPreferredSize());
		this.bodyPanel = updatePanel;
		super.addBodyPanel(bodyPanel);
		super.pack();
		super.setResizable(true);
	}


}
