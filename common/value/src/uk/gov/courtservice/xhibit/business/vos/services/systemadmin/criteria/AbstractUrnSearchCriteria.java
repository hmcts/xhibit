package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import java.util.StringTokenizer;

/**
 * URN stands for Uniform Resource Naming (ie. a Stringified Key).
 * <p>
 * SearchCriteria of this type have the ability to search on the URN value.
 * </p>
 * <p>
 * See swiki page http://gbspsiad002:8888/Xhibit/483 (GD_Issue_14) for details.
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
public abstract class AbstractUrnSearchCriteria extends AbstractSearchCriteria {
	private static final long serialVersionUID = 1666760568217887454L;
    protected final static String URN_PREFIX = "//";

    protected final static String URN_SEPARATOR = "/";

    private String urn = null;

    private String[] tokens = new String[3];

    /**
     * Default constructor.
     */
    public AbstractUrnSearchCriteria() {
    }

    /**
     * Extract the 'short name' from the URN.
     * <p>
     * For a URN of "//CHELMS" or "//CHELMS/A/01", we extract 'CHELMS'.
     * </P>
     * 
     * @return String
     */
    protected String extractCourtShortName() {
        return tokens[0];
    }

    /**
     * Extract the 'site code' from the URN.
     * <p>
     * For a URN of "//CHELMS/A" or "//CHELMS/A/01", we extract 'A'.
     * </P>
     * 
     * @return String
     */
    protected String extractCourtSiteCode() {
        return tokens[1];
    }

    /**
     * Extract the 'court room no' from the URN.
     * <p>
     * For a URN of "//CHELMS/A/01", we extract '01'.
     * </P>
     * 
     * @return String
     */
    protected String extractCourtRoomNo() {
        return tokens[2];
    }

    public String getUrn() {
        return this.urn;
    }

    /**
     * Implementations of this should extract the search criteria from the URN
     * provided.
     * <p>
     * e.g.""//CHELMS/A/01" identifies the Chelmsford main building courtroom 1
     * and "//CHELMS/A" to identify the Chelmsford main building itself.
     * </p>
     */
    protected abstract void parseUrn();

    public void setUrn(String newValue) {
        this.debug("AbstractUrnSearchCriteria::setUrn[" + newValue + "]");
        this.urn = newValue;

        if (urn == null)
            return;

        String strippedUrn = getUrn().substring(2); // remove the '//'
        // prefix
        StringTokenizer tok = new StringTokenizer(strippedUrn, URN_SEPARATOR);
        for (int i = 0; i < 3 && tok.hasMoreTokens(); i++)
            tokens[i] = tok.nextToken();

        // Parse the URN
        this.parseUrn();
    }
}