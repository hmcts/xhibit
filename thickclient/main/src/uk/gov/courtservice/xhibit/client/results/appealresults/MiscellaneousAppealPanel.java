package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import mseries.Calendar.MFieldListener;
import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchCourtAction;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.SystemRefComboBoxRenderer;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JButtonFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: MiscellaneousAppealPanel
 * </p>
 * <p>
 * Description: For entering miscellaneous appeals
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra / Rakesh Lakhani
 * @version $Id: MiscellaneousAppealPanel.java,v 1.35 2006/05/02 11:14:43 qz4rwx
 *          Exp $
 */
public class MiscellaneousAppealPanel extends XPanel implements AppealSaver {
    private static final Logger log = CSServices.getLogger(MiscellaneousAppealPanel.class);

    private static final Dimension DURATION_DIM = new Dimension(50, XHIBITConstant.getLineHeight());

    private static final String APPEAL_RESULT_MISC = "MISC_APP_RESULT";

    private static final String ABANDONED_BEFORE = "AB";

    private static final String NOT_HEARD = "NH";

    private static final String TRANSFERRED_OUT = "TO";

    private static final String EMPTY_STR = "";

    private static final int MAX_DURATION_IN_MINS = 599940;

    private static final Date NULL_DATE = null;

    private ApplicationCaseModel acm;

    private JPanel mainPanel = null;

    private JTextField appealTitleTextField;

    private JLabel courtLabel = null;

    private JLabel hearingStartDateLabel = null;

    private JLabel hearingDurationLabel = null;

    private JLabel hoursLabel = null;

    private JLabel minutesLabel = null;

    private JTextField appellantTextField = null;

    private JTextField caseDescriptionTextField = null;

    private JTextField hoursTextField = null;

    private JTextField minutesTextField = null;

    private JPanel courtPanel = null;

    private JTextField courtText = null;

    private JButton courtSearchBtn = null;

    private XComboBox resultsComboBox = null;

    private XDatePanel resultsDatePanel = null;

    private XDatePanel hearingStartDatePanel = null;

    private JPanel hearingDurationPanel = null;

    private DocumentListener documentListener = null;

    private ItemListener itemListener = null;

    private ResultsRowValue miscellaneousRow = null;

    private Integer selectedCourtID;

    private String selectedCourtCrestCode;

    private String selectedResultCodeType;

    private String selectedCourtFullName;

    private Date resultDate = new Date();

    private Date hearingStartDate = new Date();

    private boolean onLoad = false;

    private AppealResultsModel appealResultsModel;

    private Long duration = null;

    private Object deselectedItem = null;

    /**
     * Constructor
     * 
     * @param rb
     *            the resource bundle for screen text
     */
    public MiscellaneousAppealPanel(ApplicationCaseModel acm, AppealResultsModel appealResultsModel)
            throws CSRecoverableException {
        this.acm = acm;
        this.appealResultsModel = appealResultsModel;
        stepInitialise();
        setupPanel();
    }

    public void stepInitialise() throws CSRecoverableException {
        appealResultsModel.setRefData(AppealResultsHelper.getRefSystemCodes(APPEAL_RESULT_MISC));
    }

    public void stepActivate() {
        // Getting case level results
        setMiscellaneousRow(appealResultsModel.getResultsHelper().getCaseResultsRowValue());

        // Create string with defendant name
        StringBuffer sb = new StringBuffer();
        String[] defendants = acm.getScheduledHearingValue().getDefendants();
        for (int i = 0; i < defendants.length; i++) {
            sb.append(defendants[i]);
            if (i < (defendants.length - 1))
                sb.append(", ");
        }

        // Add information to screen controls
        getAppealTitleTextField().setText(appealResultsModel.getCaseBasicValue().getCaseTitle());
        getCaseDescriptionTextField().setText(appealResultsModel.getCaseBasicValue().getCaseDescription());
        getAppellantTextField().setText(sb.toString());

        if (miscellaneousRow != null) {
            RefSystemCodeBasicValue toSelect = AppealResultsHelper.getRefSystemCodeBasicValue(miscellaneousRow,
                    appealResultsModel.getRefData());
            toSelect = toSelect == null ? AppealResultsHelper.NO_RESULT_TYPE_SELECTED : toSelect;

            setDatePanelsRequired(toSelect.getCode());

            getResultsComboBox().setSelectedItem(toSelect);

            if (miscellaneousRow.getVerdictValue() != null) {
                setResultDate(miscellaneousRow.getVerdictValue().getVerdictDate());
                setHearingStartDate(miscellaneousRow.getVerdictValue().getHearingDate());
                setHearingDuration();

                getCourtText().setText(miscellaneousRow.getVerdictValue().getCccTransToRefCourtDesc());
            } else {
                getResultsDatePanel().setDate(NULL_DATE);
                getHearingStartDatePanel().setDate(NULL_DATE);
                getHoursTextField().setText(EMPTY_STR);
                getMinutesTextField().setText(EMPTY_STR);
            }
        } else {
            getResultsDatePanel().setDate(NULL_DATE);
            getHearingStartDatePanel().setDate(NULL_DATE);
        }

        getMinutesTextField().getDocument().addDocumentListener(getHearingDurationDocumentListener());
        getHoursTextField().getDocument().addDocumentListener(getHearingDurationDocumentListener());

        getCourtSearchBtn().addItemListener(getItemListener());

        getResultsComboBox().addItemListener(getItemListener());

        setModified(false);
    }

