package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchCourtAction;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra EDS
 * @version 1.0
 */

public class BreachPanel extends XPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = CSServices.getLogger(BreachPanel.class);

	public static final int VIEW_MODE = 0;

	public static final int ADD_MODE = 1;

	public static final int EDIT_MODE = 2;

	private static final String BREACH_TYPE_BRING_BACK = "B";

	private static final String BRING_BACK_BRING_BACK = "B";

	private static final String BREACH_TYPE_COMMITTAL = "C";

	private static final String BRING_BACK_COMMITTAL = "C";

	private static final String BREACH_ADMITTED_YES = "Y";

	private static final String BREACH_ADMITTED_NO = "N";

	private static final String BREACH_ADMITTED_BLANK = "";

	private static final String CASE_TYPE_SENTENCE = "S";

	private static final String CASE_TYPE_TRIAL = "T";

	private static final int ORIGINALTEXT_MAX = 72;

	/**
	 * a Date Put is not required for any of these HO PROC CODES. NOTE - Codes
	 * must be in order as this array is used in binary search!
	 */

	/*
	 * NOTE - Updating this array will also require the codes to be added to the
	 * following files
	 * 
	 * 1) Xhibit.common.results.src.gov.courtservice.xhibit.common.results.vos.
	 * PleaSaveValue - method: isNotSpecialHoProcCode 2)
	 * Xhibit.support.config.src.gov.courtservice.xhibit.support.config.src.
	 * config.bundles.errorText.properties - add HO Proc codes to existing
	 * messages detailing Ho Proc Codes.
	 */
	private static final String[] HO_PROC_CODES = new String[] { "31", "44", "53", "56", "58", "66", "67", "81", "82",
			"83", "84" };

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JLabel breachLabel = null;

	private JLabel hOCodeLabel = null;

	private JLabel originalSentenceLabel = null;

	private XDatePanel originalSentencePanel = null;

	private JComboBox hOCodeCb = null;

	private JLabel committalForBreachLabel = null;

	private JLabel originalSentenceDateLabel = null;

	private JTextField originalSentenceText = null;

	private JLabel originalCourtLabel = null;

	private JTextField originalCourtText = null;

	private JLabel bringBackLabel = null;

	private JLabel pleaLabel = null;

	private JLabel datePutLabel = null;

	private XDatePanel datePutPanel = null;

	private JPanel committalForBreachPanel = null;

	private JPanel bringBackPanel = null;

	private JPanel pleaPanel = null;

	private ButtonGroup committalForBreachGroup = null;

	private JRadioButton committalForBreachYesRb = null;

	private JRadioButton committalForBreachNoRb = null;

	private JRadioButton committalForBreachDummyRb = new JRadioButton();

	private ButtonGroup bringBackGroup = null;

	private JRadioButton bringBackYesRb = null;

	private JRadioButton bringBackNoRb = null;

	private JRadioButton bringBackDummyRb = new JRadioButton();

	private ButtonGroup pleaGroup = null;

	private JRadioButton pleaYesRb = null;

	private JRadioButton pleaNoRb = null;

	private JRadioButton pleaNotApplicableRb = null;

	private DefaultComboBoxModel hoCodesModel;

	private JButton courtSearchBtn;

	private JPanel courtPanel = null;

	private ChargesControllerModel model;

	private XhibitApplicationController xac = null;

	private BreachValue breachValue;

	private int mode;

	private RefSystemCodeBasicValue refSystemCodeValue;

	private Collection<RefSystemCodeBasicValue> colHOCodes;

	private boolean committalTypeSelected;

	private boolean sentenceCaseSelected;

	private Integer selectedOriginalCourtID;

	private String selectedOriginalCourtType;

	private String selectedOriginalCourtName;

	private Integer chargeSeqNo = null;

	private BreachController controller;

	private Integer breachID;

	private String caseType;

	private boolean enableBreachTypeSelection = true;

	private boolean back_pressed = false;

	// EDIT
	public BreachPanel(BreachController controller, ChargesControllerModel model) throws CSRecoverableException {
		this.mode = EDIT_MODE;
		this.controller = controller;
		this.model = model;
		this.xac = controller.getXac();
		this.breachValue = model.getBreachValue();
		this.caseType = xac.getApplicationCaseModel().getCaseType();
 
		stepInitialise();

		jbInit();

		stepActivate();
		stepUpdateViewState();
	}

	// ADD
	public BreachPanel(BreachController controller, int mode, BreachValue breachValue) throws CSRecoverableException {
		this.mode = mode;
		this.controller = controller;
		this.xac = controller.getXac();
		this.breachValue = breachValue;
		this.caseType = xac.getApplicationCaseModel().getCaseType();

		stepInitialise();

		jbInit();

		stepActivate();
		stepUpdateViewState();
	}

	// VIEW
	public BreachPanel(ChargesControllerModel model, int mode) throws CSRecoverableException {
		this.mode = mode;
		this.model = model; // Leave for refresh
		this.xac = model.getACM().getXhibitApplicationController();
		this.caseType = model.getACM().getCaseType();

		breachValue = model.getBreachValue();

		chargeSeqNo = model.getChargeValue().getCrestChargeSeqNo();

		stepInitialise();

		jbInit();

		BreachPopupMenu breachPopupMenu = new BreachPopupMenu(this.xac);
		BreachMouseAdapter breachMouseAdapter = new BreachMouseAdapter(this.xac, breachPopupMenu);
		this.addMouseListener(breachMouseAdapter);

		stepActivate();
	}

	public BisRefControllerBeanBusinessDelegate getBizDelegate() {
		return XhibitDelegateHelper.getBizRefDelegate();
	}

	public void stepInitialise() throws CSRecoverableException {
		LOG.debug("in stepInitialise");

		colHOCodes = getHOCodes();
		hoCodesModel = new DefaultComboBoxModel(colHOCodes.toArray());

		committalTypeSelected = true;
	}

	public void stepActivate() throws CSRecoverableException {
		LOG.debug("in stepActivate");
		// call move model to screen
		switch (mode) {
		case VIEW_MODE:
			breachValue = model.getBreachValue(); // Leave for refresh
			chargeSeqNo = model.getChargeValue().getCrestChargeSeqNo();
			moveModelToScreen();
			getBreachLabel().setVisible(true);
			enableScreen(false);
			break;

		case EDIT_MODE:
			breachID = breachValue.getBreachID();
			breachValue = XhibitDelegateHelper.getChargeDelegate().getBreachValue(breachID);
			moveModelToScreen();
			// ensure model is being set with breach
			model.setBreachValue(breachValue);
			enableScreen(true);
			break;

		case ADD_MODE:
			// Check boolean to see if information on this screen(don't want
			// moveModelToScreen() to overwrite!
			if (back_pressed) {
				back_pressed = false;
			} else {
				moveModelToScreen();
			}
			enableScreen(true);
			break;

		default:
			break;
		}
	}

	public void stepUpdateViewState() throws CSRecoverableException {
		LOG.debug("in stepUpdateViewState");

		radioButtonSelectionManagement();

		if (mode == ADD_MODE || mode == EDIT_MODE) {
			if (caseType.equalsIgnoreCase(CASE_TYPE_TRIAL)) {
				String hoProcCode = ((RefSystemCodeBasicValue) getHOCodeCb().getSelectedItem()).getCode();
				// If the selected HO Proc Code is not in the array then the
				// Date Put is required.
				if (Arrays.binarySearch(HO_PROC_CODES, hoProcCode) < 0) {
					getDatePutPanel().setRequired(true);
				} else {
					getDatePutPanel().setRequired(false);
				}
			}
		}

		if (controller != null) {
			controller.stepUpdateViewState();
		}
	}

	/**
	 * XPanel implementation called by XWizardDialog when the next, finish or ok
	 * button is pressed.
	 * 
	 * If you change the code in this method, check the code in the method
	 * breachEnableOK() which controls the enabling of the next and finish
	 * buttons of the wizard (when adding a breach) and the Ok button of the
	 * dialog (when editing a breach)
	 * 
	 * @throws CSValidationException
	 * @throws CSRecoverableException
	 */
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		LOG.debug("in stepValidate");

		getOriginalSentencePanel().stepValidate();
		getDatePutPanel().stepValidate();

		int originalSentenceTextLength = getOriginalSentenceText().getText().length();
		LOG.debug("Orig Sentence length: " + originalSentenceTextLength);

		if (originalSentenceTextLength > ORIGINALTEXT_MAX) {
			int excessChars = originalSentenceTextLength - ORIGINALTEXT_MAX;
			LOG.debug("Excess Chars: " + excessChars);
			// Error message
			JOptionPane.showMessageDialog(xac,
					getString("messageTextOriginalTextError1") + excessChars + " "
							+ getString("messageTextOriginalTextError2"),
					getString("messageTitleOriginalText"), JOptionPane.ERROR_MESSAGE);
			throw new UserCancelException();
		}

		if (!getCommittalForBreachYesRb().isSelected() && !getBringBackYesRb().isSelected()) {
			// Please selected either Committal or Bring back
			JOptionPane.showMessageDialog(xac, getString("messageErrorBreachTypeSelect"),
					getString("messageTitleBreachTypeSelect"), JOptionPane.ERROR_MESSAGE);
			throw new UserCancelException();
		}

		if (caseType.equalsIgnoreCase(CASE_TYPE_TRIAL)) {
			String hoProcCode = ((RefSystemCodeBasicValue) getHOCodeCb().getSelectedItem()).getCode();
			if (Arrays.binarySearch(HO_PROC_CODES, hoProcCode) != -1) {
				if (!getPleaYesRb().isSelected() && !getPleaNoRb().isSelected()) {
					// Please select a Plea (Admitted) either Yes or No.
					JOptionPane.showMessageDialog(xac, getString("messageTextPlea"), getString("messageTitlePlea"),
							JOptionPane.ERROR_MESSAGE);
					throw new UserCancelException();
				}
			}
		}

		// Date Put (Breach Date) must not be in the furure
		if (datePutPanel.getDate() != null && !datePutPanel.getDate().before(Calendar.getInstance())) {
			JOptionPane.showMessageDialog(xac, getString("messageBreachDate"), getString("messageTitlePlea"),
					JOptionPane.ERROR_MESSAGE);
			throw new UserCancelException();
		}
	}

	public void stepDeactivate() throws CSRecoverableException {
		// move screen to model - This should update breachValue on
		// BreachWizardModel
		if (mode == ADD_MODE || mode == EDIT_MODE)
			moveScreenToModel();
	}

	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		LOG.debug("in stepDeinitialise");

		if (update) {
			stepValidate();
			if (mode == ADD_MODE || mode == EDIT_MODE) {
				LOG.debug("Add/Edit Deinitialise\n");

				// ctx-270 Save breach and display any validation failures
				try {
					controller.stepDeinitialise();
				} catch (ChargeValidationException ex) {
					JOptionPane.showMessageDialog(xac, ex.getUserMessage(), getString("messageBreachValidationTitle"),
							JOptionPane.ERROR_MESSAGE);
					throw new UserCancelException();
				}
			}
		}
	}

	private void moveModelToScreen() {
		if (breachValue != null) {
			getBreachLabel().setText("Breach " + chargeSeqNo);

			// H/O Proceeding Code need to use Code perhaps to set item in
			// combo
			if (breachValue.getHoCode() != null) {
				Iterator it = colHOCodes.iterator();
				while (it.hasNext()) {
					refSystemCodeValue = (RefSystemCodeBasicValue) it.next();
					if (breachValue.getHoCode().equals(refSystemCodeValue.getCode())) {
						getHOCodeCb().setSelectedItem(refSystemCodeValue);
						break;
					}
				}
			}

			// Original Sentence text
			if (breachValue.getOriginalSentence() == null) {
				getOriginalSentenceText().setText("");
			} else {
				getOriginalSentenceText().setText(breachValue.getOriginalSentence());
			}

			// Original Sentence Date
			getOriginalSentencePanel().setRequired(true);
			if (breachValue.getOriginalSentenceDate() != null) {
				getOriginalSentencePanel().setDate(breachValue.getOriginalSentenceDate());
			}

			// Original Court Datails are set to 'selectedOriginalCourt'
			// variable for OK
			// see getCourtSearchBtn()

			if (breachValue.getOriginalCourtName() == null) {
				getOriginalCourtText().setText("");
			} else {
				getOriginalCourtText().setText(breachValue.getOriginalCourtName());
			}

			selectedOriginalCourtID = breachValue.getOriginalCourtID();
			selectedOriginalCourtType = breachValue.getOriginalCourtType();
			selectedOriginalCourtName = breachValue.getOriginalCourtName();

			// Breach Type
			if (caseType.equalsIgnoreCase(CASE_TYPE_TRIAL)) {
				// breachType can only be B
				getBringBackYesRb().setSelected(true);
				getCommittalForBreachNoRb().setSelected(true);
				committalTypeSelected = false;
				// disable selection
				//enableBreachTypeSelection = false;
			} else if (caseType.equalsIgnoreCase(CASE_TYPE_SENTENCE)) {
				/*try {
					CaseControllerBeanBusinessDelegate cazeDel = XhibitDelegateHelper.getCaseDelegate();
					CaseBasicValue caze = cazeDel.getCase(breachValue.getCaseID());
					if (caze != null) {
						breachValue.setBreachType(caze.getReceiptType());
					}
				} catch (CaseControllerException e) {
					e.printStackTrace();
				}*/
				if (breachValue.getBreachType() != null) {
					// enable selection
					enableBreachTypeSelection = true;
					// breach type can be B or C
					if (breachValue.getBreachType().equalsIgnoreCase(BREACH_TYPE_BRING_BACK)
							|| breachValue.getBreachType().equals("BB")) {
						getBringBackYesRb().setSelected(true);
						getCommittalForBreachNoRb().setSelected(true);
						committalTypeSelected = false;
						if (xac.getApplicationCaseModel().getScheduledHearingId() == null) {
							sentenceCaseSelected = true;
						}
					} else if (breachValue.getBreachType().equalsIgnoreCase(BREACH_TYPE_COMMITTAL)
							|| breachValue.getBreachType().equals("CS") || breachValue.getBreachType().equals("CB")) {
						getCommittalForBreachYesRb().setSelected(true);
						getBringBackNoRb().setSelected(false);
						committalTypeSelected = true;
						if (xac.getApplicationCaseModel().getScheduledHearingId() == null) {
							sentenceCaseSelected = true;
						}
					}
				}
				getPleaNotApplicableRb().setSelected(true);

			} else // other case type
			{
				getCommittalForBreachNoRb().setSelected(true);
				getBringBackNoRb().setSelected(true);
				committalTypeSelected = true;
				enableBreachTypeSelection = true;
			}

			if (breachValue.getPlea() == null || breachValue.getPlea().equals(BREACH_ADMITTED_BLANK)) {
				if (mode == EDIT_MODE || mode == VIEW_MODE) {
					getPleaNotApplicableRb().setSelected(true);
				}
			} else if (breachValue.getPlea().equalsIgnoreCase(BREACH_ADMITTED_YES)) {
				getPleaYesRb().setSelected(true);
			} else if (breachValue.getPlea().equalsIgnoreCase(BREACH_ADMITTED_NO)) {
				getPleaNoRb().setSelected(true);
			}

			// Date Put
			if (breachValue.getDatePut() != null) {
				getDatePutPanel().setDate(breachValue.getDatePut());
			}
		}
	}

	private void moveScreenToModel() throws CSValidationException {
		breachValue.setCourtLogDate(Calendar.getInstance()); // required??

		// H/O Proceeding Code & Description
		breachValue.setHoCode(((RefSystemCodeBasicValue) getHOCodeCb().getSelectedItem()).getCode());
		breachValue.setHoDescription(((RefSystemCodeBasicValue) getHOCodeCb().getSelectedItem()).getDecode());
		breachValue.setRefSystemCodeID(((RefSystemCodeBasicValue) getHOCodeCb().getSelectedItem()).getId());

		// Original Sentence
		breachValue.setOriginalSentence(getOriginalSentenceText().getText());

		// Original Sentence Date
		breachValue.setOriginalSentenceDate(getOriginalSentencePanel().getDate());

		// Original Court Details set from 'selectedOriginalCourt' variables
		// (see getCourtBtn())
		breachValue.setOriginalCourtID(selectedOriginalCourtID);
		breachValue.setOriginalCourtType(selectedOriginalCourtType);
		breachValue.setOriginalCourtName(selectedOriginalCourtName);
		breachValue.setLastUpdatedBy(
				XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

		if (getCommittalForBreachYesRb().isSelected()) {
			breachValue.setBreachType(BREACH_TYPE_COMMITTAL);
			breachValue.setBringBack(BRING_BACK_COMMITTAL);
		} else if (getBringBackYesRb().isSelected()) {
			breachValue.setBreachType(BREACH_TYPE_BRING_BACK);
			breachValue.setBringBack(BRING_BACK_BRING_BACK);
		} else {
			breachValue.setBreachType("");
			breachValue.setBringBack("");
		}

		if (getPleaYesRb().isSelected()) {
			breachValue.setPlea(BREACH_ADMITTED_YES);
		} else if (getPleaNoRb().isSelected()) {
			breachValue.setPlea(BREACH_ADMITTED_NO);
		} else {
			breachValue.setPlea(null);
		}

		// Date Put
		breachValue.setDatePut(getDatePutPanel().getDate());

		printBreachValue();
	}

	private void enableScreen(boolean enable) {
		// Manual disable
		getHOCodeCb().setEnabled(enable);
		getOriginalSentenceText().setEnabled(enable);
		getCourtSearchBtn().setEnabled(enable);
		getDatePutPanel().setDateEnabled(enable);
		getOriginalSentencePanel().setDateEnabled(enable);
		getPleaYesRb().setEnabled(enable);
		getPleaNoRb().setEnabled(enable);
		getPleaNotApplicableRb().setEnabled(enable);
		if (enableBreachTypeSelection) {
			getBringBackYesRb().setEnabled(enable);
			getBringBackNoRb().setEnabled(enable);
			getCommittalForBreachYesRb().setEnabled(enable);
			getCommittalForBreachNoRb().setEnabled(enable);
			getPleaYesRb().setEnabled(enable);
			getPleaNoRb().setEnabled(enable);
			getPleaNotApplicableRb().setEnabled(enable);
		} else { // these need to be set to false
			getBringBackYesRb().setEnabled(enableBreachTypeSelection);
			getBringBackNoRb().setEnabled(enableBreachTypeSelection);
			getCommittalForBreachYesRb().setEnabled(enableBreachTypeSelection);
			getCommittalForBreachNoRb().setEnabled(enableBreachTypeSelection);
		}

		if (sentenceCaseSelected) {
			getBringBackYesRb().setEnabled(false);
			getBringBackNoRb().setEnabled(false);
			getCommittalForBreachYesRb().setEnabled(false);
			getCommittalForBreachNoRb().setEnabled(false);
			getPleaYesRb().setEnabled(false);
			getPleaNoRb().setEnabled(false);
			getPleaNotApplicableRb().setEnabled(false);
			getDatePutPanel().setEnabled(false);
			datePutPanel.clear();
		}

	}

	private void jbInit() {
		this.setLayout(gridBagLayout1);
		if (mode == VIEW_MODE) {
			this.add(getBreachLabel(), new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		}
		this.add(getHOCodeLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getHOCodeCb(), new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getOriginalSentenceLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getOriginalSentenceText(), new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getOriginalSentenceDateLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getOriginalSentencePanel(), new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getOriginalCourtLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getCourtPanel(), new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 0), 0, 0));

		this.add(getCommittalForBreachLabel(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getCommittalForBreachPanel(), new GridBagConstraints(1, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getBringBackLabel(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getBringBackPanel(), new GridBagConstraints(1, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getPleaLabel(), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getPleaPanel(), new GridBagConstraints(1, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(getDatePutLabel(), new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(getDatePutPanel(), new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
	}

	private JPanel getCourtPanel() {
		if (courtPanel == null) {
			courtPanel = new JPanel();
			courtPanel.setLayout(new GridBagLayout());
			courtPanel.add(getOriginalCourtText(), new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(4, 0, 4, 4), 0, 0));
			courtPanel.add(getCourtSearchBtn(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		}
		return courtPanel;
	}

	private JLabel getDatePutLabel() {
		if (datePutLabel == null) {
			datePutLabel = new JLabel(getString("datePutLabel"));
		}
		return datePutLabel;
	}

	private JLabel getBringBackLabel() {
		if (bringBackLabel == null) {
			bringBackLabel = new JLabel(getString("bringBackLabel"));
		}
		return bringBackLabel;
	}

	private JLabel getPleaLabel() {
		if (pleaLabel == null) {
			pleaLabel = new JLabel(getString("pleaLabel"));
		}
		return pleaLabel;
	}

	private JLabel getOriginalCourtLabel() {
		if (originalCourtLabel == null) {
			originalCourtLabel = new JLabel(getString("originalCourtLabel"));
		}
		return originalCourtLabel;
	}

	private JLabel getOriginalSentenceLabel() {
		if (originalSentenceLabel == null) {
			originalSentenceLabel = new JLabel(getString("originalSentenceLabel"));
		}
		return originalSentenceLabel;
	}

	private JLabel getOriginalSentenceDateLabel() {
		if (originalSentenceDateLabel == null) {
			originalSentenceDateLabel = new JLabel(getString("originalSentenceDateLabel"));
		}
		return originalSentenceDateLabel;
	}

	private JLabel getCommittalForBreachLabel() {
		if (committalForBreachLabel == null) {
			committalForBreachLabel = new JLabel(getString("committalForBreachLabel"));
		}
		return committalForBreachLabel;
	}

	private JLabel getHOCodeLabel() {
		if (hOCodeLabel == null) {
			hOCodeLabel = new JLabel(getString("hOCodeLabel"));
			hOCodeLabel.setFont(new Font("Dialog", 0, 12));
			hOCodeLabel.setMaximumSize(new Dimension(130, 17));
			hOCodeLabel.setMinimumSize(new Dimension(130, 17));
			hOCodeLabel.setPreferredSize(new Dimension(130, 17));
		}
		return hOCodeLabel;
	}

	private JLabel getBreachLabel() {
		if (breachLabel == null) {
			breachLabel = new JLabel();
			breachLabel.setFont(new Font("Dialog", 0, 14));
			breachLabel.setVisible(false);
		}
		return breachLabel;
	}

	private JComboBox getHOCodeCb() {
		if (hOCodeCb == null) {
			hOCodeCb = new JComboBox(hoCodesModel);
			ComboBoxRenderer renderer = new ComboBoxRenderer();
			renderer.setPreferredSize(new Dimension(250, 20));
			hOCodeCb.setRenderer(renderer);
			hOCodeCb.setMinimumSize(new Dimension(200, 20));
			hOCodeCb.addActionListener(new XAction() {
				private static final long serialVersionUID = 1L;

				public void xActionPerformed(ActionEvent ae) {
					try {
						stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return hOCodeCb;
	}

	public JTextField getOriginalSentenceText() {
		if (originalSentenceText == null) {
			originalSentenceText = new JTextField();
			originalSentenceText.setMinimumSize(new Dimension(150, 21));
			originalSentenceText.addKeyListener(new KeyAdapter() {

				public void keyReleased(KeyEvent e) {
					updateScreen(e);
				}
			});
		}
		return originalSentenceText;
	}

	public void processCourtSearch(OpenSearchCourtAction action) throws CSRecoverableException {
		LOG.debug("Breach Panel - processCourtSearch()");
		Collection col = action.getResults();
		Iterator it = col.iterator();
		if (it.hasNext()) {
			Object o = it.next();
			RefCourtBasicValue refCourtBasicValue = (RefCourtBasicValue) o;

			if (refCourtBasicValue == null)
				throw new UserCancelException(); // Need this BAL!!

			LOG.debug("received from search object o of class " + o.getClass());

			// NOTE: breachValue is null for ADD MODE so court info held in
			// below fields
			selectedOriginalCourtID = refCourtBasicValue.getId();

			if (refCourtBasicValue.getCourtType() == null) // court type
			// not always
			// populated
				switch (refCourtBasicValue.getCrestCode().length()) {
				case 3 :
					selectedOriginalCourtType = "C";
					break;
				case 4 :
					selectedOriginalCourtType = "M";
					break;
				default : break;
			} else {
				selectedOriginalCourtType = refCourtBasicValue.getCourtType();
			}

			selectedOriginalCourtName = refCourtBasicValue.getCourtFullName();
			getOriginalCourtText().setText(selectedOriginalCourtName);

			stepUpdateViewState();
		}
	}

	private JButton getCourtSearchBtn() {
		if (courtSearchBtn == null) {
			courtSearchBtn = new JButton(getString("courtSearchBtn"));
			courtSearchBtn.setToolTipText(getString("ttCourtSearchBtn"));
			courtSearchBtn.setMaximumSize(new Dimension(70, 27));
			courtSearchBtn.setMinimumSize(new Dimension(70, 27));

			OpenSearchCourtAction openSearchCourtAction = (OpenSearchCourtAction) XhibitActions.getAction(this.xac,
					XhibitActions.OpenSearchCourt);
			openSearchCourtAction.setCaller(this);
			courtSearchBtn.setAction(openSearchCourtAction);
		}
		return courtSearchBtn;
	}

	public XDatePanel getOriginalSentencePanel() {
		if (originalSentencePanel == null) {
			Calendar blankDate = null;
			originalSentencePanel = new XDatePanel(this, blankDate) {
				private static final long serialVersionUID = 1L;
				public static final String originalSentanceDateNull = "validation.originalSentenceDate.null";
				@Override
				public void stepValidate() throws CSValidationException {
					if (this.getText().length() == 0 && !this.isMandatoryFieldsCompleted()) {
						throw new CSValidationException(originalSentanceDateNull, new String[] { XDateFormat.simpleDateFormat },
								" year not integer");
					}
					super.stepValidate();
				}
			};
			originalSentencePanel.setRequired(false);
		}
		return originalSentencePanel;
	}

	public JTextField getOriginalCourtText() {
		if (originalCourtText == null) {
			originalCourtText = new JTextField();
			originalCourtText.setMinimumSize(new Dimension(150, 21));
			originalCourtText.setEnabled(false);
			originalCourtText.addKeyListener(new KeyAdapter() {

				public void keyReleased(KeyEvent e) {
					updateScreen(e);
				}
			});
		}

		return originalCourtText;
	}

	private JPanel getCommittalForBreachPanel() {
		if (committalForBreachPanel == null) {
			// create ButtonGroup & add buttons
			committalForBreachGroup = new ButtonGroup();
			committalForBreachGroup.add(getCommittalForBreachYesRb());
			committalForBreachGroup.add(getCommittalForBreachNoRb());
			committalForBreachGroup.add(committalForBreachDummyRb);

			// create panel & add buttons to panel
			committalForBreachPanel = new JPanel();
			committalForBreachPanel.setLayout(gridBagLayout2);
			committalForBreachPanel.setMinimumSize(new Dimension(200, 21));
			committalForBreachPanel.setPreferredSize(new Dimension(200, 21));

			committalForBreachPanel.add(getCommittalForBreachYesRb(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
			committalForBreachPanel.add(getCommittalForBreachNoRb(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		}
		return committalForBreachPanel;
	}

	private JRadioButton getCommittalForBreachYesRb() {
		if (committalForBreachYesRb == null) {
			committalForBreachYesRb = new JRadioButton("Yes");
			committalForBreachYesRb.setMinimumSize(new Dimension(46, 21));
			committalForBreachYesRb.setPreferredSize(new Dimension(46, 21));
			committalForBreachYesRb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					committalForBreachYesRb_actionPerformed(e);
				}
			});
		}
		return committalForBreachYesRb;
	}

	private JRadioButton getCommittalForBreachNoRb() {
		if (committalForBreachNoRb == null) {
			committalForBreachNoRb = new JRadioButton("No");
			committalForBreachNoRb.setMinimumSize(new Dimension(46, 21));
			committalForBreachNoRb.setPreferredSize(new Dimension(46, 21));
			committalForBreachNoRb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					committalForBreachNoRb_actionPerformed(e);
				}
			});
		}
		return committalForBreachNoRb;
	}

	private JPanel getBringBackPanel() {
		if (bringBackPanel == null) {
			// create ButtonGroup & add buttons
			bringBackGroup = new ButtonGroup();
			bringBackGroup.add(getBringBackYesRb());
			bringBackGroup.add(getBringBackNoRb());
			bringBackGroup.add(bringBackDummyRb);

			// create panel & add buttons to panel
			bringBackPanel = new JPanel();
			bringBackPanel.setLayout(gridBagLayout2);
			bringBackPanel.setMinimumSize(new Dimension(200, 21));
			bringBackPanel.setPreferredSize(new Dimension(200, 21));

			bringBackPanel.add(getBringBackYesRb(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
			bringBackPanel.add(getBringBackNoRb(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		}
		return bringBackPanel;
	}

	private JPanel getPleaPanel() {
		if (pleaPanel == null) {
			// create ButtonGroup & add buttons
			pleaGroup = new ButtonGroup();
			pleaGroup.add(getPleaYesRb());
			pleaGroup.add(getPleaNoRb());
			pleaGroup.add(getPleaNotApplicableRb());

			// create panel & add buttons to panel
			pleaPanel = new JPanel();
			pleaPanel.setLayout(gridBagLayout2);
			pleaPanel.setMinimumSize(new Dimension(200, 21));
			pleaPanel.setPreferredSize(new Dimension(200, 21));

			pleaPanel.add(getPleaYesRb(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
			pleaPanel.add(getPleaNoRb(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
			pleaPanel.add(getPleaNotApplicableRb(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		}

		return pleaPanel;
	}

	private JRadioButton getBringBackYesRb() {
		if (bringBackYesRb == null) {
			bringBackYesRb = new JRadioButton("Yes");
			bringBackYesRb.setMinimumSize(new Dimension(46, 21));
			bringBackYesRb.setPreferredSize(new Dimension(46, 21));
			bringBackYesRb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					bringBackYesRb_actionPerformed(e);
				}
			});
		}
		return bringBackYesRb;
	}

	private JRadioButton getBringBackNoRb() {
		if (bringBackNoRb == null) {
			bringBackNoRb = new JRadioButton("No");
			bringBackNoRb.setMinimumSize(new Dimension(41, 21));
			bringBackNoRb.setPreferredSize(new Dimension(41, 21));
			bringBackNoRb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					bringBackNoRb_actionPerformed(e);
				}
			});
		}
		return bringBackNoRb;
	}

	private JRadioButton getPleaYesRb() {
		if (pleaYesRb == null) {
			pleaYesRb = new JRadioButton("Yes");
			pleaYesRb.setMinimumSize(new Dimension(46, 21));
			pleaYesRb.setPreferredSize(new Dimension(46, 21));
			pleaYesRb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateScreen(e);
				}
			});
		}
		return pleaYesRb;
	}

	private JRadioButton getPleaNoRb() {
		if (pleaNoRb == null) {
			pleaNoRb = new JRadioButton("No");
			pleaNoRb.setMinimumSize(new Dimension(41, 21));
			pleaNoRb.setPreferredSize(new Dimension(41, 21));
			pleaNoRb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateScreen(e);
				}
			});
		}
		return pleaNoRb;
	}

	private JRadioButton getPleaNotApplicableRb() {
		if (pleaNotApplicableRb == null) {
			pleaNotApplicableRb = new JRadioButton("N/A");
			pleaNotApplicableRb.setMinimumSize(new Dimension(46, 21));
			pleaNotApplicableRb.setPreferredSize(new Dimension(46, 21));
			pleaNotApplicableRb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateScreen(e);
				}
			});
		}
		return pleaNotApplicableRb;
	}

	public XDatePanel getDatePutPanel() {
		if (datePutPanel == null) {
			Calendar blankDate = null;
			datePutPanel = new XDatePanel(this, blankDate);
			datePutPanel.setRequired(false);
			datePutPanel.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return datePutPanel;
	}

	private void printBreachValue() {
		if (breachValue != null) {
			LOG.debug("HO code: " + breachValue.getHoCode() + "\n");
			LOG.debug("HO Description: " + breachValue.getHoDescription() + "\n");
			LOG.debug("Original Sentance Text: " + breachValue.getOriginalSentence() + "\n");
			if (breachValue.getOriginalSentenceDate() != null) {
				LOG.debug("Original Sentance Day: " + breachValue.getOriginalSentenceDate().get(Calendar.DAY_OF_MONTH)
						+ "\n");
				LOG.debug(
						"Original Sentance Month: " + breachValue.getOriginalSentenceDate().get(Calendar.MONTH) + "\n");
				LOG.debug("Original Sentance Year: " + breachValue.getOriginalSentenceDate().get(Calendar.YEAR) + "\n");
			} else
				LOG.debug("Original Sentance Date is null");

			LOG.debug("Original Court Type: " + breachValue.getOriginalCourtType() + "\n");
			LOG.debug("Original Court Name: " + breachValue.getOriginalCourtName() + "\n");
			LOG.debug("Breach Type: " + breachValue.getBreachType() + "\n");

			if (breachValue.getDatePut() != null) {
				LOG.debug("Date Put Day: " + breachValue.getDatePut().get(Calendar.DAY_OF_MONTH) + "\n");
				LOG.debug("Date Put Month: " + breachValue.getDatePut().get(Calendar.MONTH) + "\n");
				LOG.debug("Date Put Year: " + breachValue.getDatePut().get(Calendar.YEAR) + "\n");
			} else
				LOG.debug("Date put is null");

			// Other attributes.
			LOG.debug("Plea: " + breachValue.getPlea());
			LOG.debug("RefSystemCode: " + breachValue.getRefSystemCodeID());
			LOG.debug("BringBack: " + breachValue.getBringBack());
			LOG.debug("ChargeID: " + breachValue.getChargeID());
		}
	}

	class ComboBoxRenderer extends JLabel implements ListCellRenderer {

		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			RefSystemCodeBasicValue refSystemCodeValue = (RefSystemCodeBasicValue) value;
			setHorizontalAlignment(LEFT);
			if (list.getSelectedValue() != null) {
				setText(refSystemCodeValue.getCode() + " - " + refSystemCodeValue.getDecode());
			}
			return this;
		}
	}

	private void updateScreen(@SuppressWarnings("unused") AWTEvent e) {
		try {
			stepUpdateViewState();
		} catch (CSRecoverableException ex) {
			XHIBITErrorHandler.handleError(ex);
		}
	}

	private void committalForBreachYesRb_actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
		try {
			committalTypeSelected = true;
			stepUpdateViewState();
		} catch (CSRecoverableException ex) {
			XHIBITErrorHandler.handleError(ex);
		}
	}

	private void committalForBreachNoRb_actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
		try {
			committalTypeSelected = false;
			stepUpdateViewState();
		} catch (CSRecoverableException ex) {
			XHIBITErrorHandler.handleError(ex);
		}
	}

	private void bringBackYesRb_actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
		try {
			committalTypeSelected = false;
			stepUpdateViewState();
		} catch (CSRecoverableException ex) {
			XHIBITErrorHandler.handleError(ex);
		}
	}

	private void bringBackNoRb_actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
		try {
			committalTypeSelected = true;
			stepUpdateViewState();
		} catch (CSRecoverableException ex) {
			XHIBITErrorHandler.handleError(ex);
		}
	}

	private void radioButtonSelectionManagement() {
		if (committalTypeSelected) {
			getCommittalForBreachYesRb().setSelected(true);
			getBringBackNoRb().setSelected(true);
		} else {
			getCommittalForBreachNoRb().setSelected(true);
			getBringBackYesRb().setSelected(true);
		}
	}

	public static void setChildrenEnabled(Component comp, boolean enabled) {
		comp.setEnabled(enabled);
		if (comp instanceof Container) {
			Container container = (Container) comp;
			for (int idx = 0; idx < container.getComponentCount(); idx++) {
				setChildrenEnabled(container.getComponent(idx), enabled);
			}
		}
	}

	public String getSelectedOriginalCourtName() {
		return selectedOriginalCourtName;
	}

	public void setSelectedOriginalCourtName(String selectedOriginalCourtName) {
		this.selectedOriginalCourtName = selectedOriginalCourtName;
	}

	/**
	 * Called by the controlling wizard or dialog to see if the data entry on
	 * this panel is complete.
	 * 
	 * @return true if it is ok to enable the next or OK buttons.
	 */
	public boolean breachEnableOK() {
		boolean enableOK = true;

		/** @todo Check how this works does BringBack need setting */
		// Committal/Bring Back radio button behaviour
		// radioButtonSelectionManagement();
		// If Text is entered in Original Sentance
		if ((getOriginalSentenceText().getText().trim()).length() == 0) {
			enableOK = false;
		}

		// If Text is entered in Original Court this will be selected using
		// Search Button.
		if ((getOriginalCourtText().getText().trim()).length() == 0) {
			enableOK = false;
		}

		if (getOriginalSentencePanel().isMandatoryFieldsCompleted() == false) {
			enableOK = false;
		}

		if (getDatePutPanel().isMandatoryFieldsCompleted() == false) {
			enableOK = false;
		}

		if (caseType.equalsIgnoreCase(CASE_TYPE_TRIAL)) {
			String hoProcCode = ((RefSystemCodeBasicValue) getHOCodeCb().getSelectedItem()).getCode();
			if (Arrays.binarySearch(HO_PROC_CODES, hoProcCode) < 0) {
				if (!getPleaYesRb().isSelected() && !getPleaNoRb().isSelected()) {
					enableOK = false;
				}
			}
		} else if (caseType.equalsIgnoreCase(CASE_TYPE_SENTENCE)) {
			// if none of the 3 plea radio buttons are selected set enableOK
			// to
			// false, i.e. one of them should be selected for OK to be
			// enabled.
			if (!getPleaYesRb().isSelected() && !getPleaNoRb().isSelected() && !getPleaNotApplicableRb().isSelected()) {
				enableOK = false;
			}
		}

		LOG.debug("breachEnableOK() returning " + enableOK);
		return enableOK;
	}

	private Collection<RefSystemCodeBasicValue> getHOCodes() throws CSRecoverableException {

		Integer courtId = XhibitSingleton.getInstance().getCourtId();

		RefSystemCodeCriteria rcc = new RefSystemCodeCriteria();
		rcc.setCourtId(courtId.toString());
		rcc.setCodeType("HO_PROC_BREACH");
		Collection<RefSystemCodeBasicValue> colHOCodes = getBizDelegate().findSystemCodes(rcc);

		// PR6015: The 'fail to appear' HO code can only be used for
		// 'fail to appear' breaches and not ordinary breaches.
		Iterator<RefSystemCodeBasicValue> itr = colHOCodes.iterator();
		while (itr.hasNext()) {
			RefSystemCodeBasicValue rscbv = itr.next();
			if (rscbv.getCode().equals(HOProcCodeHelper.FAIL_2_APPEAR_HO_CODE)) {
				itr.remove();
			}
		}

		if (colHOCodes instanceof List) {
			List<RefSystemCodeBasicValue> list = (List<RefSystemCodeBasicValue>) colHOCodes;
			Sorter.sort(list, new String[] { "code" });
		}

		return colHOCodes;
	}

	public void setBackPressed(boolean back_pressed) {
		this.back_pressed = back_pressed;
	}

	/**
	 * Gets the BreachValue displayed by this panel. Used by ChargesController
	 * 
	 * @return the BreachValue displayed by this panel.
	 */
	protected BreachValue getBreachValue() {
		return breachValue;
	}

	/**
	 * Get a resource string from the Breach resources
	 * 
	 * @param key
	 *            the key to lookup
	 * @return the resource fro the given key.
	 */
	private String getString(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.Breaches, key);
	}
}