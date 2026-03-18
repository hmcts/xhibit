package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.Vector;

import javax.swing.event.ListDataListener;

import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;

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

public class JudgeComboModel implements javax.swing.ComboBoxModel {
    private Vector judges = new Vector(10);

    private int selectedJudgeIndex;

    public JudgeComboModel() {
        this.setup();
    }

    public void setJudges(Vector v) {
        this.judges = v;
    }

    public void addJudge(Object judge) {
        this.judges.add(judge);

    }

    private Vector getJudges() {
        return this.judges;
    }

    private void setup() {

    }

    public Object getSelectedItem() {
        return this.getJudges().elementAt(this.selectedJudgeIndex);
    }

    public Object getElementAt(int index) {
        String object = "n/a";
        RefJudgeBasicValue pv = (RefJudgeBasicValue) this.getJudges().elementAt(index);

        object = pv.getSurname();

        if (object.trim().equals("")) {
            object = pv.getFirstName().concat(" ").concat(pv.getMiddleName()).concat(" ").concat(pv.getSurname());
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