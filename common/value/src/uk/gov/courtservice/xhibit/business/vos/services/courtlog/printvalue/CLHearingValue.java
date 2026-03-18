package uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This class is not completed
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class CLHearingValue extends CSAbstractValue {
    private String currentCase;

    private String judge;

    private Collection prosecutionAdvocates;

    private Collection defenceAdvocates;

    private String hearingType;

    private String timeListed;

    private Collection defendants;

    private String shorthandWriter;
    
    private static final long serialVersionUID = 8321751192265872981L;

    public CLHearingValue() {
    }

    public Collection getDefenceAdvocates() {
        return defenceAdvocates;
    }

    public String getCurrentCase() {
        return currentCase;
    }

    public String getHearingType() {
        return hearingType;
    }

    public String getJudge() {
        return judge;
    }

    public Collection getProsecutionAdvocates() {
        return prosecutionAdvocates;
    }

    public String getTimeListed() {
        return timeListed;
    }

    public Collection getDefendants() {
        return defendants;
    }

    public String getShorthandWriter() {
        return shorthandWriter;
    }

    public void setTimeListed(String timeListed) {
        this.timeListed = timeListed;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public void setProsecutionAdvocates(Collection prosecutionAdvocates) {
        this.prosecutionAdvocates = prosecutionAdvocates;
    }

    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }

    public void setDefenceAdvocates(Collection defenceAdvocates) {
        this.defenceAdvocates = defenceAdvocates;
    }

    public void setCurrentCase(String currentCase) {
        this.currentCase = currentCase;
    }

    public void setDefendants(Collection defendants) {
        this.defendants = defendants;
    }

    public void setShorthandWriter(String shorthandWriter) {
        this.shorthandWriter = shorthandWriter;
    }
}