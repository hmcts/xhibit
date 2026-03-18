package uk.gov.courtservice.xhibit.business.services.listdistribution;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.castor.xml.ClassDescriptorResolverFactory;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.listdistribution.ListDistribution2Database;
import uk.gov.courtservice.xhibit.business.database.listdistribution.ListDistribution2DatabasePersist;
import uk.gov.courtservice.xhibit.business.entities.xhb_document_control.XhbDocumentControlBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_document_distribution.XhbDocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_document_distribution.XhbDocumentDistributionBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_control.XhbWllControlBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_xml_document.XhbXmlDocument;
import uk.gov.courtservice.xhibit.business.entities.xhb_xml_document.XhbXmlDocumentBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.listdistribution.ListDistributionException;
import uk.gov.courtservice.xhibit.business.services.formatting.FormattingControllerLocal;
import uk.gov.courtservice.xhibit.business.services.formatting.FormattingControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.DistributionStatusComplexValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.DocumentControlComplexValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.ListSummary;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;

/**
 * Controller bean to act as a facade over the processing of any available
 * documents in the database. The only remotely available method is the
 * <code>processList()</code> method, all other method are accessed via the
 * local bean interface to allow for complex transactional processing.
 * 
 * The main transaction of writing the documents to the database must rollback
 * if any error occurs, however, the status must be set to either success or
 * failure, which must occur inside another transaction. Local bean reference
 * were used to remove the complexities involved in writing custom, error-prone
 * transaction processing code.
 * 
 * @ejb.bean name="ListDistribution2Controller" description="List Distribution 2
 *           Controller Bean" type="Stateless" view-type="both"
 *           jndi-name="ListDistribution2ControllerHome"
 *           local-jndi-name="ListDistribution2ControllerLocalHome"
 * 
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 * @author tz0d5m
 * @version $Id: ListDistribution2ControllerBean.java,v 1.21 2005/11/24 08:44:53
 *          tzj8k5 Exp $
 */
