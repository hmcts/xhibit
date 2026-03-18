package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.DirectionsPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.PadPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.TimeEstimatePanel;
import uk.gov.courtservice.xhibit.client.courtlog.util.CourtLogAuditPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.text.UTF8LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Directions for case panel
 * </p>
 * <p>
 * Description: Panel allows the entry of information which form directions for
 * the case.
 * <p>
 * Copyright:Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.28 $
 */
public class DirectionsForCase extends DirectionsParentPanel {
    private static final Logger log = Logger.getLogger(DirectionsForCase.class);

    // only to be set in the constructor...
    private final DirectionsForCaseValue model;

    /**
     * Constant used to represent the maximum number of characters that can be
     * entered into the freetext text field
     */
    private static final int FREETEXT_TEXT_LIMIT = 255;

    private PadPanel pp;

    private TimeEstimatePanel tp;

    private DirectionsPanel dirp;

    private CourtLogAuditPanel lap;

    private boolean checkModified = false;

    private Integer _scheduledHearingId;

    /**
     * Constructor uses parameters passed in to set defendant and caseType
     * information
     * 
     * @param xac
     * @param model
     * @throws CSRecoverableException
     */
    public DirectionsForCase(DirectionsForCaseValue model, Integer scheduledHearingId) throws CSRecoverableException {
        this.model = model;
        _scheduledHearingId = scheduledHearingId;

        printModel_DirectionsForCaseBasicValue();

        init();
        stepActivate();
    }

    /**
     * DirectionsForCaseValue is returned after setting CourtLogCRUDValue
     * 
     * @return DirectionsForCaseValue
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public DirectionsForCaseValue getModel() throws CSValidationException, CSRecoverableException {
        model.setCourtLogCRUDValues(getCRUDValue());

        log.debug("DirectionsForCase.getModel() after model.setCourtLogCRUDValue(getCRUDValue()): ");
        printModel_DirectionsForCaseBasicValue();

        return model;
    }

    /**
     * Returns the date and time from the LogAuditPanel
     * 
     * @return Calendar
     * @throws CSValidationException
     */
    public Calendar getLogDateTime() throws CSValidationException {
        return ((lap != null) ? lap.getDateTime() : null);
    }

    /**
     * @return LogAuditPanel
     * @throws CSValidationException
     */
    public CourtLogAuditPanel getCourtLogAuditPanel() {
        return lap;
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

        XDirectionsPanel[] panels = new XDirectionsPanel[] { pp, tp, dirp };
        for (int i = 0; i < panels.length; i++) {
            HashMap caseOptions = new HashMap();
            panels[i].populateCRUD(caseOptions, null);
            if (!caseOptions.isEmpty()) {
                CourtLogCRUDValue cv = createStandardCRUDValue();

                cv.setEventType(panels[i].getEventType());
                cv.setProperty("Directions_By_Case_Options", caseOptions);
                cruds.add(cv);
            }
        }

        // Free text
        if (lap.getFreeTextString().trim().length() > 0) {
            CourtLogCRUDValue cv = createStandardCRUDValue();

            cv.setEntryFreeText(lap.getFreeTextString());
            cv.setEventType(PDHConstants.FREETEXT);
            cruds.add(cv);
        }

        CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[cruds.size()];
        cruds.toArray(crudArray);
        return crudArray;
    }

    private CourtLogCRUDValue createStandardCRUDValue() throws CSValidationException {
        CourtLogCRUDValue cv = new CourtLogCRUDValue();
        cv.setCaseId(model.getDirectionsForCaseBasicValue().getCaseId());
        cv.setScheduledHearingId(lap.getScheduledHearingId());
        cv.setEntryDate(lap.getDateTime().getTime());
        cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());

