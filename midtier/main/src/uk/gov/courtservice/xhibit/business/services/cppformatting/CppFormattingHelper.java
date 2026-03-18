package uk.gov.courtservice.xhibit.business.services.cppformatting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.cppformatting.CppFormatting;
import uk.gov.courtservice.xhibit.business.entities.cppformatting.CppFormattingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.cppformattingmerge.CppFormattingMergeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_formatting.XhbFormatting;
import uk.gov.courtservice.xhibit.business.entities.xhb_formatting.XhbFormattingBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingMergeBasicValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;

/**
 * <p>
 * Title: CppFormattingHelper
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
 * @author Chris Vincent
 * @version 1.0
 */
public class CppFormattingHelper {
	private static final Logger LOG = CSServices.getLogger(CppFormattingHelper.class);

	private static final String DOC_TYPE_PUBLIC_DISPLAY = "PD";

	private static final String DOC_TYPE_WEB_PAGE = "WP";

	protected static final String FORMAT_STATUS_SUCCESS = "MS";

	protected static final String FORMAT_STATUS_FAIL = "MF";
	
	public static final String FORMAT_STATUS_NOT_PROCESSED ="ND";

	private CppFormattingMaintainer cppFormattingMaintainer;
	private CppFormattingMergeMaintainer cppFormattingMergeMaintainer;
	private CourtMaintainer courtMaintainer;
	private CppFormattingDatabaseManager cppDBManager;

	private String methodName;

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CppFormattingHelper() {
		cppFormattingMaintainer = new CppFormattingMaintainer();
		cppFormattingMergeMaintainer = new CppFormattingMergeMaintainer();
		courtMaintainer = new CourtMaintainer();
		cppDBManager = new CppFormattingDatabaseManager();
	}

	/**
	 * Description: Returns the latest unprocessed XHB_CPP_FORMATTING record for
	 * Public Display
	 * 
	 * @param courtId
	 * @return CppFormattingBasicValue
	 * @throws CppFormattingControllerException
	 */
	@SuppressWarnings("unchecked")
	public CppFormattingBasicValue getLatestPublicDisplayDocument(Integer courtId)
			throws CppFormattingControllerException {
		methodName = "getLatestPublicDisplayDocument(" + courtId + ")";
		LOG.debug(methodName + " called");
		CppFormattingBasicValue doc = null;

		try {
			ArrayList<CppFormatting> docs = (ArrayList<CppFormatting>) cppFormattingMaintainer
					.findByCourtAndDocType(courtId, DOC_TYPE_PUBLIC_DISPLAY, getCurrentDateNoTimestamp());
			if (docs != null && docs.size() > 0) {
				CppFormatting cppF = docs.get(0);
				doc = cppFormattingMaintainer.getCppFormattingBasicValue(cppF);
			}

		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CppFormattingHelper.class);
			throw new CppFormattingControllerException("Latest Public Display document does not exist",
					"Latest Public Display document does not exist");
		}

