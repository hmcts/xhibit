package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * Title: SearchCounselFacilitiesCriteria
 * </p>
 * <p>
 * Description: A criteria object to be populated for searching counsels.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Laurent Bossard
 * @author Ian Hannaford
 * @author Marie Holmberg
 * @version 1.0
 */

public class SearchCounselFacilitiesCriteria implements Serializable {
	
	static final long serialVersionUID = 4949571631340312741L;
	
    private Date scheduleDate;

    private Integer courtId;

    private String firstName;

    private Integer courtRoomId;

    private String surname;

    private Collection partyOnCaseValueCollection;

    /**
     * Empty default constructor
     */
    public SearchCounselFacilitiesCriteria() {
    }

    // getters
    public Date getScheduleDate() {
        return scheduleDate;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public String getFirstName() {
        return firstName;
    }

    public Collection getPartyOnCaseValueCollection() {
        return partyOnCaseValueCollection;
    }

    public String getSurname() {
        return surname;
    }

    // setters
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPartyOnCaseValueCollection(Collection pocvCollection) {
        this.partyOnCaseValueCollection = pocvCollection;
    }

    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }
}