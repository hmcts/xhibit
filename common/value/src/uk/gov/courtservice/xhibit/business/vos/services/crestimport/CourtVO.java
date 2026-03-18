package uk.gov.courtservice.xhibit.business.vos.services.crestimport;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Represents a court and name
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CourtVO implements java.io.Serializable {

    /**
     * Id of the court
     */
    private Integer courtId;

    /**
     * Name of the court
     */
    private String courtName;

    private static final long serialVersionUID = 1384475319887075684L;
    
    /**
     * Initializes the court
     * 
     * @param newCourtId
     * @param newCourtName
     */
    public CourtVO(Integer newCourtId, String newCourtName) {

        if (newCourtId == null)
            throw new IllegalArgumentException("newCourtId");
        if (newCourtName == null)
            throw new IllegalArgumentException("newCourtName");

        courtId = newCourtId;
        courtName = newCourtName;

    }

    /**
     * Returns the court id
     * 
     * @return
     */
    public Integer getCourtId() {
        return courtId;
    }

    /**
     * Returns the court name4
     * 
     * @return
     */
    public String getCourtName() {
        return courtName;
    }

}