		return doc;
	}

	/**
	 * Description: Returns the latest unprocessed XHB_CPP_FORMATTING record for
	 * Internet Web Pages
	 * 
	 * @param courtId
	 * @return CppFormatting
	 * @throws CppFormattingControllerException
	 */
	@SuppressWarnings("unchecked")
	public CppFormattingBasicValue getLatestWebPageDocument(Integer courtId) throws CppFormattingControllerException {
		methodName = "getLatestWebPageDocument(" + courtId + ")";
		LOG.debug(methodName + " called");
		CppFormattingBasicValue doc = null;

		try {
			ArrayList<CppFormatting> docs = (ArrayList<CppFormatting>) cppFormattingMaintainer
					.findByCourtAndDocType(courtId, DOC_TYPE_WEB_PAGE, getCurrentDateNoTimestamp());
			if (docs != null && docs.size() > 0) {
				CppFormatting cppF = docs.get(0);
				doc = cppFormattingMaintainer.getCppFormattingBasicValue(cppF);
			}

		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CppFormattingHelper.class);
			throw new CppFormattingControllerException("Latest IWP document does not exist",
					"Latest IWP document does not exist");
		}

		return doc;
	}

	/**
	 * Description: Updates the XHB_CPP_FORMATTING object provided with a new
	 * status
	 * 
	 * @param CppFormattingBasicValue
	 *            cppFormattingBasicValue
	 * @param String
	 *            newStatus
	 */
	public void updateCppFormattingStatus(CppFormattingBasicValue cppFormattingBasicValue, String newStatus) {
		String methodName = "updateCppFormattingStatus(" + cppFormattingBasicValue + ") - ";
		LOG.debug(methodName + " called");
		cppDBManager.updateCppFormattingStatus(cppFormattingBasicValue.getCppFormattingId(), newStatus);
	}

	/**
	 * Update the XHB_CPP_FORMATTING object based on the values in the incoming object
	 * 
	 * @param cppFormattingBasicValue
	 * @param userDisplayName
	 */
	public void updateCppFormatting(CppFormattingBasicValue cppFormattingBasicValue, String userDisplayName) {
		String methodName = "updateCppFormatting(" + cppFormattingBasicValue + "," + userDisplayName + ") - ";
		LOG.debug(methodName + " called");
		try {
			cppFormattingMaintainer.update(cppFormattingBasicValue, userDisplayName);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CppFormattingHelper.class);
		}
	}
	
	
	/**
	 * A finder to locate 1 or 0 records based on date, doctype and courtid
	 * 
	 * @param courtId
	 * @param documentType
	 * @return
	 * @throws FinderException
	 */
	public CppFormattingBasicValue findLatestByCourtDateInDoc(Integer courtId, String documentType)
			throws FinderException {
		methodName = "findLatestIWPByCourtAndDateIn(" + courtId + ")";
		LOG.debug(methodName + " called");
		try {
			CppFormattingBasicValue value = cppFormattingMaintainer.findLatestByCourtDateInDoc(courtId,
					getCurrentDateNoTimestamp(), documentType);
			return value;

		} catch (ObjectNotFoundException e) {
			LOG.debug("No CPP documents for the day for court " + courtId);
			return null;
		}
	}
	
	

	/**
	 * Create a new XHB_CLOB ROW FOR THE MERGED XML, UPDATE the ID of the
	 * formatted value to point to this new one and write an entry in
	 * XHB_CPP_FORMATTING_MERGE
	 * 
	 * @throws ObjectNotFoundException
	 *             if court or cpp formatting is not found which is invalid
	 */
	public void updatePostMerge(CppFormattingMergeBasicValue formattingMergeVal, String clobData)
			throws ObjectNotFoundException {
		methodName = "updatePostMerge for cppformatting id of " + formattingMergeVal.getCppFormattingId()
				+ " and formatting id of " + formattingMergeVal.getFormattingId();
		LOG.debug(methodName + " called");
		XhbClobBasicValue clobVal = new XhbClobBasicValue();
		clobVal.setClobData(clobData);
		// Create the clob
		XhbClobBasicValue val2 = XhbClobBeanHelper2.create(clobVal);

		Court court = courtMaintainer.findByPrimaryKey(formattingMergeVal.getCourtId());
		XhbFormatting formatting = XhbFormattingBeanHelper2.findByPrimaryKey(formattingMergeVal.getFormattingId());
		formatting.setXmlDocumentClobId(val2.getClobId());

		CppFormatting cppFormatting = cppFormattingMaintainer.findByPrimaryKey(formattingMergeVal.getCppFormattingId());
		cppFormatting.setErrorMessage("");
		cppFormatting.setFormatStatus("MS");

		// insert a row into the formatting merge maintainer
		cppFormattingMergeMaintainer.create(formattingMergeVal, "XHIBIT", court, cppFormatting);
	}

	/**
	 * Description: Retrieves all new public display documents from
	 * XHB_CPP_FORMATTING
	 * 
	 * @return ArrayList<CppFormattingBasicValue>
	 * @throws CppFormattingControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CppFormattingBasicValue> findAllNewPublicDisplayDocs() throws CppFormattingControllerException {
		methodName = "findAllNewPublicDisplayDocs()";
		LOG.debug(methodName + " called");
		ArrayList<CppFormattingBasicValue> cppList = new ArrayList<CppFormattingBasicValue>();

		try {
			ArrayList<CppFormatting> docs = (ArrayList<CppFormatting>) cppFormattingMaintainer
					.findAllNewByDocType(DOC_TYPE_PUBLIC_DISPLAY, getCurrentDateNoTimestamp());
			for (CppFormatting cppF : docs) {
				if (null != cppF) {
					// Convert the CppFormatting objects to
					// CppFormattingBasicValue objects
					cppList.add(cppFormattingMaintainer.getCppFormattingBasicValue(cppF));
				}
			}
			LOG.debug(methodName + " - New records found: " + cppList.size());

		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CppFormattingHelper.class);
			throw new CppFormattingControllerException(
					"Error retrieving list of new public display documents from CPP_FORMATTING",
					"Error retrieving list of new public display documents from CPP_FORMATTING");
		}

		return cppList;
	}
	
	/**
	 * Refreshes all public displays for the court specified
	 * @param courtId Court Id
	 */
	public void refreshPublicDisplaysForCourt(Integer courtId, String userDisplayName) {
		try {
			methodName = "refreshPublicDisplaysForCourt()";
			LOG.debug(methodName + "(courtId="+courtId+") called");
			PddaHelper notifier = new PddaHelper();
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			String courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
			CourtConfigurationChange ccc = new CourtConfigurationChange(courtId.intValue(), courtName, true);
	        ConfigurationChangeEvent ccEvent = new ConfigurationChangeEvent(ccc);
	        notifier.sendMessage(ccEvent, userDisplayName);
		} catch (ObjectNotFoundException e) {
			LOG.error("Cannot find the court site name.");
			e.printStackTrace();
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