package uk.gov.courtservice.xhibit.client.counselfacilities;


public class FindInstructedAdvocateTableRowModel extends FindLegalRepresentativeTableRowModel {

    private String available;
    
    private Integer crestPostNumber;
    
    public FindInstructedAdvocateTableRowModel() {
        // empty
    }
    
    public FindInstructedAdvocateTableRowModel(FindInstructedAdvocateTableRowModel trm) {
        super(trm);
        this.setAvailable(trm.getAvailable());
        this.setCrestPostNumber(trm.getCrestPostNumber());
    }
    
    public boolean equals(Object o) {

        if (o == this) 
            return true;
        
        if (o instanceof FindInstructedAdvocateTableRowModel) {
            FindInstructedAdvocateTableRowModel obj = 
                (FindInstructedAdvocateTableRowModel)o;
            
            if (obj.getLegalRepId() != null
                    && this.getLegalRepId() != null
                    && obj.getLegalRepId().equals(this.getLegalRepId())) {
                return true;
            }
        }
        
        return false;
    }
    
    public int hashCode() {
        // if equals is defined then hashCode must also be.
        if (this.getLegalRepId() == null) {
            return 0;
        } else {
            return this.getLegalRepId().hashCode();
        } 
    }
    
    public void setAvailable(String available) {
        this.available = available;
    }
    
    public String getAvailable() {
        return available;
    }
    
    public void setCrestPostNumber(Integer crestPostNumber) {
        this.crestPostNumber = crestPostNumber;
    }
    
    public Integer getCrestPostNumber() {
        return crestPostNumber;
    }
    
    public boolean isAvailable() {
        return available == null || available.equals("");
    }
    
    public boolean isWithdrawn() {
        return available != null && available.equals(InstructedAdvocateHelper.WITHDRAWN_FLAG);
    }
}
