package uk.gov.courtservice.xhibit.business.entities.court;

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
public interface CourtHome extends EJBLocalHome {

    public static final String COMP_NAME = "java:comp/env/ejb/court";

    /*
     * - replaced with that below, but I'm not sure if I should do it the other
     * way, ie. copy the order in the commented create to the Bean ;o( public
     * Court create(String courtType, String circuit, String courtName, String
     * crestCourtId, String courtPrefix, String shortName, String
     * crestIpAddress) throws CreateException;
     */
    public Court create(String courtType, String circuit, String courtName, String crestCourtId, String crestIpAddress, Address address,
            String courtPrefix, String shortName, String obsInd, String courtCode, String courtStartTime, String userDisplayName,
            String dxRef, String countyLocCode, String tier) throws CreateException;

    public Court findByPrimaryKey(Integer courtId) throws FinderException;

    public Court findByShortName(String shortName) throws FinderException;

    public Collection findAll() throws FinderException;

}