package uk.gov.courtservice.framework.jdbc.core;

/**
 * Interface to allow value objects to be responsible for populating themselves
 * with data from the passed in <code>Row</code> values.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public interface RowPopulatorInterface {
    /**
     * Method called when a new object has been created and all of the variables
     * are required to be set.
     * 
     * @param row
     *            The <code>Row</code> from which to acquire the values.
     */
    public void populate(Row row);

    /**
     * Method called to determine if the row passed in represents mainly
     * duplicate data to that of the current row.
     * 
     * @param row
     *            The <code>Row</code> from which to acquire the values.
     */
    public boolean isDuplicateRow(Row row);

    /**
     * Method called to add the details required even if the row is duplicated.
     * 
     * @param row
     *            The <code>Row</code> from which to acquire the values.
     */
    public void addRow(Row row);
}
