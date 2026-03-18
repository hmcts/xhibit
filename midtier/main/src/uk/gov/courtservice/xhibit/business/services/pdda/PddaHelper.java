package uk.gov.courtservice.xhibit.business.services.pdda;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Session;

import org.apache.commons.lang.SerializationUtils;
import org.castor.util.Base64Encoder;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.pdda.RefStatusCodes;
import uk.gov.courtservice.xhibit.business.entities.pdda.RefStatusCodesMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_internet_html.XhbInternetHtmlBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_internet_html.XhbInternetHtmlBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule.ScheduleHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.PddaBatchBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.PddaMessageBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.PddaMessageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefPddaMessageTypeBasicValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CourtRoomEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseCourtLogInformation;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: PDDAHelper
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class PddaHelper extends XhibitPddaHelper {
	private static final Logger LOG = CSServices.getLogger(PddaHelper.class);
	
	private static interface REF_STATUS {
		static final String BATCH_FAILURE = "BF";
		static final String BATCH_SUCCESS = "BS";
		static final String NOT_SENT = "NS";
		static final String READY_TO_SEND = "RS";
	}
	private static interface CONFIG {
		static final String MAX_BATCH_RESENDS = "PDDA_BAIS_MAX_RESENDS";
		static final String MAX_BATCH_SIZE = "PDDA_BAIS_SFTP_BATCH_MAX_SIZE";
		static final String MAX_BATCH_WAIT_TIME = "PDDA_BAIS_SFTP_BATCH_TIME_TO_WAIT";
		static final String PDDA_SWITCHER = "PDDA_SWITCHER";
		static final String PDDA_SWITCHER_DEFAULT = "3";
		static final String SFTP_HOST = "PDDA_BAIS_SFTP_HOSTNAME";
		static final String SFTP_PASSWORD = "PDDA_BAIS_SFTP_PASSWORD";
		static final String SFTP_UPLOAD_LOCATION = "PDDA_BAIS_SFTP_UPLOAD_LOCATION";
		static final String SFTP_USERNAME = "PDDA_BAIS_SFTP_USERNAME";
	}
	private static final DateFormat DATETIMEFORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final DateFormat BATCH_FILENAME_DATETIMEFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String BATCH_FILENAME_PREFIX = "PDDA_";
	private static final String EMPTY_STRING = "";
	private static final String SENT_IWP_DOCUMENT_TO_PDDA = "P";
	
	private PddaBatchHelper pddaBatchHelper;
	private RefStatusCodesMaintainer refStatusCodesMaintainer;
	private CourtMaintainer courtMaintainer;
	private ConfigPropMaintainer configPropMaintainer;
	private PddaDatabaseManager pddaDatabaseManager;
	private PddaMessageHelper pddaMessageHelper;
	
	private String methodName;
	private String pddaSwitcher;
    private RefStatusCodes batchSuccessRefStatusCode = null;
    private RefStatusCodes batchFailureRefStatusCode = null;
    private RefStatusCodes readyToSendRefStatusCode = null;
    private RefStatusCodes notSentRefStatusCode = null;
    private PddaSFTPHelper sftpHelper = null;
    
	public PddaHelper() {
		courtMaintainer = new CourtMaintainer();
	}
	
	/**
	 * Description: Get the PDDA Switcher value
	 * 1 = Send to PDDA only
	 * 2 = Send to PDDA and process in Xhibit
	 * 3 = Do not send to PDDA only process in Xhibit (DEFAULT)
	 */
	public String getPDDASwitcher() {
		if (pddaSwitcher == null) {
			String result = getConfigValue(CONFIG.PDDA_SWITCHER);
			LOG.debug("PDDA_SWITCHER ="+result);
			pddaSwitcher = result != null ? result : CONFIG.PDDA_SWITCHER_DEFAULT;
		}
		return pddaSwitcher;
	}
	
	private Integer getMaxBatchSize() {
		String result = getConfigValue(CONFIG.MAX_BATCH_SIZE);
		LOG.debug("Max Batch Size ="+result);
		return result != null  ? Integer.valueOf(result) : null;
	}
	
	private Integer getMaxBatchWaitTime() {
		String result = getConfigValue(CONFIG.MAX_BATCH_WAIT_TIME);
		return result != null  ? Integer.valueOf(result) : null;
	};
	
	private Integer getMaxBatchResends() {
		String result = getConfigValue(CONFIG.MAX_BATCH_RESENDS);
		LOG.debug("Max Batch Rends ="+result);
		return result != null  ? Integer.valueOf(result) : 0;
	}
	
	public String getConfigValue(final String propertyName) {
		methodName = "getConfigValue("+propertyName+")";
		LOG.debug(methodName + " called");
		String result = getConfigPropMaintainer().getPropertyValue(propertyName);
		LOG.debug(propertyName+" = "+result);
		return result;
	}
	
	public String getMandatoryConfigValue(final String propertyName) {
		methodName = "getMandatoryConfigValue("+propertyName+")";
		LOG.debug(methodName + " called");
		String result = getConfigValue(propertyName);
		if (result == null || EMPTY_STRING.equals(result)) {
			throw new NullPointerException();
		}
		return result;
	}
	
	/**
     * Sends a public display event
     * 
     * @param event
     *            Public display event
     */
	public void sendMessage(uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent event,
			String userDisplayName, boolean skipXhibit) {
		methodName = "sendMessage(event (not shown), "+userDisplayName+")";
		LOG.debug(methodName + " called");
		
		try {
	    	String pddaSwitcher = getPDDASwitcher();
	    	
	    	// Send to Xhibit
	    	if (!skipXhibit && isSendToXhibit(pddaSwitcher)) {
	    		LOG.debug("Trying to send PublicDisplayEvent message to XHIBIT");
	    		super.sendMessage(event, userDisplayName);
	    	}
	    	//Send to PDDA
	    	if (isSendToPDDA(pddaSwitcher)) {
	    		LOG.debug("Trying to send PublicDisplayEvent message to PDDA");
	    		String EMPTY_DOCUMENT_NAME = "";
	    		String EMPTY_DOCUMENT_STATUS = "";
	    		String EMPTY_RESPONSE_GENERATED = "";
	    		Integer EMPTY_STAGING_INBOUND_ID = null;
	
	    		// The next line is not active, for details see the comment for the method convertToPDDAMessage below
	    		//uk.gov.hmcts.pdda.common.publicdisplay.events.PublicDisplayEvent pddaEvent = convertToPDDAMessage(event);
	    		byte[] serializedObject = SerializationUtils.serialize(event);
	    		String encodedInput = new String(Base64Encoder.encode(serializedObject));
	    		// The next line is not active, for details see the comment for the method convertToPDDAMessage below
	    		//sendMessageToPDDA(encodedInput, pddaEvent, userDisplayName);
	    		LOG.debug("Serialized message and about to send PublicDisplayEvent message to PDDA");
	    		sendMessageToPDDA(encodedInput, event, userDisplayName, EMPTY_DOCUMENT_NAME, EMPTY_DOCUMENT_STATUS,
	    				EMPTY_RESPONSE_GENERATED, EMPTY_STAGING_INBOUND_ID);
	    		if (event instanceof CaseStatusEvent) {
	    			LOG.debug("This is a case event so lets send a hearing progress update too");
	    			boolean isCaseEventAndSendingAnotherMessage = false;
	    			// Create a PddaHearingProgresEvent too
	    			if (event != null) {
		    			CaseCourtLogInformation ccli = ((CaseStatusEvent) event).getCaseCourtLogInformation();
		    			if (ccli != null) {
			    			CourtLogSubscriptionValue clsv = ccli.getCourtLogSubscriptionValue();
			    			if (clsv != null) {
				    			Integer scheduledHearingId = clsv.getScheduledHearingId();
				    			if ((scheduledHearingId != null) &&  (scheduledHearingId > 0)) {
				    				isCaseEventAndSendingAnotherMessage = true;
				    				LOG.debug("About to send another message of type PddaProgressHearingEvent here");
				    				new ScheduleHelper().sendCurrentHearingStatusDataToPdda(scheduledHearingId);
				    			}
			    			}
		    			}
	    			}
	    			
	    			if (!isCaseEventAndSendingAnotherMessage) {
	    				LOG.debug("Not able to send another message of type PddaProgressHearingEvent here");
	    			}
	    		}
	    	}
		} catch (Exception e) {
			LOG.error("Error sending PublicDisplayEvent message");
			e.printStackTrace();
		}
    }
	
	
	public boolean isSendToPDDA() {
		return super.isSendToPDDA(getPDDASwitcher());
	}
	
	/**
	 * Converts a PublicDisplayEvent message in XHIBIT to one that can be used in PDDA.
	 * NOTE:
	 *  - Currently not in use, this method was designed to allow an object to be refactored to the
	 *  have the same content in a class of the same name in a different package.
	 *  PDDA has been changed to have the event package names from XHIBIT so no work SHOULD be needed,
	 *  but leaving in place just in case
	 *   
	 * @param e
	 * @return
	 */
	/*private uk.gov.hmcts.pdda.common.publicdisplay.events.PublicDisplayEvent convertToPDDAMessage(PublicDisplayEvent e) {
		methodName = "uk.gov.hmcts.pdda.common.publicdisplay.events.PublicDisplayEvent convertToPDDAMessage(event (not shown)";
		LOG.debug(methodName + " called");
		
		if (e.getClass().getName().equals("HearingStatusEvent")) {
			LOG.debug("Converting a HearingStatusEvent");
			uk.gov.courtservice.xhibit.common.publicdisplay.events.HearingStatusEvent hse =
					(uk.gov.courtservice.xhibit.common.publicdisplay.events.HearingStatusEvent) e;
			
			uk.gov.hmcts.pdda.common.publicdisplay.events.HearingStatusEvent newHSE =
					new uk.gov.hmcts.pdda.common.publicdisplay.events.HearingStatusEvent(hse.getCourtRoomIdentifier(), hse.getCaseChangeInformation());
			
			return newHSE;
			
		} else if (e.getClass().getName().equals("ConfigurationChangeEvent")) {
			LOG.debug("Converting a ConfigurationChangeEvent");
			uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent cce =
					(uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent) e;
			
			uk.gov.hmcts.pdda.common.publicdisplay.events.ConfigurationChangeEvent newCCE =
					new uk.gov.hmcts.pdda.common.publicdisplay.events.ConfigurationChangeEvent(cce.getChange());
			
			return newCCE;

		} else {
			LOG.debug("What is this we're converting? ... "+e.getClass().getCanonicalName());
			return null;
		}  
	}*/
    
	/**
	 * Description: Find the PDDA Message
	 * 
	 * @param pddaMessageId
	 * @throws FinderException 
	 */
	public PddaMessageBasicValue findPddaMessage(final Integer pddaMessageId) throws FinderException {
		methodName = "findPddaMessage()";
		LOG.debug(methodName + " called");
		PddaMessageBasicValue result = getPddaMessageHelper().findByPddaMessageId(pddaMessageId);
		return result;
	}
	
	/**
	 * Description: Create the PDDA Batch
	 *  
	 * @param userDisplayName 
	 * @throws FinderException
	 */
	public PddaBatchBasicValue createBatch(final String userDisplayName) throws FinderException {
		methodName = "createBatch()";
		LOG.debug(methodName + " called");
		
		PddaBatchBasicValue basicValue = null;
		try {
			// Get the statusId for Not Processed
			RefStatusCodes refStatusCodes = getNotSentRefStatusCode();
			
			// Populate the basicValue
			basicValue = new PddaBatchBasicValue();
			basicValue.setBatchOpenedDatetime(new Date());
			basicValue.setNoOfRecordsInBatch(Integer.valueOf(1));
			basicValue.setBatchNoResends(Integer.valueOf(0));
			basicValue.setBatchStatusId(refStatusCodes.getRefStatusCodeId());
			
			// Create the record
			basicValue = getPddaBatchHelper().createBatch(basicValue, userDisplayName);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (EJBException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}	
		return basicValue;
	}
	
	private PddaBatchBasicValue validateAndSendBatch(PddaBatchBasicValue basicValue) throws FinderException {
		methodName = "validateAndSendBatch()";
		LOG.debug(methodName + " called");
		
		if (isBatchReadyToSend(basicValue.getNoOfRecordsInBatch(), basicValue.getBatchOpenedDatetime())) {
			LOG.debug("Batch "+basicValue.getPddaBatchId()+" is Ready To Send");
			basicValue = sendBatch(basicValue);
		}
		return basicValue;
	}
	
	private boolean isBatchReadyToSend(final Integer noOfRecordsInBatch, final Date batchOpenedDatetime) {
		methodName = "isbatchReadyToSend("+noOfRecordsInBatch+", "+batchOpenedDatetime+")";
		LOG.debug(methodName + " called");
		
		final Integer maxBatchSize = getMaxBatchSize();
		final Integer batchWaitTime = getMaxBatchWaitTime();
		Date expiryDateTime = batchWaitTime != null ? DateTimeUtilities.addSeconds(batchOpenedDatetime, batchWaitTime) : null;
		if (maxBatchSize != null && noOfRecordsInBatch >= maxBatchSize) {
			LOG.debug("Batch max size reached/exceeded");
			return true;
		} else if (expiryDateTime != null && expiryDateTime.after(new Date())) {
			LOG.debug("Batch max wait time reached/exceeded");
			return true;
		}
		return false;
	}
	
	private PddaBatchBasicValue sendBatch(final PddaBatchBasicValue basicValue) throws FinderException {
		methodName = "sendBatch()";
		LOG.debug(methodName + " called");
		
		// Update the status to Read To Send
		try {
			basicValue.setBatchStatusId(getReadyToSendRefStatusCode().getRefStatusCodeId());
			basicValue.setBatchClosedDatetime(new Date());
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return basicValue;
	}
	
	/**
	 * Description: Send the PDDA Batches (called from scheduler.properties)
	 */
	public void sendBatchesToPDDA(String userDisplayName) {
		methodName = "sendBatchesToPDDA()";
		LOG.debug(methodName + " called");
		
		// Check for batches to send
		try {
			Integer readyToSendId = getReadyToSendRefStatusCode().getRefStatusCodeId();
			Integer batchSuccessId = getBatchSuccessRefStatusCode().getRefStatusCodeId();
			Integer batchFailureId = getBatchFailureRefStatusCode().getRefStatusCodeId();
			
			Collection<PddaBatchBasicValue> batches = getPddaBatchHelper().findReadyToSendBatches(readyToSendId);
			if (batches != null && batches.size() > 0) {
				// Get the parameters and connect the session
				SftpConfig sftpConfig = getSftpConfigs();
				
				for (PddaBatchBasicValue basicValue : batches) {
					// Clear any error messages
					basicValue.setBatchMessage(null);
				
					// SFTP the batch
					basicValue = sftpBatch(basicValue, sftpConfig, userDisplayName);
					LOG.debug(methodName + ": basicValue data is:"+basicValue.toString());
					
					// Update the batch
					if (basicValue.getBatchMessage() == null) {
						LOG.debug(methodName + ": Setting the batch with success id "+batchSuccessId);
						basicValue.setBatchStatusId(batchSuccessId);
						basicValue.setBatchSentTime(new Date());
					} else {
						LOG.debug(methodName + ": Setting the batch with failure id "+batchFailureId);
						basicValue.setBatchStatusId(batchFailureId);
					}
					// Increment the Resends
					LOG.debug(methodName + ": Setting the resends to "+basicValue.getBatchNoResends()+1);
					basicValue.setBatchNoResends(basicValue.getBatchNoResends()+1);
					
					// Update the batch
					LOG.debug(methodName + ": Updating the batch with new data "+basicValue.toString());
					getPddaBatchHelper().updateBatch(basicValue, userDisplayName);
				}
				
				// Disconnect the session
				if (sftpConfig.session != null) {
					getSFTPHelper().disconnectSession(sftpConfig.session);
					sftpConfig.session = null;
				}
			}
		} catch (FinderException e) {
			LOG.error("Error in sendBatchesToPDDA:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param pddaBatch
	 * @param sftpConfigs
	 * @param userDisplayName
	 * @return
	 */
	private PddaBatchBasicValue sftpBatch(PddaBatchBasicValue pddaBatch, SftpConfig sftpConfigs, String userDisplayName) {
		methodName = "sftpBatch(pddaBatch (not shown), sftpConfigs (not shown), "+userDisplayName+")";
		LOG.debug(methodName + " called");
		
		PddaBatchBasicValue result = pddaBatch;
		
		// Validate the parameters
		if (sftpConfigs.errorMsg != null) {
			result.setBatchMessage(sftpConfigs.errorMsg);
			LOG.error(result.getBatchMessage());
			return result;
		}
		
		// Build the Files
		try {
			Map<String, InputStream> files = getFilesToSftp(pddaBatch.getPddaBatchId());
			LOG.debug("Generated "+files.size()+" files to send");
			
			// STFP the files
			if (files.size() > 0) {
				try {
					LOG.debug("There are "+files.size()+" files to sftp to BAIS");
					getSFTPHelper().sftpFiles(sftpConfigs.session, sftpConfigs.remoteFolder, files);
				} catch (Exception ex) {
					result.setBatchMessage(ex.getMessage());
					LOG.error("SFTP Error:"+result.getBatchMessage());
					return result;
				}
			} else {
				result.setBatchMessage("No messages in batch");
				LOG.error(result.getBatchMessage());
				return result;
			}
		} catch (FinderException ex) {
			result.setBatchMessage(ex.getMessage());
			LOG.error(result.getBatchMessage());
		}
		
		return result;
	}
	
	
	/**
	 * Get teh 
	 * @param batchId
	 * @return
	 * @throws FinderException
	 */
	protected Map<String, InputStream> getFilesToSftp(Integer batchId) throws FinderException {
		methodName = "getFilesToSftp()";
		LOG.debug(methodName + " called");
		
		Map<String, InputStream> files = new HashMap<String, InputStream>(); 
		Collection<PddaMessageComplexValue> pddaMessages = getPddaMessageHelper().findComplexByBatchId(batchId);
		if (pddaMessages != null) {
			LOG.debug("Going to put "+pddaMessages.size()+" messages in a batch");
			Integer msgNo = 0;
			String crestCourtId="";
			for (PddaMessageComplexValue pddaMessage : pddaMessages) {
				msgNo++;
				crestCourtId = getCourtCode(pddaMessage.getCourtId());
				LOG.debug("Message "+msgNo);
				if (pddaMessage.getPddaMessageTypeId() != null) {
					String messageType = getMessageType(pddaMessage.getPddaMessageTypeId());
					LOG.debug("messageType="+messageType);
					String filename = getFilename(pddaMessage.getPddaBatchId(), msgNo, crestCourtId, messageType);
					LOG.debug("filename="+filename);
					String msg = getMsgContents(pddaMessage);
					//String msg = pddaMessage.getClob() != null ? pddaMessage.getClob().getClobData() : "";
					LOG.debug("message==="+msg);
					InputStream file = new ByteArrayInputStream(msg.getBytes());
					files.put(filename, file);
				} else {
					LOG.error("Cannot batch this message as cannot determine the message type: "
							+ "pddaMessage.getRefPddaMessageType()="+pddaMessage.getRefPddaMessageType());
				}
			}
		}

		return files;
	}
	
	/**
	 * As the msg contents can be derived from either an XHB_CLOB or XHB_BLOB record, determine
	 * what the message type is and then get the record.
	 * If the message type is a webpage then get the record from XHB_BLOB, otehrwise XHB_CLOB. 
	 * @param pddaMessage
	 * @return
	 */
	private String getMsgContents(PddaMessageComplexValue pddaMessage) {
		// First check the message type
		String msgContents = "";
		if (pddaMessage != null) {
			String messageType = getMessageType(pddaMessage.getPddaMessageTypeId());
			if (messageType.equals("XWP")) {
				// Get the record from XHB_BLOB
				XhbBlobBasicValue blobRecord = XhbBlobBeanHelper2.findByPrimaryKeyValue(pddaMessage.getPddaMessageDataId());
				msgContents = new String(blobRecord.getBlobData());
			} else {
				// Get the record from XHB_CLOB
				XhbClobBasicValue clobRecord = XhbClobBeanHelper2.findByPrimaryKeyValue(pddaMessage.getPddaMessageDataId());
				msgContents = new String(clobRecord.getClobData());
			}
		} else {
			LOG.error("Trying to obtain message contents from a message that is empty.");
		}
		return msgContents;
	}

	/**
	 * Given the 2 digit court id find the 3 digit crest court id that is consistent across all environments 
	 * @param courtId
	 * @return
	 * @throws FinderException
	 */
	private String getCourtCode(final Integer courtId) throws FinderException {
		try {
			Court court = courtMaintainer.findByPrimaryKey(courtId);
			String courtCode = Integer.valueOf(court.getCrestCourtId())+"";
			return courtCode;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (NumberFormatException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());			
			throw new FinderException("XhbCourt.CrestCourtId contains an alphanumeric value");
		}
	}
	
	/**
	 * Given a RefPddaMessageTypeBasicValue object check that it is valid and get the message type.
	 * Ultimately it will be a PD or DL
	 * 
	 * @param rpdmtbv
	 */
	private String getMessageType(Integer messageTypeId) {
		// Get the ref type from the messageTypeId
		RefPddaMessageTypeBasicValue rpdmtbv = getPddaMessageHelper().findByMessageTypeId(messageTypeId);
		
		if (rpdmtbv != null) {
			String messageType = rpdmtbv.getPddaMessageType();
			LOG.debug("Message type="+messageType);
			if (messageType.equals("PublicDisplayCPP")) {
				return "CPD";
			} else if (messageType.equals("List")) {
				return "XDL";
			} else if (messageType.equals("IWP")) {
				return "XWP";
			} else {
				return "XPD";
			}
		} else {
			// We dont know what this is!!
			return "XNK"; // (XHIBIT) Not Known
		}
	}
	
	/**
	 * The crestCourtId will be 000 for public display messages. It will only be set for lists.
	 * 
	 * @param pddaBatchId
	 * @param msgNo
	 * @param crestCourtId
	 * @return
	 */
	private String getFilename(Integer pddaBatchId, Integer msgNo, String crestCourtId, String messageType) {
		return BATCH_FILENAME_PREFIX+messageType+"_"+pddaBatchId+
				"_"+msgNo+"_"+crestCourtId+"_"+
				BATCH_FILENAME_DATETIMEFORMAT.format(new Date());
	}
	
	private SftpConfig getSftpConfigs() {
		methodName = "getSftpConfigs()";
		LOG.debug(methodName + " called");
		
		SftpConfig sftpConfig = new SftpConfig();
		String hostAndPort = null;
		
		// Fetch and validate the properties
		String propertyName = null;
		try {
			propertyName = CONFIG.SFTP_USERNAME;
			sftpConfig.username = getMandatoryConfigValue(propertyName);
			propertyName = CONFIG.SFTP_PASSWORD;
			sftpConfig.password = getMandatoryConfigValue(propertyName);
			propertyName = CONFIG.SFTP_UPLOAD_LOCATION;
			sftpConfig.remoteFolder = getMandatoryConfigValue(propertyName);
			propertyName = CONFIG.SFTP_HOST;
			hostAndPort = getMandatoryConfigValue(propertyName);
			LOG.debug("sftpConfig = "+sftpConfig.toString());
		} catch (NullPointerException ex) {
			sftpConfig.errorMsg = propertyName+" not found";
			return sftpConfig;
		}
		
		// Validate the host and port
		String portDelimiter = ":";
		Integer pos = hostAndPort.indexOf(portDelimiter);
        if (pos <= 0) { 
        	sftpConfig.errorMsg = CONFIG.SFTP_HOST+" syntax is <Host>"+portDelimiter+"<Port>";
        	return sftpConfig;
        } 
        sftpConfig.host = hostAndPort.substring(0, pos);
		try {
			String strPort = hostAndPort.substring(pos+1, hostAndPort.length());
			sftpConfig.port = Integer.valueOf(strPort);
		} catch (Exception ex) {
			sftpConfig.errorMsg = CONFIG.SFTP_HOST+" contains invalid port number";
			return sftpConfig;
		}
		
		// Create a session
		try {
			sftpConfig.session = getSFTPHelper().createSession(sftpConfig.username, sftpConfig.password,  
					sftpConfig.host, sftpConfig.port.intValue());
		} catch (Exception ex) {
			sftpConfig.errorMsg = "SFTP Error:"+ex.getMessage();
			return sftpConfig;
		}
		
		return sftpConfig;
	}
	
	/**
	 * Description: Resend the PDDA Batches (called from scheduler.properties)
	 */
	public void resendBatchesToPDDA(String userDisplayName) {
		methodName = "resendBatchesToPDDA()";
		LOG.debug(methodName + " called");
		
		// Max no of resends allowed
		Integer maxResends = getMaxBatchResends();
		
		try {
			Integer readyToSendId = getReadyToSendRefStatusCode().getRefStatusCodeId();
			Integer batchFailureId = getBatchFailureRefStatusCode().getRefStatusCodeId();
			
			Collection<PddaBatchBasicValue> batches = getPddaBatchHelper().findBatchesToResend(maxResends, batchFailureId);
			if (batches != null) {
				LOG.debug("Found "+batches.size()+ " batches to resend.");
				for (PddaBatchBasicValue basicValue : batches) {
					LOG.debug("About to resemd batch with basicValue data:: "+basicValue.toString());
					basicValue.setBatchStatusId(readyToSendId);
					getPddaBatchHelper().updateBatch(basicValue, userDisplayName);
				}
			}
		} catch (FinderException e) {
			LOG.error("Error in resendBatchesToPDDA:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: Create the PDDA Message Type
	 *  
	 * @throws EJBException
	 */
	public RefPddaMessageTypeBasicValue createMessageType(final String messageType,
			final Date batchOpenedDatetime,
			final String userDisplayName) throws FinderException {
		
		methodName = "createMessageType()";
		LOG.debug(methodName + " called");
		
		RefPddaMessageTypeBasicValue basicValue = null;
		try {
			// Populate the basicValue
			basicValue = new RefPddaMessageTypeBasicValue();
			basicValue.setPddaMessageType(messageType);
			basicValue.setPddaMessageTypeDescription(DATETIMEFORMAT.format(batchOpenedDatetime));
			
			// Create the record
			basicValue = getPddaMessageHelper().createPddaMessageType(basicValue, userDisplayName);
		} catch (EJBException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}	
		return basicValue;
	}
	
	/**
	 * Description: Create the PDDA Message
	 *  
	 * @throws EJBException
	 */
	public PddaMessageBasicValue createMessage(final Integer courtId, final Integer courtRoomId, 
			final Integer pddaMessageTypeId, final Long pddaMessageClobId,
			final Long pddaMessageBlobId, final Integer pddaBatchId, final String userDisplayName,
			final String cpDocumentName, final String cpDocumentStatus,
			final String cpResponseGenerated, final Integer cpStagingInboundId) throws FinderException {
		
		methodName = "createMessage()";
		LOG.debug(methodName + " called");
		
		PddaMessageBasicValue basicValue = null;
		try {
			// Populate the basicValue
			basicValue = new PddaMessageBasicValue();
			basicValue.setCourtId(courtId);
			basicValue.setCourtRoomId(courtRoomId);
			basicValue.setPddaMessageTypeId(pddaMessageTypeId);
			if (pddaMessageClobId != null) {
				basicValue.setPddaMessageDataId(pddaMessageClobId);
			} else if (pddaMessageBlobId != null) {
				basicValue.setPddaMessageDataId(pddaMessageBlobId);
			}
			basicValue.setPddaBatchId(pddaBatchId);
			basicValue.setTimeSent(null);
			basicValue.setPddaMessageGuid(getPddaDatabaseManager().getGuid());
			basicValue.setCpDocumentName(cpDocumentName);
			basicValue.setCpDocumentStatus(cpDocumentStatus);
			basicValue.setCpResponseGenerated(cpResponseGenerated);
			basicValue.setCpStagingInboundId(cpStagingInboundId);
			
			// Create the record
			basicValue = getPddaMessageHelper().createPddaMessage(basicValue, userDisplayName);
		} catch (EJBException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		
		return basicValue;
	}
	
	private PddaBatchBasicValue findOpenBatch(String userDisplayName) {
		methodName = "findOpenBatch("+userDisplayName+")";
		LOG.debug(methodName + " called");
		
		try {
			Integer notSentId = getNotSentRefStatusCode().getRefStatusCodeId();
			LOG.debug("notSentId="+notSentId);
			Collection<PddaBatchBasicValue> batches = getPddaBatchHelper().findOpenBatch(notSentId);
			PddaBatchBasicValue result = null;
			if (batches != null && !batches.isEmpty()) {
				LOG.debug(batches.size() + " open batches found, returning the first one.");
				result = batches.iterator().next(); // Just get the first open batch
				/*for (PddaBatchBasicValue batchBasicValue : batches) {
					Integer originalBatchStatusId = new Integer(batchBasicValue.getBatchStatusId());
					batchBasicValue = validateAndSendBatch(batchBasicValue);
					// Status has changed then update it
					if (!originalBatchStatusId.equals(batchBasicValue.getBatchStatusId())) {
						getPddaBatchHelper().updateBatch(batchBasicValue, userDisplayName);
					} else if (result == null && getNotSentRefStatusCode().getRefStatusCodeId().equals(batchBasicValue.getBatchStatusId())) {
						// No changes so this one is valid
					result = batchBasicValue;
					}
				}*/
			} else {
				LOG.debug("No open batches found");
			}
			return result;
		} catch (FinderException ex) {
			return null;
		}
	}
	
	private RefStatusCodes getNotSentRefStatusCode() throws FinderException {
		if (notSentRefStatusCode == null) {
			notSentRefStatusCode = getRefStatusCode(REF_STATUS.NOT_SENT);
		}
		return notSentRefStatusCode;
	}

	private RefStatusCodes getReadyToSendRefStatusCode() throws FinderException {
		if (readyToSendRefStatusCode == null) {
			readyToSendRefStatusCode = getRefStatusCode(REF_STATUS.READY_TO_SEND);
		}
		return readyToSendRefStatusCode;
	}
	
	private RefStatusCodes getBatchSuccessRefStatusCode() throws FinderException {
		if (batchSuccessRefStatusCode == null) {
			batchSuccessRefStatusCode = getRefStatusCode(REF_STATUS.BATCH_SUCCESS);
		}
		return batchSuccessRefStatusCode;
	}
	
	private RefStatusCodes getBatchFailureRefStatusCode() throws FinderException {
		if (batchFailureRefStatusCode == null) {
			batchFailureRefStatusCode = getRefStatusCode(REF_STATUS.BATCH_FAILURE);
		}
		return batchFailureRefStatusCode;
	}
	
	private RefStatusCodes getRefStatusCode(String statusCode) throws FinderException {
		try {
			RefStatusCodes result = getRefStatusCodesMaintainer().findByCode(statusCode);
			return result;
		} catch (FinderException ex) {
			LOG.error("Error: Cannot find RefStatusCodes for statusCode "+statusCode);
			throw ex;
		}
	}
	
	
	/**
	 * Creates the CLOB of the serialized message, creates an entry in XHB_PDDA_MESASGE and places message in a
	 * batch to be sent to BAIS.
	 * 
	 * The method signature has not been changed, for details see the comment for the method convertToPDDAMessage below
	 * 
	 * @param serializedMessage
	 * @param event
	 * @param userDisplayName
	 */
	private void sendMessageToPDDA(String serializedMessage, PublicDisplayEvent event, String userDisplayName,
			String cpDocumentName, String cpDocumentStatus, String cpResponseGenerated, Integer cpStagingInboundId) {
		LOG.debug("Entering method: sendMessageToPDDA");
		try {
			// Call to find or create an open batch
			PddaBatchBasicValue batchBasicValue = findOrCreateOpenBatch(userDisplayName);
			
			// Get the court / courtRoomId for the event
			Integer courtId = event.getCourtId();
			Integer courtRoomId = null;
			if (event instanceof CourtRoomEvent) {
				courtRoomId = ((CourtRoomEvent) event).getCourtRoomId();
			} 
			
			// Create the clob data for the message - use seriialzed message which is a String
			Long pddaMessageClobId = null;
			if ((serializedMessage != null) && (serializedMessage.length() > 0)) {
				pddaMessageClobId = createClob(serializedMessage, userDisplayName);
			} else {
				LOG.debug("The serialized message is empty or null - should this happen?");
			}
			
			// Call to create the Pdda MessageType and Message
			LOG.debug("Call to create the Pdda MessageType and Message; pddaMessageClobId="+pddaMessageClobId);
			createMessageTypeAndMessage("XPD", event, batchBasicValue,
					userDisplayName, courtId, courtRoomId, pddaMessageClobId, null, cpDocumentName, cpDocumentStatus,
					cpResponseGenerated, cpStagingInboundId);
			
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}
	
	/**
	 * 
	 * @param userDisplayName
	 * @return
	 * @throws FinderException
	 */
	public PddaBatchBasicValue findOrCreateOpenBatch(String userDisplayName) throws FinderException {
		
		String METHOD_NAME="createMessageTypeAndMessage";
    	LOG.debug("Entering "+METHOD_NAME);
    	
		// Find Open batch
		PddaBatchBasicValue batchBasicValue = findOpenBatch(userDisplayName);		
		
		// If there are no open batches, create a Batch
		if (batchBasicValue == null){
			LOG.debug("No open batches, creating a new batch");
			batchBasicValue = createBatch(userDisplayName);
		} else {
			// Increment the record count in the batch
			batchBasicValue.setNoOfRecordsInBatch(batchBasicValue.getNoOfRecordsInBatch()+1);
			batchBasicValue = validateAndSendBatch(batchBasicValue);
			
			// Update the batch
			LOG.debug("updating a batch; batchBasicValue="+batchBasicValue.getPddaBatchId());
			getPddaBatchHelper().updateBatch(batchBasicValue, userDisplayName);
		}
		return batchBasicValue;
	}
	
	/**
	 * 
	 * @param isList
	 * @param event
	 * @param batchBasicValue
	 * @param userDisplayName
	 * @param courtId
	 * @param courtRoomId
	 * @param pddaMessageClobId
	 * @param pddaMessageBlobId
	 * @param cppDocumentName
	 * @throws FinderException
	 */
	public void createMessageTypeAndMessage(String messageType, PublicDisplayEvent event, 
			PddaBatchBasicValue batchBasicValue, String userDisplayName, 
			Integer courtId, Integer courtRoomId, Long pddaMessageClobId,
			Long pddaMessageBlobId,	String cppDocumentName, String cppDocumentStatus,
			String cpResponseGenerated, Integer cpStagingInboundId) throws FinderException {
		
		methodName = "createMessageTypeAndMessage";
    	LOG.debug("Entering "+methodName+"; messageType="+messageType);
    	
		// Call to find or create an open batch
		if (batchBasicValue == null) {
			batchBasicValue = findOrCreateOpenBatch(userDisplayName);
		}
		
		if (messageType.equals("XPD")) { // XHIBIT Public Display Event
			messageType = (event.getClass().getSimpleName()).replace("Event", "");
		} else if (messageType.equals("XDL")) { // XHIBIT List
			messageType = "List";
		} else if (messageType.equals("IWP")) {
			messageType="IWP";
		} else {
			messageType = "PublicDisplayCPP";
		}
		
		LOG.debug("Trying to find the message type, if it doesnt exist it will be created");
		RefPddaMessageTypeBasicValue messageTypeBasicValue = getPddaMessageHelper().findByMessageType(messageType);
		
		if (messageTypeBasicValue == null) {
			LOG.debug("Creating a new message type");
			messageTypeBasicValue = createMessageType(messageType,
					  				batchBasicValue.getBatchOpenedDatetime(),
					  				userDisplayName);
		}
		// Call createMessage
		LOG.debug("Creating a new message:: "
				+ "messageTypeBasicValue.getRefPddaMessageTypeId()="+messageTypeBasicValue.getRefPddaMessageTypeId()
				+ "pddaMessageClobId="+pddaMessageClobId
				+ "pddaMessageBlobId="+pddaMessageBlobId
				+ "batchBasicValue.getPddaBatchId()="+batchBasicValue.getPddaBatchId());
		createMessage(courtId, courtRoomId, messageTypeBasicValue.getRefPddaMessageTypeId(),
				pddaMessageClobId, pddaMessageBlobId, batchBasicValue.getPddaBatchId(),
				userDisplayName, cppDocumentName, cppDocumentStatus,
				cpResponseGenerated, cpStagingInboundId);
	}
	
	/**
	 * 
	 * @param clobData
	 * @param userDisplayName
	 * @return
	 */
	private Long createClob(String clobData, String userDisplayName) {
		methodName = "createClob";
    	LOG.debug("Entering "+methodName);
    	
		if (clobData != null) {
			try {
				XhbClobBasicValue basicValue = new XhbClobBasicValue();
				basicValue.setClobData(clobData);
				basicValue = XhbClobBeanHelper2.create(basicValue);
				return basicValue.getClobId(); 
			} catch (EJBException ex) {
				CSServices.getDefaultErrorHandler().handleError(ex, getClass());
				throw new EJBException(ex);
			}
		}
		return null;
	}
	
	
	/**
	 * Get unsent and valid data from XHB_INTERNET_HTML to send to PDDA
	 * 
	 * @param userDisplayName
	 */
	public void sendIwpDataToPDDA(String userDisplayName) {
		LOG.debug("Entering method sendIwpDataToPDDA");
		// Get next set of valid records to send to PDDA
		try {
			PddaDatabaseManager pdm = new PddaDatabaseManager();
			InternetHtmlPddaValue ihpv = pdm.getNextValidInternetHtmlForPdda(new Date());
			if (ihpv!= null) {
				LOG.debug("Found an IWP record to send to PDDA: XHB_INTERNET_HTML_ID=" +ihpv.getInternetHtmlId());
			
				// Send this to PDDA			
				// Get any other data needed to pass to the method to send to PDDA
				
				// Get the 3 digit crest court id from the 2 digit courtid
				String crestCourtId = getCourtCode(ihpv.getCourtId());
				
				// Consider the following:
				// - the original version of this method expects a clobid and not a blobid
				// - format the document name correctly
				String iwpDocumentName = "WebPage_"+crestCourtId+"_"+BATCH_FILENAME_DATETIMEFORMAT.format(new Date())
					+ "_XHIBIT.htm";
        		final String EMPTY_DOCUMENT_STATUS = "";
        		final String EMPTY_RESPONSE_GENERATED = "";
        		final Integer EMPTY_STAGING_INBOUND_ID = null;
				createMessageTypeAndMessage("IWP", null, null, userDisplayName, 
					ihpv.getCourtId(), null, null, ihpv.getHtmlBlobId(), iwpDocumentName,
					EMPTY_DOCUMENT_STATUS, EMPTY_RESPONSE_GENERATED, EMPTY_STAGING_INBOUND_ID);
				
				// Now update the record in XHB_INTERNET_HTML so that the STATUS='P' and it wont get 
				// processed again
				updateInternetHtml(ihpv.getInternetHtmlId(), ihpv.getHtmlBlobId(), ihpv.getLastUpdatedBy());
			
			} else {
				LOG.debug("Did not find any records in XHB_INTERNET_HTML to send to PDDA");
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param internetHtmlId
	 * @param blobId
	 * @param userDisplayName
	 * @return
	 */
	private void updateInternetHtml(Integer internetHtmlId, Long blobId, String userDisplayName) {
		methodName = "updateInternetHtml";
    	LOG.debug("Entering "+methodName);
    	
		if (internetHtmlId != null) {
			try {
				XhbInternetHtmlBasicValue basicValue = XhbInternetHtmlBeanHelper2.findByPrimaryKeyValue(internetHtmlId);
				basicValue.setStatus(SENT_IWP_DOCUMENT_TO_PDDA);
				basicValue = XhbInternetHtmlBeanHelper2.update(basicValue);
			} catch (EJBException ex) {
				CSServices.getDefaultErrorHandler().handleError(ex, getClass());
				throw new EJBException(ex);
			}
		}
	}
	
	private RefStatusCodesMaintainer getRefStatusCodesMaintainer() {
		if (refStatusCodesMaintainer == null) {
			refStatusCodesMaintainer = new RefStatusCodesMaintainer();
		}
		return refStatusCodesMaintainer;
	}
	
	private PddaBatchHelper getPddaBatchHelper() {
		if (pddaBatchHelper == null) {
			pddaBatchHelper = new PddaBatchHelper();
		}
		return pddaBatchHelper;
	}
	
	private PddaMessageHelper getPddaMessageHelper() {
		if (pddaMessageHelper == null) {
			pddaMessageHelper = new PddaMessageHelper();
		}
		return pddaMessageHelper;
	}
	
	private ConfigPropMaintainer getConfigPropMaintainer() {
		if (configPropMaintainer == null) {
			configPropMaintainer = new ConfigPropMaintainer();
		}
		return configPropMaintainer;
	}
	
	private PddaDatabaseManager getPddaDatabaseManager() {
		if (pddaDatabaseManager ==  null) {
			pddaDatabaseManager = new PddaDatabaseManager();
		}
		return pddaDatabaseManager;
	}
	
	private PddaSFTPHelper getSFTPHelper() {
		if (sftpHelper == null) {
			sftpHelper = new PddaSFTPHelper();
		}
		return sftpHelper;
	}
	
	private class ConfigPropMaintainer {
		
		public XhbConfigPropBasicValue getConfigPropBasicValue(String propertyName) {
			XhbConfigPropBasicValue[] properties = XhbConfigPropBeanHelper2.findByPropertyNameValue(propertyName);
			if (properties != null) {
				for( XhbConfigPropBasicValue basicValue : properties){
					return basicValue;
				}
			}
			return null;
		}
		
		public String getPropertyValue(String propertyName) {
			XhbConfigPropBasicValue basicValue = getConfigPropBasicValue(propertyName);
			if (basicValue != null) {
				return basicValue.getPropertyValue();
			}
			return null;
		}
	}
	
	private class SftpConfig {
		public String username = null;
		public String password = null;
		public String remoteFolder = null;
		public String host = null;
		public Integer port = null;
		public String errorMsg = null;
		public Session session = null;
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("sftpCOnfig is as follows:");
			sb.append("username="+username);
			sb.append("password="+password);
			sb.append("remoteFolder="+remoteFolder);
			sb.append("host="+host);
			sb.append("port="+port);
			
			return sb.toString();
		}
	}
}