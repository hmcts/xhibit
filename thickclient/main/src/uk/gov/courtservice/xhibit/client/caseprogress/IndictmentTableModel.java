package uk.gov.courtservice.xhibit.client.caseprogress;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: XHIBIT 2 - Indictment Table Model
 * </p>
 * <p>
 * Description: Containts data for the Indictment table. Also holds flags for
 * truncating long text, and displaying more detailed information.
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

public class IndictmentTableModel extends CaseProgressTableModel {
    /** Column index for column displaying offence description. */
    private static final int NO_IND = 0;

    /** Column index for column displaying offence description. */
    private static final int NO_COUNT = 1;

    /** Column index for column displaying defendant description. */
    private static final int DEFENDANT = 2;

    /** Column index for column displaying plea text. */
    private static final int PLEA = 3;

    /** Column index for column displaying plea text. */
    private static final int ARRAIGNMENT_DATE = 4;

    /** Column index for column displaying verdicts. */
    private static final int VERDICT = 5;

    /** Column index for column displaying verdicts. */
    private static final int VCO_FLAG = 6;

    /** Column index for column displaying verdicts. */
    private static final int VCO_DATE = 7;

    /** Column index for column displaying disposals. */
    private static final int DISPOSAL = 8;

    /**
     * Default constructor.
     */
    public IndictmentTableModel(Object[] resultsRowValue) {
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
        ResultsRowValue resultsRowValue = (ResultsRowValue) getDataAt(row); // super.resultsData[row];
        boolean isSameDOF = false;

        if (row > 0) {
            Integer prevDOF = ((ResultsRowValue) getDataAt(row - 1)).getDefendantOnOffenceId();
            isSameDOF = resultsRowValue.getDefendantOnOffenceId().equals(prevDOF);
        }
        String content = "";
        switch (column) {
        case NO_IND:
            if (isSameDOF) {
                content = "";
            } else {
                content = resultsRowValue.getChargeValue().getCrestChargeSeqNo().toString();
            }
            break;
        case NO_COUNT:
            if (isSameDOF) {
                content = "";
            } else {
                content = resultsRowValue.getOffenceDescription();
            }
            break;
        case DEFENDANT:
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
        case ARRAIGNMENT_DATE:
            if (isSameDOF) {
                content = "";
            } else {
                PleaValue pleaValue = resultsRowValue.getPleaValue();
                if (pleaValue != null && pleaValue.getArraignmentDate() != null) {
                    content = XDateFormat.format((java.util.Date) resultsRowValue.getPleaValue().getArraignmentDate(),
                            XDateFormat.DATEFORMAT);
                }
            }
            break;
        case VERDICT:
            if (isSameDOF) {
                content = "";
            } else {
                VerdictValue verdictValue = resultsRowValue.getVerdictValue();
                if (super.detailsOn) {
                    if (verdictValue != null && verdictValue.getRefVerdictDesc() != null) {
                        content = resultsRowValue.getVerdictValue().getRefVerdictDesc();
                        if (verdictValue.getAltRefOffenceDesc() != null
                                && verdictValue.getAltRefOffenceDesc().length() > 0) {
                            content = content + ": " + verdictValue.getAltRefOffenceDesc();
                        }
                        if (verdictValue.getOtherVerdictText() != null
                                && verdictValue.getOtherVerdictText().length() > 0) {
                            content = content + ": " + verdictValue.getOtherVerdictText();
                        }
                    }
                } else {
                    content = verdictValue != null && verdictValue.getRefVerdictDesc() != null ? verdictValue
                            .getRefVerdictDesc() : "";
                }
            }
            break;
        case VCO_FLAG:
            if (isSameDOF) {
                content = "";
            } else {
                content = resultsRowValue.getDefendantOnOffenceValue().getVcoFlag();
            }
            break;
        case VCO_DATE:
            if (isSameDOF) {
                content = "";
            } else {
                if (resultsRowValue.getDefendantOnOffenceValue().getVcoDate() != null) {
                    content = XDateFormat.format((java.util.Date) resultsRowValue.getDefendantOnOffenceValue()
                            .getVcoDate(), XDateFormat.DATEFORMAT);
                }
            }
            break;
        case DISPOSAL:
            DisposalValue dv = resultsRowValue.getDisposalValue();
            if (super.detailsOn) {
                content = CaseProgressHelper.getDisposalText(dv);
            } else {
                content = CaseProgressHelper.getSummaryDisposalText(dv);
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
        setColumnNames(new String[] {
                "No.", // XHIBITConstant.getResource(resources,CaseProgressConstants.NO_COUNT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.NO_COUNT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.DEFENDANT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.PLEA_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.ARRAIGNMENT_DATE_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.VERDICT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.VCO_FLAG_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.VCO_DATE_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.DISPOSAL_TXT) });
    }
}
