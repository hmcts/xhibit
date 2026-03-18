package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;

/**
 * <p>
 * Title: DocumentsForEvent
 * </p>
 * <p>
 * Description: Object used to return the effected documents from the rule
 * engine
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DocumentsForEvent.java,v 1.4 2006/06/05 12:32:37 bzjrnl Exp $
 */

public class DocumentsForEvent {
    // Internal array to store the Documents effected by the event
    private ArrayList documents = new ArrayList();

    public DocumentsForEvent() {
    }

    /**
     * Returns the DisplayDocumentTypes that require re-rendering for the event
     * created. Note: These need to be mapped to specific renders for specific
     * courts
     * 
     * @return
     */
    public DisplayDocumentType[] getDisplayDocumentTypes() {
        return (DisplayDocumentType[]) documents.toArray(new DisplayDocumentType[documents.size()]);
    }

    /**
     * Add an effected Display document type to the return set.
     * 
     * @param displayDocumentType
     */
    public void addDisplayDocumentType(DisplayDocumentType displayDocumentType) {
        documents.add(displayDocumentType);
    }

    /**
     * Add effected Display document types to the return set.
     * 
     * @param displayDocumentTypes
     */
    public void addDisplayDocumentTypes(DisplayDocumentType[] displayDocumentTypes) {
        for (int i = 0; i < displayDocumentTypes.length; i++) {
            documents.add(displayDocumentTypes[i]);
        }
    }
}