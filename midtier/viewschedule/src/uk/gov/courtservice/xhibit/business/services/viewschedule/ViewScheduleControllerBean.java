package uk.gov.courtservice.xhibit.business.services.viewschedule;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;

import javax.ejb.SessionBean;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.business.database.query.UnpublishedDailyListQuery;
import uk.gov.courtservice.xhibit.business.database.query.UnpublishedFirmListQuery;
import uk.gov.courtservice.xhibit.business.database.query.UnpublishedWarnedListQuery;
import uk.gov.courtservice.xhibit.business.database.query.dailylist.DailyListQueries;
import uk.gov.courtservice.xhibit.business.database.query.dailylist.DailyListQuery;
import uk.gov.courtservice.xhibit.business.database.query.schedule.ScheduleQuery;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.DailyListValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyList;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithJudge;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithWitness;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.XhbXmlDocumentSimpleValue;

/**
 * <p>
 * Title: The View Schedule Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides all business
 * services required for displaying schedules.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @ejb.bean name="ViewScheduleController" description="View Schedule Controller
 *           Bean" type="Stateless" view-type="both"
 *           jndi-name="ViewScheduleControllerHome"
 *           local-jndi-name="ViewScheduleControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Marie Holmberg, Meeraj Kunnumpurath
 * @version $Id: ViewScheduleControllerBean.java,v 1.30 2006/01/05 14:18:25
 *          bzjrnl Exp $
 */
public class ViewScheduleControllerBean extends CSSessionBean implements SessionBean {
    private static final String CS_DAILYLIST_TO_XHB_DAILYLIST_TRANSFORM = "config/xsl/viewschedule/csDailyListToXhbDailyList.xsl";

    /** JAXP property for schema language */
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /** The schema language to use */
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /** The schema source. This is loaded from the classloader */
    private static final String SCHEMA_SOURCE = "schema/xhbdailylist.xsd";

    /** JAXP property to specify the schema source */
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /** Castor binding */
    private static final String XML_BINDING = "xmlbindings/binding.xml";

    /**
     * The document builder we will use for parsing. Document builders are not
     * guaranteed to be thread safe. However, a bean instance will only run per
     * thread.
     */
    private DocumentBuilder builder;

    /**
     * The Castor marshaller. I don't know whether a Marshaller instance are
     * guaranteed to be thread safe. However, again a bean instance will only
     * run per thread.
     */
    private Mapping mapping;

