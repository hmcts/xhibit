package uk.gov.courtservice.xhibit.business.database.listdistribution;

import java.sql.Types;

import org.exolab.castor.xml.ClassDescriptorResolver;

import uk.gov.courtservice.framework.jdbc.core.AbstractDatabaseCall;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.database.listdistribution.processor.BlobDataRowProcessor;
import uk.gov.courtservice.xhibit.business.database.listdistribution.processor.DocumentControlComplexValuesRowProcessor;
import uk.gov.courtservice.xhibit.business.database.listdistribution.processor.ListSummaryRowProcessor;
import uk.gov.courtservice.xhibit.business.database.listdistribution.processor.StringClobDataRowProcessor;
import uk.gov.courtservice.xhibit.business.database.listdistribution.processor.WllControlComplexValueRowProcessor;
import uk.gov.courtservice.xhibit.business.database.listdistribution.processor.WllControlComplexValuesRowProcessor;
import uk.gov.courtservice.xhibit.business.database.listdistribution.processor.WllRecipientComplexValuesRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.DocumentControlComplexValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.ListSummary;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;

/**
 * A utility class used to extract all of the database actions performed by the
 * list distribution 2 sub-project. This class is thread-safe as it holds no
 * state. Not static for testing see getDataSource for more info.
 * 
 * @author tz0d5m
 * @version $Id: ListDistribution2Database.java,v 1.11 2005/12/01 13:31:10
 *          zzzz48 Exp $
 */
