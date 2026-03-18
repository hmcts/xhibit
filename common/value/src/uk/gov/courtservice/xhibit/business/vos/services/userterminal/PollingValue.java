package uk.gov.courtservice.xhibit.business.vos.services.userterminal;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: Version information for XHIBIT
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Patrick Dunne
 * @version $Id: PollingValue.java,v 1.2 2010/04/19 08:08:36 dunnepi Exp $
 */

public class PollingValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private String pollingInterval;

    private String checkPollingInterval;

    public String getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(String interval) {
        this.pollingInterval = interval;
    }

    public void setCheckPollingInterval(String checkInterval) {
        this.checkPollingInterval = checkInterval;
    }

    public String getCheckPollingInterval() {
        return checkPollingInterval;
    }

 }
