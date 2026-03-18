package uk.gov.courtservice.xhibit.client.search;

import java.awt.Dimension;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.util.ReflectionHelper;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT2 XHIBITSearchResults
 * </p>
 * <p>
 * Description: This is a class to be subclassed by implementors of a specific
 * search (e.g. search for jugde). this class captures the details of what and
 * how to display the results retrieved from the db. (when no results - logic in
 * XHIBITSearchCriteriaPanel)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version $Revision: 1.3 $
 */
public abstract class XHIBITSearchResults extends XHIBITSearchStep // can
// to
// the
// extend?
{
    public static int OFF = -1; // to avoid table sorting

    public static int ASC = 0; // for ascending table sorting

    public static int DESC = 1; // for descening table sorting

    private final String FIELD_PATH_DELIMITER = ".";

    private CSAbstractValue resultsValueObjectClass = null;

    protected Vector resultAttributeKeys = new Vector();

    protected Hashtable resultsAttributeFields = new Hashtable();

    protected Hashtable resultsAttributeLabels = new Hashtable();

    protected Hashtable resultsAttributeLongValues = new Hashtable();

    protected Hashtable resultsAttributeRenderers = new Hashtable();

    public XHIBITSearchResults() {
        super();
    }

    public abstract void setResultFields();

    public void setResultsValueObjectClass(CSAbstractValue c) {
        resultsValueObjectClass = c;
    }

    public CSAbstractValue getResultsValueObjectClass() {
        return this.resultsValueObjectClass;
    }

    protected void addResultsField(String fieldName, String resourceKey, TableCellRenderer cellRenderer) {
        // the JComponent must become tablecellrendered?
        if (this.resultsValueObjectClass == null) {
            log.error("Exception whilst trying to add results field with name '" + fieldName + "'.");
            log.error("Must do setSearchCriteria(AbstractSearchCriteria isearchCriteria) in constructor.");
        } else {
            try {
                log.debug("resultsValueObjectClass.getClass() = " + resultsValueObjectClass.getClass());
                Field field = ReflectionHelper.getFieldFromFieldPath(resultsValueObjectClass.getClass(), fieldName,
                        FIELD_PATH_DELIMITER);
                // Field field =
                // resultsValueObjectClass.getClass().getDeclaredField(fieldName);
                resultAttributeKeys.add(fieldName);
                resultsAttributeLabels.put(fieldName, XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                        resourceKey));
                resultsAttributeLongValues.put(fieldName, XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                        resourceKey.concat(".longValue")));
                resultsAttributeFields.put(fieldName, field);

                if (cellRenderer == null) {
                    this.resultsAttributeRenderers.put(fieldName, new DefaultTableCellRenderer());
                } else {
                    this.resultsAttributeRenderers.put(fieldName, cellRenderer);
                }
                log.debug("Added results field '" + fieldName + "'.");
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Exception whilst trying to add criteria with name '" + fieldName + "'.");
                log.error(e);
            }
        }
    }

    protected void addResultsField(String fieldName, String resourceKey) {
        addResultsField(fieldName, resourceKey, null);
    }

    // public String getStepTitle()
    // {
    // return new String("XHIBIT Search Results");
    // }
    // public String getStepDescription()
    // {
    // return new String("Select a results and hit ok or details");
    // }

    // override this method in your SearchXXXResults to set the minimum
    // number of selected elements
    public int getMinimumResultsSelected() {
        return 1;
    }

    // override this method in your SearchXXXResults to set the maximum
    // number of selected elements
    public int getMaximumResultsSelected() {
        return 1;
    }

    public Dimension getResultsTableDimension() {
        int width = 350;
        int height = 200;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }

    /**
     * Internal method used to load the column that requires sorting
     * 
     * @param resourceKeyBit
     * @return
     */
    protected int getColumnIndexToSort(String resourceKeyBit) {
        int sortColumnIndex = OFF;
        String sortColumnIndexStr = XHIBITConstant.getResource(XhibitBundles.XhibitSearch, resourceKeyBit);
        log.debug(resourceKeyBit + " from resourcebundle = " + sortColumnIndexStr);
        if (sortColumnIndexStr.indexOf(XHIBITConstant.resourceNotFoundStringStart) == -1) {
            if (sortColumnIndexStr.toLowerCase().trim().startsWith("off")) {
                log.debug(resourceKeyBit + " from resourcebundle =  turned off.");
            } else {
                try {
                    sortColumnIndex = Integer.parseInt(sortColumnIndexStr);
                    log.debug(resourceKeyBit + " set sort column index = " + sortColumnIndex);
                } catch (Exception e) {
                    log.error(resourceKeyBit + " ['" + sortColumnIndexStr + "'] could not be parsed to an int.");
                    log.error(e);
                }
            }
        } else {
            log.error(resourceKeyBit + " not found in " + XhibitBundles.XhibitSearch);
        }
        return sortColumnIndex;
    }

    /**
     * This method is called when the results are set in the
     * XhibitSearchResultsPanel.
     * 
     * Let your SearchResults subclass override this method, by specifying a
     * different property key in the XhibitBundles.XHIBITSearch resource bundle.
     * 
     * @return integer indicating which column to sort (-1 or OFF = no sorting)
     */
    public int getColumnIndexToSort() {
        return getColumnIndexToSort("xs.gen.ResultListCard.sortTableColumn");
    }

    /**
     * Internal method that loads the asc/desc setting for the sorting of the
     * column.
     * 
     * @param resourceKeyBit
     * @return
     */
    protected int getColumnSortModus(String resourceKeyBit) {
        int sortColumnModus = ASC;
        String sortColumnModusStr = XHIBITConstant.getResource(XhibitBundles.XhibitSearch, resourceKeyBit);
        log.debug("resourceKeyBit from resourcebundle = " + sortColumnModusStr);
        if (sortColumnModusStr.indexOf(XHIBITConstant.resourceNotFoundStringStart) == -1) {
            if (sortColumnModusStr.toLowerCase().trim().startsWith("off")) {
                log.debug("The '" + resourceKeyBit + "' is turned off.");
            } else {
                if (sortColumnModusStr.toLowerCase().trim().startsWith("asc")) {
                    sortColumnModus = ASC;
                } else {
                    if (sortColumnModusStr.toLowerCase().trim().startsWith("desc")) {
                        sortColumnModus = DESC;
                    }
                }
            }
        } else {
            log.error(resourceKeyBit + " not found in " + XhibitBundles.XhibitSearch);
        }
        return sortColumnModus;
    }

    /**
     * This method is called when the results are set in the
     * XhibitSearchResultsPanel.
     * 
     * Let your SearchResults subclass override this method, by specifying a
     * different property key in the XhibitBundles.XHIBITSearch resource bundle.
     * 
     * @return
     */
    public int getColumnSortModus() {
        return getColumnSortModus("xs.gen.sortTableColumnModus");
    }

    public Class[] getParameterTypes() {
        log.debug("getParameterTypes()");
        Class[] parameterTypes = { Collection.class };
        return parameterTypes;
    }

    public String getMethodName() {
        return "should not call this on a searchresultspec!";
    }

}