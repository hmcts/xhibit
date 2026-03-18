package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.helper.FormattedDisplayHelper;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchJudgeAction;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DateChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsFactory;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateKeyListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.LongAdjournDirectionsPanel;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Panel used to create a Court Log Entry for a long adjournment
 * </p>
 * <p>
 * Description: The panel forms part of a dialog to create/edit court log
 * events. It also forms part of the Directions for Defendant Panel (Pleas &
 * Directions screen) and Events for Defendant Panel (Preliminary Hearings)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 */
public class LongAdjournmentPanel extends CourtLogEventPanel {
    private String laoSchema, laoType, laoDate, laoName, laoDeftId, laoPSRRequired, laoReservedJudge, laoNotReserved;

    private LongAdjournmentModel model;

    private Collection pullDownList01 = new Vector();

    private JComboBox subEventCb;

    private JLabel adjournmentDateLabel;

    private JLabel longAdjounmentOptionsLabel;

    private JLabel judgesNameLabel;

    private JLabel eventNameLabel;

    private JLabel psrRequiredLabel;

    private JLabel allDefendantsLabel;

    private JTextField judgesNameText;

    private JCheckBox psrRequiredCbx;

    private JRadioButton reservedToJudgeRb;

    private JRadioButton notReservedRb;

    private JRadioButton notSpecifiedRb;

    private JButton judgeSearchBtn;

    private JPanel judgePanel;

    private JCheckBox allDefendantsCbx;

    private ButtonGroup reservedGroup;

    private XDatePanel adjournmentDatePanel;

    private XPanel panelParent;

    private TitledBorder lapBorder;

    private Border border;

    private boolean onDialog = true;

    private CourtLogCRUDValue availableCRUDValue = null;

    /**
     * The Directions for Defendant Panel is passed in for reference to other
     * controls on the screen. The LongAournmentModel provides data required to
     * form court log entry and display.
     * 
     * @param parent
     * @param model
     * @throws CSRecoverableException
     */
    public LongAdjournmentPanel(XPanel parent, LongAdjournmentModel model) throws CSRecoverableException {
        super(null, model);
        this.model = model;
        this.panelParent = parent;
        this.onDialog = false;

        getResources();
        stepInitialise();
        jbInit();
    }

    /**
     * The LongAdjournment Dialog is passed in for reference to other controls
     * on the screen. The LongAournmentModel provides data required to form
     * court log entry and display.
     * 
     * @param parent
     * @param model
     * @throws CSRecoverableException
     */
    public LongAdjournmentPanel(XDialog parent, LongAdjournmentModel model) throws CSRecoverableException {
        super(parent, model);
        this.model = model;
        onDialog = true;

        getResources();
        stepInitialise();
        jbInit();
    }

    /**
     * This method is called by directions for defendant. It has its own
     * defendant selector which is driving the model that is selected. Because
     * this class uses its own defendant selector when used in a dialog this
     * method will emulate the selection of a defendant so that court log events
     * are populated correctly.
     * 
     * @param model
     */
    public void setModel(LongAdjournmentModel model) {
        this.model = model;
        DefendantBasicValue def = getCourtLogEventLevelPanel().findDefendantUsingDefOnCaseId(
                model.getDefendantOnCaseId());
        getCourtLogEventLevelPanel().getDefendantCombo().setSelectedItem(def);
        getCourtLogEventLevelPanel().getDefendantList().setSelectedValue(def, false);
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        HashMap hashMap = (HashMap) propertyMap.get(laoSchema);
        model.setSubEventCode(((String) hashMap.get(laoType)));
        model.setSubEventId(findSelectedEntry((Vector) pullDownList01, model.getSubEventCode()));
        if (isDateEntryRequired(model.getSubEventId())) {
            try {
                String tempString = (String) hashMap.get(laoDate);
                model.setAdjournmentDate(XDateFormat.parse(tempString));
            } catch (ParseException ex) {
                // Ignore. Will be rectified when new date saved.
            }
        } else {
            model.setAdjournmentDate(null);
        }

        String tempString = (String) hashMap.get(laoReservedJudge);
        if (tempString != null) {
            model.setJudgeReserved(true);
            model.setJudgeName(tempString);
        }

        tempString = (String) hashMap.get(laoPSRRequired);
        if (tempString != null) {
            model.setPsrRequired(Boolean.valueOf(tempString).booleanValue());
        }

        tempString = (String) hashMap.get(laoNotReserved);
        if (tempString != null) {
            model.setNotReserved(Boolean.valueOf(tempString).booleanValue());
        }
    }

