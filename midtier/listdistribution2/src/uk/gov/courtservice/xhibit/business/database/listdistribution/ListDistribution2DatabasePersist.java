package uk.gov.courtservice.xhibit.business.database.listdistribution;

import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;

import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.listdistribution.ListDistributionException;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.ListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessor;

/**
 * A database persistence class used to handle all persistence required by the
 * list distribution 2 sub-project. This object has been coded to allow easy
 * extension for use by development/test classes.
 * 
 * This class uses custom JDBC calls to allow the same call to be performed in a
 * loop (against a <code>CallableStatement</code> to prevent numerous parsing
 * calls.
 * 
 * @author tz0d5m
 * @version $Id: ListDistribution2DatabasePersist.java,v 1.3 2005/01/13 12:36:32
 *          tz0d5m Exp $
 */
public class ListDistribution2DatabasePersist {
    /** The log4j <code>Logger</code> instance */
    protected final Logger log = CSServices.getLogger(getClass());

    // The full JDBC function call for the create list letters method
    private static final String CREATE_LIST_LETTER_FUNCTION_CALL = "{ ? = call xhb_list_distribution_pkg.create_list_letter(?,?,?,?,?,?,?,?,?,?) }";

    // Constants representing the position in the function call for each
    // property
    private static final int RETURN_CLOB_CURSOR_POS = 1;

    private static final int LIST_ID_POS = 2;

    private static final int COURT_ID_POS = 3;

    private static final int DOCUMENT_TYPE_POS = 4;

    private static final int DOCUMENT_TITLE_POS = 5;

    private static final int MAJOR_SCHEMA_VERSION_POS = 6;

    private static final int MINOR_SCHEMA_VERSION_POS = 7;

    private static final int LANGUAGE_POS = 8;

    private static final int COUNTRY_POS = 9;

    private static final int RECIPIENT_ID_POS = 10;

    private static final int RECIPIENT_TYPE_POS = 11;

    /**
     * Method to persist the <code>ListLetter</code>s genetated from the
     * passed in <code>ListSummary</code> object to the database as xml
     * documents.
     * 
     * @param classDescriptorResolver
     *            the resolver used to marshal each letter for the summary.
     * @param listSummary
     *            The summary object containing all of the parameters required
     *            for use by this method, including the processor.
     * 
     * @throws IllegalArgumentException
     *             if the passed in id or resolver is <i>null</i>.
     */
    public void persistListLetters(final ClassDescriptorResolver classDescriptorResolver, final ListSummary listSummary) {
        if (log.isDebugEnabled()) {
            log.debug("persistListLetters() - listSummary=" + listSummary);
        }

        _validateParameterNotNull("classDescriptorResolver", classDescriptorResolver);
        _validateParameterNotNull("listSummary", listSummary);

        final ListProcessor listProcessor = listSummary.getListProcessor();
        final ListLetter[] listLetters = listProcessor.processList(listSummary.getList());

        if (log.isDebugEnabled()) {
            log.debug("persistListLetters() - Need to persist " + listLetters.length + " letters");
        }

        if (listLetters.length > 0) {
            if (log.isDebugEnabled()) {
                log.debug("persistListLetters() - " + "listId=" + listSummary.getListId() + ";courtId="
                        + listSummary.getCourtId());
            }

            Connection con = null;
            CallableStatement cstmt = null;

            try {
                con = getConnection();
                cstmt = con.prepareCall(CREATE_LIST_LETTER_FUNCTION_CALL);

                // register the parameters that will not change...
                cstmt.registerOutParameter(RETURN_CLOB_CURSOR_POS, OracleTypes.CURSOR);
                cstmt.setObject(LIST_ID_POS, listSummary.getListId());
                cstmt.setObject(COURT_ID_POS, listSummary.getCourtId());

                // register the optional parameters that will not change...
                Integer majorVersion = listSummary.getMajorVersion();
                if (majorVersion != null) {
                    cstmt.setObject(MAJOR_SCHEMA_VERSION_POS, majorVersion);
                } else {
                    cstmt.setNull(MAJOR_SCHEMA_VERSION_POS, Types.INTEGER);
                }
                Integer minorVersion = listSummary.getMinorVersion();
                if (minorVersion != null) {
                    cstmt.setObject(MINOR_SCHEMA_VERSION_POS, minorVersion);
                } else {
                    cstmt.setNull(MINOR_SCHEMA_VERSION_POS, Types.INTEGER);
                }
                String language = listSummary.getLanguage();
                if (language != null) {
                    cstmt.setObject(LANGUAGE_POS, language);
                } else {
                    cstmt.setNull(LANGUAGE_POS, Types.VARCHAR);
                }
                String country = listSummary.getCountry();
                if (country != null) {
                    cstmt.setObject(COUNTRY_POS, country);
                } else {
                    cstmt.setNull(COUNTRY_POS, Types.VARCHAR);
                }
                // Process each letter in turn.

                for (int i = 0, n = listLetters.length; i < n; i++) {
                    final ListLetter listLetter = listLetters[i];
                    _writeLetter(classDescriptorResolver, listProcessor, listLetter, cstmt);
                }
            } catch (final SQLException e) {
                // wrap exception in a component custom runtime exception...
                throw new ListDistributionException(e);
            } finally {
                JdbcHelper.closeStatement(cstmt);
                closeConnection(con);
            }
        }
    }

