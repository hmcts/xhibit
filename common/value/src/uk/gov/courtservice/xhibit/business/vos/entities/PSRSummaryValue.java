package uk.gov.courtservice.xhibit.business.vos.entities;

import java.io.Serializable;

/**
 * <p>
 * Title:PSRSummaryValue
 * </p>
 * <p>
 * Description: Holds summary information for displaying list of available psrs
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Bucthorpe & Surtar Bachra
 * @version 1.0
 */

public class PSRSummaryValue implements Serializable {
	private static final long serialVersionUID = -7275345628750535565L;
    private Integer id;

    private String caseNumber;

    private String defendant;

    private String psrCourtRoom;

    private String psrStatus;

    public PSRSummaryValue() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setDefendant(String defendant) {
        this.defendant = defendant;
    }

    public String getDefendant() {
        return defendant;
    }

    public void setPsrCourtRoom(String psrCourtRoom) {
        this.psrCourtRoom = psrCourtRoom;
    }

    public String getPsrCourtRoom() {
        return psrCourtRoom;
    }

    public void setPsrStatus(String psrStatus) {
        this.psrStatus = psrStatus;
    }

    public String getPsrStatus() {
        return psrStatus;
    }
}