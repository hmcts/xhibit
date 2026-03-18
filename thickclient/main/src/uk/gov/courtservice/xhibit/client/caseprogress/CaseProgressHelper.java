package uk.gov.courtservice.xhibit.client.caseprogress;

import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.results.ResultsReferenceFactory;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Formats disposal references to be displayed on the screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.25 $
 */
public class CaseProgressHelper {
    public static final String dispBundle = XhibitBundles.Disposals;

    private static HashMap fullPlea = new HashMap();

    private static final String NEW_LINE = System.getProperty("line.separator");

    private CaseProgressHelper() {
    }

    public static String buildDefendantName(DefendantValue dv) {
        StringBuffer sb = new StringBuffer();
        sb.append(checkNull(dv.getFirstName()));
        if (sb.length() > 0)
            sb.append(" ");
        sb.append(checkNull(dv.getMiddleName()));
        if (sb.length() > 0)
            sb.append(" ");
        sb.append(checkNull(dv.getSurName()));
        return sb.toString();
    }

    private static String checkNull(String toCheck) {
        return (toCheck == null ? "" : toCheck);
    }

    /**
     * Clears the text for all Pleas. To be called each time the Case Progress
     * Screen is loaded for cases that have Pleas.
     */
    public static void clearPleaText() {
        fullPlea.clear();
    }

    public static String getPleaText(PleaValue pv) {
        if (pv != null) {
            if (fullPlea.containsKey(pv.getDefOnChargeOrOffenceID())) {
                return (String) fullPlea.get(pv.getDefOnChargeOrOffenceID());
            }

            StringBuffer sbPlea = new StringBuffer();
            if (pv.getRefPleaDesc() != null)
                sbPlea.append(pv.getRefPleaDesc());
            if (pv.getAltRefOffenceDesc() != null && pv.getAltRefOffenceDesc().length() > 0) {
                sbPlea.append(": ");
                sbPlea.append(pv.getAltRefOffenceDesc());
            }
            if (pv.getOtherPleaText() != null && pv.getOtherPleaText().length() > 0) {
                sbPlea.append(": ");
                sbPlea.append(pv.getOtherPleaText());
            }
            fullPlea.put(pv.getDefOnChargeOrOffenceID(), sbPlea.toString());
            return sbPlea.toString();
        }
        return "";
    }

    /**
     * Returns detailed text for disposal
     * 
     * @param dv
     * @return
     * @throws ResultsControllerException
     */
    public static String getDisposalText(DisposalValue dv) {
        String disposalText = "";
        {
            try {
                if (dv != null) {
                    disposalText = ResultsReferenceFactory.getInstance().getRecordSheetDisposal(
                            dv.getRefDisposalTypeId()).getCaseProgressDetailText(dv);
                }
            } catch (ResultsControllerException rce) {
                // need to handle exception here because this method is called
                // by getValueAt in CriminalTableModel.
                // This method cannot be overidden by one that throws an
                // exception.
                XHIBITErrorHandler.handleError(rce);
            }
        }
        return disposalText;
    }

    /**
     * Returns Summary Text for Disposal
     * 
     * @param dv
     * @return
     * @throws ResultsControllerException
     */
    public static String getSummaryDisposalText(DisposalValue dv) {
        String disposalText = "";
        if (dv != null) {
            try {
                disposalText = ResultsReferenceFactory.getInstance().getRecordSheetDisposal(dv.getRefDisposalTypeId())
                        .getCaseProgressSummaryText(dv);
            } catch (ResultsControllerException rce) {
                // need to handle exception here because this method is called
                // by getValueAt in CriminalTableModel.
                // This method cannot be overidden by one that throws an
                // exception.
                XHIBITErrorHandler.handleError(rce);
            }
        }
        return disposalText;
    }

    public static String getVariationDisposalText(DisposalValue[] disposals) {
        final StringBuffer disposalText = new StringBuffer(500);
        for (int i = 0; i < disposals.length; i++) {
            disposalText.append(getDisposalText(disposals[i]));
            if (i < disposals.length) {
                disposalText.append(NEW_LINE);
            }
        }
        return disposalText.toString();
    }
}
