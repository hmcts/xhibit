package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
/**
 * <p>
 * Title: UnauthorisedCasePrintValue
 * </p>
 * <p>
 * Description: This class is used to represent an Unauthorised Case to be marshalled to XML ready to be 
 * printed using XSLT / FOP 
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

public class UnauthorisedCasePrintValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;
    
    private String caseTypeAndNumber;
    private String courtRoom;
    private String conclusionDate; 
    
    private Vector defendants;
    
    public UnauthorisedCasePrintValue(){
        //Empty
    }

    public void setCaseTypeAndNumber(String caseTypeAndNumber) {
        this.caseTypeAndNumber = caseTypeAndNumber;
    }

    public String getCaseTypeAndNumber() {
        return caseTypeAndNumber;
    }

    public void setCourtRoom(String courtRoom) {
        this.courtRoom = courtRoom;
    }

    public String getCourtRoom() {
        return courtRoom;
    }

    public void setConclusionDate(String conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public String getConclusionDate() {
        return conclusionDate;
    }

    public void setDefendants(Vector defendants) {
        this.defendants = defendants;
    }

    public Vector getDefendants() {
        return defendants;
    }
}
