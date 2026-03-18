package uk.gov.courtservice.xhibit.client.order.screens.resource;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Temporary Resource
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
public class Resource extends java.util.ListResourceBundle {
    static final Object[][] contents = new String[][] { { "CaseIDLabel", "Case ID:" },
            { "CourtCodeLabel", "Court Code:" }, { "CourtNameLabel", "Court:" },
            { "DefendantLabel", "Defendant Name:" }, { "OrderTypeLabel", "OrderType:" },
            { "BackButtonLabel", "<Back" }, { "NextButtonLabel", "Next>" }, { "FinishButtonLabel", "Finish" },
            { "CancelButtonLabel", "Cancel" }, { "SummaryTitle", "Order Summary" },
            { "DefendantPanelTitle", "Select Defendant From List" }, { "DefendantLabel", "Defendant:" },
            { "OrderTypePanelTitle", "Select Order Type From List" }, { "OrderTypeLabel", "Order Types:" },
            { "DisposalPanelTitle", "Select Disposal(s) From List" }, { "DisposalLabel", "Disposals:" },
            { "OrderExistsPanelTitle", "Order Exists" }, { "OrderExistsMessage1", "An order of type" },
            { "OrderExistsMessage2", "exists and is" },
            { "OrderExistsMessage3", "Select an option below to proceed:" },
            { "OrderExistsCreateNew", "Create a new order of this type" },
            { "OrderExistsListOrders", "View existing order(s)" }, { "OrderSignedTitle", "Order Signature Details" },
            { "OrderSignedDate", "Date Signed:" }, { "OrderSignedTime", "Time Signed:" },
            { "OrderSignedBy", "Signed By:" }, { "OrderSavedTitle", "Order Saved Details" },
            { "OrderSavedDescription", "Order Description:" }, { "OrderListTitle", "Existing Orders" },
            { "OrderListOption", "Create new order" } };

    /**
     * 
     * @return
     */
    public Object[][] getContents() {
        return contents;
    }
}