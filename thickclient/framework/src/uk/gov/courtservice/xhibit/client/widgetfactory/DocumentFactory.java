package uk.gov.courtservice.xhibit.client.widgetfactory;

import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * @author Meeraj
 * @title DocumentFactory This is a factory class for document objects
 */
public class DocumentFactory {
    // -----------------------------------------------------------------------------
    /**
     * Don't instantiate me
     * 
     */
    private DocumentFactory() {
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates a document with specified capabilities
     * 
     * @param Capabilities
     * @return Document
     */
    public static Document newDocument(Capability[] capabilities) {

        // Decorate the documents for the capabilities
        for (int i = 0; capabilities != null && i < capabilities.length - 1; i++) {
            capabilities[i].getDocument().setWrappedDocument(capabilities[i + 1].getDocument());
        }

        // If we have capabilities, return the document for the first capability
        if (capabilities != null && capabilities.length > 0)
            return capabilities[0].getDocument();

        return new PlainDocument();

    }
    // -----------------------------------------------------------------------------
}
