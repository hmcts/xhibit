package uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule;

import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;

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
public class HearingTypeMatchResult {
    public static final int NO_MATCH = 0;

    public static final int MATCH_RULE_1 = 1;

    public static final int MATCH_RULE_2 = 2;

    public static final int MATCH_RULE_3 = 3;

    public static final int MATCH_RULE_4 = 4;

    private int matchRule;

    private Hearing hearing;

    public HearingTypeMatchResult() {
    }

    public HearingTypeMatchResult(int matchLevel, Hearing hearing) {
        this.matchRule = matchLevel;
        this.hearing = hearing;
    }

    public int getMatchRule() {
        return matchRule;
    }

    public Hearing getHearing() {
        return hearing;
    }

    public void setMatchRule(int matchRule) {
        this.matchRule = matchRule;
    }

    public void setHearing(Hearing hearing) {
        this.hearing = hearing;
    }

    public boolean isNewHearingRequired() {
        return (matchRule == NO_MATCH || matchRule == MATCH_RULE_2 || matchRule == MATCH_RULE_3);
    }
}
