package uk.gov.courtservice.xhibit.client.order.gui.entry.d20offencecodes;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesBasicValue;

public interface D20OffenceCodeList {
    ArrayList<String> getD20OffenceCodes();
    
    ArrayList<String> getD20OffenceCodeReasonTypes();
    
    ArrayList<String> getD20OffenceCodeReasons();

    boolean isD20OffenceCodeExists(String name);

    public XhbD20OffenceCodesBasicValue getD20OffenceCode(int offenceCodeId);
    
}