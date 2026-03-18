package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class JudgeListModel extends DefaultListModel {
    private Vector judges;

    private int selectedJudgeIndex;

    public JudgeListModel() {
        this.setup();
    }

    public void setJudges(Vector v) {
        this.judges = v;
    }

    public void addJudge(Object judge) {
        this.judges.add(judge);
        this.fireContentsChanged(this, 0, this.judges.size());
        XHIBITConstant.debug("JudgeListModel.fireContentsChanged(this, 0, " + this.judges.size() + ");");
    }

    public Vector getJudges() {
        return this.judges;
    }

    private void setup() {
        judges = new Vector(10);
    }

    public Object getSelectedItem() {
        return this.getJudges().elementAt(this.selectedJudgeIndex);
    }

    public Object getElementAt(int index) {
        String object = "n/a";
        // RefJudgeBasicValue pv =
        // (RefJudgeBasicValue)this.getJudges().elementAt(index);
        PersonValue pv = (PersonValue) this.getJudges().elementAt(index);

        object = pv.getFullName();

        if (object.trim() == "") {
            object = "Unnamed Defendant";
        }

        return object;
    }

    public int getSize() {
        return this.getJudges().size();
    }

    public void setSelectedItem(Object o) {
        this.selectedJudgeIndex = this.getJudges().indexOf(o);
    }

    public void removeListDataListener(ListDataListener ldl) {
    }

    public void addListDataListener(ListDataListener ldl) {
    }
}