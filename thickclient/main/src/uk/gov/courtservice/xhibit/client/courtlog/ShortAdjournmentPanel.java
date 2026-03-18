package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

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
public class ShortAdjournmentPanel extends CourtLogEventPanel {
    private final String saoSchema, saoType, saoTime;

    private Collection pullDownList01 = new Vector();

    private final ShortAdjournmentModel model;

    private JComboBox subEventCb;

    private XTimePanel adjournTimePanel;

    private JLabel adjournmentTimeLabel;

    private JLabel subEventCbLabel;

    private JLabel eventNameLabel;

    public ShortAdjournmentPanel(XDialog parent, ShortAdjournmentModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        // Set XSD schema fields
        saoSchema = ResourceBundleHelper.getResource(XhibitBundles.ShortAdjournment, "E" + model.getEventType()
                + "_ShortAdjournOptions");
        saoType = ResourceBundleHelper
                .getResource(XhibitBundles.ShortAdjournment, "E" + model.getEventType() + "_Type");
        saoTime = ResourceBundleHelper
                .getResource(XhibitBundles.ShortAdjournment, "E" + model.getEventType() + "_Time");

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        adjournTimePanel = new XTimePanel(this); // Generic time panel

        this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(5, 2, 25, 2), 0, 0));
        this.add(getSubEventCbLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSubEventCb(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAdjournmentTimeLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(adjournTimePanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    }

    public void stepInitialise() throws CSRecoverableException {
        Vector lovTypes = (Vector) RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd", saoType);
        pullDownList01.add(new PullDownListObject(0, "select", ResourceBundleHelper.getResource(
                XhibitBundles.ShortAdjournment, "select")));

        for (int x = 0; x < lovTypes.size(); x++) {
            String name = (String) lovTypes.get(x);
            String value = (String) lovTypes.get(x);
            value = ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, name);

            pullDownList01.add(new PullDownListObject(x + 1, name, value));
        }

        super.stepInitialise();

    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        // Save the data in the model
        HashMap hashMap = (HashMap) propertyMap.get(saoSchema);
        model.setSubEventCode(((String) hashMap.get(saoType)));
        model.setAdjournmentHours(((String) hashMap.get(saoTime)).substring(0, 2));
        model.setAdjournmentMinutes(((String) hashMap.get(saoTime)).substring(3, 5));
        model.setSubEventCode(((String) hashMap.get(saoType)));
        model.setSubEventId(findSelectedEntry((Vector) pullDownList01, model.getSubEventCode()));
    }

    protected boolean isMandatoryFieldsCompleted() {
        boolean result = true;

        if (getSubEventCb().getSelectedIndex() == 0) {
            result = false;
        } else {
            if (adjournTimePanel.isMandatoryFieldsCompleted()) {
                // NoAction
            } else {
                result = false;
            }
        }

        return result;
    }

    private JComboBox getSubEventCb() {
        if (subEventCb == null) {
            subEventCb = new JComboBox();
            subEventCb.setMaximumSize(new Dimension(275, 21));
            subEventCb.setMinimumSize(new Dimension(275, 21));
            subEventCb.setPreferredSize(new Dimension(275, 21));
            subEventCb.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.ShortAdjournment, "subEventCb"));
            populateComboBox(getSubEventCb(), (Vector) pullDownList01);
            subEventCb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }

        return subEventCb;
    }

    private JLabel getSubEventCbLabel() {
        if (subEventCbLabel == null) {
            subEventCbLabel = new JLabel();
            subEventCbLabel
                    .setText(ResourceBundleHelper.getResource(XhibitBundles.ShortAdjournment, "subEventCbLabel"));
        }

        return subEventCbLabel;
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(ResourceBundleHelper.getResource(XhibitBundles.ShortAdjournment,
                    "eventNameLabel"));
        }

        return eventNameLabel;
    }

    private JLabel getAdjournmentTimeLabel() {
        if (adjournmentTimeLabel == null) {
            adjournmentTimeLabel = new JLabel();
            adjournmentTimeLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.ShortAdjournment,
                    "adjournmentTimeLabel"));
        }

        return adjournmentTimeLabel;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        log.debug("In moveModelToScreen");
        Calendar theTime = null;

        if (model.isInEditMode()) {
            getSubEventCb().setSelectedIndex(model.getSubEventId());
            theTime = Calendar.getInstance();
            theTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(model.getAdjournmentHours()));
            theTime.set(Calendar.MINUTE, Integer.parseInt(model.getAdjournmentMinutes()));
            adjournTimePanel.setTime(theTime);
        } else {
            adjournTimePanel.setTime(theTime);
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        model.setSubEventId(getSubEventCb().getSelectedIndex());
        model.setSubEventCode(findSelectedEntry((Vector) pullDownList01, getSubEventCb().getSelectedIndex()));

        model.setAdjournmentHours(Integer.toString(adjournTimePanel.getHour()));
        model.setAdjournmentMinutes(Integer.toString(adjournTimePanel.getMinute()));
    }

    private void populateComboBox(JComboBox comboBox, Vector comboBoxData) {
        comboBox.removeAllItems();

        for (int x = 0; x < comboBoxData.size(); x++) {
            comboBox.addItem(comboBoxData.get(x));
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        PullDownListObject pdlo = ((PullDownListObject) ((Vector) pullDownList01).get(model.getSubEventId()));

        HashMap saOptionsType = new HashMap();
        saOptionsType.put(saoType, pdlo.getCode());
        saOptionsType.put(saoTime, model.getAdjournmentHours() + ":" + model.getAdjournmentMinutes());

        propertyMap.put(saoSchema, saOptionsType);

        // we need to set which defendants were listed when this event was
        // created as it impacts the defendant hearing duration calculation
        SimpleEventPanel.setListedDefendants(propertyMap, model.getXac().getApplicationCaseModel());
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();

        adjournTimePanel.stepValidate();
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
