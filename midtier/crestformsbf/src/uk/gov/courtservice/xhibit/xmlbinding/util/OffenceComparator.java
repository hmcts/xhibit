package uk.gov.courtservice.xhibit.xmlbinding.util;

import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;

/**
 * Util Class to order offences by Crest Offence Seq No.
 */

public class OffenceComparator implements java.util.Comparator {
    private static final OffenceComparator instance = new OffenceComparator();

    public static OffenceComparator getInstance() {
        return instance;
    }

    private OffenceComparator() {

    }

    public int compare(Object object1, Object object2) {
        return compare((XhbOffence) object1, (XhbOffence) object2);

    }

    public int compare(XhbOffence off1, XhbOffence off2) {
        return off1.getCrestOffenceSeqNo().compareTo(off2.getCrestOffenceSeqNo());
    }
}