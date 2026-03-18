package uk.gov.courtservice.xhibit.client.counselfacilities;

/**
 * <p>
 * Title: FindLegalRepresentativeTableRowModel
 * </p>
 * <p>
 * Description: The row model for finding a legal representative
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class AddInstructedAdvocateModel {

    private FindInstructedAdvocateTableRowModel trm;
    
    AddInstructedAdvocateModel() {
        // Empty
    }
    
    public FindInstructedAdvocateTableRowModel getFindInstructedAdvocateTableRowModel() {
        return trm;
    }
    
    public void setFindInstructedAdvocateTableRowModel(FindInstructedAdvocateTableRowModel param) {
        trm = param;
    }
}
