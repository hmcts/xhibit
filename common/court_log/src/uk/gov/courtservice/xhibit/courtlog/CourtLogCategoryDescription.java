package uk.gov.courtservice.xhibit.courtlog;

/**
 * Class used to hold the constants for category descriptions. Used to find all
 * court log events in particular categories.
 * 
 * @author tz0d5m
 * @version $Revision: 1.6 $
 */
public class CourtLogCategoryDescription {
    /**
     * The category description that represents all start events for the
     * scheduled hearing time calculation
     */
    public static final String SCHEDULED_HEARING_TIME_START_CATEGORY_DESC = "Scheduled_Hearing_Time_Start";

    /**
     * The category description that represents all stop events for the
     * scheduled hearing time calculation
     */
    public static final String SCHEDULED_HEARING_TIME_STOP_CATEGORY_DESC = "Scheduled_Hearing_Time_Stop";

    /**
     * The category description that represents all end hearing events
     */
    public static final String END_HEARING_CATEGORY_DESC = "End_Hearing";

    /**
     * The category description that represents all witness sworn events
     */
    public static final String WITNESS_SWORN_CATEGORY_DESC = "Witness_Sworn";

    /**
     * The category description that represents all witness released events
     */
    public static final String WITNESS_RELEASED_CATEGORY_DESC = "Witness_Released";

    /**
     * The category description that represents all witness events
     */
    public static final String WITNESS_CATEGORY_DESC = "Witness";

    private CourtLogCategoryDescription() {
        // private constructor to prevent external instantiation...
    }
}
