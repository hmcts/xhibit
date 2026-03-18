package uk.gov.courtservice.xhibit.business.services.messaging;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.scheduler.RemoteTask;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm.CjiAhmBasicValue;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm.CjiAhmBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm.CjiAhmBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_device_type.CjiAhmDeviceType;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_device_type.CjiAhmDeviceTypeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_device_type.CjiAhmDeviceTypeBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_reply.CjiAhmReply;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_reply.CjiAhmReplyBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_reply_status.CjiAhmReplyStatus;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_reply_status.CjiAhmReplyStatusBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_status.CjiAhmStatus;
import uk.gov.courtservice.xhibit.business.entities.cji_ahm_status.CjiAhmStatusBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.InvalidDeviceException;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.MessagingException;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.TerminalLocationException;
import uk.gov.courtservice.xhibit.business.services.messaging.im.CourtNode;
import uk.gov.courtservice.xhibit.business.services.messaging.im.CourtRoomNode;
import uk.gov.courtservice.xhibit.business.services.messaging.im.CourtSiteNode;
import uk.gov.courtservice.xhibit.business.services.messaging.im.TerminalLocation;
import uk.gov.courtservice.xhibit.business.services.messaging.im.TerminalNode;
import uk.gov.courtservice.xhibit.business.vos.services.messaging.AdHocMessageValue;
import uk.gov.courtservice.xhibit.client.im.util.AdHocMessageServices;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessageServices;

/**
 * <p>
 * Title: The Messaging Stateless Session EJB.
 * </p>
 * <p>
 * Description: This EJB provides the business functionality required for the Ad
 * Hoc Messaging part of the XHIBIT messaging functionality. It implements the
 * RemoteTask interface to allow the scheduler to regularly call this bean, on
 * being called, the bean will check for new Ad Hoc Messages.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @ejb.bean name="MessagingController" description="Messaging Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="MessagingControllerHome"
 * @ejb.transaction type="Required"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 * @author Bob Boothby
 * @version $Id: MessagingControllerBean.java,v 1.27 2006/05/05 08:31:04 bzjrnl
 *          Exp $
 */
public class MessagingControllerBean extends CSSessionBean implements SessionBean, RemoteTask {
    private static final ResourceBundle i18n = CSServices.getConfigServices().getBundle("XHIBITMessagingResources");

    // Message identifier variables.
    private static final int NUMBER_OF_LETTERS = 3;

    private static final int NUMBER_OF_DIGITS = 3;

    private static final String LETTERS_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String DIGITS_ARRAY = "0123456789";

    // The base status for an ad hoc message.
    private static final String AHM_BASE_STATUS = "New";

    // The new status from an ad hoc message reply.
    private static final String AHMR_NEW_STATUS = "New";

    // The new status from an ad hoc message reply.
    private static final String AHMR_READ_STATUS = "Read by Xhibit";

    private static final int RESPONSE_CODE_LIFE_IN_HOURS = 12;

    private static final int CANDIDATE_RETRIES = 10;

    // Instance random number generator.
    private final Random random = new Random();

    // Instance of InstantMessageServices, used to post incoming AdHoc
    // Messages
    // to the queue.
    private InstantMessageServices ims;

    /**
     * Instantiates all required home interfaces and the InstantMessageServices.
     * 
     * @throws CreateException
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
        try {
            ims = new InstantMessageServices(Integer.toString(hashCode()), Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException ex) {
            log.fatal(ex);
            throw new CreateException("Could not instantiate InstantMessageServices");
        } catch (NamingException ex) {
            log.fatal(ex);
            throw new CreateException("Could not instantiate InstantMessageServices");
        }
    }

    /**
     * <p>
     * Get any new ad-hoc messages (responses from SMS), post them to the
     * appropriate JMS queue and register the success in the message store. This
     * task looks in CJI_ADHOC_MESSAGE_REPLY for new messages.
     * </p>
     * 
     * <p>
     * There is a serious need to put the witness name in instead of the phone
     * number. I think that until we have got it up and running, we have no idea
     * how the incoming phone number will relate to the contents of the witness
     * table.
     * </p>
     * 
     * <p>
     * Will need to think about how to handle undelivered, expired messages and
     * messages that have an invalid location against them.
     * </p>
     * 
     * <p>
     * Note the essential read isolation level, that allows this scheduled task
     * to run in a clustered environment.
     * </p>
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="RequiresNew"
     * @weblogic.transaction-isolation TRANSACTION_READ_COMMITTED_FOR_UPDATE
     */
    public void doTask(String taskName) {
        log.debug("Running task");
        // Establish maximum age of a response code.
        long lowDate = System.currentTimeMillis() - (RESPONSE_CODE_LIFE_IN_HOURS * 3600000);

        log.debug("lowDate: " + lowDate);

        // Get read status entity.
        final CjiAhmReplyStatus readStatus = CjiAhmReplyStatusBeanHelper2.findByStatusText(AHMR_READ_STATUS);
        log.debug("readStatus = :" + readStatus.getStatusId() + ": " + readStatus.getStatusText());

        // Get all newly received messages...
        final Collection newMessages = CjiAhmReplyBeanHelper2.findByStatusTextAndLowDate(AHMR_NEW_STATUS,
                new Timestamp(lowDate));

        // Step through the newly received messages...
        final Iterator newMessagesIterator = newMessages.iterator();
        while (newMessagesIterator.hasNext()) {
            final CjiAhmReply newMessage = (CjiAhmReply) newMessagesIterator.next();
            log.debug("Processing Message");

            try {
                if ((newMessage.getResponseCode() != null) && (newMessage.getCjiAhm() != null)) {
                    String responseContent = newMessage.getContent();
                    responseContent = new String(
                            AdHocMessageServices.getSMStoUnicodeFormattedChars(AdHocMessageServices
                                    .htmlUnEscapeCharacters(responseContent)));

                    StringBuffer response = new StringBuffer(responseContent);
                    response.append("\n\n");
                    response.append(i18n.getString("in_response_to"));
                    response.append("\n\n");

                    String originalContent = newMessage.getCjiAhm().getContent();
                    originalContent = new String(
                            AdHocMessageServices.getSMStoUnicodeFormattedChars(AdHocMessageServices
                                    .htmlUnEscapeCharacters(originalContent)));
                    response.append(originalContent);

                    // Send them to the message queues.
                    ims.publishTextMessage(response.toString(), newMessage.getCjiAhm().getOrginalSender());
                }

                log.debug("Done with message");
                // always mark the new message as read...
                newMessage.setCjiAhmReplyStatus(readStatus);
            } catch (final NamingException ex) {
                // Log the problem, but need to continue processing.
                log.error("Problem in processing AH message", ex);
            } catch (final JMSException ex) {
                // Log the problem, but need to continue processing.
                log.error("Problem in processing AH message", ex);
            }
        }

        log.debug("Finished task.");
    }

