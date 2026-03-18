package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.rules.Rule;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.rules.RuleFlyweightPool;

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
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class RulesConfigurationHandler extends DefaultHandler {
    // Statics for the XML tags in the Rules Configuration
    private static final String RULE_REF = "RuleRef";

    private static final String EVENT = "Event";

    private static final String CONDITIONAL_DOCUMENT = "ConditionalDocument";

    private static final String RULE = "Rule";

    // Statics for the XML attributes in the Rules Configuration
    private static final String EVENT_TYPE = "type";

    private static final String DISPLAY_DOCUMENT_ID = "id";

    private static final String RULE_ID = "id";

    private EventType currentEventType;

    private ArrayList rulesList = new ArrayList();

    private DisplayDocumentType[] currentDocuments;

    private ArrayList conditionalDocumentList = new ArrayList();

    private EventMappings eventMap = new EventMappings();

    public RulesConfigurationHandler() {
    }

    /**
     * Processed when a start tag is reached. Only handles RuleRef, Event,
     * ConditionalDocument & Rule
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws org.xml.sax.SAXException
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws org.xml.sax.SAXException {
        if (qName.equals(RULE_REF)) {
            // A Rule Ref is a declaration of a reusable Rule.
            // The fly weight pool will instantiate it and hold and instance
            RuleFlyweightPool.getInstance().loadRule(attributes);
        } else if (qName.equals(EVENT)) {
            // For a Event start tag, store the Event type and clear out the
            // conditional document list
            currentEventType = EventType.getEventType(attributes.getValue(EVENT_TYPE));
            conditionalDocumentList.clear();
        } else if (qName.equals(CONDITIONAL_DOCUMENT)) {
            // For a conditional document start tag, get the
            // DisplayDocumentType
            // and clear out the rules list
            currentDocuments = DisplayDocumentType.getDisplayDocumentTypes(attributes.getValue(DISPLAY_DOCUMENT_ID));

            rulesList.clear();
        } else if (qName.equals(RULE)) {
            // For a rule, get the cached rule from the pool and add to the
            // rule list
            Rule rule = RuleFlyweightPool.getInstance().getRule(attributes.getValue(RULE_ID));
            rulesList.add(rule);
        }
    }

    /**
     * Processed when a end tag is reached. Only handles Event &
     * ConditionalDocument
     * 
     * @param uri
     * @param localName
     * @param qName
     * @throws org.xml.sax.SAXException
     */
    public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
        if (qName.equals(EVENT)) {
            // For an Event end tag add the conditional documents to the map
            // and reset the current event holder
            eventMap.putConditionalDocumentsForEvent(currentEventType, (ConditionalDocument[]) conditionalDocumentList
                    .toArray(new ConditionalDocument[conditionalDocumentList.size()]));
            currentEventType = null;
        } else if (qName.equals(CONDITIONAL_DOCUMENT)) {
            // For a ConditionalDocument end tag, remove a new conditional
            // document
            // with the associated rule set.
            if (currentEventType == null) {
                throw new SAXException("Invalid document structure. Document found before event declared");
            } else {
                ConditionalDocument cond = new ConditionalDocument(currentDocuments, (Rule[]) rulesList
                        .toArray(new Rule[rulesList.size()]), currentEventType);
                // Add to the conditional document list for the current event
                conditionalDocumentList.add(cond);
            }
        }
    }

    /**
     * Method implemented for SAXError Handler. Throws the exception back to the
     * calling method
     * 
     * @param ex
     *            generated exception
     * @throws org.xml.sax.SAXException
     */
    public void error(SAXParseException ex) throws org.xml.sax.SAXException {
        throw ex;

    }

    /**
     * Method implemented for SAXError Handler. Throws the exception back to the
     * calling method
     * 
     * @param ex
     *            generated exception
     * @throws org.xml.sax.SAXException
     */
    public void fatalError(SAXParseException ex) throws org.xml.sax.SAXException {
        throw ex;
    }

    public EventMappings getEventMappings() {
        return eventMap;
    }
}