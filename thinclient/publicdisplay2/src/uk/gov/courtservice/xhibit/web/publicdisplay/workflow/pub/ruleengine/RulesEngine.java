package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.exceptions.UnrecognizedEventException;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.exceptions.RulesConfigurationException;

/**
 * <p>
 * Title: Rules Engine
 * </p>
 * <p>
 * Description: The main interface into the Rules for established effected
 * DisplayDocuments
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: RulesEngine.java,v 1.5 2006/06/05 12:32:37 bzjrnl Exp $
 */

public class RulesEngine {
    private static final Logger log = Logger.getLogger(RulesEngine.class);

    private static RulesEngine _instance = null;

    /**
     * Link to the mapping of rules to documents
     */
    private RulesConfiguration ruleConfig;

    private RulesEngine() throws RulesConfigurationException {
        try {
            ruleConfig = RulesConfiguration.newConfiguration();
        } catch (RulesConfigurationException ex) {
            log.fatal("The rules configuration failed to load", ex);
            throw ex;
        }
    }

    /**
     * Return an instance of the rule engine
     * 
     * @return
     */
    synchronized public static RulesEngine getInstance() {
        if (_instance == null) {
            _instance = new RulesEngine();
        }
        return _instance;
    }

    /**
     * Get the display document types that are effected by the public display
     * event.
     * 
     * @param publicDisplayEvent
     * @return DocumentsForEvent containing a collection of DisplayDocumentTypes
     * 
     * @pre publicDisplayEvent != null
     * @post return != null
     * @post return.getDisplayDocumentTypes() != null
     * 
     */
    public DocumentsForEvent getDisplayDocumentTypesForEvent(PublicDisplayEvent publicDisplayEvent)
            throws UnrecognizedEventException {
        DocumentsForEvent docForEvent = new DocumentsForEvent();

        // for event type look up documents
        ConditionalDocument[] conditionalDocuments = ruleConfig.getConditionalDocumentsForEvent(publicDisplayEvent
                .getEventType());

        // for each document check if it is requires refreshing
        for (int i = 0; i < conditionalDocuments.length; i++) {
            // if document passes rules add to return list
            if (conditionalDocuments[i].isDocumentValidForEvent(publicDisplayEvent)) {
                docForEvent.addDisplayDocumentTypes(conditionalDocuments[i].getDisplayDocumentTypes());

            }
        }

        // DEBUG
        // System.out.println("Event: " + publicDisplayEvent);
        // DisplayDocumentType[] dd = docForEvent.getDisplayDocumentTypes();
        // for (int i = 0; i < dd.length; i++) {
        // System.out.println("DOC EVENT(" + i + ")=" + dd[i].getLongName());
        // }
        //
        return docForEvent;
    }
}