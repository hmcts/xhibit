package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * RefJudgeTicket BasicValue class.
 * 
 * @author Jasvir Boparai
 *
 */
public class RefJudgeTicketBasicValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 1L;

	private Integer judgeId = null;

	private String ticketType = null;
	
	private Integer version = null;

	private String obsInd = null;

	private Integer courtId = null;

	/**
	 * Default constructor.
	 */
	public RefJudgeTicketBasicValue() {
	}

	/**
	 * Key constructor.
	 * 
	 * @param id
	 *            Integer
	 * @param version
	 *            Integer
	 */
	public RefJudgeTicketBasicValue(Integer id, Integer version) {
		super(id, version);
	}

/**
 * Parameter Constructor
 * 
 * @param id
 * @param version
 * @param judgeId
 * @param ticketType
 * @param obsInd
 * @param courtId
 */
	public RefJudgeTicketBasicValue(Integer id, Integer version, Integer judgeId, String ticketType, String obsInd,
			Integer courtId) {

		this(id, version);
		this.judgeId = judgeId;
		this.ticketType = ticketType;
		this.obsInd = obsInd;
		this.courtId = courtId;
		this.version = version;
	}

public Integer getJudgeId() {
	return judgeId;
}

public void setJudgeId(Integer judgeId) {
	this.judgeId = judgeId;
}

public String getTicketType() {
	return ticketType;
}

public void setTicketType(String ticketType) {
	this.ticketType = ticketType;
}

public Integer getVersion() {
	return version;
}

public void setVersion(Integer version) {
	this.version = version;
}

public String getObsInd() {
	return obsInd;
}

public void setObsInd(String obsInd) {
	this.obsInd = obsInd;
}

public Integer getCourtId() {
	return courtId;
}

public void setCourtId(Integer courtId) {
	this.courtId = courtId;
}

	
}