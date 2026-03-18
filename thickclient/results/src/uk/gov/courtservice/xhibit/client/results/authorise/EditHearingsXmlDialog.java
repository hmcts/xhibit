package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.Dimension;
import org.w3c.dom.NodeList;

import javax.swing.JDialog;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class EditHearingsXmlDialog extends XDialog {

	private static final long serialVersionUID = 1L;
	
	private EditHearingsXmlPanel panel;

	public EditHearingsXmlDialog(XDialog parent, NodeList nodeList) throws CSRecoverableException {
		super(parent, "Edit Hearings", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		this.setPreferredSize(new Dimension(600, 300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		panel = new EditHearingsXmlPanel(this, nodeList);
		addBodyPanel(panel);
		pack();
	}
}
