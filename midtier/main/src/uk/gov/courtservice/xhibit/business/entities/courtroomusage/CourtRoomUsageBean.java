package uk.gov.courtservice.xhibit.business.entities.courtroomusage;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

public abstract class CourtRoomUsageBean extends uk.gov.courtservice.framework.business.entities.CSEntityBean
		implements EntityBean {

	private static final long serialVersionUID = 3625907274476809727L;

	public Integer ejbCreate(Integer courtRoomId, Integer amHours, Integer amMins,
			Integer pmHours, Integer pmMins, Date sittingDate, String userDisplayName) throws CreateException {

		setAmTimeHours(amHours);
		setAmTimeMins(amMins);
		setPmTimeHours(pmHours);
		setPmTimeMins(pmMins);

		setCourtRoomId(courtRoomId);
		setSittingDate(sittingDate);
		setLastUpdatedBy(userDisplayName);
		setCreatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer courtRoomId, Integer amHours, Integer amMins,
			Integer pmHours, Integer pmMins, Date sittingDate, String userDisplayName) throws CreateException {
		/** @todo Complete this method */
	}

	public abstract Integer getCourtRoomUsageId();

	public abstract void setCourtRoomUsageId(Integer Id);

	public abstract Integer getCourtRoomId();

	public abstract void setCourtRoomId(Integer Id);

	public abstract Integer getAmTimeHours();

	public abstract void setAmTimeHours(Integer hours);

	public abstract Integer getAmTimeMins();

	public abstract void setAmTimeMins(Integer mins);

	public abstract Integer getPmTimeHours();

	public abstract void setPmTimeHours(Integer hours);

	public abstract Integer getPmTimeMins();

	public abstract void setPmTimeMins(Integer mins);

	public abstract java.util.Date getSittingDate();

	public abstract void setSittingDate(Date sittingDate);

	public abstract String getCreatedBy();

	public abstract void setCreatedBy(String user);

	public abstract Date getCreationDate();

	public abstract void setCreationDate(Date creationDate);

	public abstract String getLastUpdatedBy();

	public abstract void setLastUpdatedBy(String user);

	public abstract Date getLastUpdateDate();

	public abstract void setLastUpdateDate(Date updatedDate);

	public abstract Integer getVersion();

	public abstract void setVersion(Integer version);

	public abstract String getObsInd();

	public abstract void setObsInd(String obsInd);
}