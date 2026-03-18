package uk.gov.courtservice.xhibit.business.entities.courtsite;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.court.Court;

public interface CourtSiteHome extends javax.ejb.EJBLocalHome {
    public static final String COMP_NAME = "java:comp/env/ejb/courtSite";

    public CourtSite create(String courtSiteName, String courtSiteCode, String displayName, Integer addressId, Court court,
            String obsInd, String shortName, String userDisplayName, String crestCourtId, String floaterText, String listName,
            Integer siteGroup, String tier) throws CreateException;

    public CourtSite findByPrimaryKey(Integer courtSiteId) throws FinderException;
    
    public Collection findAllCourtSites(Integer courtId) throws FinderException;
    
    public Collection findHomeCourtAndSatellite(Integer courtId) throws FinderException;
    
    public Collection findByCrestCourtId(Integer courtId) throws FinderException;
}