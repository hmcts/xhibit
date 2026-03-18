package uk.gov.courtservice.xhibit.business.vos.services.court;

import java.util.HashMap;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;

public class CourtStructureValue extends CSAbstractValue {
    private HashMap courtRoomsMap = new HashMap();

    private XhbCourtSiteBasicValue[] courtSites;

    private XhbCourtBasicValue court;
    
    private static final long serialVersionUID = 1617472985745156130L;

    public CourtStructureValue() {
    }

    public void setCourt(XhbCourtBasicValue court) {
        this.court = court;
    }

    public XhbCourtBasicValue getCourt() {
        return court;
    }

    public void setCourtSites(XhbCourtSiteBasicValue[] courtSites) {
        this.courtSites = courtSites;
    }

    public XhbCourtSiteBasicValue[] getCourtSites() {
        return courtSites;
    }

    @SuppressWarnings("unchecked")
	public void addCourtRooms(Integer courtSiteId, XhbCourtRoomBasicValue[] courtRooms) {
        courtRoomsMap.put(courtSiteId, courtRooms);
    }

    public XhbCourtRoomBasicValue[] getCourtRoomsForSite(Integer courtSiteId) {
        return (XhbCourtRoomBasicValue[]) courtRoomsMap.get(courtSiteId);
    }

    /**
     * Return a sorted list of a court rooms across all sites
     */
    public XhbCourtRoomBasicValue[] getAllCourtRooms() {
        if (getCourtSites().length == 1) {
            XhbCourtRoomBasicValue[] allCourtRooms = getCourtRoomsForSite(getCourtSites()[0].getCourtSiteId());
            Sorter.sort(allCourtRooms, new String[] { "courtSiteCode", "crestCourtRoomNo" }, Sorter.ASCENDING);
            return allCourtRooms;

        } else if (getCourtSites().length > 1) {
            int numberCourtRooms = 0;
            for (int i = 0; i < courtSites.length; i++) {
                numberCourtRooms += getCourtRoomsForSite(courtSites[i].getCourtSiteId()).length;
            }
            XhbCourtRoomBasicValue[] allCourtRooms = new XhbCourtRoomBasicValue[numberCourtRooms];

            int lastIndex = 0;
            int arrayLength;
            for (int i = 0; i < courtSites.length; i++) {
                arrayLength = getCourtRoomsForSite(courtSites[i].getCourtSiteId()).length;
                System.arraycopy(getCourtRoomsForSite(courtSites[i].getCourtSiteId()), 0, allCourtRooms, lastIndex,
                        arrayLength);
                lastIndex += arrayLength;
            }

            Sorter.sort(allCourtRooms, new String[] { "courtSiteCode", "crestCourtRoomNo" }, Sorter.ASCENDING);

            return allCourtRooms;
        } else {
            return new XhbCourtRoomBasicValue[] {};
        }
    }

}