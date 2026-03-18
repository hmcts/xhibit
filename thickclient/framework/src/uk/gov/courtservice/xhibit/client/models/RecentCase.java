package uk.gov.courtservice.xhibit.client.models;

import java.io.Serializable;

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
 * @author unascribed
 * @version 1.0
 */

public class RecentCase implements Serializable {
    private String caseNumber;

    private boolean editMode;

    public RecentCase(ApplicationCaseModel model) {
        caseNumber = model.getDisplayCaseNumber();
        editMode = model.isInEditMode();
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public boolean isReadOnly() {
        return !editMode;
    }
}