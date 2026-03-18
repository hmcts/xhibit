package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;

import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

import java.util.Comparator;

/**
 * <p>Title: Comparator to use in sorting DisplayDocument instances into
 * a known order for unit test purposes.</p>
 * <p>Description: </p>
 * <p>
 * Sorts the DisplayDocument instances by their DisplayDocumentURI.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Bob Boothby
 * @version 1.0
 */
public class DisplayDocumentComparator implements Comparator
{
    private static final DisplayDocumentComparator _instance =
            new DisplayDocumentComparator();

    /**
     * Do not allow instantiation from outside the instance.
     */
    private DisplayDocumentComparator()
    {

    }

    /**
     * Get the singleton instance of this class.
     * @return the singleton instance of this class.
     */
    public static DisplayDocumentComparator getInstance()
    {
        return _instance;
    }

    /**
     * @param o1 - the first object to be compared.
     * @param o2 - the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     * @throws ClassCastException if the arguments' types prevent them from
     * being compared by this Comparator.
     */
    public int compare(Object o1, Object o2) throws ClassCastException
    {
        DisplayDocument displayDocument1 = (DisplayDocument)o1;
        DisplayDocument displayDocument2 = (DisplayDocument)o2;
        return displayDocument1.getUri().toString().compareTo(
                displayDocument2.getUri().toString());
    }

}
