package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.NumericValidatingDocumentDecorator;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class EstimateForTrialPanel extends CourtLogEventPanel {
    private static final String MIN_TRIAL_EST = "1";

    private final EstimateForTrialModel model;

    private final Vector pullDownList01 = new Vector();

    private final String teoSchema;

    private final String teoTime;

    private final String teoUnits;

    private JComboBox subEventCb;

    private JLabel estimateTimeLabel;

    private JTextField estimateText;

    private JLabel eventNameLabel;

    public EstimateForTrialPanel(XDialog parent, EstimateForTrialModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        // Set XSD schema fields
        teoSchema = XHIBITConstant.getResource(XhibitBundles.EstimateForTrial, "E" + model.getEventType()
                + "_TimeEstimateOptions");
        teoTime = XHIBITConstant.getResource(XhibitBundles.EstimateForTrial, "E" + model.getEventType() + "_Time");
        teoUnits = XHIBITConstant.getResource(XhibitBundles.EstimateForTrial, "E" + model.getEventType() + "_Units");

        stepInitialise();
        jbInit();
        stepActivate();
    }

    public void stepInitialise() throws CSRecoverableException {

        Collection lovTypes = RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd", teoUnits);
        pullDownList01.add(new GeneralPurposeObject(0, "select", XHIBITConstant.getResource(
                XhibitBundles.EstimateForTrial, "select")));

        final Iterator it = lovTypes.iterator();
        for (int x = 1; it.hasNext(); x++) {
            String name = (String) it.next();
            String value;

            try {
                value = XHIBITConstant.getResource(XhibitBundles.SimpleEvent, name);
            } catch (MissingResourceException mre) {
                value = name;
            }

            pullDownList01.add(new GeneralPurposeObject(x, name, value));
        }

        super.stepInitialise();
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        // Save the data in the model
        HashMap hashMap = (HashMap) propertyMap.get(teoSchema);
        model.setSubEventCode(((String) hashMap.get(teoUnits)));
        model.setSubEventId(findSelectedEntry(pullDownList01, model.getSubEventCode()));
        model.setEstimate(((String) hashMap.get(teoTime)));
    }

    private void jbInit() {
        this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 2, 25, 2), 0, 0));
        this.add(getEstimateTimeLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getEstimateText(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getSubEventCb(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 3, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JTextField getEstimateText() {
        if (estimateText == null) {
            estimateText = new JTextField();
            estimateText.setColumns(5);
            // use standard way to validate the entry
            estimateText.setDocument(new NumericValidatingDocumentDecorator(new LimitedTextValidatingDocumentDecorator(
                    5)));

            estimateText.addKeyListener(new UpdateListener());
            estimateText.setMinimumSize(new Dimension(33, 21));
            estimateText.setPreferredSize(new Dimension(33, 21));
            estimateText.setToolTipText(XHIBITConstant
                    .getResource(XhibitBundles.EstimateForTrial, "The_number_of_days"));
            estimateText.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        return estimateText;
    }

    private JComboBox getSubEventCb() {
        if (subEventCb == null) {
            subEventCb = new JComboBox(pullDownList01);
            subEventCb.setMaximumSize(new Dimension(275, 21));
            subEventCb.setMinimumSize(new Dimension(275, 21));
            subEventCb.setPreferredSize(new Dimension(275, 21));
            subEventCb.setToolTipText(XHIBITConstant.getResource(XhibitBundles.EstimateForTrial, "Select_a_period"));
            subEventCb.addItemListener(new UpdateListener());
        }

        return subEventCb;
    }

    private JLabel getEstimateTimeLabel() {
        if (estimateTimeLabel == null) {
            estimateTimeLabel = new JLabel();
            estimateTimeLabel.setText(XHIBITConstant.getResource(XhibitBundles.EstimateForTrial, "Estimate"));
        }

        return estimateTimeLabel;
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(XHIBITConstant.getResource(XhibitBundles.EstimateForTrial,
                    "Time_Estimation_For"));
        }

        return eventNameLabel;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            getSubEventCb().setSelectedIndex(model.getSubEventId());
            getEstimateText().setText(model.getEstimate());
        }
    }

    protected boolean isMandatoryFieldsCompleted() {
        return ((getSubEventCb().getSelectedIndex() > 0) && (getEstimateText().getText().trim().length() > 0));
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
        validateEstimate(getEstimateText().getText());
    }

    private void validateEstimate(String text) throws CSValidationException {
        try {
            // Numeric estimate value must be > 0. Neil Entwistle 12/06/2003
            if (Long.parseLong(text) <= 0) {
                final Object[] items = { text, MIN_TRIAL_EST };
                throw new CSValidationException("validation.mininclusive", items,
                        "validation.mininclusive: Invalid Estimate");
            }
        } catch (final NumberFormatException e) {
            // Estimate validation exception
            getEstimateText().requestFocus();

            throw new CSValidationException("validation.datatype", new Object[] { text },
                    "validation.datatype: Invalid Estimate", e);
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        model.setSubEventId(getSubEventCb().getSelectedIndex());
        model.setSubEventCode(findSelectedEntry(pullDownList01, getSubEventCb().getSelectedIndex()));
        model.setEstimate(getEstimateText().getText());
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        GeneralPurposeObject pdlo = ((GeneralPurposeObject) pullDownList01.get(model.getSubEventId()));

        final HashMap cleOptionsType = new HashMap();
        cleOptionsType.put(teoTime, model.getEstimate());
        cleOptionsType.put(teoUnits, pdlo.getCode());

        propertyMap.put(teoSchema, cleOptionsType);

    }

    private int findSelectedEntry(List itemList, String code) {
        for (int x = 0, n = itemList.size(); x < n; x++) {
            GeneralPurposeObject pdlo = (GeneralPurposeObject) itemList.get(x);

            if (pdlo.getCode().equalsIgnoreCase(code))
                return x;
        }

        return 0;
    }

    private String findSelectedEntry(List itemList, int id) {
        GeneralPurposeObject pdlo = (GeneralPurposeObject) itemList.get(id);
        return pdlo.getCode();
    }

    private class GeneralPurposeObject {
        private final int id;

        private final String code;

        private final String desc;

        public GeneralPurposeObject(int id, String code, String desc) {
            this.id = id;
            this.code = code;
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String toString() {
            return desc;
        }
    }

    private class UpdateListener extends KeyAdapter implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            stepUpdateViewStateHandleExceptions();
        }

        public void keyReleased(KeyEvent e) {
            stepUpdateViewStateHandleExceptions();
        }
    }
}
