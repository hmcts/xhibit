package uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions;

/**
 * <p>
 * Title: Rotation Set not found exception
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
 * @version $Id: RotationSetNotFoundCheckedException.java,v 1.4 2004/04/07
 *          15:34:02 sz0t7n Exp $
 */

public class RotationSetNotFoundCheckedException extends PublicDisplayCheckedException {
	
	static final long serialVersionUID = -2260208483685057582L;
	
    private static final String errorKey = "publicdisplay.rotationsetnotfound";

    private static final String errorLog = "Rotation set not found for id: ";

    public RotationSetNotFoundCheckedException() {
        super(errorKey, "Rotation Set not found");
    }

    public RotationSetNotFoundCheckedException(Integer rotationSetId) {
        super(errorKey, new Object[] { rotationSetId }, errorLog + rotationSetId);
    }

    public RotationSetNotFoundCheckedException(Integer rotationSetId, Throwable throwable) {
        super(errorKey, new Object[] { rotationSetId }, errorLog + rotationSetId, throwable);
    }

}