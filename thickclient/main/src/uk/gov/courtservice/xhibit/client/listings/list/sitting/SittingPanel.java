package uk.gov.courtservice.xhibit.client.listings.list.sitting;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchListingJudgeAction;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.util.MultiLineEditField;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractComboBoxValidator;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ComboBoxValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: SittingPanel
 * </p>
 * <p>
 * Description: Panel to create/amend Sittings
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Groen
 * @version 1.0
 * 
 * @amended groenmg 03-04-18
 * CTX-1815 - removed references to predefined text fields as no longer required as part of a sitting
 * @amended groenmg 02-07-18
 * CTX-1787 - if amend judge, check validation regards required judge on each case on sitting
 * CTX-1976 - default time marking and time listed correctly.
 * CTX-2038 - rename save button to OK - i.e. let default to OK
 * @amended groenmg 10-07-18
 * CTX-2380 - bug - added null check for when the default court start time is null
 * CTX-2316 - remove classification drop down combo box
 */
public class SittingPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
		
	private static final String EMPTY_STRING = "";

	private static final boolean ALLOW_EDITABLE = true;
	private static final boolean NOT_EDITABLE = false;
	private static final boolean ALLOW_FOCUS = true;
	private static final boolean NO_FOCUS = false;
	
	private static final int JUDGE_CODE_FIELD_SIZE = 22;
	private static final int TIME_FIELD_SIZE = 2;
	private static final int DEFAULT_FIELD_SIZE = 35;
	private static final int FREETEXT_FIELD_SIZE = 100;
	
	private static final int DEFAULT_TIME_MARKING = 2;
	
	private SittingModel model;
	private XDialog parent;
	
	private AbstractDailyFirmListModel listModel;
	
	private RefJudgeComplexValue refJudgeValue = null;
	
	private JLabel timeMarkingLabel;
	private JLabel timeListedLabel;
	private XComboBox timeMarkingComboBx = null;
	private XTextField timeListedHourText = null;
	private XTextField timeListedMinText = null;
	
	private JLabel judgeCodeErrorLabel;
	private JLabel judgeLabel;
	private XTextField judgeCode;
	private XTextField judgeText = null;
	private JButton judgeSearchButton = null;
	
	private XTextField jp1Text = null;
	private XTextField jp2Text = null;
	private XTextField jp3Text = null;
	private XTextField jp4Text = null;	
	
	private MultiLineEditField freeTxtTextField = null;
		
	private ComboBoxValidationController timeMarkingVC;
	private TextValidationController timeListedHourVC;
	private TextValidationController timeListedMinVC;	
	private TextValidationController judgeCodeVC;
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private boolean initialised = false;
	
	
	private static final Logger log = CSServices.getLogger(SittingPanel.class);
	
	public SittingPanel(SittingDialog parent, SittingModel model, AbstractDailyFirmListModel listModel)
							throws CSRecoverableException {
		
		this.model = model;
		this.parent = parent;
		this.listModel = listModel;

		stepInitialise();
		jbInit();

	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(750, 450));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(700, 400));
		this.add(scrollPane, gbc);
		
		//Ensure validation is run when tab to the cancel button
		parent.setCancelVerifyInputWhenFocusTarget(true);
		
		// Time Panel Element	
		gbc.weighty = 0.10;
		JPanel timeMarkingAndListingPanel = initTimeMarkingAndListingPanel();
		mainPanel.add(timeMarkingAndListingPanel, gbc);
		
		// Judge Panel Element		
		gbc.gridy++;
		gbc.weighty = 0.10;
		JPanel judgeSearchPanel = initJudgeSearchPanel();
		mainPanel.add(judgeSearchPanel, gbc);
	
		// Justice of the Peace Panel Element		
		gbc.gridy++;
		gbc.weighty = 0.55;
		JPanel justiceOfThePeacePanel = initJusticeOfThePeacePanel();
		mainPanel.add(justiceOfThePeacePanel, gbc);
		
		//List Note Panel Element		
		gbc.gridy++;
		gbc.weighty = 0.25;
		JPanel listNotePanel = initListNotePanel();
		mainPanel.add(listNotePanel, gbc);
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();

		// Initialise display
		stepUpdateViewState();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {		
		
		validateRequiredJudge();
			
		//Throw exception if validation failures to prevent saving
		if(!ValidationControllerFactory.validateComponents(validationControllers)){
			throw new CSValidationException("validation.general", "Field validation failed");
		}	
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		moveScreenToModel();

	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
	}
	
	public void processAddJudge(OpenSearchListingJudgeAction action) {
        log.debug("processAddJudge(OpenSearchListingJudgeAction " + action + ")");
        Collection col = action.getResults();
        Iterator it = col.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            try {
                log.debug("Found object " + o + " in the OpenSearchListingJudgeAction's results.");

                // Update the model with the selected judge
                refJudgeValue = ((RefJudgeComplexValue) o);
                if (judgeText != null && refJudgeValue != null){
                	setJudgeCode(refJudgeValue);
                	setJudgeDisplayText(refJudgeValue);
                }
            } catch (final Exception e) {
                log.error("Exception thrown in processAddJudge whilst casting results objects.");
                log.error(e);
            }
        }
    }

	private void setJudgeCode(RefJudgeBasicValue judge) {
		judgeCode.setText(judge !=null ? judge.getCrestJudgeId().toString() : null);
		judgeCodeVC.validate();
	}

	private void setJudgeDisplayText(RefJudgeBasicValue judge) {
		judgeText.setText(judge != null ? StringUtils.join(new String[] {judge.getJudgeType(), judge.getTitle(),
				judge.getFirstName(), judge.getSurname()}, ' ') : null);
	}

	private JPanel initTimeMarkingAndListingPanel() {
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel timeMarkingAndListingPanel = new JPanel();
		timeMarkingAndListingPanel.setLayout(new GridBagLayout());
		
		// validation labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		JLabel timeMarkingError = new JLabel(" ");
		timeMarkingAndListingPanel.add(timeMarkingError, gbc);
		
		gbc.gridx = 3;
		gbc.gridwidth = 6;
		JLabel timeError = new JLabel(" ");
		timeMarkingAndListingPanel.add(timeError, gbc);
		
		gbc.gridwidth = 1;
		gbc.insets = XHIBITConstant.nonContainerInsets;

		//time marking label
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.10;		
		timeMarkingAndListingPanel.add(getTimeMarkingLabel(), gbc);

		gbc.gridx++;
		gbc.weightx = 0.10;
		// Panel - time marking comboBox
		addTimeMarkingComboBox(gbc, timeMarkingAndListingPanel);
		
		timeMarkingVC = ValidationControllerFactory.createComboBox(this, timeMarkingComboBx, timeMarkingError,
				new AbstractComboBoxValidator() {
					@Override
					public void validate(JComboBox target, List<String> errors) {
						setMandatoryLabels();
						if (!hasSelection(target) && eitherTimeComponentEntered()) {
							errors.add("Mandatory if time entered");
						} else if (hasSelection(target) && !isTimeEntered()) {
							errors.add("Clear selection if time not entered");
						}
					}
				});
		validationControllers.add(timeMarkingVC);
		
		// Panel - time text boxes
		gbc.gridx++;
		gbc.weightx = 0.10;
		timeMarkingAndListingPanel.add(getTimeListedLabel(), gbc);
		
		// HOUR text box
		gbc.gridx++;
		gbc.weightx = 0.08;
		timeListedHourText = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );
		timeListedHourText.setNumeric(true);
		timeListedHourText.setMaxLength(TIME_FIELD_SIZE);
		timeListedHourText.setColumns(TIME_FIELD_SIZE);
		timeMarkingAndListingPanel.add(timeListedHourText, gbc);
		
		timeListedHourVC = ValidationControllerFactory.createText(this, timeListedHourText, timeError,
				new AbstractTextValidator() {
					@Override
					public void validate(JTextComponent target, List<String> errors) {
						setMandatoryLabels();
						Integer timeInteger = null;
						if (ValidationUtils.hasText(target)){
							timeInteger =  new Integer(target.getText());
							if (timeInteger < 10){
								target.setText("0" + timeInteger.toString());
							}
						}
						if(timeInteger != null &&  timeInteger > 23){
							errors.add("Invalid time");
						}
						else {	
							timeMarkingVC.validate();							
						}
					}
				});
		validationControllers.add(timeListedHourVC);
		
		//time divider label
		gbc.gridx++;
		gbc.weightx = 0.01;		
		JLabel timeDividerLabel = new JLabel(":");
		timeMarkingAndListingPanel.add(timeDividerLabel, gbc);
		
		// MINUTE text box
		gbc.gridx++;
		gbc.weightx = 0.08;
		timeListedMinText = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );
		timeListedMinText.setNumeric(true);
		timeListedMinText.setMaxLength(TIME_FIELD_SIZE);
		timeListedMinText.setColumns(TIME_FIELD_SIZE);
		timeMarkingAndListingPanel.add(timeListedMinText, gbc);	
		
		timeListedMinVC = ValidationControllerFactory.createText(this, timeListedMinText, timeError,
				new AbstractTextValidator() {
					@Override
					public void validate(JTextComponent target, List<String> errors) {
						Integer timeInteger = null;
						if (ValidationUtils.hasText(target)){
							timeInteger =  new Integer(target.getText());
							if (timeInteger < 10){
								// set time to be 2 digits	
								target.setText("0" + timeInteger.toString());
							}
						}
						if(timeInteger != null &&  timeInteger > 59){									
							errors.add("Invalid time "); 
						}
						else {	
							timeMarkingVC.validate();					
						}
					}
				});
		validationControllers.add(timeListedMinVC);		
			
		// Panel - blank Panel to align fields
		gbc.gridx++;
		gbc.weightx = 0.52;
		timeMarkingAndListingPanel.add(XHIBITConstant.getSpacer(), gbc);
		
		return timeMarkingAndListingPanel;
	}
	
	private JLabel getTimeMarkingLabel() {
		if (timeMarkingLabel == null) {
			setTimeMarkingLabel(false);
		}
		return timeMarkingLabel;
	}
	
	private JLabel getTimeListedLabel() {
		if (timeListedLabel == null) {
			setTimeListedLabel(false);
		}
		return timeListedLabel;
	}
	
	private JLabel getJudgeLabel() {
		if (judgeLabel == null) {
			setJudgeLabel(false);
		}
		return judgeLabel;
	}
	
	private String getResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.Listings, resourceKey);
	}
	
	private void setTimeMarkingLabel(boolean isMandatory) {
		String text = getResourceBundle(isMandatory ? "caseListingSittingTimeMarkingMandatory" : "caseListingSittingTimeMarking");
		if (timeMarkingLabel == null) {
			timeMarkingLabel = new JLabel(text);
		} else {
			timeMarkingLabel.setText(text);
		}
	}
	
	private void setTimeListedLabel(boolean isMandatory) {
		String text = getResourceBundle(isMandatory ? "caseListingSittingTimeListedMandatory" : "caseListingSittingTimeListed");
		if (timeListedLabel == null) {
			timeListedLabel = new JLabel(text);
		} else {
			timeListedLabel.setText(text);
		}
	}
	
	private boolean isJudgeMandatory() {
		return model.getSitting().getJudgeRefId() != null;
	}

	private void setMandatoryLabels() {
		boolean isTimeMandatory = timeMarkingComboBx.getSelectedIndex() > 0 || eitherTimeComponentEntered();
		setTimeMarkingLabel(isTimeMandatory);
		setTimeListedLabel(isTimeMandatory);
		setJudgeLabel(isJudgeMandatory());
	}
	
	private void setJudgeLabel(boolean isMandatory) {
		String text = getResourceBundle(isMandatory ? "caseListingSittingJudgeMandatory" : "caseListingSittingJudge");
		if (judgeLabel == null) {
			judgeLabel = new JLabel(text);
		} else {
			judgeLabel.setText(text);
		}
	}
	private JPanel initJudgeSearchPanel() {
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel judgeSearchPanel = new JPanel();
		judgeSearchPanel.setLayout(new GridBagLayout());

		// Add error labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.weightx = 0.10;
		judgeSearchPanel.add(XHIBITConstant.getSpacer(), gbc);
		gbc.gridx++;
		gbc.gridwidth = 3;
		gbc.weightx = 0.90;
		judgeCodeErrorLabel = new JLabel(" ");
		judgeSearchPanel.add(judgeCodeErrorLabel, gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridwidth = 1;
		
		//judge label
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.10;		
		judgeSearchPanel.add(getJudgeLabel(), gbc);

		gbc.gridx++;
		gbc.weightx = 0.20;
		// Panel - Judge text box
		Document longNumericDoc = DocumentFactory.newDocument(new Capability[] {Capability.unlimitedNumeric(), Capability.limitedText(JUDGE_CODE_FIELD_SIZE)});
		judgeCode = new XTextField(longNumericDoc, "", 0);
		judgeSearchPanel.add(judgeCode, gbc);
		judgeCodeVC = ValidationControllerFactory.createText(this, judgeCode, judgeCodeErrorLabel,
				new AbstractTextValidator() {
					@Override
					public void validate(JTextComponent target, List<String> errors) {
						if (initialised && (!isJudgeCodeValid() || isJudgeMandatory())) {
							getJudgeByCrestId();
							if (refJudgeValue == null && (isJudgeMandatory() || isJudgeCodePopulated())) {
								errors.add(getResourceBundle("caseListingSittingJudgeNotFound"));
							}
						}
					}
				});
		validationControllers.add(judgeCodeVC);
		
		gbc.gridx++;
		gbc.weightx = 0.60;
		// Panel - Judge text box
		judgeText = setXFieldForDisplay(NOT_EDITABLE, NO_FOCUS );
		judgeSearchPanel.add(judgeText, gbc);		
		
		// Panel - Judge search button
		gbc.gridx++;
		gbc.weightx = 0.10;
		XAction openSearchListingJudgeAction = XhibitActions.getAction(model.getXac(), XhibitActions.OpenSearchListingJudge);
		openSearchListingJudgeAction.setCaller(this);
		judgeSearchButton = new JButton(openSearchListingJudgeAction);
		judgeSearchPanel.add(judgeSearchButton, gbc);

		return judgeSearchPanel;
	}
	
	private JPanel initJusticeOfThePeacePanel() {
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel justiceOfThePeacePanel = new JPanel();
		justiceOfThePeacePanel.setLayout(new GridBagLayout());		
		justiceOfThePeacePanel.setBorder(BorderFactory.createTitledBorder(getResourceBundle("caseListingSittingJusticeOfThePeace")));
		
		//JP1 label
		gbc.weightx = 0.10;		
		JLabel jp1Label = new JLabel(getResourceBundle("caseListingSittingJusticeOfThePeace1"));
		justiceOfThePeacePanel.add(jp1Label, gbc);
		
		//JP1 text field
		gbc.gridx++;
		gbc.weightx = 0.90;
		// Panel - Judge text box
		jp1Text = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );
		jp1Text.setMaxLength(DEFAULT_FIELD_SIZE);
		justiceOfThePeacePanel.add(jp1Text, gbc);				
				
		//JP2 label
		gbc.weightx = 0.10;	
		gbc.gridx = 0;
		gbc.gridy++;
		JLabel jp2Label = new JLabel(getResourceBundle("caseListingSittingJusticeOfThePeace2"));
		justiceOfThePeacePanel.add(jp2Label, gbc);
		
		//JP2 text field
		gbc.gridx++;
		gbc.weightx = 0.90;
		// Panel - Judge text box
		jp2Text = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );
		jp2Text.setMaxLength(DEFAULT_FIELD_SIZE);
		justiceOfThePeacePanel.add(jp2Text, gbc);		
		
		//JP3 label
		gbc.weightx = 0.10;	
		gbc.gridx = 0;
		gbc.gridy++;
		JLabel jp3Label = new JLabel(getResourceBundle("caseListingSittingJusticeOfThePeace3"));
		justiceOfThePeacePanel.add(jp3Label, gbc);
		
		//JP3 text field
		gbc.gridx++;
		gbc.weightx = 0.90;
		// Panel - Judge text box
		jp3Text = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );
		jp3Text.setMaxLength(DEFAULT_FIELD_SIZE);
		justiceOfThePeacePanel.add(jp3Text, gbc);	
		
		//JP4 label
		gbc.weightx = 0.10;	
		gbc.gridx = 0;
		gbc.gridy++;
		JLabel jp4Label = new JLabel(getResourceBundle("caseListingSittingJusticeOfThePeace4"));
		justiceOfThePeacePanel.add(jp4Label, gbc);
		
		//JP4 text field
		gbc.gridx++;
		gbc.weightx = 0.90;
		// Panel - Judge text box
		jp4Text = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );	
		jp4Text.setMaxLength(DEFAULT_FIELD_SIZE);
		justiceOfThePeacePanel.add(jp4Text, gbc);
		
		return justiceOfThePeacePanel;
	}

	private JPanel initListNotePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
		GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel listNotePanel = new JPanel();
		listNotePanel.setLayout(new GridBagLayout());
		listNotePanel.setBorder(BorderFactory.createTitledBorder(getResourceBundle("caseListingSittingListNote")));
		
		//reset gbc
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		//free text defined label
		gbc.weightx = 0.07;	
		JLabel freeTxtLabel = new JLabel(getResourceBundle("caseListingSittingListNoteFreeText"));
		listNotePanel.add(freeTxtLabel, gbc);
		
		// free text text field
		gbc.gridx++;
		gbc.weightx = 0.93;
		freeTxtTextField = new MultiLineEditField(2,200);
		freeTxtTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){			
					e.consume();
				}
			}
		});	

		listNotePanel.add(freeTxtTextField, gbc);		
		
		return listNotePanel;
	}

	private void addTimeMarkingComboBox(GridBagConstraints gbc, JPanel timeMarkingAndListingPanel) {
		ArrayList<RefSystemCodeBasicValue> timeMarkingArray = ListingDropdownPopulation.getTimeFormatTypes();
		timeMarkingComboBx = new XComboBox();
		setDropdownBoxArray(timeMarkingComboBx, timeMarkingArray.toArray());
		timeMarkingComboBx.addActionListener(new XAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void xActionPerformed(ActionEvent e) throws Exception {
				setMandatoryLabels();
			}
		});
		timeMarkingAndListingPanel.add(timeMarkingComboBx, gbc);
	}
	
	private XTextField setXFieldForDisplay(boolean editable, boolean focusable) {
		XTextField textField = new XTextField();
		textField.setEditable(editable);
		textField.setFocusable(focusable);		
		return textField;
	}
	
 	/**
 	 * Set the array and if the array is empty disable the dropdown box
 	 */
 	private void setDropdownBoxArray(JComboBox comboBox, final Object[] arrayItems) {
 		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
 		if (comboBox.getSelectedItem() != null) {
 			comboBox.setRenderer(new DropdownBoxCellRender());
 			
 		} else {
 			comboBox.setEnabled(false);
 		}
 	}
 	 		
	private void moveModelToScreen() throws CSRecoverableException {
		initialised = false;
		//set time fields
		Integer timeMarkingId = model.getSitting().getTimeMarkingId();
		if(timeMarkingComboBx != null &&  timeMarkingId != null){
			timeMarkingComboBx.setSelectedItemById(timeMarkingId);
		}
		
		//set hour and minute if time marking set
		if(timeMarkingId != null && timeMarkingId > 0){
			Integer hour = model.getSitting().getTimeListedHour();
			setTimeListedHourText(hour);
			
			Integer min = model.getSitting().getTimeListedMinute();
			setTimeListedMinText(min);
		}
		//else default time marking, hour and minute if new sitting
		else if(model.isNewModel() && model.getSitting().getSittingNumber() == 1) {	
			// get the default court start time
			String courtStartTime = XhibitSingleton.getInstance().getCourtBasicValue().getCourtStartTime();
			if(courtStartTime != null){
				//check in correct format
				String regExpTimePattern = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
				Pattern pattern = Pattern.compile(regExpTimePattern);
				Matcher matcher = pattern.matcher(courtStartTime);
				if(matcher.matches()){
					// set the time marking to "sitting at" and the time from the court start time.
					// set both here if a valid time match, as do not want screen to open in an invalid state
					timeMarkingComboBx.setSelectedIndex(DEFAULT_TIME_MARKING);
					String[] timeComponents = courtStartTime.split(":");
					setTimeListedHourText(new Integer(timeComponents[0]));
					setTimeListedMinText(new Integer(timeComponents[1]));
				}
			}
		}
		
		//set judge fields
		if(model.getSitting().getRefJudge() != null && model.getSitting().getJudgeRefId() != null){
            if (judgeText != null){
            	refJudgeValue = model.getSitting().getRefJudge();
            	setJudgeCode(refJudgeValue);
            	setJudgeDisplayText(refJudgeValue);
            }
        }
		
		// set JP fields
		jp1Text.setText(model.getSitting().getJp1() == null ? "" : model.getSitting().getJp1());
 		jp2Text.setText(model.getSitting().getJp1() == null ? "" : model.getSitting().getJp2());
 		jp3Text.setText(model.getSitting().getJp1() == null ? "" : model.getSitting().getJp3());
 		jp4Text.setText(model.getSitting().getJp1() == null ? "" : model.getSitting().getJp4());
 	 		
		if(freeTxtTextField != null && model.getSitting().getListNoteText() != null &&
				model.getSitting().getListNoteText() != ""){
			freeTxtTextField.setText(model.getSitting().getListNoteText());
		}			
		setMandatoryLabels();
		initialised = true;
	}

	private void setTimeListedMinText(Integer min) {		
		if(timeListedMinText != null && min != null){
			
			if (min < 10){
				timeListedMinText.setText("0" + min.toString());
			}
			else if(min < 60){
				timeListedMinText.setText(min.toString());
			}
		}
	}

	private void setTimeListedHourText(Integer hour) {
		if(timeListedHourText != null && hour != null){					
			if (hour < 10){
				timeListedHourText.setText("0" + hour.toString());
			}
			else if (hour < 24){
				timeListedHourText.setText(hour.toString());
			}					
		}
	}
 	
 	private void moveScreenToModel() {
		// Update the model from the screen
 		// set the dirty flags as saving
 		model.getSitting().setDirty(true);
 		model.setDirty(true);

 		// if time marking selected, store selection and set hour and minute
 		if(timeMarkingComboBx.getSelectedIndex() > 0){
 	 		// Set the time marking id on sitting on list and basic value for display on row
 			RefSystemCodeBasicValue timeMarking = (RefSystemCodeBasicValue) timeMarkingComboBx.getSelectedItem();
 			model.getSitting().setTimeMarkingId(timeMarking.getId()); 			
 			model.getSitting().setTimeMarking(timeMarking); 			

 			// Set the hour and minute components of time listed
 			Integer hourInteger =  new Integer(timeListedHourText.getText()); 
 			Integer minInteger =  new Integer(timeListedMinText.getText());
 			model.getSitting().setTimeListedHour(hourInteger);
 			model.getSitting().setTimeListedMinute(minInteger);
 		}
 		else{
 			// no time entered but we still have to clear time marking and time
 			model.getSitting().setTimeMarkingId(null);
 			model.getSitting().setTimeMarking(null); 			
 			model.getSitting().clearTimeListedTime();
 		}
 		
 		// set judge data
		model.getSitting().setRefJudge(refJudgeValue);
		//set the Judge Id on the sitting object
		model.getSitting().setJudgeRefId(refJudgeValue != null ? refJudgeValue.getId() : null);
 		
 		// set JP data
		model.getSitting().setJp1(jp1Text.getText());
		model.getSitting().setJp2(jp2Text.getText());
		model.getSitting().setJp3(jp3Text.getText());
		model.getSitting().setJp4(jp4Text.getText());
		
		if(ValidationUtils.hasText(freeTxtTextField)){
 			model.getSitting().setListNoteText(freeTxtTextField.getText());
 		}
		else{
			model.getSitting().setListNoteText(null);
		}
 	}
	
	/**
	 * valid if both time field has a value
	 * 
	 * @return time entered
	 */
	private boolean isTimeEntered() {
		return ValidationUtils.hasText(timeListedHourText) && ValidationUtils.hasText(timeListedMinText);
	}
	
	/**
	 * valid if either time field has a value
	 * 
	 * @return either time entered
	 */
	private boolean eitherTimeComponentEntered() {
		return ValidationUtils.hasText(timeListedHourText) || ValidationUtils.hasText(timeListedMinText);
	}
	
	private void validateRequiredJudge() throws CSRecoverableException {
		//check sitting judge has been amended
		if(isNewJudgeAssigned()){
			// validate required Judge requirement on cases assign to this sitting.
			List<CaseOnListComplexValue> caseList = listModel.getCasesOnListForSitting(model.getSitting().getSittingOnListId());		
			Integer newSittingJudge = refJudgeValue.getId();
			
			for(CaseOnListComplexValue caseOnListCV : caseList){
				// Get the judge that is required for the case
				Integer requiredJudge = null;
				if (caseOnListCV.getCaseListingEntry() != null) { 
					requiredJudge = caseOnListCV.getCaseListingEntry().getJudgeId();
				}
				if(requiredJudge!= null) {
					if (!newSittingJudge.equals(requiredJudge)) {
						showWarningConfirmationMsg();
						break;
					}
				}
			}
		}
	}
	
	private void showWarningConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent, getResourceBundle( 
				"caseListingSittingRequiredJudgeConfirmationTitle"), true,
				XMessageBox.ICONQUESTION, getResourceBundle( 
						"caseListingSittingRequiredJudgeConfirmationMessage"), XMessageBox.YESNO,
				XMessageBox.DEFAULTCANCEL);

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}
	
	private boolean isNewJudgeAssigned() {
		Integer modelJudgeId = model.getSitting().getJudgeRefId();
		Integer storedJudgeId = refJudgeValue != null ? refJudgeValue.getId() : null;
		return modelJudgeId != null && storedJudgeId != null && !modelJudgeId.equals(storedJudgeId);
	}

	private boolean isJudgeCodeValid() {
		String screenJudgeCode = isJudgeCodePopulated() ? judgeCode.getText() : EMPTY_STRING;
		String storedJudgeCode = refJudgeValue != null ? refJudgeValue.getCrestJudgeId().toString() : EMPTY_STRING;
		return screenJudgeCode.equals(storedJudgeCode);
	}

	private boolean isJudgeCodePopulated() {
		return judgeCode.getText() != null && !EMPTY_STRING.equals(judgeCode.getText());
	}
	
	private void getJudgeByCrestId() {
		refJudgeValue = null;
		if (isJudgeCodePopulated()) {
			try {
				Integer crestJudgeId = Integer.valueOf(judgeCode.getText());
				if (crestJudgeId != null) {
					refJudgeValue = XhibitDelegateHelper.getBizRefDelegate().findJudgeByCourtIdAndCrestJudgeId(
							XhibitSingleton.getInstance().getCourtId(), crestJudgeId);
				}
			} catch (NumberFormatException ex) {
				refJudgeValue = null;
			}
		}
		setJudgeDisplayText(refJudgeValue);
	}
	
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		// Only enable save if all validations pass
		boolean invalid = (validationController.hasErrors()
				|| !ValidationControllerFactory.validateComponents(validationControllers));
		parent.getButtonPanel().okButton.setEnabled(!invalid);
		
		// Time validation is special case as the two fields share the same error label,
		// so if no errors on the field just validated, ensure any error on other field
		// is still displayed to the user
		if (timeListedHourVC.equals(validationController) && !timeListedHourVC.hasErrors()) {
			timeListedMinVC.showErrors();
		} else if (timeListedMinVC.equals(validationController) && !timeListedMinVC.hasErrors()) {
			timeListedHourVC.showErrors();
		}
	}
}