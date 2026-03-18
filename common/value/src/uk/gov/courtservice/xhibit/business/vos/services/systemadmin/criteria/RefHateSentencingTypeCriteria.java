package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import static uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.Obsoletable.ATTRIBUTE_NAME_OBSIND;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria.AttributeNames;

/**
 * RefHateSentencingTypeCriteria search criteria.
 * <p>
 * System Reference Data Type.
 * </p>
 * 
 */
public class RefHateSentencingTypeCriteria extends AbstractSearchCriteria implements Obsoletable {
    private static final String TABLE_NAME = "RefHateSentencingTypeCriteria";

    protected interface AttributeNames {
        public static final String COURT_ID = "court.courtId";

        public static final String HATE_SENT_TYPE = "hateSentType";

        public static final String TITLE = "title";

        public static final String DESCRIPTION = "description";

        public static final String CJS_QUALIFIER = "cjsQualifier";

    }
  
    public RefHateSentencingTypeCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getHateSentType() {
        return this.getAttribute(AttributeNames.HATE_SENT_TYPE);
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public String getTitle() {
        return this.getAttribute(AttributeNames.TITLE);
    }

    public String getDescription() {
        return this.getAttribute(AttributeNames.DESCRIPTION);
    }

    public String getCjsQualifier() {
        return this.getAttribute(AttributeNames.CJS_QUALIFIER);
    }

    public void setHateSentType(String newValue) {
        this.setAttribute(AttributeNames.HATE_SENT_TYPE, newValue);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setTitle(String newValue) {
        this.setAttribute(AttributeNames.TITLE, newValue);
    }
	
	public void setDescription(String newValue) {
        this.setAttribute(AttributeNames.DESCRIPTION, newValue);
    }

    public void setCjsQualifier(String newValue) {
        this.setAttribute(AttributeNames.CJS_QUALIFIER, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for ref system code criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtId(), getTitle(), getDescription(), getCjsQualifier(), getHateSentType() };
    }
}