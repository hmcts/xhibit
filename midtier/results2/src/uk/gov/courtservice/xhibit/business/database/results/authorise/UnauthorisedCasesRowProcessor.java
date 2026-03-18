package uk.gov.courtservice.xhibit.business.database.results.authorise;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseValue;

/**
 * <p>
 * Title: UnauthorisedCasesRowProcessor
 * </p>
 * <p>
 * Description: Class to process the Unauthorised Case data that will be 
 * to display a list of unauthorised cases 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */
public class UnauthorisedCasesRowProcessor extends AbstractRowProcessor{
    private final ArrayList<UnauthorisedCaseValue> ucList = new ArrayList<UnauthorisedCaseValue>();
    
    public UnauthorisedCaseValue[] getUnauthorisedCases(){
        return ucList.toArray(new UnauthorisedCaseValue[ucList.size()]);
    }
    
    public ArrayList<UnauthorisedCaseValue> getUnauthorisedCaseList(){
        return ucList;
    }
    
    public void processRow(Row row){
        UnauthorisedCaseValue cv = new UnauthorisedCaseValue();
        
        //populate the object
        cv.setCase_id(row.getInteger("case_id"));
        
        //add vo to list
        ucList.add(cv);
    }
    
}
