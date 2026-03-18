package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class PrintChargesModel extends ApplicationCaseModel {
    public static final int PRINT_CHARGES = 1;

    public static final int PRINT_CHARGES_AND_LOG = 2;

    private int printSelection = -1;

    public PrintChargesModel() {
    }

    public int getPrintSelection() {
        return printSelection;
    }

    public void setPrintSelection(int printSelection) {
        this.printSelection = printSelection;
    }

}