package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: WLLRecipientBasicValue
 * </p>
 * <p>
 * Description: Warned List Letter Recipient Value Object. Maps one to one with
 * WLLRecipient entity.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Brett Williams, Laurent Bossard
 * @version 1.0
 */

public class WLLRecipientBasicValue extends CSAbstractValue {
    private Integer crestSolicitorFirmID;

    private String solicitorFirmName;

    private String solicitorFirmAddress;

    private String solicitorFirmFax;

    private String solicitorFirmEmail;

    private Integer courtID;

    private Integer wllRecipientId;
    
    private static final long serialVersionUID =6968138035918143939L;

    /**
     * Default Constructors
     */
    public WLLRecipientBasicValue() {
    }

    public WLLRecipientBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * @return Integer
     */
    public Integer getWllRecipientId() {
        return wllRecipientId;
    }

    /**
     * @return Integer
     */
    public Integer getCrestSolicitorFirmID() {
        return crestSolicitorFirmID;
    }

    /**
     * @return String
     */
    public String getSolicitorFirmAddress() {
        return solicitorFirmAddress;
    }

    /**
     * @return String
     */
    public String getSolicitorFirmEmail() {
        return solicitorFirmEmail;
    }

    /**
     * @return String
     */
    public String getSolicitorFirmFax() {
        return solicitorFirmFax;
    }

    /**
     * @return String
     */
    public String getSolicitorFirmName() {
        return solicitorFirmName;
    }

    /**
     * @return Integer
     */
    public Integer getCourtID() {
        return courtID;
    }

    /**
     * @param Integer
     *            courtID
     */
    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    /**
     * @param Integer
     *            crestSolicitorFirmID
     */
    public void setCrestSolicitorFirmID(Integer crestSolicitorFirmID) {
        this.crestSolicitorFirmID = crestSolicitorFirmID;
    }

    /**
     * @param String
     *            solicitorFirmAddress
     */
    public void setSolicitorFirmAddress(String solicitorFirmAddress) {
        this.solicitorFirmAddress = solicitorFirmAddress;
    }

    /**
     * @param String
     *            solicitorFirmEmail
     */
    public void setSolicitorFirmEmail(String solicitorFirmEmail) {
        this.solicitorFirmEmail = solicitorFirmEmail;
    }

    /**
     * @param String
     *            solicitorFirmFax
     */
    public void setSolicitorFirmFax(String solicitorFirmFax) {
        this.solicitorFirmFax = solicitorFirmFax;
    }

    /**
     * @param String
     *            solicitorFirmName
     */
    public void setSolicitorFirmName(String solicitorFirmName) {
        this.solicitorFirmName = solicitorFirmName;
    }

    /**
     * @param Integer
     */
    public void setWllRecipientId(Integer wllRecipientId) {
        this.wllRecipientId = wllRecipientId;
    }
}