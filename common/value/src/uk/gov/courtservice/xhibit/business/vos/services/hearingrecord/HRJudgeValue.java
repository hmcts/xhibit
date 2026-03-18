package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

/**
 * Value object to store the judge information required on a CREST form 'A'.
 * This is a read-only object.
 * 
 * from refJudge
 * 
 */
public class HRJudgeValue implements HRValueObject {

    private Integer refJudgeID;

    private String judgeFirstName;

    private String judgeMiddleName;

    private String judgeSurname;

    private String judgeFullListTitle1;

    private String judgeFullListTitle2;

    private String judgeFullListTitle3;
    
    private static final long serialVersionUID = -4502506355939260728L;

    public HRJudgeValue(Integer rjID) {
        this.refJudgeID = rjID;
    }

    public HRJudgeValue() {
    }

    public String getJudgeFirstName() {
        return judgeFirstName;
    }

    public String getJudgeFullListTitle1() {
        return judgeFullListTitle1;
    }

    public String getJudgeFullListTitle2() {
        return judgeFullListTitle2;
    }

    public String getJudgeFullListTitle3() {
        return judgeFullListTitle3;
    }

    public String getJudgeMiddleName() {
        return judgeMiddleName;
    }

    public String getJudgeSurname() {
        return judgeSurname;
    }

    public void setJudgeFirstName(String judgeFirstName) {
        this.judgeFirstName = judgeFirstName;
    }

    public void setJudgeFullListTitle1(String judgeFullListTitle1) {
        this.judgeFullListTitle1 = judgeFullListTitle1;
    }

    public void setJudgeFullListTitle2(String judgeFullListTitle2) {
        this.judgeFullListTitle2 = judgeFullListTitle2;
    }

    public void setJudgeFullListTitle3(String judgeFullListTitle3) {
        this.judgeFullListTitle3 = judgeFullListTitle3;
    }

    public void setJudgeMiddleName(String judgeMiddleName) {
        this.judgeMiddleName = judgeMiddleName;
    }

    public void setJudgeSurname(String judgeSurname) {
        this.judgeSurname = judgeSurname;
    }

    public Integer getRefJudgeID() {
        return refJudgeID;
    }

    public void setRefJudgeID(Integer id) {
        this.refJudgeID = id;
    }
}
