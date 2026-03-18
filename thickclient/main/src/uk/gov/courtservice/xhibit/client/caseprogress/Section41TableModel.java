package uk.gov.courtservice.xhibit.client.caseprogress;

// Value imports.
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT 2 - Section41 Table Model.
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

public class Section41TableModel extends CaseProgressTableModel {
    /** Column index for column displaying no. count. */
    public static final int NO_COUNT = 0;

    /** Column index for column displaying defendant. */
    public static final int DEFENDANT = 1;

    /** Column index for column displaying plea description. */
    public static final int PLEA = 2;

    /** Column index for column displaying disposal. */
    public static final int DISPOSAL = 3;

    /**
     * Default Constructor.
     */
    public Section41TableModel(Object[] resultsRowValue) {
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
        XHIBITConstant.debug("ROW: " + row);
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug ("RESULTS
        // DATA: "+super.resultsData);
        if (super._data != null)
            XHIBITConstant.debug("RESULTS DATA LENGTH: " + super._data.length);
        ResultsRowValue resultsRowValue = (ResultsRowValue) getDataAt(row); // super.resultsData[row];
        boolean isSameDOF = false;

        if (row > 0) {
            Integer prevDOF = ((ResultsRowValue) getDataAt(row - 1)).getDefendantOnOffenceId();
            isSameDOF = resultsRowValue.getDefendantOnOffenceId().equals(prevDOF);
        }
        String content = "";
        switch (column) {
        case NO_COUNT:
            // if (super.detailsOn) {}
            if (isSameDOF) {
                content = "";
            } else {
                // content = resultsRowValue.getOffenceValue() != null ?
                // resultsRowValue.getOffenceValue().getOffenceDescription() :
                // "";
                OffenceValue ov = resultsRowValue.getOffenceValue();
                if (ov == null) {
                    content = "";
                } else {
                    content = ov.getCrestOffenceSeqNo().toString() + " - " + ov.getOffenceDescription();
                }
            }
            break;
        case DEFENDANT:
            // if (super.detailsOn) {}
            if (isSameDOF) {
                content = "";
            } else {
                content = CaseProgressHelper.buildDefendantName(resultsRowValue.getDefendantValue());
            }
            break;
        case PLEA:
            if (isSameDOF) {
                content = "";
            } else {
                if (super.detailsOn) {
                    content = CaseProgressHelper.getPleaText(resultsRowValue.getPleaValue());
                } else {
                    content = resultsRowValue.getPleaValue() != null
                            && resultsRowValue.getPleaValue().getRefPleaDesc() != null ? resultsRowValue.getPleaValue()
                            .getRefPleaDesc() : "";
                }
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
        if (this.truncateMode) {
            content = this.truncate(content);
        }
        return content;
    }

    /**
     * Initialise the column names.
     */
    public void initColumnNames() {
        java.util.ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.CaseProgressResources);
        setColumnNames(new String[] { XHIBITConstant.getResource(resources, CaseProgressConstants.NO_COUNT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.DEFENDANT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.PLEA_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.DISPOSAL_TXT) });
    }
}
