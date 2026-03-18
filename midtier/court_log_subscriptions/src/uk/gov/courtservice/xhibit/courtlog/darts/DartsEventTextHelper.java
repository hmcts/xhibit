package uk.gov.courtservice.xhibit.courtlog.darts;

import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.DefendantNameMessageElement;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.DefendantNamesMessageElement;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/***
 * 
 * <p>
 * Title: DartsEventTextHelper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 *      Helper Class to build EventText for DARTS messages, handles the cases for defendant level events 
 *      that need Defendant name entered, witness events that need witness info entered (name or number) and 
 *      events that need prosecution or defence information adding to them.
 *      
 *      This class holds a list of Witness, defandant and prosectution/defence level events in _dartsTextProperties,
 *      the keys for these events relate to their cjseMessage code and the value is either 
 *      
 *      DEF for DEFENDANT, 
 *      DEFS for DEFENDANTS,
 *      WIT for WITNESS
 *      PA for Prosecution Advocate
 *      DA for Defence Advocate
 *      
 *      This class also holds a list of xpaths for witness events so that for each XHIBIT id, witness information
 *      can be obtained from the XHIBIT court log clob.
 * </p>
 * <p>
 *      Company: logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.1 20090730 
 */
public class DartsEventTextHelper {
    
    /** Logger */
    private static Logger LOG = Logger.getLogger(DartsEventTextHelper.class);
  
    /* 
     * Data structure holding cjse message codes for witness and defandant level events. 
     * Each message code has a flag to identify them as defendant or witness level events. 
     * The map is populated by the DartsEventText.properties file at instantiation.
     * */
    private static Properties _dartsTextProperties;
   /*
    * This structure contains a list of XHIBIT codes that relate to 
    * witness events and has xpath to witness info wihtin the court log clob as it's values
    */
    private static Properties _witnessNodes;
    /* Singleton Instance */
    private static final DartsEventTextHelper _instance = new DartsEventTextHelper();
    /* Classes used for comparison to determine if event needs Defendant information */
    private static DefendantNameMessageElement _defElement = new DefendantNameMessageElement();
    private static DefendantNamesMessageElement _defsElement = new DefendantNamesMessageElement();     
    private static ProsecutionDefenceMessageElement _defenProsElement = new ProsecutionDefenceMessageElement();
    
    /**
     * The component properties file name.
     * <p>
     * Used as:
     * <code>CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME)</code>
     * <p>
     * <code>DartsWitnessInfo</code>
     */
    public static final String PROPERTIES_FILE_NAME = "dartsEventText";
    public static final String WTINESS_PROPERTIES_FILE_NAME = "dartsWitnessEventText";
    
    public static final String DEFENDANT = "DEF";
    public static final String DEFENDANTS = "DEFS";
    public static final String APPELLANT = "APP";
    public static final String WITNESS = "WIT";
    public static final String PROSECUTION = "PA";
    public static final String DEFENCE = "DA";
    public static final String DEFENCE_APPLICATION = "DAP";
    
    public static final String WITNESS_BACKUP_FLAG = "BK";
    
    /*** 
     * Singleton Class so protect against instantiation 
     */
    private DartsEventTextHelper(){
        _dartsTextProperties = CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME);
        _witnessNodes = CSServices.getConfigServices().getProperties(WTINESS_PROPERTIES_FILE_NAME);
    }
    
    /***
     *  Singleton accessor 
     */
    public static DartsEventTextHelper getInstance(){
        return _instance;
    }
    
     
    /***
     *  Build the DARTS EventText String dependant on event type, only Defandant level events and Witness
     *  events have additional EventText values here, these are extracted from the Case.
     * 
     * @param cjseMessageCode
     * @param messageBuilder
     * @param value
     * @param theCase
     * @param xhibitEventId
     * @return
     */
    public String getDartsEventText(String cjseMessageCode, CourtLogSubscriptionValue value,
            XhbCase theCase, String xhibitEventId){
        
        /* If the xhibit event requires additional information to be present on the DARTS event then it will
         * have an entry in _dartsTextProperties.
         */
        String eventText = "";
        String dartsPropertyPreTrim = _dartsTextProperties.getProperty(cjseMessageCode);
        String dartsProperty = null;
        if (dartsPropertyPreTrim != null){
            dartsProperty = dartsPropertyPreTrim.trim();
       
        }
        
        //LOG.debug("DARTS property is " + dartsProperty + " for CJSE event " + cjseMessageCode + ".");
        if(dartsProperty != null)
        {
            /* Defendant level event */ 
            if(dartsProperty.equals(DEFENDANT))
            {
                //LOG.debug("DEFENDANT event being processed.");
                eventText = "[Defendant: " + _defElement.getElement(value, theCase) + "]";
            }
            /* Multiple Defendant event */
            else if (dartsProperty.equals(DEFENDANTS))
            {
                //LOG.debug("DEFENDANTS event being processed.");                
                eventText = "[Defendants: " + _defsElement.getElement(value, theCase) + "]";
            }
            /* Appellant event */
            else if (dartsProperty.equals(APPELLANT))
            {
                //LOG.debug("APPELANT event being processed.");
                eventText = "[Appellant: " + _defsElement.getElement(value, theCase) + "]";
            }
            /* Prosecution events */
            else if (dartsProperty.equals(PROSECUTION) )
            {
                //LOG.debug("PROSECUTION event being processed.");
                eventText = "[Prosecution: " + _defenProsElement.getProsData(value) + "]";
            }
            /* Defence events */
            else if (dartsProperty.equals(DEFENCE) )
            {
                //LOG.debug("DEFENCE event being processed.");
                eventText = "[Defendant: " + _defsElement.getElement(value, theCase) + "]" +
                        "[Defence: " + _defenProsElement.getDefenData(value) + "]";
            }
            /* Defence Application events */
            else if (dartsProperty.equals(DEFENCE_APPLICATION) )
            {
                //LOG.debug("DEFENCE APPLICATION event being processed.");
                eventText = "[Defence: " + _defenProsElement.getAllDefenData(value) + "]";
            }
            /* Witness event */
            else if (dartsProperty.equals(WITNESS))
            {
                String logEntry = value.getCourtLogViewValue().getLogEntry();
                String witnessInfo = null;
                String xpath = _witnessNodes.getProperty(xhibitEventId);
                if(xpath != null) 
                {
                    try {
                        witnessInfo = CSServices.getXMLServices().getXpathValueFromXmlString(logEntry, xpath);
                        eventText = "[Witness: " + witnessInfo + "]";
                    } 
                    catch (CSUnrecoverableException e) 
                    {   
                        /* if the primary witness information is not present, i.e. the name, then use
                         * the secondary option to determine witness number.  Secondaries are denoted by BK after is
                         * XHIBIT id in the properties file.
                         */
                         xpath = _witnessNodes.getProperty(xhibitEventId + WITNESS_BACKUP_FLAG);
                         if(xpath != null)
                         {    
                            try {
                                witnessInfo = CSServices.getXMLServices().getXpathValueFromXmlString(logEntry,xpath);
                                eventText = "[Witness: " + witnessInfo + "] ";
                            } 
                            catch (CSUnrecoverableException e2) 
                            {   
                                // Do nothing, XPATH unavailable.
                                LOG.warn(e2.getMessage());
                            }
                         }   
                    }// end of first catch
                 }// end of 1st if
            }// end of WIT if
        }
        
        return eventText;
    }// end of getDartsEventText
       
}// end of class
