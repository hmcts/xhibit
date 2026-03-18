package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */
public class WitnessReadModel extends FreeTextModel {
    // log directly from the class
    private static final Logger log = Logger.getLogger(WitnessReadModel.class);

    private int subEventId;

    private int subSubEventId;

    private String subEventCode;

    private String subSubEventCode;

    private String witnessName;

    private String witnessId;

    private Vector witNumbers = new Vector();

    public WitnessReadModel() {
        super();
    }

    // Getters
    public int getSubEventId() {
        return subEventId;
    }

    public String getSubEventCode() {
        return subEventCode;
    }

    public int getSubSubEventId() {
        return subSubEventId;
    }

    public String getSubSubEventCode() {
        return subSubEventCode;
    }

    public String getWitnessName() {
        return witnessName;
    }

    public Vector getWitNumbers() {
        return witNumbers;
    }

    public String getWitnessId() {
        return witnessId;
    }

    // Setters
    public void setSubEventId(int id) {
        subEventId = id;
    }

    public void setSubSubEventId(int id) {
        subSubEventId = id;
    }

    public void setWitnessName(String name) {
        witnessName = name;
    }

    public void setSubEventCode(String param) {
        subEventCode = param;
    }

    public void setSubSubEventCode(String param) {
        subSubEventCode = param;
    }

    public void setWitNumbers(Vector v) {
        witNumbers = v;
    }

    public void setWitnessId(String id) {
        witnessId = id;
    }

    // toString ??
    public void printModel() {
        super.printModel();

        log.info("WitnessReadModel");
        log.info("-----------------");
        log.info("Sub Event ID      : " + getSubEventId());
        log.info("Sub Event Code    : " + getSubEventCode());
        log.info("Sub Sub Event ID  : " + getSubSubEventId());
        log.info("Sub Sub Event Code: " + getSubSubEventCode());
        log.info("Witness Name      : " + getWitnessName());
        log.info("Witness ID        : " + getWitnessId());
    }

    public void clearmodel() {
        super.clearmodel();

        setSubEventId(0);
        setSubSubEventId(0);
        setSubEventCode(null);
        setSubSubEventCode(null);
        setWitnessName(null);
        getWitNumbers().clear();
        setWitnessId(null);
    }
}
