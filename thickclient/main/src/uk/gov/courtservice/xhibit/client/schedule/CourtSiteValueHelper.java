package uk.gov.courtservice.xhibit.client.schedule;

//import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CourtSiteValueHelper {
    private XhbCourtSiteBasicValue model;

    public CourtSiteValueHelper(XhbCourtSiteBasicValue obj) {
        setModel(obj);
    }

    public String toString() {
        return (getModel().getCourtSiteName() == null ? "" : getModel().getCourtSiteName());
    }

    public void setModel(XhbCourtSiteBasicValue model) {
        this.model = model;
    }

    public XhbCourtSiteBasicValue getModel() {
        return model;
    }
}