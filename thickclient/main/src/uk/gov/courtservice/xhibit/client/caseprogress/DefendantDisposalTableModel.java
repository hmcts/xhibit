package uk.gov.courtservice.xhibit.client.caseprogress;

import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValueHelper;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Defendant Disposal Table Model
 * </p>
 * <p>
 * Description: Displays a table of defendant disposals.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public class DefendantDisposalTableModel extends CaseProgressTableModel {
    public static final int DEFENDANT = 0;

    public static final int DISPOSAL_TYPE = 1;

    public static final int DISPOSAL = 2;

    public DefendantDisposalTableModel(Object[] resultsRowValues) {
        super(resultsRowValues);
    }

    public Object getValueAt(int row, int col) {
        ResultsRowValue resultsRowValue = (ResultsRowValue) getDataAt(row);
        boolean isSameDOF = false;

        if (row > 0) {
            Integer prevDOF = ((ResultsRowValue) getDataAt(row - 1)).getDefendantOnCaseId();
            isSameDOF = resultsRowValue.getDefendantOnCaseId().equals(prevDOF);
        }
        String content = "";

        switch (col) {
        case DEFENDANT:
            if (isSameDOF) {
                content = "";
            } else {
                content = CaseProgressHelper.buildDefendantName(resultsRowValue.getDefendantValue());
            }
            break;
        case DISPOSAL_TYPE:
            content = ResultsRowValueHelper.getDisposalCourtType(resultsRowValue.getDisposalValue());
            break;
        case DISPOSAL:
            if (super.detailsOn) {
                content = CaseProgressHelper.getDisposalText(resultsRowValue.getDisposalValue());
            } else {
                content = CaseProgressHelper.getSummaryDisposalText(resultsRowValue.getDisposalValue());
            }
            break;
        default:
            break;
        }
        if (super.truncateMode) {
            content = super.truncate(content);
        }
        return content;
    }

    /**
     * Initialise the column names.
     */
    public void initColumnNames() {
        setColumnNames(new String[] { getResource(CaseProgressConstants.DEFENDANT_TXT),
                getResource(CaseProgressConstants.COURT_TYPE_TXT), getResource(CaseProgressConstants.DISPOSAL_TXT) });
    }

    /**
     * Get the resource string for the given key.
     * 
     * @param key
     *            the key to lookup in the resource bundle.
     * @return the resource String for the given key.
     */
    private String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, key);
    }
}