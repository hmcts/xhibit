package uk.gov.courtservice.xhibit.client.courtlog.directions.defendant;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DateChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsFactory;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateKeyListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateRadioButtonListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsForDefendantPanel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: P&D Form Panel
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class FormBPanel extends XDirectionsForDefendantPanel {
    private ButtonGroup padGroup = new ButtonGroup();

    private GridBagLayout gridBagPAD = new GridBagLayout();

    private Collection thisRestriction = null;

    private HashMap buttonMap = new HashMap();

    private final String pdLookup = "E" + getEventType() + "_Form_B_Options";

    private final String borderTitle = "FormBBorder";

    private final String listenButton = "E" + getEventType() + "_To_Be_Filed";

    private DirectionsForDefendantValue[] model;

    // private DirectionsForDefendantBasicValue dfv;

    public FormBPanel(DirectionsForDefendantValue[] model) throws CSRecoverableException {
        setModel(model);
        init();
        // stepActivate();
    }

    public void stepActivate() throws CSRecoverableException {
        // reset warning view first
        clearWarning(padGroup);
        super.stepActivate();
    }

    public Integer getEventType() {
        return PDHConstants.DEF_FORMB;
    }

    public void moveModelToScreen() {
        if (model != null) {
            if (!isModelsIdentical()) {
                showWarning(padGroup);
            }
            // Regardless of whether the models are identical or not, use
            // the first
            // defendant for displaying on the screen
            XhbDirectionsForDefendantBasicValue dfv = model[0].getDirectionsForDefendantBasicValue();
            if (dfv != null) {
                String xmlKey = PDHConstants.getKeyForDb(dfv.getFiledFormB(), getRestrictions());

                JRadioButton toSelect = (JRadioButton) buttonMap.get(xmlKey);
                if (toSelect != null) {
                    toSelect.setSelected(true);
                } else {
                    JRadioButton toNotSelect = (JRadioButton) buttonMap.get(PDHConstants.notselected);
                    toNotSelect.setSelected(true);
                }
                if (dfv.getToBeFiledBy() != null) {
                    getDateBy().setDate(dfv.getToBeFiledBy());
                }
            }
        }
    }

    public void moveScreenToModel() throws CSValidationException {
        if (model != null && getModified()) {
            JRadioButton selectedRB = PDHConstants.getSelectedRadio(padGroup);
            if (selectedRB != null && selectedRB.getClientProperty(PDHConstants.dbCode) != null) {
                for (int i = 0; i < model.length; i++) {
                    XhbDirectionsForDefendantBasicValue dfv = model[i].getDirectionsForDefendantBasicValue();
                    dfv.setFiledFormB((String) selectedRB.getClientProperty(PDHConstants.dbCode));

                    if (getDateBy().isEnabled()) {
                        dfv.setToBeFiledBy(getDateBy().getDate().getTime());
                    } else {
                        dfv.setToBeFiledBy(null);
                    }
                }
            }
        }
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        if (model != null) {
            String eventCode = "E" + getEventType().toString();
            // JRadioButton selectedRB =
            // PDHConstants.getSelectedRadio(padGroup);
            // if (selectedRB != null &&
            // selectedRB.getClientProperty(PDHConstants.xmlCode)!= null) {
            for (int i = 0; i < model.length; i++) {
                XhbDirectionsForDefendantBasicValue dfv = model[i].getDirectionsForDefendantBasicValue();
                if (dfv.getDefendantOnCaseId().equals(defOnCaseId) && dfv.getFiledFormB() != null) {
                    HashMap formOptions = new HashMap();
                    formOptions.put(pdLookup, PDHConstants.getKeyForDb(dfv.getFiledFormB(), getRestrictions()));
                    if (dfv.getToBeFiledBy() != null) {
                        formOptions.put(eventCode + "_To_Be_Filed_Date", XDateFormat.format(dfv.getToBeFiledBy(),
                                XDateFormat.DATEFORMAT));
                    }
                    crud.put(eventCode + "_Form_B", formOptions);
                }
            }
        }
    }

    private Collection getRestrictions() {
        if (thisRestriction == null) {
            thisRestriction = RestrictionFinder.getRestrictingValues(PDHConstants.DEF_FORMB.toString() + ".xsd",
                    pdLookup);
        }
        return thisRestriction;
    }

    private void init() {
        this.setLayout(gridBagPAD);
        this.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions,
                borderTitle)));

        this.add(getWarningLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));

        ActionListener rbListner = new UpdateStateRadioButtonListener(this);
        Object[] rb = getRestrictions().toArray();
        int i = 0;
        for (i = 0; i < rb.length; i++) {
            JRadioButton thisRadio = PDHConstants.getRb((String) rb[i], buttonMap);
            thisRadio.addActionListener(rbListner);
            thisRadio.addItemListener(new ItemChangeListener(this));
            thisRadio.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED && !isActivating())
                        clearWarning(padGroup);
                }
            });
            padGroup.add(thisRadio);
            this.add(thisRadio, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, PDHConstants.PDH_INSETS, 0, 0));
        }
        padGroup.add(PDHConstants.getNotSelectRb(buttonMap));
        this.add(getDateBy(), new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, PDHConstants.PDH_INSETS, DirectionsFactory.rbDisabled.getIconWidth(), 0));
    }

    private XDatePanel dateBy = null;

    public XDatePanel getDateBy() {
        if (dateBy == null) {
            XDatePanel jt = new XDatePanel(this);
            jt.addKeyListener(new UpdateStateKeyListener(this));
            jt.getDateComponent().addMChangeListener(new DateChangeListener(this));
            jt.getDateComponent().addMChangeListener(new MChangeListener() {
                public void valueChanged(MChangeEvent e) {
                    if (e.getType() == MChangeEvent.CHANGE && !isActivating()) {
                        clearWarning(padGroup);
                    }
                }
            });
            dateBy = jt;
        }
        return dateBy;
    }

    public void setModel(DirectionsForDefendantValue[] newModel) {
        model = newModel;
        // dfv = newModel.getDirectionsForDefendantBasicValue();
    }

    public void stepValidate() throws CSValidationException {
        getDateBy().stepValidate();
    }

    public void stepUpdateViewState() {
        JRadioButton selectedRB = PDHConstants.getSelectedRadio(padGroup);
        boolean enable = false;
        if (selectedRB != null) {
            if (selectedRB.getClientProperty(PDHConstants.xmlCode) != null
                    && selectedRB.getClientProperty(PDHConstants.xmlCode).equals(listenButton)) {
                enable = true;
            }
        }
        getDateBy().setDateEnabled(enable);
        getDateBy().setRequired(enable);
    }

    private boolean isModelsIdentical() {
        if (model.length <= 1)
            return true;

        for (int i = 0; i < model.length - 1; i++) {
            XhbDirectionsForDefendantBasicValue dfd1 = model[i].getDirectionsForDefendantBasicValue();
            XhbDirectionsForDefendantBasicValue dfd2 = model[i + 1].getDirectionsForDefendantBasicValue();
            if ((dfd1.getFiledFormB() == null && dfd2.getFiledFormB() != null)
                    || (dfd1.getFiledFormB() != null && !dfd1.getFiledFormB().equals(dfd2.getFiledFormB()))) {
                return false;
            } else {
                if ((dfd1.getToBeFiledBy() == null && dfd2.getToBeFiledBy() != null)
                        || (dfd1.getToBeFiledBy() != null && !dfd1.getToBeFiledBy().equals(dfd2.getToBeFiledBy()))) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void setWarningVisible(ButtonGroup buttonGroup, boolean visible) {
        super.setWarningVisible(buttonGroup, visible);
        getDateBy().getDateComponent().getDisplay().setBackground(
                visible ? PDHConstants.DISABLED_COLOR : PDHConstants.TEXT_AREA_BACKGROUND);
    }
}