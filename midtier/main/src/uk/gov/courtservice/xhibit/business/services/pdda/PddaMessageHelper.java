package uk.gov.courtservice.xhibit.business.services.pdda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.pdda.PddaMessage;
import uk.gov.courtservice.xhibit.business.entities.pdda.PddaMessageMaintainer;
import uk.gov.courtservice.xhibit.business.entities.pdda.RefPddaMessageType;
import uk.gov.courtservice.xhibit.business.entities.pdda.RefPddaMessageTypeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.PddaMessageBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.PddaMessageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefPddaMessageTypeBasicValue;

/**
 * <p>
 * Title: PDDAMessageHelper
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
public class PddaMessageHelper {
	private static final Logger LOG = CSServices.getLogger(PddaMessageHelper.class);
	
	private String methodName;
	private PddaMessageMaintainer pddaMessageMaintainer;
	private RefPddaMessageTypeMaintainer refPddaMessageTypeMaintainer;
	
	public PddaMessageBasicValue findByPddaMessageId(final Integer pddaMessageId) throws FinderException {
		methodName = "findByPddaMessageId()";
		LOG.debug(methodName + " called");
		PddaMessageBasicValue result = null;
		try {
			PddaMessage pdmLocal = getPddaMessageMaintainer().findByPrimaryKey(pddaMessageId);
			result = getPddaMessageMaintainer().getBasicValue(pdmLocal);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;
	}
	
	public RefPddaMessageTypeBasicValue findByMessageType(String messageType) {
		methodName = "findByMessageType()";
		LOG.debug(methodName + " called");
		try {
			RefPddaMessageType refMessageType = getRefPddaMessageTypeMaintainer().findByMessageType(messageType);
			RefPddaMessageTypeBasicValue messageTypeBasicValue = getRefPddaMessageTypeMaintainer().getBasicValue(refMessageType);
			return messageTypeBasicValue;
		} catch (FinderException ex) {
			return null;	
		}
	}
	
	/**
	 * Find by primary key
	 * 
	 * @param messageTypeId
	 * @return
	 */
	public RefPddaMessageTypeBasicValue findByMessageTypeId(Integer messageTypeId) {
		methodName = "findByMessageTypeId()";
		LOG.debug(methodName + " called");
		try {
			RefPddaMessageType refMessageType = getRefPddaMessageTypeMaintainer().findByPrimaryKey(messageTypeId);
			RefPddaMessageTypeBasicValue messageTypeBasicValue = getRefPddaMessageTypeMaintainer().getBasicValue(refMessageType);
			return messageTypeBasicValue;
		} catch (FinderException ex) {
			return null;	
		}
	}
	
	public List<PddaMessageComplexValue> findComplexByBatchId(final Integer pddaBatchId) throws FinderException {
		methodName = "findComplexByBatchId()";
		LOG.debug(methodName + " called");
		List<PddaMessageComplexValue> result = new ArrayList<PddaMessageComplexValue>();
		try {
			Collection<PddaMessage> pdmLocals = getPddaMessageMaintainer().findByBatchId(pddaBatchId);
			if (pdmLocals != null) {
				for (PddaMessage pdmLocal : pdmLocals) {
					PddaMessageComplexValue complexValue = getComplexValue(pdmLocal);
					result.add(complexValue);
				}
			}
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;
	}
	
	private PddaMessageComplexValue getComplexValue(PddaMessage local) throws FinderException {
		methodName = "getComplexValue(local (not shown))";
		LOG.debug(methodName + " called");
		
		PddaMessageComplexValue complexValue = getPddaMessageMaintainer().getComplexValue(local);
		return complexValue;
	}
		
	public PddaMessageBasicValue createPddaMessage(PddaMessageBasicValue basicValue, String userDisplayName) throws FinderException {
		PddaMessage msgLocal = (PddaMessage) getPddaMessageMaintainer().create(basicValue, userDisplayName);
		return getPddaMessageMaintainer().getBasicValue(msgLocal);
	}
	
	public RefPddaMessageTypeBasicValue createPddaMessageType(RefPddaMessageTypeBasicValue basicValue, String userDisplayName) throws FinderException {
		RefPddaMessageType msgTypeLocal = (RefPddaMessageType) getRefPddaMessageTypeMaintainer().create(basicValue, userDisplayName);
		return getRefPddaMessageTypeMaintainer().getBasicValue(msgTypeLocal);
	}
	
	private PddaMessageMaintainer getPddaMessageMaintainer() {
		if (pddaMessageMaintainer == null) {
			pddaMessageMaintainer = new PddaMessageMaintainer();
		}
		return pddaMessageMaintainer;
	}
	
	private RefPddaMessageTypeMaintainer getRefPddaMessageTypeMaintainer() {
		if (refPddaMessageTypeMaintainer == null) {
			refPddaMessageTypeMaintainer = new RefPddaMessageTypeMaintainer();
		}
		return refPddaMessageTypeMaintainer;
	}
}