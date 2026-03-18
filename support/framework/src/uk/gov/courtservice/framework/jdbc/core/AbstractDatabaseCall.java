package uk.gov.courtservice.framework.jdbc.core;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * An abstract class used to hold all of the common code for accessing database
 * stored procedures/function. The methods provided allow for easier
 * testing/development (for example, the getDataSource() method, as can be
 * overridden to return a custom <code>DataSource</code>) and common
 * validation code.
 * 
 * @author tz0d5m
 * @version $Id: AbstractDatabaseCall.java,v 1.7 2006/10/20 11:01:39 rzvddy Exp $
 */
public abstract class AbstractDatabaseCall {
    /** The log4j <code>Logger</code> instance */
    protected final Logger log = CSServices.getLogger(getClass());

    // due to JDBC/Oracle limitations, need to define booleans as numbers...
    private static final Integer TRUE_INTEGER = new Integer(1);

    private static final Integer FALSE_INTEGER = new Integer(0);

    private final String dataSouceName;
    
    /**
     * Define the default constructor.
     */
    public AbstractDatabaseCall() {
        this(null);
    }
    
    /**
     * Define the default constructor.
     */
    public AbstractDatabaseCall(String dataSouceKey) {
       this.dataSouceName = dataSouceKey; 
    }

    /**
     * Method to acquire the <code>DataSource</code> to use to execute the flr
     * searches. Extracted and made protected to allow sub-classes to specify
     * custom <code>DataSource</code>s (e.g. one for testing).
     * 
     * @return The <code>DataSource</code> to use.
     */
    protected DataSource getDataSource() {
        return CSServices.getServiceLocator().getDataSource(dataSouceName);
    }

    /**
     * Due to the limitations of JDBC/Oracle handling of boolean values as
     * inputs/outputs to <code>CallableStatement</code>s and
     * <code>PreparedStatement</code>, the procedures need to handle boolean
     * values as numbers.
     * 
     * @param input
     *            The <code>boolean</code> value to convert to a number.
     * @return An <code>Integer</code> value of "1" if input was <i>true</i>,
     *         or a value of "0" if the input was <i>false</i>.
     */
    protected final Integer getBooleanAsInteger(final boolean input) {
        final Integer result = (input ? TRUE_INTEGER : FALSE_INTEGER);

        if (log.isDebugEnabled()) {
            log.debug("getBooleanAsInteger() - returning: " + result + " for input: " + input);
        }

        return result;
    }

    /**
     * Helper method used to validate that parameters are not <i>null</i>.
     * 
     * @param name
     *            The name of the parameter we are validating.
     * @param value
     *            The value we are ensuring is not <i>null</i>.
     */
    protected final void validateParameterNotNull(final String name, final Object value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }

    /**
     * Factory method used to create a <code>StoredFunction</code> object to
     * represent the database function whose name is passed in.
     * 
     * @param name
     *            The name of the function we will be wanting to call.
     * @return The newly created <code>StoredFunction</code>.
     */
    protected StoredFunction createStoredFunction(final String name) {
        return new StoredFunction(getDataSource(), name);
    }

    /**
     * Factory method used to create a <code>StoredProcedure</code> object to
     * represent the database function whose name is passed in.
     * 
     * @param name
     *            The name of the function we will be wanting to call.
     * @return The newly created <code>StoredProcedure</code>.
     */
    protected StoredProcedure createStoredProcedure(final String name) {
        return new StoredProcedure(getDataSource(), name);
    }

    /**
     * Converts a <code>Calendar</code> to a <code>Timestamp</code>.
     * @param cal a Calendar.
     * @return the Calendar as Timestamp.
     */
    protected Timestamp convertToTimestamp(Calendar cal) {
        Timestamp ts = null;
        if (cal != null) {
            ts = new Timestamp(cal.getTime().getTime());
        }
        return ts;
    }

    /**
     * Converts a <code>Date</code> to a <code>Timestamp</code>.
     * @param date a Calendar.
     * @return the Date as Timestamp.
     */
    protected Timestamp convertToTimestamp(Date date) {
        Timestamp ts = null;
        if (date != null) {
            ts = new Timestamp(date.getTime());
        }
        return ts;
    }
}
