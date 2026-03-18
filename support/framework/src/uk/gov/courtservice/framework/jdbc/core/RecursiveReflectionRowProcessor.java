package uk.gov.courtservice.framework.jdbc.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.columneditor.ColumnExtractionStrategy;
import uk.gov.courtservice.framework.jdbc.exception.PropertyExtractionException;
import weblogic.utils.StringUtils;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Processes result sets to generate Java bean instances. The class
 * can be driven by a bindings file that maps bean properties to resultset
 * columns. If the mapping is not specified the class assumes the bean property
 * name is same as the resultset column name.
 * It can create child instances of the bean instance by passing in a RecursiveRowProcessorConfiguration
 * object <font color="red">This class is
 * not thread safe</font>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author David Burden
 * @version 1.0
 */
public class RecursiveReflectionRowProcessor extends ReflectionRowProcessor {

    //This contains any Row Processors that have already been created for children of this Row level
    private HashMap<Object, RecursiveReflectionRowProcessor> groupedChildRowProcessors = new HashMap<Object, RecursiveReflectionRowProcessor>();
    
    //Contains the results of the query grouped by the value of the groupByFieldName
    private LinkedHashMap<Object, Object> groupedResults = new LinkedHashMap<Object, Object>();

	private HashMap<String, RecursiveRowProcessorConfiguration> childRowProcessorConfigurations = new HashMap<String, RecursiveRowProcessorConfiguration>();
	
	private String groupByFieldName;
    
    /**
     * Constructor initializes the target object
     * 
     * @param targetClass
     */
    public RecursiveReflectionRowProcessor(Class clazz, List<RecursiveRowProcessorConfiguration> groupingConfiguration) {
    	super(clazz);
    	if(groupingConfiguration != null){
    		for(RecursiveRowProcessorConfiguration gc : groupingConfiguration){
        		this.childRowProcessorConfigurations.put(gc.getChildFieldName(), gc);
        		this.groupByFieldName = gc.getGroupingFieldName();
        	}	
    	}
    }
    
    /**
     * Constructor initializes the target object
     * 
     * @param targetClass
     */
    public RecursiveReflectionRowProcessor(Class clazz, RecursiveRowProcessorConfiguration groupingConfiguration) {
    	super(clazz);
    	if(groupingConfiguration != null){
    		this.childRowProcessorConfigurations.put(groupingConfiguration.getChildFieldName(), groupingConfiguration);
    		this.groupByFieldName = groupingConfiguration.getGroupingFieldName();
    	}
    }


    /**
     * Row processor implementation
     * 
     * @param row
     */
    public void processRow(Row row) {

        String property = null;
        PropertyMap propertyMap = null;

        try {

            // Pre-processing
            preProcessRow(row);
            
            //If this object has children, find the primary key so we can group them by it
            String primaryKey = null;
            if(childRowProcessorConfigurations.size() > 0)
        	{
            	//Find the value of the Primary Key (the field that we will be grouping by)
            	PropertyMap primaryKeyProperty = (PropertyMap) props.get(groupByFieldName);
                ColumnExtractionStrategy strategy = ColumnExtractionStrategy.getStrategy(primaryKeyProperty.getType());
                if(strategy.getValue(row, primaryKeyProperty.getColName()) != null){
                    primaryKey = strategy.getValue(row, primaryKeyProperty.getColName()).toString();
                }
                
                //If we have already seen a row with this primary key value, retrieve the object created from the results
            	if(groupedResults.containsKey(primaryKey)){
            		target = groupedResults.get(primaryKey);
            	}
            	else{
            		// Otherwise instantiate a new target object
                    target = getTargetClass().newInstance();
            	}
        	}
            else{
            	// Instantiate the target object
                target = getTargetClass().newInstance();
            }
            
            // Get the iterator to the properties
            Iterator it = props.keySet().iterator();

            while (it.hasNext()) {
            		
                property = (String) it.next();
                // Get the property map
                propertyMap = (PropertyMap) props.get(property);
                String colName = propertyMap.getColName();
                
                //If the property is a collection then we know that we need to create a child Row Processor to populate it.
                if(childRowProcessorConfigurations.containsKey(property))  {
                	RecursiveReflectionRowProcessor childProcessor;
                	//If we have already created a childProcessor for the children of this target
                	if(groupedChildRowProcessors.containsKey(getUniqueClassFieldString(target, property))){
                		 childProcessor = groupedChildRowProcessors.get(getUniqueClassFieldString(target, property));
                	}
                	else{
                		RecursiveRowProcessorConfiguration gc = childRowProcessorConfigurations.get(property);
                		if(gc.getChildClassType() != null){
                			//Otherwise we have to rely on the type being passed in.
                			childProcessor = new RecursiveReflectionRowProcessor(gc.getChildClassType(), gc.getChildRowProcessorConfiguration());
                		}
                		else if(getTargetClass().getDeclaredField(property).getGenericType() instanceof ParameterizedType){
                			//If the collection is Parameterised, we can derive the type of the contents.
                			ParameterizedType collectionType = (ParameterizedType) getTargetClass().getDeclaredField(property).getGenericType();
                			Type collectionContentType = collectionType.getActualTypeArguments()[0];
                			childProcessor = new RecursiveReflectionRowProcessor((Class) collectionContentType, gc.getChildRowProcessorConfiguration());
                		}
                		else{
                			 String msg = propertyMap + "; Unable to derive the Type of " + property + ". Populate the childClassType to explicitly specify this.";
                	            throw new PropertyExtractionException(msg);
                		}
                		
                		childProcessor.registerDefaultBindings();
                		groupedChildRowProcessors.put(getUniqueClassFieldString(target, property), childProcessor);
                	}
                	childProcessor.processRow(row);
                	Object args[] = new Object[] { childProcessor.getResults() };
                	propertyMap.getWriteMethod().invoke(target, args);
                	
                }
                else{
	                Class type = propertyMap.getType();
	                ColumnExtractionStrategy strategy = ColumnExtractionStrategy.getStrategy(type);
	
	                Object args[] = new Object[] { strategy.getValue(row, colName) };
	
	                propertyMap.getWriteMethod().invoke(target, args);
                }
            }
         // Add the target to results
            if(primaryKey != null && !StringUtils.isEmptyString(primaryKey)){
            	groupedResults.put(primaryKey, target);	
            }
            else{
            	groupedResults.put(target, target);
            }
            
            // Post-processing
            postProcessRow(row);

        } catch (IllegalAccessException ex) {
            String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
        } catch (InstantiationException ex) {
            String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
        } catch (InvocationTargetException ex) {
            String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
        } catch (SecurityException ex) {
        	String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
		} catch (NoSuchFieldException ex) {
			String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
		}
    }
    
    private String getUniqueClassFieldString(Object clazz, String fieldName)
    {
    	return target.hashCode() + "@" + fieldName;
    }


    /**
     * Returns the current results
     * 
     * @return
     */
    public List getResults() {
    	return new ArrayList(this.groupedResults.values());
    }
}