package uk.gov.courtservice.xhibit.common.publicdisplay.events.types;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;

/**
 * <p>
 * Title: Court Room Identifier
 * </p>
 * 
 * <p>
 * Description: This class holds the court id and court room id
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: CourtRoomIdentifier.java,v 1.4 2006/06/05 12:28:23 bzjrnl Exp $
 */
public class CourtRoomIdentifier implements Serializable {
	
	static final long serialVersionUID = -7579306166442219719L;
	
    private Integer courtId;

    private Integer courtRoomId;
    
    private String courtName;
    
    private Integer courtRoomNo;
    
    private DisplayablePublicNoticeValue[] publicNotices;

	/**
     * Create the object using the court id and court room id
     * 
     * @param courtId
     *            identifier from XHB_COURT
     * @param courtRoomId
     *            identifier from XHB_COURT_ROOM
     */
    public CourtRoomIdentifier(Integer courtId, Integer courtRoomId, String courtName, Integer courtRoomNo,
    		DisplayablePublicNoticeValue[] publicNotices) {
        setCourtId(courtId);
        setCourtRoomId(courtRoomId);
        setCourtName(courtName);
        setCourtRoomNo(courtRoomNo);
        if (publicNotices != null) {
        	setPublicNotices(publicNotices);
        }
    }

    /**
     * Set a new court Id
     * 
     * @param courtId
     *            identifier from XHB_COURT
     */
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    /**
     * Get the court Id
     * 
     * @return court Id
     */
    public Integer getCourtId() {
        return courtId;
    }

    /**
     * Set a new court roomId
     * 
     * @param courtRoomId
     *            identifier from XHB_COURT_ROOM
     */
    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    /**
     * Get the court room Id
     * 
     * @return court room Id
     */
    public Integer getCourtRoomId() {
        return courtRoomId;
    }

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public Integer getCourtRoomNo() {
		return courtRoomNo;
	}

	public void setCourtRoomNo(Integer courtRoomNo) {
		this.courtRoomNo = courtRoomNo;
	}
	
    public DisplayablePublicNoticeValue[] getPublicNotices() {
		return publicNotices;
	}

	public void setPublicNotices(DisplayablePublicNoticeValue[] publicNotices) {
		this.publicNotices = publicNotices;
	}
    
}
