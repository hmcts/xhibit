package uk.gov.courtservice.xhibit.client.caseprogress;

import java.util.Date;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2 - Breach Table Model
 * </p>
 * <p>
 * Description: Provide a model of breach data for the breaches table.
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

public class BreachTableModel extends CaseProgressTableModel {
    /** Column index for column displaying no. */
    public static final int NO = 0;

    /** Column index for column displaying Proceedings code. */
    public static final int PROCEEDINGS_CODE = 1;

    /** Column index for column displaying defendant. */
    public static final int DEFENDANT = 2;

    /** Column index for column displaying plea. */
    public static final int PLEA = 3;

    /** Column index for column displaying plea. */
    public static final int DATEPUT = 4;

    /** Column index for column displaying offence. */
    public static final int OFFENCE = 5;

    /** Column index for column displaying disposal. */
    public static final int DISPOSAL = 6;

    /**
     * Default Constructor
     */
    public BreachTableModel(Object[] resultsRowValue) {
        super(resultsRowValue);
        Sorter.sort(resultsRowValue, new String[] { "chargeSequenceNumber", "defendantOnChargeId" }, Boolean.TRUE);
    }

    public Object getValueAt(int row, int column) {
        ResultsRowValue resultsRowValue = (ResultsRowValue) getDataAt(row); // super.resultsData[row];
        boolean isSameDOC = false;
        boolean isSameDOF = false;

        if (row > 0) {
            Integer prevDOC = ((ResultsRowValue) getDataAt(row - 1)).getDefendantOnChargeId();
            if (prevDOC != null && resultsRowValue.getDefendantOnChargeId() != null) {
                isSameDOC = resultsRowValue.getDefendantOnChargeId().equals(prevDOC);
            }
            Integer prevDOF = ((ResultsRowValue) getDataAt(row - 1)).getDefendantOnOffenceId();
            if (prevDOF != null && resultsRowValue.getDefendantOnOffenceId() != null) {
                isSameDOF = resultsRowValue.getDefendantOnOffenceId().equals(prevDOF);
            }
        }
        String content = "";
        switch (column) {
        case NO:
            if (isSameDOC) {
                content = "";
            } else {
                if (resultsRowValue.getChargeValue() == null) {
                    content = "";
                } else {
                    if (resultsRowValue.getChargeValue().getCrestChargeSeqNo() == null) {
                        content = "";
                    } else {
                        content = resultsRowValue.getChargeValue().getCrestChargeSeqNo().toString();
                    }
                }
            }
            break;
        case PROCEEDINGS_CODE:
            if (isSameDOC) {
                content = "";
            } else {
                content = resultsRowValue.getChargeValue().getBreachValue().getHoCode() + " - "
                        + resultsRowValue.getChargeValue().getBreachValue().getHoDescription();
            }
            break;
        case DEFENDANT:
            if (isSameDOC) {
                content = "";
            } else {
                content = CaseProgressHelper.buildDefendantName(resultsRowValue.getDefendantValue());
            }
            break;
        case PLEA:
            if (isSameDOC) {
                content = "";
            } else {
                if (resultsRowValue.getPleaValue() != null) {
                    if (resultsRowValue.getPleaValue().getBreachAdmitted() == null) {
                        content = "";
                    } else if (resultsRowValue.getPleaValue().getBreachAdmitted().equals(Boolean.TRUE)) {
                        content = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.true");
                    } else if (resultsRowValue.getPleaValue().getBreachAdmitted().equals(Boolean.FALSE)) {
                        content = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.false");
                    }
                }
            }
            break;
        case DATEPUT:
            if (isSameDOC) {
                content = "";
            } else {
                if (resultsRowValue.getBreachDatePut() != null && resultsRowValue.getBreachDatePut() instanceof Date) {
                    content = XDateFormat.format((Date) resultsRowValue.getBreachDatePut(), XDateFormat.DATEFORMAT);
                } else {
                    content = "";
                }
            }
            break;
        case OFFENCE:
            if (isSameDOF) {
                content = "";
            } else {
                content = resultsRowValue.getOffenceDescription();
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
     * Implement the super abstract class to store the column names.
     */
    public void initColumnNames() {
        setColumnNames(new String[] { getResource(CaseProgressConstants.NO_TXT),
                getResource(CaseProgressConstants.PROCEEDINGS_CODE_TXT),
                getResource(CaseProgressConstants.DEFENDANT_TXT), getResource(CaseProgressConstants.PLEA_TXT),
                getResource(CaseProgressConstants.DATE_PUT_TXT), getResource(CaseProgressConstants.NO_OFFENCE_TXT),
                getResource(CaseProgressConstants.DISPOSAL_TXT) });
    }

    /**
     * Get a case progress resource for the given resource key.
     * 
     * @param key
     *            the key to lookup in the case progress resources.
     * @return the case progress resource string.
     */
    private String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, key);
    }
}
