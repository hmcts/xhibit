package uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper;

import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventDetail;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: CJSE Event Mapper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instances of this class return the CJSE event specific information for the
 * passed in Court Log Event value object.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public interface CjseEventMapper {

    /**
     * When implemented this method will return the CJSE event specific
     * information for the passed in Court Log Event Value Object.
     * 
     * @param event
     *            the Court Log Event.
     * @return the CJSE event specific information.
     */
    public CjseEventDetail getCjseEventDetail(CourtLogSubscriptionValue event);
}