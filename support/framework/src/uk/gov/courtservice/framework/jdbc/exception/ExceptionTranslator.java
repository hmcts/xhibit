package uk.gov.courtservice.framework.jdbc.exception;

/**
 * <p>
 * Title: Exception translator
 * </p>
 * <p>
 * Description: This should be implemented by classes that translate
 * SQLException to JDBC framework Exceptions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: ExceptionTranslator.java,v 1.8 2006/06/05 12:30:17 bzjrnl Exp $
 */
public abstract class ExceptionTranslator {

    /** Translator used by the factory */
    private static final ExceptionTranslator translator;

    // static initializer for creating the translator
    static {
        translator = new ExceptionTranslator.DefaultExceptionTranslatorImpl();
    }

    /**
     * Translates the SQLException to the JDBC framework exception
     * 
     * @param SQL
     *            exception that needs to be translated
     * @return Data access exception specific to the JDBC framework
     */
    public abstract DataAccessException translate(Exception ex);

    /**
     * Translates the SQLException to the JDBC framework exception
     * 
     * @param SQL
     *            exception that needs to be translated
     * @param column
     * @return Data access exception specific to the JDBC framework
     */
    public abstract DataAccessException translate(Exception ex, String col);

    /**
     * Translates the SQLException to the JDBC framework exception
     * 
     * @param SQL
     *            exception that needs to be translated
     * @param column
     * @return Data access exception specific to the JDBC framework
     */
    public abstract DataAccessException translate(Exception ex, int col);

    /**
     * Returns the default implementation
     * 
     * @todo Should be able to configure the RTT externally
     * @return
     */
    public static ExceptionTranslator getInstance() {
        return translator;
    }

    // Default implementation with nesting
    private static class DefaultExceptionTranslatorImpl extends ExceptionTranslator {
        public DataAccessException translate(Exception ex) {
            return new DataAccessException(ex.getMessage(), ex);
        }

        public DataAccessException translate(Exception ex, String col) {
            return new DataAccessException(ex.getMessage(), ex, col);
        }

        public DataAccessException translate(Exception ex, int col) {
            return new DataAccessException(ex.getMessage(), ex, String.valueOf(col));
        }
    }
}
