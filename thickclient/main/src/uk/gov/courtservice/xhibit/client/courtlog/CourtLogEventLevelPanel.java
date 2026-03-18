package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DefendantNameRenderer;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.util.EventLevelHelper;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Event Level Panel
 * </p>
 * <p>
 * Description: Panel to be added to all court log events that depending on the
 * level of the court log event (ie case, defendant or CRN) displays the
 * appropriate drop downs so sufficient information can be collected to build
 * CJSE events.
 * </p>
 * <p>
 * Note: At present there are no CRN level events created from the client so
 * this panel will only handle CASE and DEFENDANT. If a CRN level event is
 * received it will be handled as DEFENDANT and an error message output.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: CourtLogEventLevelPanel.java,v 1.22 2005/11/10 07:57:05 bzjrnl
 *          Exp $
 */

public class CourtLogEventLevelPanel extends XPanel {

	private static final long serialVersionUID = 1L;

	private static final int ROWS_TO_SHOW = 3;

    public static final int DEFENDANT_COMBO = 0;

    public static final int DEFENDANT_LIST = 1;

    private final XhibitApplicationController _xac;

    private final FreeTextModel _freeTextModel;

    private final XPanel _parent;

    private int _eventLevel;

    private boolean enabling = false;

    /**
     * If it is decided by the helper that no additional components are required
     * to be displayed, then this remains false so none of the step methods
     * perform and code.
     */
    private boolean processStepMethods = false;

    private String resources = XhibitBundles.BailCustody;

    private JScrollPane defendantsScrollPane;

    private JList defendantsList;

    private JComboBox defendantsCb;

    private JLabel defendantCbLabel;

    private Dimension labelDim = new Dimension(75, XHIBITConstant.getLineHeight());

    private Dimension dcbDim = new Dimension(300, XHIBITConstant.getLineHeight());

    private Dimension dlistDim = new Dimension(300, XHIBITConstant.getLineHeight() * ROWS_TO_SHOW);

    private DefendantBasicValue[] listedDefendants = new DefendantBasicValue[] {};

    private int defendantDisplayType;

    /**
     * Public constructor. Saves any passed in parameters. Sets the indicator
     * that determines which type of widget will be used to display the
     * defendants details - currently either a JList or a JComboBox. Sets the
     * indicator that determines whether or not the event is a defendant level
     * event as screen components are only eligible for display for those event
     * types.
     * 
     * @param freeTextModel -
     *            the generic model type for court log events
     * @param parent -
     *            a reference to the containing panel
     * @throws CSRecoverableException
     */
    public CourtLogEventLevelPanel(FreeTextModel freeTextModel, XPanel parent) throws CSRecoverableException {
        super(new GridBagLayout());
        _freeTextModel = freeTextModel;
        _xac = _freeTextModel.getXac();
        _parent = parent;
        _eventLevel = EventLevelHelper.getEventLevel(freeTextModel.getEventType());

        // set the default defendant display type
        setDefendantDisplayType(_freeTextModel.getDefendantDisplayType());

        // Currently only care if a DEFENDANT level.
        // CRN & JOINDER treated as DEFENDANT
        if (_eventLevel >= EventLevelHelper.DEFENDANT) {
            processStepMethods = true;
            stepInitialise();
            jbInit();
        }
    }

