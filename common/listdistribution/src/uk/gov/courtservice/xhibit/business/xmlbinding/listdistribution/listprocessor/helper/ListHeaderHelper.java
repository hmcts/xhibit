package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListHeader;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListHeaderSummary;

/**
 * <p>
 * Title: ListHeaderHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating ListHeader and ListHeader Summary
 * XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ListHeaderHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class ListHeaderHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(ListHeaderHelper.class);

    /**
     * Create a summary for the detailed listHeader
     * 
     * @param listHeader
     *            the listHeader to create the summary for
     * @return the summary
     */
    public static ListHeaderSummary createSummary(ListHeader listHeader) {
        ListHeaderSummary listHeaderSummary = new ListHeaderSummary();

        listHeaderSummary.setStartDate(listHeader.getStartDate());
        listHeaderSummary.setEndDate(listHeader.getEndDate());
        listHeaderSummary.setVersion(listHeader.getVersion());
        if (listHeader.hasDuration()) {
            listHeaderSummary.setDuration(listHeader.getDuration());
        }

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(listHeaderSummary) + ".");
        }

        return listHeaderSummary;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param listHeader
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(ListHeader listHeader) {
        return "ListHeader[startDate=" + listHeader.getStartDate() + ", endDate=" + listHeader.getEndDate()
                + ", duration=" + listHeader.getDuration() + ", valid=" + listHeader.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param listHeader
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(ListHeaderSummary listHeaderSummary) {
        return "ListHeaderSummary[startDate=" + listHeaderSummary.getStartDate() + ", endDate="
                + listHeaderSummary.getEndDate() + ", duration=" + listHeaderSummary.getDuration() + ", valid="
                + listHeaderSummary.isValid() + "]";
    }

}
