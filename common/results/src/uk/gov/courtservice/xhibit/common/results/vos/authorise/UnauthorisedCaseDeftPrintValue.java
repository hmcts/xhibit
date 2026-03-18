package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;


/**
 * <p>
 * Title: UnauthorisedCaseDeftPrintValue
 * </p>
 * <p>
 * Description: This class is used to represent a defendant on an unauthorised case for the 
 * purpose of printing vla XSL / FOP. It will hold defendant level details such as
 * whether results have been exported and the export_date.
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
public class UnauthorisedCaseDeftPrintValue extends CSAbstractValue{   
    private static final long serialVersionUID = 1L;
    
    private String deftName;
    private String deftResultsExported;
    private String dateExported;
    private String dateDiff;
    
    public UnauthorisedCaseDeftPrintValue(){
        //Empty
    }
    
    public void setDeftName(String deftName) {
        this.deftName = deftName;
    }
    public String getDeftName() {
        return deftName;
    }
    public void setDeftResultsExported(String deftResultsExported) {
        this.deftResultsExported = deftResultsExported;
    }
    public String getDeftResultsExported() {
        return deftResultsExported;
    }
    public void setDateExported(String dateExported) {
        this.dateExported = dateExported;
    }
    public String getDateExported() {
        return dateExported;
    }

    public void setDateDiff(String dateDiff) {
        this.dateDiff = dateDiff;
    }

    public String getDateDiff() {
        return dateDiff;
    }
    
    
}
