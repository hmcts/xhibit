package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: UnauthorisedCasesPrintValue
 * </p>
 * <p>
 * Description: For printing a report of unauthorised cases, this will be the highest level object to be 
 * marshalled to XML before it's transformed using XSL. It will hold a list of unauthorisedCases 
 * for different court sites.
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
public class UnauthorisedCasesPrintValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;
    
    private Vector unauthorisedCasesCourtValues;
    
    public UnauthorisedCasesPrintValue(){
        //Empty
    }

    public void setUnauthorisedCasesCourtValues(Vector unauthorisedCasesCourtValues) {
        this.unauthorisedCasesCourtValues = unauthorisedCasesCourtValues;
    }

    public Vector getUnauthorisedCasesCourtValues() {
        return unauthorisedCasesCourtValues;
    }

}
