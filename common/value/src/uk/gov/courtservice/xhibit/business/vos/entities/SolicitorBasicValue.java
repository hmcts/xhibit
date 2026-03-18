package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Solicitor Basic Value
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */

public class SolicitorBasicValue extends RefLegalRepresentativeBasicValue {
	private static final long serialVersionUID = -7231726348407391811L;
    private String crestSolicitorName = null;

    private String inCrest = null;

    private Integer firmId = null;

    private Integer legalRepId = null;

    /** @todo This is the superclass instance id - do we require it too? */
    private String obsInd = null;

    /**
     * Default constructor.
     */
    public SolicitorBasicValue() {
    }

    /**
     * Key constructor.
     */
    public SolicitorBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     */
    public SolicitorBasicValue(Integer id, Integer version, String crestSolicitorName, String isInCrest, String obsInd,
            Integer legalRepId, Integer firmId) {

        this(id, version);
        this.crestSolicitorName = crestSolicitorName;
        this.setInCrest(isInCrest);
        this.firmId = firmId;
        this.legalRepId = legalRepId;
        this.obsInd = obsInd;
    }

    public String getCrestSolicitorName() {
        return crestSolicitorName;
    }

    public Integer getFirmId() {
        return firmId;
    }

    public Integer getLegalRepId() {
        return legalRepId;
    }

    public String getObsInd() {
        return obsInd;
    }

    public Boolean isInCrest() {
        return inCrest.equals("Y") ? new Boolean(true) : new Boolean(false);
    }

    public void setInCrest(Boolean isInCrest) {
        this.inCrest = isInCrest.booleanValue() ? "Y" : "N";
    }

    public void setInCrest(String isInCrest) {
        this.inCrest = isInCrest.equals("Y") ? "Y" : "N";
    }

    public void setCrestSolicitorName(String crestSolicitorName) {
        this.crestSolicitorName = crestSolicitorName;
    }

    public void setFirmId(Integer newValue) {
        this.firmId = newValue;
    }

    public void setLegalRepId(Integer newValue) {
        this.legalRepId = newValue;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getIsInCrest() {
        return inCrest;
    }
}
