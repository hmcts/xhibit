package uk.gov.courtservice.xhibit.business.database.formatting;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractDatabaseCall;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.services.formatting.FormattingServices;
import uk.gov.courtservice.xhibit.business.vos.formatting.FormattingValue;

/**
 * A utility class used to extract all of the database actions performed by the
 * formatting sub-project. This class is thread-safe as it holds no state.
 * 
 * @author tz0d5m
 * @version $Id: FormatingDatabaseProcedures.java,v 1.1 2005/02/23 13:51:27
 *          bzjrnl Exp $
 */
public class FormatingDatabaseProcedures extends AbstractDatabaseCall {
    private static final String GET_NEXT_DOCUMENT_ID_FUNCTION = "{ ? = call xhb_formatting_pkg.get_next_document_id() }";

    private static final String GET_DOCUMENT_DETAILS_FUNCTION = "{ ? = call xhb_formatting_pkg.get_document_details(?) }";

    private static final String UPDATE_DOCUMENT_STATUS_FUNCTION = "{ call xhb_formatting_pkg.update_document_status(?,?) }";
    
    private static final String UPDATE_DOCUMENT_STATUS_CPP = "{ call xhb_formatting_pkg.update_cpp_formatting(?,?) }";
    
    private static final String GET_LATEST_XHIBIT_CLOB_ID = "{?= call xhb_formatting_pkg.get_latest_xhibit_clob_Id(?,?,?,?) }";
    
    private static final String	GET_DOC_RECIPIENT_NAME = "{?= call xhb_formatting_pkg.get_doc_recipient_name(?)}";
    
    private final FormattingServices formattingServices;

    /**
     * Construct a new database proceduers for the given service
     * 
     * @param formattingServices
     */
    public FormatingDatabaseProcedures(FormattingServices formattingServices) {
        if (formattingServices == null) {
            throw new IllegalArgumentException("formattingServices: null");
        }
        this.formattingServices = formattingServices;
    }

    /**
     * Method to acquire the next available document that is yet to be
     * formatted. This method will return the primary key of the document to be
     * processed, or <i>null</i> if no more documents need to be formatted.
     * 
     * @return The primary key of the document to format, or <i>null</i> if no
     *         more documents require formatting.
     */
    public Integer getNextDocumentId() {
        if (log.isDebugEnabled()) {
            log.debug("getNextDocumentId() - called");
        }

        // no input parameters required, so call function directly...
        final StoredFunction sf = createStoredFunction(GET_NEXT_DOCUMENT_ID_FUNCTION);
        final Integer id = (Integer) sf.executeFunction(new Object[0], Types.INTEGER);

        if (log.isDebugEnabled()) {
            log.debug("getNextDocumentId() - Returning id=" + id);
        }

        return id;
    }

    /**
     * Process the document whose primary key value is passed in.
     * 
     * @param formattingId
     *            The primary key of the document to format.
     * 
     * @throws IllegalArgumentException
     *             if the passed in id is <i>null</i>.
     */
    public void processDocuments(final Integer formattingId) {
        if (log.isDebugEnabled()) {
            log.debug("processDocuments() - formattingId=" + formattingId);
        }

        validateParameterNotNull("formattingId", formattingId);

        // now that we know the id is valid, call the stored proc...
        final StoredProcedure sp = createStoredProcedure(GET_DOCUMENT_DETAILS_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(new FormattingDocumentRowProcessor(formattingServices));
        sp.execute(new Object[] { formattingId });
    }

    /**
     * Update the status of the formatting entity identified by the passed in
     * formatting id primary key to indicate success or failure of formatting of
     * the document.
     * 
     * @param formattingId
     *            Primary key of formatting entity we are updating.
     * @param success
     *            <i>true</i> if the document was successfully formatted, or
     *            <i>false</i> if not successfully formatted.
     * 
     * @throws IllegalArgumentException
     *             if the passed in id is <i>null</i>.
     */
    public void updateStatus(final Integer formattingId, final boolean success) {
        if (log.isDebugEnabled()) {
            log.debug("updateStatus() - formattingId=" + formattingId + "; success=" + success);
        }

        validateParameterNotNull("formattingId", formattingId);

        // now that we know the id is valid, call the stored proc...
        final StoredFunction sf = createStoredFunction(UPDATE_DOCUMENT_STATUS_FUNCTION);
        sf.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
        sf.executeFunction(new Object[] { formattingId, getBooleanAsInteger(success) });
    }
    
    public void updateCpp(final Integer cppFormattingId, final String errorMessage) {
    	
        validateParameterNotNull("cppFormattingId", cppFormattingId);

    	if (log.isDebugEnabled()) {
            log.debug("updateCpp() - cppFormattingId=" + cppFormattingId);
        }
    	
    	 final StoredFunction sf = createStoredFunction(UPDATE_DOCUMENT_STATUS_CPP);
         sf.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
         sf.executeFunction(new Object[] { cppFormattingId, errorMessage });
    }
    
    public Long getLatestXhibitClobId(final Integer courtId, final String documentType, final String language, final String courtSiteName) {
         
         // no input parameters required, so call function directly...
         final StoredFunction sf = createStoredFunction(GET_LATEST_XHIBIT_CLOB_ID);
         sf.registerInTypes(new int [] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
         Long id = (Long)sf.executeFunction(new Object[]{courtId, documentType, language, courtSiteName}, Types.BIGINT);

         if (log.isDebugEnabled()) {
             log.debug("Latest xhibit clob id () - Returning id=" + id);
         }
         return id;
    }
    
    public String getDocRecipientName(final String courtSiteName) {
        
        // no input parameters required, so call function directly...
        final StoredFunction sf = createStoredFunction(GET_DOC_RECIPIENT_NAME);
        sf.registerInTypes(new int [] {Types.VARCHAR});
        String docRecipient = (String)sf.executeFunction(new Object[]{ courtSiteName}, Types.VARCHAR);

        if (log.isDebugEnabled()) {
            log.debug("Latest xhibit clob id () - Returning recipient=" + docRecipient);
        }
        return docRecipient;
   }
    
    /**
     * Retrieve the newly created formatting row.  This can happen if 
     * the xml passed in from cpp has a courtsite that resides in a different xml
     * file to the first one retrieved .
     * 
     * @param formattingId
     *            The primary key of the document .
     * 
     * @throws IllegalArgumentException
     *             if the passed in id is <i>null</i>.
     */
    public FormattingValue getNewFormattingRow(final Integer formattingId) {
        if (log.isDebugEnabled()) {
            log.debug("processDocuments() - formattingId=" + formattingId);
        }

        validateParameterNotNull("formattingId", formattingId);

        // now that we know the id is valid, call the stored proc...
        final StoredProcedure sp = createStoredProcedure(GET_DOCUMENT_DETAILS_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        FormattingRowProcessor rp = new FormattingRowProcessor();
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { formattingId });
        return rp.getResults();
    }
    
    
}
