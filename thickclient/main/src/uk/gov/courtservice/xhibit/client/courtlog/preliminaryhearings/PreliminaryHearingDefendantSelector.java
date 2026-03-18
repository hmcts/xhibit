package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogEventLevelPanel;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextModel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsParentPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.util.CourtLogAuditPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: PreliminaryHearingDefendantSelector
 * </p>
 * <p>
 * Description: This panel contains a list of defendants for the case and case
 * and defendant level court log events for preliminary hearings. It is based on
 * the DirectionsforDefendantSelector class used for directions events.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class PreliminaryHearingDefendantSelector extends DirectionsParentPanel {
    private final XhibitApplicationController xac;

    private final CourtLogAuditPanel logAuditPanel;

    private final HashMap modelMap = new HashMap();

    private DefendantLevelEventsPanel dlep = null;

    private CourtLogEventLevelPanel defendantPanel = null;

    private JList defCombo = null;

    private DirectionsForDefendantValue model;

    private boolean checkModified = false;

    /**
     * This panel requires the xac to access case defendant on case information.
     * The logAuditPanelis required to compare the date with the
     * LongAdjournementPanel date.
     * 
     * @param xac
     * @param logAuditPanel
     * @throws CSRecoverableException
     */
    public PreliminaryHearingDefendantSelector(XhibitApplicationController xac, CourtLogAuditPanel logAuditPanel)
            throws CSRecoverableException {
        this.xac = xac;
        this.logAuditPanel = logAuditPanel;

        stepInitialise();
        init();
        getDefendantCombo().setSelectedIndex(-1);
        stepActivate();
    }

    /**
     * Finds DefendantOnCaseBasicValue for passed in defendantId
     * 
     * @param defendantId
     * @return DefendantOnCaseBasicValue
     */
    public DefendantOnCaseBasicValue findDefOnCase(Integer defendantId) {
        if (xac != null) {
            Collection c = xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantOnCaseBasicValues();
            Iterator iter = c.iterator();
            while (iter.hasNext()) {
                DefendantOnCaseBasicValue item = (DefendantOnCaseBasicValue) iter.next();
                if (item.getDefendantID().equals(defendantId)) {
                    return item;
                }
            }
        }

        return null;
    }

    /**
     * Returns Collection of DirectionsForDefendantValue objects
     * 
     * @return Collection
     */
    public Collection getModels() {
        ArrayList al = new ArrayList();
        Iterator iter = modelMap.values().iterator();
        while (iter.hasNext()) {
            DirectionsForDefendantValue item = (DirectionsForDefendantValue) iter.next();
            if (item.getCourtLogCRUDValues() != null && item.getCourtLogCRUDValues().length > 0) {
                al.add(item);
            }
        }

        Sorter.sort((List) al, new String[] { "surname", "firstName" });

        return al;
    }

    /**
     * Puts the widgets on the screen
     * 
     * @throws CSRecoverableException
     */
    private void init() throws CSRecoverableException {
        PropertyChangeListener pcl = new ModifyPropertyListener();
        dlep = new DefendantLevelEventsPanel(new DirectionsForDefendantValue[] { model }, this.xac, logAuditPanel);
        dlep.addPropertyChangeListener(XPanel.property_modified, pcl);

        this.setLayout(new GridBagLayout());
        this.add(getDefendantPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(dlep, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));
    }

    /**
     * Returns a JList containing defendant names
     * 
     * @return JComboBox
     * @throws CSRecoverableException
     */
    private JList getDefendantCombo() throws CSRecoverableException {
        if (defCombo == null) {
            defCombo = getDefendantPanel().getDefendantList();
            defCombo.addListSelectionListener(new DefListListener(defCombo));
        }
        return defCombo;
    }

    /**
     * Returns CourtLogEventLevelPanel for Defendants
     * 
     * @return CourtLogEventLevelPanel
     * @throws CSRecoverableException
     */
    private CourtLogEventLevelPanel getDefendantPanel() throws CSRecoverableException {
        if (defendantPanel == null) {
            FreeTextModel ftm = new FreeTextModel();
            // Need one defendant level event, so picking bail as this is in
            // the mapping
            ftm.setEventType(PDHConstants.DEF_BAIL.toString());
            ftm.setXac(xac);
            ftm.setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_LIST);
            defendantPanel = new CourtLogEventLevelPanel(ftm, this);
        }
        return defendantPanel;
    }

    /**
     * Life-cycle method that calls stepInitialise on the
     * CourtLogEventLevelPanel
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        model = new DirectionsForDefendantValue();
        model.setDirectionsForDefendantBasicValue(new XhbDirectionsForDefendantBasicValue());
        getDefendantPanel().stepInitialise();
    }

    /**
     * Life-cycle method called when the screen is made visible. Passes the
     * model to the DefendantLevelEventsPanel and runs it's stepActivate method.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        checkModified = false;
        try {
            if (getDefendantCombo().getSelectedIndex() < 0) {
                getDefendantCombo().setSelectedIndex(0);
            }
            DirectionsForDefendantValue[] newModel = getSelectedDefendantModel();
            dlep.setModel(newModel);
            // temporarily disable the processing of stepActivate otherwise
            // the super.stepActivate will call it resulting in a combo box
            // that can't be changed !
            boolean boolTemp = getDefendantPanel().isProcessStepMethods();
            getDefendantPanel().setProcessStepMethods(false);
            super.stepActivate();
            getDefendantPanel().setProcessStepMethods(boolTemp);
        } finally {
            checkModified = true;
        }
    }

    /**
     * Life-cycle method called when the screen is made invisible
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        super.stepDeactivate();
        if (dlep.getModified()) {
            this.modified();
            // Work necessary for CJSE population.
            DirectionsForDefendantValue[] modModels = dlep.getModel();

            for (int m = 0; m < modModels.length; m++) {
                DirectionsForDefendantValue defModModel = modModels[m];

                // Get the defedantBasicValue
                DefendantBasicValue defendantBasicValue = getDefendantPanel().findDefendantUsingDefOnCaseId(
                        defModModel.getDirectionsForDefendantBasicValue().getDefendantOnCaseId());

                CourtLogCRUDValue[] cruds = dlep.getCRUDValue(defendantBasicValue, defModModel
                        .getDirectionsForDefendantBasicValue().getDefendantOnCaseId());

                Calendar dateTimeCal = logAuditPanel.getDateTime();
                Date dateTime = dateTimeCal.getTime();

                for (int i = 0; i < cruds.length; i++) {
                    cruds[i].setCaseId(xac.getApplicationCaseModel().getScheduledHearingValue().getCaseId());
                    cruds[i].setScheduledHearingId(logAuditPanel.getScheduledHearingId());
                    cruds[i].setDefendantOnCaseId(defModModel.getDirectionsForDefendantBasicValue()
                            .getDefendantOnCaseId());
                    cruds[i].setEntryDate(dateTime);

                    Map crudMap = cruds[i].getPropertyMap();
                }

                defModModel.setCourtLogCRUDValue(cruds);
                // ST need the dateTime set on the basic values, this is used to
                // check for more recent directions before updating the db
                defModModel.getDirectionsForDefendantBasicValue().setDateTime(dateTimeCal.getTime());
                modelMap.put(defModModel.getDirectionsForDefendantBasicValue().getDefendantOnCaseId(), defModModel);
            }
        }
    }

    /**
     * Returns DirectionsForDefendantValue(Object) for selected defendant in the
     * Defendant combo box
     * 
     * @return DirectionsForDefendantValue[]
     * @throws CSRecoverableException
     */
    private DirectionsForDefendantValue[] getSelectedDefendantModel() throws CSRecoverableException {
        Object[] selection = getDefendantCombo().getSelectedValues();
        DefendantBasicValue[] dv = new DefendantBasicValue[selection.length];
        for (int i = 0; i < selection.length; i++) {
            dv[i] = (DefendantBasicValue) selection[i];
        }

        DirectionsForDefendantValue[] dfdList = new DirectionsForDefendantValue[dv.length];
        for (int i = 0; i < dv.length; i++) {
            DefendantOnCaseBasicValue dc = findDefOnCase(dv[i].getId());
            XHIBITConstant.debug("getSelectedDefendantModel():: Defendant id=" + dv[i].getId());

            if (modelMap.containsKey(dc.getId())) {
                dfdList[i] = (DirectionsForDefendantValue) modelMap.get(dc.getId());
            } else {
                DirectionsForDefendantValue dfdv = new DirectionsForDefendantValue();
                dfdv.setDirectionsForDefendantBasicValue(new XhbDirectionsForDefendantBasicValue());
                dfdv.getDirectionsForDefendantBasicValue().setDefendantOnCaseId(dc.getId());
                dfdv.setDefendantBasicValue(dv[i]);
                modelMap.put(dc.getId(), dfdv);
                XHIBITConstant.debug("getSelectedDefendantModel():: DefendantOnCase id=" + dc.getId());
                dfdList[i] = dfdv;
            }
        }

        return dfdList;
    }

    /**
     * Sets modified variable
     */
    public void modified() {
        if (checkModified) {
            setModified(true);
        }
    }

    /**
     * Resets the panel and child modified variables
     */
    public void resetModified() {
        dlep.resetModified();
        setModified(false);
    }

    /**
     * Sets enable property of passed in component. Method is called recursively
     * if component is instance of a container.
     * 
     * @param comp
     * @param enabled
     */
    private static void setChildrenEnabled(Component comp, boolean enabled) {
        comp.setEnabled(enabled);
        if (comp instanceof Container) {
            Container container = (Container) comp;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                setChildrenEnabled(container.getComponent(idx), enabled);
            }
        }
    }

    private class ModifyPropertyListener implements PropertyChangeListener, Serializable {
        public void propertyChange(PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                modified();
            }
        }
    }

    private class DefListListener implements ListSelectionListener {
        JList list;

        int[] currSelection = new int[] { 0 };

        boolean revertingSelection = false;

        public DefListListener(JList list) {
            this.list = list;
        }

        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting())
                return;

            try {
                if (!revertingSelection) {
                    stepDeactivate();

                    stepActivate();
                    currSelection = list.getSelectedIndices();
                }
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
                revertingSelection = true;
                list.setSelectedIndices(currSelection);
                revertingSelection = false;
            }
        }
    }

    private class ComboListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                try {
                    stepDeactivate();
                } catch (CSRecoverableException ex) {
                    XHIBITConstant.handleError(ex);
                }
            }

            // Ignore extra messages.
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem() != null) {
                    try {
                        stepActivate();
                    } catch (CSRecoverableException ex) {
                        XHIBITConstant.handleError(ex);
                    }
                }
            }
        }
    }
}
