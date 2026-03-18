package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

/**
 * RefJudgeComplexValue.
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
public class RefJudgeComplexValue extends RefJudgeBasicValue {
	
    private CourtBasicValue court = null;
    private Collection<RefJudgeTicketBasicValue> refJudgeTickets = null;
    private String allTicketTypes = null;
    private String fullListTitle = null;
    private static final long serialVersionUID = -5264641577341298967L;

    /**
     * Default constructor.
     */
    public RefJudgeComplexValue() {
    }

    /**
     * Key constructor.
     */
    public RefJudgeComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param crestJudgeId
     * @param judgeType
     * @param firstName
     * @param middleName
     * @param surname
     * @param fullListTitle1
     * @param fullListTitle2
     * @param fullListTitle3
     * @param statsCode
     * @param initials
     * @param honours
     * @param judVers
     * @param obsInd
     * @param sourceTable
     */
    public RefJudgeComplexValue(Integer id, Integer version, Integer crestJudgeId, String judgeType, String firstName,
            String middleName, String surname, String fullListTitle1, String fullListTitle2, String fullListTitle3,
            String statsCode, String initials, String honours, String judVers, String obsInd, String sourceTable,
            String title, Integer courtId) {
        super(id, version, crestJudgeId, judgeType, firstName, middleName, surname, fullListTitle1, fullListTitle2,
                fullListTitle3, statsCode, initials, honours, judVers, obsInd, sourceTable, title, courtId);
    }
    
    /**
     * Basic value constructor.
     * 
     * @param basicValue
     */
    public RefJudgeComplexValue(RefJudgeBasicValue basicValue) {
        super(basicValue.getId(), basicValue.getVersion(), basicValue.getCrestJudgeId(), basicValue.getJudgeType(), basicValue.getFirstName(),
        		basicValue.getMiddleName(), basicValue.getSurname(), basicValue.getFullListTitle1(), basicValue.getFullListTitle2(),
        		basicValue.getFullListTitle3(), basicValue.getStatsCode(), basicValue.getInitials(), basicValue.getHonours(),
        		basicValue.getJudVers(), basicValue.getObsInd(), basicValue.getSourceTable(), basicValue.getTitle(), basicValue.getCourtId());
    }

    public CourtBasicValue getCourt() {
        return this.court;
    }

    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }
    
    public Collection<RefJudgeTicketBasicValue> getRefJudgeTickets() {
        return this.refJudgeTickets;
    }

    public void setRefJudgeTickets( Collection<RefJudgeTicketBasicValue>  newValue) {
        this.refJudgeTickets = newValue;
    }

	public String getAllTicketTypes() {
		return allTicketTypes;
	}

	public void setAllTicketTypes(String allTicketTypes) {
		this.allTicketTypes = allTicketTypes;
	}
	
	public String getFullListTitle() {
		return fullListTitle;
	}

	public void setFullListTitle(String fullListTitle) {
		this.fullListTitle = fullListTitle;
	}
}