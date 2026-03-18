package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.text.ParseException;
import java.util.Date;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V7DataComponent
 * </p>
 * <p>
 * Description: Use a date control to ensure we have a valid date and is not
 * after todays date
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.7 $
 */
public class V7DataComponent extends DateFieldDataComponent {
    /**
     * Throw a parse exception if date is new valid and is after today
     */
    public Date getValue() throws ParseException {
        Date date = super.getValue();
        if (date != null && date.compareTo(new Date()) > 0) {
            throw new ParseException("Data out of range.", 0);
        }
        return date;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v7DataComponentToolTip");
    }
}