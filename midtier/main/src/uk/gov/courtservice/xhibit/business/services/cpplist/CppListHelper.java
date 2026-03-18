package uk.gov.courtservice.xhibit.business.services.cpplist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.cpplist.CppList;
import uk.gov.courtservice.xhibit.business.entities.cpplist.CppListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.cppstaginginbound.CppStagingInbound;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.CppListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CppListComplexValue;

/**
 * <p>
 * Title: CppListHelper
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
 * @author Mark Harris
 * @version 1.0
 */
public class CppListHelper {
	private static final Logger LOG = CSServices.getLogger(CppListHelper.class);

	private CppListMaintainer cppListMaintainer;
	private CourtMaintainer courtMaintainer;
	
	private String methodName;
	
	public static String NOT_PROCESSED = "NP";

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CppListHelper() {
		cppListMaintainer = new CppListMaintainer();
		courtMaintainer = new CourtMaintainer();
	}
	
	/**
	 * Description: Update XHB_CPP_LIST record
	 * 
	 * @param complexValue
	 * @throws FinderException 
	 */
	public Long updateCppList(final CppListComplexValue complexValue, final String userDisplayName) throws FinderException {
		methodName = "updateCppList()";
		LOG.debug(methodName + " called");
		try {
			if (complexValue.getListClob() != null && complexValue.getListClobId() == null) {
				Long listClobId = createClob(complexValue.getListClob());
				complexValue.setListClobId(listClobId);
			}
			if (complexValue.getMergedClob() != null && complexValue.getMergedClobId() == null) {
				Long mergedClobId = createClob(complexValue.getMergedClob());
				complexValue.setMergedClobId(mergedClobId);
			}
			
			cppListMaintainer.update(complexValue, userDisplayName);
			return complexValue.getMergedClobId();
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
	
	/**
	 * Description: Update XHB_CPP_LIST record (basic update only)
	 * 
	 * @param basicValue
	 * @throws FinderException 
	 */
	public void updateCppList(final CppListBasicValue basicValue, final String userDisplayName) throws FinderException {
		methodName = "updateCppList()";
		LOG.debug(methodName + " called");
		try {
			cppListMaintainer.update(basicValue, userDisplayName);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}

	/**
	 * Description: Returns the latest unprocessed XHB_CPP_LIST record for a given court/listType/date
	 * 
	 * @param courtId
	 * @param listType
	 * @param listStartDate
	 * @return CppListComplexValue
	 */
	public CppListComplexValue getLatestCPPList(final Integer courtId, final String listType, final Date listStartDate) {
		methodName = "getLatestCPPList(" + courtId + "," + listType + "," + listStartDate + ")";
		LOG.debug(methodName + " called");
		CppListComplexValue result = null;
		try {
			List<CppList> locals = getCPPLists(courtId, listType, listStartDate);
			if (locals.size() > 0) {
				result = cppListMaintainer.getComplexValue(locals.get(0));
				result = populateComplexValue(result);
			}
		} catch (FinderException e) {
			result = null;
		}		
		return result;
	}
	
	/**
	 * Description: Returns the latest unprocessed XHB_CPP_LIST record for a given court/listType/start date/end date
	 * 
	 * @param courtId
	 * @param listType
	 * @param listStartDate
	 * @param listEndDate
	 * @return CppListComplexValue
	 */
	public CppListComplexValue getCPPListByCourtAndDate(final Integer courtId, final String listType, final Date listStartDate, final Date listEndDate) {
		methodName = "getCPPListByCourtAndDate(" + courtId + "," + listType + "," + listStartDate + "," + listEndDate + ")";
		LOG.debug(methodName + " called");
		CppListComplexValue result = null;
		try {
			if (listType != null) {
				ArrayList<CppList> docs = (ArrayList<CppList>) cppListMaintainer.findByCourtCodeAndListTypeAndListStartAndEndDate(courtId, listType.substring(0,1), listStartDate, listEndDate);
				if (docs.size() > 0) {
					CppList cppList = docs.get(0);
					result = cppListMaintainer.getComplexValue(cppList);
				}
			} else {
				LOG.debug("List type is invalid");
			}
		} catch (FinderException e) {
			result = null;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<CppList> getCPPLists(final Integer courtId, final String listType, final Date listStartDate) throws FinderException {
		
		// Get the crest court code from the court id 
		Integer courtCode = getCourtCode(courtId);
		
		// Populate the return value
		List<CppList> locals;
		try {
			locals = (List<CppList>) cppListMaintainer.findByCourtCodeAndListTypeAndListDate
					(courtCode, listType, listStartDate);
		} catch (FinderException e) {
			locals = new ArrayList<CppList>();
		}
		return locals;
	}

	private CppListComplexValue populateComplexValue(CppListComplexValue value) throws FinderException {
		LOG.debug("populateComplexValue(value="+value+")");
		
		CppListComplexValue complexValue = value;
		if (value.getListClobId() != null) {			
			value.setListClob(getClob(value.getListClobId().longValue()));
		}
		if (value.getMergedClobId() != null) {
			value.setMergedClob(getClob(value.getMergedClobId().longValue()));
		}
		
		return complexValue;
	}

	private Integer getCourtCode(final Integer courtId) throws FinderException {
		try {
			Court court = courtMaintainer.findByPrimaryKey(courtId);
			Integer courtCode = Integer.valueOf(court.getCrestCourtId()); 
			return courtCode;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (NumberFormatException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());			
			throw new FinderException("XhbCourt.CrestCourtId contains an alphanumeric value");
		}
	}
		
	private Long createClob(XhbClobBasicValue clobBasicValue) {		
		clobBasicValue = XhbClobBeanHelper2.create(clobBasicValue);
		return clobBasicValue.getClobId(); 
	}
	
	private XhbClobBasicValue getClob(Long clobId) {
		XhbClobBasicValue result = XhbClobBeanHelper2.findByPrimaryKeyValue(clobId); 
		return result;
	}
	
}