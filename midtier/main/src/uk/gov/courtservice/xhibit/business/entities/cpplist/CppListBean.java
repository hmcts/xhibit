package uk.gov.courtservice.xhibit.business.entities.cpplist;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CppListBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer cppListId, Integer courtCode, String listType, Date timeLoaded,
			Date listStartDate, Date listEndDate, Long listClobId, Long mergedClobId,
			String status, String errorMessage, String userDisplayName) throws CreateException {
		setCppListId(cppListId);
		setCourtCode(courtCode);
		setListType(listType);
		setTimeLoaded(timeLoaded);
		setListStartDate(listStartDate);
		setListEndDate(listEndDate);
		setListClobId(listClobId);
		setMergedClobId(mergedClobId);
		setStatus(status);
		setErrorMessage(errorMessage);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer cppListId, Integer courtCode, String listType, Date timeLoaded,
			Date listStartDate, Date listEndDate, Long listClobId, Long mergedClobId,
			String status, String errorMessage, String userDisplayName) throws CreateException {
	}

	public abstract Integer getCppListId();
	public abstract void setCppListId(Integer cppListId);
	public abstract Integer getCourtCode();
	public abstract void setCourtCode(Integer courtCode);
	public abstract String getListType();
	public abstract void setListType(String listType);
	public abstract Date getTimeLoaded();
	public abstract void setTimeLoaded(Date timeLoaded);
	public abstract Date getListStartDate();
	public abstract void setListStartDate(Date listStartDate);
	public abstract Date getListEndDate();
	public abstract void setListEndDate(Date listEndDate);
	public abstract Long getListClobId();
	public abstract void setListClobId(Long listClobId);
	public abstract Long getMergedClobId();
	public abstract void setMergedClobId(Long mergedClobId);
	public abstract String getStatus();
	public abstract void setStatus(String status);
	public abstract String getErrorMessage();
	public abstract void setErrorMessage(String errorMessage);
	public abstract String getObsInd();
	public abstract void setObsInd(String obsInd);
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy);

}