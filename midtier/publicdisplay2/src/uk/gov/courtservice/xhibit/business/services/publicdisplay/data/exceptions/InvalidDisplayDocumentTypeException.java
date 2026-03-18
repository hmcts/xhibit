package uk.gov.courtservice.xhibit.business.services.publicdisplay.data.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions.DataSourceException;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.5 $
 */
public class InvalidDisplayDocumentTypeException extends DataSourceException {
	
	static final long serialVersionUID = 4248775735639782376L;
	
    /**
     * Creates a new InvalidDisplayDocumentTypeException object.
     * 
     * @param typethe
     *            display document type for which there is no associated
     *            DataSource.
     */
    public InvalidDisplayDocumentTypeException(DisplayDocumentType type) {
        super("There is no DataSource associated with the type '" + type + "'.");
    }
}
