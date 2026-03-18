package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: IndictmentLogValue
 * </p>
 * <p>
 * Description: A Value Object where the attributes map one to one with the
 * LegalAidOrder enitity CMP fields.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Nia Walters
 * @version 1.0
 * 
 */

public class LegalAidOrderBasicValue extends CSAbstractValue{
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Fields that map in the bean.
	 */
	private Integer crestLeoId;	
    private Integer defendantOnCaseId;
	private Integer caseProsAgencyId;
	private String grantedBy;
	private Date orderDate;
	private String psdRoRef;
	private Integer numberOfAdvocates;
	private Integer numberOfQcs;
	private Date dateOfRevocation;
	private Integer reasonForRevocationId;
	private String obsInd;
	//this is needed for public rep to tie it to the defendant in the table/ pros in the table
	private Integer defendantId;
	private Integer respId;

	
	public LegalAidOrderBasicValue() {
		super();
	}
	
	/**
	 * Constructor to set id and version.
	 * @param id Integer
	 * @param version Integer
	 */
	public LegalAidOrderBasicValue(Integer id, Integer version) {
		super(id, version);
	}
	
	/**
	 * Used when creating an order in Public representation
	 * @param orderDate Date
	 * @param grantedBy String 
	 * @param psdRoRef String 
	 * @param numberOfAdvocates Integer
	 * @param numberOfQcs Integer
	 */
	public LegalAidOrderBasicValue(Date orderDate, String grantedBy, 
			String psdRoRef, Integer numberOfAdvocates, Integer numberOfQcs ) {
		this.orderDate = orderDate;
		this.grantedBy = grantedBy;
		this.psdRoRef = psdRoRef;
		this.numberOfAdvocates = numberOfAdvocates;
		this.numberOfQcs = numberOfQcs;
	}
	
	public Integer getCrestLeoId() {
		return crestLeoId;
	}
    
    public void setCrestLeoId(Integer crestLeoId) {
    	this.crestLeoId=crestLeoId;
    }
    
    public Integer getDefendantOnCaseId() {
    	return defendantOnCaseId;
    }
    
    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
    	this.defendantOnCaseId = defendantOnCaseId;
    }
    
    public String getGrantedBy() {
    	return grantedBy;
    }
    public void setGrantedBy(String grantedBy) {
    	this.grantedBy = grantedBy;
    }

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getPsdRoRef() {
		return psdRoRef;
	}

	public void setPsdRoRef(String psdRoRef) {
		this.psdRoRef = psdRoRef;
	}

	public Integer getNumberOfAdvocates() {
		return numberOfAdvocates;
	}

	public void setNumberOfAdvocates(Integer numberOfAdvocates) {
		this.numberOfAdvocates = numberOfAdvocates;
	}

	public Integer getNumberOfQcs() {
		return numberOfQcs;
	}

	public void setNumberOfQcs(Integer numberOfQcs) {
		this.numberOfQcs = numberOfQcs;
	}

	public Integer getCaseProsAgencyId() {
		return caseProsAgencyId;
	}

	public void setCaseProsAgencyId(Integer caseProsAgencyId) {
		this.caseProsAgencyId = caseProsAgencyId;
	}

	public Date getDateOfRevocation() {
		return dateOfRevocation;
	}

	public void setDateOfRevocation(Date dateOfRevocation) {
		this.dateOfRevocation = dateOfRevocation;
	}

	public Integer getReasonForRevocationId() {
		return reasonForRevocationId;
	}

	public void setReasonForRevocationId(Integer reasonForRevocationId) {
		this.reasonForRevocationId = reasonForRevocationId;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	public Integer getDefendantId() {
		return defendantId;
	}

	public void setDefendantId(Integer defendantId) {
		this.defendantId = defendantId;
	}

	public Integer getRespId() {
		return respId;
	}

	public void setRespId(Integer respId) {
		this.respId = respId;
	}

}
