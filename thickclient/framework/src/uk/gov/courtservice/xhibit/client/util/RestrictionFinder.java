package uk.gov.courtservice.xhibit.client.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class RestrictionFinder extends XMLServicesImpl {

    public RestrictionFinder() {
    }

    public static void main(String[] args) {
        Collection c = RestrictionFinder.getRestrictingValues("aschema.xsd", "SAO-Type");
        Iterator cit = c.iterator();
        XHIBITConstant.debug("Allowed vos for element SAO-Type: ");
        while (cit.hasNext()) {
            XHIBITConstant.debug("- [" + (String) cit.next() + "]");
        }
    }

    public static Collection getRestrictingValues(String schemaFileName, String elementName) {
        Vector restrictingValues = new Vector();
        try {
            XHIBITConstant.debug("schemaFileName: </config/courtlog/schemas/" + schemaFileName + ">");
            Document xml = XMLServicesImpl.getInstance()
                    .createDocFromFile("/config/courtlog/schemas/" + schemaFileName);

            NodeList elementNodeList = xml.getElementsByTagName("xs:element");
            int noOfElementsInXML = elementNodeList.getLength();

            for (int i = 0; i < noOfElementsInXML; i++) {
                Node node = elementNodeList.item(i);

                NamedNodeMap attribs = node.getAttributes();
                Node nameAttribute = attribs.getNamedItem("name");
                if (nameAttribute != null) {
                    if (nameAttribute.getNodeValue().equals(elementName)) {
                        NodeList elementChildNodeList = node.getChildNodes();

                        for (int x = 0; x < elementChildNodeList.getLength(); x++) {
                            Node node2 = elementChildNodeList.item(x);
                            XHIBITConstant.debug("node2 " + node2.getNodeName());

                            if (node2.getNodeName().equals("xs:simpleType")) {
                                NodeList elementChildNodeList2 = node2.getChildNodes();

                                for (int xx = 0; xx < elementChildNodeList2.getLength(); xx++) {
                                    Node node3 = elementChildNodeList2.item(xx);
                                    XHIBITConstant.debug("      node3 " + node3.getNodeName());

                                    if (node3.getNodeName().equals("xs:restriction")) {
                                        NodeList elementChildNodeList3 = node3.getChildNodes();

                                        for (int xxx = 0; xxx < elementChildNodeList3.getLength(); xxx++) {
                                            Node node4 = elementChildNodeList3.item(xxx);
                                            XHIBITConstant.debug("               node4 " + node4.getNodeName());
                                            if (node4.getNodeName().equals("xs:enumeration")) {
                                                NamedNodeMap restrictionAttributes = node4.getAttributes();
                                                restrictingValues.add(restrictionAttributes.getNamedItem("value")
                                                        .getNodeValue());
                                            }
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            XHIBITConstant.handleError(e);
        }
        return (Collection) restrictingValues;
    }

}