package uk.gov.courtservice.framework.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: TestUtils
 * </p>
 * <p>
 * Description: Utilities to support unit testing
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 * 
 * Faisal Shoukat - added amendements to incorporate properties file Jon Powell -
 * copy of original file from support/framework, with references to framework
 * removed
 */
public class TestUtils {
    public static final String TEST_PROPERTIES = "/config/test/testprops";

    private static final Logger log = Logger.getLogger(TestUtils.class);

    public static final Properties props = new Properties();

    /**
     * Construct an instance of test utils loading the properties.
     */
    public TestUtils() {
        try {
            loadTestProperties();
        } catch (IOException ioe) {
            log.error("Unable to load proeprties file");
            ioe.printStackTrace();
        }
    }

    /**
     * Load the test properties.
     * 
     * @return the newly loaded properties.
     */
    public static Properties loadTestProperties() throws IOException// ,
    // CSConfigurationException
    {
        String file = TEST_PROPERTIES + ".properties";
        log("path of file = " + file);
        InputStream is = TestUtils.class.getResourceAsStream(file);
        if (is == null) {
            log.error("file not found: file=" + file);
            String msg = "file not found: file=" + file;
            throw new IOException(msg);
        }
        log("loading props file");
        props.load(is);

        return props;
    }

    /**
     * Retrieve an instance of InitialContext to support JNDI lookups.
     * 
     * @return a properly configured initial context.
     */
    public static Context getInitialContext() {
        String url = null;
        String ctx_factory = null;
        String user = null;
        String password = null;
        Properties properties = null;
        try {
            loadTestProperties();
            url = props.getProperty("JNDI.PROVIDER_URL");
            log("url = " + url);
            ctx_factory = props.getProperty("INITIAL_CONTEXT_FACTORY");
            log("factory = " + ctx_factory);
            properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, ctx_factory);
            properties.put(Context.PROVIDER_URL, url);
            if (user != null) {
                properties.put(Context.SECURITY_PRINCIPAL, user);
                properties.put(Context.SECURITY_CREDENTIALS, password == null ? "" : password);
            }
        } catch (IOException ioe) {
            log.error("IOException caught when loading props");
            ioe.printStackTrace();
        }

        try {
            return new InitialContext(properties);
        } catch (NamingException nme) {
            log.error("naming exception caught");
            nme.printStackTrace();
        }

        return null;
    }

    /**
     * Bob Boothby - Sanity checking this method says to me that it could never
     * work as the connection is closed before the resultset can be used.
     * 
     * @param sql
     *            the sql string to execute...
     * @return the resultset created by this query...
     */
    //
    // There is a new function in
    // uk.gov.courtservice.framework.testutils.DatabaseUtil
    // named executeSql(). This method should be used in place of this for
    // insert, updates or deletes, and it returns an int showing the number
    // of
    // rows altered. You are required to pass in your own connection.
    // The reasoning behind this is as mentioned in Bob's comment above, and
    // for clarity
    //
    public static ResultSet execSql(String sql) throws Exception {
        loadTestProperties();
        ResultSet rs = null;
        Connection conn = null;
        // String datasource = props.getProperty("xhibit.datasourcename");
        String datasource = "XhibitOracleTxDataSource";
        log("datasource name = " + datasource);
        DataSource ds = (javax.sql.DataSource) getInitialContext().lookup(datasource);
        log("done lookup to database");
        try {
            conn = ds.getConnection();
            System.out.println(".execSQL(): " + sql);
            Statement st = conn.createStatement();
            log("statement = " + st);
            rs = st.executeQuery(sql);
            // log("rset " + rs);
            st.close();

        }

        finally {
            if (conn != null)
                conn.close();
        }

        return rs;
    }

    /*
     * This method converts the string returned by toString() of ValueObject to
     * a formatted ordered string, which is used in JUnit test for ValueObject's
     * toString method. @param fields is an array of field names in the order
     * that we expect them to be output. @param values is a string of the
     * format: "\nfield=value\n---field=value\n...\n---field=value" @return a
     * string of the format: "field=value;field=value;...;field=value"
     */
    public static String orderString(String[] fields, String values) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            int index = values.indexOf(fields[i]);
            if (index == -1) {
                continue;
            }
            int start = values.indexOf("=", index);
            int end = values.indexOf("\n", index);
            if (end == -1) {
                str.append(fields[i]).append(values.substring(start).trim());
            } else {
                str.append(fields[i]).append(values.substring(start, end).trim()).append(";");
            }

        }

        return str.toString();
    }

    private static void log(String msg) {
        log.debug(msg);
    }
}