package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefListingDataBasicValue
 * </p>
 * <p>
 * Description: RefListingDataBasicValue is intended to represent case entities as stored
 * in the RefListingData table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */

public class RefListingDataBasicValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    public static interface DataType {
        public static final String LIST_TYPE = "LIST_TYPE";
        public static final String NOTE_CLASSIFICATION = "NOTE_CLASSIFICATION";
        public static final String NOTE_TYPE = "NOTE_TYPE";
        public static final String PREDEFINED_LIST_NOTE = "PREDEFINED_LIST_NOTE";        
    }     

    public static interface DataValue {
    	// Valid Values for NOTE_TYPE
    	public static final String CASE_NOTE = "CN";
    	public static final String DEFAULT_CASE_NOTE = "DCN"; 
    	public static final String GENERAL_DIARY_NOTE = "GDN";
    	public static final String HIGHLIGHT_NOTE = "HN";
    	public static final String INTERPRETER_NOTE = "IN";  
    }
    
    public static interface ShortName {
    	// Valid Values for NOTE_TYPE
    	public static final String CASE_NOTE = "C";
    	public static final String DEFAULT_CASE_NOTE = "L"; 
    	public static final String GENERAL_DIARY_NOTE = "D";
    	public static final String HIGHLIGHT_NOTE = "H";
    	public static final String INTERPRETER_NOTE = "I";  
    }
    
    private Integer refListingDataId;
	private String refDataType;
	private String refDataValue;
	private String obsInd;
	
	public RefListingDataBasicValue() {
        super();
    }
    
    public RefListingDataBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
    /**
     * @return java.lang.Integer
     */
	public Integer getRefListingDataId() {
		return refListingDataId;
	}

	public void setRefListingDataId(Integer refListingDataId) {
		this.refListingDataId = refListingDataId;
	}

	public String getRefDataType() {
		return refDataType;
	}

	public void setRefDataType(String refDataType) {
		this.refDataType = refDataType;
	}

	public String getRefDataValue() {
		return refDataValue;
	}

	public void setRefDataValue(String refDataValue) {
		this.refDataValue = refDataValue;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}	
}
