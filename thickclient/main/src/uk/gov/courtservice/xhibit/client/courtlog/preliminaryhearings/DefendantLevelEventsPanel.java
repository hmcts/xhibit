package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.LongAdjournmentModel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsParentPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.BailCustodyPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.FormBPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.IdentificationPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.LongAdjournDirectionsPanel;
import uk.gov.courtservice.xhibit.client.courtlog.util.CourtLogAuditPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: DefendantLevelEventsPanel
 * </p>
 * <p>
 * Description: This panels allows the entry of information which forms
 * preliminary hearing court log events for the defendant. It is based on the
 * DirectionsForDefendant class which performs a similar function for
 * Directions.
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
public class DefendantLevelEventsPanel extends DirectionsParentPanel {
    private GridBagLayout gridBagMain = new GridBagLayout();

    private IdentificationPanel ip;

    private BailCustodyPanel bp;

    private FormBPanel fp;

    private LongAdjournDirectionsPanel lap;

    private XhibitApplicationController xac;

    public CourtLogAuditPanel logAuditPanel;

    boolean checkModified = false;

    private DirectionsForDefendantValue[] model;

    /**
     * The model and xac paramaters taken allow the setting of defendant
     * information. The logAuditPanel is required for LongAdjournment Date
     * validation.
     * 
     * @param model
     * @param xac
     * @param logAuditPanel
     * @throws CSRecoverableException
     */
    public DefendantLevelEventsPanel(DirectionsForDefendantValue[] model, XhibitApplicationController xac,
            CourtLogAuditPanel logAuditPanel) throws CSRecoverableException {
        this.model = model;
        this.xac = xac;
        this.logAuditPanel = logAuditPanel;

        stepInitialise();
        init();
        stepActivate();
    }

    /**
     * Creates CourtLogCRUDValues from child panels and returns array.
     * 
     * @return CourtLogCRUDValue[]
     * @throws CSRecoverableException
     */
    public CourtLogCRUDValue[] getCRUDValue(DefendantBasicValue dbv, Integer defOnCaseId) throws CSRecoverableException {
        Collection cruds = new ArrayList();
        XDirectionsPanel[] panels = new XDirectionsPanel[] { ip, bp, fp };
        for (int i = 0; i < panels.length; i++) {
            HashMap defOptions = new HashMap();
            panels[i].populateCRUD(defOptions, defOnCaseId);
            if (!defOptions.isEmpty()) {
                CourtLogCRUDValue cv = new CourtLogCRUDValue();
                cv.setEventType(panels[i].getEventType());
                cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom()); // XI2B134
                defOptions.put("Defendant_Name", PDHConstants.buildDefendantName(dbv));
                cv.setProperty("Direction_By_Defendant_Options", defOptions);
                cruds.add(cv);
            }
        }

        LongAdjournmentModel lam = getLongAdjournModel(defOnCaseId);
        if (lam.getSubEventCode() != null && lam.getSubEventId() > 0) {
            CourtLogCRUDValue cv = lap.getCRUD(lam);
            if (cv != null)
                cruds.add(cv);
        }

        CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[cruds.size()];
        cruds.toArray(crudArray);
        return crudArray;
    }

    /**
     * The passed in DirectionsForDefendantValue is set to the child panels
     * contained.
     * 
     * @param newModel
     */
    public void setModel(DirectionsForDefendantValue[] newModel) {
        model = newModel;
        ip.setModel(newModel);
        bp.setModel(newModel);
        fp.setModel(newModel);
        LongAdjournmentModel[] lams = new LongAdjournmentModel[newModel.length];
        for (int i = 0; i < newModel.length; i++) {
            lams[i] = getLongAdjournModel(newModel[i].getDirectionsForDefendantBasicValue().getDefendantOnCaseId());
        }
        lap.setModel(lams);
    }

    HashMap longAdjournModelMap = new HashMap();

    protected LongAdjournmentModel getLongAdjournModel(Integer defOnCaseId) {
        if (longAdjournModelMap.containsKey(defOnCaseId)) {
            return (LongAdjournmentModel) longAdjournModelMap.get(defOnCaseId);
        } else {
            LongAdjournmentModel lam = createLongAdjournModel();
            lam.setDefendantOnCaseId(defOnCaseId);
            longAdjournModelMap.put(defOnCaseId, lam);
            return lam;
        }
    }

    /**
     * DirectionsForDefendantValue is returned with modifications made to date.
     * 
     * @return DirectionsForCaseValue
     * @throws CSRecoverableException
     */
    public DirectionsForDefendantValue[] getModel() {
        return model;
    }

    private void init() throws CSRecoverableException {
        PropertyChangeListener pcl = new ModifyPropertyListener();
        ip = new IdentificationPanel(model);
        ip.addPropertyChangeListener(XPanel.property_modified, pcl);
        bp = new BailCustodyPanel(model);
        bp.addPropertyChangeListener(XPanel.property_modified, pcl);
        fp = new FormBPanel(model);
        fp.addPropertyChangeListener(XPanel.property_modified, pcl);

        // Need to pass a dummy model in here so the constructor does not fail.
        // This will be replaced as soon as a defendant is selected.
        LongAdjournmentModel dummyLam = createLongAdjournModel();
        dummyLam.setInEditMode(false);
        lap = new LongAdjournDirectionsPanel(new LongAdjournmentModel[] { dummyLam }, logAuditPanel); // this,
        // lam);
        lap.addPropertyChangeListener(XPanel.property_modified, pcl);

        this.setLayout(gridBagMain);
        this.add(ip, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(bp, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(fp, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(lap, new GridBagConstraints(1, 0, 1, 3, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
    }

    public void stepInitialise() {
    }

    protected LongAdjournmentModel createLongAdjournModel() {
        LongAdjournmentModel lam = new LongAdjournmentModel();
        lam.setXac(xac);
        lam.setInEditMode(true);
        lam.setEventType(PDHConstants.DEF_LONG_ADJOURNMENT.toString());
        return lam;
    }

    public void stepActivate() throws CSRecoverableException {
        checkModified = false;
        try {
            super.stepActivate();
        } catch (CSRecoverableException ex) {
            throw ex;
        } finally {
            checkModified = true;
            resetModified();
        }
    }

    /**
     * Called when panel has been modified
     */
    public void modified() {
        if (checkModified) {
            setModified(true);
        }
    }

    /**
     * Reset modification booleans
     */
    public void resetModified() {
        ip.setModified(false);
        fp.setModified(false);
        lap.setModified(false);
        setModified(false);
    }

    /**
     * <p>
     * Title: ModifyPropertyListener
     * </p>
     * <p>
     * Description: Inner class to control setting of the screens modification
     * status
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
    class ModifyPropertyListener implements PropertyChangeListener, Serializable {
        public ModifyPropertyListener() {
        }

        public void propertyChange(java.beans.PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                modified();
            }
        }
    }
}
