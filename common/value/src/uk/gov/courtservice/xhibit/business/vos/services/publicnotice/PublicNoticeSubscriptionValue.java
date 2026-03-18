package uk.gov.courtservice.xhibit.business.vos.services.publicnotice;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: PublicNoticeSubscriptionValue Description: Copyright: Copyright (c)
 * 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pat Fox
 * @created 19 February 2003
 * @version
 * 
 * Used for passing info onto the queue for Public Displays. 19/02/03 - PFOX
 * created
 * </P>
 */

public class PublicNoticeSubscriptionValue extends CSAbstractValue {
	private static final long serialVersionUID = -8678371141795789407L;
    private String m_urn;

    private Integer m_pnEventType;

    private static Logger log = CSServices.getLogger(PublicNoticeSubscriptionValue.class);

    /**
     * Constructor for the CourtLogSubscriptionValue object
     */
    public PublicNoticeSubscriptionValue() {
    }

    /**
     * Constructor for the PublicNoticeSubscriptionValue object
     * 
     * @param urn
     * @param pnEventType
     *            Description of the Parameter
     */
    public PublicNoticeSubscriptionValue(String urn, Integer pnEventType) {
        if (log.isDebugEnabled()) {
            log.debug("PublicNoticeSubscriptionValue(" + urn + "," + pnEventType);
        }
        m_urn = urn;
        m_pnEventType = pnEventType;
    }

    /**
     * sets the urn attribute of the PublicNoticeSubscriptionValue
     * 
     * @param urn
     */
    public void setUrn(String urn) {
        this.m_urn = urn;
    }

    /**
     * gets the urn attribute of the PublicNoticeSubscriptionValue
     * 
     * @param
     */
    public String getUrn() {
        return m_urn;
    }

    /**
     * Sets the pnEventType attribute of the PublicNoticeSubscriptionValue
     * object
     * 
     * @param pnEventType
     *            The new pnEventType value
     */
    public void setPnEventType(Integer pnEventType) {
        this.m_pnEventType = pnEventType;
    }

    /**
     * Gets the pnEventType attribute of the PublicNoticeSubscriptionValue
     * object
     * 
     * @return The pnEventType value
     */
    public Integer getPnEventType() {
        return m_pnEventType;
    }
}
