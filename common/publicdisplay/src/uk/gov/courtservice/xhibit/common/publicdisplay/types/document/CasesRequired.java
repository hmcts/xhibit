package uk.gov.courtservice.xhibit.common.publicdisplay.types.document;

import java.io.Serializable;

/**
 * <p>
 * Title: An enumeration of possible CasesRequired values.
 * </p>
 * 
 * <p>
 * Description: Possible values are simply <b>all</b> or <b>active</b>.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class CasesRequired implements Serializable {
	
	static final long serialVersionUID = -8030925848816402590L;
	
    public static final CasesRequired ALL = new CasesRequired("all");

    public static final CasesRequired ACTIVE = new CasesRequired("active");

    private final String value;

    private CasesRequired(String val) {
        this.value = val;
    }

    /**
     * Object comparison.
     * 
     * @param obj
     *            Object to compare.
     * 
     * @return true if they are semantically identical.
     */
    public boolean equals(Object obj) {
        return (obj != null) && obj instanceof CasesRequired && value.equals(((CasesRequired) obj).value);
    }

    /**
     * Returns the String representation of the instance.
     * 
     * @return the String representation of the instance.
     * 
     * @post return != null
     */
    public String toString() {
        return value;
    }
}
