package uk.gov.courtservice.xhibit.business.entities.solicitor;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface SolicitorHome extends EJBLocalHome {

    public Solicitor create(String crestSolicitorName, String isInCrest, String obsInd) throws CreateException;

    public Solicitor findByPrimaryKey(Integer solicitorId) throws FinderException;
    /*
     * public Collection findByCourtId(Integer courtId) throws FinderException;
     */
}