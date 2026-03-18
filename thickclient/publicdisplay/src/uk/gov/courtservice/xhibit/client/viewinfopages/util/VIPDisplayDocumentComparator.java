package uk.gov.courtservice.xhibit.client.viewinfopages.util;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationDisplayDocument;

/**
 * <p>
 * Title: Comparator used to sort display document basic values alphabetically
 * but Welsh document types first.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Initial naive implementation that sorts purely based on the crest court room
 * number. Implemented as a 'factory' style singleton so that we can easily
 * change implementation in the future.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: VIPDisplayDocumentComparator.java,v 1.1 2005/12/02 12:19:57
 *          szfnvt Exp $
 */

public class VIPDisplayDocumentComparator implements Comparator {
    private static final VIPDisplayDocumentComparator _instance = new VIPDisplayDocumentComparator();

    private VIPDisplayDocumentComparator() {
    }

    /**
     * Get a comparator to sort court rooms.
     * 
     * @return A class implementing Comparator able to sort by
     *         <code>XhbDisplayDocumentBasicValue</code>.
     */
    public static final Comparator getInstance() {
        return _instance;
    }

    /**
     * The compare method will order the display documents Currently there are
     * only Welsh and English documents The documents need to be order with the
     * Welsh version for each document type to appear first. The laguage type
     * for Welsh is 'cy and for English can be 'en' or null(if default). Before
     * comparison the language is appended to the document name and because 'cy'
     * is alphabetically before 'en' this document is ordered before. If there
     * is no language for the document type then this is the default locale and
     * should be the last in the order so 'zz' is appended for sorting purposes.
     */
    public int compare(Object o1, Object o2) {
        VIPDisplayConfigurationDisplayDocument sort1 = ((VIPDisplayConfigurationDisplayDocument) o1);
        VIPDisplayConfigurationDisplayDocument sort2 = ((VIPDisplayConfigurationDisplayDocument) o2);
        String docName1 = PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAYDOCUMENT
                + sort1.getDescriptionCode());
        String docName2 = PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAYDOCUMENT
                + sort2.getDescriptionCode());

        // If there is no language for the document then append 'zz'. Curren
        if (docName1 != null) {
            docName1 = docName1.concat(sort1.getLanguage() != null ? sort1.getLanguage() : "zz");
        }

        if (docName2 != null) {
            docName2 = docName2.concat(sort2.getLanguage() != null ? sort2.getLanguage() : "zz");
        }

        return docName1.compareTo(docName2);
    }

    public boolean equals(Object obj) {
        return this == obj;
    }
}