package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;

import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CourtCalendarTableCellPanel extends JPanel {
	private static final long serialVersionUID = 8353704183762420421L;

	private static String resources = XhibitBundles.CourtCalendar;

	private CourtCalendarPanel myParent;
	private RefCalendarValue refCalendar;
	private JLabel calDateLabel;
	private JLabel availLabel;
	private JPanel buttonPanel;
	private JRadioButton yesButton;
	private JRadioButton noButton;
	private ButtonGroup availGroup;
	private JLabel reasonLabel;
	private JTextArea reasonText;
	private boolean checkText;
	protected static final Integer COURT_ID = XhibitSingleton.getInstance().getCourtId();


	public CourtCalendarTableCellPanel(CourtCalendarPanel myParent) {
		this.myParent = myParent;
		this.setMinimumSize(new Dimension(350, 175));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2, 2, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		// Column 1
		// Row 1
		c.gridwidth = 3;
		this.add(getCalDateLabel(), c);

		// Row 2
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.insets = new Insets(0, 2, 0, 0);
		c.fill = GridBagConstraints.NONE;
		this.add(getAvailLabel(), c);

		// Row 3
		c.gridy = 2;
		c.insets = new Insets(0, 2, 0, 0);
		this.add(getReasonLabel(), c);

		// Column 2
		// Row 2
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.7;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 0, 0);

		getAvailGroup();
		getButtonPanel().add(getYesButton());
		getButtonPanel().add(getNoButton());
		this.add(getButtonPanel(), c);

		// Row 3
		c.gridy = 2;
		c.gridheight = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 2);
		this.add(getReasonText(), c);
	}

	/**
	 * @return the parent
	 */
	public CourtCalendarPanel getMyParent() {
		return myParent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setMyParent(CourtCalendarPanel myParent) {
		this.myParent = myParent;
	}

	public RefCalendarValue getRefCalendar() {
		return refCalendar;
	}

	public void setRefCalendar(RefCalendarValue refCalendar) {
		this.refCalendar = refCalendar;
	}

	public JLabel getCalDateLabel() {
		if (calDateLabel == null) {
			calDateLabel = new JLabel("");
			calDateLabel.setFont(deriveFont(calDateLabel, 11));
			calDateLabel.setVisible(false);
		}
		return calDateLabel;
	}

	public void setCalDateLabel(JLabel calDateLabel) {
		this.calDateLabel = calDateLabel;
	}

	public JLabel getAvailLabel() {
		if (availLabel == null) {
			availLabel = new JLabel(XHIBITConstant.getResource(resources, "lblAvailable"));
			availLabel.setFont(deriveFont(availLabel, 9));
		}
		return availLabel;
	}

	public void setAvailLabel(JLabel availLabel) {
		this.availLabel = availLabel;
	}

	public JRadioButton getYesButton() {
		if (yesButton == null) {
			yesButton = new JRadioButton(XHIBITConstant.getResource(resources, "lblYesButton"));
			yesButton.setFont(deriveFont(yesButton, 9));
			yesButton.setBackground(Color.WHITE);
			yesButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					checkAvailDate(true, true, false);
				}
			});
		}
		return yesButton;
	}

	public void setYesButton(JRadioButton yesButton) {
		this.yesButton = yesButton;
	}

	public JRadioButton getNoButton() {
		if (noButton == null) {
			noButton = new JRadioButton(XHIBITConstant.getResource(resources, "lblNoButton"));
			noButton.setFont(deriveFont(noButton, 9));
			noButton.setBackground(Color.WHITE);
			noButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					checkAvailDate(false, true, false);
				}
			});
		}
		return noButton;
	}

	public void setNoButton(JRadioButton noButton) {
		this.noButton = noButton;
	}

	public ButtonGroup getAvailGroup() {
		if (availGroup == null) {
			availGroup = new ButtonGroup();
			availGroup.add(getYesButton());
			availGroup.add(getNoButton());
		}
		return availGroup;
	}

	public void setAvailGroup(ButtonGroup availGroup) {
		this.availGroup = availGroup;
	}

	public JLabel getReasonLabel() {
		if (reasonLabel == null) {
			reasonLabel = new JLabel(XHIBITConstant.getResource(resources, "lblReason"));
			reasonLabel.setFont(deriveFont(reasonLabel, 9));
		}
		return reasonLabel;
	}

	public void setReasonLabel(JLabel reasonLabel) {
		this.reasonLabel = reasonLabel;
	}

	public JTextArea getReasonText() {
		if (reasonText == null) {
			reasonText = new JTextArea(2, 10);
			reasonText.setPreferredSize(new Dimension(10, 5));
			reasonText.setEditable(true);
			reasonText.setLineWrap(true);
			reasonText.setWrapStyleWord(true);
			reasonText.setBorder(BorderFactory.createEtchedBorder());
			reasonText.setFont(deriveFont(reasonText, 9));

			reasonText.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					checkReasonText();
					refCalendar.setDescription(getReasonTextString());
					checkAvailDate((refCalendar.getAvail() == "Y" ? true : false), false, true);
				}
			});
			reasonText.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					getMyParent().setModified(true);
				}

				@Override
				public void keyTyped(KeyEvent e) {
					int max = 34;
					if (getReasonTextString().length() > max + 1) {
						e.consume();
						String shortened = getReasonTextString().substring(0, max);
						getReasonText().setText(shortened);
					} else if (getReasonTextString().length() > max) {
						e.consume();
					}
				}
			});
		}
		return reasonText;
	}

	/**
	 * Checks if the Reason Text has changed. When the the field is initially
	 * populated it shouldn't be checked.
	 */
	private boolean checkReasonText() {
		if (checkText && checkForChanges()) {
			refCalendar.setModified(true);
		}
		return refCalendar.isModified();
	}

	private boolean checkForChanges() {
		String description = reasonText.getText();
		boolean isModified = false;
		if (description != null && !description.equals("")) {
			if (!description.equals(refCalendar.getDescription())) {
				isModified = true;
			}
		} else {
			if (refCalendar.getDescription() != null && !refCalendar.getDescription().equals(description)) {
				isModified = true;
			}
		}
		return isModified;
	}

	private String getReasonTextString() {
		return getReasonText().getText();
	}

	public void setReasonText(JTextArea reasonText) {
		this.reasonText = reasonText;
	}

	private boolean isReasonTextEmpty() {
		if (refCalendar.getDescription() == null || refCalendar.getDescription().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel(new FlowLayout());
			buttonPanel.setBackground(Color.WHITE);
		}
		return buttonPanel;
	}

	public void setButtonPanel(JPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	private Font deriveFont(Component comp, float fontSize) {
		Font originalFont = comp.getFont();
		return originalFont.deriveFont(fontSize);
	}

	public void updateData(RefCalendarValue refCalendar, boolean isSelected, JTable table) {
		this.refCalendar = refCalendar;
		if (refCalendar != null) {
			Calendar calDate = Calendar.getInstance();
			calDate.setTime(refCalendar.getCalDate());
			String dateString = XDateFormat.format(calDate, XDateFormat.DATEFORMAT);
			getCalDateLabel().setText(dateString.toUpperCase());
			getCalDateLabel().setVisible(true);
			if ("Y".equals(refCalendar.getAvail())) {
				getYesButton().setSelected(true);
				checkAvailDate(true, false, false);
			} else {
				getNoButton().setSelected(true);
				checkAvailDate(false, false, false);
			}
			// Do not check for Text changes here (should only be done when a
			// Key event is triggered)
			checkText = false;
			getReasonText().setText(refCalendar.getDescription());
			checkText = true;

			this.setBackground(Color.WHITE);
			getCalDateLabel().setVisible(true);
			getButtonPanel().setVisible(true);
			getReasonLabel().setVisible(true);
			getReasonText().setVisible(true);
			getAvailLabel().setVisible(true);
		} else {
			getCalDateLabel().setText("");
			getCalDateLabel().setVisible(false);
			getButtonPanel().setVisible(false);
			getReasonLabel().setVisible(false);
			getReasonText().setVisible(false);
			getAvailLabel().setVisible(false);
			this.setBackground(Color.LIGHT_GRAY);
		}
	}

	public void updateModel(String description, String avail) {
		if (checkForChanges()) {
			refCalendar.setDescription(description);
			refCalendar.setModified(true);
		}
		refCalendar.setAvail(avail);
	}

	/**
	 * If 'No' is selected as avail for a weekday then a reason must be given.
	 */
	private void checkAvailDate(boolean isAvail, boolean radioButtonSelected, boolean textSelected) {
		if (isAvail) {
			refCalendar.setAvail("Y");
			if (radioButtonSelected) {
				refCalendar.setModified(true);
				getMyParent().setModified(true);
			}
			if (isWeekend()) {
				setError(radioButtonSelected, textSelected);
			} else {
				clearError();
			}
		} else {
			if (isWeekend()) {
				if (radioButtonSelected) {
					refCalendar.setModified(true);
					getMyParent().setModified(true);
				}
				refCalendar.setAvail("N");
				clearError();
			} else {
				if (radioButtonSelected) {
					boolean foundDailyListing = CourtDailyListing.getInstance().hasDailyListing(COURT_ID,
							refCalendar.getCalDate());
					if (foundDailyListing) {
						JOptionPane.showMessageDialog(null,
								XHIBITConstant.getResource(resources, "dailyListExistsErrorMessage"),
								XHIBITConstant.getResource(resources, "dailyListExistsErrorHeader"),
								JOptionPane.WARNING_MESSAGE);
						getYesButton().setSelected(true);
					} else {
						refCalendar.setAvail("N");
						refCalendar.setModified(true);
						getMyParent().setModified(true);
						if (getReasonTextString().equals("")) {
							getReasonText().requestFocus();
						}
					}
				} else {
					refCalendar.setAvail("N");
					clearError();
				}
			}
		}
	}

	private boolean isWeekend() {
		Calendar date = Calendar.getInstance();
		date.setTime(refCalendar.getCalDate());
		int day = date.get(Calendar.DAY_OF_WEEK);

		if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
			return true;
		} else {
			return false;
		}
	}

	private void setError(boolean radioButtonSelected, boolean textSelected) {
		if (radioButtonSelected || refCalendar.isModified()) {
			if (isReasonTextEmpty() && isWeekend()) {
				getReasonText().setBorder(BorderFactory.createLineBorder(Color.RED));
				getReasonText().requestFocus();
			} else {
				clearError();
			}
		} else {
			if (!isReasonTextEmpty()) {
				clearError();
			}
		}
	}

	/**
	 * If availability is set to 'N' for a weekday, move the focus to the reason
	 * field but don't make it mandatory.
	 */
	private void setWeekdayFocus() {
		getReasonText().requestFocus();
	}

	private void clearError() {
		reasonText.setBorder(BorderFactory.createEtchedBorder());
	}
}
