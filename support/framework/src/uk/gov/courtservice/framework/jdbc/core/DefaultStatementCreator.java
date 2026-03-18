package uk.gov.courtservice.framework.jdbc.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: DefaultStatementCreator
 * </p>
 * <p>
 * Description: The default implementation for the statement creator
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author XHIBIT User
 * @version 1.0
 */

public class DefaultStatementCreator extends StatementCreator {

    private static final Logger log = Logger.getLogger(DefaultStatementCreator.class);

    private static final String IS_CLOB_PROCESSING_ENABLED = "IS_CLOB_PROCESSING_ENABLED";

    private static final String IS_CLOB_PROCESSING_DEFAULT = "TRUE";

    private static final String IS_BLOB_PROCESSING_ENABLED = "IS_BLOB_PROCESSING_ENABLED";

    private static final String IS_BLOB_PROCESSING_DEFAULT = "FALSE";

    private static final String CHARACTER_ENCODING = "CHARACTER_ENCODING";

    private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    /**
     * Creates a prepared statement
     * 
     * @connection con
     * @param sql
     * @param input
     *            types
     * @param input
     *            arguments
     * @return
     * @throws SQLException
     */
    public PreparedStatement createPreparedStatement(Connection con, String sql, Parameter[] params)
            throws SQLException {

        // Create the prepared statement
        PreparedStatement ps = con.prepareStatement(sql);
        // Set the parameters
        try {
            setParameters(ps, params, con);
        } catch (IOException io) {
            log.error("IOException during CLOB or BLOB processing for prepared statement" + io, io);
            throw new SQLException("IOException setting CLOB or BLOB");
        }
        // Set the query timeout
        ps.setQueryTimeout(0);

        return ps;

    }

    /**
     * Creates a callable statement
     * 
     * @connection con
     * @param sql
     * @param input
     *            types
     * @param Output
     *            types
     * @param input
     *            arguments
     * @return
     * @throws SQLException
     */
    public CallableStatement createCallableStatement(Connection con, String call, Parameter[] params)
            throws SQLException {

        // Create the prepared statement
        CallableStatement cs = con.prepareCall(call);
        // Set the in parameters
        try {
            setParameters(cs, params, con);
        } catch (IOException io) {
            log.error("IOException during CLOB or BLOB processing for callable statement" + io, io);
            throw new SQLException("IOException setting CLOB or BLOB");
        }
        // Set the query timeout
        cs.setQueryTimeout(0);

        // Return the callable statement
        return cs;

    }

    /**
     * Sets the in parameters
     * 
     * Note: CLOBs and BLOBs have different processing as going via the normal mechanism
     * leads to database exceptions (ORA-01460) when inserting CLOBs or BLOBs of
     * larger than 3k that contain National Characters (eg £) due to an Oracle bug
     * 
     * This CLOB and BLOB processing can be enabled via configurable properties,
     * however only the CLOB processing is enabled by default (as this is required
     * for XHIBIT 8.1 requirements) whereas BLOB processing is not enabled by default
     * although it can be if required
     * 
     * @param Prepared statement
     * @param Parameters
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, Parameter params[], Connection con) throws SQLException,
            IOException {

        // Set the parameters
        for (int i = 0; params != null && i < params.length; i++) {
            // Set the IN params
            if (params[i].isIn()) {
                if (params[i].getValue() != null) {
                    if (params[i].getSqlType() == Types.CLOB && isClobProcessingEnabled()) {
                        String clobString = (String) params[i].getValue();
                        CLOB clob = getCLOB(clobString, con);
                        ps.setClob(i + 1, clob);
                    } else if (params[i].getSqlType() == Types.BLOB && isBlobProcessingEnabled()) {
                        String blobString = (String) params[i].getValue();
                        BLOB blob = getBLOB(blobString, con);
                        ps.setBlob(i + 1, blob);
                    } else {
                        ps.setObject(i + 1, params[i].getValue());
                    }
                } else {
                    ps.setNull(i + 1, params[i].getSqlType());
                }
                // Set the OUT param for callable statements
            } else if (params[i].isOut() && ps instanceof CallableStatement) {
                ((CallableStatement) ps).registerOutParameter(i + 1, params[i].getSqlType());
            } else {
                throw new IllegalArgumentException("Unsupported parameter type.");
            }
        }

    }

    /**
     * Builds the CLOB
     * 
     * @param xmlData String
     * @param Connection
     * @return CLOB
     * @throws SQLException, IOException
     */
    private CLOB getCLOB(String xmlData, Connection conn) throws SQLException, IOException {
        log.info("Start getCLOB");

        CLOB tempClob = null;
        Writer tempClobWriter = null;

        try {
            tempClob = CLOB.createTemporary(conn, true, CLOB.DURATION_SESSION);

            tempClob.open(CLOB.MODE_READWRITE);

            tempClobWriter = tempClob.setCharacterStream(0);

            tempClobWriter.write(xmlData);
        } catch (SQLException sqle) {
            freeTempClob(tempClob);
            throw sqle;
        } catch (IOException io) {
            freeTempClob(tempClob);
            throw io;
        } catch (Exception e) {
            freeTempClob(tempClob);
            log.error("Unexpected Exception during CLOB processing for callable statement" + e, e);
            throw new SQLException("Unexpected Exception setting CLOB");
        } finally {
            closeClobResources(tempClob, tempClobWriter);
        }

        log.info("End getCLOB");

        return tempClob;
    }

