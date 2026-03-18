package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Vector;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: UnauthorisedCasesCourtPrintValue
 * </p>
 * <p>
 * Description: This class is used to represent all unauthorised cases at a particular court in
 * order to be printed using XSL / FOP 
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
public class UnauthorisedCasesCourtPrintValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;
    
    private String courtName;
    private String courtId;
    private Vector unauthorisedCases;
    
    public UnauthorisedCasesCourtPrintValue(){
        //Empty        
    }
    
    public void setUnauthorisedCases(Vector unauthorisedCases) {
        this.unauthorisedCases = unauthorisedCases;
    }

    public Vector getUnauthorisedCases() {
        return unauthorisedCases;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    public String getCourtId() {
        return courtId;
    }

    
    
    
    
}