    /**
     * Bean create method initialzes the document builder and castor mapping
     * 
     */
    public void ejbCreate() {
        try {
            // Initialize the castor mapping
            initCastorMapping();
            // Initialize the JAXP document builder
            initDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw handleException(ex);
        } catch (IOException ex) {
            throw handleException(ex);
        } catch (MappingException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Returns the today's schedule. It will call the schedule query to get the
     * result.
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param courtId
     *            Date
     * @param date
     *            Integer
     * @return uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue
     *         populated from the ScheduleQuery
     */
    public TodaysScheduleValue getTodaysSchedule(Integer courtId, Date date) {

        log.debug("getTodaysSchedule () called :: courtId: " + courtId + " date: " + date);

        TodaysScheduleValue todaysScheduleValue = TodaysScheduleSimpleCache.getCache().retrieve(courtId, date);

        log.debug("getTodaysSchedule() Exited OK");
        return todaysScheduleValue;
    }

    /**
     * This will get the today's scheudle for a given court, date and courtroom.
     * It uses the same query as getTodaysSchedule(courtid, date)
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param courtId
     *            Integer
     * @param date
     *            Date
     * @param courtRoomId
     *            Integer
     * @return uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue
     *         populated from the ScheduleQuery
     */
    public TodaysScheduleValue getTodaysScheduleForCourtRoom(Integer courtId, Date date, Integer courtRoomId) {
        log.debug("getTodaysScheduleForCourtRoom() called :: courtId: " + courtId + " date: " + date + "CourtRoomId "
                + courtRoomId);

        TodaysScheduleValue tsv = new ScheduleQuery().getSchedule(courtId, date, courtRoomId);

        log.debug("getTodaysScheduleForCourtRoom() Exited OK");
        return tsv;
    }

    /**
     * Returns the daily list. It will use the fastlane reader for daily list to
     * get the data from the database and castor to created the xml that
     * conforms to the DailyList.xsd.
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param courtId
     *            Date
     * @param date
     *            Integer
     * @return String - xml string of all the dailylist data.
     * @throws DailyListException
     */
    public java.lang.String getDailyList(Integer courtId, Date date) throws DailyListException {

        log.debug("getDailyList() called :: courtId: " + courtId + " date: " + date);

        // Call the daily list query to get the result.
        DailyListValue dlValue = new DailyListQuery().getDailyList(courtId, date);
        if (dlValue.getCourtSiteValues().size() == 0) {
            throw new DailyListException();
        }

        // Return the validated XML
        return generateXml(dlValue);

    }

    /**
     * Returns the daily list. It will use the fastlane reader for daily list to
     * get the data from the database and castor to created the xml that
     * conforms to the DailyList.xsd.
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param listId  Integer
     * @param showCourtList Boolean
     * @param courtId Integer
     * @return String - xml string of all the dailylist data.
     * @throws DailyListException
     */
    public java.lang.String getUnpublishedDailyList(Integer listId, Boolean showCourtList) throws DailyListException {

        log.debug("getUnpublishedDailyList() called :: listId: " + listId + " showCourtList: " + showCourtList);

        // Call the daily list query to get the result.
        String dailyListXml = new UnpublishedDailyListQuery().getDailyList(listId, showCourtList);
        
        // Return the XML
         return dailyListXml;
    }
    
    /**
     * Returns the firm list. It will create xml that conforms to the FirmList.xsd.
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param listId  Integer
     * @return String - xml string of all the firmlist data.
     * @throws SAXException 
     * @throws IOException 
     */
    public java.lang.String getUnpublishedFirmList(Integer listId){ 

        log.debug("getUnpublishedFirmList() called :: listId: " + listId);

        // Call the daily list query to get the result.
        String firmListXml = new UnpublishedFirmListQuery().getFirmList(listId);
        
        // Return the XML
         return firmListXml;
    }
    
    /**
     * Returns the warned list. It will create xml that conforms to the WarnedList.xsd.
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param listId  Integer
     * @param includeStandardNotes Boolean
     * @param includePriorityNotes Boolean
     * @param includeRestrictedNotes Boolean
     * @param annotated  Boolean - whether the annotated or non-annotated warned list is required
     * @return String - xml string of all the warnedlist data.
     * @throws SAXException 
     * @throws IOException 
     */
    public java.lang.String getUnpublishedWarnedList(Integer listId, boolean includeStandardNotes,
			boolean includePriorityNotes, boolean includeRestrictedNotes, boolean annotated){ 

        log.debug("getUnpublishedWarnedList() called :: listId: " + listId);

        // Call the daily list query to get the result.
        String warnedListXml = new UnpublishedWarnedListQuery().getWarnedList(listId, includeStandardNotes,
    			includePriorityNotes,  includeRestrictedNotes, annotated);
        
        // Return the XML
         return warnedListXml;
    }

    
    
    /**
     * Returns tomorrows list
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param courtId
     *            Date
     * @param date
     *            Integer
     * @return String - xml string of all the dailylist data.
     * @throws TomorrowsListException
     */
    public java.lang.String getTomorrowsList(Integer courtId, Date date) throws TomorrowsListException {
        // This transformation is done in the mid tier to make the xml returned
        // the same format as getDailyList
        // This means that on the frontend they can be handled in exactly the
        // same way
        log.debug("getTomorrowsList called with date: " + date);

        return XSLServices.getInstance().transform(getCSTomorrowsList(courtId, date),
                CS_DAILYLIST_TO_XHB_DAILYLIST_TRANSFORM, null, null);
    }

    /**
     * Get the raw cs tomorrows list from the xhb documents table this is
     * primarily an interface adapter
     * 
     * @param courtId
     *            Date
     * @param date
     *            Integer
     * @return String - xml string of all the dailylist data.
     * @throws TomorrowsListException
     */
    private String getCSTomorrowsList(Integer courtId, Date date) throws TomorrowsListException {
        XhbXmlDocumentSimpleValue xhbXmlDocumentSimpleValue = findLatestListByCourt("DL", courtId, date);
        if (xhbXmlDocumentSimpleValue != null) {
            // get the xml from the clob entity linked by the clob id
            return XhbClobBeanHelper2.findByPrimaryKey(xhbXmlDocumentSimpleValue.getXmlDocumentClobId()).getClobData();
        }

        throw new TomorrowsListException(); // No tomorrows list
    }

    /**
     * Method initializes the Castor mapping
     * 
     * @throws IOException
     * @throws MappingException
     */
    private void initCastorMapping() throws IOException, MappingException {
        // Initialize the mapping
        mapping = new Mapping(getClass().getClassLoader());
        URL mappingUrl = getClass().getClassLoader().getResource(XML_BINDING);
        mapping.loadMapping(mappingUrl);
    }

    /**
     * Method initializes the document builder
     * 
     * @throws ParserConfigurationException
     */
    private void initDocumentBuilder() throws ParserConfigurationException {

        // Create a namespace aware and validating document builder factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Try setting the validation properties
        try {

            // Set the schema language and schema source
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream(SCHEMA_SOURCE);
            factory.setAttribute(JAXP_SCHEMA_SOURCE, schema);

            // Set the parse to validating only if the parser recognises the
            // above properties
            factory.setValidating(true);

        } catch (IllegalArgumentException ex) {
            // This is thrown if the parser is not JAXP 1.2 compliant
            log.error("Schema validation not enabled. Use a JAXP 1.2 parser", ex);
        }

        // Create the builder and set the error handler
        builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new DefaultHandler() {
            public void error(SAXParseException ex) throws SAXException {
                throw ex;
            }
        });
    }

    /**
     * Generates the castor XML
     * 
     * @param dlValue
     *            DailyListValue
     * @return XML String
     */
    private String generateXml(DailyListValue dlValue) {

        // Initalize stuff as not always guaranteed to be done correctly at this point
        try {
            initCastorMapping();
            initDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println("Error parsing (pce) at ViewScheduleControllerBean.generateXml");
            pce.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("Error parsing (ioe) at ViewScheduleControllerBean.generateXml");
            ioe.printStackTrace();
        } catch (MappingException me) {
            System.err.println("Error parsing (me) at ViewScheduleControllerBean.generateXml");
            me.printStackTrace();
        }
        
        try {

            StringWriter writer = new StringWriter();

            // Create the marshaller and set the proeprties
            Marshaller marshaller = new Marshaller(writer);
            marshaller.setMapping(mapping);
            marshaller.setSuppressXSIType(true);

            // Marshall the VO and return the XML
            marshaller.marshal(dlValue);
            writer.close();

            // Generate the XML
            String xml = writer.toString();
            log.debug("Xml: " + xml);

            // Validate the XML
            validateXml(xml);

            return xml;

        } catch (IOException ex) {
            throw handleException(ex);
        } catch (MappingException ex) {
            throw handleException(ex);
        } catch (MarshalException ex) {
            throw handleException(ex);
        } catch (ValidationException ex) {
            throw handleException(ex);
        } catch (SAXException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Validates the XML
     * 
     * @param xml
     *            String
     * @throws IOException
     * @throws SAXException
     */
    private void validateXml(String xml) throws IOException, SAXException {

        // Parse the XML
        StringReader reader = new StringReader(xml);
        builder.parse(new InputSource(reader));

        reader.close();
    }

    /**
     * Utility method for handling exceptions
     * 
     * @param ex
     *            Exception
     * @return CSUnrecoverableException
     */
    private CSUnrecoverableException handleException(Exception ex) {
        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
        return new CSUnrecoverableException(ex);
    }

    /**
     * Returns a XhbXmlDocumentSimpleValue object which is the last list
     * generated on the first day after the date passed in. ie. The last version
     * of the daily list created for 'tomorrow' Note: it is possible to create
     * daily list for multiple days in the future.
     * 
     * @param String
     *            documentType, Integer courtID, Integer dateCreated
     * @return XMLDocumentBasicValue
     */
    private XhbXmlDocumentSimpleValue findLatestListByCourt(String documentType, Integer courtId, Date today) {
        // Get an array of XhbXmlDocumentBasicValue objects for the court and
        // date
        XhbXmlDocumentSimpleValue[] xmlDocumentSimpleValue = DailyListQueries.getNextListByType(courtId, today,
                documentType);

        // If none can be found, then no tomorrow's list is available, return
        // null
        if (xmlDocumentSimpleValue == null || xmlDocumentSimpleValue.length == 0) {
            log.debug("No tomorrow Daily List found");
            return null;
        } else {
            return xmlDocumentSimpleValue[0];
        }
    }

    //
    // Start of copy from
    // uk.gov.courtservice.xhibit.business.services.listdistribution.WLLManagementHelper
    // code in WLLManagementHelper will be removed
    //

    /**
     * Method to acquire the daily list with the most simple data (defendant
     * name, court name, etc), ordered by the defendant name.
     * 
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return A <code>DailyList[]</code> array containing the daily list.
     * 
     * @see uk.gov.courtservice.xhibit.business.database.query.dailylist
     *      .DailyListQueries#getDailyListByDefendant(int, java.util.Date)
     * 
     * @ejb.interface-method view-type="local"
     */
    public DailyList[] getDailyListByDefendant(Integer courtId, Date date) {
        // currently (03/08/2004) used by thinclient witness and probation
        // service
        log.debug("getDailyListBasic() called :: courtId: " + courtId + " date: " + date);

        return DailyListQueries.getDailyListByDefendant(courtId, date);
    }

    /**
     * Method to acquire the daily list with the most simple data, plus the name
     * of the judge, ordered as per the daily list requirements (ordered by the
     * database).
     * 
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return A <code>DailyListWithJudge[]</code> array containing the daily
     *         list.
     * 
     * @see uk.gov.courtservice.xhibit.business.database.query.dailylist
     *      .DailyListQueries#getDailyListWithJudge(int, java.util.Date)
     * 
     * @ejb.interface-method view-type="both"
     */
    public DailyListWithJudge[] getDailyListWithJudge(Integer courtId, Date date) {
        // currently (03/08/2004) used by thinclient probation service
        log.debug("getDailyListWithJudge() called :: courtId: " + courtId + " date: " + date);

        return DailyListQueries.getDailyListWithJudge(courtId, date);
    }

    /**
     * Method to acquire the daily list with the most simple data, plus the name
     * of the judge and extra details used to indicate whether a skeleton
     * schedule exists the schedule been issued and whether there are witnesses
     * on the case. Ordered as per the daily list requirements (ordered by the
     * database).
     * 
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return A <code>DailyListWithWitness[]</code> array containing the
     *         daily list.
     * 
     * @see uk.gov.courtservice.xhibit.business.database.query.dailylist
     *      .DailyListQueries#getDailyListWithWitness(int, java.util.Date)
     * 
     * @ejb.interface-method view-type="local"
     */
    public DailyListWithWitness[] getDailyListWithWitness(Integer courtId, Date date) {
        // currently (03/08/2004) used by thinclient witness
        log.debug("getDailyListWithWitness() called :: courtId: " + courtId + " date: " + date);
        return DailyListQueries.getDailyListWithWitness(courtId, date);
    }

    /**
     * Gets the Court structure for the given court id. i.e. it gets the court
     * sites and their respective court rooms.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtId
     *            the id of the court.
     * @return CourtStructureValue
     */
    public CourtStructureValue getCourtStructure(Integer courtId) {
        return XhbCourtBeanHelper2.getCourtStructure(courtId);
    }
}
