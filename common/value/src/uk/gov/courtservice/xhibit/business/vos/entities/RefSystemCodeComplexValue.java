package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * 'System Code' Reference Data - Complex Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
public class RefSystemCodeComplexValue extends RefSystemCodeBasicValue {
	
	static final long serialVersionUID = -7909391510499857669L;

    private CourtBasicValue court = null;

    /**
     * Default constructor.
     */
    public RefSystemCodeComplexValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefSystemCodeComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param refCodeOrder
     * @param code
     * @param codeType
     * @param codeTitle
     * @param decode
     * @param xhbVersion
     */
    public RefSystemCodeComplexValue(Integer id, Integer version, Integer refCodeOrder, String code, String codeType,
            String codeTitle, String decode, String obsInd, Integer courtId) {

        super(id, version, refCodeOrder, code, codeType, codeTitle, decode, obsInd, courtId);
    }

    public CourtBasicValue getCourt() {
        return court;
    }

    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }
}