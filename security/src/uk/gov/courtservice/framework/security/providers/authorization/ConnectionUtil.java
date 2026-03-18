package uk.gov.courtservice.framework.security.providers.authorization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Utility class for getting database connections. This class is used by the
 * thread that does the deployment and the thread that refreshes the in-memory
 * cache. Hence deployment always happen at server startup (at least in
 * production) and the default refresh period is six hours, it is unlikely that
 * the connection will be concurrently accessed by two threads. However, if it
 * is perceived that that the deployment thread and refresh thread would run
 * concurrently, the code should be restructured to implement a thread-safe two
 * connection pool. Now the connection iks opened at server startup and shutdown
 * and closed at server shutdown. When a thread requests for a connection, it is
 * checked whether the connection is open before it is returned.
 * 
 * @author Meeraj
 * @version $Id: ConnectionUtil.java,v 1.3 2006/06/05 12:30:04 bzjrnl Exp $
 */
public final class ConnectionUtil {
    private static final Logger log = Logger.getLogger(ConnectionUtil.class);

    private static String URL;

    private static String user;

    private static String password;

    private static Connection con;

    private static boolean initialized;

    /**
     * Initializes the connection util
     * 
     * @param driverName
     *            name
     * @param newURL
     *            URL
     * @param newUser
     *            name
     * @param newPassword
     */
    public static void initialize(String driverName, String newURL, String newUser, String newPassword) {
        URL = newURL;
        user = newUser;
        password = newPassword;

        try {
            Class.forName(driverName);
        } catch (Exception ex) {
            log.error(log);
            return;
        }

        initialized = true;
    }

    /**
     * Gets a connection to the database
     * 
     * @return Connection to the database
     * 
     * @throws SQLException
     */
    public static final Connection getConnection() throws SQLException {
        if (!initialized) {
            throw new SQLException("Not initialized");
        }

        if ((con == null) || con.isClosed()) {
            con = DriverManager.getConnection(URL, user, password);
        }

        return con;
    }

    /**
     * Closes the connection
     */
    public static void close() {
        if (con != null) {
            try {
                con.close();
            } catch (final Throwable t) {
                log.error("Error closing connection", t);
            }
        }
    }

    /**
     * Helper method used to close a <code>Statement</code> without throwing
     * any exceptions. To be used in finally blocks where appropriate, if the
     * passed parameter is <i>null</i>, then it will return immediately.
     * 
     * @param rset
     *            The <code>Statement</code> to close.
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (final Throwable t) {
                log.error("Error closing statement", t);
            }
        }
    }

    /**
     * Helper method used to close a <code>ResultSet</code> without throwing
     * any exceptions. To be used in finally blocks where appropriate, if the
     * passed parameter is <i>null</i>, then it will return immediately.
     * 
     * @param rset
     *            The <code>ResultSet</code> to close.
     */
    public static void closeResultSet(ResultSet rset) {
        if (rset != null) {
            try {
                rset.close();
            } catch (Throwable t) {
                log.error("Error closing resultset", t);
            }
        }
    }
}
