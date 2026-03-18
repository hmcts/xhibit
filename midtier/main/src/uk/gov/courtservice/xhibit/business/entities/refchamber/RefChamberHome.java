package uk.gov.courtservice.xhibit.business.entities.refchamber;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface RefChamberHome extends EJBLocalHome {

    public RefChamber create(Integer crestChamberId, String dxRef, String firmName, String isGlobal,
            String locationCode, String obsInd, String userDisplayName, String clerkName) throws CreateException;

    public RefChamber findByPrimaryKey(Integer refChamberId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
    
    public Collection findAllChambersByCrestChamberIdCourtId(Integer crestChamberId, Integer courtId) throws FinderException;
    
    public Collection findChamberByCrestChamberIdCourtId(Integer crestChamberId, Integer courtId) throws FinderException;
    
    public Collection findChamberByFirmNameCourtId(String firmName, Integer courtId) throws FinderException;
    
    public Collection findChamberByFirmNameCrestChamberIdCourtId(String firmName, Integer crestChamberId, Integer courtId) throws FinderException;
}