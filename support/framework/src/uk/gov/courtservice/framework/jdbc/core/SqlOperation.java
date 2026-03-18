package uk.gov.courtservice.framework.jdbc.core;

import javax.sql.DataSource;

import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;

/**
 * <p>
 * Title: SqlOperation
 * </p>
 * <p>
 * Description: Super class for all SQL operations
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: SqlOperation.java,v 1.8 2013/11/15 16:19:53 hingstb Exp $
 */
public abstract class SqlOperation extends DatabaseOperation {
    /**
     * Initializes the object
     * 
     * @param Datasource
     *            to use
     * @param SQL
     *            to use
     */
    public SqlOperation(DataSource ds, String sql) {
        super(ds, sql);
    }

    /**
     * Executes the SQL operation
     * 
     * @param Arguments
     *            to the operation
     * @throws DataAccessException
     */
    public int execute(Object args[]) throws DataAccessException {
        final Parameter[] params = getParameters(args);
        return helper.execute(params);
    }

    /**
     * Executes the SQL operation
     * 
     * @param Arguments
     *            to the operation
     * @throws DataAccessException
     */
    public void executeUpdate(Object args[]) throws DataAccessException {
        final Parameter[] params = getParameters(args);
        helper.executeUpdate(params);
    }

    
    /**
     * Validation method for the parameters passed to the execute method.
     * 
     * @param parameters
     *            The <code>Object[]</code> to validate to ensure everything
     *            has been set up correctly.
     * @throws <code>IllegalArgumentException</code> if the passed in args
     *             variable is <i>null</i>, or the size of the array does not
     *             match that set up by the <code>registerInTypes</code>
     *             method.
     * 
     * @see #registerInTypes()
     */
    protected final void validateParameters(Object[] parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("parameters cannot be null");
        }

        if (parameters.length != inTypes.length) {
            throw new IllegalArgumentException("parameters - Expected " + inTypes.length + ", but got "
                    + parameters.length);
        }
    }

    /**
     * Method to construct a <code>Parameter</code> array using the types
     * specified in the inTypes array and the value passed in in the parameters
     * list.
     * 
     * This method will also validate to ensure that the parameters array passed
     * in is not null, and has the same number of entries as the inTypes array.
     * 
     * @param parameters
     *            An array of parameter values used to construct the
     *            <code>Parameter</code> array.
     * @return An array containing the <code>Parameter</code>s.
     * 
     * @throws <code>IllegalArgumentException</code> if the passed in args
     *             variable is <i>null</i>, or the size of the array does not
     *             match that set up by the <code>registerInTypes</code>
     *             method.
     */
    protected final Parameter[] getParameters(Object[] parameters) {
        validateParameters(parameters);

        // Create the parameter array
        final Parameter[] params = new Parameter[inTypes.length];

        for (int i = 0; i < params.length; i++) {
            // Set the parameter
            params[i] = Parameter.getInParameter(inTypes[i], parameters[i]);
        }

        return params;
    }

    /**
     * Method to construct a <code>Parameter</code> array using the types
     * specified in the inTypes array and the value passed in in the parameters
     * list. This is pre-pended with the output parameter whose type is
     * specified in the outputType method parameter.
     * 
     * This method will also validate to ensure that the parameters array passed
     * in is not null, and has the same number of entries as the inTypes array.
     * 
     * @param parameters
     *            An array of parameter values used to construct the
     *            <code>Parameter</code> array.
     * @param outputType
     *            The type of the output from the function.
     * @return An array containing the <code>Parameter</code>s.
     * 
     * @throws <code>IllegalArgumentException</code> if the passed in args
     *             variable is <i>null</i>, or the size of the array does not
     *             match that set up by the <code>registerInTypes</code>
     *             method.
     */
    protected final Parameter[] getParameters(Object[] parameters, int outputType) {
        validateParameters(parameters);

        // Create the parameter array
        final Parameter[] params = new Parameter[inTypes.length + 1];

        // Set the first parameter as output...
        params[0] = Parameter.getOutParameter(outputType);
        for (int i = 1; i < params.length; i++) {
            // Set the parameters
            params[i] = Parameter.getInParameter(inTypes[i - 1], parameters[i - 1]);
        }

        return params;
    }
}
