package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsParentPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateKeyListener;
import uk.gov.courtservice.xhibit.client.courtlog.util.CourtLogAuditPanel;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: CaseLevelEventsPanel
 * </p>
 * <p>
 * Description: Panel allows the entry of information which form preliminary
 * hearing court log events for the case.
 * <p>
 * Copyright:Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version $Revision: 1.5 $
 */

public class CaseLevelEventsPanel extends DirectionsParentPanel {
    private static final Logger log = Logger.getLogger(CaseLevelEventsPanel.class);

    // Only to be set in the constructor...
    private final CaseLevelEventsModel model;

    private CourtLogAuditPanel clap;

    private IndictmentByPanel ibp;

    private CaseCalledOnPanel ccop;

    protected boolean checkModified = false;

    /**
     * Constructor uses parameters passed in to set general case level events
     * data
     * 
     * @param model
     * @throws CSRecoverableException
     */
    public CaseLevelEventsPanel(CaseLevelEventsModel model) throws CSRecoverableException {
        this(model, null);
    }

    /**
     * Constructor uses parameters passed in to set general case level events
     * data
     * 
     * @param model
     * @throws CSRecoverableException
     */
    public CaseLevelEventsPanel(CaseLevelEventsModel model, CourtLogAuditPanel clap) throws CSRecoverableException {
        this.model = model;
        this.clap = clap;
        printModel_CaseLevelEventsValue();

        init();
        stepActivate();
    }

    /**
     * CaseLevelEventsModel is returned after setting CourtLogCRUDValue
     * 
     * @return CaseLevelEventsModel
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public CaseLevelEventsModel getModel() throws CSValidationException, CSRecoverableException {
        model.setCourtLogCRUDValues(getCRUDValue());

        log.debug("CaseLevelEventsPanel.getModel() after model.setCourtLogCRUDValue(getCRUDValue()): ");
        printModel_CaseLevelEventsValue();

        return model;
    }

    /**
     * Returns the date and time from the LogAuditPanel
     * 
     * @return Calendar
     * @throws CSValidationException
     */
    public Calendar getLogDateTime() throws CSValidationException {
        return ((clap != null) ? clap.getDateTime() : null);
    }

    /**
     * @return LogAuditPanel
     * @throws CSValidationException
     */
    public CourtLogAuditPanel getCourtLogAuditPanel() {
        return clap;
    }

    /**
     * Creates CourtLogCRUDValues from child panels and returns array.
     * 
     * @return CourtLogCRUDValue[]
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public CourtLogCRUDValue[] getCRUDValue() throws CSValidationException, CSRecoverableException {
        Collection cruds = new ArrayList();

        // Case Called On
        if (ccop.getCaseCalledOnCbx().isSelected()) {
            CourtLogCRUDValue cv = createStandardCRUDValue();
            cv.setEventType(ccop.getEventType());
            Collection defOnCaseBVs = model.getXac().getApplicationCaseModel().getScheduledHearingValue()
                    .getDefendantOnCaseBasicValues();
            Iterator defOnCaseBVIt = defOnCaseBVs.iterator();
            ArrayList docIDs = new ArrayList();
            while (defOnCaseBVIt.hasNext()) {
                DefendantOnCaseBasicValue docBV = (DefendantOnCaseBasicValue) defOnCaseBVIt.next();
                HashMap h = new HashMap();
                h.put("doc_id", docBV.getId());
                docIDs.add(h);
            }

            if (!docIDs.isEmpty()) {
                HashMap h = new HashMap();
                h.put("Def_On_Case_Id", docIDs);
                cv.getPropertyMap().put("Listed_Def_On_Case_Ids", h);
                cruds.add(cv);
            }
        }

        // Indictment By
        if (ibp.getModified() && ibp.getIndictmentBy().getDate() != null) {
            CourtLogCRUDValue cv = createStandardCRUDValue();
            cv.setEventType(ibp.getEventType());
            cv.setProperty("E" + ibp.getEventType() + "_Indictment_By_Date", XDateFormat.format(ibp.getIndictmentBy()
                    .getDate(), XDateFormat.DATEFORMAT));
            cruds.add(cv);
        }

        // Freetext
        if (clap.getFreeTextString().trim().length() > 0) {
            CourtLogCRUDValue cv = createStandardCRUDValue();
            cv.setEntryFreeText(clap.getFreeTextString());
            cv.setEventType(PDHConstants.FREETEXT);
            cruds.add(cv);
        }

        CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[cruds.size()];
        cruds.toArray(crudArray);
        return crudArray;
    }

    /**
     * Populates a CourtLogCRUDValue with generic information
     * 
     * @return CourtLogCRUDValue
     * @throws CSValidationException
     */
    protected CourtLogCRUDValue createStandardCRUDValue() throws CSValidationException {
        CourtLogCRUDValue cv = new CourtLogCRUDValue();
        cv.setCaseId(model.getCaseLevelEventsValue().getCaseId());
        cv.setScheduledHearingId(clap.getScheduledHearingId());
        cv.setEntryDate(clap.getDateTime().getTime());
        cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());

