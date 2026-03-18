package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
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
public class CourtValue extends CSAbstractValue {
	private static final long serialVersionUID = -2096468988823187177L;
    // Id
    private String courtId;

    // Type
    private String courtType;

    // Circuit
    private String circuit;

    // Name
    private String courtName;

    // Crest court Id
    private String crestCourtId;

    // Prefix
    private String courtPrefix;

    // Short name
    private String shortName;

    // courtCode
    private String courtCode;

    /**
     * Default constructor
     */
    public CourtValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Gets type
     * 
     * @param val
     */
    public void setCourtType(String val) {
        courtType = val;
    }

    /**
     * Gets court type
     * 
     * @return
     */
    public String getCourtType() {
        return courtType;
    }

    /**
     * Sets circuit
     * 
     * @param val
     */
    public void setCircuit(String val) {
        circuit = val;
    }

    /**
     * Gets circuit
     * 
     * @return
     */
    public String getCircuit() {
        return circuit;
    }

    /**
     * Sets crest court name
     * 
     * @param val
     */
    public void setCourtName(String val) {
        courtName = val;
    }

    /**
     * Gets crest court name
     * 
     * @return
     */
    public String getCourtName() {
        return courtName;
    }

    /**
     * Sets crest court id
     * 
     * @param val
     */
    public void setCrestCourtId(String val) {
        crestCourtId = val;
    }

    /**
     * Get crest court id
     * 
     * @return
     */
    public String getCrestCourtId() {
        return crestCourtId;
    }

    /**
     * Sets prefix
     * 
     * @param val
     */
    public void setCourtPrefix(String val) {
        courtPrefix = val;
    }

    /**
     * Gets prefix
     * 
     * @return
     */
    public String getCourtPrefix() {
        return courtPrefix;
    }

    /**
     * Sets short name
     * 
     * @param val
     */
    public void setShortName(String val) {
        shortName = val;
    }

    /**
     * Gets short name
     * 
     * @return
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets courtCode
     * 
     * @param val
     */
    public void setCourtCode(String val) {
        courtCode = val;
    }

    /**
     * Gets courtCode
     * 
     * @return
     */
    public String getCourtCode() {
        return courtCode;
    }

}