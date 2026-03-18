package uk.gov.courtservice.xhibit.xmlbinding.util;

import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;

/**
 * Util Class to order Charges by Crest Charge Seq No.
 */

public class ChargeComparator implements java.util.Comparator {
    private static final ChargeComparator instance = new ChargeComparator();

    public static ChargeComparator getInstance() {
        return instance;
    }

    private ChargeComparator() {

    }

    public int compare(Object o, Object ob) {
        return compare((XhbCharge) o, (XhbCharge) ob);

    }

    public int compare(XhbCharge ch, XhbCharge charge) {
        return ch.getCrestChargeSeqNo().compareTo(charge.getCrestChargeSeqNo());
    }
}