    /**
     * This method sends an ad hoc message, by creating an instance in the
     * Message Store CJI_ADHOC_MESSAGE table.
     * 
     * @param adHocMessage
     *            The ad hoc message to send.
     * @throws MessagingException
     *             When there is a problem creating the message for send.
     * @throws InvalidDeviceException
     *             When an invalid device type is selected.
     * @ejb.interface-method view-type="remote"
     */
    public void sendMessage(AdHocMessageValue adHocMessage) throws MessagingException, InvalidDeviceException {
        CjiAhmStatus newStatus = CjiAhmStatusBeanHelper2.findByStatusText(AHM_BASE_STATUS);
        Integer deviceTypeId = null;
        try {
            deviceTypeId = CjiAhmDeviceTypeBeanHelper2.findByDeviceTextUniquely(adHocMessage.getReceiverDeviceType())
                    .getDeviceId();
        } catch (CjiAhmDeviceTypeBeanNotFoundException ex) {
            // Could not retrieve device type.
            throw new InvalidDeviceException("MESSAGE_002", "The device selected was invalid: "
                    + adHocMessage.getReceiverDeviceType(), ex);
        }

        String[] uniqueIdentifier = { generateMessageIdentifier() };

        // Sort reply message...
        String messageContent = MessageFormat.format(i18n.getString("respond_using"), uniqueIdentifier)
                + adHocMessage.getMessage();
        String content = AdHocMessageServices.htmlEscapeCharacters(AdHocMessageServices
                .getUnicodeToSMSFormattedChars(messageContent.toCharArray()));

        // Save the message to the database for delivery.
        final CjiAhmBasicValue val = new CjiAhmBasicValue();
        val.setAggregatorId(null);
        val.setContent(content);
        val.setResponseCode(uniqueIdentifier[0]);
        val.setTel(adHocMessage.getReceiverNumber());
        val.setSentAggDate(new Timestamp(System.currentTimeMillis()));
        val.setSentProDate(null);
        val.setOrginalSender(adHocMessage.getSenderLocation());
        val.setDeviceId(deviceTypeId);
        val.setStatusId(newStatus.getStatusId());

        CjiAhmBeanHelper2.createLocal(val);
    }

    /**
     * Returns an array of the valid device types that ad hoc messages can be
     * sent to.
     * 
     * @return an array of the valid device types that an ad hoc message can be
     *         sent to.
     * @ejb.interface-method view-type="remote"
     */
    public String[] getDeviceTypes() {
        final Collection deviceTypes = CjiAhmDeviceTypeBeanHelper2.findAll();
        final String[] returnArray = new String[deviceTypes.size()];

        final Iterator it = deviceTypes.iterator();
        for (int i = 0; it.hasNext(); i++) {
            returnArray[i] = ((CjiAhmDeviceType) it.next()).getDeviceText();
        }

        return returnArray;
    }

