package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Model holding information required for added offences.
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
 * @author Bal Bhamra
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 14-08-2003 AW Daley AddedOffencesPanel changed to XPanel
 */
public class AddedOffencesPanelModel {
    private int index;

    private List<OffenceValue> offenceList;

    private XPanel addedOffencesPanel;

    private ChargeWizardModel model;

    public AddedOffencesPanelModel() {
    }

    public AddedOffencesPanelModel(List offenceList, XPanel addedOffencesPanel, ChargeWizardModel model) {
        this.offenceList = offenceList;
        this.addedOffencesPanel = addedOffencesPanel;
        this.model = model;
    }

    public int getSelectedRowIndex() {
        return index;
    }

    public void setSelectedRowIndex(int index) {
        this.index = index;
    }

    public List<OffenceValue> getOffenceList() {
        return offenceList;
    }
    
    public void setOffenceList(List<OffenceValue> offenceList) {
        this.offenceList = offenceList;
    }
    
    public boolean addOffence(OffenceValue offenceValue) {
        return offenceList.add(offenceValue);
    }
    
    public OffenceValue removeOffenceAtIndex(int i) {
        return offenceList.remove(i);
    }

    public XPanel getAddedOffencesPanel() {
        return addedOffencesPanel;
    }

    public void setAddedOffencesPanel(XPanel addedOffencesPanel) {
        this.addedOffencesPanel = addedOffencesPanel;
    }

    public ChargeWizardModel getChargeWizardModel() {
        return model;
    }

    public void setChargeWizardModel(ChargeWizardModel model) {
        this.model = model;
    }
}
