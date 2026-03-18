package uk.gov.courtservice.xhibit.business.database.results.authorise;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseValue;
/**
 * <p>
 * Title: UnauthorisedCasesDatabaseManager
 * </p>
 * <p>
 * Description: Manages access to the XHIBIT data base for the Unauthorised Cases.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class UnauthorisedCasesDatabaseManager extends AbstractXhibitDatabase{
    private static final String GET_UNAUTHORISED_CASES = "{ call xhb_unauthorised_cases_pkg.get_unauthorised_cases(?,?) }";
    
    public UnauthorisedCaseValue[] getUnauthorisedCases(Integer courtId) {
        final UnauthorisedCasesRowProcessor rp = new UnauthorisedCasesRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_UNAUTHORISED_CASES);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId });
        return rp.getUnauthorisedCases();
    }
    
}
