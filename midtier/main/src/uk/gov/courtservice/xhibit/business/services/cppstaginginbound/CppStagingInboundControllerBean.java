package uk.gov.courtservice.xhibit.business.services.cppstaginginbound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigProp;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.validation.ValidationException;
import uk.gov.courtservice.xhibit.business.services.validation.ValidationResult;
import uk.gov.courtservice.xhibit.business.services.validation.ValidationService;
import uk.gov.courtservice.xhibit.business.services.validation.sax.FileEntityResolver;
import uk.gov.courtservice.xhibit.business.services.validation.sax.SAXValidationService;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CppStagingInboundBasicValue;

/**
 * <p>
 * Title: CppStagingInboundControllerBean
 * </p>
 * <p>
 * Description: Local interface to CPP Staging Inbound session facade.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="CppStagingInboundController" description="CPP Staging Inbound
 *           Session Bean" type="Stateless" view-type="both"
 *           jndi-name="CppStagingInboundControllerHome"
 *           local-jndi-name="CppStagingInboundControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Scott Atwell
 * @version $Revision: 1.0 $
 */
public class CppStagingInboundControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = -1482124759093214736L;
	
	private static final Logger log = CSServices.getLogger(CppStagingInboundControllerBean.class);
	
    // Specify location of schemas in the startup properties - we cannot set a default as it changes from environment to environment
    private static final String SCHEMA_DIR_DEFAULT = System.getProperty("ValidationMessageBean.schemaDir");
    
    private static final String ENTERED = " : entered";
    private static final String DOCUMENT_NAME = "Document name ";

	// set up the helper class
	CppStagingInboundHelper cppStagingInboundHelper = new CppStagingInboundHelper();
	
	public enum CppDocumentTypes {
		WP,PD,DL,FL,WL;
	}
	
	public enum CppDocumentNameInIncomingDocument {
		WebPage,PublicDisplay,DailyList,FirmList,WarnedList;
	}

	private static uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropHome configHome = null;

	static Class clazz = CppStagingInboundControllerBean.class;
	static {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			configHome = (uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropHome.JNDI_NAME);
			
		} catch (NamingException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ignore) {
				CSServices.getDefaultErrorHandler().handleError(ignore, clazz);
				ignore.printStackTrace();
			}
		}
	}

	
	/**
	 * <p>
	 * Returns the latest unprocessed XHB_CPP_STAGING_INBOUND record for processing
	 * </p>
	 * 
	 * @return CppStagingInboundBasicValue
	 * @throws CppStagingInboundControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public ArrayList<CppStagingInboundBasicValue> getLatestUnprocessedDocument()
			throws CppStagingInboundControllerException {
		String methodName = "getLatestUnprocessedDocument() - ";
		log.debug(methodName + ENTERED);
		ArrayList<CppStagingInboundBasicValue> toReturn = new ArrayList<CppStagingInboundBasicValue>();

		try {
			String processingStatus = ""; // Not doing any searches by processingStatus but passing a necessary empty string
			toReturn = cppStagingInboundHelper.findNextDocumentByStatus(CppStagingInboundHelper.VALIDATION_STATUS_NOTPROCESSED, processingStatus);
		} catch (CppStagingInboundControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
		return toReturn;
	}
	
	
	/**
	 * <p>
	 * Returns the latest record from XHB_CPP_STAGING_INBOUND that has been validated successfully and not processed 
	 * </p>
	 * 
	 * @return CppStagingInboundBasicValue
	 * @throws CppStagingInboundControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public ArrayList<CppStagingInboundBasicValue> getNextValidatedDocument()
			throws CppStagingInboundControllerException {
		String methodName = "getNextValidatedDocument() - ";
		log.debug(methodName + ENTERED);
		ArrayList<CppStagingInboundBasicValue> doc = new ArrayList<CppStagingInboundBasicValue>();

		try {
			// Find documents where VALIDATION_STATUS='VS' and PROCESSING_STATUS='NP'
			doc = cppStagingInboundHelper.findNextDocumentByStatus(CppStagingInboundHelper.VALIDATION_STATUS_SUCCESS, CppStagingInboundHelper.PROCESSING_STATUS_NOTPROCESSED);
		} catch (CppStagingInboundControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
		return doc;
	}
	
	
	/**
	 * <p>
	 * Returns the earliest XHB_CPP_STAGING_INBOUND from today that is to be validated
	 * </p>
	 * 
	 * @return CppStagingInboundBasicValue
	 * @throws CppStagingInboundControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public ArrayList<CppStagingInboundBasicValue> getNextDocumentToValidate()
			throws CppStagingInboundControllerException {
		String methodName = "getNextDocumentToValidate() - ";
		log.debug(methodName + ENTERED);
		ArrayList<CppStagingInboundBasicValue> toReturn = new ArrayList<CppStagingInboundBasicValue>();

		try {
			String processingStatus = ""; // Not doing any searches by processingStatus but passing a necessary empty string
			toReturn = cppStagingInboundHelper.findNextDocumentByStatus(CppStagingInboundHelper.VALIDATION_STATUS_INPROCESS, processingStatus);
		} catch (CppStagingInboundControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
		return toReturn;
	}
	
	
	/**
	 * Updates an XHB_CPP_STAGING_INBOUND record with a status of successfully validated 
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @paran String userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateStatusSuccess(CppStagingInboundBasicValue cppStagingInboundBasicValue, String userDisplayName) {
		String methodName = "updateStatusSuccess(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		log.debug(methodName + ENTERED);
		try {
			cppStagingInboundBasicValue.setProcessingStatus(CppStagingInboundHelper.PROCESSING_STATUS_NOTPROCESSED);
			cppStagingInboundBasicValue.setValidationStatus(CppStagingInboundHelper.VALIDATION_STATUS_SUCCESS);
			cppStagingInboundBasicValue.setLastUpdatedBy(userDisplayName);
			cppStagingInboundHelper.updateCppStagingInbound(
					cppStagingInboundBasicValue,
					userDisplayName
			);
				
		} catch (EJBException e) {
			this.errorHandling(methodName, e);
			throw e;
		}
	}
	
	/**
	 * Updates an XHB_CPP_STAGING_INBOUND record with a status of validation failed 
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @paran String userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateStatusFailed(CppStagingInboundBasicValue cppStagingInboundBasicValue, String reasonForFail, String userDisplayName) {
		String methodName = "updateStatusFailed(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		log.debug(methodName + ENTERED);
		try {
			cppStagingInboundBasicValue.setValidationStatus(CppStagingInboundHelper.VALIDATION_STATUS_FAIL);
			
			if (reasonForFail.length() > 4000) {
				reasonForFail = reasonForFail.substring(0,3999);
			}
			cppStagingInboundBasicValue.setValidationErrorMessage(reasonForFail); // Column limited to 4000 chars so truncate to avoid unrecoverable error
			cppStagingInboundBasicValue.setLastUpdatedBy(userDisplayName);
			cppStagingInboundHelper.updateCppStagingInbound(
					cppStagingInboundBasicValue, 
					userDisplayName
			);
				
		} catch (EJBException e) {
			this.errorHandling(methodName, e);
			throw e;
		}
	}
	
	/**
	 * Updates an XHB_CPP_STAGING_INBOUND record with a status of In Progress 
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @paran String userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateStatusInProcess(CppStagingInboundBasicValue cppStagingInboundBasicValue, String userDisplayName) {
		String methodName = "updateStatusInProcess(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		log.debug(methodName + ENTERED);
		try {
			cppStagingInboundBasicValue.setValidationStatus(CppStagingInboundHelper.VALIDATION_STATUS_INPROCESS);
			cppStagingInboundBasicValue.setLastUpdatedBy(userDisplayName);
			cppStagingInboundHelper.updateCppStagingInbound(
					cppStagingInboundBasicValue,
					userDisplayName
			);
				
		} catch (EJBException e) {
			this.errorHandling(methodName, e);
			throw e;
		}
	}
	
	
	/**
	 * Updates an XHB_CPP_STAGING_INBOUND record with a processing status of fail
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @paran String userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateStatusProcessingFail(CppStagingInboundBasicValue cppStagingInboundBasicValue, String reasonForFail, String userDisplayName) {
		String methodName = "updateStatusProcessingFail(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		log.debug(methodName + ENTERED);
		try {
			cppStagingInboundBasicValue.setProcessingStatus(CppStagingInboundHelper.PROCESSING_STATUS_FAIL);
			if (reasonForFail.length() > 4000) {
				reasonForFail = reasonForFail.substring(0,3999);
			}
			cppStagingInboundBasicValue.setValidationErrorMessage(reasonForFail); // Column limited to 4000 chars so truncate to avoid unrecoverable error
			cppStagingInboundBasicValue.setLastUpdatedBy(userDisplayName);
			cppStagingInboundHelper.updateCppStagingInbound(
					cppStagingInboundBasicValue,
					userDisplayName
			);
				
		} catch (EJBException e) {
			this.errorHandling(methodName, e);
			throw e;
		}
	}
	
	/**
	 * Updates an XHB_CPP_STAGING_INBOUND record with a processing status of fail
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @paran String userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateStatusProcessingSuccess(CppStagingInboundBasicValue cppStagingInboundBasicValue, String userDisplayName) {
		String methodName = "updateStatusProcessingSuccess(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		log.debug(methodName + ENTERED);
		try {
			cppStagingInboundBasicValue.setProcessingStatus(CppStagingInboundHelper.PROCESSING_STATUS_SENT);
			cppStagingInboundBasicValue.setLastUpdatedBy(userDisplayName);
			cppStagingInboundHelper.updateCppStagingInbound(
					cppStagingInboundBasicValue,
					userDisplayName
			);
				
		} catch (EJBException e) {
			this.errorHandling(methodName, e);
			throw e;
		}
	}
	
	/**
	 * Updates an XHB_CPP_STAGING_INBOUND record such that all status values are reset back to when there initial values
	 * This is useful for testing 
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @paran String userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void resetDocumentStatus(CppStagingInboundBasicValue cppStagingInboundBasicValue, String userDisplayName) {
		String methodName = "resetDocumentStatus(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		log.debug(methodName + ENTERED);
		try {
			cppStagingInboundHelper.updateCppStagingInbound(
					cppStagingInboundBasicValue,
					userDisplayName
			);
				
		} catch (EJBException e) {
			this.errorHandling(methodName, e);
			throw e;
		}
	}
	
	/**
	 * Validates document from XHB_STAGING_INBOUND where the current VALIDATION_STATUS='IP'
	 * This document is validated as follows:
	 * 	1. The DOCUMENT_NAME is checked to follow a valid format
	 *  2. The DOCUMENT_TYPE is checked to be valid
	 *  3. The appropriate schema to validate the XML against will be determined
	 *  4. Validation of the XML will be done against the appropriate schema 
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @paran String userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public boolean validateDocument(CppStagingInboundBasicValue cppStagingInboundBasicValue, String userDisplayName) throws ValidationException {
		String methodName = "validateDocument(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		log.debug(methodName + ENTERED);
		boolean validDocument = false;
		// Get schema to validate against
		String schemaName = getSchemaName(cppStagingInboundBasicValue.getDocumentType());
		try {
			boolean isDocumentNameOk = isValidDocumentName(cppStagingInboundBasicValue.getDocumentName());
			if (!isDocumentNameOk) {
				updateStatusFailed(cppStagingInboundBasicValue, "Document Name is invalid", userDisplayName);
				return false;
			}
			
			boolean isDocumentTypeOk = isValidDocumentType(cppStagingInboundBasicValue.getDocumentType());
			if (!isDocumentTypeOk) {
				updateStatusFailed(cppStagingInboundBasicValue, "Document Type is invalid", userDisplayName);
				return false;
			}
			
			
			// Get the XML
			String xmlToValidate = getClobXmlAsString(cppStagingInboundBasicValue.getClobId());
			
			
			// Validate the XML
			log.debug("SCHEMA_DIR_DEFAULT: "+SCHEMA_DIR_DEFAULT);
			ValidationService service = new SAXValidationService(new FileEntityResolver(SCHEMA_DIR_DEFAULT));
			ValidationResult validDoc = service.validate(xmlToValidate, schemaName);
			validDocument = validDoc.isValid();
			
			if (!validDocument) {
				updateStatusFailed(cppStagingInboundBasicValue, "Validation failed: Schema name:"+schemaName+"; error::"+validDoc.toString(), userDisplayName);
			} //Do a check to make sure the court is a cpp court if not we want to fail 
			else if(!isCPPCourt(cppStagingInboundBasicValue.getCourtCode())) {
				updateStatusFailed(cppStagingInboundBasicValue, "Validation failed: error:: CPP court flag not set ", userDisplayName);
			} //if its iwp and has zero courtsites the fail validation 
			else if(cppStagingInboundBasicValue.getDocumentType().equals("WP") && !hasCourtSites(xmlToValidate)) {
				updateStatusFailed(cppStagingInboundBasicValue, "Validation failed: error:: No court sites in document ", userDisplayName);
			} else {
				updateStatusSuccess(cppStagingInboundBasicValue, userDisplayName);
			}
			
		} catch (ValidationException ve) {
			updateStatusFailed(cppStagingInboundBasicValue, "Validation failed: Schema name:"+schemaName+"; error::"+ve.getCause().getMessage(), userDisplayName);
		} catch (EJBException e) {
			this.errorHandling(methodName, e);
			throw e;
		}
		return validDocument;
	}
	
	/**
	 * The incoming CPP document name must be valid according to the following rules:
	 * 	1. Daily List: DailyList_<CourtCode>_<SendDate>.xml
	 *  2. Firm List: FirmList_<CourtCode>_<SendDate>.xml
	 *  3. Warned List: WarnedList_<CourtCode>_<SendDate>.xml
	 *  4. Web Page: WebPage_<CourtCode>_<SendDate>.xml
	 *  
	 * Where:
	 * 		CourtCode is a 3 digit number between 400 and 500
	 * 		SendDate is a date of the format YYYYMMDDHH24MISS
	 * @param documentName
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public boolean isValidDocumentName(String documentName) {
		String methodName = "isValidDocumentName(" + documentName + ") - ";
		log.debug(methodName + ENTERED);
		
		boolean validDoc = false;
		if (!documentName.endsWith(".xml")) {
			return validDoc;
		}
		String s1 = documentName.substring(0, documentName.indexOf(".xml"));

		// Break into constituent parts
		String[] parts = s1.split("_");
		if (parts.length != 3) {
			log.error(DOCUMENT_NAME+documentName+" is invalid.");
			return validDoc;
		}
		String docType = parts[0];
		String courtCode = parts[1];
		String docTime = parts[2];
		
		// Check name is valid
		CppDocumentNameInIncomingDocument[] dts = CppDocumentNameInIncomingDocument.values();
		boolean validDocType = false;
		for (CppDocumentNameInIncomingDocument dt: dts) {
			if (docType.equals(dt.name())) {
				validDocType = true;
				break;
			}
		}
		if (!validDocType) {
			log.error(DOCUMENT_NAME+documentName+" is invalid. Unknown document type in title.");
			return validDoc;
		}
		
		// Check court code
		try {
			int courtId = Integer.parseInt(courtCode);
			if (courtId<400 || courtId>499) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Invalid court name in title.");
				return validDoc;
			}
		} catch (NumberFormatException nfe) {
			log.error("Cannot parse the time component of " + documentName);
			return false;
		}

		// Check time component
		if (docTime.length() != 14) {
			log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid.");
			return validDoc;
		}
		
		try {
			int docTimeYear = Integer.parseInt(docTime.substring(0,4));
			int docTimeMonth = Integer.parseInt(docTime.substring(4,6));
			int docTimeDay = Integer.parseInt(docTime.substring(6,8));
			int docTimeHour = Integer.parseInt(docTime.substring(8,10));
			int docTimeMinute = Integer.parseInt(docTime.substring(10,12));
			int docTimeSecond = Integer.parseInt(docTime.substring(12,14));
			if (docTime.length() != 14) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid.");
				return validDoc;
			}
			if (docTimeYear < 2020 || docTimeYear > 2099) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid - invalid year.");
				return validDoc;
			}
			if (docTimeMonth < 1 || docTimeMonth > 12) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid - invalid month.");
				return validDoc;
			}
			if (docTimeDay < 1 || docTimeDay > 31) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid - invalid day.");
				return validDoc;
			}
			if (docTimeHour < 0 || docTimeHour > 23) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid - invalid hour.");
				return validDoc;
			}
			if (docTimeMinute < 0 || docTimeMinute > 59) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid - invalid minute.");
				return validDoc;
			}
			if (docTimeSecond < 0 || docTimeSecond > 59) {
				log.error(DOCUMENT_NAME+documentName+" is invalid. Time is invalid - invalid second.");
				return validDoc;
			}
		} catch (NumberFormatException nfe) {
			log.error("Cannot parse the time component of " + documentName);
			return false;
		}
		
		return true;
	}
	
	/**
	 * The incoming CPP document type must be valid, i.e. WP, PD, DL, FL or WL
	 * 
	 * @param documentType
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public boolean isValidDocumentType(String documentType) {
		String methodName = "isValidDocumentType(" + documentType + ") - ";
		log.debug(methodName + ENTERED);
		
		boolean validDoc = false;
		CppDocumentTypes[] dts = CppDocumentTypes.values();
		for (CppDocumentTypes dt: dts) {
			if (documentType.equals(dt.name())) {
				validDoc = true;
				break;
			}
		}
		
		return validDoc;
	}
	
	/**
	 * Based on the document type return the (name of the) schema that is to be used to validate the XML
	 * The schema document itself will be picked up from SCHEMA_DIR as defined at the top of the class 
	 * 
	 * @param documentType
	 * @return String
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public String getSchemaName(String documentType) {
		String methodName = "getSchemaName(" + documentType + ") - ";
		log.debug(methodName + ENTERED);
		
		String schemaName = "";
		try {
			Iterator itr = configHome.findByPropertyName("CPPX_Schema"+documentType).iterator();
			while (itr.hasNext()) { // Should really only be one result returned!!
				XhbConfigProp tempConfigProp = (XhbConfigProp) itr.next();
				schemaName = tempConfigProp.getPropertyValue();
			}
		} catch (FinderException e) {
			log.error("Error finding schema name based on document type: "+documentType);
			throw new EJBException(e);		
		}

		return schemaName;
	}
	
	
	/**
	 * Get courtId from XHB_COURT_SITE using crest court id (aka court code)
	 * 
	 * @param courtCode
	 * @return
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	@SuppressWarnings("unchecked")
	public int getCourtId(Integer courtCode) {
		String methodName = "getCourtId(" + courtCode.intValue() + ") - ";
		log.debug(methodName + ENTERED);
		
		int courtId = 0;
		
		try {
			CourtSiteMaintainer courtSiteMaintainer = new CourtSiteMaintainer();
			Collection data = courtSiteMaintainer.findByCrestCourtId(courtCode);

			try {
				if (!data.isEmpty()) {
					CourtSiteBasicValue courtSite =  ((ArrayList<CourtSiteBasicValue>)data).get(0);
					courtId = courtSite.getCourtId();
				} else {
					log.debug("No court site items returned when searching for court code: "+courtCode.intValue());
				}
					
			} catch (Exception e) {
				log.error("Error finding court site based on crest court id: "+courtCode);
				throw new EJBException(e);
			}
			
		} catch (Exception fe) {
			log.error("Error finding court site based on crest court id: "+courtCode);
			throw new EJBException(fe);
		}
		
		return courtId;
	}
	
	

	/**
	 * Given a clob Id get the Xml
	 * 
	 * @param clobId
	 * @return
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public String getClobXmlAsString(Long clobId) {
		String methodName = "getClobXmlAsString(" + clobId.longValue() + ") - ";
		log.debug(methodName + ENTERED);
		
		XhbClobBasicValue clobObj = XhbClobBeanHelper2.findByPrimaryKeyValue(clobId);
		return clobObj.getClobData();
		
	}
	
	
	/**
	 * Errorhandling method that will be used for all catch blocks where the
	 * ???? is being caught. This is used since
	 * all public methods in this class are handled in the same way.
	 * 
	 * @param methodName
	 *            String
	 * @param e
	 *            Exception
	 */
	private void errorHandling(String methodName, Exception e) {
		ctx.setRollbackOnly();
		CSServices.getDefaultErrorHandler().handleError(e, getClass());
		log.error(methodName + " : failed! Transaction Rollback");
	}
	
	
	/**
     * Is court a CPP court 
     * @param courtId
     * @return true if so
     */
    private boolean isCPPCourt(Integer crestCourtId) {
    	//get court value
    	XhbCourtBasicValue[] court = XhbCourtBeanHelper2.findByCrestCourtIdValue(crestCourtId.toString());
    	if(court!=null && court.length>0) {
    		return ("Y").equals(court[0].getCppCourt());
    	}
    	return false;
    	
	}

	/**
	 * Used to check if there is a non blank courtsitename in there.
	 * @return
	 */
	public boolean hasCourtSites(String clob) {
		//<courtsitename>  followed by anything but <
		String matchString = "<courtsitename>[^<]";
		Pattern patt = Pattern.compile(matchString);
		Matcher match = patt.matcher(clob);
		return match.find();
		
	}


}