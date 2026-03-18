package uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface RefLegalRepresentativeHome extends EJBLocalHome {

    public RefLegalRepresentative create(String firstName, String middleName, String surname, String title,
            String initials, String legalRepType, String obsInd, CSEntityLocal court, String userDisplayName) throws CreateException;

    public RefLegalRepresentative findByPrimaryKey(Integer refLegalRepId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}