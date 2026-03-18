package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;

/**
 * Common superclass for public display data classes
 * 
 * @author pznwc5
 */
public class PublicDisplayValue extends AbstractValue implements Comparable<PublicDisplayValue> {
	
	static final long serialVersionUID = -1321945076202443846L;
	
	private static final Logger log = CSServices.getLogger(PublicDisplayValue.class);

    /**
     * Name of the court room
     */
    private String courtRoomName;

    /**
     * Moved from court room
     */
    private String movedFromCourtRoomName;

    /**
     * Moved from court site short name
     */
    private String movedFromCourtSiteShortName;

    /**
     * Id of the court room
     */
    private int courtRoomId;

    /**
     * Moved from court room id
     */
    private int movedFromCourtRoomId;

    /**
     * Not before time
     */
    private Timestamp notBeforeTime;

    /**
     * Court Site Name
     */
    private String courtSiteName;

    /**
     * Court Site Short Name
     */
    private String courtSiteShortName;

    /**
     * Court Site Code
     */
    private String courtSiteCode;
    
    /**
     * Crest Court Room Number
     */
    private int crestCourtRoomNo;

    /**
     * The Court Log event information to be processed by the Public Display
     * rendering.
     */
    private BranchEventXMLNode event;

    /**
     * The Court Log event information to be processed by the Public Display
     * rendering.
     */
    private Timestamp eventTime;

    /**
     * Gets the Court Log Event Node
     * 
     * @param val
     */
    public BranchEventXMLNode getEvent() {
        return event;
    }

    /**
     * Sets the Court Log Event Node
     * 
     * @param val
     */
    public void setEvent(BranchEventXMLNode val) {
        event = val;
    }

    /**
     * Gets the Court Log Event Time
     */
    public Timestamp getEventTime() {
        return eventTime;
    }

    /**
     * Gets the Court Log Event Time as String
     */
    public String getEventTimeAsString() {
        if (eventTime == null) {
        	log.debug("Invalid event time entered.");
            return "";
        }

        return dateFormat.format(eventTime);
    }

    /**
     * Sets the Court Log Event Node
     * 
     * @param val
     */
    public void setEventTime(Timestamp timeIn) {
        eventTime = timeIn;
    }

    /**
     * Gets the court site name
     * 
     * @return Court Site Name
     */
    public String getCourtSiteName() {
        return courtSiteName;
    }

    /**
     * Gets the court site short name
     * 
     * @return Court Site Short Name
     */
    public String getCourtSiteShortName() {
        return courtSiteShortName;
    }

    /**
     * Gets the court site code
     * 
     * @return Court Site Code
     */
    public String getCourtSiteCode() {
        return courtSiteCode;
    }

    /**
     * Sets the Court Site Name
     * 
     * @param courtSiteName
     */
    public void setCourtSiteName(String courtSiteName) {
        this.courtSiteName = courtSiteName;
    }

    /**
     * Sets the Court Site Short Name
     * 
     * @param courtSiteShortName
     */
    public void setCourtSiteShortName(String courtSiteShortName) {
        this.courtSiteShortName = courtSiteShortName;
    }

    /**
     * Sets the Court Site Code
     * 
     * @param courtSiteCode
     */
    public void setCourtSiteCode(String courtSiteCode) {
        this.courtSiteCode = courtSiteCode;
    }

    /**
     * Gets the court room in which the case is heard
     * 
     * @param Court
     *            room in which the case is heard
     */
    public void setCourtRoomId(int val)

    {
        courtRoomId = val;
    }

    /**
     * Gets the court room in which the case is heard
     * 
     * @return Court room in which the case is heard
     */
    public int getCourtRoomId()

    {
        return courtRoomId;
    }

    /**
     * Gets the moved court room in which the case is heard
     * 
     * @param Court
     *            room in which the case is heard
     */
    public void setMovedFromCourtRoomId(int val) {
        movedFromCourtRoomId = val;
    }

    /**
     * Gets the moved court room in which the case is heard
     * 
     * @return Court room in which the case is heard
     */
    public int getMovedFromCourtRoomId() {
        return movedFromCourtRoomId;
    }

