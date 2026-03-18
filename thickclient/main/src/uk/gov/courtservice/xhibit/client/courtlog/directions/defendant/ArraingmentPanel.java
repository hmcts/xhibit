package uk.gov.courtservice.xhibit.client.courtlog.directions.defendant;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
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

public class ArraingmentPanel extends XDirectionsForDefendantPanel {
    private static final String borderTitle = "ArraignmentBorder";

    private ButtonGroup padGroup = new ButtonGroup();

    private GridBagLayout gridBagPAD = new GridBagLayout();

    private Collection thisRestriction = null;

    private HashMap buttonMap = new HashMap();

    private DirectionsForDefendantValue[] model;

    // private DirectionsForDefendantBasicValue dfv;

    public ArraingmentPanel(DirectionsForDefendantValue[] model) throws CSRecoverableException {
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
        return PDHConstants.DEF_ARRAIGNMENT;
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
                String xmlKey = PDHConstants.getKeyForDb(dfv.getArraigned(), getRestrictions());

                JRadioButton toSelect = (JRadioButton) buttonMap.get(xmlKey);
                if (toSelect != null) {
                    toSelect.setSelected(true);
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
                    dfv.setArraigned((String) selectedRB.getClientProperty(PDHConstants.dbCode));
                }
            }
        }
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        if (model != null) {
            // JRadioButton selectedRB =
            // PDHConstants.getSelectedRadio(padGroup);
            // if (selectedRB != null &&
            // selectedRB.getClientProperty(PDHConstants.xmlCode)!= null) {
            for (int i = 0; i < model.length; i++) {
                XhbDirectionsForDefendantBasicValue dfv = model[i].getDirectionsForDefendantBasicValue();
                if (dfv.getDefendantOnCaseId().equals(defOnCaseId)) {
                    if (dfv.getArraigned() != null) {
                        String xmlKey = PDHConstants.getKeyForDb(dfv.getArraigned(), getRestrictions());
                        if (xmlKey.equals("Arraigned")) {
                            crud.put("E" + getEventType() + "_Arraignment", new Boolean(true));
                        } else {
                            crud.put("E" + getEventType() + "_Arraignment", new Boolean(false));
                        }
                    }
                }
            }
            // }
        }
    }

    private Collection getRestrictions() {
        if (thisRestriction == null) {
            ArrayList al = new ArrayList();
            al.add("Arraigned");
            al.add("NotArraigned");
            thisRestriction = al;
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

            thisRadio.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED && !isActivating())
                        clearWarning(padGroup);
                }
            });
            thisRadio.addItemListener(new ItemChangeListener(this));
            padGroup.add(thisRadio);
            this.add(thisRadio, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, PDHConstants.PDH_INSETS, 0, 0));
        }
        padGroup.add(PDHConstants.getNotSelectRb(buttonMap));
    }

    public void setModel(DirectionsForDefendantValue[] newModel) {
        model = newModel;
    }

    private boolean isModelsIdentical() {
        if (model.length <= 1)
            return true;

        for (int i = 0; i < model.length - 1; i++) {
            XhbDirectionsForDefendantBasicValue dfd1 = model[i].getDirectionsForDefendantBasicValue();
            XhbDirectionsForDefendantBasicValue dfd2 = model[i + 1].getDirectionsForDefendantBasicValue();
            if ((dfd1.getArraigned() == null && dfd2.getArraigned() != null)
                    || (dfd1.getArraigned() != null && !dfd1.getArraigned().equals(dfd2.getArraigned()))) {
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