package uk.gov.courtservice.xhibit.business.services.witness.exceptions;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * 
 */
public class NoDirectionsForCaseException extends CSBusinessException {
    public NoDirectionsForCaseException(Integer caseId) {
        super("witness.witness.nodirectionsforcase", new Object[] { caseId },
                "Error: Creation of a Skeleton Schedule requires entry of a Trial Time Estimate for"
                        + " the case (as part of Directions for Case).");
    }
}
