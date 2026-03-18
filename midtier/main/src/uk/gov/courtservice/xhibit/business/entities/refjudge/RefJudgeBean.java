package uk.gov.courtservice.xhibit.business.entities.refjudge;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

public abstract class RefJudgeBean extends CSEntityBean {

    public Integer ejbCreate(String judgeType, Integer crestJudgeId, String firstName, String middleName,
            String surname, String fullListTitle1, String fullListTitle2, String fullListTitle3, String statsCode,
            String initials, String honours, String judVers, String obsInd, String sourceTable, String title,
            Integer courtId, String userDisplayName) throws CreateException {

        setCourtId(courtId);
        setCreatedBy(userDisplayName);
        setCrestJudgeId(crestJudgeId);
        setFirstName(firstName);
        setFullListTitle1(fullListTitle1);
        setFullListTitle2(fullListTitle2);
        setFullListTitle3(fullListTitle3);
        setHonours(honours);
        setInitials(initials);
        setJudVers(judVers);
        setJudgeType(judgeType);
        setLastUpdatedBy(userDisplayName);
        setMiddleName(middleName);
        setObsInd(obsInd);
        setSourceTable(sourceTable);
        setStatsCode(statsCode);
        setSurname(surname);
        setTitle(title);
        return null;
    }

    public void ejbPostCreate(String judgeType, Integer crestJudgeId, String firstName, String middleName,
            String surname, String fullListTitle1, String fullListTitle2, String fullListTitle3, String statsCode,
            String initials, String honours, String judVers, String obsInd, String sourceTable, String title,
            Integer courtId, String userDisplayName) throws CreateException {
    }

    public abstract Integer getRefJudgeId(); // PK

    public abstract Integer getCourtId();

    public abstract Integer getCrestJudgeId();

    public abstract String getFirstName();

    public abstract String getFullListTitle1();

    public abstract String getFullListTitle2();

    public abstract String getFullListTitle3();

    public abstract String getHonours();

    public abstract String getInitials();

    public abstract String getJudVers();

    public abstract String getJudgeType();

    public abstract String getMiddleName();

    public abstract String getObsInd();

    public abstract String getSourceTable();

    public abstract String getStatsCode();

    public abstract String getSurname();

    public abstract String getTitle();

    public abstract void setCourtId(Integer courtId);

    public abstract void setCrestJudgeId(Integer crestJudgeId);

    public abstract void setFirstName(String firstName);

    public abstract void setFullListTitle1(String fullListTitle1);

    public abstract void setFullListTitle2(String fullListTitle2);

    public abstract void setFullListTitle3(String fullListTitle3);

    public abstract void setHonours(String honours);

    public abstract void setInitials(String initials);

    public abstract void setJudVers(String judVers);

    public abstract void setJudgeType(String judgeType);

    public abstract void setMiddleName(String middleName);

    public abstract void setObsInd(String obsInd);

    public abstract void setRefJudgeId(Integer refJudgeId);

    public abstract void setSourceTable(String sourceTable);

    public abstract void setStatsCode(String statsCode);

    public abstract void setSurname(String surname);

    public abstract void setTitle(String title);
    /*
     * public abstract Court getCourt(); public abstract void getCourt(Court
     * newValue);
     */
}