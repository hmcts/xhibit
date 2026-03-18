package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;

import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;

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

public class ProsecutionAdvocateListModel extends DefaultListModel {
    private int selectedjusticeIndex;

    public ProsecutionAdvocateListModel() {
        this.setup();
    }

    public void setSelectedIndex(int index) {
        this.selectedjusticeIndex = index;
    }

    public void setProsecutionAdvocate(Vector vectorOfVectors) {
        this.clear();
        Iterator iterator = vectorOfVectors.iterator();
        while (iterator.hasNext()) {
            this.addProsecutionAdvocate(iterator.next());
        }
    }

    public void addProsecutionAdvocate(Object prosecutionAdvocateVector) {
        this.addElement(prosecutionAdvocateVector);
    }

    private Vector getProsecutionAdvocates() {
        Vector aVector = new Vector();
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements())
            aVector.add(enumeration.nextElement());
        return aVector;
    }

    private void setup() {

    }

    public Object getSelectedItem() {
        if ((this.selectedjusticeIndex > -1) && (this.selectedjusticeIndex < this.getProsecutionAdvocates().size())) {
            return this.elementAt(this.selectedjusticeIndex);
        } else {
            return null;
        }
    }

    public Object getElementAt(int index) {
        Vector a = (Vector) this.elementAt(index);
        return getFormattedName((RefLegalRepresentativeBasicValue) a.elementAt(0));
    }

    public void removeListDataListener(ListDataListener ldl) {
    }

    public void addListDataListener(ListDataListener ldl) {
    }

    public String getFormattedName(RefLegalRepresentativeBasicValue rjbv) {
        String object = "n/a";
        String title = rjbv.getTitle() == null ? "" : rjbv.getTitle().concat(" ");
        String initials = rjbv.getInitials() == null ? "" : rjbv.getInitials().concat(" ");
        String firstName = rjbv.getFirstName() == null ? "" : rjbv.getFirstName().concat(" ");
        String middleName = rjbv.getMiddleName() == null ? "" : rjbv.getMiddleName().concat(" ");
        String surName = rjbv.getSurname() == null ? "" : rjbv.getSurname();

        object = title.concat(initials).concat(firstName).concat(middleName).concat(surName);
        if ((object == null) || (object.trim().equals(""))) {
            object = "name not set";
        }
        return object;
    }
}