    private void setDatePanelsRequired(String verdictCode) {
        log.debug("setDatePanelsRequired verdictCode = [" + verdictCode + "]");
        getResultsDatePanel().setRequired(!EMPTY_STR.equals(verdictCode));
        getHearingStartDatePanel().setRequired(
                !EMPTY_STR.equals(verdictCode) && !NOT_HEARD.equals(verdictCode)
                        && !ABANDONED_BEFORE.equals(verdictCode) && !TRANSFERRED_OUT.equals(verdictCode));
    }

    public void stepUpdateViewState() {
    }

    public void stepValidate() throws UserCancelException, CSValidationException {
        if (miscellaneousRow.getAction() == ResultsRowValue.RESULT_DELETE) {
            boolean rc = XMessageBox.alert(acm.getXhibitApplicationController(), ResourceBundleHelper.getResource(
                    XhibitBundles.AppealResults, "miscellaneous.delete.title"), true, XMessageBox.ICONQUESTION,
                    ResourceBundleHelper.getResource(XhibitBundles.AppealResults, "miscellaneous.delete.message"),
                    XDialog.YESNO, XDialog.DEFAULTNO);

            if (!rc)
                throw new UserCancelException();
        } else {
            duration = calculateHearingDuration();
            if (duration != null) {
                long calculatedDurationInMins = duration.longValue() / 60000;
                if (calculatedDurationInMins > MAX_DURATION_IN_MINS) {
                    throw new CSValidationException("miscappeal.maxdurationexceeded", new Object[] {},
                            "Duration exceeded: " + calculatedDurationInMins);
                }
            }
        }

        if (miscellaneousRow.getVerdictDate() != null && miscellaneousRow.getVerdictValue().getHearingDate() != null) {
            if (miscellaneousRow.getVerdictDate().before(miscellaneousRow.getVerdictValue().getHearingDate())) {
                JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.AppealResults,
                        "miscellaneous.invalidDate.message"), ResourceBundleHelper.getResource(
                        XhibitBundles.AppealResults, "miscellaneous.invalidDate.title"), JOptionPane.ERROR_MESSAGE);
                throw new UserCancelException();
            }
        }
    }

    public void stepDeactivate() {
        getCourtSearchBtn().removeItemListener(getItemListener());
        getResultsComboBox().removeItemListener(getItemListener());
        getHoursTextField().getDocument().removeDocumentListener(getHearingDurationDocumentListener());
        getMinutesTextField().getDocument().removeDocumentListener(getHearingDurationDocumentListener());
    }

    public void stepDeinitialise(boolean save) throws CSValidationException {
        if (save) {
            if (miscellaneousRow.getAction() != ResultsRowValue.RESULT_DELETE) {
                getHearingStartDatePanel().stepValidate();
                getResultsDatePanel().stepValidate();
                if (getResultsDatePanel().getDate().after(Calendar.getInstance())) {
                    throw new CSValidationException("validation.date.maxinclusive", new String[] { XDateFormat.format(
                            getResultsDatePanel().getDate(), XDateFormat.DATEFORMAT) },
                            "The result date entered is in the future - not allowed");
                }
            }
        }
    }

    public void populateResultsSaveValue(ResultsSaveValue resultsSaveValue) throws CSValidationException,
            CSRecoverableException {
        stepValidate();
        stepDeactivate();
        stepDeinitialise(true);
        AppealResultsHelper.setAlteredFlag(miscellaneousRow, resultsSaveValue, appealResultsModel, false);
    }

    /**
     * Sets ResultsRowValue for Case.
     * 
     * @param miscellaneousRow
     */
    public void setMiscellaneousRow(ResultsRowValue miscellaneousRow) {
        this.miscellaneousRow = miscellaneousRow;
        VerdictValue verdictValue = miscellaneousRow.getVerdictValue();
        if (verdictValue != null) {
            selectedCourtID = verdictValue.getCccTransToRefCourtId();
            selectedCourtCrestCode = verdictValue.getCccTransToRefCourtCode();
            selectedCourtFullName = verdictValue.getCccTransToRefCourtDesc();
            selectedResultCodeType = verdictValue.getRefVerdictCode();
            onLoad = true;
            enableControls();
            onLoad = false;
        }
    }

    /**
     * Retrieves ResultsRowValue for case.
     * 
     * @return ResultsRowValue
     */
    public ResultsRowValue getMiscellaneousRow() {
        return miscellaneousRow;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
        getResultsDatePanel().setDate(resultDate);
    }

    public void setHearingStartDate(Date hearingStartDate) {
        this.hearingStartDate = hearingStartDate;
        getHearingStartDatePanel().setDate(hearingStartDate);
    }

    /**
     * sets up this panel
     */
    private void setupPanel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        setLayout(gbl);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = XHIBITConstant.nonContainerInsets;
        gbc.weightx = 1.0f;
        gbc.weighty = 1.0f;
        add(getMainPanel(), gbc);
    }

    /**
     * get the panel for the main fields
     * 
     * @return the main panel
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();

            GridBagLayout gbl = new GridBagLayout();
            mainPanel.setLayout(gbl);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.insets = XHIBITConstant.nonContainerInsets;
            gbc.weightx = 0.0f;
            gbc.weighty = 0.0f;
            gbc.gridwidth = 1;

            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            gbc.gridy = 0;
            mainPanel.add(new JLabel(getString("miscellaneous.appealTitleLabel")), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            mainPanel.add(new JLabel(getString("miscellaneous.appellantLabel")), gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            mainPanel.add(new JLabel(getString("miscellaneous.caseDescriptionLabel")), gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            mainPanel.add(new JLabel(getString("miscellaneous.resultsLabel")), gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            mainPanel.add(new JLabel(getString("miscellaneous.resultsDateLabel")), gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            mainPanel.add(getHearingStartDateLabel(), gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            mainPanel.add(getHearingDurationLabel(), gbc);

            gbc.gridx = 0;
            gbc.gridy = 7;
            mainPanel.add(getCourtLabel(), gbc);

            // Text/Combos
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 1;
            gbc.gridy = 0;
            mainPanel.add(getAppealTitleTextField(), gbc);

            gbc.gridwidth = 2;
            gbc.gridx = 1;
            gbc.gridy = 1;
            mainPanel.add(getAppellantTextField(), gbc);

            gbc.gridwidth = 2;
            gbc.gridx = 1;
            gbc.gridy = 2;
            mainPanel.add(getCaseDescriptionTextField(), gbc);

            gbc.gridwidth = 2;
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.weightx = 1.0f;
            mainPanel.add(getResultsComboBox(), gbc);

            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 2;
            gbc.gridx = 1;
            gbc.gridy = 4;
            mainPanel.add(getResultsDatePanel(), gbc);

            gbc.gridwidth = 2;
            gbc.gridx = 1;
            gbc.gridy = 5;
            mainPanel.add(getHearingStartDatePanel(), gbc);

            gbc.insets = new Insets(-2, 4, 4, 4);
            gbc.gridwidth = 1;
            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.weightx = 1.0f;
            mainPanel.add(getHearingDurationPanel(), gbc);

            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;
            gbc.gridx = 1;
            gbc.gridy = 7;
            mainPanel.add(getCourtPanel(), gbc);
        }

        return mainPanel;
    }

    /**
     * gets the results hearingStartDateLabel
     * 
     * @return the hearingStartDateLabel
     */
    private JLabel getHearingStartDateLabel() {
        if (hearingStartDateLabel == null) {
            hearingStartDateLabel = new JLabel(getString("miscellaneous.hearingStartDateLabel"));
        }
        return hearingStartDateLabel;
    }

    /**
     * gets the results hearingDurationLabel
     * 
     * @return the hearingDurationLabel
     */
    private JLabel getHearingDurationLabel() {
        if (hearingDurationLabel == null) {
            hearingDurationLabel = new JLabel(getString("miscellaneous.hearingDurationLabel"));
        }
        return hearingDurationLabel;
    }

    /**
     * gets the results label
     * 
     * @return the results label
     */
    private JLabel getCourtLabel() {
        if (courtLabel == null) {
            courtLabel = new JLabel(getString("miscellaneous.courtLabel"));
        }

        return courtLabel;
    }

    /**
     * gets the appeal title text field
     * 
     * @return the appeal title text field
     */
    public JTextField getAppealTitleTextField() {
        if (appealTitleTextField == null) {
            appealTitleTextField = new JTextField();
            appealTitleTextField.setEditable(false);
        }

        return appealTitleTextField;
    }

    /**
     * gets the appellant text field
     * 
     * @return the appellant text field
     */
    public JTextField getAppellantTextField() {
        if (appellantTextField == null) {
            appellantTextField = new JTextField();
            appellantTextField.setEditable(false);
        }

        return appellantTextField;
    }

    /**
     * gets the case description text field
     * 
     * @return the case description text field
     */
    public JTextField getCaseDescriptionTextField() {
        if (caseDescriptionTextField == null) {
            caseDescriptionTextField = new JTextField();
            caseDescriptionTextField.setEditable(false);
        }

        return caseDescriptionTextField;
    }

    /**
     * gets the results combo box
     * 
     * @return the results combo box
     */
    public XComboBox getResultsComboBox() {
        if (resultsComboBox == null) {
            resultsComboBox = new XComboBox();
            resultsComboBox.setModel(new DefaultComboBoxModel(appealResultsModel.getRefData().toArray()));
            resultsComboBox.setRenderer(new SystemRefComboBoxRenderer());
            resultsComboBox.setPreferredSize(new Dimension(0, getAppealTitleTextField().getPreferredSize().height));
        }

        return resultsComboBox;
    }

    /**
     * gets the resultsDatePanel
     * 
     * @return the resultsDatePanel
     */
    public XDatePanel getResultsDatePanel() {
        if (resultsDatePanel == null) {
            resultsDatePanel = new XDatePanel(this);
            resultsDatePanel.getDateComponent().addMChangeListener(new MChangeListener() {
                public void valueChanged(MChangeEvent e) {
                    if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
                        // If the chooser is being opened or closed the
                        // date will not have
                        // changed.
                        return;
                    }
                    if (e.getValue() != null) {
                        processResultsDateEvent();
                    }
                }
            });
            resultsDatePanel.getDateComponent().addMFieldListener(new MFieldListener() {
                public void fieldEntered(FocusEvent fe) {
                }

                public void fieldExited(FocusEvent fe) {
                    if (!fe.isTemporary()) {
                        processResultsDateEvent();
                    }
                }
            });
        }
        return resultsDatePanel;
    }

    /**
     * gets the results getHearingStartDatePanel
     * 
     * @return the getHearingStartDatePanel
     */
    public XDatePanel getHearingStartDatePanel() {
        if (hearingStartDatePanel == null) {
            hearingStartDatePanel = new XDatePanel(this);
            hearingStartDatePanel.getDateComponent().addMChangeListener(new MChangeListener() {
                public void valueChanged(MChangeEvent e) {
                    if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
                        // If the chooser is being opened or closed the
                        // date will not have
                        // changed.
                        return;
                    }
                    if (e.getValue() != null) {
                        processHearingStartDateEvent();
                    }
                }
            });
            hearingStartDatePanel.getDateComponent().addMFieldListener(new MFieldListener() {
                public void fieldEntered(FocusEvent fe) {
                    // not currently required.
                }

                public void fieldExited(FocusEvent fe) {
                    if (!fe.isTemporary()) {
                        processHearingStartDateEvent();
                    }
                }
            });
        }
        return hearingStartDatePanel;
    }

    /**
     * gets the results hearingDurationPanel
     * 
     * @return the hearingDurationPanel
     */
    public JPanel getHearingDurationPanel() {
        if (hearingDurationPanel == null) {
            hearingDurationPanel = new JPanel(new GridBagLayout());
            hearingDurationPanel.add(getHoursTextField(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 4), 0, 0));
            hearingDurationPanel.add(getHoursLabel(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            hearingDurationPanel.add(getMinutesTextField(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            hearingDurationPanel.add(getMinutesLabel(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        }
        return hearingDurationPanel;
    }

    public JTextField getHoursTextField() {
        if (hoursTextField == null) {
            hoursTextField = JTextFieldFactory.getTextField(DocumentFactory.newDocument(new Capability[] {
                    Capability.longNumeric(), Capability.limitedText(5) }));
            hoursTextField.setMinimumSize(DURATION_DIM);
            hoursTextField.setPreferredSize(DURATION_DIM);
        }
        return hoursTextField;
    }

    private JLabel getHoursLabel() {
        if (hoursLabel == null) {
            hoursLabel = new JLabel(getString("miscellaneous.hoursLabel"));
        }
        return hoursLabel;
    }

    public JTextField getMinutesTextField() {
        if (minutesTextField == null) {
            minutesTextField = JTextFieldFactory.getTextField(DocumentFactory.newDocument(new Capability[] {
                    Capability.longNumeric(), Capability.limitedText(2) }));
            minutesTextField.setMinimumSize(DURATION_DIM);
            minutesTextField.setPreferredSize(DURATION_DIM);
        }
        return minutesTextField;
    }

    private JLabel getMinutesLabel() {
        if (minutesLabel == null) {
            minutesLabel = new JLabel(getString("miscellaneous.minutesLabel"));
        }
        return minutesLabel;
    }

    /**
     * Get pnel which allows selection and display of Transferred to court
     * 
     * @return
     */
    private JPanel getCourtPanel() {
        if (courtPanel == null) {
            courtPanel = new JPanel();
            courtPanel.setLayout(new GridBagLayout());
            courtPanel.add(getCourtText(), new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 4), 0, 0));
            courtPanel.add(getCourtSearchBtn(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        }
        return courtPanel;
    }

    /**
     * Returns CourtTExt field
     * 
     * @return JTextField
     */
    public JTextField getCourtText() {
        if (courtText == null) {
            courtText = new JTextField();
            courtText.setMinimumSize(new Dimension(200, 21));
            courtText.setPreferredSize(new Dimension(250, 21));
            courtText.setEditable(false);
        }
        return courtText;
    }

    /**
     * Returns courtSearchBtn
     * 
     * @return Jbutton
     */
    public JButton getCourtSearchBtn() {
        if (courtSearchBtn == null) {
            OpenSearchCourtAction openSearchCourtAction = (OpenSearchCourtAction) XhibitActions.getAction(this.acm
                    .getXhibitApplicationController(), XhibitActions.OpenSearchCourt);
            openSearchCourtAction.setCaller(this);

            courtSearchBtn = JButtonFactory.getButton(openSearchCourtAction);
            courtSearchBtn.setToolTipText(getString("miscellaneous.ttCourtSearchBtn"));
            courtSearchBtn.setMaximumSize(new Dimension(70, 27));
            courtSearchBtn.setMinimumSize(new Dimension(70, 27));

            courtSearchBtn.setEnabled(false);
        }
        return courtSearchBtn;
    }

    /**
     * Get the AppealResults resource string for the supplied key.
     * 
     * @param key
     *            the rsource string key to lookup.
     * @return the resource string
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AppealResults, key);
    }

    /**
     * get the document listener for the judges text modifications
     * 
     * @param sm
     *            the class which needs to know about modifications
     * @return the document listener
     */
    public DocumentListener getHearingDurationDocumentListener() {
        if (documentListener == null) {
            documentListener = new DocumentAdapter() {
                public void insertUpdate(DocumentEvent de) {
                    populateDuration();
                    enableControls();
                }

                public void removeUpdate(DocumentEvent de) {
                    populateDuration();
                    enableControls();
                }

                private void populateDuration() {
                    if (miscellaneousRow.getVerdictValue() != null) {
                        miscellaneousRow.getVerdictValue().setLastCalculatedDuration(calculateHearingDuration());
                        if (miscellaneousRow.getAction() == ResultsRowValue.RESULT_UNCHANGED) {
                            miscellaneousRow.setAction(ResultsRowValue.RESULT_UPDATE);
                        }
                    }
                }
            };
        }

        return documentListener;
    }

    /**
     * get the item listener for selection changes in the combo box
     * 
     * @param sm
     *            the class that needs to know about changes
     * @return the item listener
     */
    public ItemListener getItemListener() {
        if (itemListener == null) {
            itemListener = new ItemListener() {
                public void itemStateChanged(ItemEvent ie) {
                    processItemSelection(ie);
                }
            };
        }

        return itemListener;
    }

    private void processItemSelection(ItemEvent ie) {
        selectedResultCodeType = ((RefSystemCodeBasicValue) getResultsComboBox().getSelectedItem()).getCode();

        Object item = ie.getItem();
        if (ie.getStateChange() == ItemEvent.SELECTED) {
            if (item == deselectedItem) {
                return;
            }

            VerdictValue verdictValue = miscellaneousRow.getVerdictValue();
            if (verdictValue == null) {
                // Check if previous action was a Delete. If so we want to
                // restore previous pleaValue.
                if (miscellaneousRow.getAction() == ResultsRowValue.RESULT_DELETE) {
                    verdictValue = miscellaneousRow.getDeleteVerdictValue();
                    miscellaneousRow.setAction(ResultsRowValue.RESULT_UPDATE);
                } else {
                    // Adding new verdict
                    miscellaneousRow.setAction(ResultsRowValue.RESULT_ADD);
                    verdictValue = new VerdictValue();
                    verdictValue.setCaseId(acm.getCaseId());
                    // verdictValue.setVerdictDate(resultDate);
                    miscellaneousRow.setVerdictValue(verdictValue);
                }
            } else {
                // Check if row is being added but plea is changed before
                // saving.
                if (miscellaneousRow.getAction() != ResultsRowValue.RESULT_ADD) {
                    miscellaneousRow.setAction(ResultsRowValue.RESULT_UPDATE);
                }
            }

            if (item != null || !item.equals(EMPTY_STR)) {
                RefSystemCodeBasicValue rscbv = (RefSystemCodeBasicValue) item;
                setDatePanelsRequired(rscbv.getCode());

                if (rscbv.getCode() != null && !rscbv.getCode().equals(EMPTY_STR)) {
                    verdictValue.setRefVerdictId(rscbv.getId());
                    verdictValue.setRefVerdictCode(rscbv.getCode());
                    verdictValue.setRefVerdictDesc(rscbv.getDecode());
                    verdictValue.setRefVerdictType(rscbv.getCodeType());
                    // need here to set this as it could be set before
                    // result has been selected!!
                    // verdictValue.setVerdictDate(resultDate);

                    // Ensure hearing date is set to null when this is
                    // selected
                    if ((rscbv.getCode().equals(ABANDONED_BEFORE) || rscbv.getCode().equals(NOT_HEARD))) {
                        verdictValue.setHearingDate(NULL_DATE);
                        clearHearingDateandDuration();
                    } else {
                        if (!(rscbv.getCode().equals(TRANSFERRED_OUT))) {
                            verdictValue.setHearingDate(hearingStartDate);
                            verdictValue.setLastCalculatedDuration(calculateHearingDuration());
                        }
                    }

                    if (!rscbv.getCode().equals(TRANSFERRED_OUT)) {
                        clearCourtDetails();
                    }
                } else {
                    verdictValue.setHearingDate(NULL_DATE);
                    verdictValue.setVerdictDate(NULL_DATE);
                    miscellaneousRow.setAction(ResultsRowValue.RESULT_DELETE);
                    miscellaneousRow.setDeleteVerdictValue(verdictValue);
                    getResultsDatePanel().setDate(NULL_DATE);
                    clearHearingDateandDuration();
                    verdictValue = null;
                }
            } else {
                // item should always be a RefSystemCodeBasicValue
            }
            enableControls();
        } else {
            // ItemEvent.Deselected
            deselectedItem = item;
        }
    }

    /**
     * Set ResultDate on Verdict Value from ResultsDatePanel if verdict value is
     * null then resultDate will be set when result is selected and a new
     * verdict value is created.
     */
    private void processResultsDateEvent() {
        log.debug("processResultsDateEvent - Begin");
        try {
            resultDate = getResultsDatePanel().getDate() == null ? null : getResultsDatePanel().getDate().getTime();
            VerdictValue verdictValue = miscellaneousRow.getVerdictValue();

            // Don't process if data has not changed.
            if (resultDate == null) {
                log.debug("processResultsDateEvent - verdictValue = " + verdictValue);
                if (verdictValue == null)
                    return;
                log.debug("processResultsDateEvent - verdict date = " + verdictValue.getVerdictDate());
                if (verdictValue.getVerdictDate() == null)
                    return;
            } else if (verdictValue != null) {
                log.debug("processResultsDateEvent - resultDate = " + resultDate);
                if (resultDate.equals(verdictValue.getVerdictDate()))
                    return;
                log.debug("processResultsDateEvent new date selected");
            }

            if (verdictValue != null) {
                verdictValue.setVerdictDate(resultDate);
            }
            if (miscellaneousRow.getAction() == ResultsRowValue.RESULT_UNCHANGED) {
                miscellaneousRow.setAction(ResultsRowValue.RESULT_UPDATE);
            }
            enableControls();
        } catch (CSValidationException csve) {
            XHIBITErrorHandler.handleError(csve);
        }
    }

    /**
     * Set ResultDate on Verdict Value from HearingStartDatePanel if verdict
     * value is null then hearingDate will be set when result is selected and a
     * new verdict value is created.
     */
    private void processHearingStartDateEvent() {
        try {
            hearingStartDate = getHearingStartDatePanel().getDate() == null ? null : getHearingStartDatePanel()
                    .getDate().getTime();

            VerdictValue verdictValue = miscellaneousRow.getVerdictValue();

            // Don't process if data has not changed.
            if (hearingStartDate == null) {
                if (verdictValue == null)
                    return;
                if (verdictValue.getHearingDate() == null)
                    return;
            } else if (verdictValue != null) {
                if (hearingStartDate.equals(verdictValue.getHearingDate()))
                    return;
            }

            // set hearingStartDate on verdcitValue
            if (verdictValue != null) {
                verdictValue.setHearingDate(hearingStartDate);
            }
            if (miscellaneousRow.getAction() == ResultsRowValue.RESULT_UNCHANGED) {
                miscellaneousRow.setAction(ResultsRowValue.RESULT_UPDATE);
            }
            // set Action
            if (miscellaneousRow.getAction() == ResultsRowValue.RESULT_UNCHANGED) {
                miscellaneousRow.setAction(ResultsRowValue.RESULT_UPDATE);
            }

            enableControls();
        } catch (CSValidationException csve) {
            XHIBITErrorHandler.handleError(csve);
        }
    }

    /**
     * Breaks down duration into hours and minutes and sets to miscellaneos
     * panel If the hours and minutes fields are both empty then return null -
     * this will occur where the result is 'AB' or 'NH'.
     * 
     * @return the hearing duration.
     */
    private Long calculateHearingDuration() {
        Long hearingDuration = null;
        if (getHoursTextField().getText().trim().length() > 0 || getMinutesTextField().getText().trim().length() > 0) {
            long newDuration = 0;
            if (getHoursTextField().getText().trim().length() > 0) {
                newDuration = Long.parseLong(getHoursTextField().getText()) * 60 * 60 * 1000;
            }
            if (getMinutesTextField().getText().trim().length() > 0) {
                newDuration += Long.parseLong(getMinutesTextField().getText()) * 60 * 1000;
            }
            if (newDuration >= 0) {
                hearingDuration = new Long(newDuration);
            }
        }
        return hearingDuration;
    }

    /**
     * Process return result from Search Court Action
     * 
     * @param action
     * @throws CSRecoverableException
     */
    public void processCourtSearch(OpenSearchCourtAction action) throws UserCancelException {
        log.debug("MiscellaneousAppealPanel - processCourtSearch()");
        Collection col = action.getResults();
        Iterator it = col.iterator();
        if (it.hasNext()) {
            Object o = it.next();
            log.debug("received from search object o of class " + o.getClass());
            RefCourtBasicValue refCourtBasicValue = (RefCourtBasicValue) o;

            if (refCourtBasicValue == null)
                throw new UserCancelException(); // Need this BAL!!

            selectedCourtID = refCourtBasicValue.getId();
            selectedCourtCrestCode = refCourtBasicValue.getCrestCode();
            selectedCourtFullName = refCourtBasicValue.getCourtFullName();
            getCourtText().setText(selectedCourtFullName);
            VerdictValue verdictValue = miscellaneousRow.getVerdictValue();

            if (verdictValue != null) {
                verdictValue.setCccTransToRefCourtId(selectedCourtID);
                verdictValue.setCccTransToRefCourtCode(selectedCourtCrestCode);
                verdictValue.setCccTransToRefCourtDesc(selectedCourtFullName);

                if (miscellaneousRow.getAction() != ResultsRowValue.RESULT_ADD) {
                    miscellaneousRow.setAction(ResultsRowValue.RESULT_UPDATE);
                }
            }

            enableControls();
        }
    }

    private void clearCourtDetails() {
        // screen controls
        getCourtText().setText(EMPTY_STR);
        getCourtSearchBtn().setEnabled(false);
        getCourtPanel().setEnabled(false);

        // instance variables
        selectedCourtFullName = null;
        selectedCourtCrestCode = null;
        selectedCourtID = null;
        if (miscellaneousRow.getVerdictValue() != null) {
            miscellaneousRow.getVerdictValue().setCccTransToRefCourtId(null);
            miscellaneousRow.getVerdictValue().setCccTransToRefCourtCode(null);
            miscellaneousRow.getVerdictValue().setCccTransToRefCourtDesc(null);
        }
    }

    private void enableControls() {
        log.debug("enableControls - Begin");
        boolean codeTypeEnabled = false;

        if (selectedResultCodeType != null && !selectedResultCodeType.equals(EMPTY_STR)) {
            getResultsDatePanel().getDateComponent().setEnabled(true);

            // enable startDate and duration fields
            boolean hasNoHearingDetails = selectedResultCodeType.equals(ABANDONED_BEFORE)
                    || selectedResultCodeType.equals(NOT_HEARD);

            getHearingStartDatePanel().getDateComponent().setEnabled(!hasNoHearingDetails);
            getHoursTextField().setEnabled(!hasNoHearingDetails);
            getMinutesTextField().setEnabled(!hasNoHearingDetails);

            // Do not fire validation as hearing date for TO is optional
            getHearingStartDatePanel().setRequired(
                    selectedResultCodeType != null && !selectedResultCodeType.equals(TRANSFERRED_OUT)
                            && !hasNoHearingDetails);

            if (selectedResultCodeType.equals(TRANSFERRED_OUT)) {
                getCourtSearchBtn().setEnabled(true);
                getCourtPanel().setEnabled(true);
                if (selectedCourtID != null) {
                    getCourtText().setText(selectedCourtFullName);
                    codeTypeEnabled = !onLoad;
                } else {
                    codeTypeEnabled = false;
                }
            } else {
                codeTypeEnabled = !onLoad;
            }
        } else // no selected code type
        {
            getResultsDatePanel().getDateComponent().setEnabled(false);
            getHearingStartDatePanel().getDateComponent().setEnabled(false);
            getHoursTextField().setEnabled(false);
            getMinutesTextField().setEnabled(false);
            clearCourtDetails();
            codeTypeEnabled = !onLoad;
        }

        // SET MODIFIED
        // result deleted
        if (selectedResultCodeType == null || selectedResultCodeType.equals(EMPTY_STR)) {
            setModified(true);
            return;
        }

        // code Type has been changed and results date valid
        if (codeTypeEnabled && getResultsDatePanel().isMandatoryFieldsCompleted()) {
            // result AB, NH or TO
            if (selectedResultCodeType.equals(ABANDONED_BEFORE) || selectedResultCodeType.equals(NOT_HEARD)
                    || selectedResultCodeType.equals(TRANSFERRED_OUT)) {
                setModified(true);
                return;
            }

            // result of type AA, AC, AD or PH (or TO see above todo)
            if (getHearingStartDatePanel().isMandatoryFieldsCompleted()
                    && ((getHoursTextField().getText().trim().length() > 0 || getMinutesTextField().getText().trim()
                            .length() > 0))) {
                setModified(true);
                return;
            }
        }

        setModified(false);
    }

    /**
     * breaks down milliseconds into hours and minutes
     */
    private void setHearingDuration() {
        log.debug("setHearingDuration duration = " + miscellaneousRow.getVerdictValue().getLastCalculatedDuration());
        if (miscellaneousRow.getVerdictValue().getLastCalculatedDuration() != null) {
            long durationMilliseconds = miscellaneousRow.getVerdictValue().getLastCalculatedDuration().longValue();
            long durationMinutes = durationMilliseconds / (60 * 1000);
            long hours = durationMinutes / 60;
            long minutes = durationMinutes % 60;
            getHoursTextField().setText(hours + EMPTY_STR);
            getMinutesTextField().setText(minutes + EMPTY_STR);
        }
    }

    private void clearHearingDateandDuration() {
        log.debug("clearHearingDateandDuration");
        // clear and disable hearing start date and duration fields
        getHearingStartDatePanel().setDate(NULL_DATE);
        getHoursTextField().setText("");
        getMinutesTextField().setText("");
        getHearingStartDatePanel().setEnabled(false);
        getHearingStartDatePanel().getDateComponent().setEnabled(false);
        getHoursTextField().setEnabled(false);
        getMinutesTextField().setEnabled(false);
    }

}