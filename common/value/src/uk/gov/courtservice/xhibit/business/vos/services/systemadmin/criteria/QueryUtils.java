package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * EJB QL utilities.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
public class QueryUtils {

    private static final String AND = " AND ";

    private static final String EQUALS = " = ";

    private static final String LIKE = " LIKE ";

    private static final String OBJECT_REFERENCE = "o"; // The 'o' in queries

    // like select object(o)
    // from RefDisposalMenu
    // o WHERE o.menuGroup =
    // '311'

    private static final String QUOTE = "'";

    private static final String WHERE = " WHERE ";

    private static final String WILDCARD = "%";

    private static Logger logger = null;

    /**
     * Default constructor.
     */
    public QueryUtils() {
        logger = CSServices.getLogger(QueryUtils.class.getClass());
    }

    /**
     * Given a String, search for the target character and insert the prefix
     * before it.
     * <p>
     * The resultant String is, of course, not the same instance that was
     * provided.
     * </p>
     * <p>
     * The types of characters that require attention are '%', '_' - but I dont
     * know the entire set.
     * </p>
     * 
     * <p>
     * NB.There was no comment here so here is my interpretation of what is
     * going on and why. I'm happy with the 'what', its the 'why' that I am not
     * 100% sure about.
     * </p>
     * 
     * @param queryString
     *            String the querystring to check for dodgy chars in. these are
     *            normally chars which will be prefixed like % or _
     * @param target
     *            the unwanted char
     * @param prefix
     *            the char to prefix
     * @return the clean string
     * @todo Find out the exact reason why ' has to be represented as '' in
     *       EJBQL
     */
    public static String prefixChar(String queryString, char target, char prefix) {
        String resultantString = queryString;
        for (int i = queryString.indexOf(target); i >= 0; i = queryString.indexOf(target, i)) {
            resultantString = queryString.substring(0, i) + prefix + queryString.substring(i);
            i = i + 2;
        }
        return resultantString;
    }

    /*
     * Writes a 'debug' message if the log is 'debug enabled'.
     */
    private void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    /**
     * An overload of the nect method excluding the object reference variable.
     * <p>
     * Here, we provide the default, 'o' but if the query has more than one
     * object, use explicit call supplying the right one.
     * </p>
     * 
     * @param queryBuffer
     *            StringBuffer the Query so far
     * @param attributeName
     *            String column name
     * @param value
     *            String value to include in the query
     * @return StringBuffer the modified query
     * @throws QueryUtilsException
     */
    public StringBuffer addWhereClause(StringBuffer queryBuffer, String attributeName, String value)
            throws QueryUtilsException {
        return this.addWhereClause(queryBuffer, OBJECT_REFERENCE, attributeName, value);
    }

    /**
     * Given an SQL Query string buffer, add a 'where' clause.
     * <p>
     * Changed method to accept the Query so that we can determine whether
     * 'WHERE' or 'AND' is required.
     * </p>
     * 
     * @param queryBuffer
     *            StringBuffer the Query so far
     * @param objectName
     *            String the name assigned to an object in the query
     * @param attributeName
     *            String column name
     * @param value
     *            String value to include in the query
     * @return StringBuffer the modified query
     * @throws QueryUtilsException
     */
    public StringBuffer addWhereClause(StringBuffer queryBuffer, String objectName, String attributeName, String value)
            throws QueryUtilsException {
        StringBuffer appendix = new StringBuffer();
        if (this.isPopulated(value)) {
            String objectReference = objectName + "." + attributeName;
            String cleanValue = prefixChar(value, QUOTE.charAt(0), QUOTE.charAt(0));
            if (queryBuffer.toString().indexOf(WHERE) < 0) { // We've not
                // been here
                // before; stick
                // 'Where' on
                // the end
                appendix.append(WHERE);
            } else {
                appendix.append(AND);
            }
            if (value.indexOf(WILDCARD) > -1) { // If there is a wild-card
                // character on the end, make it
                // a 'LIKE' clause
                appendix.append(objectReference + LIKE + QUOTE + cleanValue + QUOTE);
            } else {
                appendix.append(objectReference + EQUALS + QUOTE + cleanValue + QUOTE);
            }
        }
        return queryBuffer.append(appendix.toString());
    }

    /**
     * Check whether a value should be included in a query.
     * 
     * @param String
     *            queryParameterValue
     * @return boolean
     * @throws QueryUtilsException
     */
    private boolean isPopulated(String value) throws QueryUtilsException {
        this.debug(" QueryUtils.isPopulated [" + value + "]");
        if ((value == null) || (value.equals(""))) {
            return false;
        }
        /*
         * - I'm missing something here. This code picks up the wildcard
         * character inserted previously! So, I've commented it out... if
         * (value.indexOf("%") > 0) { Object[] parameterArray = new Object[] {
         * value }; String message = "Query parameter value (" + value + ")
         * contains invalid character(s)"; throw new
         * QueryUtilsException("systemadmin.queryutils.invalidcharacters",
         * parameterArray, message); }
         */
        return true;
    }
}