public class ListDistribution2Database extends AbstractDatabaseCall {
    private static final String GET_NEXT_CONTROL_ID_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_next_control_id(?) }";

    private static final String UPDATE_CONTROL_STATUS_FUNCTION = "{ call xhb_list_distribution_pkg.update_control_status(?,?) }";

    private static final String GET_XML_DOCUMENT_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_xml_document(?) }";

    private static final String GET_WLL_CONTROL_BY_PK_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_wll_control_by_pk(?) }";

    private static final String GET_WLL_CONTROL_BY_COURT_ID_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_wll_control_by_court_id(?) }";

    private static final String GET_DOCUMENT_CONTROL_BY_COURT_ID_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_doc_control_by_court_id(?) }";

    private static final String GET_LETTER_XML_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_letter_xml(?,?,?,?) }";

    private static final String GET_SUBSCRIBED_WLL_RECIPIENTS_BY_COURT_ID_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_sub_wll_rec_by_court_id(?) }";

    private static final String GET_UNSUBSCRIBED_WLL_RECIPIENTS_BY_COURT_ID_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_unsub_wll_rec_by_court_id(?) }";

    private static final String GET_BLOB_DATA_FUNCTION = "{ ? = call xhb_list_distribution_pkg.get_blob_data(?) }";

    /**
     * Method to acquire the next available list that is yet to be processed.
     * This method will return the primary key of the list to be processed, or
     * <i>null</i> if no more lists need to be processed.
     * 
     * @return The primary key of the list to process, or <i>null</i> if no
     *         more lists require processing.
     */
    public Integer getNextListId(Boolean isPilot) {
        if (log.isDebugEnabled()) {
            log.debug("getNextListId() - called");
        }

        // no input parameters required, so call function directly...
        final StoredFunction sf = createStoredFunction(GET_NEXT_CONTROL_ID_FUNCTION);
        sf.registerInTypes(new int[] { Types.BOOLEAN });
        final Integer id = (Integer) sf.executeFunction(new Object[]{isPilot}, Types.INTEGER);

        if (log.isDebugEnabled()) {
            log.debug("getNextListId() - Returning id=" + id);
        }

        return id;
    }

    /**
     * Update the status of the list entity identified by the passed in list id
     * primary key to indicate success or failure of processing of the list.
     * 
     * @param listId
     *            Primary key of list entity we are updating.
     * @param success
     *            <i>true</i> if the list was successfully processed, or
     *            <i>false</i> if not successfully processed.
     * 
     * @throws IllegalArgumentException
     *             if the passed in id is <i>null</i>.
     */
    public void updateStatus(final Integer listId, final boolean success) {
        if (log.isDebugEnabled()) {
            log.debug("updateStatus() - listId=" + listId + "; success=" + success);
        }

        validateParameterNotNull("listId", listId);

        // now that we know the id is valid, call the stored proc...
        final StoredFunction sf = createStoredFunction(UPDATE_CONTROL_STATUS_FUNCTION);
        sf.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
        sf.executeFunction(new Object[] { listId, getBooleanAsInteger(success) });
    }

    /**
     * Get the list summary for the list whose id is passed in.
     * 
     * @param classDescriptorResolver
     *            the resolver used to read the list for the summary.
     * @param listId
     *            Primary key of list entity we are getting the details for.
     * @return An implementation of <code>ListSummary</code> for the document
     *         found, containing all required information.
     * 
     * @throws IllegalArgumentException
     *             if the passed in id or resolver is <i>null</i>.
     */
    public ListSummary getListSummary(final ClassDescriptorResolver classDescriptorResolver, final Integer listId) {
        if (log.isDebugEnabled()) {
            log.debug("getListSummary() - listId=" + listId);
        }

        validateParameterNotNull("classDescriptorResolver", classDescriptorResolver);
        validateParameterNotNull("listId", listId);

        final ListSummaryRowProcessor rp = new ListSummaryRowProcessor(classDescriptorResolver);
        final StoredProcedure sp = createStoredProcedure(GET_XML_DOCUMENT_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);

        sp.execute(new Object[] { listId });

        return rp.getListSummary();
    }

    /**
     * Get the control value for the specified court with the
     * 
     * @param courtId
     *            the court to get the data for
     * @return an array containing the control data
     * 
     * @throws IllegalArgumentException
     *             if the courtId is <i>null</i>.
     */
    public WllControlComplexValue[] getWllControlComplexValues(final Integer courtId) {
        if (log.isDebugEnabled()) {
            log.debug("getWllControlComplexValues() - courtId=" + courtId);
        }

        validateParameterNotNull("courtId", courtId);

        final WllControlComplexValuesRowProcessor rp = new WllControlComplexValuesRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_WLL_CONTROL_BY_COURT_ID_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId });
        return rp.getWllControlComplexValues();
    }

    /**
     * Get the control value for the specified court with the
     * 
     * @param courtId
     *            the court to get the data for
     * @return an array containing the control data
     * 
     * @throws IllegalArgumentException
     *             if the courtId is <i>null</i>.
     */
    public DocumentControlComplexValue[] getDocumentControlComplexValues(final Integer courtId) {
        if (log.isDebugEnabled()) {
            log.debug("getDocumentControlComplexValues() - courtId=" + courtId);
        }

        validateParameterNotNull("courtId", courtId);

        final DocumentControlComplexValuesRowProcessor rp = new DocumentControlComplexValuesRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_DOCUMENT_CONTROL_BY_COURT_ID_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId });
        return rp.getDocumentControlComplexValues();
    }

    /**
     * Get the control value for the specified court
     * 
     * @param wllControlId
     *            the court to get the data for
     * @return an array containing the control data
     * 
     * @throws IllegalArgumentException
     *             if the wllControlId is <i>null</i>.
     */
    public WllControlComplexValue getWllControlComplexValue(final Integer wllControlId) {
        if (log.isDebugEnabled()) {
            log.debug("getWllControlComplexValue() - wllControlId=" + wllControlId);
        }

        validateParameterNotNull("wllControlId", wllControlId);

        final WllControlComplexValueRowProcessor rp = new WllControlComplexValueRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_WLL_CONTROL_BY_PK_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { wllControlId });
        return rp.getWllControlComplexValue();
    }

    /**
     * Get the unsubscribed wll recipient values for the specified court
     * 
     * @param courtId
     *            the court to get the data for
     * @return an array containing the recipient data
     * 
     * @throws IllegalArgumentException
     *             if the courtId is <i>null</i>.
     */
    public WllRecipientComplexValue[] getUnsubscribedWllRecipientComplexValues(final Integer courtId) {
        if (log.isDebugEnabled()) {
            log.debug("getControlSummaries() - courtId=" + courtId);
        }

        validateParameterNotNull("courtId", courtId);

        final WllRecipientComplexValuesRowProcessor rp = new WllRecipientComplexValuesRowProcessor(false);
        final StoredProcedure sp = createStoredProcedure(GET_UNSUBSCRIBED_WLL_RECIPIENTS_BY_COURT_ID_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId });
        return rp.getWllRecipientComplexValues();
    }

    /**
     * Get the subscribed wll recipient values and their document distribution
     * data for the specified court
     * 
     * @param courtId
     *            the court to get the data for
     * @return an array containing the recipient data
     * 
     * @throws IllegalArgumentException
     *             if the courtId is <i>null</i>.
     */
    public WllRecipientComplexValue[] getSubscribedWllRecipientComplexValues(final Integer courtId) {
        if (log.isDebugEnabled()) {
            log.debug("getControlSummaries() - courtId=" + courtId);
        }

        validateParameterNotNull("courtId", courtId);

        final WllRecipientComplexValuesRowProcessor rp = new WllRecipientComplexValuesRowProcessor(true);
        final StoredProcedure sp = createStoredProcedure(GET_SUBSCRIBED_WLL_RECIPIENTS_BY_COURT_ID_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId });
        return rp.getWllRecipientComplexValues();
    }

    /**
     * Get the xml for the letters for the specified list, uses the flags to
     * determine which type of letters to include.
     * 
     * @param wllControlId
     *            identifies which list to get letters for
     * @return an array of Strings containing the Xml data
     * 
     * @throws IllegalArgumentException
     *             if the courtId is <i>null</i>.
     */
    public String[] getLetterXml(final Integer wllControlId, final boolean includePost, final boolean includeEmail,
            final boolean includeFax) {
        if (log.isDebugEnabled()) {
            log.debug("getLetterXml() - wllControlId=" + wllControlId + ", includePost=" + includePost
                    + ", includeEmail=" + includeEmail + ", includeFax=" + includeFax);
        }

        validateParameterNotNull("wllControlId", wllControlId);

        final StringClobDataRowProcessor rp = new StringClobDataRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_LETTER_XML_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { wllControlId, boolean2Integer(includePost), boolean2Integer(includeEmail),
                boolean2Integer(includeFax) });
        return rp.getStrings();
    }

    /**
     * Get the html from a blob for a given blob_id
     * 
     * @param blobId
     *            identifies which blob to retrieve
     * @return String containing the html data
     * 
     * @throws IllegalArgumentException
     *             if the blobId is <i>null</i>.
     */
    public String[] getBlobData(final Integer blobId) {
        if (log.isDebugEnabled()) {
            log.debug("getBlobData() - blobId=" + blobId);
        }

        validateParameterNotNull("blobId", blobId);

        final BlobDataRowProcessor rp = new BlobDataRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_BLOB_DATA_FUNCTION);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { blobId });
        return rp.getStrings();
    }

    /**
     * Convert the boolean flag into an integer
     */
    private static Integer boolean2Integer(boolean flag) {
        return flag ? new Integer(1) : new Integer(0);
    }

}
