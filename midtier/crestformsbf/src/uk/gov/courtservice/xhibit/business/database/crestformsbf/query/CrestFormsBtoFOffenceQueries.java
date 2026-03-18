package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import uk.gov.courtservice.framework.jdbc.core.AbstractDatabaseCall;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.DisposalCountRowProcessor;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.OffenceCountRowProcessor;

/**
 * A utility class used to obtain offence information for CREST Forms B to F
 * 
 * @author Neil Entwistle
 * @version
 */
public class CrestFormsBtoFOffenceQueries extends AbstractDatabaseCall {
    // SQL used by this query
    private static final String OFFENCE_SQL = "{ call xhb_crestformsbtof_pkg.number_of_offences ( ?, ?, ?, ? ) }";

    private static final String DISPOSAL_SQL = "{ call xhb_crestformsbtof_pkg.number_of_unrelated_disposals ( ?, ? ) }";

    /**
     * @return the number of offences of type offenceType, for case and
     *         defendant
     */
    public int getNumberOfOffences(Integer caseId, Integer defendantId, String offenceType) {

        // Check the parameters
        validateParametersNotNull("xhb_crestformsbtof_pkg.number_of_offences", new Object[] { caseId, defendantId,
                offenceType });

        final StoredProcedure sp = createStoredProcedure(OFFENCE_SQL);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.VARCHAR });

        final OffenceCountRowProcessor rowProcessor = new OffenceCountRowProcessor();
        sp.setRowProcessor(rowProcessor);

        sp.execute(new Object[] { caseId, defendantId, offenceType });

        return rowProcessor.getNumber();

    }

    /**
     * @return the number of unrelated disposals for defendant
     */
    public int getNumberOfUnrelatedDisposals(Integer defendantId) {

        // Check the parameters
        validateParametersNotNull("xhb_crestformsbtof_pkg.number_of_unrelated_disposals", new Object[] { defendantId });

        final StoredProcedure sp = createStoredProcedure(DISPOSAL_SQL);
        sp.registerInTypes(new int[] { Types.INTEGER });

        final DisposalCountRowProcessor rowProcessor = new DisposalCountRowProcessor();
        sp.setRowProcessor(rowProcessor);

        sp.execute(new Object[] { defendantId });

        return rowProcessor.getNumber();

    }

    // *************************************************************************
    // *************************************************************************
    // *************************************************************************

    /**
     * Utility method to validate that the passed in array of values does not
     * contain any <i>null</i> values.
     * 
     * @param procedureName
     *            The name of the procedure we should be executing, used only if
     *            an error occurs to construct the message.
     * @param values
     *            The array of values to check for <i>null</i>'s.
     * @throws IllegalArgumentException
     *             if the passed in values array contains any <i>null</i>
     *             values.
     */
    private void validateParametersNotNull(final String procedureName, final Object[] values) {
        // ensure that all of the passed parameters are not null...
        for (int i = 0, n = values.length; i < n; i++) {
            if (values[i] == null) {
                final String error = procedureName + ": null parameter supplied: " + Arrays.asList(values);
                log.error(error);
                throw new IllegalArgumentException(error);
            }
        }
    }

    /**
     * Extraction of common code to convert the passed in <code>Date</code>
     * object to a <code>Timestamp</code> object to be passed to the stored
     * procedures/functions.
     * 
     * @param input
     *            The <code>Date</code> to convert.
     * @return The converted <code>Timestamp</code>.
     */
    private Timestamp convertDateToTimestamp(final Date input) {
        return ((input != null) ? new Timestamp(input.getTime()) : null);
    }
}