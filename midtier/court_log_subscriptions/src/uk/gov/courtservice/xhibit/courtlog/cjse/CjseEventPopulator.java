package uk.gov.courtservice.xhibit.courtlog.cjse;

// jdk
import java.util.Locale;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.EventLevelAndIdentifier;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper.CjseEventMapper;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.MessageFactory;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;

/**
 * <p>
 * Title: Cjse Event Populator
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is concerned with populating CJSE events from CourtLogEvents. This
 * class is normally instantiated by the CjseEventPopulatorFactory via the
 * CjseSwitchHandler.
 * </p>
 * <p>
 * The population of a CJSE event can be broken down into three areas:
 * <ul>
 * <li> Identification of the CJSE event that the XHIBIT event maps to, handled
 * by instances of CjseEventMapper. </li>
 * <li> Building the CJSE message text, taking into account
 * internationalisation. Handled by the MessageBuilder </li>
 * <li> Populating the event level related XML data, handled by instances of
 * CjseEventLevelPopulator. </li>
 * </ul>
 * The major role of this class is to hold the appropriate instances of the
 * above interfaces on a per XHIBIT event basis (held in a map in CjseClHelper)
 * and coordinate their application in order to build up the castor bounds java
 * classes representing the CJSE event XML.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby, Sarah Tong
 * @version $Id: CjseEventPopulator.java,v 1.4 2009/08/05 15:59:49 valenzul Exp $
 */
public class CjseEventPopulator {
    private CjseEventMapper _eventMapper;

    private CjseEventLevelPopulator _levelPopulator;

    /**
     * This method will populate the event parameters common to all event levels
     * and then delegate the population of level specific attributes to the
     * appropriate instance of CjseEventLevelPopulator.
     * 
     * @param value
     *            The Court Log value object to use for population.
     * @param theCase
     *            The XhbCase entity to use for population.
     * @param cjseEventParameters
     *            The castor bound object to populate.
     * @param locale
     *            The locale for i18n population.
     * @return the Event Level and Identifier for this event, or null if this
     *         event is not sent to CJSE
     */
    public EventLevelAndIdentifier populateCjseEvent(CourtLogSubscriptionValue value, XhbCase theCase,
            EventParameters cjseEventParameters, Locale locale) {
        CjseEventDetail cjseEventDetail = _eventMapper.getCjseEventDetail(value);

        if (cjseEventDetail == null) {
            // there is no CJSE Event for this Xhibit event
            return null;
        }

        cjseEventParameters.setEventTypeID(cjseEventDetail.getCjseId());
        cjseEventParameters.setEventTime(value.getCourtLogViewValue().getEntryDate());
        cjseEventParameters.setEventLocation(theCase.getXhbCourt().getCrestCourtId());
        cjseEventParameters.setMessageText(MessageFactory.getMessageForMessageCode(
                cjseEventDetail.getCjseMessageCode(), locale, value, theCase));
        // default this value to false until we know when CJSE intend to use it
        cjseEventParameters.setIsUrgent(false);

        EventLevelAndIdentifier levelAndIdentifier = _levelPopulator.populate(cjseEventParameters, theCase, value);

        // would like to use an assertion here but not till we move to jdk1.4..
        if (levelAndIdentifier.getEventLevel().equals(new Integer(-1))) {
            throw new CSUnrecoverableException("Event Level -1 should never be returned. A populator with "
                    + "starting event level -1 should select the relevant level " + "within the populate method.");
        } else {
            return levelAndIdentifier;
        }
    }



    /**
     * This method will populate the event parameters common to all event levels
     * and then delegate the population of level specific attributes to the
     * appropriate instance of CjseEventLevelPopulator.
     * 
     * @param value
     *            The Court Log value object to use for population.
     * @param theCase
     *            The XhbCase entity to use for population.
     * @param cjseEventParameters
     *            The castor bound object to populate.
     * @param xhibtEventId
     *            The XHIBIT event id           
     * @return the Event Level and Identifier for this event, or null if this
     *         event is not sent to CJSE
     */
    public EventLevelAndIdentifier populateDartsEvent(CourtLogSubscriptionValue value, XhbCase theCase,
            EventParameters dartsEventParameters, Integer xhibtEventId) {
        CjseEventDetail cjseEventDetail = _eventMapper.getCjseEventDetail(value);

        if (cjseEventDetail == null) {
            // there is no CJSE Event for this Xhibit event
            return null;
        }

        dartsEventParameters.setEventTypeID(cjseEventDetail.getCjseId());
        dartsEventParameters.setEventTime(value.getCourtLogViewValue().getEntryDate());
        dartsEventParameters.setEventLocation(theCase.getXhbCourt().getCrestCourtId());
        dartsEventParameters.setMessageText(MessageFactory.getDartsMessageForMessageCode( 
                value, theCase, cjseEventDetail.getCjseMessageCode(), xhibtEventId.toString()));
        
        // default this value to false until we know when CJSE intend to use it
        dartsEventParameters.setIsUrgent(false);

        EventLevelAndIdentifier levelAndIdentifier = _levelPopulator.populate(dartsEventParameters, theCase, value);

        // would like to use an assertion here but not till we move to jdk1.4..
        if (levelAndIdentifier.getEventLevel().equals(new Integer(-1))) {
            throw new CSUnrecoverableException("Event Level -1 should never be returned. A populator with "
                    + "starting event level -1 should select the relevant level " + "within the populate method.");
        } else {
            return levelAndIdentifier;
        }
    }

    
    /**
     * 
     * @param eventMapper
     */
    public void setEventMapper(CjseEventMapper eventMapper) {
        _eventMapper = eventMapper;
    }

    public void setLevelPopulator(CjseEventLevelPopulator levelPopulator) {
        _levelPopulator = levelPopulator;
    }
    
 }