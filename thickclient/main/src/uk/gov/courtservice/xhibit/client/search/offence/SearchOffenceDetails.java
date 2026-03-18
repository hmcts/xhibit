package uk.gov.courtservice.xhibit.client.search.offence;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public class SearchOffenceDetails extends XHIBITSearchDetails {

    public SearchOffenceDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefOffenceBasicValue();
    }

    public void setDetails() {
        JTextArea jt = new JTextArea();
        jt.setEditable(false);
        jt.setEnabled(false);
        jt.setDisabledTextColor(SystemColor.controlText);
        jt.setBackground(SystemColor.control);
        jt.setRequestFocusEnabled(false);
        jt.setLineWrap(true);
        jt.setWrapStyleWord(true);
        jt.setPreferredSize(new Dimension(400, 3 * XHIBITConstant.getLineHeight()));

        UIDefaults defaults = UIManager.getDefaults();
        Font f = defaults.getFont("OptionPane.font");
        jt.setFont(f);

        // addDetail( DetailAttributeName, XHIBITSearchResourceKey, optional
        // widget)
        addDetail("offenceCode", "offence.ResultDetailsCard.offenceCode");
        addDetail("offenceDesc", "offence.ResultDetailsCard.offenceDesc", jt);
        addDetail("offenceDesc2", "offence.ResultDetailsCard.offenceDesc2");
        addDetail("statute", "offence.ResultDetailsCard.statute");
        addDetail("section", "offence.ResultDetailsCard.section");
        addDetail("offenceClass", "offence.ResultDetailsCard.offenceClass");
        addDetail("dvlcCode", "offence.ResultDetailsCard.dvlcCode");
    }

    public String getStepTitleResourceKey() {
        return "offence.ResultDetailsCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "offence.ResultDetailsCard.description";
    }
}