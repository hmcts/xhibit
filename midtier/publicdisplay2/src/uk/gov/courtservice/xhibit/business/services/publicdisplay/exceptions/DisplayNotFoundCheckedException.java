package uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions;

/**
 * <p>
 * Title: Dispaly not found exception
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DisplayNotFoundCheckedException.java,v 1.2 2004/03/29 15:59:45
 *          pznwc5 Exp $
 */

public class DisplayNotFoundCheckedException extends PublicDisplayCheckedException {
	
	static final long serialVersionUID = -4161603114289391442L;
	
    private static final String errorKey = "publicdisplay.displaynotfound";

    private static final String errorLog = "XhbDisplay not found for id: ";

    public DisplayNotFoundCheckedException() {
        super(errorKey, "Display not found");
    }

    public DisplayNotFoundCheckedException(Integer displayId) {
        super(errorKey, new Object[] { displayId }, errorLog + displayId);
    }

    public DisplayNotFoundCheckedException(Integer displayId, Throwable throwable) {
        super(errorKey, new Object[] { displayId }, errorLog + displayId, throwable);
    }

}