    /**
     * Internal method used to finish populating the parameters of the
     * <code>CallableStatement</code>, execute it and populate the returned
     * <code>Clob</code> with the correct details.
     * 
     * Extracted to own method to break down a larger method.
     */
    private void _writeLetter(final ClassDescriptorResolver classDescriptorResolver, final ListProcessor listProcessor,
            final ListLetter listLetter, final CallableStatement cstmt) throws SQLException {
        if (log.isDebugEnabled()) {
            final StringBuffer buffer = new StringBuffer();

            buffer.append("_writeLetter::");
            buffer.append("letterType=").append(listProcessor.getLetterType(listLetter));
            buffer.append(";letterTitle=").append(listProcessor.getLetterTitle(listLetter));

            if (listProcessor.hasRecipientId(listLetter)) {
                buffer.append(";recipientId=").append(listProcessor.getRecipientId(listLetter));
                buffer.append(";cecipientType=").append(listProcessor.getRecipientType(listLetter));
            } else {
                buffer.append(";No recipient");
            }

            log.debug(buffer);
        }

        // the writer must always be closed in a finally block, so define
        // early...
        Writer writer = null;

        try {
            // register/alter the parameters that change each iteration...
            cstmt.setObject(DOCUMENT_TYPE_POS, listProcessor.getLetterType(listLetter));
            cstmt.setObject(DOCUMENT_TITLE_POS, listProcessor.getLetterTitle(listLetter));

            if (listProcessor.hasRecipientId(listLetter)) {
                cstmt.setObject(RECIPIENT_ID_POS, listProcessor.getRecipientId(listLetter));
                cstmt.setObject(RECIPIENT_TYPE_POS, listProcessor.getRecipientType(listLetter));
            } else {
                // ensure that the previous values get cleared out...
                cstmt.setNull(RECIPIENT_ID_POS, Types.INTEGER);
                cstmt.setNull(RECIPIENT_TYPE_POS, Types.INTEGER);
            }

            cstmt.execute();

            // acquire the writer from the returned clob...
            writer = getWriter(cstmt, RETURN_CLOB_CURSOR_POS);

            // write the letter out to the clob...
            listProcessor.writeLetter(classDescriptorResolver, writer, listLetter);
        } finally {
            // always ensure that the writer is closed correctly...
            _closeWriter(writer);
        }
    }

    /**
     * Extracted helper method used to acquire a <code>Writer</code> from the
     * passed in <code>CallableStatement</code> to write to a clob. This
     * method uses database specific calls and has been extracted to allow
     * possible future enhancements to allow for different databases.
     * 
     * @param cstmt
     *            The <code>CallableStatement</code> to read the clob from.
     * @param columnPos
     *            The position of the output used to acquire the clob from the
     *            passed in <code>CallableStatement</code>
     * 
     * @return A <code>Writer</code> from the database.
     * 
     * @throws SQLException
     *             If an error occurs whilst acquiring the <code>Writer</code>.
     */
    protected Writer getWriter(final CallableStatement cstmt, final int columnPos) throws SQLException {
        ResultSet rset = null;

        try {
            rset = (ResultSet) cstmt.getObject(columnPos);
            if (rset.next()) {
                return rset.getClob("CLOB_DATA").setCharacterStream(0);
            }

            // no values found, so throw an exception...
            throw new ListDistributionException("ResultSet is empty");
        } finally {
            JdbcHelper.closeResultSet(rset);
        }
    }

    /**
     * Extracted helper method used to acquire a database connection. Extracted
     * to allow overriding classes (e.g. a test class) to return a custom
     * <code>Connection</code> object.
     * 
     * @return A database <code>Connection</code>
     * @throws SQLException
     *             If an error occurs whilst acquiring the
     *             <code>Connection</code>.
     */
    protected Connection getConnection() throws SQLException {
        return CSServices.getServiceLocator().getDataSource().getConnection();
    }

    /**
     * Extracted helper method used to close the <code>Connection</code> used
     * by the persistListLetters method. Extracted to allow overriding class
     * (e.g. test class) to perform extra processing (such as rolling back an
     * active transaction, to allow out-of-container testing).
     * 
     * @param con
     *            The <code>Connection</code> to close.
     */
    protected void closeConnection(final Connection con) {
        JdbcHelper.closeConnection(con);
    }

    /**
     * Extracted internal helper method used to close the passed in
     * <code>Writer</code> and to ignore/log any errors but allow to continue
     * as normal.
     * 
     * @param writer
     *            The <code>Writer</code> to close.
     */
    private final void _closeWriter(final Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (final Throwable t) {
                log.error("Error closing writer", t);
            }
        }
    }

    /**
     * Internal helper method used to validate that parameters are not <i>null</i>.
     * 
     * @param name
     *            The name of the parameter we are validating.
     * @param value
     *            The value we are ensuring is not <i>null</i>.
     */
    private final void _validateParameterNotNull(final String name, final Object value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }
}
