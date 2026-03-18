package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.ChildOfXPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.RefSystemCodeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;

import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

public class OffenceInfoPanel extends JPanel implements ChildOfXPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger log = CSServices.getLogger(OffenceInfoPanel.class);

	private JLabel offenceLocationAddressLabel;

	private JLabel townLabel;

	private JLabel countyLabel;

	private JLabel postCodeLabel;

	private JLabel forceLocationCodeLabel;

	private JLabel startDateLabel;

	private JLabel startTimeLabel;

	private JLabel endDateLabel;

	private JLabel endTimeLabel;

	private JButton lookupButton;

	private JTextField addressLine1Text;

	private JTextField addressLine2Text;

	private JTextField addressLine3Text;

	private JTextField addressLine4Text;

	private JTextField townText;

	private JTextField countyText;

	private JTextField postCodeText;

	private JTextField forceLocationCodeText;

	private XDatePanel startDatePanel;

	private XDatePanel endDatePanel;

	private XTimePanel startTimePanel;

	private XTimePanel endTimePanel;

	private Calendar blankDate = null;

	private TitledBorder titledBorder1;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private Dimension addressDim = new Dimension(200, 20);

	private Dimension medDim = new Dimension(100, 20);

	private Dimension shortDim = new Dimension(50, 20);

	private Dimension dateDim = new Dimension(100, 25);

	private XPanel parent;

	private List<RefSystemCodeBasicValue> sortedForceLocationCodes;

	private Integer courtId;

	private Date currentDate = new Date();

	// Three radio buttons in a group
	private ButtonGroup appealButtonGroup = new ButtonGroup();
	private JRadioButton convictionButton = new JRadioButton("Conviction");
	private JRadioButton sentenceButton = new JRadioButton("Sentence");
	private JRadioButton bothButton = new JRadioButton("Both");

	private ChargesControllerHelper.MODE mode;

	private XhibitApplicationController xac;

	private static final String REG_EXP_POSTCODE = "(GIR 0AA)|^((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|"
			+ "(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$";

	public OffenceInfoPanel(XhibitApplicationController xac, XPanel parent, Integer courtId) {

		if (parent == null || courtId == null || xac == null) {
			throw new IllegalArgumentException("OffenceInfoPanel - parent and courtId parameters must contain values");
		}

		this.xac = xac;
		this.parent = parent;
		this.courtId = courtId;
		setUpRefData();
		init();
	}

	public OffenceInfoPanel(XhibitApplicationController xac, XPanel parent, Integer courtId,
			ChargesControllerHelper.MODE mode) {

		if (parent == null || courtId == null || xac == null) {
			throw new IllegalArgumentException("OffenceInfoPanel - parent and courtId parameters must contain values");
		}

		this.xac = xac;
		this.parent = parent;
		this.courtId = courtId;
		this.mode = mode;
		setUpRefData();
		init();
	}

	private void init() {
		this.setLayout(gridBagLayout1);
		titledBorder1 = new TitledBorder(
				BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight, SystemColor.controlShadow),
				ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources,
						"offencedetails.border.title"));
		this.setBorder(titledBorder1);

		// Add Labels
		this.add((getOffenceLocationAddressLabel()), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getTownLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getCountyLabel(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getPostCodeLabel(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getForceLocationCodeLabel(), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getStartDateLabel(), new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getStartTimeLabel(), new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getEndDateLabel(), new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getEndTimeLabel(), new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		// Add Entry fields
		this.add(getAddressLine1Text(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getAddressLine2Text(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getAddressLine3Text(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getAddressLine4Text(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getTownText(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getCountyText(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		JPanel forceLocationPanel = new JPanel();
		forceLocationPanel.add(getForceLocationCodeText());
		forceLocationPanel.add(getLookupButton());

		this.add(getPostCodeText(), new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		if (false) {
			// The ability to lookup force location code was not asked for.
			// Will have to consult the MoJ to decide if they actaually want it.
			this.add(forceLocationPanel, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		} else {
			this.add(getForceLocationCodeText(), new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		}

		this.add(getStartDatePanel(), new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getStartTimePanel(), new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getEndDatePanel(), new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getEndTimePanel(), new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		// Check for appeal offence before adding extra section
		ChargesController chargesController = (ChargesController) xac.getBodyPanel();
		ChargesControllerModel model = chargesController.getModel();

			if (model.getSelectedChargeType()==5){ // 5" = criminal appeal
				// CTX-501 modification
				JPanel appealDetailsPanel = new JPanel();
				appealDetailsPanel.setPreferredSize(new Dimension(350, 75));
				appealDetailsPanel.setBorder(BorderFactory.createTitledBorder("Appeal Details"));

				convictionButton.addActionListener(new MandatoryRadioButtonListener());
				sentenceButton.addActionListener(new MandatoryRadioButtonListener());
				bothButton.addActionListener(new MandatoryRadioButtonListener());
				appealButtonGroup.add(convictionButton);
				appealButtonGroup.add(sentenceButton);
				appealButtonGroup.add(bothButton);
				JLabel appealAgainstLabel = new JLabel("Appeal Against       "); // to
																					// get
																					// correct
																					// formatting
				appealDetailsPanel.add(appealAgainstLabel);
				appealDetailsPanel.add(convictionButton);
				appealDetailsPanel.add(sentenceButton);
				appealDetailsPanel.add(bothButton);

				this.add(appealDetailsPanel, new GridBagConstraints(0, 13, 4, 1, 0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

				convictionButton.setSelected(false);
				sentenceButton.setSelected(false);
				bothButton.setSelected(false);

				if (mode != null) {
					if (mode.name().equals("EDIT")) { // mode == EDIT when
														// right-click
														// "Additional Offence
														// Info.." else is
														// ADD/REMOVE
						OffenceValue currentOffence = model.getOffenceValue();

						if (currentOffence.getAppealType() != null) {
							if (currentOffence.getAppealType().equals("C")) {
								convictionButton.setSelected(true);
							} else if (currentOffence.getAppealType().equals("S")) {
								sentenceButton.setSelected(true);
							} else if (currentOffence.getAppealType().equals("B")) {
								bothButton.setSelected(true);
							}
						}
					}
				
			}
		}
	}

	public JTextField getAddressLine1Text() {
		if (addressLine1Text == null) {
			addressLine1Text = createUpperCaseAddressField();
			addressLine1Text.setPreferredSize(addressDim);
			addressLine1Text.setToolTipText(getString("ttAddressLineText"));
			addressLine1Text.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return addressLine1Text;
	}

	private JButton getLookupButton() {
		if (lookupButton != null) {
			return lookupButton;
		}

		lookupButton = new JButton();
		lookupButton.setToolTipText(getString("ttLookupForceLocationCode"));
		lookupButton.setEnabled(true);
		lookupButton.setText(getString("forceLocationButtonText"));

		lookupButton.addActionListener(new XAction() {
			private static final long serialVersionUID = 1L;

			public void xActionPerformed(ActionEvent e) throws CSRecoverableException {

				LookupForceLocationModel lookupForceLocationModel = new LookupForceLocationModel(xac,
						sortedForceLocationCodes);

				java.awt.Frame frame = xac;
				LookupForceLocationDialog dialog = new LookupForceLocationDialog(frame, lookupForceLocationModel);

				dialog.setVisible(true);

				if (dialog.isOkClicked()) {
					if (lookupForceLocationModel.getForceLocationCode() != null) {
						getForceLocationCodeText().setText(lookupForceLocationModel.getForceLocationCode());
					}
				}

			}
		});

		return lookupButton;
	}

	public void setAddressLine1Text(JTextField addressLine1Text) {
		this.addressLine1Text = addressLine1Text;
	}

	public JTextField getAddressLine2Text() {
		if (addressLine2Text == null) {
			addressLine2Text = createUpperCaseAddressField();
			addressLine2Text.setPreferredSize(addressDim);
			addressLine2Text.setToolTipText(getString("ttAddressLineText"));
			addressLine2Text.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return addressLine2Text;
	}

	public void setAddressLine2Text(JTextField addressLine2Text) {
		this.addressLine2Text = addressLine2Text;
	}

	public JTextField getAddressLine3Text() {
		if (addressLine3Text == null) {
			addressLine3Text = createUpperCaseAddressField();
			addressLine3Text.setPreferredSize(addressDim);
			addressLine3Text.setToolTipText(getString("ttAddressLineText"));
			addressLine3Text.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return addressLine3Text;
	}

	public void setAddressLine3Text(JTextField addressLine3Text) {
		this.addressLine3Text = addressLine3Text;
	}

	public JTextField getAddressLine4Text() {
		if (addressLine4Text == null) {
			addressLine4Text = createUpperCaseAddressField();
			addressLine4Text.setPreferredSize(addressDim);
			addressLine4Text.setToolTipText(getString("ttAddressLineText"));
			addressLine4Text.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return addressLine4Text;
	}

	public void setAddressLine4Text(JTextField addressLine4Text) {
		this.addressLine4Text = addressLine4Text;
	}

	public XDatePanel getEndDatePanel() {
		if (endDatePanel == null) {
			endDatePanel = new XDatePanel(this, blankDate);
			endDatePanel.setRequired(false);
			endDatePanel.setPreferredSize(dateDim);
			endDatePanel.setToolTipText(getString("ttEndDate"));
			endDatePanel.setEnabled(false);
			endDatePanel.setDateEditable(false);
			endDatePanel.setDateEnabled(false);
			endDatePanel.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						processKeyReleasedEvent(e, "stepUpdateViewState");
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return endDatePanel;
	}

	public void setEndDatePanel(XDatePanel endDatePanel) {
		this.endDatePanel = endDatePanel;
	}

	private JLabel getEndTimeLabel() {
		if (endTimeLabel == null) {
			endTimeLabel = new JLabel(getString("endTimeLabel"));
		}
		return endTimeLabel;
	}

	public XTimePanel getEndTimePanel() {
		if (endTimePanel == null) {
			endTimePanel = new XTimePanel(this);
			endTimePanel.setRequired(false);
			endTimePanel.setToolTipText(getString("ttEndTime"));
			endTimePanel.setEnabled(false);
			endTimePanel.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return endTimePanel;
	}

	public void setEndTimePanel(XTimePanel endTimePanel) {
		this.endTimePanel = endTimePanel;
	}

	private JLabel getForceLocationCodeLabel() {
		if (forceLocationCodeLabel == null) {
			forceLocationCodeLabel = new JLabel(getString("forceLocationCodeLabel"));
		}
		return forceLocationCodeLabel;
	}

	private JLabel getOffenceLocationAddressLabel() {
		if (offenceLocationAddressLabel == null) {
			offenceLocationAddressLabel = new JLabel(getString("offenceLocationAddressLabel"));
		}
		return offenceLocationAddressLabel;
	}

	private JLabel getStartDateLabel() {
		if (startDateLabel == null) {
			startDateLabel = new JLabel(getString("startDateLabel"));
		}
		return startDateLabel;
	}

	public XDatePanel getStartDatePanel() {
		if (startDatePanel == null) {
			startDatePanel = new XDatePanel(this, blankDate);
			startDatePanel.setPreferredSize(dateDim);
			startDatePanel.setToolTipText(getString("ttStartDate"));

			startDatePanel.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						processKeyReleasedEvent(e, "defaultEndDate");
						processKeyReleasedEvent(e, "stepUpdateViewState");
					} catch (CSRecoverableException csre) {
						log.debug("Suppress CSRecoverableException error - will disable OK button");
					}
				}
			});

			startDatePanel.getDateComponent().addMChangeListener(new MChangeListener() {
				public void valueChanged(MChangeEvent e) {

					if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
						return;
					}

					defaultEndDate();

					try {
						stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						log.debug("Suppress CSRecoverableException error - will disable OK button");
					}
				}
			});
		}
		return startDatePanel;
	}

	public void processKeyReleasedEvent(KeyEvent e, String operation) throws CSRecoverableException {
		if (!(e.getKeyCode() == KeyEvent.VK_LEFT) && !(e.getKeyCode() == KeyEvent.VK_RIGHT)
				&& !(e.getKeyCode() == KeyEvent.VK_SHIFT) && !(e.getKeyCode() == KeyEvent.VK_CAPS_LOCK)
				&& !(e.getKeyCode() == KeyEvent.VK_HOME) && !(e.getKeyCode() == KeyEvent.VK_END)
				&& !(e.getKeyCode() == KeyEvent.VK_ALT) && !(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				&& !(e.getKeyCode() == KeyEvent.VK_PAGE_UP) && !(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN
						&& !(e.getKeyCode() == KeyEvent.VK_UP) && !(e.getKeyCode() == KeyEvent.VK_DOWN))) {

			if (operation.trim().equalsIgnoreCase("defaultEndDate") && !(e.getKeyCode() == KeyEvent.VK_DELETE)
					&& !(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
				defaultEndDate();
			}

			if (operation.trim().equalsIgnoreCase("stepUpdateViewState")) {
				stepUpdateViewState();
			}
		}
	}

	/**
	 * Processing to check whether the end date should be defaulted to the start
	 * date when the start date changes
	 * 
	 * The end date should not be defaulted to the start date when 1. The start
	 * date is invalid or empty 2. The end date is already populated (in this
	 * scenario the end date should be the next component focused following a
	 * valid change to the start date)
	 */
	public void defaultEndDate() {
		// The end date should not be defaulted anymore.
		// RFC0458
	}

	public boolean validateDatePanel(XDatePanel datePanel) {

		boolean isDateValid = false;

		try {
			if (datePanel.getDateComponent().getDisplay() != null
					&& !datePanel.getDateComponent().getDisplay().getText().trim().equals("")) {
				datePanel.stepValidate();
				isDateValid = true;
			}
		} catch (CSValidationException ve) {
			log.debug("Date display populated but invalid");
			isDateValid = false;
		}

		return isDateValid;
	}

	public void setStartDatePanel(XDatePanel startDatePanel) {
		this.startDatePanel = startDatePanel;
	}

	public XTimePanel getStartTimePanel() {
		if (startTimePanel == null) {
			startTimePanel = new XTimePanel(this);
			startTimePanel.setRequired(false);
			startTimePanel.setToolTipText(getString("ttStartTime"));
			startTimePanel.setEnabled(false);
			startTimePanel.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});

		}
		return startTimePanel;
	}

	public void setStartTimePanel(XTimePanel startTimePanel) {
		this.startTimePanel = startTimePanel;
	}

	private JLabel getTownLabel() {
		if (townLabel == null) {
			townLabel = new JLabel(getString("townLabel"));
		}
		return townLabel;
	}

	public JTextField getTownText() {
		if (townText == null) {
			townText = createUpperCaseAddressField();
			townText.setPreferredSize(addressDim);
			townText.setToolTipText(getString("ttAddressLineText"));
			townText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return townText;
	}

	public void setTownText(JTextField townText) {

		this.townText = townText;

	}

	/**
	 * Get a resource string from the Additional resources
	 * 
	 * @param key
	 *            the key to lookup
	 * @return the resource from the given key.
	 */
	private String getString(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
	}

	private JLabel getCountyLabel() {
		if (countyLabel == null) {
			countyLabel = new JLabel(getString("countyLabel"));
		}
		return countyLabel;
	}

	public JTextField getCountyText() {
		if (countyText == null) {
			countyText = createUpperCaseAddressField();
			countyText.setPreferredSize(addressDim);
			countyText.setToolTipText(getString("ttAddressLineText"));
			countyText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return countyText;
	}

	public void setCountyText(JTextField countyText) {
		this.countyText = countyText;
	}

	public JTextField getPostCodeText() {
		if (postCodeText == null) {
			postCodeText = createUpperCasePostCodeField(8);
			postCodeText.setPreferredSize(medDim);
			postCodeText.setToolTipText(getString("ttPostCodeText"));
			postCodeText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return postCodeText;
	}

	private JTextField createUpperCasePostCodeField(final int size) {
		// create the required capabilities (document decorators/validators)
		final Capability[] capabilities = new Capability[] { Capability.upperCase(), Capability.alphaNumeric(),
				Capability.limitedText(size) };

		// Create the text document with the required capabilities.
		final Document doc = DocumentFactory.newDocument(capabilities);

		// create the text field to return
		final JTextField textField = JTextFieldFactory.getTextField(doc);

		return textField;
	}

	private JTextField createUpperCaseAddressField() {
		// create the required capabilities (document decorators/validators)
		final Capability[] capabilities = new Capability[] { Capability.upperCase(),
				Capability.utf8LimitedTextCapability(30) };

		// Create the text document with the required capabilities.
		final Document doc = DocumentFactory.newDocument(capabilities);

		// create the text field to return
		final JTextField textField = JTextFieldFactory.getTextField(doc);

		return textField;
	}

	public void setPostCodeText(JTextField postCodeText) {
		this.postCodeText = postCodeText;
	}

	private JLabel getPostCodeLabel() {
		if (postCodeLabel == null) {
			postCodeLabel = new JLabel(getString("postCodeLabel"));
		}
		return postCodeLabel;
	}

	private JLabel getEndDateLabel() {
		if (endDateLabel == null) {
			endDateLabel = new JLabel(getString("endDateLabel"));
		}
		return endDateLabel;
	}

	private JLabel getStartTimeLabel() {
		if (startTimeLabel == null) {
			startTimeLabel = new JLabel(getString("startTimeLabel"));
		}
		return startTimeLabel;
	}

	public JTextField getForceLocationCodeText() {
		if (forceLocationCodeText == null) {
			// Field is now 2digits + 2letters
			Document doc = DocumentFactory.newDocument(new Capability[] { Capability.limitedText(4) });
			forceLocationCodeText = JTextFieldFactory.getTextField(doc);
			forceLocationCodeText.setPreferredSize(shortDim);
			forceLocationCodeText.setToolTipText(getString("ttForceLocationCode"));
			forceLocationCodeText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return forceLocationCodeText;
	}

	public void setForceLocationCodeText(JTextField forceLocationCodeText) {
		this.forceLocationCodeText = forceLocationCodeText;
	}

	public void stepUpdateViewState() throws CSRecoverableException {

		boolean startTimeEmpty = getStartTimePanel().getText() == null || getStartTimePanel().getText().equals("");

		boolean startDateEmpty = getStartDatePanel().getText() == null || getStartDatePanel().getText().equals("");

		boolean endDateEmpty = getEndDatePanel().getText() == null || getEndDatePanel().getText().equals("");

		if (startTimeEmpty || startDateEmpty || endDateEmpty) {
			getEndTimePanel().setText(null);
		}

		if (startDateEmpty) {
			getStartTimePanel().setText(null);
		}

		if (startDateEmpty) {
			getEndDatePanel().setDate((Date) null);
		}

		boolean endDateEnabled = !startDateEmpty;
		getEndDatePanel().setEnabled(endDateEnabled);
		getEndDatePanel().setDateEditable(endDateEnabled);
		getEndDatePanel().setDateEnabled(endDateEnabled);

		boolean startTimeEnabled = !startDateEmpty;
		getStartTimePanel().setEnabled(startTimeEnabled);

		boolean endTimeEnabled = endDateEnabled && !endDateEmpty && startTimeEnabled && !startTimeEmpty;

		getEndTimePanel().setEnabled(endTimeEnabled);

		parent.stepUpdateViewState();
	}

	/**
	 * Validates that the post code conforms to one of the UK standard formats.
	 * Currently, these are:
	 * <ul>
	 * <li>AN NAA
	 * <li>ANN NAA
	 * <li>AAN NAA
	 * <li>AANN NAA
	 * <li>ANA NAA
	 * <li>AANA NAA
	 * </ul>
	 * Note too that the post code GIR 0AA is a special code that does not fit
	 * the standard.
	 * 
	 * @param param
	 *            - the post code to be validated
	 * @return - true if the post code format is valid otherwise false
	 */
	public void validatePostCode() throws UserCancelException {
		if (getPostCodeText().getText() != null && !getPostCodeText().getText().trim().equals("")) {
			if (!isDataMatchesRegularExpression(REG_EXP_POSTCODE, getPostCodeText().getText())) {
				JOptionPane.showMessageDialog(this, getString("postCodeValidation.message"),
						getString("postCodeValidation.title"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			}
		}
	}

	/**
	 * Determines whether or not the data matches the expression
	 * 
	 * @param expression
	 *            - the regular expression to check the data against
	 * @param data
	 *            - the data to be checked
	 * @return true - if the data is valid for the expression
	 */
	private boolean isDataMatchesRegularExpression(String expression, String data) {
		boolean matched = false;

		try {
			RE regexp = new RE(expression);
			matched = regexp.match(data);
		} catch (RESyntaxException e) {
			matched = false;
		}

		return matched;
	}

	/**
	 * Validate dates in breach panel - they are all on the same dialog box so
	 * we don't need the special end date validation If any of these are invalid
	 * a message box detailing the problem is displayed and UserCancelException
	 * thrown.
	 * 
	 * @return
	 * @throws CSValidationException
	 * @throws UserCancelException
	 */
	public void validateDates() throws CSValidationException, UserCancelException {
		validateDates(null);
	}

	/**
	 * Validate dates in panel. If any of these are invalid a message box
	 * detailing the problem is displayed and UserCancelException thrown.
	 * 
	 * @param OffenceValue
	 * @return
	 * @throws CSValidationException
	 * @throws UserCancelException
	 */
	public void validateDates(OffenceValue offenceValue) throws CSValidationException, UserCancelException {
		// Dates syntactily valid
		getStartDatePanel().stepValidate();
		getStartTimePanel().stepValidate();
		getEndDatePanel().stepValidate();
		getEndTimePanel().stepValidate();

		if (getStartDatePanel().getDate() != null && getEndDatePanel().getDate() != null) {
			if (getStartDatePanel().getDate().getTime().after(currentDate)
					|| getEndDatePanel().getDate().getTime().after(currentDate)) {
				log.debug("validateDates: A date is in future");
				JOptionPane.showMessageDialog(this, getString("messageDatesNotInFuture"),
						getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			}

			if (getStartDatePanel().getDate().after(getEndDatePanel().getDate())) {
				log.debug("validateDates: start date after end date");
				JOptionPane.showMessageDialog(this, getString("endBeforeStartDate.message"),
						getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			} else if (getStartDatePanel().getDate().equals(getEndDatePanel().getDate())) {
				// The start and end time are both optional
				Calendar startTime = getStartTimePanel().getDate();
				Calendar endTime = getEndTimePanel().getDate();
				if (startTime == null && endTime == null) {
					// ok
				} else if (endTime == null) {
					// We assume a null time means the start of the day
					log.debug("validateDates: start date same as end date but end time is empty");
					JOptionPane.showMessageDialog(this, getString("endBeforeStartTime.message"),
							getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
					throw new UserCancelException();
				} else if (startTime == null) {
					// ok - Start time exception caught in the next but one if
					// statement
				} else if (startTime.after(endTime)) {
					log.debug("validateDates: start date same as end date but end time before start time");
					JOptionPane.showMessageDialog(this, getString("endBeforeStartTime.message"),
							getString("messageDateValidationTime"), JOptionPane.ERROR_MESSAGE);
					throw new UserCancelException();
				}
			}

			// Check if StartTime is empty but EndTime is not empty
			if (getStartTimePanel().getDate() == null && getEndTimePanel().getDate() != null) {
				log.debug("validateDates: Start time is null but end time is not null");
				JOptionPane.showMessageDialog(this, getString("startTimeNullButEndTimeFull"),
						getString("messageDateValidationTime"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			}
		} else if (getStartDatePanel().getDate() != null) {
			// Only the start date is set
			if (getEndTimePanel().getDate() != null) {
				log.debug("validateDates: End date is null but end time is not null");
				JOptionPane.showMessageDialog(this, getString("endDateNullButEndTimeFull"),
						getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			}
			if (getStartDatePanel().getDate().getTime().after(currentDate)) {
				log.debug("validateDates: A date is in future");
				JOptionPane.showMessageDialog(this, getString("messageDatesNotInFuture"),
						getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			}
		} else {
			JOptionPane.showMessageDialog(this, getString("startEndDatesNull.message"),
					getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
			throw new UserCancelException();
		}

		// We don't need to do this processing for breaches as there is only one
		// defendant and
		// the end date, arrest date and charge date validation is all on the
		// same dialog box.
		// So the offenceValue passed in is null
		if (offenceValue != null) {
			HashMap defOnOffenceBasicValues = offenceValue.getDefOnOffenceBasicValues();
			if (defOnOffenceBasicValues != null && !defOnOffenceBasicValues.isEmpty()) {
				DefendantOnOffenceComplexValue defOnOffenceComplexValue = null;
				Date arrestDate = null;
				Date chargeDate = null;
				Integer newDefendantId = null;

				Calendar endDate = getEndDatePanel().getDate();
				if (endDate == null) {
					endDate = (Calendar) getStartDatePanel().getDate().clone();
				}
				endDate.set(Calendar.HOUR, 0);
				endDate.set(Calendar.MINUTE, 0);
				endDate.set(Calendar.SECOND, 0);
				endDate.set(Calendar.AM_PM, Calendar.AM);
				log.debug("EndDate: " + endDate.getTime().toString());

				for (Object defendantId : defOnOffenceBasicValues.keySet()) {
					newDefendantId = (Integer) defendantId;
					log.debug("Looping thru Defendant On Offence hashmap for end date validation using defendant Id: "
							+ newDefendantId);

					defOnOffenceComplexValue = (DefendantOnOffenceComplexValue) defOnOffenceBasicValues
							.get(defendantId);

					if (defOnOffenceComplexValue.getArrestDate() != null) {
						arrestDate = defOnOffenceComplexValue.getArrestDate();
						log.debug("arrestDate: " + arrestDate.toString());
					} else {
						arrestDate = null;
					}
					if (defOnOffenceComplexValue.getChargeDate() != null) {
						chargeDate = defOnOffenceComplexValue.getChargeDate();
						log.debug("chargeDate: " + chargeDate.toString());
					} else {
						chargeDate = null;
					}

					if (arrestDate != null && endDate.getTime().after(arrestDate)) {
						log.error("Error: Offence End Date is after the Arrest Date");
						JOptionPane.showMessageDialog(this,
								getString("messageOffenceEndDateAfterArrestDate")
										+ getDefendantName(offenceValue, newDefendantId),
								getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
						throw new UserCancelException();
					}

					if (chargeDate != null && endDate.getTime().after(chargeDate)) {
						log.error("Error: Offence End Date is after the Charge Date");
						JOptionPane.showMessageDialog(this,
								getString("messageOffenceEndDateAfterChargeDate")
										+ getDefendantName(offenceValue, newDefendantId),
								getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
						throw new UserCancelException();
					}
				}
			}
		}
	}

	private String getDefendantName(OffenceValue offenceValue, Integer newDefendantId) {
		Collection defendantsOnCount = offenceValue.getDefendantValues();

		DefendantValue defendant = null;
		String defendantName = null;

		if (defendantsOnCount != null && defendantsOnCount.size() > 0) {
			java.util.Iterator defendantIterator = defendantsOnCount.iterator();

			while (defendantIterator.hasNext()) {
				defendant = (DefendantValue) defendantIterator.next();
				log.debug("Looping thru defendant values on Offence for defendant Id: "
						+ defendant.getDefendantID().intValue());
				if (defendant.getDefendantID().intValue() == newDefendantId.intValue()) {
					log.debug("Got a match!!!: " + defendant.getDefendantID().intValue());
					defendantName = defendant.getSurName() + ", " + defendant.getFirstName();
					log.debug("defendantName: " + defendantName);
				}
			}
		}

		if (defendantName == null) {
			defendantName = "Defendant cannot be determined";
		}

		log.debug("Returning defendantName: " + defendantName);

		return defendantName;
	}

	public void setUpRefData() {
		sortedForceLocationCodes = RefSystemCodeHelper.getSortedForceLocationCodes(courtId);
	}

	private boolean doesTimeStartAtMidnight(Calendar time) {
		if (time == null)
			return false;

		return time.get(Calendar.HOUR_OF_DAY) == 0 && time.get(Calendar.MINUTE) == 0;
	}

	public void moveModelToScreen(OffenceValue offenceValue, AddressValue addressValue) {
		if (offenceValue != null) {
			getForceLocationCodeText().setText(nullToString(offenceValue.getForceLocationCode()));
			getStartDatePanel().setDate(offenceValue.getOffenceStartDateTime());

			if (doesTimeStartAtMidnight(offenceValue.getOffenceStartDateTime())) {
				getStartTimePanel().setTime((Calendar) null);
			} else {
				getStartTimePanel().setTime(offenceValue.getOffenceStartDateTime());
			}

			getEndDatePanel().setDate(offenceValue.getOffenceEndDateTime());

			if (doesTimeStartAtMidnight(offenceValue.getOffenceEndDateTime())) {
				getEndTimePanel().setTime((Calendar) null);
			} else {
				getEndTimePanel().setTime(offenceValue.getOffenceEndDateTime());
			}
		}

		if (addressValue != null) {
			getAddressLine1Text().setText(nullToString(addressValue.getAddress1()));
			getAddressLine2Text().setText(nullToString(addressValue.getAddress2()));
			getAddressLine3Text().setText(nullToString(addressValue.getAddress3()));
			getAddressLine4Text().setText(nullToString(addressValue.getAddress4()));
			getTownText().setText(nullToString(addressValue.getTown()));
			getCountyText().setText(nullToString(addressValue.getCounty()));
			getPostCodeText().setText(nullToString(addressValue.getPostcode()));
		}

		try {
			stepUpdateViewState();
		} catch (CSRecoverableException csre) {
			XHIBITErrorHandler.handleError(csre);
		}
	}

	public void validateForceLocationCode() throws UserCancelException {
		String forceLocationCode = forceLocationCodeText.getText();

		if (forceLocationCode != null && !forceLocationCode.equals("")) {

			final String regex = "[0-9][0-9][a-zA-Z0-9]*";

			if (!forceLocationCode.matches(regex)) {

				JOptionPane.showMessageDialog(this, getString("forceLocationCode.incorrectFormat.message"),
						getString("forceLocationCode.incorrectFormat.title"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			}

			// create Search Data
			RefSystemCodeBasicValue rscbv = new RefSystemCodeBasicValue();
			// Only the 2 digit prefix is looked up from the HO_POL_FORCE
			// entries in the XHB_REF_SYSTEM_CODE table.
			rscbv.setCode(forceLocationCode.substring(0, 2));

			// get sorted reference data & search reference data
			if (!RefSystemCodeHelper.isValidRefSystemCode(sortedForceLocationCodes, rscbv)) {
				JOptionPane.showMessageDialog(this, getString("forceLocationCode.Invalid.message"),
						getString("forceLocationCode.Invalid.title"), JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
			}
		}
	}

	public OffenceValue populateOffenceValue(OffenceValue offenceValue, AddressValue addressValue)
			throws CSValidationException {

		// The start time, end date and end time are all optional

		if (offenceValue == null)
			throw new IllegalArgumentException("OffenceValue must not be null");

		Calendar startDateTime = getStartDatePanel().getDate();
		if (getStartTimePanel().getDate() != null) {
			startDateTime.add(Calendar.HOUR_OF_DAY, getStartTimePanel().getDate().get(Calendar.HOUR_OF_DAY));
			startDateTime.add(Calendar.MINUTE, getStartTimePanel().getDate().get(Calendar.MINUTE));
		} else {
			startDateTime.add(Calendar.HOUR_OF_DAY, 0);
			startDateTime.add(Calendar.MINUTE, 0);
		}

		Calendar endDateTime = getEndDatePanel().getDate();
		if (endDateTime == null) {
			endDateTime = (Calendar) startDateTime.clone();
		}

		if (getEndTimePanel().getDate() != null) {
			endDateTime.add(Calendar.HOUR_OF_DAY, getEndTimePanel().getDate().get(Calendar.HOUR_OF_DAY));
			endDateTime.add(Calendar.MINUTE, getEndTimePanel().getDate().get(Calendar.MINUTE));
		} else {
			endDateTime.add(Calendar.HOUR_OF_DAY, 0);
			endDateTime.add(Calendar.MINUTE, 0);
		}

		offenceValue.setOffenceStartDateTime(startDateTime);

		if (getEndDatePanel().getDate() != null) {
			offenceValue.setOffenceEndDateTime(endDateTime);
		} else {
			offenceValue.setOffenceEndDateTime(null);
		}

		if (getForceLocationCodeText().getText() != null && !getForceLocationCodeText().getText().equals("")) {
			offenceValue.setForceLocationCode(getForceLocationCodeText().getText());
		} else {
			offenceValue.setForceLocationCode(null);
		}

		offenceValue.setAddressValue(populateAddressValue(addressValue));

		if (convictionButton.isSelected())
			offenceValue.setAppealType("C");
		else if (sentenceButton.isSelected())
			offenceValue.setAppealType("S");
		else if (bothButton.isSelected())
			offenceValue.setAppealType("B");

		return offenceValue;
	}

	private AddressValue populateAddressValue(AddressValue addressValue) {

		if (addressValue == null) {
			addressValue = new AddressValue();
		}

		addressValue.setAddress1(getAddressLine1Text().getText());
		addressValue.setAddress2(getAddressLine2Text().getText());
		addressValue.setAddress3(getAddressLine3Text().getText());
		addressValue.setAddress4(getAddressLine4Text().getText());
		addressValue.setTown(getTownText().getText());
		addressValue.setCounty(getCountyText().getText());
		addressValue.setPostcode(getPostCodeText().getText());

		return addressValue;
	}

	public boolean isMandatoryFieldsCompleted() {
		return getStartDatePanel().isMandatoryFieldsCompleted()
				&& (getAddressLine1Text().getText() != null && getAddressLine1Text().getText().trim().length() > 0)
				&& (getAddressLine2Text().getText() != null && getAddressLine2Text().getText().trim().length() > 0)
				&& ((getConvictionButton().isSelected()) || (getSentenceButton().isSelected())
						|| (getBothButton().isSelected()) || !isAppealOffence());
	}

	// Added in for 1908

	public JRadioButton getConvictionButton() {
		return convictionButton;
	}

	public JRadioButton getSentenceButton() {
		return sentenceButton;
	}

	public JRadioButton getBothButton() {
		return bothButton;
	}

	private class MandatoryRadioButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				parent.stepUpdateViewState();
			} catch (CSRecoverableException csre) {
				XHIBITErrorHandler.handleError(csre);
			}
		}
	}
	////////

	private String nullToString(Object object) {
		if (object == null)
			return "";
		return object.toString();
	}

	private boolean isAppealOffence() {
		ChargesController chargesController = (ChargesController) xac.getBodyPanel();
		ChargesControllerModel model = chargesController.getModel();

		if (model.getSelectedChargeType()==5)	{
				return true;
			
		}
		
		return false;
	}
}
