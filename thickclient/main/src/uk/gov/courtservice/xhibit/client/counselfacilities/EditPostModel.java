package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Vector;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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

public class EditPostModel {
    
    private XhibitApplicationController xac;
    
    private Integer postNumber;
    
    private Vector<FindInstructedAdvocateTableRowModel> instructedAdvocates =
        new Vector<FindInstructedAdvocateTableRowModel>();

    
    public EditPostModel(
            XhibitApplicationController xac,
            Integer postNumber,
            Collection<FindInstructedAdvocateTableRowModel> advocates) {
        
        this.xac = xac;
        this.postNumber = postNumber;

        for (FindInstructedAdvocateTableRowModel item : advocates) {
 
            if (item.getCrestPostNumber() != null
                    && item.getCrestPostNumber().equals(postNumber)) {
                
                // Copy the row data because the edit post dialog edits the rows
                // and we want to discard any changes if the cancel button is pressed.
                FindInstructedAdvocateTableRowModel advocate = 
                    new FindInstructedAdvocateTableRowModel(item);
                
                instructedAdvocates.add(advocate);
            }
        }
    }
    
    public XhibitApplicationController getXac() {
        return xac;
    }
    
    public void setXac(XhibitApplicationController xac) {
        this.xac = xac;
    }
    
    public Integer getPostNumber() {
        return postNumber;
    }
    
    public void setPostNumber(Integer postNumber) {
        this.postNumber = postNumber;
    }
    
    public Vector<FindInstructedAdvocateTableRowModel> getInstructedAdvocates() {
        return instructedAdvocates;
    }
    
    public void setInstructedAdvocates(Vector<FindInstructedAdvocateTableRowModel> instructedAdvocates) {
        this.instructedAdvocates = instructedAdvocates;
    }
}