public class ListDistribution2ControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 1L;

    // The Schemas, and xml prefix and suffix
    private static final String DAILY_LIST_RINGOUT_TYPE = getProperty("xsl.type.dailylistringout");

    private static final String DAILY_LIST_LETTERS_TYPE = getProperty("xsl.type.dailylistletters");

    private static final String DAILY_LIST_XML_PREFIX = getProperty("xml.prefix.dailylist");

    private static final String DAILY_LIST_XML_SUFFIX = getProperty("xml.suffix.dailylist");

    private static final String FIRM_LIST_TYPE = getProperty("xsl.type.firmlist");

    private static final String FIRM_LIST_XML_PREFIX = getProperty("xml.prefix.firmlist");

    private static final String FIRM_LIST_XML_SUFFIX = getProperty("xml.suffix.firmlist");

    private static final String WARNED_LIST_TYPE = getProperty("xsl.type.warnedlist");

    private static final String WARNED_LIST_XML_PREFIX = getProperty("xml.prefix.warnedlist");

    private static final String WARNED_LIST_XML_SUFFIX = getProperty("xml.suffix.warnedlist");

    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static final Logger log = CSServices.getLogger(ListDistribution2ControllerBean.class);

    // set in the ejbCreate() method...
    private ListDistribution2Database database;

    private ListDistribution2DatabasePersist databasePersist;

    private ListDistribution2Workflow workflow;

    private FormattingControllerLocal formattingController;

    /**
     * Initialises all of the instance variables for this session bean.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();

        this.database = new ListDistribution2Database();
        this.databasePersist = new ListDistribution2DatabasePersist();
        this.workflow = new ListDistribution2Workflow();

        this.formattingController = (FormattingControllerLocal) CSServices.getEJBServices().createLocalSession(
                FormattingControllerLocalHome.class);
    }

    //
    // Distribution Status
    // 

    /**
     * Get the distribution status for lists and letters.
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public DistributionStatusComplexValue[] getDistributionStatus(final Integer courtId) {
        List valueList = new ArrayList();

        // Add the document control data
        DocumentControlComplexValue[] documentControlComplexValues = database.getDocumentControlComplexValues(courtId);
        for (int i = 0; i < documentControlComplexValues.length; i++) {
            valueList.add(documentControlComplexValues[i]);
        }

        // Add the wll control data for letters which require printing or have
        // been printed!
        WllControlComplexValue[] wllControlComplexValues = database.getWllControlComplexValues(courtId);
        for (int i = 0; i < wllControlComplexValues.length; i++) {
            if (wllControlComplexValues[i].hasPost()) {
                valueList.add(wllControlComplexValues[i]);
            }
        }

        return (DistributionStatusComplexValue[]) valueList
                .toArray(new DistributionStatusComplexValue[valueList.size()]);
    }

    /**
     * Mark the distribution records as deleted
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public void delete(DistributionStatusComplexValue[] values) {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof DocumentControlComplexValue) {
                    delete((DocumentControlComplexValue) values[i]);
                } else if (values[i] instanceof WllControlComplexValue) {
                    delete((WllControlComplexValue) values[i]);
                } else {
                    throw new ListDistributionException("Unrecognised DistributionStatusComplexValue type \""
                            + values[i].getClass().getName() + "\".");
                }
            }
        }
    }

    private void delete(DocumentControlComplexValue value) {
        if (value != null) {
            value.delete();
            XhbDocumentControlBeanHelper2.update(value.getDocumentControlBasicValue());
        }
    }

    private void delete(WllControlComplexValue value) {
        if (value != null) {
            value.delete();
            XhbWllControlBeanHelper2.update(value.getBasicValue());
        }
    }

    //
    // Recipient Management
    //

    /**
     * Get the wll recipient values for the specified court who have specified a
     * delivery method.
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public WllRecipientComplexValue[] getSpecifiedRecipients(final Integer courtId) {
        return database.getSubscribedWllRecipientComplexValues(courtId);
    }

    /**
     * Get the wll recipient for the specified court with no delivery method
     * specified.
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public WllRecipientComplexValue[] getDefaultRecipients(final Integer courtId) {
        return database.getUnsubscribedWllRecipientComplexValues(courtId);
    }

    // Recipients Set Delivery

    /**
     * Subscribe the wll recipient complex values.
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public WllRecipientComplexValue[] setDeliveryMethod(WllRecipientComplexValue[] recipients) {
        if (log.isDebugEnabled()) {
            log.debug(toDebug("Before Set", recipients));
            WllRecipientComplexValue[] newRecipients = _setDeliveryMethod(recipients);
            log.debug(toDebug("After Set", newRecipients));
            return newRecipients;
        } else {
            return _setDeliveryMethod(recipients);
        }
    }

    private WllRecipientComplexValue[] _setDeliveryMethod(WllRecipientComplexValue[] recipients) {
        // Execute Inserts and Updates then refresh from the db to ensure we
        // have
        // the values updated by db triggers note uses workflow to create a new
        // transaction to ensure that the values are persisted to the DB store
        // before being refreshed!
        return refresh(workflow.setDeliveryMethodPersist(recipients));
    }

    /**
     * Set the wll recipient complex values, note the returned values do not
     * contain the values updated by DB triggers!
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public WllRecipientComplexValue[] setDeliveryMethodPersist(WllRecipientComplexValue[] recipients) {

        if (recipients != null) {
            for (int i = 0; i < recipients.length; i++) {
                recipients[i] = setDeliveryMethodPersist(recipients[i]);
            }
            return recipients;
        } else {
            return null;
        }
    }

    private WllRecipientComplexValue setDeliveryMethodPersist(WllRecipientComplexValue recipient) {
        if (recipient != null) {
            // Update the recipient record
            XhbWllRecipientBasicValue recipientBasicValue = recipient.getRecipientBasicValue();
            if (recipientBasicValue.getWllRecipientId() != null) {
                recipientBasicValue = XhbWllRecipientBeanHelper2.update(recipientBasicValue);
            } else {
                recipientBasicValue = XhbWllRecipientBeanHelper2.create(recipientBasicValue);
            }
            recipient.setRecipientBasicValue(recipientBasicValue);

            // If we have a document distribution record update or create it
            XhbDocumentDistributionBasicValue distributionBasicValue = recipient.getDocumentDistributionBasicValue();
            if (distributionBasicValue != null) {
                if (distributionBasicValue.getDocDistributionId() != null) {
                    distributionBasicValue = XhbDocumentDistributionBeanHelper2.update(distributionBasicValue);
                } else {
                    distributionBasicValue.setWllRecipientId(recipientBasicValue.getWllRecipientId());
                    distributionBasicValue = XhbDocumentDistributionBeanHelper2.create(distributionBasicValue);
                }
                recipient.setDocumentDistributionBasicValue(distributionBasicValue);
            }
        }

        return recipient;
    }

    // Recipients Clear Delivery Method

    /**
     * Unsubscribe the wll recipient complex values.
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public WllRecipientComplexValue[] clearDeliveryMethod(WllRecipientComplexValue[] recipients) {
        if (log.isDebugEnabled()) {
            log.debug(toDebug("Before Clear", recipients));
            WllRecipientComplexValue[] newRecipients = _clearDeliveryMethod(recipients);
            log.debug(toDebug("After Clear", newRecipients));
            return newRecipients;
        } else {
            return _clearDeliveryMethod(recipients);
        }
    }

    private WllRecipientComplexValue[] _clearDeliveryMethod(WllRecipientComplexValue[] recipients) {
        // Execute Inserts and Updates then refresh from the db to ensure we
        // have
        // the values updated by db triggers note uses workflow to create a new
        // transaction to ensure that the values are persisted to the DB store
        // before being refreshed!
        return refresh(workflow.clearDeliveryMethodPersist(recipients));
    }

    /**
     * Unsubscribe the wll recipient complex values, note the returned values do
     * not contain the values updated by DB triggers!
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public WllRecipientComplexValue[] clearDeliveryMethodPersist(WllRecipientComplexValue[] recipients) {
        if (recipients != null) {
            for (int i = 0; i < recipients.length; i++) {
                recipients[i] = clearDeliveryMethodPersist(recipients[i]);
            }
            return recipients;
        } else {
            return null;
        }
    }

    private WllRecipientComplexValue clearDeliveryMethodPersist(WllRecipientComplexValue recipient) {
        if (recipient != null) {
            // Update the recipient record
            XhbWllRecipientBasicValue recipientBasicValue = recipient.getRecipientBasicValue();
            if (recipientBasicValue.getWllRecipientId() != null) {

                recipientBasicValue = XhbWllRecipientBeanHelper2.update(recipientBasicValue);
            } else {
                recipientBasicValue = XhbWllRecipientBeanHelper2.create(recipientBasicValue);
            }
            recipient.setRecipientBasicValue(recipientBasicValue);

            // If we have a document distribution record delete it
            XhbDocumentDistributionBasicValue distributionBasicValue = recipient.getDocumentDistributionBasicValue();
            if (distributionBasicValue != null) {
                if (distributionBasicValue.getDocDistributionId() != null) {
                    XhbDocumentDistributionBeanHelper2.remove(distributionBasicValue);
                    distributionBasicValue = null;
                }
                recipient.setDocumentDistributionBasicValue(distributionBasicValue);
            }
        }
        return recipient;
    }

    // Recipients Refresh & Debug

    /**
     * Refresh the recipient values to ensure they have any fields updated by
     * the db triggers.
     */
    private WllRecipientComplexValue[] refresh(WllRecipientComplexValue[] recipients) {
        for (int i = 0; i < recipients.length; i++) {
            recipients[i] = refresh(recipients[i]);
        }
        return recipients;
    }

    private WllRecipientComplexValue refresh(WllRecipientComplexValue recipient) {
        XhbWllRecipientBasicValue recipientBasicValue = recipient.getRecipientBasicValue();
        if (recipientBasicValue != null) {
            recipient.setRecipientBasicValue(XhbWllRecipientBeanHelper2.findByPrimaryKeyValue(recipientBasicValue
                    .getPrimaryKey()));
        }

        XhbDocumentDistributionBasicValue distributionBasicValue = recipient.getDocumentDistributionBasicValue();
        if (distributionBasicValue != null) {
            recipient.setDocumentDistributionBasicValue(XhbDocumentDistributionBeanHelper2
                    .findByPrimaryKeyValue(distributionBasicValue.getPrimaryKey()));
        }
        return recipient;
    }

    private String toDebug(String message, WllRecipientComplexValue[] recipients) {
        StringBuffer buffer = new StringBuffer(message);
        buffer.append(": ");
        if (recipients != null) {
            if (0 < recipients.length) {
                buffer.append(recipients[0]);
                for (int i = 1; i < recipients.length; i++) {
                    buffer.append(", ");
                    buffer.append(recipients[i]);
                }
            } else {
                buffer.append("empty");
            }
        } else {
            buffer.append("null");
        }
        return buffer.toString();
    }

    //
    // List Management
    //

    // Control Get List Data

    /**
     * Get all the list control data for a given court
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public WllControlComplexValue[] getLists(Integer courtId) {
        return database.getWllControlComplexValues(courtId);
    }

    /**
     * Get the xml for the letters for the list described in value
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public String[] getLetterXml(WllControlComplexValue value, boolean includePost, boolean includeEmail,
            boolean includeFax) {
        return database.getLetterXml(value.getWllControlId(), includePost, includeEmail, includeFax);
    }

    /**
     * Format Letters xml into PDF
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="RequiresNew"
     */
    public String getLettersFop(WllControlComplexValue value, boolean asRingOut, boolean includePost,
            boolean includeEmail, boolean includeFax) {
        String lettersXML = getLettersXml(value, includePost, includeEmail, includeFax);

        byte[] buffer = formattingController.formatDocument("POST", "FOP", getDocumentType(value, asRingOut), value
                .getMajorSchemaVersion(), value.getMinorSchemaVersion(), value.getLanguage(), value.getCountry(),
                lettersXML, null);

        try {
            return new String(buffer, 0, buffer.length, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("Could not encode UTF-8 output.", e);
            return new String(buffer, 0, buffer.length);
        }
    }

    /**
     * Get the xml for the letters
     */
    private String getLettersXml(WllControlComplexValue value, boolean includePost, boolean includeEmail,
            boolean includeFax) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getXmlPrefix(value));
        String[] letterXml = getLetterXml(value, includePost, includeEmail, includeFax);
        for (int i = 0; i < letterXml.length; i++) {
            // Strip XML declaration if at start of letters
            if (letterXml[i].startsWith(XML_DECLARATION)) {
                buffer.append(letterXml[i].substring(XML_DECLARATION.length()));
            } else {
                buffer.append(letterXml[i]);
            }
        }
        buffer.append(getXmlSuffix(value));
        return buffer.toString();
    }

    // Control Authorize Lists

    /**
     * Delete the lists specified by the values
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public void deleteLists(WllControlComplexValue[] values) {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                values[i].delete();
                XhbWllControlBeanHelper2.update(values[i].getBasicValue());
            }
        }
    }

    // Control Authorize Lists

    /**
     * Update to indicate that letters which require printing have been
     * authorized.
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public WllControlComplexValue[] authorizeLists(WllControlComplexValue[] values) {
        if (log.isDebugEnabled()) {
            log.debug(toDebug("Before authorizeList", values));
            WllControlComplexValue[] newValues = _authorizeLists(values);
            log.debug(toDebug("After authorizeList", newValues));
            return newValues;
        } else {
            return _authorizeLists(values);
        }
    }

    private WllControlComplexValue[] _authorizeLists(WllControlComplexValue[] value) {
        // Execute Inserts and Updates then refresh from the db to ensure we
        // have
        // the values updated by db triggers note uses workflow to create a new
        // transaction to ensure that the values are persisted to the DB store
        // before being refreshed!
        return refresh(workflow.authorizeListsPersist(value));
    }

    /**
     * Authorize the lists specified by the values, note the returned values do
     * not contain the values updated by DB triggers!
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public WllControlComplexValue[] authorizeListsPersist(WllControlComplexValue[] values) {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                values[i].authorize();
                values[i].setBasicValue(XhbWllControlBeanHelper2.update(values[i].getBasicValue()));
            }
            return values;
        } else {
            return null;
        }
    }

    // Control Printed List

    /**
     * Update to indicate that letters which require printing have been printed.
     * 
     * @ejb.interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public WllControlComplexValue printedList(WllControlComplexValue value) {
        if (log.isDebugEnabled()) {
            log.debug(toDebug("Before printedList", value));
            WllControlComplexValue newValue = _printedList(value);
            log.debug(toDebug("After printedList", newValue));
            return newValue;
        } else {
            return _printedList(value);
        }
    }

    private WllControlComplexValue _printedList(WllControlComplexValue value) {
        // Execute Inserts and Updates then refresh from the db to ensure we
        // have
        // the values updated by db triggers note uses workflow to create a new
        // transaction to ensure that the values are persisted to the DB store
        // before being refreshed!
        return refresh(workflow.printedListPersist(value));
    }

    /**
     * Update to indicate that letters which require printing have been printed,
     * note the returned values do not contain the values updated by DB
     * triggers!
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public WllControlComplexValue printedListPersist(WllControlComplexValue value) {
        if (value != null) {
            // Update Letter XmlDocument Records
            Collection letterCollection = XhbXmlDocumentBeanHelper2.findByWllControlIdAndStatus(
                    value.getWllControlId(), WllControlComplexValue.LETTER_STATUS_PRINTING_REQUIRED);
            Iterator letters = letterCollection.iterator();
            while (letters.hasNext()) {
                ((XhbXmlDocument) letters.next()).setStatus(WllControlComplexValue.LETTER_STATUS_DOCUMENT_PRINTED);
            }

            // Update Summary Data held in WllControlComplexValue
            value.printed();

            return value;
        } else {
            return null;
        }
    }

    // Control Refresh & Debug

    /**
     * Refresh the recipient values to ensure they have any fields updated by
     * the db triggers.
     */
    private WllControlComplexValue[] refresh(WllControlComplexValue[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = refresh(values[i]);
        }
        return values;
    }

    private WllControlComplexValue refresh(WllControlComplexValue value) {
        return database.getWllControlComplexValue(value.getListId());
    }

    private String toDebug(String message, WllControlComplexValue[] values) {
        StringBuffer buffer = new StringBuffer(message);
        buffer.append(": ");
        if (values != null) {
            if (0 < values.length) {
                buffer.append(values[0]);
                for (int i = 1; i < values.length; i++) {
                    buffer.append(", ");
                    buffer.append(values[i]);
                }
            } else {
                buffer.append("empty");
            }
        } else {
            buffer.append("null");
        }
        return buffer.toString();
    }

    private String toDebug(String message, WllControlComplexValue value) {
        return message + ": " + value;
    }

    //
    // List Processing
    //

    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process. This method must have the same transactional behaviour as
     * processLists
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Never"
     */
    public void doTask(String taskName) {
        processList();
    }

    /**
     * This method processes the next available list if one is available. If no
     * list is available, then method will return.
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Never"
     * 
     * @throws RuntimeException
     *             if any <code>RuntimeException</code> is thrown during
     *             processing of the list, after first having the status of the
     *             list set to failed.
     */
    public void processList() {
        // @todo - would we want to do some kind of loop???
        final Integer listId = workflow.getNextListId();

        if (listId != null) {
            try {
                workflow.processList(listId);
                // indicate that formatting was successful...
                workflow.updateListStatus(listId, true);
            } catch (final RuntimeException e) {
                // indicate that formatting failed...
                workflow.updateListStatus(listId, false);
                throw e;
            }
        } else {
            // debugging for when id is not null is handled in the
            // processList method...
            if (log.isDebugEnabled()) {
                log.debug("processList() - No list to process");
            }
        }

    }

    /**
     * Method to acquire the next available list that is yet to be formatted.
     * This method will return the primary key of the list to be processed, or
     * <i>null</i> if no more lists need to be formatted.
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     * 
     * @return The primary key of the list to process, or <i>null</i> if no
     *         more lists require formatting.
     */
    public Integer getNextListId() {
    	//if pilot is enabled then pass in 1 
        String isPilotWeblogicServer = System.getProperty("PILOT_ENABLED");
    	log.debug("Pilot enabled is "+isPilotWeblogicServer);
    	if (isPilotWeblogicServer.equalsIgnoreCase("true")) {
    		log.debug("Processing for pilot court");
            return this.database.getNextListId(true);
    	} else {
    		log.debug("Processing for classic court ");
    		return this.database.getNextListId(false);
    	}
    }

    /**
     * Process the list whose primary key value is passed in.
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     * 
     * @param listId
     *            Primary key of list entity we are processing.
     */
    public void processList(final Integer listId) {
        if (log.isDebugEnabled()) {
            log.debug("processList() - listId=" + listId);
        }

        // required to construct a castor class descriptor resolver for use in
        // unmarshalling the xml from the database and for marshalling the
        // objects to be written to the database. Use the same instance.
        final ClassDescriptorResolver cdr = ClassDescriptorResolverFactory.getClassDescriptorResolver();

        final ListSummary listSummary = this.database.getListSummary(cdr, listId);

        // Now that we have done the database call, we need to process the
        // returned results...

        this.databasePersist.persistListLetters(cdr, listSummary);
    }

    /**
     * Update the status of the formatting entity identified by the passed in
     * formatting id primary key to indicate success or failure of formatting of
     * the document.
     * 
     * Only accessible via the EJB local interface.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     * 
     * @param listId
     *            Primary key of formatting entity we are updating.
     * @param success
     *            <i>true</i> if the list was successfully processed, or
     *            <i>false</i> if not successfully processed.
     */
    public void updateListStatus(final Integer listId, final boolean success) {
        if (log.isDebugEnabled()) {
            log.debug("updateFormattingDocumentStatus() - listId=" + listId + "; success=" + success);
        }

        this.database.updateStatus(listId, success);
    }

    /**
     * Get a property from the listdistribution component properties
     */
    protected static String getProperty(String key) throws IllegalArgumentException {
        if (key != null) {
            String property = getProperties().getProperty(key);
            if (property != null) {
                return property;
            }
        }
        throw new IllegalArgumentException("key: " + key);
    }

    /**
     * Get properties collection for listdistribution (consider caching)
     */
    protected static Properties getProperties() {
        return CSServices.getConfigServices().getProperties("listdistribution");
    }

    private static String getXmlPrefix(WllControlComplexValue value) {
        if (value.isWarnedList()) {
            return WARNED_LIST_XML_PREFIX;
        } else if (value.isFirmList()) {
            return FIRM_LIST_XML_PREFIX;
        } else
        // if(value.isDailyList())
        {
            return DAILY_LIST_XML_PREFIX;
        }
    }

    private static String getXmlSuffix(WllControlComplexValue value) {
        if (value.isWarnedList()) {
            return WARNED_LIST_XML_SUFFIX;
        } else if (value.isFirmList()) {
            return FIRM_LIST_XML_SUFFIX;
        } else
        // if(value.isDailyList())
        {
            return DAILY_LIST_XML_SUFFIX;
        }
    }

    private static String getDocumentType(WllControlComplexValue value, boolean asRingOut) {
        if (value.isWarnedList()) {
            return WARNED_LIST_TYPE;
        } else if (value.isFirmList()) {
            return FIRM_LIST_TYPE;
        } else
        // if(value.isDailyList())
        {
            if (asRingOut) {
                return DAILY_LIST_RINGOUT_TYPE;
            } else {
                return DAILY_LIST_LETTERS_TYPE;
            }
        }
    }
}
