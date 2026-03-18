package uk.gov.courtservice.xhibit.client.courtlog.util;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class EventConstants {
    /**
     * Event header tag. The court log event header text that is displayed in
     * bold is contained between this and the EVENT_HEADER_END tag.
     */
    public static final String EVENT_HEADER_BEGIN = "<event_header>";

    /**
     * End of event header tag. The court log event header text that is
     * displayed in bold is contained between the EVENT_HEADER_BEGIN tag and
     * this tag.
     */
    public static final String EVENT_HEADER_END = "</event_header>";

    /**
     * Empty event header tag. This tag will be created by a transformation if
     * there is nothing between the EVENT_HEADER_BEGIN and EVENT_HEADER_END
     * tags.
     */
    public static final String EVENT_HEADER_EMPTY = "<event_header/>";

    /**
     * Event text tag. The court log event text that is displayed normally is
     * contained between this and the EVENT_TEXT_END tag.
     */
    public static final String EVENT_TEXT_BEGIN = "<event_text>";

    /**
     * End of event text tag. The court log event text that is displayed in
     * normal is contained between the EVENT_TEXT_BEGIN tag and this tag.
     */
    public static final String EVENT_TEXT_END = "</event_text>";

    /**
     * Empty event text tag. This tag will be created by a transformation if
     * there is nothing between the EVENT_TEXT_BEGIN and EVENT_TEXT_END tags.
     */
    public static final String EVENT_TEXT_EMPTY = "<event_text/>";

    /**
     * Never want to instantiate this class as it only contains constants.
     */
    private EventConstants() {
    }
}