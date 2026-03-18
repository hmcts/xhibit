package uk.gov.courtservice.xhibit.business.vos.entities;



/**
 * RefJudgeTicket ComplexValue class.
 * 
 * @author Jasvir Boparai
 *
 */
public class RefJudgeTicketComplexValue extends RefJudgeTicketBasicValue {


	/**
	 * Default constructor.
	 */
	public RefJudgeTicketComplexValue() {
	}

	/**
	 * Key constructor.
	 * 
	 * @param id
	 *            Integer
	 * @param version
	 *            Integer
	 */
	public RefJudgeTicketComplexValue(Integer id, Integer version) {
		super(id, version);
	}

/**
 * Parameter Constructor.
 * 
 * @param id
 * @param version
 * @param judgeId
 * @param ticketType
 * @param obsInd
 * @param courtId

 */
	public RefJudgeTicketComplexValue(Integer id, Integer version, Integer judgeId, String ticketType, String obsInd,
			Integer courtId) {

		super(id, version, judgeId, ticketType, obsInd, courtId);
	}



}