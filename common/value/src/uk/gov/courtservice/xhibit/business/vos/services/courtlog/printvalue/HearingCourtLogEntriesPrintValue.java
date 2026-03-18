package uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class HearingCourtLogEntriesPrintValue extends CourtLogControllerPrintCompositeValue {
    private HearingHeaderValue hearingHeaderValue;

    private String caseNumber;

    private String defendants;

    private String judge;

    private String prosAdvocate;

    private String defAdvocate;

    private String hearingType;

    private String timeListed;

    private String courtReporter;

    private String linkedCases;

    private String justice01;

    private String justice02;

    private String justice03;

    private String justice04;

    private String respondent;

    private int typeOfCase;

    private String courtClerk;

    private String objAdvocate;
    private static final long serialVersionUID = -4539929962782204586L;

    public HearingCourtLogEntriesPrintValue(Date date) {
        super("hearing", date); // type used to identify object in xsl
    }

    public HearingHeaderValue getHearingHeaderValue() {
        return hearingHeaderValue;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public String getDefendants() {
        return defendants;
    }

    public String getJudge() {
        return judge;
    }

    public String getProsAdvocate() {
        return prosAdvocate;
    }

    public String getDefAdvocate() {
        return defAdvocate;
    }

    public String getHearingType() {
        return hearingType;
    }

    public String getTimeListed() {
        return timeListed;
    }

    public String getCourtReporter() {
        return courtReporter;
    }

    public String getLinkedCases() {
        return linkedCases;
    }

    public String getJustice01() {
        return justice01;
    }

    public String getJustice02() {
        return justice02;
    }

    public String getJustice03() {
        return justice03;
    }

    public String getJustice04() {
        return justice04;
    }

    public String getRespondent() {
        return respondent;
    }

    public int getTypeOfCase() {
        return typeOfCase;
    }

    public String getObjAdvocate() {
        return objAdvocate;
    }

    public void setHearingHeaderValue(HearingHeaderValue param) {
        hearingHeaderValue = param;
    }

    public void setCaseNumber(String param) {
        caseNumber = param;
    }

    public void setDefendants(String param) {
        defendants = param;
    }

    public void setJudge(String param) {
        judge = param;
    }

    public void setProsAdvocate(String param) {
        prosAdvocate = param;
    }

    public void setDefAdvocate(String param) {
        defAdvocate = param;
    }

    public void setHearingType(String param) {
        hearingType = param;
    }

    public void setTimeListed(String param) {
        timeListed = param;
    }

    public void setCourtReporter(String param) {
        courtReporter = param;
    }

    public void setLinkedCases(String param) {
        linkedCases = param;
    }

    public void setJustice01(String param) {
        justice01 = param;
    }

    public void setJustice02(String param) {
        justice02 = param;
    }

    public void setJustice03(String param) {
        justice03 = param;
    }

    public void setJustice04(String param) {
        justice04 = param;
    }

    public void setRespondent(String param) {
        respondent = param;
    }

    public void setTypeOfCase(int param) {
        typeOfCase = param;
    }

    public void setObjAdvocate(String param) {
        objAdvocate = param;
    }

    public String getCourtClerk() {
        return this.courtClerk;
    }

    public void setCourtClerk(String courtClerk) {
        this.courtClerk = courtClerk;
    }
}
