package uk.gov.courtservice.xhibit.test.business.entities.cpplist;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Ignore;

import junit.framework.TestCase;


/**
 * Test classes for passed in basicValue objects.
 * 
 * @author harrism
 *
 */
@Ignore
public abstract class AbstractTestBasicValue extends TestCase {	
	
	private List<String> fieldIgnoreList = new ArrayList<String>();
	private final Date date;
	
	public AbstractTestBasicValue(String s) throws NamingException {
		super(s);
		fieldIgnoreList.add("serialVersionUID");
		date = Calendar.getInstance().getTime();
	}	  
  
	protected void testBasicValue(Object basicValue) {		
		// Get the list of fields to set
		Field[] fields = getFieldArray(basicValue);
		
		// Set the fields
		for (int fieldNo = 0; fieldNo < fields.length; fieldNo++) {
			String fieldName = fields[fieldNo].getName();			
			if (!fieldIgnoreList.contains(fieldName)) {
				Class<?> fieldType = fields[fieldNo].getType();
				Object fieldValue = getFieldValueToSet(fieldType, fieldNo); 				
				setFieldValue(basicValue, fieldName, fieldValue);
			}
		}

		// Validate the fields
		for (int fieldNo = 0; fieldNo < fields.length; fieldNo++) {
			String fieldName = fields[fieldNo].getName();			
			if (!fieldIgnoreList.contains(fieldName)) {
				Class<?> fieldType = fields[fieldNo].getType();			
				Object expectedFieldValue = getFieldValueToSet(fieldType, fieldNo);
				Object actualFieldValue = getFieldValue(basicValue, fields[fieldNo]);			
				assertEquals("Error in field: " + fieldName, expectedFieldValue, actualFieldValue);
			}
		}
	} 
	
	private Object getFieldValue(Object basicValue, final Field field) {
		Object fieldValue = null;
		try {
			field.setAccessible(true);
			fieldValue = field.get(basicValue);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return fieldValue;
	}
	
	private Object getFieldValueToSet(Class<?> fieldType, int fieldNo) {
		if (Integer.class.isAssignableFrom(fieldType)) {
			return Integer.valueOf(fieldNo);			
		} else if (Date.class.isAssignableFrom(fieldType)) {
			return date; 
		} else if(Long.class.isAssignableFrom(fieldType)) {
		  return Long.valueOf(fieldNo);
		}
		return Integer.valueOf(fieldNo).toString();
	}
	
	private Field[] getFieldArray(Object basicValue) {		
		return basicValue.getClass().getDeclaredFields();
	}
	
    private void setFieldValue(Object basicValue, final String fieldName, final Object fieldValue) {
    	try {
    		PropertyDescriptor pd = new PropertyDescriptor(fieldName, basicValue.getClass());
    		Method writeMethod = pd.getWriteMethod();
    		writeMethod.invoke(basicValue, fieldValue);
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail();
    	}
    }
}