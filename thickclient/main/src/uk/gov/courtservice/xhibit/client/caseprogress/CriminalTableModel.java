package uk.gov.courtservice.xhibit.client.caseprogress;

import java.util.Collection;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.appealresults.AppealResultsHelper;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: XHIBIT 2 - Criminal Table Model
 * </p>
 * <p>
 * Description: Model of data for the Criminal table.
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

public class CriminalTableModel extends CaseProgressTableModel {
    /** Column index for column displaying offence description. */
    public static final int OFFENCE = 0;

    /** Column index for column displaying appellant. */
    public static final int APPELLANT = 1;

    /** Column index for column displaying appeal against. */
    public static final int APPEAL_AGAINST = 2;

    /** Column index for column displaying result. */
    public static final int RESULT = 3;

    public static final int COURTTYPE = 4;

    /** Column index for column displaying disposal. */
    public static final int DISPOSAL = 5;

    private static final String APPEAL_AGAINST_RESOURCE = "appealAgainst.";

    private Collection appealOffenceResults;

    /**
     * Default constructor
     */
    public CriminalTableModel(Object[] resultsRowValues) throws CSRecoverableException {
        super(resultsRowValues);
        appealOffenceResults = AppealResultsHelper.getCrimOffenceRefData();
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
        VerdictValue verdictValue = resultsRowValue.getVerdictValue();
        RefAppResultBasicValue rarbv = AppealResultsHelper.getRefAppResultBasicValue(resultsRowValue,
                appealOffenceResults);

        boolean isSameDOF = false;

        if (row > 0) {
            Integer prevDOF = ((ResultsRowValue) getDataAt(row - 1)).getDefendantOnOffenceId();
            isSameDOF = resultsRowValue.getDefendantOnOffenceId().equals(prevDOF);
        }
        String content = "";
        switch (column) {
        case OFFENCE:
            if (isSameDOF) {
                content = "";
            } else {
                content = resultsRowValue.getOffenceDescription();
            }
            break;
        case APPELLANT:
            if (isSameDOF) {
                content = "";
            } else {
                content = CaseProgressHelper.buildDefendantName(resultsRowValue.getDefendantValue());
            }
            break;
        case APPEAL_AGAINST:
            if (isSameDOF) {
                content = "";
            } else if (resultsRowValue.getDefendantOnOffenceValue().getAppealAgainstType() != null) {
                content = XHIBITConstant.getResource(XhibitBundles.CaseProgressResources, APPEAL_AGAINST_RESOURCE
                        + resultsRowValue.getDefendantOnOffenceValue().getAppealAgainstType());
            } else {
                content = "";
            }
            break;
        case RESULT:
            if (isSameDOF) {
                content = "";
            } else {
                if (super.detailsOn) {
                    if (verdictValue != null) {
                        content = AppealResultsHelper.getAppealResultDescription(rarbv);
                        if (verdictValue.getAltRefOffenceDesc() != null
                                && verdictValue.getAltRefOffenceDesc().length() > 0) {
                            content = content + ": " + verdictValue.getRefAppealOffenceDesc();
                        }
                    }
                } else {
                    content = verdictValue != null ? AppealResultsHelper.getAppealResultDescription(rarbv) : "";
                }
            }
            break;
        case COURTTYPE:
            // get courtType
            content = "";
            if (resultsRowValue.getDisposalValue() != null) {
                if (resultsRowValue.getDisposalValue().getCourtType().equals("M")) {
                    content = XHIBITConstant.getResource(XhibitBundles.CaseProgressResources,
                            CaseProgressConstants.MAGISTRATE_TXT);
                    break;
                }

                if (resultsRowValue.getDisposalValue().getPsdDisposal2Id() == null) {
                    content = XHIBITConstant.getResource(XhibitBundles.CaseProgressResources,
                            CaseProgressConstants.CROWN_COURT_TXT);
                    break;
                } else {
                    content = XHIBITConstant.getResource(XhibitBundles.CaseProgressResources,
                            CaseProgressConstants.VARIATION_TXT);
                    break;
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
        if (super.truncateMode) {
            content = super.truncate(content);
        }
        return content;
    }

    /**
     * Initialise the column name.
     */
    public void initColumnNames() {
        java.util.ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.CaseProgressResources);
        setColumnNames(new String[] { XHIBITConstant.getResource(resources, CaseProgressConstants.NO_OFFENCE_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.APPELLANT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.APPEAL_AGAINST_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.RESULT_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.COURT_TYPE_TXT),
                XHIBITConstant.getResource(resources, CaseProgressConstants.DISPOSAL_TXT) });
    }
}