package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class DefendantChargesModel extends FreeTextModel {

    private String defendantName;

    private String countNumber;

    private String indictmentNumber;

    public DefendantChargesModel() {
        super();
    }

    // Getters
    public String getDefendantName() {
        return defendantName;
    }

    public String getCountNumber() {
        return countNumber;
    }

    public String getIndictmentNumber() {
        return indictmentNumber;
    }

    // Setters
    public void setDefendantName(String defendantName) {
        this.defendantName = defendantName;
    }

    public void setCountNumber(String countNumber) {
        this.countNumber = countNumber;
    }

    public void setIndictmentNumber(String indictmentNumber) {
        this.indictmentNumber = indictmentNumber;
    }

    public void printModel() {
        super.printModel();

        XHIBITConstant.info("DefendantChargesModel");
        XHIBITConstant.info("---------------------");
        XHIBITConstant.info("Defendant Name   : " + getDefendantName());
        XHIBITConstant.info("Count Number     : " + getCountNumber());
        XHIBITConstant.info("Indictment Number: " + getIndictmentNumber());
    }

    public void clearmodel() {
        super.clearmodel();

        setDefendantName(null);
        setCountNumber(null);
        setIndictmentNumber(null);
    }
}
