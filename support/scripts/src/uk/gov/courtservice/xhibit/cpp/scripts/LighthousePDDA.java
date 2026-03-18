package uk.gov.courtservice.xhibit.cpp.scripts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.cpp.scripts.db.DBCPDataSource;
import uk.gov.courtservice.xhibit.cpp.scripts.pojo.DBConnectionProperties;
import uk.gov.courtservice.xhibit.cpp.scripts.pojo.GeneralProperties;

/**
 * This class is the main class for dealing with inserting CPP data into
 * XHB_CPP_STAGING_INBOUND and updating XHB_PDDA_MESSAGE.
 * 
 * When running it will delegate as needed to: - initially setup a database
 * connection pool to be used throughout its lifetime - check for the existence
 * of (unprocessed) records in the database - threads, up to a defined maximum
 * allowed, will be spawned to deal with each record - for each record it will
 * add an entry to XHB_CPP_STAGING_INBOUND - once processed it will then update
 * the message record to show it has been processed.
 */
public class LighthousePDDA {

	// cppProperties can be found in
	// C:\XHIBIT\XHIBIT_8_2\support\scripts\resources
	private static String propertiesFilename = "cppProperties.properties";
	static DBConnectionProperties dbProps;
	static GeneralProperties genProps;
	static final Logger logger = Logger.getLogger(LighthousePDDA.class);

	public static void main(String[] args) {
		logger.debug(System.currentTimeMillis() + " :: METHOD ENTRY:: main");
		setup();
		LighthousePDDA lhcpp = new LighthousePDDA();
		lhcpp.processFiles();

		logger.debug(System.currentTimeMillis() + " :: All RunPDDAJob threads are complete");
		try {
			DBCPDataSource.shutdownDataSource();
		} catch (SQLException e) {
			logger.error(System.currentTimeMillis() + " :: Error occurred shutting down data source " + e);
		}

		logger.debug(System.currentTimeMillis() + " :: METHOD EXIT:: main");
	}

	/**
	 * Do all setup tasks
	 */
	private static void setup() {
		logger.debug(System.currentTimeMillis() + " :: METHOD ENTRY:: setup");

		// Get all properties
		try {
			Configuration config = new PropertiesConfiguration(propertiesFilename);
			dbProps = new DBConnectionProperties();
			genProps = new GeneralProperties();

			setDBConnectionProperties(config);
			setGeneralProperties(config);
		} catch (ConfigurationException ce) {
			ce.printStackTrace();
		}

		// Setup connection pool
		DBCPDataSource.setupConnectionPool(dbProps);

		logger.debug(System.currentTimeMillis() + " :: METHOD EXIT:: setup");
	}

	/**
	 * Process all the messages held in the database that have been validated
	 * and are awaiting processing.
	 */
	private void processFiles() {
		logger.debug(System.currentTimeMillis() + " :: METHOD ENTRY:: processFiles");
		final String query = "select xpm.pdda_message_id, xpm.cp_document_name,"
				+ " xpm.pdda_message_clob_id, xpm.pdda_message_blob_id"
				+ " from xhb_pdda_message xpm" + " where xpm.cp_document_status is not null"
				+ " and xpm.cp_document_status = 'VN'" + " and coalesce(xpm.obs_ind,'N') = 'N'";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		ExecutorService executor = Executors.newFixedThreadPool(genProps.getNumThreads());
		try {
			// Get the connection from the datasource and setup the statement
			conn = DBCPDataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				// Retrieve the message id, document name and clob id
				Long messageId = rs.getLong(1);
				String docName = rs.getString(2).trim();
				Long clobId = rs.getLong(3);

				Runnable worker = new RunPDDAJob(messageId, docName, clobId);
				executor.execute(worker);
			}

		} catch (SQLException e) {
			logger.error("Error retrieving rows from the database" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("Error with closing result set : " + e);
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error("Error with closing statement : " + e);
			}
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
			// Wait until all threads are finished
		}
		logger.debug(System.currentTimeMillis() + " :: METHOD EXIT:: processFiles");
	}

	/**
	 * Set the Database properties
	 * 
	 * @param config
	 *            Configuration object to read from
	 */
	private static void setDBConnectionProperties(final Configuration config) {
		dbProps.setUrl(config.getString("db.url"));
		dbProps.setUsername(config.getString("db.username"));
		dbProps.setPassword(config.getString("db.password"));
		dbProps.setMinIdle(config.getInt("db.minIdle"));
		dbProps.setMaxIdle(config.getInt("db.maxIdle"));
		dbProps.setMaxOpenStatements(config.getInt("db.maxOpenStatements"));
	}

	/**
	 * Set the General properties
	 * 
	 * @param config
	 *            Configuration object to read from
	 */
	private static void setGeneralProperties(final Configuration config) {
		genProps.setNumThreads(config.getInt("gen.numThreads"));
	}
}

