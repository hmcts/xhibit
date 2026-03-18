package uk.gov.courtservice.framework.services.subscription;

// Framwork
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Joseph Babad
 * @version 1.0
 */

public class CourtLogListenerExample extends XhibitListener {
    private static final Logger log = CSServices.getLogger(CourtLogListenerExample.class);

    public CourtLogListenerExample() {
    }

    public String getListenerType() {
        return "courtLogListener";
    }

    public void newMessage(CSAbstractValue object) {
        String methodName = "newMessage() - ";
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.business.services.subscription.XhibitListener
         *       abstract method
         */
        log.debug(methodName + "called");
    }
}