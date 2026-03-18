package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Solicitor Complex Value.
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

public class SolicitorComplexValue extends SolicitorBasicValue {
	private static final long serialVersionUID = 5430966029520756848L;
    private RefSolicitorFirmComplexValue firm = null;

    // private Integer legalRepId = null;

    /**
     * Default constructor.
     */
    public SolicitorComplexValue() {
    }

    /**
     * Key constructor.
     */
    public SolicitorComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     */
    public SolicitorComplexValue(Integer id, Integer version, String crestSolicitorName, String isInCrest,
            String obsInd, Integer legalRepId, Integer firmId) {

        super(id, version, crestSolicitorName, isInCrest, obsInd, legalRepId, firmId);
    }

    public RefSolicitorFirmComplexValue getFirm() {
        return firm;
    }

    public void setFirm(RefSolicitorFirmComplexValue newValue) {
        firm = newValue;
    }

}