/**
 * A class that does the actual work, adds a record the xhb_cpp_staging_inbound
 * table and updates the xhb_pdda_message table. Each message is picked up by
 * its own thread. With a maximum number of threads running at a time (defined
 * in the property file).
 */
class RunPDDAJob implements Runnable {

	private final Long messageId;
	private final String docName;
	private final Long clobId;

	private static final String new_staging_inbound_status = "NP";
	private static final String message_status_inprogress = "IP";
	private static final String message_status_processed = "VP";
	private static final String message_status_invalid = "INV";

	static final Logger logger = Logger.getLogger(RunPDDAJob.class);

	/**
	 * Constructor.
	 * 
	 * @param messageId
	 *            Message Id
	 * @param docName
	 *            Document Name
	 * @param clobId
	 *            Clob Id
	 */
	RunPDDAJob(final Long messageId, final String docName, final Long clobId) {
		writeToLog("Setting up the RunPDDAJob to process a file");
		this.messageId = messageId;
		this.docName = docName;
		this.clobId = clobId;
	}

	/**
	 * Overridden run method to be invoked for each instance in a Thread.
	 */
	@Override
	public void run() {
		writeToLog("About to process file " + docName);
		Connection conn = null;

		try {
			// split up the filename into its 3 parts : type_courtCode_dateTime
			String[] fileParts = docName.split("_");
			if (fileParts.length == 3) {
				conn = DBCPDataSource.getConnection();

				// Update the status to indicate it is being processed
				updatePDDAMessageInProgress(conn);

				// Insert XHB_CPP_STAGING_INBOUND row, returning PK
				writeToLog("About to add " + clobId + " to the cpp staging inbound table");
				long stagingInboundId = insertStaging(docName, fileParts[1], getDocType(fileParts[0]),
						fileParts[2].replaceAll(".xml", ""), clobId, conn);
				writeToLog("Successfully added  " + stagingInboundId + " to the cpp staging inbound table");

				// Update XHB_PDDA_MESSAGE record
				updatePDDAMessage(stagingInboundId, conn);

				writeToLog("Processing of " + docName + " completed");
			} else {
				logger.error("Filename is not valid : " + docName);
			}
		} catch (SQLException e) {
			logger.error("Error adding data to the database for file " + docName + " :" + e.getStackTrace());
			// Change the status of the XHB_PDDA_MESSAGE record to invalid
			updatePDDAMessageForError(e.getMessage(), conn);
		} finally {
			try {
				DBCPDataSource.shutdownDataSource();
			} catch (SQLException e) {
				logger.error("Error with closing connection : " + e);
			}
		}
	}

	/**
	 * Write to the debug log if debug is enabled.
	 * 
	 * @param string
	 *            Debug message to write to the log
	 */
	private void writeToLog(final String string) {
		if (logger.isDebugEnabled()) {
			logger.debug(System.currentTimeMillis() + " :: " + string);
		}
	}

