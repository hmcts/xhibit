package uk.gov.courtservice.ant.taskdefs.storedprocedure;

/**
 * 
 * @title NamingStrategy
 * @description
 *        <p>
 *        This is a strategy class for generating Java class names from stored
 *        procedures. The class assumes the stored procedure name is of the
 *        format <package>.GET_<table>. For example if the stored procedure
 *        name is <code>XHIBIT_SEARCH_PKG.GET_COURT</code> and the name will
 *        be Court.
 *        </p>
 * @author Meeraj
 */
public class SimpleNamingStrategy extends NamingStrategy {

    /**
     * Creates a Java class name from the stored procedure name.
     * 
     * @param Stored
     *            procedure name
     * @return Java class name
     */
    public String getJavaName(String spName) {

        if (spName == null) {
            throw new IllegalArgumentException("spName");
        }

        // Strip out the package from the fully-qualified name
        int dotIndex = spName.indexOf(".");
        if (dotIndex == -1) {
            throw new IllegalArgumentException("spName");
        }
        String procName = spName.substring(dotIndex + 1);

        // Strip out the first four characters (GET_)
        procName = procName.substring(4);

        return convertProcName(procName);

    }

    // Converts proc name to a class name
    private static final String convertProcName(final String procName) {
        final StringBuffer returnBuffer = new StringBuffer(procName.length());
        // always convert the first character
        boolean toUpper = true;
        char charAt;

        for (int i = 0; i < procName.length(); i++) {
            charAt = procName.charAt(i);

            if (charAt == '_') {
                toUpper = true;
            } else if (toUpper) {
                returnBuffer.append(Character.toUpperCase(charAt));
                toUpper = false;
            } else {
                returnBuffer.append(Character.toLowerCase(charAt));
            }
        }

        // convert to a String once, for printing and returning
        final String returnString = returnBuffer.toString();
        // couldn't we do something better than System.out???
        System.out.println(procName + " --> " + returnString);

        return returnString;
    }

}