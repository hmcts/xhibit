package uk.gov.courtservice.xhibit.courtlog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.SubscriptionValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * @author pznwc5
 * @version $Revision: 1.29 $
 */
public class OperationContext implements Cloneable {
    /** Original CRUD value that is passed */
    protected final CourtLogCRUDValue crudVal;

    /** Map of attributes */
    private final Map attributes = new HashMap();

    /** New view values */
    private CourtLogViewValue[] newViewValues;

    /** New Subscription values */
    private CourtLogSubscriptionValue[] newSubValues;

    /** Categories */
    private String[] categories;

    /**
     * Initializes the context
     * 
     * @param originalCrudVal
     *            Original CRUD value sent by the client
     * @throws CourtLogBusinessException
     */
    protected OperationContext(CourtLogCRUDValue crudVal) {
        this.crudVal = crudVal;
    }

    /**
     * Creates a new instance
     * 
     * @param crudVal
     * @return
     */
    public static final OperationContext newInstance(CourtLogCRUDValue crudVal) {
        if (crudVal.getLogEntryId() == null) {
            return new OperationContext(crudVal);
        }

        return new UpdateDeleteContext(crudVal);
    }

    /**
     * Stores a context attribute
     * 
     * @param key
     * @param value
     */
    public void putAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * Retrieves a context attribute
     * 
     * @param key
     * @return
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Gets the original crud value
     * 
     * @return
     */
    public CourtLogCRUDValue getCrudValue() {
        return crudVal;
    }

    /**
     * Gets the original view value
     * 
     * @return
     */
    public CourtLogViewValue getOriginalViewValue() {
        throw new UnsupportedOperationException("Not supported for create");
    }

    /**
     * Gets the original view value
     * 
     * @return
     */
    public XhbCourtLogEntryBasicValue getOriginalBasicValue() {
        throw new UnsupportedOperationException("Not supported for create");
    }

    /**
     * Returns the new view values that have benn updated/created
     * 
     * @return
     */
    public CourtLogViewValue[] getNewViewValues() {
        return newViewValues;
    }

    /**
     * Sets the new view values that have benn updated/created
     * 
     * @return
     */
    public void setNewViewValues(CourtLogViewValue[] newViewValues) {
        this.newViewValues = newViewValues;
        newSubValues = new CourtLogSubscriptionValue[newViewValues.length];
        for (int i = 0; i < newViewValues.length; i++) {
            newSubValues[i] = SubscriptionValueAssembler.getSubsciptionValue(newViewValues[i]);
        }
    }

    /**
     * Returns the new subscription values that have benn updated/created
     * 
     * @return
     */
    public CourtLogSubscriptionValue[] getNewSubscriptionValues() {
        return newSubValues;
    }

    /**
     * @param categories
     */
    public void setCategories(String[] categories) {
        this.categories = categories;
        Arrays.sort(this.categories);
    }

    /**
     * Checks whether the current event belongs to the category
     * 
     * @param category
     * @return
     */
    public boolean hasCategory(String category) {
        return Arrays.binarySearch(categories, category) > 0;
    }

    /**
     * Returns the scheduled hearing
     * 
     * @return
     */
    public Integer getScheduledHearingId() {
        return crudVal.getScheduledHearingId();
    }

    /**
     * Clone suppoet
     * 
     * @return
     */
    public OperationContext deepCopy() {
        try {
            return (OperationContext) clone();
        } catch (CloneNotSupportedException ex) {
            // This should never happen
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Context for update and delete
     * 
     * @author pznwc5
     */
    protected static class UpdateDeleteContext extends OperationContext {
        /** Original view value */
        private CourtLogViewValue originalViewVal;

        /** Original basic value */
        private XhbCourtLogEntryBasicValue originalBasicVal;

        /**
         * Initializes the context
         * 
         * @param originalCrudVal
         *            Original CRUD value sent by the client
         * @throws CourtLogBusinessException
         */
        protected UpdateDeleteContext(CourtLogCRUDValue crudVal) {
            super(crudVal);
        }

        /**
         * Populates the original value
         * 
         * @param originalCrudVal
         *            Original CRUD value sent by the client
         */
        private void populateOriginalValues() {
            final Long logEntryId = crudVal.getLogEntryId();

            XhbCourtLogEntry courtLogEntry = EntityHelper.getXhbCourtLogEntry(logEntryId);

            ViewValueAssembler viewValueAssembler = ViewValueAssembler.newInstance(crudVal);
            this.originalViewVal = viewValueAssembler.assembleViewValue(courtLogEntry.getData());
            this.originalBasicVal = courtLogEntry.getData();
        }

        /**
         * Gets the original view value
         * 
         * @return
         */
        public CourtLogViewValue getOriginalViewValue() {
            if (originalViewVal == null) {
                populateOriginalValues();
            }

            return originalViewVal;
        }

        /**
         * Gets the original view value
         * 
         * @return
         */
        public XhbCourtLogEntryBasicValue getOriginalBasicValue() {
            if (originalBasicVal == null) {
                populateOriginalValues();
            }

            return originalBasicVal;
        }
    }
}
