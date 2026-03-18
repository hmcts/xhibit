package uk.gov.courtservice.xhibit.business.vos.entities;

//framework
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefAppResD20Map
 * </p>
 * <p>
 * Description: RefAppResD20Map
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

public class RefAppResD20MapBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -3336104395794492449L;
    private Integer refAppResD20MapId;

    private String appResultCode;

    private String d20Result;

    private String obsInd;

    /**
     * Default constructor
     */
    public RefAppResD20MapBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefAppResD20MapBasicValue(Integer id, Integer version) {
        super(id, version);
    }
	
	/**
     * Constructor that takes some params as arguments
     * 
     * @param refAppResD20MapId
     * @param appResultCode
     * @param d20Result
     * @param obsInd
     */
    public RefAppResD20MapBasicValue(Integer refAppResD20MapId, String appResultCode, String d20Result) {
        this.refAppResD20MapId = refAppResD20MapId;
        this.appResultCode = appResultCode;
        this.d20Result = d20Result;
    }

    /**
     * Constructor that takes all params as arguments
     * 
     * @param refAppResD20MapId
     * @param appResultCode
     * @param d20Result
     * @param obsInd
     */
    public RefAppResD20MapBasicValue(Integer refAppResD20MapId, String appResultCode, String d20Result, String obsInd) {
        this.refAppResD20MapId = refAppResD20MapId;
        this.appResultCode = appResultCode;
        this.d20Result = d20Result;
        this.obsInd = obsInd;
    }

    public Integer getRefAppResD20MapId() {
        return refAppResD20MapId;
    }

    public String getAppResultCode() {
        return appResultCode;
    }

    public String getD20Result() {
        return d20Result;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setRefAppResD20MapId(Integer refAppResD20MapId) {
        this.refAppResD20MapId = refAppResD20MapId;
    }

    public void setAppResultCode(String appResultCode) {
        this.appResultCode = appResultCode;
    }

    public void setD20Result(String d20Result) {
        this.d20Result = d20Result;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }
}