    /**
     * Put the components onto the screen. Both defendant display methods( the
     * list and the combo box ) are instantiated for convenience sake so that we
     * can refer to them without fear of NULL pointer exceptions.
     * 
     * The value of the defendantDisplayType attribute determines which one of
     * the components will actually be seen.
     */
    private void jbInit() {
        if (processStepMethods) {
            // Build both display types up-front
            getDefendantCombo();
            getDefendantScrollPane();

            // defendant label
            this.add(getDefendantCbLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            if (isListDisplay()) {
                // defendant list
                this.add(getDefendantScrollPane(),
                        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            } else {
                // defendant combo
                this.add(getDefendantCombo(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            }
        }
    }

    public JLabel getDefendantCbLabel() {
        if (defendantCbLabel == null) {
            defendantCbLabel = new JLabel();
            defendantCbLabel.setMinimumSize(labelDim);
            defendantCbLabel.setPreferredSize(labelDim);
            // set the defendant label according to the caseType
            defendantCbLabel.setText(getDefendantLabel(resources, "lblBCAppellants", "lblBCDefendants"));
        }
        return defendantCbLabel;
    }

    public JComboBox getDefendantCombo() {
        if (defendantsCb == null) {
            defendantsCb = new JComboBox(listedDefendants);
            defendantsCb.setRenderer(new DefendantNameRenderer());
            defendantsCb.setMinimumSize(dcbDim);
            defendantsCb.setPreferredSize(dcbDim);
            defendantsCb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewState();
                }
            });
        }
        return defendantsCb;
    }

    public JScrollPane getDefendantScrollPane() {
        if (defendantsScrollPane == null) {
            defendantsScrollPane = new JScrollPane(getDefendantList());
            defendantsScrollPane.setMinimumSize(dlistDim);
            defendantsScrollPane.setPreferredSize(dlistDim);
        }
        return defendantsScrollPane;
    }

    public JList getDefendantList() {
        if (defendantsList == null) {
            defendantsList = new JList(listedDefendants);
            defendantsList.setVisibleRowCount(ROWS_TO_SHOW);
            defendantsList.setCellRenderer(new DefendantNameRenderer());
            defendantsList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    stepUpdateViewState();
                }
            });
        }
        return defendantsList;
    }

    /**
     * From the listed defendants on the case find the defendantOnCaseBV for the
     * defendant Id supplied
     * 
     * @param defendantId
     * @return DefendantOnCaseBasicValue
     */
    public DefendantOnCaseBasicValue findDefOnCase(Integer defendantId) {
        if (_freeTextModel.getXac() != null) {
            Collection c = _xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantOnCaseBasicValues();
            Iterator iter = c.iterator();
            while (iter.hasNext()) {
                DefendantOnCaseBasicValue item = (DefendantOnCaseBasicValue) iter.next();
                if (item.getDefendantID().equals(defendantId))
                    return item;
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * From the listed defendants on the case find the defendantOnCaseBV for the
     * defendantOnCase Id supplied
     * 
     * @param defendantOnCaseId
     * @return DefendantBasicValue
     */
    public DefendantBasicValue findDefendantUsingDefOnCaseId(Integer defendantOnCaseId) {
        if (_freeTextModel.getXac() != null) {
            Collection c = _xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantOnCaseBasicValues();
            Iterator iter = c.iterator();
            while (iter.hasNext()) {
                DefendantOnCaseBasicValue item = (DefendantOnCaseBasicValue) iter.next();
                if (item.getId().equals(defendantOnCaseId)) {
                    return findDefendant(item.getDefendantID());
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * From the listed defendants find the defendantBV from the defendant Id
     * supplied
     * 
     * @param defendantId
     * @return DefendantBasicValue
     */
    public DefendantBasicValue findDefendant(Integer defendantId) {
        if (_freeTextModel.getXac() != null) {
            Collection c = _xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantsOnCase();
            Iterator iter = c.iterator();
            while (iter.hasNext()) {
                DefendantBasicValue item = (DefendantBasicValue) iter.next();
                if (item.getId().equals(defendantId)) {
                    return item;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * This is typically called in the panels stepDeinitialise to populated the
     * CRUD value with any additional information required for CJSE level
     * population
     * 
     * @param crud
     */
    public void populateCRUD(CourtLogCRUDValue crud) {
        if (processStepMethods) {
            XHIBITConstant.debug("populateCRUD:: defendantOnCaseId=" + _freeTextModel.getDefendantOnCaseId());
            crud.setDefendantOnCaseId(_freeTextModel.getDefendantOnCaseId());
            XHIBITConstant.debug("populateCRUD:: defendantOnOffenceId=" + _freeTextModel.getDefendantOnOffenceId());
            crud.setDefendantOnOffenceId(_freeTextModel.getDefendantOnOffenceId());

            if (_freeTextModel.getDefendantOnCaseId() != null) {
                DefendantBasicValue defBv = findDefendantUsingDefOnCaseId(_freeTextModel.getDefendantOnCaseId());
                DefendantOnCaseBasicValue defOnCaseBv = findDefOnCase(defBv.getId());
                crud.setProperty("defendant_on_case_id", _freeTextModel.getDefendantOnCaseId());
                crud.setProperty("defendant_name", PDHConstants.buildDefendantName(defBv));
                crud.setProperty("defendant_masked_name", defOnCaseBv.getMaskedName());
                crud.setProperty("defendant_masked_flag", defOnCaseBv.getIsMasked());
            }

            specificEventHandling(crud);
        }
    }

    /**
     * This method carries out specific CRUD population for certain events.
     * 
     * @param crud
     */
    private void specificEventHandling(CourtLogCRUDValue crud) {
        if ("20606".equals(_freeTextModel.eventType)) {
            crud.setProperty("E20606_Appellant_CO_ID", _freeTextModel.getDefendantId());
            crud.setProperty("E20606_Appellant_CO_Name", _freeTextModel.getDefendantName());
        } else if ("20906".equals(_freeTextModel.eventType)) {
            crud.setProperty("E20906_Defence_CO_ID", _freeTextModel.getDefendantId());
            crud.setProperty("E20906_Defence_CO_Name", _freeTextModel.getDefendantName());
        } else if ("20910".equals(_freeTextModel.eventType)) {
            crud.setProperty("E20910_Defence_CC_ID", _freeTextModel.getDefendantId());
            crud.setProperty("E20910_Defence_CC_Name", _freeTextModel.getDefendantName());
        }
    }

    /**
     * This is typically called in stepInitialise if the event is in edit mode.
     * It is used to take the CJSE specific id's out of the CRUD and put into
     * the freeTextModel.
     * 
     * @param crud
     */
    public void populateEditableModel(CourtLogCRUDValue crud) {
        if (processStepMethods && _freeTextModel.isInEditMode()) {
            _freeTextModel.setDefendantOnCaseId(crud.getDefendantOnCaseId());
            _freeTextModel.setDefendantOnOffenceId(crud.getDefendantOnOffenceId());
        }
    }

    /**
     * Retrieve a list of defendants from the AppCaseModel and prep for use by
     * the combo box
     */
    public void stepInitialise() throws CSRecoverableException {
        if (processStepMethods) {
            // If we are editing.
            if (_freeTextModel.isInEditMode()) {
                CourtLogCRUDValue courtLogCRUDValue = XhibitDelegateHelper.getCourtLogDelegate2().getEntry(
                        _freeTextModel.getEventId());
                // Put the extra data to the model
                _freeTextModel.setDefendantOnCaseId(courtLogCRUDValue.getDefendantOnCaseId());
                _freeTextModel.setDefendantOnOffenceId(courtLogCRUDValue.getDefendantOnOffenceId());
            }

            int listedDefendantCount = _xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantsOnCase()
                    .size();
            if (listedDefendantCount <= 0) {
                // No defendants therefore throw an error stating the event is
                // not available.
                CSBusinessException busEx = new CSBusinessException("Court_Log.NoDefendantsListed", new Object[] {
                        _xac.getApplicationCaseModel().getDisplayCaseNumber(),
                        _freeTextModel.getPanelText(),
                        XHIBITConstant.getResource(XhibitBundles.CourtLogResources, isAppeal() ? "Appellant_s_"
                                : "Defendant_s_"),
                        XHIBITConstant.getResource(XhibitBundles.CourtLogResources, isAppeal() ? "Appellant"
                                : "Defendant") }, "No defendants listed on case so event not available");
                throw busEx;
            } else {
                // Determine who the possible defendants are...
                listedDefendants = new DefendantBasicValue[listedDefendantCount];
                _xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantsOnCase().toArray(
                        listedDefendants);

                if (listedDefendants == null) {
                    throw new IllegalArgumentException();
                }

                // Sort the defendants in ascending surname, firstname order
                Sorter.sort(listedDefendants, new String[] { "surname", "firstName" }, new Boolean(true));
            }
        }
    }

    /**
     * Determines whether or not this is an appeal case.
     * 
     * @return true if this is an appeal case; false if this is not an appeal
     *         case
     */
    private boolean isAppeal() {
        boolean rc = false;
        try {
            rc = XHIBITConstant.isCriminalAppeal_CaseType(_xac.getApplicationCaseModel().getScheduledHearingValue())
                    || XHIBITConstant.isMiscelleanousAppeal_CaseType(_xac.getApplicationCaseModel()
                            .getScheduledHearingValue());
        } catch (UnknownCaseTypeException ex) {
            XHIBITConstant.error("Case type could not be established");
        }
        return rc;
    }

    /**
     * pre-select the defendant in the combo box if there is an id in the model.
     */
    public void stepActivate() {
        // If in a situation where we have defendants.
        if (processStepMethods) {
            // And there is a selected defendant (edit mode)
            if (_freeTextModel.getDefendantOnCaseId() != null) {
                getDefendantCombo().setSelectedItem(
                        findDefendantUsingDefOnCaseId(_freeTextModel.getDefendantOnCaseId()));
            }
            // Else see if there are any defendants
            else if (getDefendantCombo().getItemCount() > 0) {
                // And default to the first name in the list.
                getDefendantCombo().setSelectedIndex(0);
                getDefendantList().setSelectedIndex(0);

            }
        }
    }

    /**
     * Life-cycle method to enable/disable screen components depending on user
     * input
     */
    public void stepUpdateViewState() {
        if (XSwingUtilities.getWindowAncestor(_parent) instanceof XDialog) {
            XDialog parentDialog = (XDialog) XSwingUtilities.getWindowAncestor(_parent);
            XAction okAction = ((OkCancelPanel) parentDialog.getButtonPanel()).getOkAction();
            getDefendantCombo().setVisible(isComboDisplay());
            getDefendantScrollPane().setVisible(isListDisplay());

            if (processStepMethods && _eventLevel >= EventLevelHelper.DEFENDANT && !isItemSelected()) {
                okAction.setEnabled(false);
            } else {
                okAction.setEnabled(true);
            }
        }
    }

    /**
     * Life-cycle method to ensure user input is valid.
     * 
     * @throws CSValidationException
     *             if defendant selection is required and none has been selected
     */
    public void stepValidate() throws CSValidationException {
        if (processStepMethods && _eventLevel >= EventLevelHelper.DEFENDANT && !isItemSelected()) {
            getDisplayedWidget().requestFocus();
            throw new CSValidationException("Court_Log.SelectDefendant", new Object[] {}, "Defendant Not Selected");
        }
    }

    /**
     * Life-cycle method executed when a screen is made invisible
     */
    public void stepDeactivate() {
        // No implementation by default...
    }

    /**
     * If the defendants were displayed in a list, then save all selected items
     * in the free text model.
     * 
     * If the defendants were displayed in a combo box, then take the id from
     * the combo box and store in the free text model. Also lookup the
     * defendantOnCaseId and build the defendant name string so that they can be
     * used by the parent panel if required.
     * 
     * @param update :
     *            if true, then save the selected defendant(s) details in the
     *            model
     */
    public void stepDeinitialise(boolean update) // throws
    // uk.gov.courtservice.framework.exception.CSRecoverableException
    {
        if (processStepMethods) {
            if (update) {
                if (_eventLevel >= EventLevelHelper.DEFENDANT) {
                     if (isListDisplay()) {
                        // Store the selected defendants in the model
                        _freeTextModel.setSelectedDefendants(getDefendantList().getSelectedValues());
                     } else {
                          // store the defendant on case id in the model
                        Integer defendantId = ((DefendantBasicValue) getDefendantCombo().getSelectedItem()).getId();
                        Integer defendantOnCaseId = findDefOnCase(defendantId).getId();
                        XHIBITConstant.debug("Defendant Id: " + defendantId + ", defendantOnCaseId: "
                                + defendantOnCaseId + " selected");
                        _freeTextModel.setDefendantOnCaseId(defendantOnCaseId);
                        _freeTextModel.setDefendantId(defendantId);
                        _freeTextModel.setDefendantName(PDHConstants
                                .buildDefendantName((DefendantBasicValue) getDefendantCombo().getSelectedItem()));

                        _freeTextModel.setSelectedDefendants(new Object[] { (DefendantBasicValue) getDefendantCombo()
                                .getSelectedItem() });
                     }
                }
            }
        }
    }

    /**
     * enable or disable all the components in the panel.
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        if (!enabling) {
            enabling = true;
            super.setEnabled(enabled);
            setChildrenEnabled(this, enabled);
            enabling = false;
        }
    }

    public void setChildrenEnabled(Component comp, boolean enabled) {
        comp.setEnabled(enabled);
        if (comp instanceof Container) {
            Container container = (Container) comp;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                setChildrenEnabled(container.getComponent(idx), enabled);
            }
        }
    }

    /**
     * Utility method to override the internal logic to enable / disable the
     * processing of step methods. To be used with caution as results can not be
     * gauranteed. This was added for DirectionsByDefendant which needs to
     * disable the processing of the stepActivate method temporarily while the
     * recursive step calls are happening.
     * 
     * @param newValue
     */
    public void setProcessStepMethods(boolean newValue) {
        processStepMethods = newValue;
    }

    /**
     * Is the flag for processing step methods set?
     * 
     * @return
     */
    public boolean isProcessStepMethods() {
        return processStepMethods;
    }

    /**
     * Method to pick up the appropriate label depending on the Case Type
     * 
     * @return Label from the resource bundle
     */
    private String getDefendantLabel(String resource, String appellantLabel, String defendantLabel) {
        if (isAppeal()) {
            return XHIBITConstant.getResource(resource, appellantLabel);
        }

        return XHIBITConstant.getResource(resource, defendantLabel);
    }

    /**
     * Determines whether or not an item is selected depending on the type of
     * screen component used to display the defendants
     * 
     * @return true if at least 1 item has been selected; false if none have yet
     *         been selected
     */
    public boolean isItemSelected() {
        if (isListDisplay()) {
            return getDefendantList().getSelectedIndex() >= 0;
        }

        return getDefendantCombo().getSelectedIndex() >= 0;
    }

    /**
     * Determines what type of component has been used to display the defendants
     * 
     * @return the screen component as a JComponent
     */
    public JComponent getDisplayedWidget() {
        if (isListDisplay()) {
            return getDefendantList();
        }

        return getDefendantCombo();
    }

    /**
     * Setter for the defendantDisplayType. Note that in "edit" mode, the
     * display type always defaults to a combo box.
     * 
     * @param defendantDisplayType -
     *            the requested type either DEFENDANT_COMBO or DEFENDANT_LIST
     */
    public void setDefendantDisplayType(int defendantDisplayType) {
        // When the record is being edited, the display type defaults to
        // JComboBox
        this.defendantDisplayType = _freeTextModel.isInEditMode() ? DEFENDANT_COMBO : defendantDisplayType;
    }

    public int getDefendantDisplayType() {
        return this.defendantDisplayType;
    }

    public boolean isComboDisplay() {
        return getDefendantDisplayType() == DEFENDANT_COMBO;
    }

    public boolean isListDisplay() {
        return getDefendantDisplayType() == DEFENDANT_LIST;
    }
}
