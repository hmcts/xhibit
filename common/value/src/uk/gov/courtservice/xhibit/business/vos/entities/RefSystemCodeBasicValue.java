package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * 'System Code' Reference Data - Basic Value.
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
public class RefSystemCodeBasicValue extends CSAbstractValue {
	
	static final long serialVersionUID = 4440438374490089263L;

    private Integer refSystemCodeId = null;

    private Integer refCodeOrder = null;

    private String code = null;

    private String codeType = null;

    private String codeTitle = null;

    private String decode = null;

    private String obsInd = null;

    private Integer courtId = null;

    /**
     * Default constructor.
     */
    public RefSystemCodeBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param Integer
     *            id
     * @param Integer
     *            version
     */
    public RefSystemCodeBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param Integer
     *            id
     * @param Integer
     *            version
     * @param Integer
     *            refCodeOrder
     * @param String
     *            code
     * @param String
     *            codeType
     * @param String
     *            codeTitle
     * @param String
     *            decode
     * @param String
     *            obsInd Obsolete indicator
     * @param Integer
     *            courtId
     */
    public RefSystemCodeBasicValue(Integer id, Integer version, Integer refCodeOrder, String code, String codeType,
            String codeTitle, String decode, String obsInd, Integer courtId) {

        this(id, version);
        this.refCodeOrder = refCodeOrder;
        this.code = code;
        this.codeType = codeType;
        this.codeTitle = codeTitle;
        this.decode = decode;
        this.obsInd = obsInd;
        this.courtId = courtId;
    }

    public String getCode() {
        return code;
    }

    public String getCodeTitle() {
        return codeTitle;
    }

    public String getCodeType() {
        return codeType;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public String getDecode() {
        return decode;
    }

    public String getObsInd() {
        return obsInd;
    }

    public Integer getRefCodeOrder() {
        return refCodeOrder;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCodeTitle(String codeTitle) {
        this.codeTitle = codeTitle;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public void setDecode(String decode) {
        this.decode = decode;
    }

    public void setRefCodeOrder(Integer refCodeOrder) {
        this.refCodeOrder = refCodeOrder;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }
}