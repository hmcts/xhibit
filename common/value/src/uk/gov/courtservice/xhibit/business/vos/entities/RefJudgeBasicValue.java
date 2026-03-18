package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * RefJudgeBasicValue.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @see uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue
 */
public class RefJudgeBasicValue extends CSAbstractValue {
	
	private static final long serialVersionUID = -1077341690695512718L;
	
    private Integer crestJudgeId = null;

    private String judgeType = null;

    private String firstName = null;

    private String middleName = null;

    private String surname = null;

    private String fullListTitle1 = null;

    private String fullListTitle2 = null;

    private String fullListTitle3 = null;

    private String statsCode = null;

    private String initials = null;

    private String honours = null;

    private String judVers = null;

    private String obsInd = null;

    private String sourceTable = null;

    private String title = null;

    private Integer courtId = null;
    
    private String lastUpdatedBy;

    /**
     * Default constructor.
     */
    public RefJudgeBasicValue() {
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefJudgeBasicValue(Integer id, Integer version) {
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
    public RefJudgeBasicValue(Integer id, Integer version, Integer crestJudgeId, String judgeType, String firstName,
            String middleName, String surname, String fullListTitle1, String fullListTitle2, String fullListTitle3,
            String statsCode, String initials, String honours, String judVers, String obsInd, String sourceTable,
            String title, Integer courtId) {
        this(id, version);
        this.crestJudgeId = crestJudgeId;
        this.judgeType = judgeType;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.fullListTitle1 = fullListTitle1;
        this.fullListTitle2 = fullListTitle2;
        this.fullListTitle3 = fullListTitle3;
        this.statsCode = statsCode;
        this.initials = initials;
        this.honours = honours;
        this.judVers = judVers;
        this.obsInd = obsInd;
        this.sourceTable = sourceTable;
        this.title = title;
        this.courtId = courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return this.courtId;
    }

    public Integer getCrestJudgeId() {
        return this.crestJudgeId;
    }

    public void setCrestJudgeId(Integer crestJudgeId) {
        this.crestJudgeId = crestJudgeId;
    }

    public String getJudgeType() {
        return this.judgeType;
    }

    public void setJudgeType(String judgeType) {
        this.judgeType = judgeType;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullListTitle1() {
        return this.fullListTitle1;
    }

    public void setFullListTitle1(String fullListTitle1) {
        this.fullListTitle1 = fullListTitle1;
    }

    public String getFullListTitle2() {
        return this.fullListTitle2;
    }

    public void setFullListTitle2(String fullListTitle2) {
        this.fullListTitle2 = fullListTitle2;
    }

    public String getFullListTitle3() {
        return this.fullListTitle3;
    }

    public void setFullListTitle3(String fullListTitle3) {
        this.fullListTitle3 = fullListTitle3;
    }

    public String getStatsCode() {
        return this.statsCode;
    }

    public void setStatsCode(String statsCode) {
        this.statsCode = statsCode;
    }

    public String getInitials() {
        return this.initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getHonours() {
        return this.honours;
    }

    public void setHonours(String honours) {
        this.honours = honours;
    }

    public String getJudVers() {
        return this.judVers;
    }

    public void setJudVers(String judVers) {
        this.judVers = judVers;
    }

    public String getObsInd() {
        return this.obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getSourceTable() {
        return this.sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}
