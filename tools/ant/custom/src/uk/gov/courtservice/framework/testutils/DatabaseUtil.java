package uk.gov.courtservice.framework.testutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>
 * Title: Utility class to help running tests against the database.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class contains methods helpful to unit testing against a database.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class DatabaseUtil {
    /**
     * Utility method provided to execute scripts.
     * 
     * @param scriptURL
     *            The URL of the script.
     * @param delimiter
     *            The character delimiter to use.
     * @param sqlConnection
     *            The sql connection to use.
     * @throws IOException
     *             When there is a problem with loading the script.
     * @throws SQLException
     *             When there is a database problem.
     */
    public static void executeScript(URL scriptURL, int delimiter, Connection sqlConnection) throws IOException,
            SQLException {
        // get the contents of the file
        URLConnection urlConnection = scriptURL.openConnection();
        urlConnection.setDoInput(true);
        InputStream urlInputStream = urlConnection.getInputStream();
        // tokenize.
        StreamTokenizer streamTokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(urlInputStream)));
        // Initially set all characters as word characters.
        streamTokenizer.wordChars(0, 255);
        // treat EOL as a token
        streamTokenizer.eolIsSignificant(true);
        // the delimiter is not a word character and will be treated as a
        // separate token.
        streamTokenizer.ordinaryChar(delimiter);

        Statement stmt = sqlConnection.createStatement();

        // Step through the tokens.
        int nextToken = streamTokenizer.nextToken();
        StringBuffer currentStatement = new StringBuffer();
        while (nextToken != StreamTokenizer.TT_EOF) {
            if (nextToken == StreamTokenizer.TT_WORD)
                currentStatement.append(streamTokenizer.sval);
            else if (nextToken == StreamTokenizer.TT_EOL)
                currentStatement.append("\n");
            else if (nextToken == delimiter) {
                // Add the statement to the batch execution queue.
                stmt.addBatch(currentStatement.toString());
                // Start afresh...
                currentStatement = new StringBuffer();
            }
            nextToken = streamTokenizer.nextToken();
        }

        // Execute the script as a batch.
        stmt.executeBatch();
    }

    /**
     * Directly execute some SQL using the <code>Connection</code> Object
     * passed in.
     * <p>
     * This should only be used for <code>INSERT</code>, <code>UPDATE</code>
     * or <code>DELETE</code> statements.
     * 
     * @param sqlConnection
     *            The sql connection to use.
     * @param sql
     *            the sql string to execute...
     * @return The result from the executeUpdate call on the statement, either
     *         the row count for <code>INSERT</code>,<code>UPDATE</code>
     *         or <code>DELETE</code> statements, or 0 for SQL statements that
     *         return nothing
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     * @throws java.sql.SQLException
     *             If any database related error occurs
     */
    public static int executeSql(final Connection sqlConnection, final String sql) throws SQLException {
        Statement stmt = null;

        try {
            stmt = sqlConnection.createStatement();
            return stmt.executeUpdate(sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
