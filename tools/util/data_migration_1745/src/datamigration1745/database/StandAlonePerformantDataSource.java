package datamigration1745.database;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * A simple implementation of a <code>DataSource</code> to allow database connections
 * to be made from a standalone app 
 * This particular class will keep the same connection
 *
 * @author tz0d5m
 * @version $Id: StandAlonePerformantDataSource.java,v 1.3 2014/06/22 18:24:49 atwells Exp $
 */
public class StandAlonePerformantDataSource implements DataSource {
    // The log4j logger instance
    private static final Logger log = Logger.getLogger(StandAlonePerformantDataSource.class);

    // load all properties from the command line
    public final static String driver = getDatabaseProperty("database.driver");

    public final static String user = getDatabaseProperty("database.user");

    public final static String password = getDatabaseProperty("database.password");

    public final static String url = getDatabaseProperty("database.url");
    
    private static ArrayList<Connection> connectionPool = new ArrayList<Connection>();
    
    private static final Integer MAX_POOL_SIZE = new Integer(System.getProperty("connection.pool.maxsize",DataMigrationDatabaseFactory.DEFAULT_MAX_POOL_SIZE.toString()));

    private static Integer currentConnection = 0;
    
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
    public synchronized Connection getConnection(String userName, String pword) throws SQLException {
        
        Connection connection = null;
        
        if(connectionPool==null)
        {
            connectionPool = new ArrayList<Connection>();
        }
        
        if(connectionPool.size() < MAX_POOL_SIZE || currentConnection==null)
        {
            connection = DriverManager.getConnection(url, user, pword);
            connectionPool.add(connection);
            currentConnection = connectionPool.indexOf(connection);
            System.out.println("Create a new connection, currentConnection: " + currentConnection);
        }
        else
        {
            connection = connectionPool.get(currentConnection);
            System.out.println("Use a current connection, currentConnection: " + currentConnection + " pool size is:" + connectionPool.size());
            if(currentConnection<=connectionPool.size())
            {
                currentConnection=0;
            }
            else
            {
                currentConnection=currentConnection+1;
            }
            System.out.println("currentConnection updated to: " + currentConnection);
        }
        
        return connection;
    }

     public synchronized void resetConnectionPool() {
        
         System.out.println("resetConnectionPool");
         connectionPool = null;
         currentConnection = null;
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
