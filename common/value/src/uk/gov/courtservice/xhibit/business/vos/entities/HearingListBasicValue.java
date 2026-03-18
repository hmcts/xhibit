package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: HearingListBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * HearingList enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 * 
 * <Change History/>
 * 
 * <P>
 * 27/02/03 - JB - Deprecated getListId. Removed setListId
 * </P>
 * 
 */

public class HearingListBasicValue extends CSAbstractValue {
    
	private static final long serialVersionUID = 8772760369503655022L;
	
	private Integer editionNo;

    private Integer crestListId;

    private Integer courtId;

    private String listType;

    private String status;

    private String printReference;

    private String listCourtType;

    private Date startDate;

    private Date endDate;

    private Date publishedTime;

    public HearingListBasicValue() {
        super();
    }

    public HearingListBasicValue(Integer version) {
        super(version);
    }

    public HearingListBasicValue(Integer listID, Integer version) {
        super(listID, version);
    }

    public void setEditionNo(Integer editionNo) {
        this.editionNo = editionNo;
    }

    public Integer getEditionNo() {
        return editionNo;
    }

    public void setCrestListId(Integer crestListId) {
        this.crestListId = crestListId;
    }

    public Integer getCrestListId() {
        return crestListId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getListType() {
        return listType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPrintReference(String printReference) {
        this.printReference = printReference;
    }

    public String getPrintReference() {
        return printReference;
    }

    public void setListCourtType(String listCourtType) {
        this.listCourtType = listCourtType;
    }

    public String getListCourtType() {
        return listCourtType;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }
}