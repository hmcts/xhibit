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
import uk.gov.courtservice.xhibit.business.entities.judgeusage.JudgeUsage;
import uk.gov.courtservice.xhibit.business.entities.judgeusage.JudgeUsageMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudge;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.JudgeUsageComplexValue;

public class JudgeUsageHelper extends AbstractHelper {

	private JudgeUsageMaintainer judgeUsagemaintainer = new JudgeUsageMaintainer();
	private CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
	private RefJudgeMaintainer judgeMaintainer = new RefJudgeMaintainer();
	
	
	public Collection<JudgeUsageComplexValue> findBySittingDateAndCourtRoom(final Integer courtRoomId,
			final Date sittingDate) throws SysRefControllerException {
		String METHOD_NAME = "findBySittingDateAndCourtRoom";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection<JudgeUsageComplexValue> complexValues = new ArrayList<JudgeUsageComplexValue>();
		try {

			Calendar endOfDay = DateTimeUtilities.convertToCalendar(sittingDate);
			endOfDay.set(Calendar.HOUR_OF_DAY, 23);
			endOfDay.set(Calendar.MINUTE, 59);
			endOfDay.set(Calendar.SECOND, 59);
			Collection<JudgeUsage> localValues = judgeUsagemaintainer.findBySittingDateAndCourtRoom(courtRoomId,
					sittingDate, endOfDay.getTime());

			for (JudgeUsage localValue : localValues) {
				final JudgeUsageComplexValue complexValue = judgeUsagemaintainer.getComplexValue(localValue);
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
    
	
	public JudgeUsageComplexValue setComplexTypes(final JudgeUsageComplexValue complex) throws SysRefControllerException {
		String METHOD_NAME = "setComplexTypes";
		log.debug(METHOD_ENTER + METHOD_NAME);
		setJudge(complex);
		setCourtRoom(complex);
		log.debug(METHOD_EXIT + METHOD_NAME);
		return complex;
	}

	private void setJudge(final JudgeUsageComplexValue complex) throws SysRefControllerException {
		String METHOD_NAME = "setJudge";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (complex != null && complex.getRefJudgeId() != null) {
			try {
				// Get the judge and set it on the object.
				final RefJudge judge = judgeMaintainer.findByPrimaryKey(complex.getRefJudgeId());
				judgeMaintainer.getBasicValue(judge);
				if (judge != null) {
					complex.setJudge(judgeMaintainer.getBasicValue(judge));
				}

			} catch (final ObjectNotFoundException e) {
				CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());

				throw new SysRefControllerException("JudgeUsage",
						"Object with key [" + complex.getJudgeUsageId() + "] not found", e);
			}
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	private void setCourtRoom(final JudgeUsageComplexValue complex) throws SysRefControllerException {
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