    /**
     * Gets the court room in which the case is heard
     * 
     * @param Court
     *            room in which the case is heard
     */
    public void setCourtRoomName(String val) {
        courtRoomName = val;
    }

    /**
     * Gets the court room in which the case is heard
     * 
     * @return Court room in which the case is heard
     */
    public String getCourtRoomName() {
        return courtRoomName;
    }

    /**
     * Sets the court site short name from where the case is moved
     */
    public void setMovedFromCourtSiteShortName(String val) {
        movedFromCourtSiteShortName = val;
    }

    /**
     * Gets the court site short name from where the case is moved
     * 
     * @return Court site short name from where the case is moved
     */
    public String getMovedFromCourtSiteShortName() {
        return movedFromCourtSiteShortName;
    }

    /**
     * Gets the court room from where the case is moved
     * 
     * @param Court
     *            room from where the case is moved
     */
    public void setMovedFromCourtRoomName(String val) {
        movedFromCourtRoomName = val;
    }

    /**
     * Gets the court room from where the case is moved
     * 
     * @return Court room from where the case is moved
     */
    public String getMovedFromCourtRoomName() {
        return movedFromCourtRoomName;
    }

    /**
     * Gets the time of hearing
     * 
     * @param Time
     *            of hearing
     */
    public void setNotBeforeTime(Timestamp val) {
        notBeforeTime = val;
    }

    /**
     * Gets the time of hearing
     * 
     * @return Time of hearing
     */
    public Timestamp getNotBeforeTime() {
        return notBeforeTime;
    }

    /**
     * Returns not before time as a formatted string
     * 
     * @return
     */
    public String getNotBeforeTimeAsString() {
        if (notBeforeTime == null) {
        	log.debug("Invalid not before time entered.");
            return "";
        }

        return dateFormat.format(notBeforeTime);
    }

    /**
     * Returns Crest Court Room Number
     * 
     * @return Crest Court Room Number
     */
    public int getCrestCourtRoomNo() {
		return crestCourtRoomNo;
	}

    /**
     * Sets Crest Court Room Number
     * 
     * @param crestCourtRoomNo
     */
	public void setCrestCourtRoomNo(int crestCourtRoomNo) {
		this.crestCourtRoomNo = crestCourtRoomNo;
	}

	public boolean hasInformationForDisplay() {
        return true;
    }
	
	public int compareTo(PublicDisplayValue other) {
		if ( !this.courtSiteCode.equals(other.getCourtSiteCode()) ) {
			return this.courtSiteCode.compareTo(other.getCourtSiteCode());
		}
		if ( this.crestCourtRoomNo != other.getCrestCourtRoomNo() ) {
			return this.crestCourtRoomNo - other.getCrestCourtRoomNo();
		}
		return 0;
	}
	
	@Override
    public boolean equals(Object o) {
    	if (o instanceof AllCourtStatusValue) {
    		if ( this.getCourtSiteCode().equals(((AllCourtStatusValue) o).getCourtSiteCode()) &&
    			 this.getCrestCourtRoomNo() == ((AllCourtStatusValue) o).getCrestCourtRoomNo() ) {
    			return true;
    		}
    	}
    	return false; 
    }
    
    @Override
    public int hashCode() {
    	String hash = this.getCourtSiteCode() + this.getCrestCourtRoomNo();
    	return hash.hashCode();
    }
    
    /**
     * Method used to compare two timestamps which could potentially be null
     * @param ts1	Timestamp 1
     * @param ts2	Timestamp 2
     * @return	comparison result
     */
    protected int compareNotBeforeTimeCheckNull(Timestamp ts1, Timestamp ts2) {
    	int cmp = 0;
    	if ( ts1 == null ) {
    		// Timestamp 1 is null
    		if ( ts2 == null ) {
    			// Timestamp 2 is also null
    			cmp = 0;
    		} 
    		else {
    			// Timestamp 2 is not null
    			cmp = -1;
    		}
    	} 
    	else if ( ts2 == null ) {
    		// Timestamp 1 is not null but Timestamp 2 is null
    		cmp = 1;
    	} 
    	else {
    		// Neither Timestamp is null, so do a Timestamp compare
    		cmp = ts1.compareTo(ts2);
    	}
    	return cmp;
    }

}
