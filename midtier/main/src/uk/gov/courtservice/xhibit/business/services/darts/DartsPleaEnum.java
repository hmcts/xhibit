
package uk.gov.courtservice.xhibit.business.services.darts;

public enum DartsPleaEnum implements DartsRetentionPolicyEnum {

    AA("AA","Autrefois Acquit",11300,1029,1),
    AC("AC","Autrefois Convict",11301,1030,2),
	CPG("CPG","Change of Plea: Not guilty to guilty (no jury sworn)",11304,1033,2),
	CPGJ("CPGJ","Change of Plea: Not guilty to guilty (after jury sworn)",11303,1032,2),
	CPNG("CPNG","Change of Plea: Guilty to not guilty",11302,1031,1),
	G("G","Guilty",11305,1034,2),
	GAO("GAO","Guilty to alternative offence not charged namely",11306,1040,2),
	GLO("GLO","Guilty to lesser offence namely",11307,1039,2),
	NG("NG","Not Guilty",11308,1035,1),
	NPT("NPT","No Plea taken",11309,1036,1),	
	O("O","OTHER (ENTER AS FREE TEXT)",11310,1037,1),
	P("P","Pardon",11311,1038,1);
	
    private final String pleaCode;
    private final String pleaDesc;
    private final Integer code;
    private final Integer subcode;
    private final Integer retentionPolicyNo;

    DartsPleaEnum(String pleaCode, String pleaDesc, Integer code, Integer subcode, Integer retentionPolicyNo) {
        this.pleaCode = pleaCode;
        this.pleaDesc = pleaDesc;
        this.code = code;
        this.subcode = subcode;
        this.retentionPolicyNo = retentionPolicyNo;
    }

    public String getPleaCode() {
		return pleaCode;
	}

	public String getPleaDesc() {
		return pleaDesc;
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

    public static DartsPleaEnum fromCodeAndDesc(String pleaCode, final String pleaDesc) {
        for (DartsPleaEnum enumValue: DartsPleaEnum.values()) {
            if (enumValue.pleaCode.toLowerCase().equals(pleaCode.toLowerCase()) &&
            		enumValue.pleaDesc.toLowerCase().equals(pleaDesc.toLowerCase())) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(pleaCode);
    }
}
