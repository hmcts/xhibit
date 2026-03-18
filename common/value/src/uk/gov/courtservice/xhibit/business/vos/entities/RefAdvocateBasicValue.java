package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * RefAdvocate Basic Value.
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

public class RefAdvocateBasicValue extends RefLegalRepresentativeBasicValue {

    private String isGlobal = null;

    private Integer crestAdvocateId = null;

    private Integer crestChamberId = null;

    private String advTypeInd = null;

    private String initials = null;

    private String obsInd = null;

    private Integer yearOfCall = null;

    private String vatNo = null;

    private Integer barNo = null;

    private String honours = null;

    private Integer legalRepId = null;
    
    private String available = null;
    
    private Integer crestPostNumber = null;
    
    private String crestAdvCategory = null;
    
	private Integer refChamberId = null;
    
    private static final long serialVersionUID = -2513126957356301852L;

    /**
     * Default constructor
     */
    public RefAdvocateBasicValue() {
        // empty
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param Integer
     *            version
     */
    public RefAdvocateBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param Integer
     *            version
     * @param isGlobal
     * @param crestAdvocateId
     * @param crestChamberId
     */
    public RefAdvocateBasicValue(Integer id, Integer version, String isGlobal, Integer crestAdvocateId,
            Integer crestChamberId, String advTypeInd, String initials, String obsInd, Integer yearOfCall,
            String vatNo, Integer barNo, String honours, Integer legalRepId,
            String available, Integer crestPostNumber, String crestAdvCategory, Integer refChamberId) {

        this(id, version);
        this.isGlobal = isGlobal;
        this.crestAdvocateId = crestAdvocateId;
        this.crestChamberId = crestChamberId;
        this.advTypeInd = advTypeInd;
        this.initials = initials;
        this.obsInd = obsInd;
        this.yearOfCall = yearOfCall;
        this.vatNo = vatNo;
        this.barNo = barNo;
        this.honours = honours;
        this.legalRepId = legalRepId;
        this.available = available;
        this.crestPostNumber = crestPostNumber;
        this.crestAdvCategory = crestAdvCategory;
        this.refChamberId = refChamberId;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public String getVatNo() {
        return vatNo;
    }

    public void setHonours(String honours) {
        this.honours = honours;
    }

    public String getHonours() {
        return honours;
    }

    public void setyearOfCall(Integer yearOfCall) {
        this.yearOfCall = yearOfCall;
    }

    public Integer getyearOfCall() {
        return yearOfCall;
    }

    public void setbarNo(Integer barNo) {
        this.barNo = barNo;
    }

    public Integer getbarNo() {
        return barNo;
    }

    public void setIsGlobal(String isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getIsGlobal() {
        return isGlobal;
    }

    public void setCrestAdvocateId(Integer crestAdvocateId) {
        this.crestAdvocateId = crestAdvocateId;
    }

    public Integer getCrestAdvocateId() {
        return crestAdvocateId;
    }

    public void setCrestChamberId(Integer crestChamberId) {
        this.crestChamberId = crestChamberId;
    }

    public Integer getCrestChamberId() {
        return crestChamberId;
    }

    public void setAdvTypeInd(String advTypeInd) {
        this.advTypeInd = advTypeInd;
    }

    public String getAdvTypeInd() {
        return advTypeInd;
    }

    public void setLegalRepId(Integer repId) {
        legalRepId = repId;
    }

    public Integer getLegalRepId() {
        return legalRepId;
    }
    
    public void setAvailable(String available) {
        this.available = available;
    }

    public String getAvailable() {
        return available;
    }
    
    public void setCrestPostNumber(Integer crestPostNumber) {
        this.crestPostNumber = crestPostNumber;
    }
    
    public Integer getCrestPostNumber() {
        return crestPostNumber;
    }
    
    public void setCrestAdvCategory(String crestAdvCategory) {
        this.crestAdvCategory = crestAdvCategory;
    }
    
    public String getCrestAdvCategory() {
        return crestAdvCategory;
    }
    
    public void setRefChamberId(Integer refChamberId) {
    	this.refChamberId = refChamberId;
    }
    
    public Integer getRefChamberId() {
    	return refChamberId;
    }
}