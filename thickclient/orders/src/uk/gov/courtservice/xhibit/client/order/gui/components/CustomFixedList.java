package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JList;

/**
 * <p>
 * Title: CustomFixedList
 * </p>
 * <p>
 * Description: A custom List class that contains related case numbers
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */

public class CustomFixedList extends JList {
    private int caseCount;

    public CustomFixedList(Vector cases) {
        super(cases);
        this.caseCount = cases.size();
        this.setSelectionBackground(Color.blue); // SCR 53248
    }

    public CustomFixedList(Object[] cases) {
        super(cases);
        this.caseCount = cases.length;
    }

    public int getCaseCount() {
        return this.caseCount;
    }

}