    /**
     * Load the custom resources from the required property bundles.
     */
    private void getResources() {
        // Set XSD schema fields
        laoSchema = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType()
                + "_LongAdjournOptions");
        laoType = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType() + "_Type");
        laoDate = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType() + "_Date");
        laoName = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType() + "_Name");
        laoDeftId = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType()
                + "_DeftId");
        laoPSRRequired = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType()
                + "_LAO_PSR_Required");
        laoReservedJudge = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType()
                + "_LAO_Reserved_To_Judge_Name");
        laoNotReserved = ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E" + model.getEventType()
                + "_LAO_Not_Reserved");

        Vector lovTypes = (Vector) RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd", laoType);
        pullDownList01.add(new PullDownListObject(0, "select", ResourceBundleHelper.getResource(
                XhibitBundles.LongAdjournment, "select")));

        // Populate model for Long Adjournment options combo
        for (int x = 0; x < lovTypes.size(); x++) {
            String name = (String) lovTypes.get(x);
            String value = (String) lovTypes.get(x);
            value = ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, name);

            pullDownList01.add(new PullDownListObject(x + 1, name, value));
        }
    }

    private void jbInit() {
        adjournmentDatePanel = new XDatePanel(this); // Generic date panel
        adjournmentDatePanel.getDateComponent().addMChangeListener(new DateChangeListener(this));

        if (onDialog) {
            this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
            this.add(getCourtLogEventLevelPanel().getDefendantCbLabel(), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getCourtLogEventLevelPanel().getDefendantCombo(), new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getAllDefendantsLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getAllDefendantsCbx(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getLogAuditPanel(), new GridBagConstraints(0, 9, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        }

        this.add(getLongAdjournmentOptionsLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getSubEventCb(), new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getAdjournmentDateLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(adjournmentDatePanel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getPsrRequiredLabel(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getPSRRequiredCbx(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getReservedToJudgeRb(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getJudgePanel(), new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(getNotReservedRb(), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getNotSpecifiedRb(), new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        

        getReservedGroup().add(getReservedToJudgeRb());
        getReservedGroup().add(getNotReservedRb());
        getReservedGroup().add(getNotSpecifiedRb());
    }

    private JTextField getJudgesNameText() {
        if (judgesNameText == null) {
            judgesNameText = new JTextField();
            judgesNameText.addKeyListener(new UpdateStateKeyListener(this));
            if (onDialog) {
                judgesNameText.setColumns(25);
                judgesNameText.setMaximumSize(new Dimension(275, 21));
                judgesNameText.setMinimumSize(new Dimension(275, 21));
                judgesNameText.setPreferredSize(new Dimension(275, 21));
            } else {
                judgesNameText.setColumns(16);
                judgesNameText.setMaximumSize(new Dimension(30, 21));
                judgesNameText.setMinimumSize(new Dimension(30, 21));
                judgesNameText.setPreferredSize(new Dimension(30, 21));
            }
            judgesNameText.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment,
                    "Enter_judge_name"));
            judgesNameText.setEnabled(false);
            judgesNameText.setBackground(SystemColor.text);
        }
        return judgesNameText;
    }

    private JComboBox getSubEventCb() {
        if (subEventCb == null) {
            subEventCb = new JComboBox();
            if (onDialog) {
                subEventCb.setMaximumSize(new Dimension(275, 21));
                subEventCb.setMinimumSize(new Dimension(275, 21));
                subEventCb.setPreferredSize(new Dimension(275, 21));
            } else {
                subEventCb.setMaximumSize(new Dimension(30, 21));
                subEventCb.setMinimumSize(new Dimension(30, 21));
                subEventCb.setPreferredSize(new Dimension(30, 21));
            }
            subEventCb.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "Select_a_long"));
            populateComboBox(getSubEventCb(), (Vector) pullDownList01);
            subEventCb.addItemListener(getItemChangeListener());
            subEventCb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return subEventCb;
    }

    private JLabel getLongAdjournmentOptionsLabel() {
        if (longAdjounmentOptionsLabel == null) {
            longAdjounmentOptionsLabel = new JLabel();
            longAdjounmentOptionsLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment,
                    "Long_Adjournment_Options"));
        }
        return longAdjounmentOptionsLabel;
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment,
                    "eventName"));
        }
        return eventNameLabel;
    }

    private JLabel getAllDefendantsLabel() {
        if (allDefendantsLabel == null) {
            allDefendantsLabel = new JLabel();
            allDefendantsLabel.setText(ResourceBundleHelper
                    .getResource(XhibitBundles.LongAdjournment, "All_Defendants"));
        }
        return allDefendantsLabel;
    }

    private JLabel getPsrRequiredLabel() {
        if (psrRequiredLabel == null) {
            psrRequiredLabel = new JLabel();
            psrRequiredLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "PSR_Required"));
        }
        return psrRequiredLabel;
    }

    private JCheckBox getPSRRequiredCbx() {
        if (psrRequiredCbx == null) {
            psrRequiredCbx = new JCheckBox();
            psrRequiredCbx.addItemListener(getItemChangeListener());
            psrRequiredCbx.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return psrRequiredCbx;
    }

    private JCheckBox getAllDefendantsCbx() {
        if (allDefendantsCbx == null) {
            allDefendantsCbx = new JCheckBox();
            allDefendantsCbx.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return allDefendantsCbx;
    }

    private ButtonGroup getReservedGroup() {
        if (reservedGroup == null) {
            reservedGroup = new ButtonGroup();
        }

        return reservedGroup;
    }

    private JRadioButton getReservedToJudgeRb() {
        if (reservedToJudgeRb == null) {
            reservedToJudgeRb = new JRadioButton(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment,
                    "Reserved_Judge"));
            reservedToJudgeRb.addItemListener(getItemChangeListener());
            reservedToJudgeRb.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return reservedToJudgeRb;
    }

    private JRadioButton getNotReservedRb() {
        if (notReservedRb == null) {
            notReservedRb = new JRadioButton(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment,
                    "Not_Reserved"));
            notReservedRb.addItemListener(getItemChangeListener());
            notReservedRb.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return notReservedRb;
    }

    private ItemChangeListener itemChangeListener;

    private ItemChangeListener getItemChangeListener() {
        if (itemChangeListener == null) {
            itemChangeListener = new ItemChangeListener(this);
        }
        return itemChangeListener;
    }

    private JRadioButton getNotSpecifiedRb() {
        if (notSpecifiedRb == null) {
            notSpecifiedRb = new JRadioButton(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment,
                    "Not_Specified"));
            notSpecifiedRb.addItemListener(getItemChangeListener());
            notSpecifiedRb.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }

        return notSpecifiedRb;
    }

    private JButton getJudgeSearchBtn() {
        if (judgeSearchBtn == null) {
            XAction openSearchJudgeUpdateParentAction = XhibitActions.getAction(model.getXac(),
                    XhibitActions.OpenSearchJudgeUpdateParent, this);
            judgeSearchBtn = new JButton(openSearchJudgeUpdateParentAction);
        }
        return judgeSearchBtn;
    }

    private JPanel getJudgePanel() {
        if (judgePanel == null) {
            judgePanel = new JPanel();
            judgePanel.setLayout(new GridBagLayout());
            judgePanel.add(getJudgesNameText());
            judgePanel.add(getJudgesNameText(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            judgePanel.add(getJudgeSearchBtn(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        }
        return judgePanel;
    }

    private JLabel getJudgesNameLabel() {
        if (judgesNameLabel == null) {
            judgesNameLabel = new JLabel();
            judgesNameLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "Judge_s_Name_"));
        }
        return judgesNameLabel;
    }

    private JLabel getAdjournmentDateLabel() {
        if (adjournmentDateLabel == null) {
            adjournmentDateLabel = new JLabel();
            adjournmentDateLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "Date_"));
        }
        return adjournmentDateLabel;
    }

    /**
     * Refreshes screen with LongAdjournmentModel data and ensures controls hava
     * correct status
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        int width = getLongAdjournmentOptionsLabel().getPreferredSize().width;
        width = Math.max(width, getAdjournmentDateLabel().getPreferredSize().width);
        width = Math.max(width, getJudgesNameLabel().getPreferredSize().width);
        makeColumnZeroWidth(width, getCourtLogEventLevelPanel());

        super.stepActivate();
    }

    private void makeColumnZeroWidth(int width, JPanel child) {
        Component c = null;
        for (int i = 0; i < child.getComponents().length; i++) {
            if (child.getComponent(i) instanceof JLabel) {
                c = child.getComponent(i);
                break;
            }
        }

        if (c != null && c instanceof JComponent && ((JComponent) c).getPreferredSize().width != width) {
            Dimension d = new Dimension(width, XHIBITConstant.getLineHeight());
            ((JComponent) c).setMinimumSize(d);
            ((JComponent) c).setPreferredSize(d);
            ((JComponent) c).setSize(d);
            child.revalidate();
            child.repaint();
        }
    }

    protected void moveModelToScreen() {
        log.debug("In moveModelToScreen");
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            getSubEventCb().setSelectedIndex(model.getSubEventId());
            getPSRRequiredCbx().setSelected(model.isPsrRequired());
            getNotReservedRb().setSelected(model.isNotReserved());
            getNotSpecifiedRb().setSelected(!(model.isJudgeReserved() || model.isNotReserved()));
            getReservedToJudgeRb().setSelected(model.isJudgeReserved());
            getJudgesNameText().setText(model.getJudgeName());
            adjournmentDatePanel.setDate(model.getAdjournmentDate());
        } else {
            adjournmentDatePanel.setDate((Calendar) null);
            getNotSpecifiedRb().setSelected(true);
        }
    }

    /**
     * Ensures screen behaviour is reflective of user activity
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        super.stepUpdateViewState();
        final boolean showAllDefendantsCbx = showAllDefendantsCbx();
        log.debug("showDefendantsCbx: " + showAllDefendantsCbx);

        if (getSubEventCb().getSelectedIndex() == 0) {
            // Adjournment date
            getAdjournmentDateLabel().setEnabled(false);
            adjournmentDatePanel.setDateEnabled(false);
            adjournmentDatePanel.setRequired(false);
            adjournmentDatePanel.setDate((Calendar) null);

            if (onDialog) {
                getCourtLogEventLevelPanel().getDefendantCbLabel().setEnabled(false);
                getCourtLogEventLevelPanel().getDefendantCombo().setEnabled(false);
                getAllDefendantsCbx().setEnabled(false);
                getAllDefendantsLabel().setEnabled(false);
            }

            // PSR checkbox
            getPsrRequiredLabel().setEnabled(false);
            getPSRRequiredCbx().setEnabled(false);
            getPSRRequiredCbx().setSelected(false);

            // Judge
            getJudgesNameText().setText(null);
            getJudgeSearchBtn().setEnabled(false);

            getReservedToJudgeRb().setEnabled(false);
            getNotReservedRb().setEnabled(false);
            getNotSpecifiedRb().setEnabled(false);
            getNotSpecifiedRb().setSelected(true);
        } else {
            boolean enableState = isDateEntryRequired(getSubEventCb().getSelectedIndex());
            getAdjournmentDateLabel().setEnabled(enableState);
            adjournmentDatePanel.setDateEnabled(enableState);
            adjournmentDatePanel.setRequired(enableState);
            if (onDialog) {
                if (model.isInEditMode()) {
                    getCourtLogEventLevelPanel().getDefendantCbLabel().setEnabled(false);
                    getCourtLogEventLevelPanel().getDefendantCombo().setEnabled(false);
                    getAllDefendantsCbx().setEnabled(false);
                    getAllDefendantsLabel().setEnabled(false);
                } else {
                    getAllDefendantsCbx().setEnabled(true);
                    getAllDefendantsLabel().setEnabled(true);
                    getCourtLogEventLevelPanel().getDefendantCbLabel().setEnabled(!getAllDefendantsCbx().isSelected());
                    getCourtLogEventLevelPanel().getDefendantCombo().setEnabled(!getAllDefendantsCbx().isSelected());
                }
            }
            getPsrRequiredLabel().setEnabled(true);
            getPSRRequiredCbx().setEnabled(true);
            getJudgesNameLabel().setEnabled(true);
            getReservedToJudgeRb().setEnabled(true);
            getNotReservedRb().setEnabled(true);
            getNotSpecifiedRb().setEnabled(true);
            getJudgeSearchBtn().setEnabled(getReservedToJudgeRb().isSelected());
        }

        // Only display the allDefendantsCbx for non-appeal cases
        getAllDefendantsCbx().setVisible(showAllDefendantsCbx);
        getAllDefendantsLabel().setVisible(showAllDefendantsCbx);
    }

    /**
     * Determines whether or not to display the allDefendantsCbx as it is only
     * applicable to non-appeal cases.
     * 
     * @return - true if the checkbox is applicable for the current case type
     * @throws CSRecoverableException
     */
    protected boolean showAllDefendantsCbx() throws CSRecoverableException {
        boolean returnCode = true;

        if (model != null && model.getXac() != null && model.getXac().getApplicationCaseModel() != null
                && model.getXac().getApplicationCaseModel().getScheduledHearingValue() != null) {
            ScheduledHearingValue temp = model.getXac().getApplicationCaseModel().getScheduledHearingValue();
            if (CaseTypeHelper.isCriminalAppeal_CaseType(temp) || CaseTypeHelper.isMiscelleanousAppeal_CaseType(temp)) {
                returnCode = false;
            }
        }
        return returnCode;
    }

    protected boolean isMandatoryFieldsCompleted() {
        if (getSubEventCb().getSelectedIndex() == 0) {
            return false;
        } else {
            if (isDateEntryRequired(getSubEventCb().getSelectedIndex())) {
                if (!adjournmentDatePanel.isMandatoryFieldsCompleted()) {
                    return false;
                }
            }

            if (getReservedToJudgeRb().isSelected()) {
                if (getJudgesNameText().getText().trim().length() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isDateEntryRequired(int param) {
        PullDownListObject pdlo01 = (PullDownListObject) ((Vector) pullDownList01).get(param);

        // return true/false based on logic below
        return (ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E30200_Case_listed_trial_code").trim()
                .equalsIgnoreCase(pdlo01.getCode().trim())
                || ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment,
                        "E30200_Case_listed_Further_Mention/PAD_code").equalsIgnoreCase(pdlo01.getCode())
                || ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E30200_Case_listed_Sentence_code")
                        .equalsIgnoreCase(pdlo01.getCode())
                || ResourceBundleHelper.getResource(XhibitBundles.LongAdjournment, "E30200_Case_listed_on_code")
                        .equalsIgnoreCase(pdlo01.getCode()) || ResourceBundleHelper.getResource(
                XhibitBundles.LongAdjournment, "E30200_Case_listed_week_commencing_code").equalsIgnoreCase(
                pdlo01.getCode()));
    }

    /**
     * Fires necessary validate of entred data on panel
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if (isDateEntryRequired(getSubEventCb().getSelectedIndex())) {
            Calendar tomorrow = null;
            if (onDialog) {
                super.stepValidate();
                adjournmentDatePanel.stepValidate();
                tomorrow = getLogAuditPanel().getDate();
            } else {
                tomorrow = ((LongAdjournDirectionsPanel) panelParent).getLogAuditPanel().getDate();
            }

            tomorrow.set(Calendar.HOUR_OF_DAY, 23);
            tomorrow.set(Calendar.MINUTE, 59);
            tomorrow.set(Calendar.SECOND, 59);
            if (adjournmentDatePanel.getDate().before(tomorrow)) {
                adjournmentDatePanel.requestFocus();
                throw new CSValidationException("validation.date.afterlog", new String[] { adjournmentDatePanel
                        .getText() }, "Date is in the past");
            }
        }
    }

    /**
     * After forming CourtLogCRUDValues for selected defendants with either
     * delegate calls are made to create court log entries or the
     * availableCRUDValue is set to be referenced by parent panel.
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        super.getCourtLogEventLevelPanel().stepDeinitialise(update);
        if (update) {
            // Initialise variables
            CourtLogCRUDValue courtLogCRUDValue = null;
            int defendantCount = 1;
            DefendantBasicValue[] defendantArray = null;
            CourtLogCRUDValue[] courtLogCRUDValues = null;
            HashMap laOptionsType = loadLAOptionsTypes();

            // create an array of courtLogCRUDValues if allDefendants option
            // selected
            if (model.isAllDefendants()) {
                Collection values = model.getXac().getApplicationCaseModel().getScheduledHearingValue()
                        .getDefendantsOnCase();
                defendantCount = values.size();
                defendantArray = new DefendantBasicValue[defendantCount];
                values.toArray(defendantArray);

                // Sort the defendantArray in surname/first name order
                Sorter.sort(defendantArray, new String[] { "surname", "firstName" }, new Boolean(true));

                // Size an array
                courtLogCRUDValues = new CourtLogCRUDValue[defendantCount];
            }

            // loop through to create courtLogCRUDValue for each listed
            // defendant
            for (int i = 0; i < defendantCount; i++) {
                courtLogCRUDValue = new CourtLogCRUDValue();

                // Standard fields
                courtLogCRUDValue.setCaseId(model.getXac().getApplicationCaseModel().getCaseId());
                courtLogCRUDValue.setEventType(new Integer(model.getEventType()));
                courtLogCRUDValue.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
                courtLogCRUDValue.setScheduledHearingId(model.getScheduledHearingId());

                if (model.isInEditMode()) { // update event for single
                    // existing defendant on dialog
                    courtLogCRUDValue.setEntryDate(model.getDateTime().getTime());
                    courtLogCRUDValue.setEntryFreeText(model.getFreeText());
                    courtLogCRUDValue.setVersion(model.getVersion());

                    // Not sure if required on Edit as defendant details
                    // already there.
                    super.getCourtLogEventLevelPanel().populateCRUD(courtLogCRUDValue);

                    laOptionsType.put(laoDeftId, model.getDefendantId());
                    courtLogCRUDValue.setProperty(laoSchema, laOptionsType);
                    courtLogCRUDValue.setLogEntryId(model.getEventId());
                } else { // new Long adjournment entry
                    if (onDialog) {
                        courtLogCRUDValue.setEntryDate(model.getDateTime().getTime());
                        courtLogCRUDValue.setEntryFreeText(model.getFreeText());

                        if (model.isAllDefendants()) { // add CourtLogCRUDValue
                            // to array after
                            // setting
                            // defendant information
                            if (courtLogCRUDValues != null && defendantArray != null) {
                                model.setDefendantOnCaseId(getDefendantOnCaseBasicValue(defendantArray[i]).getId());
                                model.setDefendantId(defendantArray[i].getId());
                                super.getCourtLogEventLevelPanel().populateCRUD(courtLogCRUDValue);
                                laOptionsType.put(laoName, courtLogCRUDValue.getProperty("defendant_name"));
                                laOptionsType.put(laoDeftId, defendantArray[i].getId());
                                courtLogCRUDValue.setProperty(laoSchema, laOptionsType);

                                // add to array
                                courtLogCRUDValues[i] = courtLogCRUDValue;
                            }
                        } else { // new event for single defendant on
                            // dialog
                            super.getCourtLogEventLevelPanel().populateCRUD(courtLogCRUDValue);
                            laOptionsType.put(laoDeftId, model.getDefendantId());
                            laOptionsType.put(laoName, model.getDefendantName());
                            courtLogCRUDValue.setProperty(laoSchema, laOptionsType);
                        }
                    } else { // new event for existing single defendant on
                        // P&D or Preliminary Hearings panel
                        super.getCourtLogEventLevelPanel().populateCRUD(courtLogCRUDValue);
                        laOptionsType.put(laoDeftId, model.getDefendantId());
                        laOptionsType.put(laoName, courtLogCRUDValue.getProperty("defendant_name"));
                        courtLogCRUDValue.setProperty(laoSchema, laOptionsType);
                        availableCRUDValue = courtLogCRUDValue;
                    }
                }
            } // for loop

            // call appropriate delegate method
            if (model.isInEditMode()) {
                getCLCDelegate().updateEntry(courtLogCRUDValue);
            } else if (onDialog) {
                if (model.isAllDefendants()) {
                    getCLCDelegate().newEntries(courtLogCRUDValues);
                } else {
                    getCLCDelegate().newEntry(courtLogCRUDValue);
                }
            }

            setModified(false);
        } // update
    }

    /**
     * Returns a CourtLogCRUDValue created in the stepDeinitialise method and
     * set to the variable availableCRUDValue.
     */
    public CourtLogCRUDValue getAvailableCRUDValue() {
        return availableCRUDValue;
    }

    private HashMap loadLAOptionsTypes() {
        HashMap laOptionsType = new HashMap();

        PullDownListObject pdlo = ((PullDownListObject) ((Vector) pullDownList01).get(model.getSubEventId()));

        laOptionsType.put(laoType, pdlo.getCode());

        if (isDateEntryRequired(model.getSubEventId())) {
            laOptionsType.put(laoDate, XDateFormat.format(model.getAdjournmentDate(), XDateFormat.DATEFORMAT));
        } else {
            laOptionsType.put(laoDate, "");
        }

        // Change to Judge Name
        if (model.isJudgeReserved()) {
            laOptionsType.put(laoReservedJudge, model.getJudgeName());
            if (laOptionsType.containsKey(laoNotReserved)) {
                laOptionsType.remove(laoNotReserved);
            }
        } else if (model.isNotReserved()) {
            laOptionsType.put(laoNotReserved, "true");
            if (laOptionsType.containsKey(laoReservedJudge)) {
                laOptionsType.remove(laoReservedJudge);
            }
        } else
        // Just in case non set
        {
            if (laOptionsType.containsKey(laoReservedJudge)) {
                laOptionsType.remove(laoReservedJudge);
            }
            if (laOptionsType.containsKey(laoNotReserved)) {
                laOptionsType.remove(laoNotReserved);
            }
        }

        if (model.isPsrRequired()) {
            laOptionsType.put(laoPSRRequired, "true");
        }
        // SG - XML Schema defaults to false for this node. If the node is
        // written with false, then CJSE will create a PSR event 11108
        // which it should not do.
        // else
        // {
        // laOptionsType.put(laoPSRRequired, "false");
        // }

        return laOptionsType;
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        model.setSubEventId(getSubEventCb().getSelectedIndex());
        model.setSubEventCode(findSelectedEntry((Vector) pullDownList01, getSubEventCb().getSelectedIndex()));
        model.setAdjournmentDate(adjournmentDatePanel.getDate());
        if (onDialog) {
            if (getAllDefendantsCbx().isSelected()) {
                model.setAllDefendants(true);
                model.setName(null);
            } else {
                model.setAllDefendants(false);
                model.setName(model.getDefendantName());
            }
        }

        model.setPsrRequired(getPSRRequiredCbx().isSelected());
        model.setJudgeReserved(getReservedToJudgeRb().isSelected());
        model.setNotReserved(getNotReservedRb().isSelected());

        if (getReservedToJudgeRb().isSelected()) {
            model.setJudgeName(getJudgesNameText().getText());
        } else {
            model.setJudgeName(null);
        }
    }

    private void populateComboBox(JComboBox comboBox, Vector comboBoxData) {
        comboBox.removeAllItems();
        for (int x = 0; x < comboBoxData.size(); x++) {
            comboBox.addItem(comboBoxData.get(x));
        }
    }

    private int findSelectedEntry(Vector itemList, String code) {
        int returnCode = 0;
        for (int x = 0; x < itemList.size(); x++) {
            PullDownListObject pdlo = (PullDownListObject) itemList.get(x);
            if (pdlo.getCode().equalsIgnoreCase(code)) {
                returnCode = x;
            }
        }
        return returnCode;
    }

    private String findSelectedEntry(Vector itemList, int id) {
        PullDownListObject pdlo = (PullDownListObject) itemList.get(id);
        return pdlo.getCode();
    }

    /**
     * Returns a CourtLogCRUDValue created in the stepDeinitialise method and
     * set to the variable availableCRUDValue.
     * 
     * @param action
     */
    public void processAddJudge(OpenSearchJudgeAction action) {
        log.debug("processAddJudge(OpenSearchJudgeAction " + action + ")");
        Collection col = action.getResults();
        log.debug("the results collection has " + col.size() + " objects in it.");
        Iterator it = col.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            try {
                log.debug("Found object " + o + " in the OpenSearchJudgeAction's results.");
                RefJudgeBasicValue aNewJudge = (RefJudgeBasicValue) o;
                final String title = FormattedDisplayHelper.getDisplayName(aNewJudge);
                getJudgesNameText().setText(title);
            } catch (final Exception e) {
                log.error("Exception thrown in processAddJudge whilst casting results objects.", e);
            }
        }
    }

    public void setWarningVisible(boolean visible) {
        if (getReservedGroup() != null) {
            Enumeration enumeration = getReservedGroup().getElements();
            while (enumeration.hasMoreElements()) {
                JRadioButton item = (JRadioButton) enumeration.nextElement();
                item.setIcon(visible ? DirectionsFactory.rbDisabled : null);
                item.setSelectedIcon(visible ? DirectionsFactory.rbDisabledSelected : null);
            }
            getSubEventCb().setBackground(visible ? PDHConstants.DISABLED_COLOR : PDHConstants.TEXT_AREA_BACKGROUND);
            adjournmentDatePanel.getDateComponent().getDisplay().setBackground(
                    visible ? PDHConstants.DISABLED_COLOR : PDHConstants.TEXT_AREA_BACKGROUND);
            getJudgesNameText()
                    .setBackground(visible ? PDHConstants.DISABLED_COLOR : PDHConstants.TEXT_AREA_BACKGROUND);
            getPSRRequiredCbx().setIcon(visible ? DirectionsFactory.chkDisabled : null);
            getPSRRequiredCbx().setSelectedIcon(visible ? DirectionsFactory.chkDisabledSelected : null);
        }
    }

    /**
     * Convenience method to return a DefendantOnCaseBasicValue for a given
     * DefendantBasicValue.
     * 
     * @param defendantBV -
     *            the DefendantBasicValue that is used as a source of the
     *            defendant ID
     * @return DefendantOnCaseBasicValue
     */
    private DefendantOnCaseBasicValue getDefendantOnCaseBasicValue(DefendantBasicValue defendantBV) {
        return getDefendantOnCaseBasicValue(defendantBV.getId());
    }

    /**
     * Convenience method to return a DefendantOnCaseBasicValue stored in the
     * CourtLogEventLevelPanel using the defendant ID passed in.
     * 
     * @param defendantId -
     *            the ID of the defendant whose DefendantOnCaseBasicValue is
     *            required
     * @return DefendantOnCaseBasicValue
     */
    private DefendantOnCaseBasicValue getDefendantOnCaseBasicValue(Integer defendantId) {
        return (DefendantOnCaseBasicValue) getCourtLogEventLevelPanel().findDefOnCase(defendantId);
    }
}
