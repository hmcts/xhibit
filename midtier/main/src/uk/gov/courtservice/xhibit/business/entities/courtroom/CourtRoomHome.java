package uk.gov.courtservice.xhibit.business.entities.courtroom;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;

public interface CourtRoomHome extends javax.ejb.EJBLocalHome {

    public CourtRoom create(CourtSite courtSite, String courtRoomName, String description, Integer crestCourtRoomNo, String obsInd, String userDisplayName,
    		String securityInd, String videoInd)
            throws CreateException;

    public CourtRoom findByPrimaryKey(Integer courtRoomId) throws FinderException;
}