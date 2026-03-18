package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: HearingProgressValue
 * </p>
 * <p>
 * Description: Constants used in Hearing Progress
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version $Id: HearingProgressValue.java,v 1.3 2006/06/05 12:28:47 bzjrnl Exp $
 */

public abstract class HearingProgressValue extends CSAbstractValue {
	private static final long serialVersionUID = 7043069690794105843L;
    /**
     * Hearing to be heard.
     */
    public static final Integer TO_BE_HEARD = new Integer(0);

    /**
     * Hearing in progress.
     */
    public static final Integer IN_PROGRESS = new Integer(5);

    /**
     * Hearing adjourned.
     */
    public static final Integer ADJOURNED = new Integer(8);

    /**
     * Hearing finished.
     */
    public static final Integer FINISHED = new Integer(9);

    public HearingProgressValue() {
    }
}