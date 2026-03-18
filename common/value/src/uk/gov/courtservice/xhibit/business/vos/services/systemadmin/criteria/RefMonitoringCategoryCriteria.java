package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * 'RefMonitoringCategory' search criteria.
 */
public class RefMonitoringCategoryCriteria extends AbstractSearchCriteria implements Obsoletable {
    private static final String TABLE_NAME = "RefMonitoringCategory";

    protected interface AttributeNames {
        public static final String REF_MONITORING_CATEGORY_ID = "refMonitoringCategoryId";

        public static final String MONITORING_CATEGORY_CODE = "monitoringCategoryCode";

        public static final String MONITORING_CATEGORY_NAME = "monitoringCategoryName";

    }

    public RefMonitoringCategoryCriteria(){
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getRefMonitoringCategoryId() {
        return this.getAttribute(AttributeNames.REF_MONITORING_CATEGORY_ID);
    }

    public String getMonitoringCategoryCode(){
        return this.getAttribute(AttributeNames.MONITORING_CATEGORY_CODE);
    }

    public String getMonitoringCategoryname(){
        return this.getAttribute(AttributeNames.MONITORING_CATEGORY_NAME);
    }


    public void setRefMonitoringCategoryId(String newValue) {
        this.setAttribute(AttributeNames.REF_MONITORING_CATEGORY_ID, newValue);
    }

    public void setMonitoringCategoryCode(String newValue) {
        this.setAttribute(AttributeNames.MONITORING_CATEGORY_CODE, newValue);
    }

    public void setMonitoringCategoryname(String newValue) {
        this.setAttribute(AttributeNames.MONITORING_CATEGORY_NAME, newValue);
    }

  
    /**
     * Returns the ordered list of arguments for ref system code criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getRefMonitoringCategoryId(), getMonitoringCategoryCode(), getMonitoringCategoryname()};
    }
}