        return cv;
    }

    /**
     * Drops the case level events panels onto the screen
     * 
     * @throws CSRecoverableException
     */
    private void init() throws CSRecoverableException {
        PropertyChangeListener pcl = new ModifyPropertyListener();
        ibp = new IndictmentByPanel(model);
        ibp.addPropertyChangeListener(XPanel.property_modified, pcl);
        ccop = new CaseCalledOnPanel(model);
        ccop.addPropertyChangeListener(XPanel.property_modified, pcl);

        List values = CourtLogAuditPanel.getValues(model.getCaseLevelEventsValue().getCaseId());
        clap = new CourtLogAuditPanel(values, model.getCaseLevelEventsValue().getScheduledHearingId());
        clap.getDatePicker().getDropDownDate().addItemListener(new ItemChangeListener(this));
        clap.getFreeText().getFreeTextArea().addKeyListener(new UpdateStateKeyListener(this));
        clap.getTimePanel().addKeyListener(new UpdateStateKeyListener(this));

        this.setLayout(new GridBagLayout());
        this.add(ccop, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));
        this.add(ibp, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));
    }

    /**
     * Saves details from the panels in the model
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        // Call the parent panel's stepDeactivate method which, in turn, calls
        // the stepDeactivate method on the panels within this container
        super.stepDeactivate();

        // These attributes are saved explicitly as the "log audit panel" is a
        // non-XDirectionsPanel panel
        model.getCaseLevelEventsValue().setFreeText(clap.getFreeTextString());
        model.getCaseLevelEventsValue().setDateTime(clap.getDateTime());

        log.debug("CaseLevelEventsPanel.stepDeactivate()");
        printModel_CaseLevelEventsValue();
    }

    /**
     * Life-cycle method - called when the parent screen is made visible - to
     * move details from the model to the screen
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        checkModified = false;
        try {
            super.stepActivate();
            if (model.getCaseLevelEventsValue().getFreeText() != null) {
                clap.setFreeTextString(model.getCaseLevelEventsValue().getFreeText());
            }
            Calendar modelCal = model.getCaseLevelEventsValue().getDateTime();
            if (modelCal != null) {
                clap.setDateDefault(modelCal);
                clap.setTimeDefault(modelCal);
            }
            ibp.setModel(model);
            ccop.setModel(model);
            ibp.moveModelToScreen();
            ccop.moveModelToScreen();
        } finally {
            checkModified = true;
            resetModified();
        }

        log.debug("CaseLevelEventsPanel.stepActivate()");
        printModel_CaseLevelEventsValue();
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
        ibp.setModified(false);
        ccop.setModified(false);
        setModified(false);
    }

    /**
     * For test purposes only.
     */
    protected void printModel_CaseLevelEventsValue() {
        if (log.isDebugEnabled()) {
            CaseLevelEventsValue clev = model.getCaseLevelEventsValue();
            String modelString = "= handling model.getCaseLevelEventsValue()" + "\n\t=.getCaseId()             = "
                    + clev.getCaseId() + "\n\t=.getScheduledHearingId() = " + clev.getScheduledHearingId()
                    + "\n\t=.getDateTime()           = " + clev.getDateTime() + "\n\t=.getFreeText()           = "
                    + clev.getFreeText() + "\n\t=.getIndictmentBy()       = " + clev.getIndictmentBy()
                    + "\n\t=.getCaseCalledOn()       = " + clev.getCaseCalledOn();
            log.debug(modelString);
        }
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
    private class ModifyPropertyListener implements PropertyChangeListener, Serializable {
        public void propertyChange(PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                modified();
            }
        }
    }
}
