package uk.gov.courtservice.ant.taskdefs.storedprocedure;

/**
 * 
 * @title NamingStrategy
 * @description
 *        <p>
 *        This is a strategy class for generating Java class names from stored
 *        procedures
 *        </p>
 * @author Meeraj
 */
public abstract class NamingStrategy {

    /**
     * Creates a Java class name from the stored procedure name.
     * 
     * @param Stored
     *            procedure name
     * @return Java class name
     */
    public abstract String getJavaName(String spName);

    /**
     * This method currently returns the simple naming strategy. This should be
     * altered when new strategies are defined.
     * 
     * @return Strategy name
     */
    public static NamingStrategy getStrategy() {
        return new SimpleNamingStrategy();
    }

}