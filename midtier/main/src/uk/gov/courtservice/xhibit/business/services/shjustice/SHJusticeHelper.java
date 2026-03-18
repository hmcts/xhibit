package uk.gov.courtservice.xhibit.business.services.shjustice;


import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJusticeMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataDatabaseManager;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;

/**
 * <p>
 * Title: SHJusticeHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Kudzin
 * @version 1.0
 */
public class SHJusticeHelper {
	private static final Logger log = CSServices.getLogger(SHJusticeHelper.class);
	private ShJusticeMaintainer shJusticeMaintainer;
	private SchedHearingAttendeeMaintainer shaMaintainer;
	private ScheduledHearingMaintainer shMaintainer;

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public SHJusticeHelper() {
		shJusticeMaintainer = new ShJusticeMaintainer();
		shMaintainer = new ScheduledHearingMaintainer();
		shaMaintainer = new SchedHearingAttendeeMaintainer();
	}

	/**
	 * This method updates the SHJustice
	 * 
	 * @param hearingRecordValue
	 *            a HearingRecordValue object
	 * @param userDisplayName
	 *            a String
	 * @throws SHJusticeException
	 *             custom exception
	 * @throws ObjectNotFoundException
	 * @throws SQLException 
	 */
	public void update(HearingRecordValue hearingRecordValue, String userDisplayName, Integer scheduledHearingId)
			throws SHJusticeException, ObjectNotFoundException, IllegalArgumentException, EJBException, OptimisticLockException, SQLException {
		log.debug("Enter method update SHJustice");
		Collection shJus = hearingRecordValue.getHearingRecordDisplayValue().getHrHearingDisplayValue()
				.getHrJusticeValues();
			Iterator it = shJus.iterator();

			while (it.hasNext()) {
				SHJusticeBasicValue justice = (SHJusticeBasicValue) it.next();
				if (justice != null) {
					if (justice.getId() == null) {
						ShJustice shJustice = (ShJustice)shJusticeMaintainer.create(justice, userDisplayName);
						//insert an entry into the XHB_SCHED_HEARING_ATTENDEE
						SchedHearingAttendeeBasicValue shaValue = new SchedHearingAttendeeBasicValue();
						shaValue.setAttendeeType("JP");
			            ScheduledHearing sh = shMaintainer.findByPK(scheduledHearingId);
			            SchedHearingAttendee schedHearingAttendee = (SchedHearingAttendee)shaMaintainer.create(shaValue, sh, userDisplayName);
			            schedHearingAttendee.setShJustice(shJustice);
	
					} else {
						// if empty, delete - ctx-3666
						if (justice.getJusticeName() == null || justice.getJusticeName().isEmpty()) {
							ReferenceDataDatabaseManager dbMan = new ReferenceDataDatabaseManager();
							dbMan.deleteShJustice(justice.getId());
						} else {
							shJusticeMaintainer.update(justice, userDisplayName);
						}
					}
				}
			}
		log.debug("Exit method update SHJustice");
	}
	
	/**
	 * This method retrieves SHjustices by hearingId
	 * 
	 * @param hearingId
	 *            integer
	 * @throws FinderException
	 *             finder exception
	 */
	public Collection findByHearingId(Integer hearingId) throws FinderException {
		log.debug("Enter method findByHearingId");

		ShJusticeMaintainer shJusticeMaintainer = new ShJusticeMaintainer();
		return shJusticeMaintainer.findByHearingId(hearingId);
	}

}
