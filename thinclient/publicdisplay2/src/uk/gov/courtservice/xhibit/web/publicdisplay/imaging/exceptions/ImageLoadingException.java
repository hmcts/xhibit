package uk.gov.courtservice.xhibit.web.publicdisplay.imaging.exceptions;

import java.net.URL;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.Fatal;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

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
 * @version $Revision: 1.3 $
 */
public class ImageLoadingException extends PublicDisplayRuntimeException implements Fatal {
    public ImageLoadingException(URL url) {
        super("Could not load the image at the URL '" + url + "'.");
    }
}
