package uk.gov.courtservice.xhibit.client.courtlog;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class Juror {

    private String name;

    private String jurorId;

    public Juror(String n, String j) {

        name = n;
        jurorId = j;
    }

    public String getName() {
        return name;
    }

    public String getJurorId() {
        return jurorId;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setJurorId(String newJurorId) {
        jurorId = newJurorId;
    }

    public void printModel() {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Juror Name      : " + getName());
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Juror Id        : " + getJurorId());
    }

    public Object clone() {
        Juror copy = new Juror(null, null);
        copy.setName(this.getName());
        copy.setJurorId(this.getJurorId());
        return copy;
    }

    public void clearmodel() {
        setName(null);
        setJurorId(null);
    }
}
