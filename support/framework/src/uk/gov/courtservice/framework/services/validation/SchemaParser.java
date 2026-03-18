package uk.gov.courtservice.framework.services.validation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ConfigServices;

/**
 * <p>
 * Title: SchemaParser
 * </p>
 * <p>
 * Description: Parses the corresponding schema and creates a collection of
 * <code>Constraint</code> objects as described in the schema. If no schema is
 * found the assumption is made that there are no constraints. In this case a
 * warning is logged, but no Exception thrown. A
 * <code>CSConfigurationException</code> will be thrown if required vos are
 * absent from the schema i.e. the schema itself is invalid.
 * </p>
 * <p>
 * This class is only intended to be used with the <code>Validator</code>
 * class of this package.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

class SchemaParser {
    private String schemaLoc;

    private Logger log = CSServices.getLogger(SchemaParser.class);

    private Map constraints;

    private static String VALIDATION_PROPERTIES = "validationschemas";

    private String schemaName;

    /**
     * 
     * @param schemaName
     *            ref name of schema in validation.properties file
     */
    SchemaParser(String schemaName) {
        if (log.isDebugEnabled()) {
            log.debug("START: SchemaParser( String schemaName )");
            log.debug("ATTRIBUTE: schemaName-  " + schemaName);
        }
        this.schemaName = schemaName;
        ConfigServices cs = CSServices.getConfigServices();
        Properties prop = cs.getProperties(VALIDATION_PROPERTIES);
        schemaLoc = prop.getProperty(schemaName);
        constraints = new HashMap();
        parseSchema();
        if (log.isDebugEnabled())
            log.debug("END: SchemaParser( String schemaName )");
    }

    private void parseSchema() {
        if (log.isDebugEnabled())
            log.debug("START: parseSchema()");

        InputStream is = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            try {
                URL url = new URL(schemaLoc);
                is = url.openStream();
            } catch (MalformedURLException ex) { // if can't read as url
                // then try a file input
                // stream
                // throws an IOException if that fails as well.
                if (log.isDebugEnabled())
                    log.debug("Failed to get schema as url: schemaloc = " + schemaLoc
                            + " Attempting to open as a FileInputStream");
                is = new FileInputStream(schemaLoc);
            }

            Document document = documentBuilder.parse(is);
            NodeList attlist = document.getElementsByTagName("attribute");

            for (int i = 0; i < attlist.getLength(); i++) {
                Element attribute = (Element) attlist.item(i);
                createConstraint(attribute);
            }

        } catch (ParserConfigurationException e) {
            throw new CSConfigurationException("Unable to create a document builder for validation. ", e);
        } catch (IOException e) {
            log.warn(schemaLoc + " not found. " + e
                    + " Continue with assumption that absence of schema means that there are no constraints");
        } catch (SAXException e) {
            log.warn(e);
        } catch (NullPointerException e) {
            log.warn(schemaLoc + " not found. " + e
                    + " Continue with assumption that absence of schema means that there are no constraints");
        }
        if (log.isDebugEnabled())
            log.debug("END: parseSchema()");
    }

    private void createConstraint(Element attribute) {
        if (log.isDebugEnabled())
            log.debug("START: createContstraint(Element)");
        String name = attribute.getAttribute("name");

        if (name != null && name.length() < 1) {
            throw new CSConfigurationException("Attribute node: \"" + attribute.getNodeName() + "\" in \"" + schemaName
                    + "\" has no \"name\" value.");
        }

        Constraint constraint = new Constraint(name);

        // get the data type
        NodeList list = attribute.getElementsByTagName("simpleType");
        Element simpleType = ((Element) list.item(0));

        String dataType = simpleType.getAttribute("baseType");
        constraint.setDataType(DataConverter.getInternalDataType(dataType));

        // get minExclusive
        NodeList minXlst = simpleType.getElementsByTagName("minExclusive");
        if (minXlst.getLength() > 0) {
            Double value = new Double(((Element) minXlst.item(0)).getAttribute("value"));
            constraint.setMinExclusive(value.doubleValue());
        }

        NodeList minIncLst = simpleType.getElementsByTagName("minInclusive");
        if (minIncLst.getLength() > 0) {
            Double value = new Double(((Element) minIncLst.item(0)).getAttribute("value"));
            constraint.setMinInclusive(value.doubleValue());
        }

        NodeList maxXlst = simpleType.getElementsByTagName("maxExclusive");
        if (maxXlst.getLength() > 0) {
            Double value = new Double(((Element) maxXlst.item(0)).getAttribute("value"));
            constraint.setMaxExclusive(value.doubleValue());
        }

        NodeList maxIncLst = simpleType.getElementsByTagName("maxInclusive");
        if (maxIncLst.getLength() > 0) {
            Double value = new Double(((Element) maxIncLst.item(0)).getAttribute("value"));
            constraint.setMaxInclusive(value.doubleValue());
        }

        NodeList minLngth = simpleType.getElementsByTagName("minLength");
        if (minLngth.getLength() > 0) {
            Integer value = new Integer(((Element) minLngth.item(0)).getAttribute("value"));
            constraint.setMinLength(value);
        }

        NodeList maxLngth = simpleType.getElementsByTagName("maxLength");
        if (maxLngth.getLength() > 0) {
            Integer value = new Integer(((Element) maxLngth.item(0)).getAttribute("value"));
            constraint.setMaxLength(value);
        }

        NodeList allowLst = simpleType.getElementsByTagName("enumeration");
        for (int i = 0; i < allowLst.getLength(); i++) {
            String value = ((Element) allowLst.item(i)).getAttribute("value");
            constraint.addAllowedValue(value);
        }

        constraints.put(name, constraint);

        if (log.isDebugEnabled())
            log.debug("END: createContstraint(Element)");
    }

    /**
     * 
     * @return collection of <code>Constraint<code>s
     */
    public Map getContraints() {
        return constraints;
    }

    /**
     * 
     * @param constraintName
     *            name of attribute
     * @return <code>Constraint</code> object or null if not found
     */
    public Constraint getConstraint(String constraintName) {
        Object o = constraints.get(constraintName);

        if (o != null) {
            return (Constraint) o;
        } else {
            return null;
        }
    }
}