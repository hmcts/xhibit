package uk.gov.courtservice.xhibit.business.services.directions;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;

/**
 * <p>
 * Title: Directions Controller
 * </p>
 * <p>
 * Description: Local interface to results session facade.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @ejb.bean name="DirectionsController" description="Directions Session Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="DirectionsControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @author Ian Hannaford
 * @version $Revision: 1.4 $
 */
public class DirectionsControllerBean extends CSSessionBean implements SessionBean {
    /**
     * Method to save directions either for case of defendant
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param directionsValue
     *            a DirectionsValue object
     * @throws CourtLogBusinessException
     */
    public void saveDirections(DirectionsValue directionsValue) throws CourtLogBusinessException {
        String methodName = "saveDirections()";
        log.debug(methodName + " : entered");

        try {
            DirectionsWorkFlow.saveDirections2(directionsValue);
        } catch (CourtLogBusinessException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " : failed! Transaction Rollback");
            throw e;
        }
    }
}