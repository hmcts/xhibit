package uk.gov.courtservice.xhibit.business.services.formatting;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * This workflow class is provided for the sole purpose of allowing the session
 * bean to call its own methods within different transactions. All methods have
 * exactly the same signatures as those defined for the methods defined as local
 * in the <code>FormattingControllerBean</code>. The local home is acquired
 * at object creation time, and used to call the methods on the session bean
 * (and thus allowing the containers EJB container to handle the transactions).
 * </p>
 * 
 * <p>
 * Declared as package private so as to reduce the visibility of the class, and
 * to help prevent external classes using the methods that should only be called
 * from the session bean.
 * </p>
 * 
 * @author tz0d5m
 * @version $Id: FormattingWorkflow.java,v 1.3 2006/06/05 12:29:10 bzjrnl Exp $
 * 
 * @see uk.gov.courtservice.xhibit.business.services.formatting.FormattingControllerBean
 */
final class FormattingWorkflow {
    /** The <code>FormattingControllerBean</code> local home interface */
    private static final FormattingControllerLocal formattingController = (FormattingControllerLocal) CSServices
            .getEJBServices().createLocalSession(FormattingControllerLocalHome.class);

    /**
     * @see uk.gov.courtservice.xhibit.business.services.formatting.FormattingControllerBean
     *      #processFormattingDocument(java.lang.Integer)
     */
    protected void processFormattingDocument(final Integer formattingDocumentId) {
        formattingController.processFormattingDocument(formattingDocumentId);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.formatting.FormattingControllerBean
     *      #updateFormattingDocumentStatus(java.lang.Integer, boolean)
     */
    protected void updateFormattingDocumentStatus(final Integer formattingDocumentId, final boolean success) {
        formattingController.updateFormattingDocumentStatus(formattingDocumentId, success);
    }
    
    protected void updateCppFormatting(final Integer cppFormattingId, final String errorMessage) {
        formattingController.updateCppFormatting(cppFormattingId, errorMessage);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.formatting.FormattingControllerBean
     *      #getNextFormattingDocumentId()
     */
    protected Integer getNextFormattingDocumentId() {
        return formattingController.getNextFormattingDocumentId();
    }
}
