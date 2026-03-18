package uk.gov.courtservice.xhibit.business.entities.refjudge;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefJudge extends CSEntityLocal {

    public Integer getRefJudgeId(); // PK

    public Integer getCourtId();

    public Integer getCrestJudgeId();

    public String getFirstName();

    public String getFullListTitle1();

    public String getFullListTitle2();

    public String getFullListTitle3();

    public String getHonours();

    public String getInitials();

    public String getJudVers();

    public String getJudgeType();

    public String getMiddleName();

    public String getObsInd();

    public String getSourceTable();

    public String getStatsCode();

    public String getSurname();

    public String getTitle();

    public void setCourtId(Integer courtId);

    public void setCrestJudgeId(Integer crestJudgeId);

    public void setFirstName(String firstName);

    public void setFullListTitle1(String fullListTitle1);

    public void setFullListTitle2(String fullListTitle2);

    public void setFullListTitle3(String fullListTitle3);

    public void setHonours(String honours);

    public void setInitials(String initials);

    public void setJudVers(String judVers);

    public void setJudgeType(String judgeType);

    public void setMiddleName(String middleName);

    public void setObsInd(String obsInd);

    public void setSourceTable(String sourceTable);

    public void setStatsCode(String statsCode);

    public void setSurname(String surname);

    public void setTitle(String title);
    /*
     * public Court getCourt(); public void getCourt(Court newValue);
     */

	public String getLastUpdatedBy();

	public void setLastUpdatedBy(String userName);
}