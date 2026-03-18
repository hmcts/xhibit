package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: UnauthorisedCaseValue
 * </p>
 * <p>
 * Description: A Value Object which contains the Case ID of a case returned from the
 * stored procedure as 'Uanuthorised'
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

public class UnauthorisedCaseValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;
    
    private Integer case_id;

    public UnauthorisedCaseValue(){
        super();
    }
    
    public void setCase_id(Integer case_id) {
        this.case_id = case_id;
    }

    public Integer getCase_id() {
        return case_id;
    }
    
    
}
