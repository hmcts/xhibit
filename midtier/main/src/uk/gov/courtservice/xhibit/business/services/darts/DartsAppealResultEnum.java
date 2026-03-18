
package uk.gov.courtservice.xhibit.business.services.darts;

public enum DartsAppealResultEnum {

	AB   ("AB"   ,"Appeal abandoned before court.",null, DartsAppealRetentionRulesEnum.USE_MAGS_DISPOSAL),
	ACA  ("ACA"  ,"Appeal against conviction allowed.",null, DartsAppealRetentionRulesEnum.USE_NG_ACQUITTAL),
	ACALO("ACALO","Appeal against conviction allowed -","Lesser offence substituted namely", DartsAppealRetentionRulesEnum.USE_LESSER_OFFENCE),
	ACD  ("ACD"  ,"Appeal against conviction dismissed.",null, DartsAppealRetentionRulesEnum.USE_MAGS_DISPOSAL),
	ACDSI("ACDSI","Appeal against conviction dismissed;","Sentence increased.", DartsAppealRetentionRulesEnum.USE_INCREASED_SENTENCE_DISPOSAL),
	ACDSV("ACDSV","Appeal against conviction dismissed;","Appeal against sentence allowed - sentence varied.", DartsAppealRetentionRulesEnum.USE_CROWN_VARIATION_DISPOSAL),
	AD   ("AD"   ,"Appeal against conviction and sentence dismissed.",null, DartsAppealRetentionRulesEnum.USE_MAGS_DISPOSAL),
	ADAC ("ADAC" ,"Appeal dismissed;","Abandoned in court.", DartsAppealRetentionRulesEnum.USE_MAGS_DISPOSAL),
	ADSI ("ADSI" ,"Appeal against conviction and sentence dismissed;","Sentence increased.", DartsAppealRetentionRulesEnum.USE_INCREASED_CROWN_SENTENCE_DISPOSAL),
	ASASV("ASASV","Appeal against sentence allowed - sentence varied.",null, DartsAppealRetentionRulesEnum.USE_VARIED_SENTENCE_DISPOSAL),
	ASD  ("ASD"  ,"Appeal against sentence dismissed.",null, DartsAppealRetentionRulesEnum.USE_MAGS_DISPOSAL),
	ASDSI("ASDSI","Appeal against sentence dismissed; Sentence increased.",null, DartsAppealRetentionRulesEnum.USE_INCREASED_SENTENCE),
	REM  ("REM"  ,"Plea held to be equivocal; case remitted to Magistrates","Court for plea of not guilty to be entered.", DartsAppealRetentionRulesEnum.USE_MAGS_DISPOSAL);
	
    private final String code;
    private final String desc1;
	private final String desc2;
	private final DartsAppealRetentionRulesEnum rulesEnum;
    
    DartsAppealResultEnum(String code, String desc1, String desc2, DartsAppealRetentionRulesEnum rulesEnum) {
        this.code = code;
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.rulesEnum = rulesEnum;
    }

    public String getCode() {
		return code;
	}

	public String getDesc1() {
		return desc1;
	}
	
	public String getDesc2() {
		return desc2;
	}
	
	public DartsAppealRetentionRulesEnum getRulesEnum() {
		return rulesEnum;
	}

	public static DartsAppealResultEnum fromCodeAndDesc(String code, final String desc1, final String desc2) {
        for (DartsAppealResultEnum enumValue: DartsAppealResultEnum.values()) {
            if (isMatch(enumValue.getCode(),code) &&
            		isMatch(enumValue.getDesc1(),desc1) &&
            		isMatch(enumValue.getDesc2(),desc2)) {
                return enumValue;
            }
        }
        return null;
    }
	
	private static boolean isMatch(String string1, String string2) {
		return (string1 == null && string2 == null) || 
				(string1 != null && string1.toLowerCase().equals(string2.toLowerCase()));
	}
}
