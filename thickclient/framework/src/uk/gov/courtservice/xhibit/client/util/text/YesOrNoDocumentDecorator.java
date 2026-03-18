package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.text.Document;
import uk.gov.courtservice.xhibit.client.util.text.ValidatingDocumentDecorator;

public class YesOrNoDocumentDecorator extends ValidatingDocumentDecorator {
    /**
     * Construct a YesOrNoOnly which will delegate to an implementation of the
     * Document interface.
     */
    public YesOrNoDocumentDecorator() {
        super();
    }

    /**
     * Construct a YesOrNoDocumentDecorator which delegates to the specified
     * Document.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     */
    public YesOrNoDocumentDecorator(Document wrappedDocument) {
        super(wrappedDocument);
    }

    /**
     * YesOrNoDocumentDecorator Implementation returns true if the candidate
     * text is Y or N.
     * 
     * @param candidate
     *            the text (proposed result of the Document) to validate.
     * @return true if the candidate text is valid positive long number.
     */
    public boolean validate(String candidate) {
        int len = candidate.length();
        if (len == 0) {
            return true;
        } else if (len == 1 && candidate.equals("Y")) {
            return true;
        } else if (len == 1 && candidate.equals("y")) {
            return true;
        } else if (len == 1 && candidate.equals("N")) {
            return true;
        } else if (len == 1 && candidate.equals("n")) {
            return true;
        } else {
            return false;
        }
    }
}