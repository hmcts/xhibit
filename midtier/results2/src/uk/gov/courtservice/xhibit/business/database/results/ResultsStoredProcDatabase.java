package uk.gov.courtservice.xhibit.business.database.results;

import java.sql.Types;
import uk.gov.courtservice.framework.jdbc.core.AbstractDatabaseCall;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;

import uk.gov.courtservice.xhibit.business.database.results.processor.DisposalCountForResultsRowProcessor;

/**
 * <p>
 * Title: ResultsStoredProcDatabase
 * </p>
 * <p>
 * Description: The facade interface for fast lane readers that use stored procs
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version 1.0
 */
public class ResultsStoredProcDatabase extends AbstractDatabaseCall {
    // SQL used by this query
    private static final String DISPOSAL_SQL = "{ call xhb_crestformsbtof_pkg.number_of_unrelated_disposals ( ?, ? ) }";
    /**
     * The stored proc called by this was developed for Crest forms B to F
     * The B to F database class can't be referenced from Results as a cyclic build error would occur
     * - it was decided not to refactor that class for reusability due to the unneccessary impact on Forsm B to F
     * @param defOnCaseId
     * @return the number of unrelated disposals for defendant
     */
    public int getNumberOfUnrelatedDisposals(Integer defOnCaseId) {

        final StoredProcedure sp = createStoredProcedure(DISPOSAL_SQL);
        sp.registerInTypes(new int[] { Types.INTEGER });

        final DisposalCountForResultsRowProcessor rowProcessor = new DisposalCountForResultsRowProcessor();

        sp.setRowProcessor(rowProcessor);

        sp.execute(new Object[] { defOnCaseId });

        return rowProcessor.getNumber();
    }
}
