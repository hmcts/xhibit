package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;

/**
 * <p>
 * Title: UnauthorisedCaseDeftValue
 * </p>
 * <p>
 * Description: This VO class is used to represent a defendant on case. It holds a copy of
 * XHBDefendant and other defendant related date which is needed to display Unauthorised Cases
 * to display the required data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class UnauthorisedCaseDeftValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;
    
    private XhbDefendantBasicValue defBasic;
    private Date exportDate;
    private String authoriseStatus
    ;
    public void setDefBasic(XhbDefendantBasicValue defBasic) {
        this.defBasic = defBasic;
    }
    public XhbDefendantBasicValue getDefBasic() {
        return defBasic;
    }
    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }
    public Date getExportDate() {
        return exportDate;
    }
    public void setAuthoriseStatus(String authoriseStatus) {
        this.authoriseStatus = authoriseStatus;
    }
    public String getAuthoriseStatus() {
        return authoriseStatus;
    }
    
    

}
