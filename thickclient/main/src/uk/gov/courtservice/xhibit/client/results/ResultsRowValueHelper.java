package uk.gov.courtservice.xhibit.client.results;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0
 */

public class ResultsRowValueHelper {
    private static final String MAGISTRATE = getDisposalResource("typeMagistrate");

    private static final String MAGISTRATE_GENERAL = getDisposalResource("typeMagistrateGeneral");

    private static final String CROWN_COURT = getDisposalResource("typeCrownCourt");

    private static final String VARIATION = getDisposalResource("typeVariation");

    private ResultsRowValueHelper() {
    }

    private static String getDisposalResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.Disposals, key);
    }

    /**
     * Determine and return court type for disposal
     * 
     * @return String
     */
    public static String getDisposalCourtType(final DisposalValue disposalValue) {
        if (disposalValue != null) {
            if (disposalValue.getCourtType().equals("M")) {
                if (disposalValue.isUnrelatedDisposal()) {
                    return MAGISTRATE_GENERAL;
                } else {
                    return MAGISTRATE;
                }
            }

            // if (getDisposalValue().getCourtType().equals("C"))
            if (disposalValue.getPsdDisposal2Id() == null) {
                return CROWN_COURT;
            }

            return VARIATION;
        }
        return "";
    }

    /**
     * Gets the defendant name for the given Defendant. This method is required
     * by the ResultSaveValue objects. This is not called be table models
     * 
     * @return the defendant name.
     */
    public static String getDefendantName(final DefendantValue defendantValue,
            final ScheduledHearingValue scheduledHearingValue) {
        if (defendantValue != null) {
            return ResultsHelper.getName(defendantValue);
        }

        StringBuffer sb = new StringBuffer();
        String[] defendants = scheduledHearingValue.getDefendants();
        for (int i = 0; i < defendants.length; i++) {
            sb.append(defendants[i]);
            if (i < (defendants.length - 1)) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

}