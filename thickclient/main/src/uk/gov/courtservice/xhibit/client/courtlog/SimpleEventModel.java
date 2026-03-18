package uk.gov.courtservice.xhibit.client.courtlog;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class SimpleEventModel extends FreeTextModel {
    private static final Logger LOG = Logger.getLogger(SimpleEventModel.class);

    protected boolean okPressed = false;

    // Utility methods
    public void printModel() {
        LOG.debug("SimpleEventModel");
        LOG.debug("--------------------");
        LOG.debug("In Edit Mode?           : " + isInEditMode());
        LOG.debug("DateTime                : " + getDateTime());
        LOG.debug("FreeText                : " + getFreeText());
        LOG.debug("EventId                 : " + getEventId());
        LOG.debug("EventType               : " + getEventType());
        LOG.debug("Schema                  : " + getSchema());
        LOG.debug("PanelText               : " + getPanelText());
        LOG.debug("XAC                     : " + getXac());
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
    }
}
