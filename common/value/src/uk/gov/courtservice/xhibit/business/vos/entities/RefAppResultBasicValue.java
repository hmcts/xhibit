package uk.gov.courtservice.xhibit.business.vos.entities;

//framework
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefAppResultBasicValue
 * </p>
 * <p>
 * Description: RefAppResultBasicValue
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class RefAppResultBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -3336104395794492479L;
    private Integer refAppResId;

    private String code;

    private String description1;

    private String description2;

    private Integer hoCode;

    private String varySentence;

    private String lesserOffInd;

    private Integer courtId;

    private String obsInd;

    /**
     * Default constructor
     */
    public RefAppResultBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefAppResultBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Constructor that takes all params as arguments
     * 
     * @param refAppResId
     * @param code
     * @param description1
     * @param description2
     * @param hoCode
     * @param varySentence
     * @param lesserOffInd
     * @param courtId
     * @param obsInd
     */
    public RefAppResultBasicValue(Integer refAppResId, String code, String description1, String description2,
            Integer hoCode, String varySentence, String lesserOffInd, Integer courtId, String obsInd) {
        this.refAppResId = refAppResId;
        this.code = code;
        this.description1 = description1;
        this.description2 = description2;
        this.hoCode = hoCode;
        this.varySentence = varySentence;
        this.lesserOffInd = lesserOffInd;
        this.courtId = courtId;
        this.obsInd = obsInd;
    }

    public Integer getRefAppResId() {
        return refAppResId;
    }

    public String getCode() {
        return code;
    }

    public String getDescription1() {
        return description1;
    }

    public String getDescription2() {
        return description2;
    }

    public Integer getHoCode() {
        return hoCode;
    }

    public String getVarySentence() {
        return varySentence;
    }

    public String getLesserOffInd() {
        return lesserOffInd;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setRefAppResId(Integer refAppResId) {
        this.refAppResId = refAppResId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public void setHoCode(Integer hoCode) {
        this.hoCode = hoCode;
    }

    public void setVarySentence(String varySentence) {
        this.varySentence = varySentence;
    }

    public void setLesserOffInd(String lesserOffInd) {
        this.lesserOffInd = lesserOffInd;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }
}