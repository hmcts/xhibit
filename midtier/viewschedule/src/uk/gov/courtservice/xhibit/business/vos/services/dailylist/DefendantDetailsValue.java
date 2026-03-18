package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;

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
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class DefendantDetailsValue {

    private Integer defendantId;

    private PersonDetailsValue personDetailsValue;

    private String maskedName;

    private String masked;

	private String piturn;

    public DefendantDetailsValue() {
    }

    public DefendantDetailsValue(DefendantBasicValue defendantBasicValue,
            DefendantOnCaseBasicValue defendantOnCaseBasicValue) {
        if (defendantBasicValue == null)
            throw new IllegalArgumentException("defendantBasicValue");

        defendantId = defendantBasicValue.getId();
        personDetailsValue = new PersonDetailsValue(defendantBasicValue.getFirstName(), defendantBasicValue
                .getSurname(), defendantBasicValue.getMiddleName());

        if (defendantOnCaseBasicValue != null) {
            maskedName = defendantOnCaseBasicValue.getMaskedName();
            masked = defendantOnCaseBasicValue.getIsMasked();
        }
        
        this.setPiturn(defendantOnCaseBasicValue.getPtiurn());
    }

    public String getMasked() {
        return masked;
    }

    public String getMaskedName() {
        return maskedName;
    }

    public PersonDetailsValue getPersonDetailsValue() {
        return personDetailsValue;
    }

	public String getPiturn() {
		return piturn;
	}

	public void setPiturn(String piturn) {
		this.piturn = piturn;
	}

}