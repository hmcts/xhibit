package uk.gov.courtservice.xhibit.client.publicnotice;

import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;

/**
 * <p>
 * Title: Model for PublicNoticesPanel
 * </p>
 * <p>
 * Description: This model contains information on each Public Notice which is
 * displayed on the panel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 * 
 */

public class PublicNoticesModel {
    /**
     * DisplayablePublicNoticeValue[] publicNoticesVal
     */
    private DisplayablePublicNoticeValue[] publicNoticesVal;

    /**
     * getPublicNoticesVal
     * 
     * @return the returned DisplayablePublicNoticeValue[]
     */
    public DisplayablePublicNoticeValue[] getPublicNoticesVal() {
        return publicNoticesVal;
    }

    /**
     * setPublicNoticesVal
     * 
     * @param x
     *            parameter for setPublicNoticesVal
     */
    public void setPublicNoticesVal(DisplayablePublicNoticeValue[] x) {
        publicNoticesVal = x;
    }
}