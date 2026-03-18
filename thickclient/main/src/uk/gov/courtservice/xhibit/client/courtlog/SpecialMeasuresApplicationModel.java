package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Iterator;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class SpecialMeasuresApplicationModel extends FreeTextModel {

    public static final String ORDERS_FOR_SCREENS_GRANTED = "OrdersForScreensGranted";
    public static final String EVIDENCE_BY_LIVE_LINK = "EvidenceByLiveLink";
    public static final String EVIDENCE_TO_BE_GIVEN_IN_PRIVATE = "EvidenceToBeGivenInPrivate";
    public static final String REMOVAL_OF_WIGS_AND_GOWNS = "RemovalOfWigsAndGowns";
    public static final String VIDEO_RECORDED_EVIDENCE_IN_CHIEF = "VideoRecordedEvidenceInChief";
    public static final String AIDS_TO_COMMUNICATION = "AidsToCommunication";

    private Set<String> specialMeasures = new TreeSet<String>();
    
    public SpecialMeasuresApplicationModel() {
        // empty
    }
    
    public boolean getSpecialMeasuresApplication(String specialMeasure) {
        return specialMeasures.contains(specialMeasure);
    }
    
    public void setSpecialMeasuresApplication(String specialMeasure, boolean selected) {
        if (selected) {
            specialMeasures.add(specialMeasure);
        } else {
            specialMeasures.remove(specialMeasure);
        }
    }
 
    // Utility methods
    public void printModel() {
        XHIBITConstant.info("SpecialMeasuresModel");
        XHIBITConstant.info("--------------------");
        XHIBITConstant.info("In Edit Mode?           : " + isInEditMode());
        XHIBITConstant.info("DateTime                : " + getDateTime());
        XHIBITConstant.info("FreeText                : " + getFreeText());
        XHIBITConstant.info("EventId                 : " + getEventId());
        XHIBITConstant.info("EventType               : " + getEventType());
        XHIBITConstant.info("Schema                  : " + getSchema());
        XHIBITConstant.info("PanelText               : " + getPanelText());
        XHIBITConstant.info("XAC                     : " + getXac());
        XHIBITConstant.info("Defendant Name          : " + getDefendantName());
        XHIBITConstant.info("------------------------- ");
    }

    public Object clone() throws CloneNotSupportedException {
        SpecialMeasuresApplicationModel model = new SpecialMeasuresApplicationModel();
        model.setDateTime(this.dateTime == null ? null : (Calendar)this.dateTime.clone());
        model.setInEditMode(this.inEditMode);
        model.setFreeText(this.freeText);
        model.setEventId(this.eventId);
        model.setEventType(this.eventType);
        model.setSchema(this.schema);
        model.setPanelText(this.panelText);
        model.setXac(this.xac);
        Iterator<String> itr = specialMeasures.iterator();
        while (itr.hasNext()) {
            model.setSpecialMeasuresApplication(itr.next(), true);
        }
        return model;
    }
    
    public void clearmodel() {
        setDateTime(null);
        setInEditMode(false);
        setFreeText(null);
        setEventId(null);
        setEventType(null);
        setSchema(null);
        setPanelText(null);
        setXac(null);
        specialMeasures = new TreeSet<String>(); 
    }
}