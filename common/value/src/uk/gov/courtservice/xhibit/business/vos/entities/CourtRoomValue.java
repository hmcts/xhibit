package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CourtRoomValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 * @deprecated - This version is not up to date with the DB. Use
 *             Basic/ComplexValue instead.
 */

public class CourtRoomValue extends CSAbstractValue {
	private static final long serialVersionUID = -7035092723525451947L;
    // Name
    private String courtRoomName;

    // Description
    private String description;

    // Location
    private String location;

    // Room no
    private Integer crestCourtRoomNo;

    // Id
    private Integer courtRoomId;

    /**
     * Default constructor
     */
    public CourtRoomValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtRoomValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Return the name
     * 
     * @return
     */
    public String getCourtRoomName() {
        return courtRoomName;
    }

    /**
     * Sets the court room name
     * 
     * @param val
     */
    public void setCourtRoomName(String val) {
        courtRoomName = val;
    }

    /**
     * Gets the description
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description
     * 
     * @param val
     */
    public void setDescription(String val) {
        description = val;
    }

    /**
     * Gets the location
     * 
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location
     * 
     * @param val
     */
    public void setLocation(String val) {
        location = val;
    }

    /**
     * Gets the crest court room no
     * 
     * @return
     */
    public Integer getCrestCourtRoomNo() {
        return crestCourtRoomNo;
    }

    /**
     * Sets the crest court room number
     * 
     * @param val
     */
    public void setCrestCourtRoomNo(Integer val) {
        crestCourtRoomNo = val;
    }

    /**
     * Sets the id
     * 
     * @deprecated Ids are not mutable
     * @param val
     */
    public void setCourtRoomId(Integer val) {
    }

    /**
     * Retunrs the id
     * 
     * @deprecated Should be using getId
     * @return
     */
    public Integer getCourtRoomId() {
        return new Integer(-1);
    }

}