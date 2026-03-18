package uk.gov.courtservice.xhibit.business.database.results.authorise;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;

/**
 * <p>
 * Title: AuthoriseCheckRowProcessor
 * </p>
 * <p>
 * Description: Class to process the raw pre-authorise check data
 * returned from the database.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class AuthoriseCheckRowProcessor extends AbstractRowProcessor {
    
    private final ArrayList<AuthoriseWarning> warningsList = 
        new ArrayList<AuthoriseWarning>();
    
    public AuthoriseWarning[] getAuthoriseCheck(){
        return warningsList.toArray(new AuthoriseWarning[warningsList.size()]);
    }
    
    public ArrayList<AuthoriseWarning> getAuthoriseCheckList(){
        return warningsList;
    }
    
    public void processRow(Row row){
        AuthoriseWarning acv = new AuthoriseWarning();
        
        acv.setDefendantId(row.getInteger("defendant_id"));
        acv.setFirstName(row.getString("first_name"));
        acv.setSurname(row.getString("surname"));
        acv.setDisposalCode(row.getString("disposal_code"));
        acv.setDisposalResultDate(row.getDate("disposal_result_date"));
        acv.setHearingEndDate(row.getDate("hearing_end_date"));
        
        warningsList.add(acv);
    }
    
}
