package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSatelliteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the Court Site Location screen.
 * @author toftn
 *
 */
public class CourtSiteLocationModel {
	
	private XPanel callingClass;
	
	private CourtSiteComplexValue courtSiteComplexVal;

	public CourtSiteLocationModel() {
		clearmodel();
	}
	
	public CourtSiteLocationModel(XPanel callingClass) {
		setCallingClass(callingClass);
		clearmodel();
	}
	
	/**
	 * @return courtSiteComplexVal
	 */
	public CourtSiteComplexValue getCourtSite() {
		return courtSiteComplexVal;
	}

	/**
	 * @param courtSiteComplexVal
	 */
	public void setCourtSite(CourtSiteComplexValue courtSiteComplexVal) {
		this.courtSiteComplexVal = courtSiteComplexVal;
	}
	
	public void updateValueObject(String siteName, String siteCode, String address1, String address2, String address3,
			String address4, String town, String county, String postcode, String telNo, String faxNo,
			String isSatellite, String lcdCode, String tier, String notInUse, int siteGroup, String listName,
			String freeText, List<CourtRoomBasicValue> courtRooms) {

		courtSiteComplexVal.setCourtSiteName(siteName.trim());
		courtSiteComplexVal.setCourtSiteCode(siteCode);
		courtSiteComplexVal.setAddress1(address1);
		courtSiteComplexVal.setAddress2(address2);
		courtSiteComplexVal.setAddress3(address3);
		courtSiteComplexVal.setAddress4(address4);
		courtSiteComplexVal.setTown(town);
		courtSiteComplexVal.setPostcode(postcode);
		courtSiteComplexVal.setCounty(county);
		courtSiteComplexVal.setTelephoneNumber(telNo);
		courtSiteComplexVal.setFaxNumber(faxNo);
		setCourtSatellite(isSatellite, courtSiteComplexVal);
		courtSiteComplexVal.setCrestCourtId(lcdCode);
		courtSiteComplexVal.setFloaterText(freeText);
		courtSiteComplexVal.setListName(listName);
		courtSiteComplexVal.setSiteGroup(siteGroup);
		courtSiteComplexVal.setTier(tier);
		courtSiteComplexVal.setObsInd(notInUse);
		setCourtRooms(courtRooms);
	}
	
	public void createValueObject(String siteName, String siteCode, String address1, String address2, String address3,
			String address4, String town, String county, String postcode, String telNo, String faxNo,
			String isSatellite, String lcdCode, String tier, String notInUse, int siteGroup, String listName,
			String freeText, List<CourtRoomBasicValue> courtRooms) {
		CourtSiteComplexValue newValue = new CourtSiteComplexValue();
		setCourtSite(newValue);
		
		newValue.setCourtSiteName(siteName.trim());
		newValue.setDisplayName(siteName);
		newValue.setCourtSiteCode(siteCode);
		newValue.setAddress1(address1);
		newValue.setAddress2(address2);
		newValue.setAddress3(address3);
		newValue.setAddress4(address4);
		newValue.setTown(town);
		newValue.setPostcode(postcode);
		newValue.setCounty(county);
		newValue.setTelephoneNumber(telNo);
		newValue.setFaxNumber(faxNo);
		setCourtSatellite(isSatellite, newValue);
		newValue.setCrestCourtId(lcdCode);
		newValue.setFloaterText(freeText);
		newValue.setListName(listName);
		newValue.setSiteGroup(siteGroup);
		newValue.setTier(tier);
		newValue.setObsInd(notInUse);
		setCourtRooms(courtRooms);
	}
	
	/**
	 * Sets the CourtSatellite in the value object.
	 * 
	 * @param isSatellite
	 * @param complex
	 */
	public void setCourtSatellite(String isSatellite, CourtSiteComplexValue complex) {
		if (isSatellite.equals("Y")) {
			if (complex.getCourtSatellite() != null) {
				complex.getCourtSatellite().setObsInd("N");
			} else {
				CourtSatelliteBasicValue courtSatellite = getCourtSatelliteForInsert(complex);
				complex.setCourtSatellite(courtSatellite);
			}
		} else {
			if (complex.getCourtSatellite() != null) {
				complex.getCourtSatellite().setObsInd("Y");
			}
		}
	}
	
	public CourtSatelliteBasicValue getCourtSatelliteForInsert(CourtSiteComplexValue courtSite) {
		CourtSatelliteBasicValue retValue = new CourtSatelliteBasicValue();
		if (courtSite.getId() != null) {
			retValue.setCourtSiteId(courtSite.getId());
		}
		retValue.setDisplayName(courtSite.getDisplayName());
		retValue.setInternetSatelliteName(courtSite.getCourtSiteName());
		retValue.setObsInd("N");
		courtSite.setCourtSatellite(retValue);

		return retValue;
	}

	public void setCourtRooms(List<CourtRoomBasicValue> courtRooms) {
		this.courtSiteComplexVal.setCourtRooms(courtRooms);
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}
	
	public void clearmodel() {

    }	
}