	/**
	 * Updates the XHB_PDDA_MESSAGE record to show it is in progress.
	 * 
	 * @param errorMsg
	 *            The error message
	 * @param connection
	 *            Database connection
	 * @throws SQLException
	 */
	private void updatePDDAMessageInProgress(final Connection connection) throws SQLException {
		PreparedStatement ps = null;
		final String updateSQL = "UPDATE xhb_pdda_message SET cp_document_status = ? WHERE pdda_message_id = ?";

		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, message_status_inprogress);
			ps.setLong(2, messageId);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Unable to update xhb_pdda_message to in progress status " + e);
			throw e;
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * Insert the row into XHB_CPP_STAGING_INBOUND
	 * 
	 * @param docName
	 *            - Document name e.g. DailyList_453_20200101123213.xml
	 * @param courtCode
	 *            from the document name
	 * @param documentType
	 *            e.g. DL
	 * @param timeLoaded
	 *            which is the 3rd part of the document name
	 * @param clobId
	 *            that was created pre this insert
	 * @param connection
	 *            Database connection
	 * @return the staging inbound id for debugging/logging purposes
	 * @throws SQLException
	 */
	private long insertStaging(final String docName, final String courtCode, final String documentType,
			final String timeLoaded, final Long clobId, final Connection connection) throws SQLException {

		PreparedStatement statement = null;
		ResultSet rs = null;
		writeToLog("doc " + docName + " courtCode: " + courtCode + " documentType: " + documentType + " timeLoaded: "
				+ timeLoaded + " clobId :" + clobId + " validationStatus :" + new_staging_inbound_status);

		// Please not that this will work on POSTGRESQL, but not on ORACLE
		final String insertQuery = "INSERT INTO XHB_CPP_STAGING_INBOUND (document_name, court_code, document_type, time_loaded, clob_id, validation_status) "
				+ "VALUES (?, ?, ?, to_date(?,'YYYYMMDDHH24MISS'),  ?, ?) returning cpp_staging_inbound_id";

		try {
			statement = connection.prepareCall(insertQuery);
			statement.setString(1, docName);
			statement.setString(2, courtCode);
			statement.setString(3, documentType);
			statement.setString(4, timeLoaded);
			statement.setLong(5, clobId);
			statement.setString(6, new_staging_inbound_status);
			statement.execute();
			long id = 0;
			rs = statement.getResultSet();
			if (rs.next()) {
				id = rs.getLong(1);
			}

			return id;
		} catch (SQLException e) {
			logger.error("Unable to insert staging info " + e);
			throw e;
		} finally {
			closeStatement(statement);
		}
	}

	/**
	 * Update the XHB_PDA_MESSAGE record
	 * 
	 * @param stagingInboundId
	 *            - cpp staging inbound id
	 * @param connection
	 *            Database connection
	 * @throws SQLException
	 */
	private void updatePDDAMessage(final long stagingInboundId, final Connection connection) throws SQLException {

		PreparedStatement ps = null;
		writeToLog("doc " + docName + " docStatus: " + message_status_processed + " messageId: " + messageId);

		final String updateSQL = "UPDATE xhb_pdda_message" + " SET cpp_staging_inbound_id = ?,"
				+ " cp_document_status = ?" + " WHERE pdda_message_id = ?";

		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setLong(1, stagingInboundId);
			ps.setString(2, message_status_processed);
			ps.setLong(3, messageId);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Unable to update xhb_pdda_message " + e);
			throw e;
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * In the event of an error performing the normal database updates, this
	 * method updates the status of the XHB_PDDA_MESSAGE record to invalid.
	 * 
	 * @param errorMsg
	 *            The error message
	 * @param connection
	 *            Database connection
	 */
	private void updatePDDAMessageForError(final String errorMsg, final Connection connection) {
		PreparedStatement ps = null;
		final String updateSQL = "UPDATE xhb_pdda_message SET cp_document_status = ?, error_message = ? WHERE pdda_message_id = ?";

		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, message_status_invalid);
			ps.setString(2, errorMsg);
			ps.setLong(3, messageId);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Unable to update xhb_pdda_message to invalid status " + e);
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * Return the document type depending on what's been used in the file name.
	 * 
	 * @param fileType
	 *            portion of the filename
	 * @return shorthand documentType
	 */
	private String getDocType(final String fileType) {
		writeToLog("METHOD ENTRY: getDocType");

		if (fileType.equalsIgnoreCase(DocumentType.DL.getDocName())) {
			return DocumentType.DL.name();
		} else if (fileType.equalsIgnoreCase(DocumentType.WL.getDocName())) {
			return DocumentType.WL.name();
		} else if (fileType.equalsIgnoreCase(DocumentType.FL.getDocName())) {
			return DocumentType.FL.name();
		} else if (fileType.equalsIgnoreCase(DocumentType.WP.getDocName())) {
			return DocumentType.WP.name();
		} else if (fileType.equalsIgnoreCase(DocumentType.PD.getDocName())) {
			return DocumentType.PD.name();
		}
		return null;
	}

	/**
	 * Close statement after create/update complete.
	 * 
	 * @param stmt
	 *            Statement to close
	 */
	private void closeStatement(final Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (final SQLException t) {
				logger.error("Error closing Statement", t);
			}
		}
	}

}