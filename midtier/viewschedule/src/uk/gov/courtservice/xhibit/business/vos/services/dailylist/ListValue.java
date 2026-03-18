package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: ListValue
 * </p>
 * <p>
 * Description: Value to hold data about a daily list. This is more meta data
 * about the list itself.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class ListValue extends CSAbstractValue {
    private String listCategory;

    private Date startDate;

    private String listVersion;

    private String printRef;

    private Date publishedTime;

    private Integer crestListId;
    private static final long serialVersionUID =5612106987066809361L;
    

    public ListValue() {
    }

    public Integer getCrestListId() {
        return crestListId;
    }

    public void setCrestListId(Integer crestListId) {
        this.crestListId = crestListId;
    }

    public void setPrintRef(String printRef) {
        this.printRef = printRef;
    }

    public void setListCategory(String listCategory) {
        this.listCategory = listCategory;
    }

    public String getListCategory() {
        return listCategory;
    }

    public String getPrintRef() {
        return printRef;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getListVersion() {
        return listVersion;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setListVersion(String listVersion) {
        this.listVersion = listVersion;
    }

    public Integer getHearingListId() {
        return getId();
    }

    public void setHearingListId(Integer hearingListId) {
        setId(hearingListId);
    }
}