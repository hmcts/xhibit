package uk.gov.courtservice.xhibit.business.services.orders.query;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbAddressValue;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;

/**
 * Class used to process each row generated from the query.
 * 
 * @author czvsws
 * @version $Revision: 1.3 $
 */
public class RefCourtRowProcessor extends AbstractRowProcessor {

    // estimate as to an average size to reduce dynamic expansion...
    private final List results = new ArrayList(1000);

    /**
     * Create an implementation of the <code>TerminalSummary</code> interface
     * based upon the value for the <code>Row</code> passed in.
     * 
     * @param row
     *            The row to process.
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(Row row) {
        XhbRefCourtValue refCourtValue = new XhbRefCourtValue(row.getInteger("REF_COURT_ID"), row
                .getString("COURT_FULL_NAME"), row.getString("COURT_SHORT_NAME"), row.getString("NAME_PREFIX"), row
                .getString("COURT_TYPE"), row.getString("CREST_CODE"), row.getString("REF_COURT_OBS_IND"), row
                .getString("IS_PSD"), row.getString("DX_REF"), row.getTimestamp("COURT_LAST_UPDATE_DATE"), row
                .getTimestamp("COURT_CREATION_DATE"), row.getString("COURT_CREATED_BY"), row
                .getString("COURT_LAST_UPDATED_BY"), row.getInteger("COURT_VERSION"), row
                .getInteger("COURT_ADDRESS_ID"), row.getInt("COURT_ID"));
        refCourtValue.setXhbAddress(new XhbAddressValue(row.getInteger("ADDRESS_ID"), row.getString("ADDRESS_1"), row
                .getString("ADDRESS_2"), row.getString("ADDRESS_3"), row.getString("ADDRESS_4"), row.getString("TOWN"),
                row.getString("COUNTY"), row.getString("POSTCODE"), row.getString("COUNTRY"), row
                        .getTimestamp("ADDRESS_LAST_UPDATE_DATE"), row.getTimestamp("ADDRESS_CREATION_DATE"), row
                        .getString("ADDRESS_CREATED_BY"), row.getString("ADDRESS_LAST_UPDATED_BY"), row
                        .getInteger("ADDRESS_VERSION")));

        results.add(refCourtValue);
    }

    /**
     * Acquire the results generated from the processing of the rows.
     * 
     * @return An array of <code>XhbrefCourtValue</code> value objects.
     */
    public XhbRefCourtValue[] getResults() {
        return (XhbRefCourtValue[]) results.toArray(new XhbRefCourtValue[results.size()]);
    }
}