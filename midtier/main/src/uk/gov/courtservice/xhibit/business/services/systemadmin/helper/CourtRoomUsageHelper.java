package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroomusage.CourtRoomUsage;
import uk.gov.courtservice.xhibit.business.entities.courtroomusage.CourtRoomUsageMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomUsageComplexValue;

public class CourtRoomUsageHelper extends AbstractHelper {

	private CourtRoomUsageMaintainer courtRoomUsageMaintainer = new CourtRoomUsageMaintainer();
	private CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
	
	
	public Collection<CourtRoomUsageComplexValue> findBySittingDateAndCourtRoom(final Integer courtRoomId,
			final Date sittingDate) throws SysRefControllerException {
		String METHOD_NAME = "findBySittingDateAndCourtRoom";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection<CourtRoomUsageComplexValue> complexValues = new ArrayList<CourtRoomUsageComplexValue>();
		try {

			Calendar endOfDay = DateTimeUtilities.convertToCalendar(sittingDate);
			endOfDay.set(Calendar.HOUR_OF_DAY, 23);
			endOfDay.set(Calendar.MINUTE, 59);
			endOfDay.set(Calendar.SECOND, 59);
			Collection<CourtRoomUsage> localValues = courtRoomUsageMaintainer.findBySittingDateAndCourtRoom(courtRoomId, sittingDate, endOfDay.getTime());

			for (CourtRoomUsage localValue : localValues) {
				final CourtRoomUsageComplexValue complexValue = courtRoomUsageMaintainer.getComplexValue(localValue);
				setComplexTypes(complexValue);
				complexValues.add(complexValue);
			}

		} catch (SysRefControllerException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException finExp) {
			CSServices.getDefaultErrorHandler().handleError(finExp, getClass(), finExp.toString());
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return complexValues;
	}
    
	
	public CourtRoomUsageComplexValue setComplexTypes(final CourtRoomUsageComplexValue complex) throws SysRefControllerException {
		String METHOD_NAME = "setComplexTypes";
		log.debug(METHOD_ENTER + METHOD_NAME);
		setCourtRoom(complex);
		log.debug(METHOD_EXIT + METHOD_NAME);
		return complex;
	}


	private void setCourtRoom(final CourtRoomUsageComplexValue complex) throws SysRefControllerException {
		String METHOD_NAME = "setCourtRoom";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (complex != null && complex.getCourtRoomId() != null) {

			CourtRoom courtRoom;
			try {
				courtRoom = courtRoomMaintainer.findByPrimaryKey(complex.getCourtRoomId());

				if (courtRoom != null) {
					complex.setCourtRoom(courtRoomMaintainer.getBasicValue(courtRoom));
				}
			} catch (ObjectNotFoundException e) {
				CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());

				throw new SysRefControllerException("JudgeUsage",
						"Object with key [" + complex.getCourtRoomId() + "] not found", e);
			}
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}
}
