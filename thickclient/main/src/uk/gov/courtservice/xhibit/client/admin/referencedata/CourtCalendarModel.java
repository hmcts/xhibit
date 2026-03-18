package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * CourtCalendarModel class.
 * 
 * @author grewalg
 *
 */
public class CourtCalendarModel implements Cloneable {
    // log directly from method rather than through XHIBITConstants

    private Calendar fromDate;
    
    private Calendar toDate;
    
    private boolean fromDateChanged;
    
    private boolean toDateChanged;
    
    private boolean dateRangeInvalid;

    private XhibitApplicationController xac;

    public CourtCalendarModel() {
        // empty
    }

    public Calendar getFromDate() {
		return fromDate;
	}

	public void setFromDate(Calendar fromDate) {
		this.fromDate = fromDate;
	}

	public Calendar getToDate() {
		return toDate;
	}

	public void setToDate(Calendar toDate) {
		this.toDate = toDate;
	}

	public boolean isFromDateChanged() {
		return fromDateChanged;
	}

	public void setFromDateChanged(boolean fromDateChanged) {
		this.fromDateChanged = fromDateChanged;
	}

	public boolean isToDateChanged() {
		return toDateChanged;
	}

	public void setToDateChanged(boolean toDateChanged) {
		this.toDateChanged = toDateChanged;
	}
	
	

	public boolean isDateRangeInvalid() {
		return dateRangeInvalid;
	}

	public void setDateRangeInvalid(boolean dateRangeInvalid) {
		this.dateRangeInvalid = dateRangeInvalid;
	}

	public XhibitApplicationController getXac() {
        return xac;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {

        setXac(null);

    }
}
