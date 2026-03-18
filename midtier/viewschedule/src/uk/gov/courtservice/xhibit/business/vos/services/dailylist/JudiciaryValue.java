package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_judge.XhbRefJudgeBasicValue;

/**
 * <p>
 * Title: JudiciaryValue
 * </p>
 * <p>
 * Description: Value object that holds judge and justices.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author: Marie Holmberg
 * @version 1.0
 */

public class JudiciaryValue extends CSAbstractValue {

    /**
     * The judge for the sitting
     */
    private XhbRefJudgeBasicValue xhbRefJudgeBasicValue;
    private static final long serialVersionUID =-5829712232412800355L;
    

    /**
     * The list of justices for the sitting. Not more than 4.
     */
    private ArrayList justiceList;

    public JudiciaryValue() {
    }

    public XhbRefJudgeBasicValue getJudgeValue() {
        return xhbRefJudgeBasicValue;
    }

    public ArrayList getJusticeList() {
        if (justiceList == null) {
            justiceList = new ArrayList();
        }
        return justiceList;
    }

    public void setJudgeValue(XhbRefJudgeBasicValue xhbRefJudgeBasicValue) {
        this.xhbRefJudgeBasicValue = xhbRefJudgeBasicValue;
    }

    public void setJusticeList(ArrayList justiceList) {
        this.justiceList = justiceList;
    }
}