package uk.gov.courtservice.xhibit.courtlog.helpers.category;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDescBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDescBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * @author pznwc5
 * 
 * Helper class for court log categories
 */
public class CategoryHelper {
    /**
     * Gets court log categories for an event
     * 
     * @param eventId
     *            Event id
     * @return Categories
     */
    public static XhbCourtLogCategoryDescBasicValue[] getCategoryDescriptionsByEventType(Integer eventType) {
        return XhbCourtLogCategoryDescBeanHelper2.findByEventTypeValue(eventType);
    }

    /**
     * Gets the category descriptions for an event
     * 
     * @param eventType
     *            Event type
     * @return Categories
     */
    public static String[] getDescriptions(CourtLogCRUDValue crud) {
        XhbCourtLogCategoryDescBasicValue[] vals = getCategoryDescriptionsByEventType(crud.getEventType());
        String categories[] = new String[vals.length];
        for (int i = 0; i < vals.length; i++) {
            categories[i] = vals[i].getCategoryDescription();
        }
        return categories;
    }
}
