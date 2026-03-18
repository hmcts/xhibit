package uk.gov.courtservice.xhibit.business.services.formatting;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.database.formatting.FormatingDatabaseProcedures;
import uk.gov.courtservice.xhibit.business.vos.formatting.FormattingValue;

/**
 * TODO
 * 
 * @ejb.bean name="FormattingController" description="Formatting Controller
 *           Bean" type="Stateless" view-type="both"
 *           jndi-name="FormattingControllerHome"
 *           local-jndi-name="FormattingControllerLocalHome"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 * @author Bal Bhamra
 * @version $Id: FormattingControllerBean.java,v 1.2 2005/12/01 15:20:33 bzjrnl
 *          Exp $
 */
public class FormattingControllerBean extends CSSessionBean implements SessionBean {
    private static final long serialVersionUID = 1L;

    // set in the ejbCreate() method...
    private FormattingWorkflow formattingWorkFlow;

    private FormattingServices formattingServices;

    private FormatingDatabaseProcedures formattingDatabaseProcedures;

    /**
     * Initialises all of the instance variables for this session bean.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();

        this.formattingWorkFlow = new FormattingWorkflow();
        this.formattingServices = new FormattingServices();
        this.formattingDatabaseProcedures = new FormatingDatabaseProcedures(formattingServices);
    }

    // /////////////////////////////////////////////////////////////////////////
    // DEFINE ALL REMOTE METHODS...
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process. This method must have the same transactional behaviour as
     * processFormattingDocument
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Never"
     */
    public void doTask(String taskName) {
        processFormattingDocument();
    }

    /**
     * Format the xml
     * 
     * @param distributionTypeIn
     * @param mimeTypeIn
     * @param documentTypeIn
     * @param majorVersion
     * @param minorVersion
     * @param language
     * @param country
     * @param xml
     * @return an array containg the formated document data
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public byte[] formatDocument(String distributionTypeIn, String mimeTypeIn, String documentTypeIn,
            Integer majorVersion, Integer minorVersion, String language, String country, String xml, Integer courtId) {

        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

        formattingServices.processDocument(new FormattingValue(distributionTypeIn, mimeTypeIn, documentTypeIn,
                majorVersion, majorVersion, language, country, new StringReader(xml), out, courtId));

        return out.toByteArray();
    }

    /**
     * Only remotely accessible method, this method processes the next available
     * document if one is available. If no document is available, then method
     * will return.
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Never"
     * 
     * @throws RuntimeException
     *             if any <code>RuntimeException</code> is thrown during
     *             processing of the document, after first having the status of
     *             the document set to failed.
     */
    public void processFormattingDocument() {
        final Integer formattingDocumentId = formattingWorkFlow.getNextFormattingDocumentId();

        if (formattingDocumentId != null) {
            try {
                formattingWorkFlow.processFormattingDocument(formattingDocumentId);
                // indicate that formatting was successful...
                formattingWorkFlow.updateFormattingDocumentStatus(formattingDocumentId, true);
            } catch (final RuntimeException e) {
                // indicate that formatting failed...
                formattingWorkFlow.updateFormattingDocumentStatus(formattingDocumentId, false);
                //if it's a merge issue then we need to update the cppformatting row as well
                String errorMessage = e.getCause().getMessage();
                if(errorMessage.contains("Error Merging: ")) {
                	int startIndex = errorMessage.indexOf("Error Merging: ")+15;
                	int endIndex = errorMessage.indexOf(":", startIndex)-1;
                	Integer cppFormattingId = Integer.valueOf(errorMessage.substring(startIndex , endIndex));
                	formattingWorkFlow.updateCppFormatting(cppFormattingId,errorMessage);
                }
                throw e;
            }
        } else {
            // debugging for when id is not null is handled in the
            // processFormattingDocument method...
            if (log.isDebugEnabled()) {
                log.debug("processFormattingDocument() - No documents to process");
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // DEFINE ALL LOCAL METHODS...
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Method to acquire the next available document that is yet to be
     * formatted. This method will return the primary key of the document to be
     * processed, or <i>null</i> if no more documents need to be formatted.
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     * 
     * @return The primary key of the document to format, or <i>null</i> if no
     *         more documents require formatting.
     */
    public Integer getNextFormattingDocumentId() {
        if (log.isDebugEnabled()) {
            log.debug("getNextFormattingDocumentId() - Called");
        }

        // validation of input parameters is performed in the called method...
        final Integer id = formattingDatabaseProcedures.getNextDocumentId();

        if (log.isDebugEnabled()) {
            log.debug("getNextFormattingDocumentId() - Returning id: " + id);
        }

        return id;
    }

    /**
     * Process the document whose primary key value is passed in.
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     * 
     * @param formattingId
     *            Primary key of formatting entity we are processing.
     */
    public void processFormattingDocument(final Integer formattingId) {
        if (log.isDebugEnabled()) {
            log.debug("processFormattingDocument() - formattingId=" + formattingId);
        }

        // validation of input parameters is performed in the called method...
        formattingDatabaseProcedures.processDocuments(formattingId);
    }

    /**
     * Update the status of the formatting entity identified by the passed in
     * formatting id primary key to indicate success or failure of formatting of
     * the document.
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     * 
     * @param formattingId
     *            Primary key of formatting entity we are updating.
     * @param success
     *            <i>true</i> if the document was successfully formatted, or
     *            <i>false</i> if not successfully formatted.
     */
    public void updateFormattingDocumentStatus(final Integer formattingId, final boolean success) {
        if (log.isDebugEnabled()) {
            log.debug("updateFormattingDocumentStatus() - formattingId=" + formattingId);
        }

        // validation of input parameters is performed in the called method...
        formattingDatabaseProcedures.updateStatus(formattingId, success);
    }
    
    /**
     * Update the status of the formatting entity identified by the passed in
     * formatting id primary key to indicate success or failure of formatting of
     * the document.
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     * 
     * @param formattingId
     *            Primary key of formatting entity we are updating.
     * @param success
     *            <i>true</i> if the document was successfully formatted, or
     *            <i>false</i> if not successfully formatted.
     */
    public void updateCppFormatting(final Integer cppFormattingId, final String errorMessage) {
        if (log.isDebugEnabled()) {
            log.debug("updateCppFormatting() - cppFormattingId=" + cppFormattingId);
        }

        // validation of input parameters is performed in the called method...
        formattingDatabaseProcedures.updateCpp(cppFormattingId, errorMessage);
    }
}
