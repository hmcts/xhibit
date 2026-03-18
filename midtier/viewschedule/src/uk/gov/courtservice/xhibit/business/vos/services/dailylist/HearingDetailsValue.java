package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.Date;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: HearingDetailsValue.java,v 1.4 2006/06/05 12:30:00 bzjrnl Exp $
 */
public class HearingDetailsValue {
    private String hearingType;

    private String hearingDesc;

    private Date hearingDate;

    public HearingDetailsValue() {
    }

    public HearingDetailsValue(String hearingType, String hearingDesc, Date hearingDate) {
        this.hearingType = hearingType;
        this.hearingDesc = hearingDesc;
        this.hearingDate = hearingDate;
    }

    public Date getHearingDate() {
        return hearingDate;
    }

    public String getHearingDesc() {
        return hearingDesc;
    }

    public String getHearingType() {
        return hearingType;
    }
}
