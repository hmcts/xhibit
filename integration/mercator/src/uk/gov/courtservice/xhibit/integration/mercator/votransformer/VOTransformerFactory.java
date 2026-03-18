package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

// JDK
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/*
 *
 * <p>Title: VOTransformerFactory</p>
 * <p>Description: This class creates VOTransformers according the logicalMapName</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Abdul Rahim Hussain, William Fardell
 * @version 1.0
 */

public class VOTransformerFactory {
    private static final Logger log = CSServices.getLogger(VOTransformerFactory.class);

    private static final VOTransformerFactory instance = new VOTransformerFactory();

    public static VOTransformerFactory getInstance() {
        log.debug("VOTransformerFactory getInstance called.");
        return instance;
    }

    private final ResourceBundle voTransformerBundle;

    private VOTransformerFactory() {
        log.debug("VOTransformerFactory called.");
        voTransformerBundle = ResourceBundle
                .getBundle("uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformerBundle");
    }

    /**
     * Lookup the transformer generating a bundle key from the class
     * 
     * @param clazz
     *            the class to look up the transformer for
     * @return an instance of the transformer
     */
    public VOTransformer createVOTransformer(Class clazz) throws TransformationException {
        return createVOTransformer(getVOName(clazz));
    }

    /**
     * Get the voName bundle key for the class.
     * 
     * <p>
     * Examples: (name: voname)
     * <ul>
     * <li>"Test": "Test"</li>
     * <li>"java.lang.Object": "Object"</li>
     * <li>"int": "int"</li>
     * <li>"[LTest;": "Test[]"</li>
     * <li>"[Ljava.lang.Object;": "Object[]"</li>
     * <li>"[I": "int[]"</li>
     * <li>"[[LTest;": "Test[][]"</li>
     * <li>"[[Ljava.lang.Object;": "Object[][]"</li>
     * <li>"[[I": "int[][]"</li>
     * <ul>
     * </p>
     * 
     * @param clazz
     *            the class to get the voName from
     * @return the voName
     */
    public String getVOName(Class clazz) {
        String name = clazz.getName();
        int dimensionIndex = name.lastIndexOf('[');

        if (dimensionIndex != -1) {
            // An array
            int semicolonIndex = name.lastIndexOf(';');
            if (semicolonIndex != -1) {
                // complex type (class)
                int seperatorIndex = name.lastIndexOf('.');
                if (seperatorIndex != -1) {
                    return name.substring(seperatorIndex + 1, semicolonIndex) + getDimensionSuffix(dimensionIndex + 1);
                } else {
                    return name.substring(dimensionIndex + 2, semicolonIndex) + getDimensionSuffix(dimensionIndex + 1); // + 2
                    // to
                    // remove
                    // L
                }
            } else {
                // primitve type
                return getPrimitveName(name.substring(dimensionIndex + 1)) + getDimensionSuffix(dimensionIndex + 1);
            }
        } else {
            // Not an array
            int seperatorIndex = name.lastIndexOf('.');
            if (seperatorIndex != -1) {
                return name.substring(seperatorIndex + 1);
            } else {
                return name;
            }
        }
    }

    /**
     * Get the dimension suffix to the voName
     * 
     * @param count
     *            the number of dimensions
     * @return the dimension suffix
     */
    private static String getDimensionSuffix(int count) {
        // Optimise common case
        if (count == 1) {
            return "[]";
        } else {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < count; i++) {
                buffer.append("[]");
            }
            return buffer.toString();
        }
    }

    /**
     * Maps internal java primitve names to names
     * <ul>
     * <li>B byte</li>
     * <li>C char</li>
     * <li>D double</li>
     * <li>F float</li>
     * <li>I int</li>
     * <li>J long</li>
     * <li>S short</li>
     * <li>Z boolean</li>
     * </ul>
     * 
     * @param the
     *            name of the primitive type
     * @return the name
     * @throws IllegalArgumentException
     *             if not a primitive name
     */
    private static String getPrimitveName(String name) throws IllegalArgumentException {
        if ("B".equals(name)) {
            return "byte";
        } else if ("C".equals(name)) {
            return "char";
        } else if ("D".equals(name)) {
            return "double";
        } else if ("F".equals(name)) {
            return "float";
        } else if ("I".equals(name)) {
            return "int";
        } else if ("J".equals(name)) {
            return "long";
        } else if ("S".equals(name)) {
            return "short";
        } else if ("Z".equals(name)) {
            return "boolean";
        } else {
            throw new IllegalArgumentException("name: " + name);
        }
    }

    /**
     * Gets the transformer from the bundle, if its an instance return it
     * (transformer is stateless), if its a class instantiate an instance
     * (transformer is stateful)
     * 
     * @param voName
     *            the name of the vo to create the transformer for
     * @return the VOTransformer for the voName
     * @throws TransformationException
     *             if an error occures.
     * @todo Exception Message Keys
     */
    public VOTransformer createVOTransformer(String voName) throws TransformationException {
        try {
            Object resource = voTransformerBundle.getObject(voName);
            try {
                VOTransformer vot = (VOTransformer) ((resource instanceof VOTransformer) ? resource
                        : ((Class) resource).newInstance());
                if (log.isDebugEnabled()) {
                    log.debug("Found transformer " + vot.getClass().getName() + " for vo " + voName + ".");
                }
                return vot;
            } catch (NullPointerException npe) {
                throw new TransformationException(null, "Resource " + voName
                        + " is null which is not a valid VOTransformer or VOTransformer class.", npe);
            } catch (ClassCastException cce) {
                throw new TransformationException(null, "Resource " + voName + " is a " + resource.getClass()
                        + " which is not a valid VOTransformer or VOTransformer class.", cce);
            } catch (InstantiationException ie) {
                throw new TransformationException(null, "Resource " + voName + " is a " + resource.getClass()
                        + " which is not a valid VOTransformer or VOTransformer class.", ie);
            } catch (SecurityException se) {
                throw new TransformationException(null, "Resource " + voName + " is a " + resource.getClass()
                        + " which is not a valid VOTransformer or VOTransformer class.", se);
            } catch (IllegalAccessException eae) {
                throw new TransformationException(null, "Resource " + voName + " is a " + resource.getClass()
                        + " which is not a valid VOTransformer or VOTransformer class.", eae);
            }
        } catch (MissingResourceException mre) {
            throw new TransformationException(null, "Could not find VOTransformer " + voName + ".", mre);
        }
    }
}