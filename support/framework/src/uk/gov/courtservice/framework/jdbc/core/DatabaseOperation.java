package uk.gov.courtservice.framework.jdbc.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sql.DataSource;

import uk.gov.courtservice.framework.jdbc.exception.UnableToReadSqlException;

/**
 * <p>
 * Title: DatabaseOperation
 * </p>
 * <p>
 * Description: Super class for all Database operations
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public abstract class DatabaseOperation {

    // JDBC helper to use
    protected JdbcHelper helper;

    // Parameter types
    protected int[] inTypes = new int[0];

    /**
     * Initializes the object
     * 
     * @param Datasource
     *            to use
     * @param SQL
     *            to use
     */
    public DatabaseOperation(DataSource ds, String sql) {
        helper = new JdbcHelper(ds, sql);
    }

    /**
     * Register the types of parameters
     * 
     * @param newTypes
     * @deprecated Use registerInTypes
     */
    public void registerParameterTypes(int[] newInTypes) {
        inTypes = newInTypes;
    }

    /**
     * Register the in types of parameters
     * 
     * @param newTypes
     */
    public void registerInTypes(int[] newInTypes) {
        inTypes = newInTypes;
    }

    /**
     * This is a utility method that can be used by sibclasses to read SQL from
     * the classpath
     * 
     * @param sqlResource
     * @return
     */
    protected static String readSql(String sqlResource) {

        BufferedReader reader = null;

        try {

            InputStream input = SqlOperation.class.getClassLoader().getResourceAsStream(sqlResource);
            if (input == null)
                throw new UnableToReadSqlException(sqlResource + " not found");

            reader = new BufferedReader(new InputStreamReader(input));

            StringBuffer sql = new StringBuffer();

            String temp = reader.readLine();
            while (temp != null) {
                sql.append(temp).append(" ");
                temp = reader.readLine();
            }

            return sql.toString();

        } catch (IOException ex) {
            throw new UnableToReadSqlException(ex.getMessage(), ex);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
        }
    }

}