        return cv;
    }

    private void init() throws CSRecoverableException {
        PropertyChangeListener pcl = new ModifyPropertyListener();
        pp = new PadPanel(model);
        pp.addPropertyChangeListener(XPanel.property_modified, pcl);
        tp = new TimeEstimatePanel(model);
        tp.addPropertyChangeListener(XPanel.property_modified, pcl);
        dirp = new DirectionsPanel(model);
        dirp.addPropertyChangeListener(XPanel.property_modified, pcl);

        List values = CourtLogAuditPanel.getValues(model.getDirectionsForCaseBasicValue().getCaseId());
        lap = new CourtLogAuditPanel(values, _scheduledHearingId);

        lap.getDatePicker().getDropDownDate().addItemListener(new ItemChangeListener(this));
        lap.getFreeText().getFreeTextArea().addKeyListener(new UpdateStateKeyListener(this));
        lap.getTimePanel().addKeyListener(new UpdateStateKeyListener(this));
        Dimension defaultSize = new Dimension(100, XHIBITConstant.getLineHeight() * 6);
        lap.setPreferredSize(defaultSize);
        lap.setMinimumSize(defaultSize);

        this.setLayout(new GridBagLayout());
        this.add(pp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));
        this.add(tp, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));
        this.add(dirp, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));
        this.add(lap, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0));
    }

    public void stepDeactivate() throws CSRecoverableException {
        super.stepDeactivate();
        // Save the freetext after truncating it to the length of FREETEXT_TEXT_LIMIT utf8 characters
        model.getDirectionsForCaseBasicValue().setFreetext(truncate(lap.getFreeTextString(), FREETEXT_TEXT_LIMIT ));
        model.getDirectionsForCaseBasicValue().setDateTime(lap.getDateTime().getTime());

        log.debug("DirectionsForCase.stepDeactivate() after setFreeText() and setDateTime() - and nothing else.");
        printModel_DirectionsForCaseBasicValue();
    }

    public void stepActivate() throws CSRecoverableException {
        checkModified = false;
        try {
            super.stepActivate();
            if (model.getDirectionsForCaseBasicValue().getFreetext() != null) {
                lap.setFreeTextString(model.getDirectionsForCaseBasicValue().getFreetext());
            }
            Date modelCal = model.getDirectionsForCaseBasicValue().getDateTime();
            if (modelCal != null) {
                lap.setDateDefault(modelCal);
                lap.setTimeDefault(modelCal);
            }
            tp.setModel(model);
            tp.moveModelToScreen();
            pp.setModel(model);
            pp.moveModelToScreen();
        } finally {
            checkModified = true;
            resetModified();
        }

        log.debug("DirectionsForCase.stepActivate()");
        printModel_DirectionsForCaseBasicValue();
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
        pp.setModified(false);
        tp.setModified(false);
        dirp.setModified(false);
        setModified(false);
    }

    /**
     * For test purposes only.
     */
    private void printModel_DirectionsForCaseBasicValue() {
        if (log.isDebugEnabled()) {
            XhbDirectionsForCaseBasicValue dfcbv = model.getDirectionsForCaseBasicValue();
            String modelString = "= handling model.getDirectionsForCaseBasicValue()"
                    + "\n\t=.getCaseId()             = "
                    + dfcbv.getCaseId()
                    // + "\n\t=.getScheduledHearingId() = " +
                    // dfcbv.getScheduledHearingId()
                    + "\n\t=.getDateTime()           = " + dfcbv.getDateTime() + "\n\t=.getDirectionsText()     = "
                    + dfcbv.getDirectionsText() + "\n\t=.getFreeText()           = " + dfcbv.getFreetext()
                    + "\n\t=.getHasPandDForm()       = " + dfcbv.getHasPanddForm() + "\n\t=.getTrialTimeEstimate()  = "
                    + dfcbv.getTrialTimeEstimate() + "\n\t=.getTrialTimeUnit()      = " + dfcbv.getTrialTimeUnit()
                    + "\n\tAttendees:";

            log.debug(modelString);
        }
    }

    private class ModifyPropertyListener implements PropertyChangeListener, Serializable {
        public void propertyChange(PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                modified();
            }
        }
    }
    
    /**
     * Truncates the "param" string to a maximum of "maxLength" utf8 characters
     * @param param
     * @param maxLength
     * @return the truncated string
     */
    private String truncate( String param, int maxLength ) {
        log.info( "truncate() called to a maximum of " + maxLength );
        UTF8LimitedTextValidatingDocumentDecorator doc = new UTF8LimitedTextValidatingDocumentDecorator( maxLength );
        
        String returnString = param;
        while( !doc.validate( returnString ) ) {
            returnString = returnString.substring( 0, returnString.length() - 1 );
        }
        
        return returnString; 
    }
}
