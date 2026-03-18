package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Search Criteria are the one parameter passed to the various Reference Data
 * finder methods.
 * <p>
 * This Abstract Class holds the attributes common to all search criteria, ID
 * and Detail Indicator.
 * </p>
 * <p>
 * It can also construct the Query for the database. This feature means that
 * this is the ONE place that knows the specifics about the search.
 * </p>
 * <p>
 * It might be worth considering splitting out the Query Creation from the
 * attribute holder because this feature does make this a 'little bit more than
 * a Value Object'.
 * </p>
 * <p>
 * Added criterion 'obsInd and defaulted value to 'N'. I have not put its name
 * in the regular AttributeNames Interface because this would interfere with the
 * concrete class implementations of that Interface.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
public abstract class AbstractSearchCriteria implements Serializable {
	private static final long serialVersionUID = -5433567113454942092L;
    public static final String ADDRESS = "ADDRESS";

    private static final String CLASS_NAME = "AbstractSearchCriteria ";

    private static final String ATTRIBUTE_NAME_OBSIND = "obsInd";

    private static Logger logger = CSServices.getLogger(AbstractSearchCriteria.class.getClass());

    // I wish I'd made this a Boolean! - anything other than null is
    // considered a 'Complex' return type
    private String detailIndicator = null;

    /** @todo How should we treat compound Primary Keys. */
    private Integer primaryKey = null;

    private QueryUtils queryUtils = null;

    /*
     * Each criterion is stored as an attribute, keyed by the DB column name (in
     * EJB QL)
     */
    private Map attributes = null;

    /**
     * Default constructor.
     */
    public AbstractSearchCriteria() {
    }

    private Map getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap(); // HashMap allows null values
        }
        return this.attributes;
    }

    public String getAttribute(String name) {
        return (String) this.getAttributes().get(name);
    }

    public String getDetailIndicator() {
        return this.detailIndicator;
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    public Integer getPrimaryKey() {
        return this.primaryKey;
    }

    protected QueryUtils getQueryUtils() {
        if (queryUtils == null) {
            this.debug(CLASS_NAME + "Lazy initialise this.queryUtils");
            queryUtils = new QueryUtils();
        }
        return queryUtils;
    }

    public abstract String getTableName();

    public boolean isBasicRequest() {
        return this.getDetailIndicator() == null;
    }

    public String queryString() throws QueryUtilsException {
        StringBuffer queryBuffer = new StringBuffer("select object(o) from " + this.getTableName() + " o ");
        Iterator allEntries = this.getAttributes().entrySet().iterator();
        Map.Entry eachEntry = null;
        while (allEntries.hasNext()) {
            eachEntry = (Map.Entry) allEntries.next();
            if (eachEntry.getValue() != null) {
                this.getQueryUtils().addWhereClause(queryBuffer, eachEntry.getKey().toString(),
                        eachEntry.getValue().toString());
            }
        }

        if (this instanceof Obsoletable && this.getAttribute(ATTRIBUTE_NAME_OBSIND) == null) {
            this.debug("The search criteria implements obsoletable but obsInd is not set");
            // check if we have a where clause
            if (queryBuffer.toString().indexOf(" WHERE ") < 0)
                queryBuffer.append(" WHERE (o.obsInd IS NULL OR NOT o.obsInd = 'Y')");
            else
                queryBuffer.append(" AND (o.obsInd IS NULL OR NOT o.obsInd = 'Y')");
        }
        String queryString = queryBuffer.toString();
        this.debug("Built query: " + queryString);
        return queryString;
    }

    public void setAttribute(String name, String value) {
        this.getAttributes().put(name, value);
    }

    public void setDetailIndicator(String newValue) {
        this.debug(CLASS_NAME + "::setDetailIndicator[" + newValue + "]");
        this.detailIndicator = newValue;
    }

    /**
     * @deprecated Use setPrimaryKey
     */
    public void setId(Integer newValue) {
        this.setPrimaryKey(newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public void setPrimaryKey(Integer newValue) {
        this.debug(CLASS_NAME + "::setPrimaryKey[" + newValue + "]");
        this.primaryKey = newValue;
    }

    /**
     * The attribute is deemed to be 'wild' by the concrete class; that is
     * values are allowed to be partially filled.
     * <p>
     * If the value ends in a splat ('*') translate it into a DB wildcard '%'.
     */
    public void setWildAttribute(String name, String value) {
        String wildValue = value.replace('*', '%');
        this.setAttribute(name, wildValue);
    }

    /**
     * If the logger is allowing debug messages, write one.
     * <p>
     * When used by sub-classes, the messages ae logged against java.lang.Class.
     * </p>
     * 
     * @param message
     *            String
     */
    protected void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public String toString() {
        String result = null;
        Integer key = this.getPrimaryKey();
        if (key == null) {
            StringBuffer attributeBuffer = new StringBuffer("Attributes: ");
            Iterator allEntries = this.getAttributes().entrySet().iterator();
            Map.Entry eachEntry = null;
            while (allEntries.hasNext()) {
                eachEntry = (Map.Entry) allEntries.next();
                attributeBuffer.append(" '");
                attributeBuffer.append(eachEntry.getKey());
                attributeBuffer.append("' -> '");
                attributeBuffer.append(eachEntry.getValue());
                attributeBuffer.append("' ");
            }
            result = attributeBuffer.toString();
        } else {
            result = "Primary Key: " + key.toString();
        }
        return result;
    }

    /**
     * This method should be implemented by the sub classes. The only reason
     * this is abstract is to keep the sub classes compiling.
     * 
     * @return
     */
    public Object[] getArgs() {
        throw new UnsupportedOperationException("Implement in the subclass");
    }

    /**
     * This method should be implemented by the sub classes if specific
     * arguments need to be excluded from GUI validation. If this is not
     * overridden then all arguments will be validated by GUI.
     * 
     * @return
     */
    public Object[] getIncludeArgs() {
        return getArgs();
    }
}
