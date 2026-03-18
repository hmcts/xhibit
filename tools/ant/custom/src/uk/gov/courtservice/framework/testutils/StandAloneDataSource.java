package uk.gov.courtservice.framework.testutils;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * A simple implementation of a <code>DataSource</code> to allow testing to be
 * performed outside of an application server. This method will not cache/pool
 * any connection, each call to getConnection will return a new database
 * connection.
 * 
 * @author tz0d5m
 * @version $Id: StandAloneDataSource.java,v 1.5 2014/06/22 18:18:44 atwells Exp $
 */
public class StandAloneDataSource implements DataSource {
    // The log4j logger instance
    private static final Logger log = Logger.getLogger(StandAloneDataSource.class);

    // load all properties from the command line
    public final static String driver = getDatabaseProperty("database.driver");

    public final static String user = getDatabaseProperty("database.user");

    public final static String password = getDatabaseProperty("database.password");

    public final static String url = getDatabaseProperty("database.url");

    // Get a property from the system properties, throw an error if not
    // found.
    // Log the value returned.
    private static String getDatabaseProperty(String key) {
        String value = System.getProperty(key);
        if (value != null) {
            if (log.isDebugEnabled()) {
                log.debug("Found system property \"" + key + "\" with value \"" + value + "\".");
            }
            return value;
        }
        throw new RuntimeException("Mandatory system property \"" + key + "\" not set.");
    }

    // now try to load the driver specified, if error we cannot handle so
    // throw a runtime exception...
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load driver \"" + driver + "\": " + e.getMessage());
        }
    }

    private PrintWriter logWriter = new PrintWriter(new OutputStreamWriter(System.err));

    private int loginTimeout = 0;

    /**
     * @see javax.sql.DataSource#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return getConnection(user, password);
    }

    /**
     * @see javax.sql.DataSource#getConnection(java.lang.String,
     *      java.lang.String)
     */
    public Connection getConnection(String userName, String pword) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, pword);
        return connection;
    }

    /**
     * @see javax.sql.DataSource#getLogWriter()
     */
    public PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }

    /**
     * @see javax.sql.DataSource#getLoginTimeout()
     */
    public int getLoginTimeout() throws SQLException {
        return this.loginTimeout;
    }

    /**
     * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        this.logWriter = logWriter;
    }

    /**
     * @see javax.sql.DataSource#setLoginTimeout(int)
     */
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        this.loginTimeout = loginTimeout;
    }
    
    @Override
    public boolean isWrapperFor(Class <?> iface) throws SQLException {
        return false;
    }
    
    @Override
    public <T extends Object> T unwrap(Class <T> iface) throws SQLException {
        return null;
    }
}
