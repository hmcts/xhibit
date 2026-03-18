package uk.gov.courtservice.xhibit.business.database.results.authorise;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;

/**
 * <p>
 * Title: AuthoriseCheckDatabaseManager
 * </p>
 * <p>
 * Description: Performs pre-authorisation checks.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class AuthoriseCheckDatabaseManager extends AbstractXhibitDatabase{
    private static final String GET_AUTHORISE_CHECK_RESULTS = 
        "{ call xhb_authorise_check_pkg.get_warnings(?,?) }";
    
    public AuthoriseWarning[] getAuthoriseWarnings(Integer caseId) {
        final AuthoriseCheckRowProcessor rp = new AuthoriseCheckRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_AUTHORISE_CHECK_RESULTS);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { caseId });
        return rp.getAuthoriseCheck();
    }
    
}
