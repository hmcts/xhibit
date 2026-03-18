package uk.gov.courtservice.xhibit.business.services.pdda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.pdda.PddaBatch;
import uk.gov.courtservice.xhibit.business.entities.pdda.PddaBatchMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.PddaBatchBasicValue;

/**
 * <p>
 * Title: PDDABatchHelper
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
public class PddaBatchHelper {
	private static final Logger LOG = CSServices.getLogger(PddaBatchHelper.class);
	
	private String methodName;
	private PddaBatchMaintainer pddaBatchMaintainer;
	
	public List<PddaBatchBasicValue> findOpenBatch(Integer readyToSendId) throws FinderException {
		methodName = "findReadyToSendBatches()";
		LOG.debug(methodName + " called");
		List<PddaBatchBasicValue> result = new ArrayList<PddaBatchBasicValue>();
		try {
			Collection<PddaBatch> locals = getPddaBatchMaintainer().findOpenBatch(readyToSendId);
			if (locals != null) {
				for (PddaBatch local : locals) {
					result.add(getPddaBatchMaintainer().getBasicValue(local));		
				}
			}
			return result;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
	
	public List<PddaBatchBasicValue> findReadyToSendBatches(Integer readyToSendId) throws FinderException {
		methodName = "findReadyToSendBatches()";
		LOG.debug(methodName + " called");
		List<PddaBatchBasicValue> result = new ArrayList<PddaBatchBasicValue>();
		try {
			Collection<PddaBatch> locals = getPddaBatchMaintainer().findReadyToSendBatches(readyToSendId);
			if (locals != null) {
				for (PddaBatch local : locals) {
					result.add(getPddaBatchMaintainer().getBasicValue(local));		
				}
			}
			return result;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
	
	public List<PddaBatchBasicValue> findBatchesToResend(Integer maxResends, Integer batchFailureId) throws FinderException {
		methodName = "findBatchesToResend()";
		LOG.debug(methodName + " called");
		List<PddaBatchBasicValue> result = new ArrayList<PddaBatchBasicValue>();
		try {
			Collection<PddaBatch> locals = getPddaBatchMaintainer().findBatchesToResend(maxResends, batchFailureId);
			if (locals != null) {
				for (PddaBatch local : locals) {
					result.add(getPddaBatchMaintainer().getBasicValue(local));		
				}
			}
			return result;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
	
	public PddaBatchBasicValue createBatch(PddaBatchBasicValue basicValue, String userDisplayName) throws FinderException {
		PddaBatch batchLocal = (PddaBatch) getPddaBatchMaintainer().create(basicValue, userDisplayName);
		return getPddaBatchMaintainer().getBasicValue(batchLocal);
	}
	
	public void updateBatch(PddaBatchBasicValue basicValue, String userDisplayName) throws FinderException {
		getPddaBatchMaintainer().update(basicValue, userDisplayName);
	}
	
	private PddaBatchMaintainer getPddaBatchMaintainer() {
		if (pddaBatchMaintainer == null) {
			pddaBatchMaintainer = new PddaBatchMaintainer();
		}
		return pddaBatchMaintainer;
	}
}