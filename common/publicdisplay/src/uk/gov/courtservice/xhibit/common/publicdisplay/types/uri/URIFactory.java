package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions.UnsupportedURIException;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class URIFactory {

    public static AbstractURI create(String uriString) {
        if (uriString.startsWith("pd://document")) {
            return new DisplayDocumentURI(uriString);
        } else if (uriString.startsWith("pd://display")) {
            return new DisplayURI(uriString);
        } else {
            throw new UnsupportedURIException("The uri " + uriString + " is not a recognized URI type.");
        }
    }
}