    /**
     * This is called when we get an exception in order to free the temp clob
     * 
     * @param CLOB     
     */
    private void freeTempClob(CLOB tempClob) {
        try {
            if (tempClob != null && tempClob.isTemporary()) {
                tempClob.freeTemporary();
            }
        } catch (final Throwable t) {
            log.error("Error freeing Temp CLOB", t);
        }

        if (log.isDebugEnabled()) {
            log.debug("Temporary Clob Freed");
        }
    }

    /**
     * Closes the CLOB resources
     * 
     * @param CLOB
     * @param Writer      
     */
    private void closeClobResources(CLOB tempClob, Writer out) {
        closeClob(tempClob);
        closeWriter(out);

        if (log.isDebugEnabled()) {
            log.debug("Clob Resources released");
        }
    }

    /**
     * A true helper method used to close the passed in <code>CLOB</code>
     * if it is not <i>null</i>. If any errors occur whilst closing they will
     * simply be logged and processing allowed to continue.
     * 
     * @param tempClob
     *            The <code>CLOB</code> to close.
     */
    private static final void closeClob(final CLOB tempClob) {
        if (tempClob != null) {
            try {
                tempClob.close();
            } catch (final Throwable t) {
                log.error("Error closing CLOB", t);
            }
        }
    }

    /**
     * A true helper method used to close the passed in <code>Writer</code>
     * if it is not <i>null</i>. If any errors occur whilst closing they will
     * simply be logged and processing allowed to continue.
     * 
     * @param out
     *            The <code>Writer</code> to close.
     */
    private static final void closeWriter(final Writer out) {
        if (out != null) {
            try {
                out.flush();
            } catch (final Throwable t) {
                log.error("Error flushing Writer", t);
            } finally {
                try {
                    out.close();
                } catch (final Throwable t) {
                    log.error("Error closing Writer", t);
                }
            }
        }
    }

    /**
     * Builds the BLOB
     * 
     * @param xmlData String
     * @param Connection
     * @return BLOB
     * @throws SQLException, IOException
     */
    private BLOB getBLOB(String xmlData, Connection conn) throws SQLException, IOException {
        log.info("Start getBLOB");

        BLOB tempBlob = null;
        OutputStream tempBlobWriter = null;

        try {
            tempBlob = BLOB.createTemporary(conn, true, BLOB.DURATION_SESSION);

            tempBlob.open(BLOB.MODE_READWRITE);

            tempBlobWriter = tempBlob.setBinaryStream(0);

            tempBlobWriter.write(xmlData.getBytes(getCharacterEncoding()));

        } catch (SQLException sqle) {
            freeTempBlob(tempBlob);
            throw sqle;
        } catch (IOException io) {
            freeTempBlob(tempBlob);
            throw io;
        } catch (Exception e) {
            freeTempBlob(tempBlob);
            log.error("Unexpected Exception during BLOB processing for callable statement" + e, e);
            throw new SQLException("Unexpected Exception setting BLOB");
        } finally {
            closeBlobResources(tempBlob, tempBlobWriter);
        }

        log.info("End getBLOB");

        return tempBlob;
    }

