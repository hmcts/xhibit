package uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.address.Address;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface RefSolicitorFirmHome extends EJBLocalHome {

    public RefSolicitorFirm create(Address address, Integer courtId, Integer crestSofId, String dxRef, String obsInd,
            String shortName, String laCode, String solicitorFirmName, String vatNo, String userDisplayName) throws CreateException;

    public RefSolicitorFirm findByPrimaryKey(Integer refSolicitorFirmId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}