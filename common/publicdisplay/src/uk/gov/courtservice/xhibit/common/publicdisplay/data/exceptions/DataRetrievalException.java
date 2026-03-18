package uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions;

import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayFailureException;

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
public class DataRetrievalException extends PublicDisplayFailureException {
	
	static final long serialVersionUID = -9080538249223354097L;
	
    public DataRetrievalException(FinderException e) {
        super(
                "Could not locate the local home for the EJB; this error maybe unrecoverable and may require a restart of the application server.",
                e);
    }
}