    /**
     * This is called when we get an exception in order to free the temp blob
     * 
     * @param BLOB     
     */
    private void freeTempBlob(BLOB tempBlob) {
        try {
            if (tempBlob != null && tempBlob.isTemporary()) {
                tempBlob.freeTemporary();
            }
        } catch (final Throwable t) {
            log.error("Error freeing Temp BLOB", t);
        }

        if (log.isDebugEnabled()) {
            log.debug("Temporary Blob Freed");
        }
    }

    /**
     * Closes the BLOB resources
     * 
     * @param BLOB
     * @param OutputStream      
     */
    private void closeBlobResources(BLOB tempBlob, OutputStream out) {
        closeBlob(tempBlob);
        closeOutputStream(out);

        if (log.isDebugEnabled()) {
            log.debug("Blob Resources released");
        }
    }

    /**
     * A true helper method used to close the passed in <code>BLOB</code>
     * if it is not <i>null</i>. If any errors occur whilst closing they will
     * simply be logged and processing allowed to continue.
     * 
     * @param tempBlob
     *            The <code>BLOB</code> to close.
     */
    private static final void closeBlob(final BLOB tempBlob) {
        if (tempBlob != null) {
            try {
                tempBlob.close();
            } catch (final Throwable t) {
                log.error("Error closing BLOB", t);
            }
        }
    }

    /**
     * A true helper method used to close the passed in <code>OutputStream</code>
     * if it is not <i>null</i>. If any errors occur whilst closing they will
     * simply be logged and processing allowed to continue.
     * 
     * @param out
     *            The <code>OutputStream</code> to close.
     */
    private static final void closeOutputStream(final OutputStream out) {
        if (out != null) {
            try {
                out.flush();
            } catch (final Throwable t) {
                log.error("Error flushing OutputStream", t);
            } finally {
                try {
                    out.close();
                } catch (final Throwable t) {
                    log.error("Error closing OutputStream", t);
                }
            }
        }
    }

    /**
     * Returns the encoding required.
     * Defaults to UTF-8 which is used for ORACLE 9i
     *
     * @return String
     */
    private String getCharacterEncoding() {
        String characterEncoding = CSServices.getConfigServices().getProperty(CHARACTER_ENCODING,
                DEFAULT_CHARACTER_ENCODING);

        log.debug("<< characterEncoding: " + characterEncoding + " >>");

        return characterEncoding;
    }

    /**
     * Return true if we require additional CLOB processing to occur
     * to ensure National Characters are processed and do not error
     * (CLOBs larger than 3k that contain National Characters (eg £) 
     * fail with an ORA-01460 error due to an Oracle bug)
     * 
     * Will be TRUE by default as this functionality is definitely required
     * in XHIBIT Rel 8.1 onwards (due to Reqs 1492 and 1669 which have direct
     * JDBC commits of CLOBs to an Oracle 9i database)
     *
     * @return boolean
     */
    private boolean isClobProcessingEnabled() {
        String isClobProcessingEnabled = CSServices.getConfigServices().getProperty(IS_CLOB_PROCESSING_ENABLED,
                IS_CLOB_PROCESSING_DEFAULT);

        log.debug("<< isClobProcessingEnabled: " + isClobProcessingEnabled + " >>");

        return isClobProcessingEnabled != null && isClobProcessingEnabled.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if we require additional BLOB processing to occur
     * to ensure National Characters are processed and do not error
     * 
     * Will be FALSE by default as this functionality is not definitely required
     * in Rel 8.1 onwards and so BLOBs will be processing like normal fields
     * 
     * If BLOBs are inserted in future requirements and  they are < 3k and contain
     * national characters (eg £) then this property should be enabled and the BLOBs tested
     * using this functionality
     *
     * @return boolean
     */
    private boolean isBlobProcessingEnabled() {
        String isBlobProcessingEnabled = CSServices.getConfigServices().getProperty(IS_BLOB_PROCESSING_ENABLED,
                IS_BLOB_PROCESSING_DEFAULT);

        log.debug("<< isBlobProcessingEnabled: " + isBlobProcessingEnabled + " >>");

        return isBlobProcessingEnabled != null && isBlobProcessingEnabled.equalsIgnoreCase("TRUE");
    }
}