    /**
     * This method returns an high confidence unique/checked message identifier.
     * Without much more extensive work we cannot guarantee uniqueness, so we
     * will have to recover from the circumstance when a non-unique code is
     * generated.
     * 
     * @return new unique message identifier for the day.
     * @throws MessagingException
     *             When there is a problem creating unique identifier.
     */
    private String generateMessageIdentifier() throws MessagingException {
        // perform uniqueness checking here.
        long lowDate = System.currentTimeMillis() - (RESPONSE_CODE_LIFE_IN_HOURS * 3600000);
        for (int i = 0; i < CANDIDATE_RETRIES; i++) {
            String candidate = generateCandidateIdentifier();
            try {
                CjiAhmBeanHelper2.findByResponseCodeAndLowDate(candidate, new Timestamp(lowDate));
            } catch (CjiAhmBeanNotFoundException ex) // good choice.
            {
                return candidate;
            }
        }
        throw new MessagingException("MESSAGE_001", "Could not generate unique message response code.");
    }

    /**
     * This method randomly generates a new candidate message identifier. This
     * candidate may not be unique, and so needs checking before use.
     * 
     * @return a candidate identifier for a message.
     */
    private String generateCandidateIdentifier() {
        // Always reseed with current time. We are not high throughput,
        // and maintaining the same seeded instance of Random throughout will
        // potentially cause the PRNG to be too predictable.
        random.setSeed(System.currentTimeMillis());
        StringBuffer returnBuffer = new StringBuffer();
        for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
            returnBuffer.append(LETTERS_ARRAY.charAt(random.nextInt(LETTERS_ARRAY.length())));
        }

        for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
            returnBuffer.append(DIGITS_ARRAY.charAt(random.nextInt(DIGITS_ARRAY.length())));
        }
        return returnBuffer.toString();
    }

    /**
     * This method services the clients with the Java Swing TreeModel based on
     * the terminalId of the client user.
     * 
     * @param value
     * @return
     * @ejb.interface-method view-type="remote"
     */
    public DefaultTreeModel getTreeModelByCourtId(Integer courtId) throws MessagingException {
        log.debug("Method: getTreeModel(" + courtId + ")");
        return getTreeModel(new CourtNode(courtId));
    }

    private DefaultTreeModel getTreeModel(CourtNode court) throws MessagingException {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(court.getName());

        for (int i = 0; i < court.getCourtSites().length; i++) {
            CourtSiteNode courtSite = court.getCourtSites()[i];
            DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode((court.getCourtSites()[i]).getName());
            Set s = new HashSet();
            for (int j = 0; j < courtSite.getCourtRooms().length; j++) {
                CourtRoomNode courtRoom = courtSite.getCourtRooms()[j];
                s.add(courtRoom.getName());
            }

            s.addAll(getTerminalLocations(courtSite.getCourtSiteId()));

            Iterator iter = s.iterator();
            while (iter.hasNext()) {
                String ss = (String) iter.next();
                dmtn.add(new DefaultMutableTreeNode(ss));
            }
            s.clear();
            root.add(dmtn);
        }

        return new DefaultTreeModel(root);
    }

    /**
     * This method services the clients with the Java Swing TreeModel based on
     * the terminalName of the client user.
     * 
     * @param the
     *            terminal name
     * @return DefaultTreeModel of the courts/sites/rooms based on the terminal
     *         name
     * @ejb.interface-method view-type="remote"
     */
    public DefaultTreeModel getTreeModel(String terminalName) throws MessagingException {
        log.debug("Method: getTreeModel(" + terminalName + ")");

        try {
            XhbTerminalBasicValue terminal = XhbTerminalBeanHelper2.findByTerminalNameValue(terminalName);
            TerminalNode terminalNode = new TerminalNode(terminal.getTerminalId());
            return getTreeModel(terminalNode.getCourt());
        } catch (XhbTerminalBeanNotFoundException e) {
            throw new MessagingException("MESSAGING_006", "Could not locate unique terminal for " + terminalName);
        }
    }

    /**
     * This method returns a collection of Strings for each terminal in
     * XHB_TERMINAL match the provided courtSiteId. This allows the terminals
     * that have been configured in Active Directory as not belonging to a Court
     * Room, to be displayed as part of the messaging destination list.
     * 
     * @param the
     *            Court Site Id
     * @return Collection of the sites based on the court site id
     */
    private Collection getTerminalLocations(Integer courtSiteId) throws MessagingException {
        log.debug("Method: getTerminalLocations(" + courtSiteId + ")");

        final XhbTerminalBasicValue[] terminalColl = XhbTerminalBeanHelper2
                .findByCourtSiteIdNoCourtRoomNoRoamValue(courtSiteId);
        final ArrayList list = new ArrayList();

        for (int i = 0; i < terminalColl.length; i++) {
            TerminalLocation loc = getTerminalLocation(terminalColl[i].getLocation());
            // Filter hidden court rooms
            if (!loc.isHidden()) {
                list.add(loc.getCourtRoomName());
            }
        }

        return list;
    }

    /**
     * Return the formatted CourtRoom name
     * 
     * @param location
     *            the location from XHB_TERMINAL
     * @return the formatted court room name
     * @throws MessagingException
     */
    private TerminalLocation getTerminalLocation(String location) throws MessagingException {
        try {
            return new TerminalLocation(location, location);
        } catch (TerminalLocationException ex) {
            throw new MessagingException("MESSAGING_007", "Could not format location " + location);
        }
    }
}
