package uk.gov.courtservice.xhibit.business.entities.cpplist;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * Represents data in XHB_CPP_LIST.
 * @author harrism
 *
 */
public interface CppList extends CSEntityLocal {

	public Integer getCppListId();
	public void setCppListId(Integer cppListId);
	public Integer getCourtCode();
	public void setCourtCode(Integer courtCode);
	public String getListType();
	public void setListType(String listType);
	public Date getTimeLoaded();
	public void setTimeLoaded(Date timeLoaded);
	public Date getListStartDate();
	public void setListStartDate(Date listStartDate);
	public Date getListEndDate();
	public void setListEndDate(Date listEndDate);
	public Long getListClobId();
	public void setListClobId(Long listClobId);
	public Long getMergedClobId();
	public void setMergedClobId(Long mergedClobId);
	public String getStatus();
	public void setStatus(String status);
	public String getErrorMessage();
	public void setErrorMessage(String errorMessage);
	public String getObsInd();
	public void setObsInd(String obsInd);
	public String getLastUpdatedBy();
	public void setLastUpdatedBy(String lastUpdatedBy);
	public String getCreatedBy();
	public void setCreatedBy(String createdBy);
}