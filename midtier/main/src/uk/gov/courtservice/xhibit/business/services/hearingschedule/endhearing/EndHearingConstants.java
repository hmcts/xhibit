package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

/**
 * <p>
 * Title: EndHearingConstants
 * </p>
 * <p>
 * Description: This interface holds all static, final variables.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public interface EndHearingConstants {
    public static final int NULL_VALUE = 0;

    // ############################ EXCEPTION KEYS
    // ################################//

    // Exception keys for validation OF start and end date
    public static final String START_AFTER_TODAY = "endhearing.startdate_after_today";

    public static final String END_AFTER_TODAY = "endhearing.enddate_after_today";

    public static final String START_AFTER_END = "endhearing.startdate_after_end";

    // Hearing has already been ended
    public static final String HEARING_ALREADY_ENDED = "endhearing.hearing_already_ended";
    
    public static final String CANNOT_DELETE_END_HEARING_AFTER_FORM_A_EXPORT = "endhearing.cannot_delete_after_form_a_export";

    public static final String DEFENDANT_HEARING_ALREADY_ENDED = "endhearing.defendant_hearing_already_ended";

    // end "linked" hearing that has not been linked.
    // public static final String HEARING_NOT_LINKED =
    // "endhearing.hearing_NOT_LINKED";

}