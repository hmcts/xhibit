package uk.gov.courtservice.xhibit.client.caseprogress;

// Value imports.
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT 2 - Commitals Table Model
 * </p>
 * <p>
 * Description:
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

public class CommitalsTableModel extends CaseProgressTableModel {
    /** Column index for column displaying offence description. */
    public static final int NO_OFFENCE = 0;

    /** Column index for column displaying defendant description. */
    public static final int DEFENDANT = 1;

    /** Column index for column displaying disposals. */
    public static final int DISPOSAL = 2;

    /**
     * Default constructor.
     */
    public CommitalsTableModel(Object[] resultsRowValue) {
        super(resultsRowValue);
    }

    /**
     * Overrides the corresponding method in XTableHelperModel for retrieving
     * data to be displayed.
     * 
     * @param row
     *            row of the table cell invoking this method
     * @param column
     *            column of the table cell invoking this method
     * 
     * @return value to be stored in the cell.
     */
    public Object getValueAt(int row, int column) {
        String content = "";
        ResultsRowValue resultsRowValue = (ResultsRowValue) getDataAt(row); // super.resultsData[row];
        boolean isSameDOF = false;

        if (row > 0) {
            Integer prevDOF = ((ResultsRowValue) getDataAt(row - 1)).getDefendantOnOffenceId();
            isSameDOF = resultsRowValue.getDefendantOnOffenceId().equals(prevDOF);
        }
        switch (column) {
        case NO_OFFENCE:
            // if (isSameDOF)
            // {
            // content = "";
            // }
            // else
            // {
            // content = resultsRowValue.getOffenceDescription();
            // }
            // break;
            // ************************************
            if (isSameDOF) {
                content = "";
            } else {
                OffenceValue ov = resultsRowValue.getOffenceValue();
                if (ov == null) {
                    content = "";
                } else {
                    content = ov.getCrestOffenceSeqNo().toString() + " - " + ov.getOffenceDescription();
                }
            }
            break;
        case DEFENDANT:
            if (isSameDOF) {
                content = "";
            } else {
                content = CaseProgressHelper.buildDefendantName(resultsRowValue.getDefendantValue());
            }
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
        java.util.ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.CaseProgressResources);
        setColumnNames(new String[] { XHIBITConstant.getResource(resources, CaseProgressConstants.NO_OFFENCE_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.DEFENDANT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.DISPOSAL_TXT) });
    }
}