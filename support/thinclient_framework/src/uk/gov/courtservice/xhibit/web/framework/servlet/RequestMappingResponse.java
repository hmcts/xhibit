package uk.gov.courtservice.xhibit.web.framework.servlet;

/**
 * <p>
 * Title: Request Mapping Response
 * </p>
 * <p>
 * Description: This holds information about a request mapping response
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.6 $ $Log:
 *         RequestMappingResponse.java,v $ Revision 1.4 2003/10/01 15:31:11
 *         bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.3 2003/03/21 11:48:27 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:05 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:44 fz0n8j Added CVS log comments - ecawley
 * 
 */

public class RequestMappingResponse {

    /**
     * The name of the request response
     */
    private final String name;

    /**
     * The title key (key into messages resource bundle)
     */
    private final String titleKey;

    /**
     * The page key (key into Pages resource bundle)
     */
    private final String pageKey;

    /**
     * The response type
     */
    private final String type;

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param newName
     *            the name of the request mapping response
     * @param newTitleKey
     *            the title key of the title to show if this response is
     *            returned
     * @param newPageKey
     *            the page key of the page to show if this response is returned
     * @param newType
     *            the response type to use if this response is returned
     * @throws IllegalArgumentException
     *             if any of the parameters are null
     */
    public RequestMappingResponse(String newName, String newTitleKey, String newPageKey, String newType)
            throws IllegalArgumentException {
        if (newName == null) {
            throw new IllegalArgumentException("newName");
        }
        if (newTitleKey == null) {
            throw new IllegalArgumentException("newTitleKey");
        }
        if (newPageKey == null) {
            throw new IllegalArgumentException("newPageKey");
        }
        if (newType == null) {
            throw new IllegalArgumentException("newType");
        }
        name = newName;
        titleKey = newTitleKey;
        pageKey = newPageKey;
        type = newType;
    }

    /**
     * Returns the name of the request response.
     * 
     * @return the name of the request response
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the page key of the request response.
     * 
     * @return the page key of the request response
     */
    public String getPageKey() {
        return pageKey;
    }

    /**
     * Returns the title key of the request response.
     * 
     * @return the title key of the request response
     */
    public String getTitleKey() {
        return titleKey;
    }

    /**
     * Returns the response type.
     * 
     * @return the response type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns a string representation of the object. In general, the toString
     * method returns a string that "textually represents" this object. The
     * result should be a concise but informative representation that is easy
     * for a person to read.
     * 
     * @return a string representation of the object.
     */
    public String toString() {
        return getName() + ": " + getType() + " " + getPageKey() + " (" + getTitleKey() + ")";
    }

    /**
     * Get formated debug information about the object
     * 
     * @param indent
     *            the amount to indent the text
     * @return a String repsentation of the object useful for debuging
     */
    public String toDebug(String indent) {
        return indent + toString();
    }

}
