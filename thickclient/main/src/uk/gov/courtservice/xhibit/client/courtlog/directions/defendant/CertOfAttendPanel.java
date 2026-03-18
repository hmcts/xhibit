package uk.gov.courtservice.xhibit.client.courtlog.directions.defendant;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsForDefendantPanel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
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

public class CertOfAttendPanel extends XDirectionsForDefendantPanel {
    private ButtonGroup padGroup = new ButtonGroup();

    private GridBagLayout gridBagPAD = new GridBagLayout();

    private Collection thisRestriction = null;

    private HashMap buttonMap = new HashMap();

    private final String pdLookup = "E" + getEventType() + "_Certificate_Of_Attendance";

    private final String borderTitle = "CertificateOfAttendanceBorder";

    private DirectionsForDefendantValue[] model;

    // private DirectionsForDefendantBasicValue dfv;

    public CertOfAttendPanel(DirectionsForDefendantValue[] model) throws CSRecoverableException {
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
        return PDHConstants.DEF_CERTATTENDANCE;
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
                if (dfv.getCertAttendance() != null) {
                    String xmlKey = PDHConstants.getKeyForDb(dfv.getCertAttendance().toString(), getRestrictions());
                    JRadioButton toSelect = (JRadioButton) buttonMap.get(xmlKey);
                    if (toSelect != null) {
                        toSelect.setSelected(true);
                    } else {
                        JRadioButton toNotSelect = (JRadioButton) buttonMap.get(PDHConstants.notselected);
                        toNotSelect.setSelected(true);
                    }
                } else {
                    JRadioButton toNotSelect = (JRadioButton) buttonMap.get(PDHConstants.notselected);
                    toNotSelect.setSelected(true);
                }
            }
        }
    }

    public void moveScreenToModel() {
        if (model != null && getModified()) {
            JRadioButton selectedRB = PDHConstants.getSelectedRadio(padGroup);
            if (selectedRB != null && selectedRB.getClientProperty(PDHConstants.dbCode) != null) {
                for (int i = 0; i < model.length; i++) {
                    XhbDirectionsForDefendantBasicValue dfv = model[i].getDirectionsForDefendantBasicValue();
                    dfv.setCertAttendance((String) selectedRB.getClientProperty(PDHConstants.dbCode));
                }
            }
        }
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        if (model != null) {
            for (int i = 0; i < model.length; i++) {
                XhbDirectionsForDefendantBasicValue dfv = model[i].getDirectionsForDefendantBasicValue();
                if (dfv.getDefendantOnCaseId().equals(defOnCaseId) && dfv.getCertAttendance() != null) {
                    crud.put(pdLookup, PDHConstants.getKeyForDb(dfv.getCertAttendance().toString(), getRestrictions()));
                    break;
                }
                // JRadioButton selectedRB =
                // PDHConstants.getSelectedRadio(padGroup);
                // if (selectedRB != null &&
                // selectedRB.getClientProperty(PDHConstants.xmlCode)!= null) {
                // crud.put(pdLookup,
                // selectedRB.getClientProperty(PDHConstants.xmlCode));
                // }
            }
        }
    }

    private Collection getRestrictions() {
        if (thisRestriction == null) {
            thisRestriction = RestrictionFinder.getRestrictingValues(PDHConstants.DEF_CERTATTENDANCE.toString()
                    + ".xsd", pdLookup);
        }
        return thisRestriction;
    }

    private void init() {
        this.setLayout(gridBagPAD);
        this.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions,
                borderTitle)));

        this.add(getWarningLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));

        Object[] rb = getRestrictions().toArray();
        for (int i = 0; i < rb.length; i++) {
            JRadioButton thisRadio = PDHConstants.getRb((String) rb[i], buttonMap);
            padGroup.add(thisRadio);
            thisRadio.addItemListener(new ItemChangeListener(this));
            thisRadio.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED && !isActivating())
                        clearWarning(padGroup);
                }
            });
            this.add(thisRadio, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, PDHConstants.PDH_INSETS, 0, 0));
        }
        padGroup.add(PDHConstants.getNotSelectRb(buttonMap));
    }

    public void setModel(DirectionsForDefendantValue[] newModel) {
        model = newModel;
        // dfv = newModel.getDirectionsForDefendantBasicValue();
    }

    private boolean isModelsIdentical() {
        if (model.length <= 1)
            return true;

        for (int i = 0; i < model.length - 1; i++) {
            XhbDirectionsForDefendantBasicValue dfd1 = model[i].getDirectionsForDefendantBasicValue();
            XhbDirectionsForDefendantBasicValue dfd2 = model[i + 1].getDirectionsForDefendantBasicValue();
            if ((dfd1.getCertAttendance() == null && dfd2.getCertAttendance() != null)
                    || (dfd1.getCertAttendance() != null && !dfd1.getCertAttendance().equals(dfd2.getCertAttendance()))) {
                return false;
            }
        }
        return true;
    }

    public void stepValidate() {
    }

    public void stepUpdateViewState() {
    }

}