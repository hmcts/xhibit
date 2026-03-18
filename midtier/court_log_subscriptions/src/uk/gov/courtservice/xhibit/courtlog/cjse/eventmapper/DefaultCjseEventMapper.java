package uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper;

import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventDetail;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: Default CJSE Event mapper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is intended to handle the simplest cases of mapping Court Log
 * events to CJSE events, one to one, many to one. This class is designed to
 * keep the framework symmetrical. Functionally we could easily have made
 * CjseEventDetail implement CjseEventMapper, but the opinion was that it could
 * easily be confusing.
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
public class DefaultCjseEventMapper implements CjseEventMapper {
    // The CJSE events detail for this given mapping.
    private CjseEventDetail _cjseEventDetail;

    /**
     * Public constructor.
     * 
     * @param eventDetail
     *            the CjseEventDetail that is relevant to the mapper.
     */
    public DefaultCjseEventMapper(CjseEventDetail eventDetail) {
        setCjseEventDetail(eventDetail);
    }

    /**
     * Get the CjseEventDetail for this instance of DefaultCjseEventMapper.
     * 
     * @return the CjseEventDetail for this instance of DefaultCjseEventMapper.
     * @param event
     *            not used in simple mapping.
     */
    public CjseEventDetail getCjseEventDetail(CourtLogSubscriptionValue event) {
        return _cjseEventDetail;
    }

    /**
     * Set the CjseEventDetail for this instance of DefaultCjseEventMapper.
     * 
     * @param eventDetail
     *            he CjseEventDetail for this instance of
     *            DefaultCjseEventMapper.
     */
    private void setCjseEventDetail(CjseEventDetail eventDetail) {
        this._cjseEventDetail = eventDetail;
    }

}