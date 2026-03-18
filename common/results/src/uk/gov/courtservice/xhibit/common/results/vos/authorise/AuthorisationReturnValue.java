package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AuthorisationReturnValue.java,v 1.4 2005/01/28 08:33:24 rzvddy
 *          Exp $
 */

public class AuthorisationReturnValue extends CSAbstractValue {
	
	static final long serialVersionUID = -7144909323113592452L;
	
    private final CaseAuthorisationReturnValue caseAuth;

    private final DefendantAuthorisationReturnValue[] defAuth;

    private final DefendantOnCaseAuthorisationReturnValue[] defOnCaseAuth;

    private final D20OffenceLinkReturnValue OffenceLink;
    
    public AuthorisationReturnValue(CaseAuthorisationReturnValue caseAuthorisation,
            DefendantAuthorisationReturnValue[] defAuthorisation,
            DefendantOnCaseAuthorisationReturnValue[] defOnCaseAuthorisation,
            D20OffenceLinkReturnValue OffenceLink){
        this.caseAuth = caseAuthorisation;
        this.defAuth = defAuthorisation;
        this.defOnCaseAuth = defOnCaseAuthorisation;
        this.OffenceLink = OffenceLink;
    }

	public CaseAuthorisationReturnValue getCaseAuthorisationReturnValue() {
        return caseAuth;
    }

    public DefendantAuthorisationReturnValue[] getDefendantAuthorisationReturnValue() {
        return defAuth;
    }

    public DefendantOnCaseAuthorisationReturnValue[] getDefOnCaseAuthorisationReturnValue() {
        return defOnCaseAuth;
    }

    public D20OffenceLinkReturnValue getOffenceLink() {
		return OffenceLink;
	}

}