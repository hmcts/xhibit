package uk.gov.courtservice.xhibit.business.services.cppstaginginbound;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.cppstaginginbound.CppStagingInbound;
import uk.gov.courtservice.xhibit.business.entities.cppstaginginbound.CppStagingInboundMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigProp;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.CppStagingInboundBasicValue;


/**
 * <p>
 * Title: CppStagingInboundHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */
public class CppStagingInboundHelper {
	private static final Logger LOG = CSServices.getLogger(CppStagingInboundHelper.class);

	public static final String VALIDATION_STATUS_SUCCESS = "VS";
	
	public static final String VALIDATION_STATUS_FAIL ="VF";
	
	public static final String VALIDATION_STATUS_INPROCESS ="IP";
	
	public static final String VALIDATION_STATUS_NOTPROCESSED ="NP";
	
	public static final String PROCESSING_STATUS_NOTPROCESSED ="NP";
	
	public static final String PROCESSING_STATUS_FAIL ="PF";
	
	public static final String PROCESSING_STATUS_SENT ="SP";
	
	public static final String ACKNOWLEDGMENT_STATUS_SENT ="AS";

    private static Integer NUMBER_OF_DOCS_TO_PROCESS;

	private CppStagingInboundMaintainer cppStagingInboundMaintainer;
	
	private String methodName;
	
	

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CppStagingInboundHelper() {
		cppStagingInboundMaintainer = new CppStagingInboundMaintainer();
		ArrayList<XhbConfigProp> properties = (ArrayList<XhbConfigProp>) XhbConfigPropBeanHelper2.findByPropertyName("STAGING_DOCS_TO_PROCESS");
        if(properties.size()>0) {
        	NUMBER_OF_DOCS_TO_PROCESS=Integer.parseInt(properties.get(0).getPropertyValue());
        } else {
        	NUMBER_OF_DOCS_TO_PROCESS=1;
        }
	}

	/**
	 * Description: Returns the latest unprocessed XHB_CPP_STAGING_INBOUND record
	 * 
	 * @return CppStagingInboundBasicValue
	 * @throws CppStagingInboundControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CppStagingInboundBasicValue> findNextDocumentByStatus(String validationStatus, String processingStatus) 
		throws CppStagingInboundControllerException {
		methodName = "getLatestPublicDisplayDocument()";
		LOG.debug(methodName + " called");
		ArrayList<CppStagingInboundBasicValue> toReturn = new ArrayList<CppStagingInboundBasicValue>();
		
		if (validationStatus == null) {
			validationStatus = "";
		}
		if (processingStatus == null) {
			processingStatus = "";
		}

		try {
			ArrayList<CppStagingInbound> docs = (ArrayList<CppStagingInbound>) cppStagingInboundMaintainer.findNextDocumentByStatus(getCurrentDateNoTimestamp(),validationStatus, processingStatus);
			if ( docs != null && docs.size() > 0 ) {
				for(int i=0;i<NUMBER_OF_DOCS_TO_PROCESS;i++){
					if(i>=docs.size()) {
						break;
			}
					CppStagingInbound cppSI = docs.get(i);
					toReturn.add(cppStagingInboundMaintainer.getCppStagingInboundBasicValue(cppSI));
				}
			}

		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CppStagingInboundHelper.class);
		}

		return toReturn;
	}
	

	
	/**
	 * Description: Updates the XHB_CPP_STAGING_INBOUND object provided with a new status for either or both of:
	 * 				ValidationStatus and ProcessingStatus
	 * 
	 * @param CppStagingInboundBasicValue cppStagingInboundBasicValue
	 * @param String userDisplayName
	 */
	public void updateCppStagingInbound(CppStagingInboundBasicValue cppStagingInboundBasicValue, String userDisplayName) {
		String methodName = "updateCppStagingInbound(" + cppStagingInboundBasicValue + "," + userDisplayName + ") - ";
		LOG.debug(methodName + " called");
		try {
			cppStagingInboundMaintainer.update(cppStagingInboundBasicValue, userDisplayName);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CppStagingInboundHelper.class);
		}

	}
	
	
	private Date getCurrentDateNoTimestamp() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}