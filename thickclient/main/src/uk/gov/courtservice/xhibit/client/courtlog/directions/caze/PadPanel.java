package uk.gov.courtservice.xhibit.client.courtlog.directions.caze;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsPanel;
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

public class PadPanel extends XDirectionsPanel {
    private ButtonGroup padGroup = new ButtonGroup();

    private GridBagLayout gridBagPAD = new GridBagLayout();

    private TitledBorder padBorder;

    private Border border2;

    private Collection thisRestriction = null;

    private HashMap buttonMap = new HashMap();

    private final String pdLookup = "E" + getEventType() + "_P_And_D";

    private final String borderTitle = "PadBorder";

    private DirectionsForCaseValue model;

    private XhbDirectionsForCaseBasicValue dcv;

    public PadPanel(DirectionsForCaseValue model) throws CSRecoverableException {
        setModel(model);
        init();
        stepActivate();
    }

    public Integer getEventType() {
        return PDHConstants.CASE_PDFORM;
    }

    public void setModel(DirectionsForCaseValue newModel) {
        this.model = newModel;
        dcv = newModel.getDirectionsForCaseBasicValue();
    }

    public void moveModelToScreen() {
        if (dcv != null) {
            String xmlKey = PDHConstants.getKeyForDb(dcv.getHasPanddForm(), getRestrictions());

            JRadioButton toSelect = (JRadioButton) buttonMap.get(xmlKey);
            if (toSelect != null) {
                toSelect.setSelected(true);
            } else {
                JRadioButton toNotSelect = (JRadioButton) buttonMap.get(PDHConstants.notselected);
                toNotSelect.setSelected(true);
            }
        }
    }

    public void moveScreenToModel() {
        if (dcv != null) {
            JRadioButton selectedRB = PDHConstants.getSelectedRadio(padGroup);
            if (selectedRB != null && selectedRB.getClientProperty(PDHConstants.dbCode) != null) {
                dcv.setHasPanddForm((String) selectedRB.getClientProperty(PDHConstants.dbCode));
            }
        }
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        if (dcv != null) {
            JRadioButton selectedRB = PDHConstants.getSelectedRadio(padGroup);
            if (selectedRB != null && selectedRB.getClientProperty(PDHConstants.xmlCode) != null) {
                crud.put(pdLookup, selectedRB.getClientProperty(PDHConstants.xmlCode));
            }
        }
    }

    private Collection getRestrictions() {
        if (thisRestriction == null) {
            thisRestriction = RestrictionFinder.getRestrictingValues(PDHConstants.CASE_PDFORM.toString() + ".xsd",
                    pdLookup);
        }
        return thisRestriction;
    }

    private void init() {
        this.setLayout(gridBagPAD);
        padBorder = new TitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions, borderTitle));
        border2 = BorderFactory.createCompoundBorder(padBorder, BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setBorder(border2);

        Object[] rb = getRestrictions().toArray();
        for (int i = 0; i < rb.length; i++) {
            JRadioButton thisRadio = PDHConstants.getRb((String) rb[i], buttonMap);
            thisRadio.addItemListener(new ItemChangeListener(this));
            padGroup.add(thisRadio);
            this.add(thisRadio, new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        }
        padGroup.add(PDHConstants.getNotSelectRb(buttonMap));
    }

    public void stepValidate() {
    }

    public void stepUpdateViewState() {
    }

    public void modified() {
        super.modified();
    }
}