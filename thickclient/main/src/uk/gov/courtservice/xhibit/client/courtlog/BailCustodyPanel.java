package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class BailCustodyPanel extends CourtLogEventPanel {
    private final Dimension labelDim = new Dimension(75, XHIBITConstant.getLineHeight());

    private final BailCustodyModel model;

    private JLabel optionCbLabel;

    private JComboBox optionCb;

    private final Vector pullDownList01 = new Vector();

    private XDatePanel optionalDatePanel;

    private JLabel optionalDateLabel;

    public BailCustodyPanel(final XDialog parent, final BailCustodyModel model) throws CSRecoverableException {
        super(parent, model);
        this.model = model;

        stepInitialise();
        jbInit();
    }

    private void jbInit() {
        final JPanel bcPanel = new JPanel();

        bcPanel.setLayout(new GridBagLayout());
        CompoundBorder border2 = BorderFactory.createCompoundBorder(new TitledBorder(""), BorderFactory
                .createEmptyBorder(0, 0, 0, 0));
        bcPanel.setBorder(border2);

        optionalDatePanel = new XDatePanel(this);
        optionalDatePanel.setDateEnabled(false);

        bcPanel.add(getOptionCbLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        bcPanel.add(getOptionCb(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));
        bcPanel.add(getOptionalDateLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        bcPanel.add(optionalDatePanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));

        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(bcPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    public void stepInitialise() throws CSRecoverableException {
        if (model.isInEditMode()) {
            // Defendant selection is prohibited as the event may only be
            // related
            // to the defendant it was created for
            getCourtLogEventLevelPanel().setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_COMBO);
        } else {
            // Defendant selection is permitted since this is a new event
            getCourtLogEventLevelPanel().setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_LIST);
        }

        // Populate pullDownList01 with the selectable options from the
        // resources file
        Vector bacTypes = (Vector) RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd",
                "E20200_BC_Type");

        pullDownList01.add(new PullDownListObject(0, "select", ResourceBundleHelper.getResource(
                XhibitBundles.BailCustody, "select")));
        for (int x = 0; x < bacTypes.size(); x++) {
            String name = (String) bacTypes.get(x);
            String value = (String) bacTypes.get(x);

            try {
                value = ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, name);
            } catch (MissingResourceException mre) {
            }

            pullDownList01.add(new PullDownListObject(x + 1, name, value));
        }

        // now that everything has been prepared, perform the rest of the
        // initialisation...
        super.stepInitialise();
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
        if (isDateRequired(getOptionCb().getSelectedIndex())) {
            optionalDatePanel.stepValidate();
        }
    }

    /**
     * Life-cycle method that is executed when the screen is destroyed. This is
     * generally as a result of the user clicking the OK/Cancel buttons. It
     * constructs a CourtLogCRUDValue for each record to be added/updated and
     * calls the appropriate method on the business delegate.
     * 
     * @param update -
     *            true if the user clicked the OK button
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            /**
             * There may be many records to add depending on event type: - case
             * level always has 1 - defendant level has one for each selected
             * defendant, so store all CRUD values in an array and call the
             * delegate method that expects multiple records
             */
            // if (model.isInEditMode() || isCaseLevelEvent())
            if (model.isInEditMode()) {
                super.stepDeinitialise(update);
            } else {
                super.getCourtLogEventLevelPanel().stepDeinitialise(update);

                final int arrayLength = model.getSelectedDefendants().length;
                final CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[arrayLength];

                for (int x = 0; x < arrayLength; x++) {
                    DefendantBasicValue dbv = (DefendantBasicValue) model.getSelectedDefendants()[x];

                    model.setDefendantId(dbv.getId());
                    model.setDefendantOnCaseId(getCourtLogEventLevelPanel().findDefOnCase(dbv.getId()).getId());
                    model.setDefendantName(PDHConstants.buildDefendantName(dbv));

                    crudArray[x] = createCRUDFromModel();
                }
                getCLCDelegate().newEntries(crudArray);
            }
        }
    }

    protected void populateCRUDProperties(Map propertyMap) {
        // Specific fields
        HashMap bacOptionsType = new HashMap();
        bacOptionsType.put("E20200_BC_Defendant_Name", model.getDefendantName());
        bacOptionsType.put("E20200_BC_Type", model.getSelectedItemCode());
        if (isDateRequired(model.getSelectedIndex())) {
            bacOptionsType.put("E20200_BC_Date", XDateFormat.format(model.getEnteredDate().getTime(),
                    XDateFormat.DATEFORMAT));
        }
        propertyMap.put("E20200_Bail_And_Custody_Options", bacOptionsType);
        log.debug("PropertySet: " + propertyMap.get("E20200_Bail_And_Custody_Options"));
    }

    protected void populateModelProperties(Map propertyMap) {
        HashMap bacOptionsType = (HashMap) propertyMap.get("E20200_Bail_And_Custody_Options");
        model.setSelectedItemCode((String) bacOptionsType.get("E20200_BC_Type"));
        model.setSelectedIndex(findSelectedEntry(pullDownList01, model.getSelectedItemCode()));

        if (isDateRequired(model.getSelectedIndex())) {
            try {
                String dateString = (String) bacOptionsType.get("E20200_BC_Date");
                model.setEnteredDate(XDateFormat.parse(dateString));
            } catch (ParseException ex) {
                // Ignore. Will be rectified when new date saved.
            }
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        super.stepUpdateViewState();
        final boolean enabled = isDateRequired(getOptionCb().getSelectedIndex());

        optionalDatePanel.setDateEnabled(enabled);
        optionalDatePanel.setRequired(enabled);
    }

    protected boolean isMandatoryFieldsCompleted() {
        boolean result = true;

        if (getOptionCb().getSelectedIndex() == 0) {
            result = false;
        } else if (isDateRequired(getOptionCb().getSelectedIndex())) {
            if (!optionalDatePanel.isMandatoryFieldsCompleted()) {
                result = false;
            }
        }

        return result;
    }

    private JLabel getOptionCbLabel() {
        if (optionCbLabel == null) {
            optionCbLabel = new JLabel();
            optionCbLabel.setMinimumSize(labelDim);
            optionCbLabel.setPreferredSize(labelDim);
            optionCbLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.BailCustody, "lblBCOption"));
        }
        return optionCbLabel;
    }

    private JLabel getOptionalDateLabel() {
        if (optionalDateLabel == null) {
            optionalDateLabel = new JLabel();
            optionalDateLabel.setMinimumSize(labelDim);
            optionalDateLabel.setPreferredSize(labelDim);
            optionalDateLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.BailCustody, "lblBCOptionalDate"));
        }
        return optionalDateLabel;
    }

    private JComboBox getOptionCb() {
        if (optionCb == null) {
            optionCb = new JComboBox(pullDownList01);
            optionCb.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.BailCustody, "ttBCOption"));
            optionCb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return optionCb;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            getOptionCb().setSelectedIndex(model.getSelectedIndex());
            optionalDatePanel.setDate(model.getEnteredDate());
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        model.setEnteredDate(optionalDatePanel.getDate());
        model.setSelectedIndex(getOptionCb().getSelectedIndex());
        model.setSelectedItemCode(findSelectedEntry(pullDownList01, model.getSelectedIndex()));
        if (isDateRequired(model.getSelectedIndex())) {
            model.setEnteredDate(optionalDatePanel.getDate());
        }
    }

    private boolean isDateRequired(int param) {
        PullDownListObject gpo = (PullDownListObject) pullDownList01.get(param);
        return "E20200_Custody_limits_extended_to".equalsIgnoreCase(gpo.getCode());
    }

    private int findSelectedEntry(Vector itemList, String code) {
        int returnCode = 0;

        for (int x = 0; x < itemList.size(); x++) {
            PullDownListObject pdlo = (PullDownListObject) itemList.get(x);

            if (pdlo.getCode().equalsIgnoreCase(code))
                returnCode = x;
        }

        return returnCode;
    }

    private String findSelectedEntry(Vector itemList, int id) {
        PullDownListObject pdlo = (PullDownListObject) itemList.get(id);

        return pdlo.getCode();
    }
}
