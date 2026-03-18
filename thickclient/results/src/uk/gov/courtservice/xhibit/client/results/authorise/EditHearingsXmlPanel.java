package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import org.w3c.dom.NodeList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicArrowButton;

import org.w3c.dom.Node;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;

public class EditHearingsXmlPanel extends XPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton previewButton;
	private ArrayList<JPanel> panels;
	
	private Integer currentPageNum = 1;
	private Integer maxPageNum;
	
	private XDialog parent;
	
	public EditHearingsXmlPanel(XDialog parent, NodeList nodeList) throws CSRecoverableException {
		this.maxPageNum = nodeList.getLength();
		this.parent = parent;
		this.panels = new ArrayList<JPanel>(nodeList.getLength());
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		
		this.setPreferredSize(new Dimension(600, 300));

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0.02;
		gbc.gridwidth = 3;
		this.add(new JLabel("Please make any necessary changes to the hearings data below and view them in the preview."), gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.98;
		for (int i = 0; i < nodeList.getLength(); i++) {
			panels.add(createHearingsPanel(nodeList.item(i)));
			panels.get(i).setVisible(false);
			this.add(panels.get(i), gbc);
		}
		
		panels.get(0).setVisible(true);
		
		gbc.weighty = 0.02;
		gbc.gridwidth = 1;
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		final JButton buttonLeft = new BasicArrowButton(BasicArrowButton.WEST);
		final JButton buttonRight = new BasicArrowButton(BasicArrowButton.EAST);
		final JLabel label = new JLabel("1 of " + maxPageNum);
		buttonLeft.setEnabled(false);
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRight.setEnabled(true);
				panels.get(currentPageNum - 1).setVisible(false);
				currentPageNum--;
				label.setText(currentPageNum + " of " + maxPageNum);
				panels.get(currentPageNum - 1).setVisible(true);
				
				if (currentPageNum <= 1) {
					buttonLeft.setEnabled(false);
				}
			}
		});
		this.add(buttonLeft, gbc);
		
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		this.add(label, gbc);
		
		gbc.gridx++;
		if (currentPageNum == maxPageNum) {
			buttonRight.setEnabled(false);
		}
		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonLeft.setEnabled(true);
				panels.get(currentPageNum - 1).setVisible(false);
				currentPageNum++;
				label.setText(currentPageNum + " of " + maxPageNum);
				panels.get(currentPageNum - 1).setVisible(true);
				
				if (currentPageNum >= maxPageNum) {
					buttonRight.setEnabled(false);
				}
			}
		});
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(buttonRight, gbc);
		
		gbc.gridx=1;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.NONE;
		this.add(createPreviewButton(), gbc);
	}
	
	private JPanel createHearingsPanel(Node nodeToEdit) {
		HearingPanel hearingPanel = new HearingPanel(nodeToEdit);
	
		return hearingPanel;
	}
	
	private class HearingPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private Node node;
		private JTextArea textArea;
		private String originalNodeValue;
		
		public HearingPanel(Node node) {
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			this.setLayout(new GridBagLayout());
			this.node = node;
			
			JLabel label = new JLabel("Hearing Details:");
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.weightx = 0.02;
			this.add(label, gbc);
			
			gbc.weightx = 0.98;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx++;
			textArea = new JTextArea();
			textArea.setLineWrap(true);
			if (node.getFirstChild() != null) {
				textArea.setText(node.getFirstChild().getNodeValue());
				originalNodeValue = node.getFirstChild().getNodeValue();
			} 
			
			// Listener to ensure element gets updated whenever value changes
			textArea.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					setElementText(textArea.getText());
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					setElementText(textArea.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					setElementText(textArea.getText());
				}
			});
			
			this.add(textArea, gbc);
		}
		
		public void setElementText(String text) {
			if (node.getFirstChild() != null) {
				node.getFirstChild().setNodeValue(text);
			}
		}
		
		public void resetElementText() {
			if (node.getFirstChild() != null) {
				node.getFirstChild().setNodeValue(originalNodeValue);
			}
		}
		
		public boolean changesMade() {
			if (node.getFirstChild() != null) {
				return !(node.getFirstChild().getNodeValue().equals(originalNodeValue));
			} else {
				return false;
			}
		}
	}
	
	private JButton createPreviewButton() {
		if (previewButton == null) {
			previewButton = new JButton("Preview Changes");
			previewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Preview the formated list xml
					parent.dispose();
				}
			});
		}
		
		return previewButton;
	}
	
	private GridBagConstraints getDefaultGridBagConstraints() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0);
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {

	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		for (int i = 0; i < panels.size(); i++) {
			if (((HearingPanel) panels.get(i)).changesMade()) {
				boolean messageBoxReply = false;
				messageBoxReply = XMessageBox.alert(parent,
						"Unsaved Changes", true, XMessageBox.ICONQUESTION, "Any unsaved changes will be lost, continue?",
						XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
				if (!messageBoxReply) {
					throw new UserCancelException();
				} else {
					break;
				}
			}
		}
		
		// copy old strings back to nodes to revert changes made
		for (int i = 0; i < panels.size(); i++) {
			((HearingPanel) panels.get(i)).resetElementText();
		}
		parent.dispose();
	}
}
