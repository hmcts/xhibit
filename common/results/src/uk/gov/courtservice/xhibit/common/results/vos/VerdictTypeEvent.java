package uk.gov.courtservice.xhibit.common.results.vos;

public interface VerdictTypeEvent {
    public static final int INVALID_TYPE = -1;

    public static final int GUILTY_MAJORITY_TYPE = 1;

    public static final int GUILTY_UNANIMOUS_TYPE = 2;

    public static final int GUILTY_JUDGES_DIRECTION_TYPE = 3;

    public static final int NOT_GUILTY_TYPE = 4;

    public static final int NOT_GUILTY_JU_TYPE = 5;

    public static final int NOT_GUILTY_JUDGES_DIRECTION_TYPE = 6;

    public static final int ALTERNATE_TYPE = 9;

    public static final int ALTERNATE_JUDGES_DIRECTION_TYPE = 10;

    public static final int LESSER_TYPE = 11;

    public static final int LESSER_JUDGES_DIRECTION_TYPE = 12;

    public static final int OTHER_TYPE = 13;

    public static final int DELETE_VERDICT_TYPE = 14;

    public static final int DELETE_APPEAL_CASE_TYPE = 15;

    public static final int DELETE_APPEAL_OFFENCE_TYPE = 16;

    public static final int DELETE_APPEAL_DISPOSAL_TYPE = 22;

    public static final int CASE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE = 17;

    public static final int OFFENCE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE = 18;

    public static final int OFFENCE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE_ALT = 19;

    public static final int CASE_LEVEL_MISC_APPEAL_RESULT_TYPE = 20;

    public static final int DISPOSAL_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE = 21;
    
    public static final int NOT_GUILTY_JUDGE_UNDER_DUC_VA2004_TYPE = 23;
    
    public static final int GUILTY_BY_JUDGE_ALONE_DVC_VA_TYPE = 24;
    
    public static final int ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA_TYPE = 25; 
    
    public static final int LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA_TYPE = 26;

    // Court Log events - Verdicts
    public static final Integer GUILTY_MAJORITY_EVENT = new Integer(40720);

    public static final Integer GUILTY_UNANIMOUS_EVENT = new Integer(40721);

    public static final Integer NOT_GUILTY_EVENT = new Integer(40722);

    public static final Integer ALTERNATE_OFFENCE_EVENT = new Integer(40725);

    public static final Integer LESSER_OFFENCE_EVENT = new Integer(40726);

    public static final Integer OTHER_EVENT = new Integer(40727);

    public static final Integer GUILTY_EVENT = new Integer(40729);

    public static final Integer GUILTY_JUDGES_DIRECTION_EVENT = new Integer(40736);
    
    public static final Integer GUILTY_BY_JUDGE_ALONE_DVC_VA_EVENT = new Integer(40756);
    
    public static final Integer ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA_EVENT =new Integer(40725); 
    
    public static final Integer LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA_EVENT = new Integer(40726);

    // Court Log events - Criminal Appeals
    public static final Integer CaseLevelCriminalAppealResult = new Integer(40730);

    public static final Integer OffenceLevelCriminalAppealResult = new Integer(40731);

    public static final Integer OffenceLevelCriminalAppealResultWithAltOffence = new Integer(40732);

    /**
     * @todo Create court log event code for appeal result against magistrates
     *       court general disposal.
     */
    public static final Integer DisposalLevelCriminalAppealResult = new Integer(40737);

    // Court Log events - Miscellaneous Appeals
    public static final Integer CaseLevelMiscellaneousAppealResult = new Integer(40733);

    // Court Log events - Deletes
    public static final Integer DeleteVerdictOnIndictment = new Integer(40728);

    public static final Integer DeleteCaseLevelAppealResult = new Integer(40734);

    public static final Integer DeleteOffenceLevelAppealResult = new Integer(40735);

    /**
     * @todo Create court log event code for deleting appeal result against
     *       magistrates court general disposal.
     */
    public static final Integer DeleteDisposalLevelAppealResult = new Integer(40738);

    // Verdict codes
    public static final String GUILTY = "G";

    public static final String GUILTY_JUDGES_DIRECTION = "GJJ";

    public static final String NOT_GUILTY = "NG";

    public static final String NOT_GUILTY_JU = "NGJU";

    public static final String NOT_GUILTY_JUDGES_DIRECTION = "NGJJ";

    public static final String NOT_GUILTY_JUDGE_UNDER_DUC_VA2004 = "NGJA";
    
    public static final String ALTERNATE = "GA";

    public static final String ALTERNATE_JUDGES_DIRECTION = "GAJ";

    public static final String LESSER = "GL";

    public static final String LESSER_JUDGES_DIRECTION = "GLJ";
    
    public static final String GUILTY_BY_JUDGE_ALONE_DVC_VA = "GJ";
    
    public static final String ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA ="GAOJ"; 
    
    public static final String LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA = "GLOJ";

    public static final String CRIM_APP_RESULT = "APP_RESULT";

    public static final String DISP_DEFENDANT_DISCHARGED = "DISCH";

    public static final String DISP_NO_EVIDENCE_OFFERED = "NOEV";

    public static final String GUILTY_BY_OTHER_JURY = "RTG";

    public static final String DEFENDANT_FOUND_UNDER_DISABILITY = "DUD";

    public static final String OTHER = "O";
}
