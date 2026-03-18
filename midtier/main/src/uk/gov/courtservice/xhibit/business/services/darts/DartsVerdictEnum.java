
package uk.gov.courtservice.xhibit.business.services.darts;

public enum DartsVerdictEnum implements DartsRetentionPolicyEnum {

    AA("AA","Autrefois Acquit",11402,1029,1),
    AC("AC","Autrefois Convict",11403,1030,2),
    DUD("DUD","Defendant found under disability",11404,1088,2),
    G("G","Guilty",11400,1034,2),
    GA("GA","Not guilty but guilty of alternative offence not charged namely",11405,1082,2),
    GAJ("GAJ","Not guilty but guilty of alternative offence on Judge's direction namely",11406,1083,2),
    GAOJ("GAOJ","Not Guilty but Guilty (By Judge alone under DVC & VA 2004) of Alternative Offence not charged namely",11421,1297,2),
    GJ("GJ","Guilty (by Judge alone under DVC & VA 2004)",11420,1296,2),
    GJJ("GJJ","Guilty (by Jury on Judge's direction)",11407,1089,2),
    GL("GL","Not guilty but guilty of lesser offence not charged namely",11408,1084,2),
    GLJ("GLJ","Not guilty but guilty of lesser offence on Judge's direction namely",11409,1085,2),
    GLOJ("GLOJ","Not Guilty but Guilty (By Judge alone under DVC & VA 2004) of Lesser Offence not charged namely",11422,1298,2),
    JUTA("JUTA","Jury unable to agree",11410,1090,1),
    NG("NG","Not guilty",11401,1035,1),
    NGIS("NGIS","Not Guilty by reason of insanity",11411,1091,1),
    NGJA("NGJA","Not Guilty (by Judge alone under DVC & VA 2004)",11423,1312,2),
    NGJJ("NGJJ","Not guilty (by jury on judge's direction)",11412,1092,1),
    NGJU("NGJU","Not guilty under section 17 Criminal Justice Act (1967)",11413,1093,1),
    NV("NV","No verdict (alternative count)",11414,1094,1),
    O("O","OTHER (ENTER AS FREE TEXT)",11415,1095,1),
    RTG("RTG","Original Jury discharged, unable to agree. Found guilty by another Jury",11416,1096,2),
    RTNG("RTNG","Original Jury discharged, unable to agree. Found not guilty by another Jury",11417,1097,1);
	

	private final String verdictCode;
    private final String verdictDesc;
    private final Integer code;
    private final Integer subcode;
    private final Integer retentionPolicyNo;

    DartsVerdictEnum(String verdictCode, String verdictDesc, Integer code, Integer subcode, Integer retentionPolicyNo) {
        this.verdictCode = verdictCode;
        this.verdictDesc = verdictDesc;
        this.code = code;
        this.subcode = subcode;
        this.retentionPolicyNo = retentionPolicyNo;
    }

    public String getVerdictCode() {
		return verdictCode;
	}

	public String getVerdictDesc() {
		return verdictDesc;
	}

	public Integer getCode() {
		return code;
	}

	public Integer getSubcode() {
		return subcode;
	}

	public Integer getRetentionPolicyNo() {
		return retentionPolicyNo;
	}

    public static DartsVerdictEnum fromCodeAndDesc(String verdictCode, final String verdictDesc) {
        for (DartsVerdictEnum enumValue: DartsVerdictEnum.values()) {
            if (enumValue.verdictCode.equals(verdictCode) &&
            		enumValue.verdictDesc.equals(verdictDesc)) {
                return enumValue;
            }
        }
        return null;
    }
}
