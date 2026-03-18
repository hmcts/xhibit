package uk.gov.courtservice.xhibit.courtlog.cjse;

/**
 * <p>
 * Title: CJSE event detail
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class encapsulates the fields necessary to complete population of a CJSE
 * event.
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
public class CjseEventDetail {
    // The CJSE event id.
    private String _cjseId;

    // The message code. At present this is intended to be looked
    // up in a resource bundle for i18n before populating with
    // requisite fields.
    private String _cjseMessageCode;

    /**
     * Simple constructor containing the fields.
     * 
     * @param cjseId
     *            The cjse event id.
     * @param cjseMessageCode
     *            The CJSE message code for look up in an i18n manner.
     */
    public CjseEventDetail(String cjseId, String cjseMessageCode) {
        setCjseId(cjseId);
        setCjseMessageCode(cjseMessageCode);
    }

    /**
     * Get the CJSE event ID.
     * 
     * @return the CJSE event ID.
     */
    public String getCjseId() {
        return _cjseId;
    }

    /**
     * Get the CJSE message code.
     * 
     * @return the CJSE message code.
     */
    public String getCjseMessageCode() {
        return _cjseMessageCode;
    }

    /**
     * Set the CJSE message code.
     * 
     * @param cjseId
     *            the CJSE message code.
     */
    public void setCjseId(String cjseId) {
        this._cjseId = cjseId;
    }

    /**
     * Set the CJSE message code.
     * 
     * @param cjseMessageCode
     *            the CJSE message code.
     */
    public void setCjseMessageCode(String cjseMessageCode) {
        this._cjseMessageCode = cjseMessageCode;
    }

    public String toString() {

        return getClass().getName() +

        "[" + "_cjseId=" + _cjseId + ",_cjseMessageCode=" + _cjseMessageCode + "]";
    }

}