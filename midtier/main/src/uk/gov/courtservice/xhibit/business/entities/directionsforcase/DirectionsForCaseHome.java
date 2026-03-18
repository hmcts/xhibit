package uk.gov.courtservice.xhibit.business.entities.directionsforcase;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DirectionsForCaseHome extends javax.ejb.EJBLocalHome
{
	public DirectionsForCase create(
			String freetext, Date dateTime,
			Date listDate, String listType,
			String listedAs, String directionsText,
			Integer trialTimeUnit, Float trialTimeEstimate,
			String hasPanddForm, Integer caseId, String userDisplayName)
			throws CreateException;

	public DirectionsForCase findByPrimaryKey(Integer pk) 
			throws FinderException;
	
	public DirectionsForCase findByCaseId(Integer caseId) 
			throws FinderException;
}
