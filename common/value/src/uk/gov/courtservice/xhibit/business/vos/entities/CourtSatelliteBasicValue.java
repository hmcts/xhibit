package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Court Satellite Basic Value.
 * 
 * @author grewalg
 *
 */
public class CourtSatelliteBasicValue extends CSAbstractValue{

	private String internetSatelliteName = null;

    private Integer courtSiteId = null;

    private String displayName;

    private String obsInd;
    /**
	 * 
	 */
	private static final long serialVersionUID = -2898890282283039347L;

	/**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtSatelliteBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
    public CourtSatelliteBasicValue() {
    }

	/**
	 * @return the internetSatelliteName
	 */
	public String getInternetSatelliteName() {
		return internetSatelliteName;
	}

	/**
	 * @param internetSatelliteName the internetSatelliteName to set
	 */
	public void setInternetSatelliteName(String internetSatelliteName) {
		this.internetSatelliteName = internetSatelliteName;
	}

	/**
	 * @return the courtSiteId
	 */
	public Integer getCourtSiteId() {
		return courtSiteId;
	}

	/**
	 * @param courtSiteId the courtSiteId to set
	 */
	public void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the obsInd
	 */
	public String getObsInd() {
		return obsInd;
	}

	/**
	 * @param obsInd the obsInd to set
	 */
	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}
