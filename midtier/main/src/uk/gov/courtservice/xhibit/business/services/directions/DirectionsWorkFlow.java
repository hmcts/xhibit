package uk.gov.courtservice.xhibit.business.services.directions;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;

/**
 * <p>
 * Title: DirectionsWorkFlow
 * </p>
 * <p>
 * Description: This is the workflow class for directions.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version $Revision: 1.3 $
 */
public class DirectionsWorkFlow {
    private static final Logger log = CSServices.getLogger(DirectionsWorkFlow.class);

    private DirectionsWorkFlow() {
        // private constructor to prevent external instantiation...
    }

    /**
     * Method to save directions either for case of defendant
     * 
     * @param directionsValue
     *            a DirectionsValue object.
     * @exception CourtLogBusinessException
     */
    public static void saveDirections2(DirectionsValue directionsValue) throws CourtLogBusinessException {
        log.debug("saveDirections() : entered");

        // @todo Decide what other helpers/methods need calling
        DirectionsHelper.saveDirections(directionsValue);
    }
}