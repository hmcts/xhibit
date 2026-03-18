package uk.gov.courtservice.xhibit.business.entities.courtroom;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;

abstract public class CourtRoomBean extends CSEntityBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7668299051302814269L;

	public Integer ejbCreate(CourtSite courtSite, String courtRoomName, String description, Integer crestCourtRoomNo, String obsInd,
			String userDisplayName, String securityInd, String videoInd) throws CreateException {
		setCourtRoomName(courtRoomName);
		setDescription(description);
		setCrestCourtRoomNo(crestCourtRoomNo);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		setObsInd(obsInd);
		setDisplayName(description);
		setSecurityInd(securityInd);
		setVideoInd(videoInd);
		return null;
	}

	public void ejbPostCreate(CourtSite courtSite, String courtRoomName, String description, Integer crestCourtRoomNo, String obsInd,
			String userDisplayName, String securityInd, String videoInd) throws CreateException {
		setCourtSite(courtSite);
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract Integer getCourtRoomId();

	public abstract String getCourtRoomName();

	public abstract String getDescription();

	public abstract Integer getCrestCourtRoomNo();

	public abstract String getDisplayName();

	public abstract String getObsInd();
	
	public abstract String getSecurityInd();

	public abstract String getVideoInd();

	public abstract void setCourtRoomId(Integer courtRoomId);

	public abstract void setCourtRoomName(String courtRoomName);

	public abstract void setDescription(String description);

	public abstract void setCrestCourtRoomNo(Integer crestCourtRoomNo);

	public abstract void setDisplayName(String displayName);

	public abstract void setObsInd(String obsInd);
	
	public abstract void setSecurityInd(String securityInd);

	public abstract void setVideoInd(String videoInd);

	// ------------------------------CMR
	// Fields------------------------------------
	public abstract CourtSite getCourtSite();

	public abstract void setCourtSite(CourtSite courtSite);

	// -------------------------------Business Method
	// -----------------------------
	public String getUrn() {
		String courtSiteCode = getCourtSite().getCourtSiteCode();
		String courtShortName = getCourtSite().getCourt().getShortName();

		return "//" + courtShortName + "/" + courtSiteCode + "/" + getCrestCourtRoomNo();
	}

}