package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * Common superclass for public display data classes
 * 
 * @author pznwc5
 */
public abstract class AbstractValue implements Serializable {
	
	static final long serialVersionUID = 4294442881965374830L;
	
    /**
     * Date formatter
     */
    protected static DateFormat dateFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

    protected static final String IS_FLOATING = "1";

    /**
     * Implements pretty print
     * 
     * @return Pretty print string
     * 
     * @throws CSUnrecoverableException
     *             DOCUMENT ME!
     */
    public String toString() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            Arrays.sort(pds, new Comparator() {
                public int compare(Object o1, Object o2) {
                    PropertyDescriptor pd1 = (PropertyDescriptor) o1;
                    PropertyDescriptor pd2 = (PropertyDescriptor) o2;

                    return pd1.getName().compareTo(pd2.getName());
                }
            });

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < pds.length; i++) {
                String propertyName = pds[i].getName();
                if ("class".equals(propertyName))
                    continue;
                Method readMethod = pds[i].getReadMethod();

                if (readMethod != null) {
                    Object value = pds[i].getReadMethod().invoke(this, new Object[] {});
                    if (value == null)
                        continue;
                    if (value.getClass().isArray()) {
                        sb.append(propertyName + "=" + Arrays.asList((Object[]) value) + ";");
                    } else {
                        sb.append(propertyName + "=" + value + ";");
                    }
                }
            }

            return sb.toString();
        } catch (Exception e) {
            throw new CSUnrecoverableException(e);
        }
    }

    public abstract boolean hasInformationForDisplay();

}
