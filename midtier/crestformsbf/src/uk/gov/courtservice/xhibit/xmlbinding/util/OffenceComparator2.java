package uk.gov.courtservice.xhibit.xmlbinding.util;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * Util Class to order offences by Crest Offence Seq No.
 */

public class OffenceComparator2 implements java.util.Comparator {
    private static final OffenceComparator2 instance = new OffenceComparator2();

    public static OffenceComparator2 getInstance() {
        return instance;
    }

    private OffenceComparator2() {

    }

    public int compare(Object object1, Object object2) {
        return compare((OffenceValue) object1, (OffenceValue) object2);

    }

    public int compare(OffenceValue off1, OffenceValue off2) {
        return off1.getCrestOffenceSeqNo().compareTo(off2.getCrestOffenceSeqNo());
    }
}