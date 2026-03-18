package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution;

import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.DailyListProcessor;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.FirmListProcessor;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.WarnedListProcessor;

/**
 * <p>
 * Title: ListProcessorFactory
 * </p>
 * <p>
 * Description: Construct list processors.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ListProcessorFactory.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class ListProcessorFactory {
    // The document types available...
    private static final String DAILY_LIST_FOR_DISTRIBUTION_TYPE = "DLD";

    private static final String FIRM_LIST_FOR_DISTRIBUTION_TYPE = "FLD";

    private static final String WARNED_LIST_FOR_DISTRIBUTION_TYPE = "WLD";

    // The list processors available...
    private static final ListProcessor DAILY_LIST_PROCESSOR = new DailyListProcessor();

    private static final ListProcessor FIRM_LIST_PROCESSOR = new FirmListProcessor();

    private static final ListProcessor WARNED_LIST_PROCESSOR = new WarnedListProcessor();

    /**
     * Stop construction of this static class
     */
    private ListProcessorFactory() {
        // Implemented to change permisions of default constructor
    }

    /**
     * Get the list processor for the document type
     * 
     * @param type
     *            The type of processor to retrieve.
     * @return the new processor
     * @throws ListProcessorException
     *             if a suitable processor can not be found.
     */
    public static ListProcessor getListProcessor(String type) throws ListProcessorException {
        if (DAILY_LIST_FOR_DISTRIBUTION_TYPE.equals(type)) {
            return DAILY_LIST_PROCESSOR;
        } else if (FIRM_LIST_FOR_DISTRIBUTION_TYPE.equals(type)) {
            return FIRM_LIST_PROCESSOR;
        } else if (WARNED_LIST_FOR_DISTRIBUTION_TYPE.equals(type)) {
            return WARNED_LIST_PROCESSOR;
        } else {
            throw new ListProcessorException("Invalid document type: \"" + type + "\"");
        }
    }
}
