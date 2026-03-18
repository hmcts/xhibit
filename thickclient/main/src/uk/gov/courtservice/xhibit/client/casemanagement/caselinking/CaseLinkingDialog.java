package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CaseLinkingDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private CaseLinkingPanel caseLinkingPanel;
	private CaseLinkingModel caseLinkingModel;
	private Frame parent;
	
	public CaseLinkingDialog(Frame frame, CaseLinkingModel caseLinkingModel) throws CSRecoverableException { 
		super(frame, "Link Case", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		parent = frame;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.caseLinkingModel = caseLinkingModel;
		caseLinkingPanel = new CaseLinkingPanel(this, caseLinkingModel, (XhibitApplicationController) frame);
		addBodyPanel(caseLinkingPanel, new Insets(5, 5, 5, 5));
		pack();
	}

	@Override
	public void cancelClicked(ActionEvent ae) throws Exception {
		processCancel();
	}
	
	public void cancelDispose(int decision) {
		if (decision == JOptionPane.NO_OPTION || decision == JOptionPane.CLOSED_OPTION) {
			return;
		} else {
			super.dispose();
		}	
	}
	
	public void processCancel() throws CSRecoverableException {
		if (caseLinkingModel.isCaseSearched() && !caseLinkingModel.isCaseLinked()) {		
			
			final JOptionPane pane = new JOptionPane();
			
			JButton yesButton = new JButton("Yes");
			yesButton.getInputMap().put(KeyStroke.getKeyStroke("Y"), "yesButtonPressed");
			yesButton.getInputMap().put(KeyStroke.getKeyStroke("N"), "noButtonPressed");
			Action yesAction = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cancelDispose(JOptionPane.YES_OPTION);
				}
			};
			yesButton.getActionMap().put("yesButtonPressed", yesAction);
			yesButton.setAction(yesAction);
			yesButton.setText("Yes");
			
			final JButton noButton = new JButton("No");
			noButton.getInputMap().put(KeyStroke.getKeyStroke("N"), "noButtonPressed");
			noButton.getInputMap().put(KeyStroke.getKeyStroke("Y"), "yesButtonPressed");
			Action noAction = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Window w = SwingUtilities.getWindowAncestor(noButton);
					if (w != null) {
						w.setVisible(false);
					}
					
					cancelDispose(JOptionPane.NO_OPTION);
				}
			};
			noButton.getActionMap().put("noButtonPressed", noAction);
			noButton.getActionMap().put("yesButtonPressed", yesAction);
			yesButton.getActionMap().put("noButtonPressed", noAction);
			noButton.setAction(noAction);
			noButton.setText("No");
			
			Object[] options = {yesButton, noButton};
			
			pane.showOptionDialog(this, "<html><body>This case was not linked." + 
										"<br/><br/>Are you sure you wish to close?</body></html>", 
										"Case Not Linked", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		} else {
			this.clearStatusBarScreenCode();
			this.dispose();
		}
	}
}