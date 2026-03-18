package uk.gov.courtservice.xhibit.client.util.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Class to Retrieve sorted and validate System ref codes.
 * 
 * @author szfnvt
 *
 */
public class RefSystemCodeHelper {

	private static List<RefSystemCodeBasicValue> refSystemCodeList;

	/**
	 * No Arg constructor
	 *
	 */
	public RefSystemCodeHelper() {
		// Empty
	}

	/**
	 * Create RefSystemCodeCriteria to get correct codes.
	 * 
	 * @param codeType
	 * @param courtID
	 * @return
	 */
	private static RefSystemCodeCriteria createCriteria(String codeType, String courtID) {
		if (codeType == null || courtID == null) {
			throw new IllegalArgumentException("createCriteria - codeType and courtID parameters must contain values.");
		}
		RefSystemCodeCriteria refCriteria = new RefSystemCodeCriteria();
		refCriteria.setCodeType(codeType);
		refCriteria.setCourtId(courtID);
		return refCriteria;
	}

	/**
	 * Get Default RefSystemCodeComparator
	 * 
	 * @return
	 */
	public static RefSystemCodeComparator getRefSystemCodeComparator() {
		return new RefSystemCodeComparator(RefSystemCodeComparator.Comparer.CODE);
	}

	/**
	 * Get System code List
	 * 
	 * @param refCriteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<RefSystemCodeBasicValue> getSystemCodeList(RefSystemCodeCriteria refCriteria)
			throws BisRefControllerException {
		Collection findSystemCodes = XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(refCriteria);
		refSystemCodeList = new ArrayList<RefSystemCodeBasicValue>(findSystemCodes);
		return refSystemCodeList;
	}

	public static boolean isValidRefSystemCode(List<RefSystemCodeBasicValue> sortedList,
			RefSystemCodeBasicValue rscbv) {

		if (sortedList == null || rscbv == null)
			throw new IllegalArgumentException();

		return Collections.binarySearch(sortedList, rscbv, getRefSystemCodeComparator()) >= 0;
	}

	/*
	 * CREST now use two code_types to store force location codes. The main one
	 * is HO_POL_FORCE, but there is also HO_PNC_CODE which is currently used
	 * just for the 00 force location code.
	 */
	public static List<RefSystemCodeBasicValue> getSortedForceLocationCodes(Integer courtId) {

		List<RefSystemCodeBasicValue> forceLocationCodes;
		List<RefSystemCodeBasicValue> pncLocationCodes;

		RefSystemCodeCriteria HO_POL_FORCE = RefSystemCodeHelper.createCriteria("HO_POL_FORCE", courtId.toString());

		try {
			forceLocationCodes = RefSystemCodeHelper.getSystemCodeList(HO_POL_FORCE);
		} catch (BisRefControllerException e) {
			throw new CSUnrecoverableException(e);
		}

		RefSystemCodeCriteria HO_PNC_CODE = RefSystemCodeHelper.createCriteria("HO_PNC_CODE", courtId.toString());

		try {
			pncLocationCodes = RefSystemCodeHelper.getSystemCodeList(HO_PNC_CODE);

		} catch (BisRefControllerException e) {
			throw new CSUnrecoverableException(e);
		}

		forceLocationCodes.addAll(pncLocationCodes);
		Collections.sort(forceLocationCodes, getRefSystemCodeComparator());

		refSystemCodeList = forceLocationCodes;
		return refSystemCodeList;
	}
}

class RefSystemCodeComparator implements Comparator<RefSystemCodeBasicValue> {
	enum Comparer {
		CODE, DECODE
	}

	Comparer comparer;

	public RefSystemCodeComparator(Comparer comparer) {
		this.comparer = comparer;
	}

	public int compare(RefSystemCodeBasicValue o1, RefSystemCodeBasicValue o2) {
		int rtn = 0;
		int comparison;
		switch (comparer) {
		case CODE:
			comparison = o1.getCode().compareTo(o2.getCode());
			if (comparison > 0) {
				rtn = 1;
			} else if (comparison < 0) {
				rtn = -1;
			}
			return rtn;
		case DECODE:
			comparison = o1.getDecode().compareTo(o2.getDecode());
			if (comparison > 0) {
				rtn = 1;
			} else if (comparison < 0) {
				rtn = -1;
			}
			return rtn;
		default:
			return rtn;
		}
	}
}