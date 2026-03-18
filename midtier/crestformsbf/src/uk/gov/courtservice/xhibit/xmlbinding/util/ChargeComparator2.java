package uk.gov.courtservice.xhibit.xmlbinding.util;

import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;

/**
 * Util Class to order Charges by Crest Charge Seq No.
 */

public class ChargeComparator2 implements java.util.Comparator {
    private static final ChargeComparator2 instance = new ChargeComparator2();

    public static ChargeComparator2 getInstance() {
        return instance;
    }

    private ChargeComparator2() {

    }

    public int compare(Object o, Object ob) {
        return compare((ChargeValue) o, (ChargeValue) ob);

    }

    public int compare(ChargeValue ch, ChargeValue charge) {
        return ch.getCrestChargeSeqNo().compareTo(charge.getCrestChargeSeqNo());
    }
}
