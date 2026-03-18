package uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue;

import java.util.Date;

/**
 * <p>
 * Title: CourtLogChargesPrintValue
 * </p>
 * <p>
 * Description: This object holds related court log entries for printing the
 * court log.
 * 
 * creates a common base class for the objects which hold court log view values
 * for printing court log events.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell (xDevelopement 2003)
 * @version 1.0
 */
public class RelatedCourtLogEntriesPrintValue extends CourtLogControllerPrintCompositeValue {
    /**
     * The related type this is used to identify this object in the xsl
     */
    private static final String RELATED_TYPE = "related";
    
    private static final long serialVersionUID = 4126282878570035049L;

    /**
     * Construct a blank RelatedCourtLogEntriesPrintValue
     * 
     * @param the
     *            date of the related entries (used for sorting)
     */
    public RelatedCourtLogEntriesPrintValue(Date date) {
        super(RELATED_TYPE, date);
    }
}
