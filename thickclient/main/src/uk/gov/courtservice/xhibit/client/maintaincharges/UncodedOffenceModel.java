package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.StringTokenizer;

import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: UncodedOffenceModel
 * </p>
 * <p>
 * Description: The Model Component of the uncoded offences
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UncodedOffenceModel.java,v 1.7 2006/06/05 12:31:29 bzjrnl Exp $
 * 
 */
public class UncodedOffenceModel implements UncodedOffenceInterface {

    /**
     * Reserved Code PK
     */
    public static Integer refOffenceId;

    /**
     * The Ref Offence Description
     */
    private String refOffenceDesc;

    /**
     * Formatted in CREST Format hoDesc*rsDesc
     */
    private String crestDesc;

    /**
     * Home Office Description
     */
    private String hoDesc;

    /**
     * Record Sheet Description
     */
    private String rsDesc;

    /**
     * Home Office Class
     */
    private String hoClass = new String(DEFAULT_CLASS);

    /**
     * Home Office Subclass
     */
    private String hoSubclass = new String(DEFAULT_SUBCLASS);

    /**
     * <init>
     */
    public UncodedOffenceModel() {
    }

    /**
     * <init>
     * 
     * @param crestDesc
     *            parameter for <init>
     */
    public UncodedOffenceModel(String crestDesc) {
        StringTokenizer tokenizer;
        if (crestDesc != null && crestDesc.indexOf("*") != -1) {
            tokenizer = new StringTokenizer(crestDesc, "*");

            if (tokenizer.countTokens() == 2) {
                this.hoDesc = tokenizer.nextToken(); // First token is home
                // office description
                this.rsDesc = tokenizer.nextToken(); // Second token
            } else if (tokenizer.countTokens() == 1) {
                if (crestDesc.startsWith("*")) {
                    this.rsDesc = tokenizer.nextToken();
                } else if (crestDesc.endsWith("*")) {
                    this.hoDesc = tokenizer.nextToken();
                }
            }
        }
        this.crestDesc = crestDesc;
    }

    /**
     * <init>
     * 
     * @param value
     *            parameter for <init>
     */
    public UncodedOffenceModel(OffenceValue value) {
        this(value.getCrestOffenceFreeText());
        setHoClass(value.getCrestHOClass());
        setHoSubclass(value.getCrestHOSubclass());
    }

    /**
     * <init>
     * 
     * @param hoDesc
     *            parameter for <init>
     * @param rsDesc
     *            parameter for <init>
     * @param hoClass
     *            parameter for <init>
     * @param hoSubclass
     *            parameter for <init>
     */
    public UncodedOffenceModel(String hoDesc, String rsDesc, String hoClass, String hoSubclass) {
        this.hoDesc = hoDesc;
        this.rsDesc = rsDesc;
        setHoClass(hoClass);
        setHoSubclass(hoSubclass);
    }

    /**
     * getRefOffenceDesc
     * 
     * @return the returned String
     */
    public String getRefOffenceDesc() {
        return refOffenceDesc;
    }

    /**
     * getHODesc
     * 
     * @return the returned String
     */
    public String getCrestDesc() {
        return new String(hoDesc + "*" + rsDesc);
    }

    /**
     * getHoClass
     * 
     * @return the returned String
     */
    public String getHoClass() {
        return hoClass;
    }

    /**
     * getHoDesc
     * 
     * @return the returned String
     */
    public String getHoDesc() {
        return hoDesc;
    }

    /**
     * getHoSubclass
     * 
     * @return the returned String
     */
    public String getHoSubclass() {
        return hoSubclass;
    }

    /**
     * getRsDesc
     * 
     * @return the returned String
     */
    public String getRsDesc() {
        return rsDesc;
    }

    /**
     * setHoClass
     * 
     * @param hoClass
     *            parameter for setHoClass
     */
    public void setHoClass(String hoClass) {
        this.hoClass = hoClass;
    }

    /**
     * setRefOffenceDesc
     * 
     * @param refOffenceDesc
     *            parameter for setRefOffenceDesc
     */
    public void setRefOffenceDesc(String refOffenceDesc) {
        this.refOffenceDesc = refOffenceDesc;
    }

    /**
     * setHoDesc
     * 
     * @param hoDesc
     *            parameter for setHoDesc
     */
    public void setHoDesc(String hoDesc) {
        this.hoDesc = hoDesc;
    }

    /**
     * setHoSubclass
     * 
     * @param hoSubclass
     *            parameter for setHoSubclass
     */
    public void setHoSubclass(String hoSubclass) {
        this.hoSubclass = hoSubclass;
    }

    /**
     * setRsDesc
     * 
     * @param rsDesc
     *            parameter for setRsDesc
     */
    public void setRsDesc(String rsDesc) {
        this.rsDesc = rsDesc;
    }
}
