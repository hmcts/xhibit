package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V6DataComponent
 * </p>
 * <p>
 * Description: Use a date control to ensure we have a valid date and is
 * bettween 01-JAN-1990 and today inclusive
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.10 $
 */
public class V6DataComponent extends DateFieldDataComponent {
    private static Date START_DATE = new GregorianCalendar(1980, 0, 1).getTime();

    /**
     * Throw a parse exception if date is new valid and is not bettween
     * 01-JAN-1990 and today inclusive
     */
    public Date getValue() throws ParseException {
        Date date = super.getValue();
        if (date != null && (date.compareTo(START_DATE) < 0 || date.compareTo(new Date()) > 0)) {
            throw new ParseException("Data out of range.", 0);
        }
        return date;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v6DataComponentToolTip");
    }
}